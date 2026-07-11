package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.XincoCoreAce;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLog;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNode;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroupPK;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Covers uncovered branches in XincoCoreAceJpaController (create/edit/destroy FK branches),
 * XincoCoreNodeJpaController (destroy data orphan check, paginated find), and
 * XincoCoreUserJpaController (create() log+UHG inner IFs, paginated find). Also covers
 * XincoCoreUserHasXincoCoreGroupJpaController edit() main path and paginated find.
 */
public class ControllerAceNodeUserBranchesTest extends AbstractXincoDataBaseTestCase {

  public ControllerAceNodeUserBranchesTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerAceNodeUserBranchesTest.class);
  }

  // -------------------------------------------------------------------------
  // XincoCoreAceJpaController
  // -------------------------------------------------------------------------

  /**
   * Create ACE with both xincoCoreData and xincoCoreNode set. Covers ACE.create() if-branches for
   * data (lines 65-88) and node (lines 70-92). Destroy covers the matching destroy() branches.
   */
  public void testAce_createWithDataAndNode() throws Exception {
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreNode node = buildNode(nodeCtrl, langCtrl, "TestAceCreateNodeRef");
    XincoCoreData data = buildData(dataCtrl, "test.ace.create.dataref");

    // Create ACE with data AND node: both if-branches in create() fire
    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreData(dataCtrl.findXincoCoreData(data.getId()));
    ace.setXincoCoreNode(nodeCtrl.findXincoCoreNode(node.getId()));
    aceCtrl.create(ace);
    int aceId = ace.getId();
    assertTrue(aceId > 0);

    // Destroy: covers destroy() data != null (lines 211-215) and node != null (lines 216-220)
    aceCtrl.destroy(aceId);
    dataCtrl.destroy(data.getId());
    nodeCtrl.destroy(node.getId());
  }

  /**
   * Edit ACE changing its xincoCoreUser from U1 to U2. Covers ACE.edit() user FK-change branches
   * (lines 143-150): U1.aceList.remove, U2.aceList.add.
   */
  public void testAce_editChangeUser() throws Exception {
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());

    XincoCoreUser u1 = buildUser(userCtrl, "ace.edit.user.u1");
    XincoCoreUser u2 = buildUser(userCtrl, "ace.edit.user.u2");

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreUser(userCtrl.findXincoCoreUser(u1.getId()));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Edit: change user from U1 to U2
    XincoCoreAce toEdit = aceCtrl.findXincoCoreAce(aceId);
    toEdit.setXincoCoreUser(userCtrl.findXincoCoreUser(u2.getId()));
    aceCtrl.edit(toEdit);

    // Cleanup
    aceCtrl.destroy(aceId);
    userCtrl.destroy(u2.getId());
    userCtrl.destroy(u1.getId());
  }

  /**
   * Edit ACE changing its xincoCoreData from D1 to D2. Covers ACE.edit() data FK-change branches
   * (lines 151-158): D1.aceList.remove, D2.aceList.add.
   */
  public void testAce_editChangeData() throws Exception {
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoCoreData d1 = buildData(dataCtrl, "test.ace.edit.data.d1");
    XincoCoreData d2 = buildData(dataCtrl, "test.ace.edit.data.d2");

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreData(dataCtrl.findXincoCoreData(d1.getId()));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Edit: change data from D1 to D2
    XincoCoreAce toEdit = aceCtrl.findXincoCoreAce(aceId);
    toEdit.setXincoCoreData(dataCtrl.findXincoCoreData(d2.getId()));
    aceCtrl.edit(toEdit);

    // Cleanup
    aceCtrl.destroy(aceId);
    dataCtrl.destroy(d1.getId());
    dataCtrl.destroy(d2.getId());
  }

  /**
   * Edit ACE changing its xincoCoreNode from N1 to N2. Covers ACE.edit() node FK-change branches
   * (lines 159-166): N1.aceList.remove, N2.aceList.add.
   */
  public void testAce_editChangeNode() throws Exception {
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreNode n1 = buildNode(nodeCtrl, langCtrl, "TestAceEditNodeN1");
    XincoCoreNode n2 = buildNode(nodeCtrl, langCtrl, "TestAceEditNodeN2");

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreNode(nodeCtrl.findXincoCoreNode(n1.getId()));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Edit: change node from N1 to N2
    XincoCoreAce toEdit = aceCtrl.findXincoCoreAce(aceId);
    toEdit.setXincoCoreNode(nodeCtrl.findXincoCoreNode(n2.getId()));
    aceCtrl.edit(toEdit);

    // Cleanup
    aceCtrl.destroy(aceId);
    nodeCtrl.destroy(n2.getId());
    nodeCtrl.destroy(n1.getId());
  }

  /**
   * Edit ACE changing its xincoCoreGroup from G1 to G2. Covers ACE.edit() group FK-change branches
   * (lines 167-174): G1.aceList.remove, G2.aceList.add.
   */
  public void testAce_editChangeGroup() throws Exception {
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());

    XincoCoreGroup g1 = buildGroup(groupCtrl, "TestAceEditGroupG1");
    XincoCoreGroup g2 = buildGroup(groupCtrl, "TestAceEditGroupG2");

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(g1.getId()));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Edit: change group from G1 to G2
    XincoCoreAce toEdit = aceCtrl.findXincoCoreAce(aceId);
    toEdit.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(g2.getId()));
    aceCtrl.edit(toEdit);

    // Cleanup
    aceCtrl.destroy(aceId);
    groupCtrl.destroy(g2.getId());
    groupCtrl.destroy(g1.getId());
  }

  /**
   * Call paginated findXincoCoreAceEntities(maxResults, firstResult). Covers the overloaded find
   * method (findEntities(false, maxResults, firstResult) path).
   */
  public void testAce_findPaginated() {
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    assertNotNull(aceCtrl.findXincoCoreAceEntities(10, 0));
  }

  // -------------------------------------------------------------------------
  // XincoCoreNodeJpaController
  // -------------------------------------------------------------------------

  /**
   * Attempt to destroy a node that has a data item. Covers Node.destroy() orphan-check loop (lines
   * 357-371) → IllegalOrphanException.
   */
  public void testNode_destroyOrphanCheckData() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreNode node = buildNode(nodeCtrl, langCtrl, "TestNodeDestroyOrphanData");
    XincoCoreData data = buildDataUnderNode(dataCtrl, node.getId(), "test.data.orphan.node");

    // Try to destroy node while data still references it
    try {
      nodeCtrl.destroy(node.getId());
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    // Cleanup: destroy data first, then node
    dataCtrl.destroy(data.getId());
    nodeCtrl.destroy(node.getId());
  }

  /**
   * Call paginated findXincoCoreNodeEntities(maxResults, firstResult). Covers the overloaded find
   * method.
   */
  public void testNode_findPaginated() {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    assertNotNull(nodeCtrl.findXincoCoreNodeEntities(10, 0));
  }

  // -------------------------------------------------------------------------
  // XincoCoreUserJpaController
  // -------------------------------------------------------------------------

  /**
   * Create user U2 with a log item (previously linked to U1) in its log list. Covers
   * XincoCoreUser.create() log update-refs loop (lines 152-164) with inner IF (oldUser != null)
   * firing.
   */
  public void testUser_createWithLogFromOtherUser() throws Exception {
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoCoreUser u1 = buildUser(userCtrl, "u1.create.logsrc");
    XincoCoreData data = buildData(dataCtrl, "test.data.user.create.log");

    // Create log linked to U1
    XincoCoreLog log = new XincoCoreLog();
    log.setOpCode(1);
    log.setOpDatetime(new Date());
    log.setOpDescription("log for user create test");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("");
    log.setXincoCoreData(dataCtrl.findXincoCoreData(data.getId()));
    log.setXincoCoreUser(userCtrl.findXincoCoreUser(u1.getId()));
    logCtrl.create(log);
    int logId = log.getId();

    // Create U2 with log in its list: log's old user = U1 != null → inner IF fires
    XincoCoreUser u2 = new XincoCoreUser();
    u2.setUsername("user.u2.create.logdst");
    u2.setUserpassword("pw_u2logdst");
    u2.setLastName("Last");
    u2.setFirstName("U2");
    u2.setEmail("u2.logdst@example.com");
    u2.setStatusNumber(1);
    u2.setAttempts(0);
    u2.setLastModified(new Date());
    u2.setXincoCoreAceList(new ArrayList<>());
    u2.setXincoCoreLogList(Arrays.asList(logCtrl.findXincoCoreLog(logId)));
    u2.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    userCtrl.create(u2);
    int u2Id = u2.getId();
    assertTrue(u2Id > 0);

    // Cleanup: log now belongs to U2 (FK is updatable)
    logCtrl.destroy(logId);
    dataCtrl.destroy(data.getId());
    userCtrl.destroy(u2Id);
    userCtrl.destroy(u1.getId());
  }

  /**
   * Create user U2 with a UHG (previously under U1) in its list. Covers XincoCoreUser.create() UHG
   * update-refs loop (lines 165-184) with inner IF (oldUser != null) firing. UHG's xincoCoreUser FK
   * is non-updatable so DB userId stays at U1.id.
   */
  public void testUser_createWithUhgFromOtherUser() throws Exception {
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupJpaController uhgCtrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());

    XincoCoreUser u1 = buildUser(userCtrl, "u1.create.uhgsrc");
    XincoCoreGroup group = buildGroup(groupCtrl, "TestGrpForUserCreateUhg");

    // Create UHG under U1
    XincoCoreUserHasXincoCoreGroupPK pk =
        new XincoCoreUserHasXincoCoreGroupPK(u1.getId(), group.getId());
    XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup();
    uhg.setXincoCoreUserHasXincoCoreGroupPK(pk);
    uhg.setStatusNumber(1);
    uhg.setXincoCoreUser(userCtrl.findXincoCoreUser(u1.getId()));
    uhg.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(group.getId()));
    uhgCtrl.create(uhg);

    // Create U2 with UHG in list: UHG's old user = U1 → inner IF fires in-memory
    XincoCoreUser u2 = new XincoCoreUser();
    u2.setUsername("user.u2.create.uhgdst");
    u2.setUserpassword("pw_u2uhgdst");
    u2.setLastName("Last");
    u2.setFirstName("U2");
    u2.setEmail("u2.uhgdst@example.com");
    u2.setStatusNumber(1);
    u2.setAttempts(0);
    u2.setLastModified(new Date());
    u2.setXincoCoreAceList(new ArrayList<>());
    u2.setXincoCoreLogList(new ArrayList<>());
    u2.setXincoCoreUserHasXincoCoreGroupList(
        Arrays.asList(uhgCtrl.findXincoCoreUserHasXincoCoreGroup(pk)));
    userCtrl.create(u2);
    int u2Id = u2.getId();
    assertTrue(u2Id > 0);

    // Cleanup: UHG's PK still has U1.id (non-updatable FK), U2 has no UHGs in DB
    uhgCtrl.destroy(pk);
    userCtrl.destroy(u2Id);
    userCtrl.destroy(u1.getId());
    groupCtrl.destroy(group.getId());
  }

  /**
   * Call paginated findXincoCoreUserEntities(maxResults, firstResult). Covers the overloaded find
   * method.
   */
  public void testUser_findPaginated() {
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    assertNotNull(userCtrl.findXincoCoreUserEntities(10, 0));
  }

  // -------------------------------------------------------------------------
  // XincoCoreUserHasXincoCoreGroupJpaController
  // -------------------------------------------------------------------------

  /**
   * Call edit() on an existing UHG without changing any FK values. Covers the main edit() path in
   * XincoCoreUserHasXincoCoreGroupJpaController (lines 110-182).
   */
  public void testUhg_editNoChange() throws Exception {
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupJpaController uhgCtrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());

    XincoCoreUser user = buildUser(userCtrl, "uhg.edit.nochange");
    XincoCoreGroup group = buildGroup(groupCtrl, "TestGrpUhgEditNoChange");

    XincoCoreUserHasXincoCoreGroupPK pk =
        new XincoCoreUserHasXincoCoreGroupPK(user.getId(), group.getId());
    XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup();
    uhg.setXincoCoreUserHasXincoCoreGroupPK(pk);
    uhg.setStatusNumber(1);
    uhg.setXincoCoreUser(userCtrl.findXincoCoreUser(user.getId()));
    uhg.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(group.getId()));
    uhgCtrl.create(uhg);

    // Reload and edit with same values: main edit() path, no FK-change branches fire
    XincoCoreUserHasXincoCoreGroup uhgReload = uhgCtrl.findXincoCoreUserHasXincoCoreGroup(pk);
    uhgReload.setXincoCoreUser(userCtrl.findXincoCoreUser(user.getId()));
    uhgReload.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(group.getId()));
    uhgCtrl.edit(uhgReload);

    // Cleanup
    uhgCtrl.destroy(pk);
    userCtrl.destroy(user.getId());
    groupCtrl.destroy(group.getId());
  }

  /**
   * Call paginated findXincoCoreUserHasXincoCoreGroupEntities(maxResults, firstResult). Covers the
   * overloaded find method.
   */
  public void testUhg_findPaginated() {
    XincoCoreUserHasXincoCoreGroupJpaController uhgCtrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());
    assertNotNull(uhgCtrl.findXincoCoreUserHasXincoCoreGroupEntities(10, 0));
  }

  // -------------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------------

  private XincoCoreNode buildNode(
      XincoCoreNodeJpaController nodeCtrl,
      XincoCoreLanguageJpaController langCtrl,
      String designation)
      throws Exception {
    XincoCoreNode node = new XincoCoreNode();
    node.setDesignation(designation);
    node.setStatusNumber(1);
    node.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    node.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    node.setXincoCoreNodeList(new ArrayList<>());
    node.setXincoCoreAceList(new ArrayList<>());
    node.setXincoCoreDataList(new ArrayList<>());
    nodeCtrl.create(node);
    return node;
  }

  private XincoCoreData buildData(XincoCoreDataJpaController dataCtrl, String designation)
      throws Exception {
    return buildDataUnderNode(dataCtrl, 1, designation);
  }

  private XincoCoreData buildDataUnderNode(
      XincoCoreDataJpaController dataCtrl, int nodeId, String designation) throws Exception {
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
    data.setXincoCoreNode(nodeCtrl.findXincoCoreNode(nodeId));
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
