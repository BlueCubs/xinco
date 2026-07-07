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
 * Covers uncovered branches in XincoDependencyTypeJpaController (create/edit dep-list loops),
 * XincoCoreDataHasDependencyJpaController (edit + paginated find), and XincoCoreGroupJpaController
 * (create with populated lists + edit orphan check).
 */
public class ControllerDepTypeAndGroupBranchesTest extends AbstractXincoDataBaseTestCase {

  public ControllerDepTypeAndGroupBranchesTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerDepTypeAndGroupBranchesTest.class);
  }

  // -------------------------------------------------------------------------
  // XincoDependencyTypeJpaController
  // -------------------------------------------------------------------------

  /**
   * Create new depType T2 with an existing dep (originally linked to type 1) in its list. Covers
   * XincoDependencyType.create() attachment loop (lines 73-84) and update-refs loop (lines 91-109)
   * with inner IF (oldType != null) firing.
   */
  public void testDepType_createWithDepInList() throws Exception {
    XincoDependencyTypeJpaController typeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoCoreData dChild = buildData(dataCtrl, "test.dt.cwithdep.child");
    XincoCoreData dParent = buildData(dataCtrl, "test.dt.cwithdep.parent");

    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(dChild.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(dParent.getId()));
    dep.setXincoDependencyType(typeCtrl.findXincoDependencyType(1));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    // Create T2 with dep in list: attachment loop + update-refs loop run;
    // dep's old type = 1 != null → inner IF fires (type1.depList.remove(dep))
    XincoDependencyBehavior behavior =
        new XincoDependencyBehaviorJpaController(getEntityManagerFactory())
            .findXincoDependencyBehavior(1);
    XincoDependencyType t2 = new XincoDependencyType();
    t2.setDesignation("test.deptype.create.withdeplist");
    t2.setDescription("test");
    t2.setXincoDependencyBehavior(behavior);
    t2.setXincoCoreDataHasDependencyList(
        Arrays.asList(depCtrl.findXincoCoreDataHasDependency(depPK)));
    typeCtrl.create(t2);
    int t2Id = t2.getId();
    assertTrue(t2Id > 0);

    // Cleanup: dep's PK still has typeId=1 (non-updatable FK → DB unchanged)
    depCtrl.destroy(depPK);
    getEntityManagerFactory().getCache().evictAll();
    typeCtrl.destroy(t2Id);
    dataCtrl.destroy(dChild.getId());
    dataCtrl.destroy(dParent.getId());
  }

  /**
   * Edit depType T1 with empty dep list while T1 has a dep in its persistent list. Covers
   * XincoDependencyType.edit() orphan-check loop (lines 135-150) → IllegalOrphanException.
   */
  public void testDepType_editOrphanCheck() throws Exception {
    XincoDependencyTypeJpaController typeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoDependencyBehavior behavior =
        new XincoDependencyBehaviorJpaController(getEntityManagerFactory())
            .findXincoDependencyBehavior(1);
    XincoDependencyType t1 = new XincoDependencyType();
    t1.setDesignation("test.deptype.edit.orphancheck");
    t1.setDescription("test");
    t1.setXincoDependencyBehavior(behavior);
    t1.setXincoCoreDataHasDependencyList(new ArrayList<>());
    typeCtrl.create(t1);
    int t1Id = t1.getId();

    XincoCoreData dChild = buildData(dataCtrl, "test.dt.editorphan.child");
    XincoCoreData dParent = buildData(dataCtrl, "test.dt.editorphan.parent");

    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(dChild.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(dParent.getId()));
    dep.setXincoDependencyType(typeCtrl.findXincoDependencyType(t1Id));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    // Edit T1 with empty dep list: dep in old list, not in new → orphan check fires
    XincoDependencyType toEdit = typeCtrl.findXincoDependencyType(t1Id);
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    try {
      typeCtrl.edit(toEdit);
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    // Cleanup
    depCtrl.destroy(depPK);
    getEntityManagerFactory().getCache().evictAll();
    typeCtrl.destroy(t1Id);
    dataCtrl.destroy(dChild.getId());
    dataCtrl.destroy(dParent.getId());
  }

  /**
   * Edit depType T2 (empty dep list) adding a dep that belongs to T1. Covers
   * XincoDependencyType.edit() update-refs loop (lines 183-207): dep not in T2 old list → body.
   * dep's old type = T1 != T2 → inner IF fires (T1.depList.remove(dep)).
   */
  public void testDepType_editMoveDepFromOtherType() throws Exception {
    XincoDependencyTypeJpaController typeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoDependencyBehavior behavior =
        new XincoDependencyBehaviorJpaController(getEntityManagerFactory())
            .findXincoDependencyBehavior(1);
    XincoDependencyType t1 = new XincoDependencyType();
    t1.setDesignation("test.deptype.edit.movedep.src");
    t1.setDescription("test");
    t1.setXincoDependencyBehavior(behavior);
    t1.setXincoCoreDataHasDependencyList(new ArrayList<>());
    typeCtrl.create(t1);
    int t1Id = t1.getId();

    XincoDependencyType t2 = new XincoDependencyType();
    t2.setDesignation("test.deptype.edit.movedep.dst");
    t2.setDescription("test");
    t2.setXincoDependencyBehavior(behavior);
    t2.setXincoCoreDataHasDependencyList(new ArrayList<>());
    typeCtrl.create(t2);
    int t2Id = t2.getId();

    XincoCoreData dChild = buildData(dataCtrl, "test.dt.movedep.child");
    XincoCoreData dParent = buildData(dataCtrl, "test.dt.movedep.parent");

    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(dChild.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(dParent.getId()));
    dep.setXincoDependencyType(typeCtrl.findXincoDependencyType(t1Id));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    // Edit T2 with dep in list: dep not in T2's old list → body; dep's old type = T1 != T2 → inner
    // IF
    XincoDependencyType toEdit = typeCtrl.findXincoDependencyType(t2Id);
    toEdit.setXincoCoreDataHasDependencyList(
        Arrays.asList(depCtrl.findXincoCoreDataHasDependency(depPK)));
    typeCtrl.edit(toEdit);

    // Cleanup: dep's PK still has T1.id (non-updatable FK), destroy dep first
    depCtrl.destroy(depPK);
    getEntityManagerFactory().getCache().evictAll();
    typeCtrl.destroy(t2Id);
    typeCtrl.destroy(t1Id);
    dataCtrl.destroy(dChild.getId());
    dataCtrl.destroy(dParent.getId());
  }

  // -------------------------------------------------------------------------
  // XincoCoreDataHasDependencyJpaController
  // -------------------------------------------------------------------------

  /**
   * Call edit() on an existing dep without changing any FK values. Covers the main edit() path in
   * XincoCoreDataHasDependencyJpaController (lines 122-208) including FK-ref loading and merge.
   */
  public void testDep_editNoChange() throws Exception {
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController typeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoCoreData dChild = buildData(dataCtrl, "test.dep.edit.nochange.child");
    XincoCoreData dParent = buildData(dataCtrl, "test.dep.edit.nochange.parent");

    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(dChild.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(dParent.getId()));
    dep.setXincoDependencyType(typeCtrl.findXincoDependencyType(1));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    // Reload dep and edit with same values — exercises the main edit() path
    XincoCoreDataHasDependency depReload = depCtrl.findXincoCoreDataHasDependency(depPK);
    depReload.setXincoCoreData(dataCtrl.findXincoCoreData(dChild.getId()));
    depReload.setXincoCoreData1(dataCtrl.findXincoCoreData(dParent.getId()));
    depReload.setXincoDependencyType(typeCtrl.findXincoDependencyType(1));
    depCtrl.edit(depReload);

    // Cleanup
    depCtrl.destroy(depPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(dChild.getId());
    dataCtrl.destroy(dParent.getId());
  }

  /**
   * Call paginated findXincoCoreDataHasDependencyEntities(maxResults, firstResult). Covers the
   * overloaded find method (findEntities(false, maxResults, firstResult) path).
   */
  public void testDep_findPaginated() {
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    java.util.List<XincoCoreDataHasDependency> result =
        depCtrl.findXincoCoreDataHasDependencyEntities(10, 0);
    assertNotNull(result);
  }

  // -------------------------------------------------------------------------
  // XincoCoreGroupJpaController
  // -------------------------------------------------------------------------

  /**
   * Create group G2 with an ACE (previously under G1) in its list. Covers Group.create()
   * update-refs loop (lines 96-108) with inner IF (oldGroup != null) firing.
   */
  public void testGroup_createWithAceFromOtherGroup() throws Exception {
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());

    XincoCoreGroup g1 = buildGroup(groupCtrl, "TestGrpAceSrcCreate");

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(g1.getId()));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Create G2 with ACE in list: ACE's old group = G1 → inner IF fires (G1.aceList.remove(ace))
    XincoCoreGroup g2 = new XincoCoreGroup();
    g2.setDesignation("TestGrpAceDstCreate");
    g2.setStatusNumber(1);
    g2.setXincoCoreAceList(Arrays.asList(aceCtrl.findXincoCoreAce(aceId)));
    g2.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    groupCtrl.create(g2);
    int g2Id = g2.getId();
    assertTrue(g2Id > 0);

    // Cleanup: destroy G2 first (nullifies ACE.group), then ACE, then G1
    groupCtrl.destroy(g2Id);
    aceCtrl.destroy(aceId);
    groupCtrl.destroy(g1.getId());
  }

  /**
   * Create group G2 with a UHG (previously under G1) in its list. Covers Group.create() update-refs
   * loop (lines 109-129) with inner IF (oldGroup != null) firing. UHG's xincoCoreGroup FK is
   * non-updatable so DB group_id stays at G1.id.
   */
  public void testGroup_createWithUhgFromOtherGroup() throws Exception {
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupJpaController uhgCtrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());

    XincoCoreGroup g1 = buildGroup(groupCtrl, "TestGrpUhgSrcCreate");
    XincoCoreUser user = buildUser(userCtrl, "grp.uhg.create.src");
    int userId = user.getId();

    XincoCoreUserHasXincoCoreGroupPK pk = new XincoCoreUserHasXincoCoreGroupPK(userId, g1.getId());
    XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup();
    uhg.setXincoCoreUserHasXincoCoreGroupPK(pk);
    uhg.setStatusNumber(1);
    uhg.setXincoCoreUser(userCtrl.findXincoCoreUser(userId));
    uhg.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(g1.getId()));
    uhgCtrl.create(uhg);

    // Create G2 with UHG in list: UHG's old group = G1 → inner IF fires in-memory
    XincoCoreGroup g2 = new XincoCoreGroup();
    g2.setDesignation("TestGrpUhgDstCreate");
    g2.setStatusNumber(1);
    g2.setXincoCoreAceList(new ArrayList<>());
    g2.setXincoCoreUserHasXincoCoreGroupList(
        Arrays.asList(uhgCtrl.findXincoCoreUserHasXincoCoreGroup(pk)));
    groupCtrl.create(g2);
    int g2Id = g2.getId();
    assertTrue(g2Id > 0);

    // Cleanup: UHG's PK still has G1.id (non-updatable FK), G2 has no UHGs in DB
    uhgCtrl.destroy(pk);
    groupCtrl.destroy(g2Id);
    groupCtrl.destroy(g1.getId());
    userCtrl.destroy(userId);
  }

  /**
   * Edit group G with empty UHG list while persistent G has a UHG. Covers Group.edit() orphan-check
   * loop (lines 159-175) → IllegalOrphanException.
   */
  public void testGroup_editOrphanCheck() throws Exception {
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupJpaController uhgCtrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());

    XincoCoreGroup group = buildGroup(groupCtrl, "TestGrpEditOrphanCheck");
    int groupId = group.getId();
    XincoCoreUser user = buildUser(userCtrl, "grp.edit.orphan.check");
    int userId = user.getId();

    XincoCoreUserHasXincoCoreGroupPK pk = new XincoCoreUserHasXincoCoreGroupPK(userId, groupId);
    XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup();
    uhg.setXincoCoreUserHasXincoCoreGroupPK(pk);
    uhg.setStatusNumber(1);
    uhg.setXincoCoreUser(userCtrl.findXincoCoreUser(userId));
    uhg.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(groupId));
    uhgCtrl.create(uhg);

    // Edit group with empty UHG list: UHG in old list not in new list → orphan check fires
    XincoCoreGroup toEdit = groupCtrl.findXincoCoreGroup(groupId);
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    try {
      groupCtrl.edit(toEdit);
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    // Cleanup
    uhgCtrl.destroy(pk);
    userCtrl.destroy(userId);
    groupCtrl.destroy(groupId);
  }

  // -------------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------------

  private XincoCoreData buildData(XincoCoreDataJpaController dataCtrl, String designation)
      throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreData data = new XincoCoreData();
    data.setDesignation(designation);
    data.setStatusNumber(1);
    data.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    data.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    data.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    data.setXincoCoreAceList(new ArrayList<>());
    data.setXincoCoreLogList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    data.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.create(data);
    return data;
  }

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
    user.setXincoCoreAceList(new ArrayList<>());
    user.setXincoCoreLogList(new ArrayList<>());
    user.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    ctrl.create(user);
    return user;
  }
}
