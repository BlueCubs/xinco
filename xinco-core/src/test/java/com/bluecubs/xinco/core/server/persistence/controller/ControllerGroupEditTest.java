package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Covers Group.edit() ACE-move and membership-move branches, Group.destroy() with ACEs, and
 * Language/DataType create-with-relations (the last meaningful FK-change gaps).
 */
public class ControllerGroupEditTest extends AbstractXincoDataBaseTestCase {

  public ControllerGroupEditTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerGroupEditTest.class);
  }

  /**
   * Edit G2 with ACE from G1. Covers Group.edit() lines ~215-222: !old.contains(ace) → body runs.
   * ace's old group = G1 != G2 → inner IF body executes.
   */
  public void testGroup_editMoveAceFromOtherGroup() throws Exception {
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());

    XincoCoreGroup g1 = buildGroup(groupCtrl, "TestGrpAceSrc");
    XincoCoreGroup g2 = buildGroup(groupCtrl, "TestGrpAceDst");

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(g1.getId()));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Edit G2 adding ace from G1: ace's old group = G1 != G2 → inner IF runs
    XincoCoreGroup toEdit = groupCtrl.findXincoCoreGroup(g2.getId());
    toEdit.setXincoCoreAceList(Arrays.asList(aceCtrl.findXincoCoreAce(aceId)));
    toEdit.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    groupCtrl.edit(toEdit);

    aceCtrl.destroy(aceId);
    groupCtrl.destroy(g2.getId());
    groupCtrl.destroy(g1.getId());
  }

  /**
   * Edit G2 with membership from G1. Covers Group.edit() lines ~238-248: !old.contains(uhg) → body
   * runs. uhg's old group = G1 != G2 → inner IF body executes.
   */
  public void testGroup_editMoveMembershipFromOtherGroup() throws Exception {
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupJpaController uhgCtrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());

    XincoCoreGroup g1 = buildGroup(groupCtrl, "TestGrpMembSrc");
    XincoCoreGroup g2 = buildGroup(groupCtrl, "TestGrpMembDst");

    XincoCoreUser user = buildUser(userCtrl, "grp.memb.move");
    int userId = user.getId();

    XincoCoreUserHasXincoCoreGroupPK pk = new XincoCoreUserHasXincoCoreGroupPK(userId, g1.getId());
    XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup();
    uhg.setXincoCoreUserHasXincoCoreGroupPK(pk);
    uhg.setStatusNumber(1);
    uhg.setXincoCoreUser(userCtrl.findXincoCoreUser(userId));
    uhg.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(g1.getId()));
    uhgCtrl.create(uhg);

    // Edit G2 adding uhg from G1: uhg's old group = G1 != G2 → inner IF runs
    XincoCoreGroup toEdit = groupCtrl.findXincoCoreGroup(g2.getId());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreUserHasXincoCoreGroupList(
        Arrays.asList(uhgCtrl.findXincoCoreUserHasXincoCoreGroup(pk)));
    groupCtrl.edit(toEdit);

    // Cleanup
    uhgCtrl.destroy(pk);
    userCtrl.destroy(userId);
    groupCtrl.destroy(g2.getId());
    groupCtrl.destroy(g1.getId());
  }

  /**
   * Destroy a group that has ACEs. Covers Group.destroy() lines ~301-305: ace.setXincoCoreGroup
   * (null) loop runs before em.remove(group).
   */
  public void testGroup_destroyWithAce() throws Exception {
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());

    XincoCoreGroup group = buildGroup(groupCtrl, "TestGrpDestroyWithAce");
    int groupId = group.getId();

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(groupId));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Destroy group while ACE still linked — ace.setXincoCoreGroup(null) runs in destroy()
    groupCtrl.destroy(groupId);
    // ACE still exists with null group FK
    aceCtrl.destroy(aceId);
  }

  /**
   * Destroy a group that has an existing membership. Covers the orphan-check loop in Group.destroy
   * () (lines ~283-297) → IllegalOrphanException thrown.
   */
  public void testGroup_destroyOrphanCheck() throws Exception {
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupJpaController uhgCtrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());

    XincoCoreGroup group = buildGroup(groupCtrl, "TestGrpOrphanCheck");
    int groupId = group.getId();
    XincoCoreUser user = buildUser(userCtrl, "grp.orphan.check");
    int userId = user.getId();

    XincoCoreUserHasXincoCoreGroupPK pk = new XincoCoreUserHasXincoCoreGroupPK(userId, groupId);
    XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup();
    uhg.setXincoCoreUserHasXincoCoreGroupPK(pk);
    uhg.setStatusNumber(1);
    uhg.setXincoCoreUser(userCtrl.findXincoCoreUser(userId));
    uhg.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(groupId));
    uhgCtrl.create(uhg);

    try {
      groupCtrl.destroy(groupId); // group has membership → orphan check fails
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    // Cleanup
    uhgCtrl.destroy(pk);
    userCtrl.destroy(userId);
    groupCtrl.destroy(groupId);
  }

  /**
   * Create DataType DT2 with a data item (from DT1) in its list. Covers DataType.create() lines
   * ~107-131: attachment loop runs, update-refs loop runs with oldDt != null → inner IF body.
   */
  public void testDataType_createWithDataInList() throws Exception {
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    // Create DT1 and data linked to DT1
    XincoCoreDataType dt1 = new XincoCoreDataType();
    dt1.setDesignation("test.dt.create.src");
    dt1.setDescription("test");
    dt1.setXincoCoreDataList(new ArrayList<>());
    dt1.setXincoCoreDataTypeAttributeList(new ArrayList<>());
    dtCtrl.create(dt1);
    int dt1Id = dt1.getId();

    XincoCoreData data =
        buildEmptyData(dataCtrl, langCtrl, nodeCtrl, dt1Id, "test.data.for.dt.create");
    int dataId = data.getId();

    // Create DT2 with data in list: data's old dt = DT1 != DT2 → inner IF body
    XincoCoreDataType dt2 = new XincoCoreDataType();
    dt2.setDesignation("test.dt.create.dst");
    dt2.setDescription("test");
    dt2.setXincoCoreDataList(Arrays.asList(dataCtrl.findXincoCoreData(dataId)));
    dt2.setXincoCoreDataTypeAttributeList(new ArrayList<>());
    dtCtrl.create(dt2);
    int dt2Id = dt2.getId();
    assertTrue(dt2Id > 0);

    // Cleanup
    dataCtrl.destroy(dataId);
    dtCtrl.destroy(dt2Id);
    dtCtrl.destroy(dt1Id);
  }

  /**
   * Edit Group removing an ACE that was in its list. Covers Group.edit() old-ACE-not-in-new-list
   * branch (ace.setXincoCoreGroup(null)).
   */
  public void testGroup_editRemoveAce() throws Exception {
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());

    XincoCoreGroup group = buildGroup(groupCtrl, "TestGrpEditRemoveAce");
    int groupId = group.getId();

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(groupId));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Edit group with EMPTY ACE list while persistent group has the ACE
    // xincoCoreAceListOld = [ace], xincoCoreAceListNew = [] → ace.setXincoCoreGroup(null)
    XincoCoreGroup toEdit = groupCtrl.findXincoCoreGroup(groupId);
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    groupCtrl.edit(toEdit);

    aceCtrl.destroy(aceId);
    groupCtrl.destroy(groupId);
  }

  // -------------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------------

  private XincoCoreGroup buildGroup(XincoCoreGroupJpaController ctrl, String name)
      throws Exception {
    XincoCoreGroup group = new XincoCoreGroup();
    group.setDesignation(name);
    group.setStatusNumber(1);
    group.setXincoCoreAceList(new ArrayList<>());
    group.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    ctrl.create(group);
    return group;
  }

  private XincoCoreUser buildUser(XincoCoreUserJpaController ctrl, String suffix) throws Exception {
    XincoCoreUser user = new XincoCoreUser();
    user.setUsername("user." + suffix);
    user.setUserpassword("pw_" + suffix);
    user.setLastName("Last");
    user.setFirstName("First");
    user.setEmail(suffix + "@example.com");
    user.setStatusNumber(1);
    user.setAttempts(0);
    user.setLastModified(new Date());
    user.setXincoCoreUserModifiedRecordList(new ArrayList<>());
    user.setXincoCoreAceList(new ArrayList<>());
    user.setXincoCoreLogList(new ArrayList<>());
    user.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    ctrl.create(user);
    return user;
  }

  private XincoCoreData buildEmptyData(
      XincoCoreDataJpaController dataCtrl,
      XincoCoreLanguageJpaController langCtrl,
      XincoCoreNodeJpaController nodeCtrl,
      int dtId,
      String designation)
      throws Exception {
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreData data = new XincoCoreData();
    data.setDesignation(designation);
    data.setStatusNumber(1);
    data.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    data.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(dtId));
    data.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    data.setXincoCoreAceList(new ArrayList<>());
    data.setXincoCoreLogList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    data.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.create(data);
    return data;
  }
}
