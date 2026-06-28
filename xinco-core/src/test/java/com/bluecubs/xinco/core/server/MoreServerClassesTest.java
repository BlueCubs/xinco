package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.persistence.*;
import com.bluecubs.xinco.core.server.persistence.controller.*;
import com.bluecubs.xinco.server.service.XincoCoreACE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests XincoCoreACEServer, XincoCoreNodeServer, XincoDependencyTypeServer, and
 * XincoCoreDataHasDependencyServer — constructors, write2DB(), deleteFromDB(), and static methods.
 */
public class MoreServerClassesTest extends AbstractXincoDataBaseTestCase {

  public MoreServerClassesTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(MoreServerClassesTest.class);
  }

  // =========================================================================
  // XincoCoreACEServer
  // =========================================================================

  /** Covers constructor(int id) with userId+nodeId non-null; groupId+dataId null. */
  public void testAce_loadById_userNode() throws Exception {
    // Seed ACE id=1: userId=1, nodeId=1, all perms
    XincoCoreACEServer ace = new XincoCoreACEServer(1);
    assertEquals(1, ace.getId());
    assertEquals(1, ace.getXincoCoreUserId());
    assertEquals(1, ace.getXincoCoreNodeId());
  }

  /** Covers constructor(int id) with groupId+dataId non-null; userId+nodeId null. */
  public void testAce_loadById_groupData() throws Exception {
    // Seed ACE id=12: groupId=1, dataId=1
    XincoCoreACEServer ace = new XincoCoreACEServer(12);
    assertEquals(12, ace.getId());
    assertEquals(1, ace.getXincoCoreGroupId());
    assertEquals(1, ace.getXincoCoreDataId());
  }

  /** Covers constructor(int id) not-found → else-branch → XincoException. */
  public void testAce_loadById_notFound() {
    try {
      new XincoCoreACEServer(99999);
      fail("Expected XincoException for non-existent ACE");
    } catch (XincoException e) {
      // expected: controller returns null → else branch
    }
  }

  /** Covers constructor(persistence.XincoCoreAce) with all 4 FK null-check branches. */
  public void testAce_constructorFromEntity() {
    XincoCoreAceJpaController ctrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    // ACE 1: user+node → user T, group F, node T, data F
    XincoCoreACEServer s1 = new XincoCoreACEServer(ctrl.findXincoCoreAce(1));
    assertEquals(1, s1.getId());
    // ACE 12: group+data → user F, group T, node F, data T
    XincoCoreACEServer s12 = new XincoCoreACEServer(ctrl.findXincoCoreAce(12));
    assertEquals(12, s12.getId());
  }

  /** Covers constructor(int, int, int, int, int, boolean×4) — direct field assignment. */
  public void testAce_constructorFields() throws Exception {
    XincoCoreACEServer ace = new XincoCoreACEServer(0, 1, 0, 1, 0, true, false, false, true);
    assertEquals(0, ace.getId());
    assertTrue(ace.isReadPermission());
    assertTrue(ace.isAdminPermission());
    assertFalse(ace.isWritePermission());
  }

  /** Covers write2DB() create path (id==0) and removeFromDB(). */
  public void testAce_write2DB_create() throws Exception {
    XincoCoreACEServer ace = new XincoCoreACEServer(0, 1, 0, 1, 0, true, false, false, false);
    int newId = ace.write2DB();
    assertTrue("New ACE id must be positive", newId > 0);
    assertEquals(0, XincoCoreACEServer.removeFromDB(ace, 1));
  }

  /** Covers write2DB() update path (id > 0 — namedQuery + edit). */
  public void testAce_write2DB_update() throws Exception {
    // Create one ACE first so we have a known id > 0
    XincoCoreACEServer aceCreate = new XincoCoreACEServer(0, 1, 0, 1, 0, true, false, false, false);
    int newId = aceCreate.write2DB();
    assertTrue(newId > 0);

    // Update path: id > 0 → loads via namedQuery, edits
    XincoCoreACEServer aceUpdate =
        new XincoCoreACEServer(newId, 1, 0, 1, 0, false, true, false, false);
    assertEquals(newId, aceUpdate.write2DB());

    XincoCoreACEServer.removeFromDB(aceCreate, 1);
  }

  /** Covers getXincoCoreACL() — node 1 has seeded ACEs. */
  public void testAce_getXincoCoreACL() {
    List<XincoCoreACEServer> acl = XincoCoreACEServer.getXincoCoreACL(1, "xincoCoreNode.id");
    assertFalse("Node 1 must have at least one ACE", acl.isEmpty());
  }

  /** Covers checkAccess() user-match path — userId == attrU.getId(). */
  public void testAce_checkAccess_userMatch() throws Exception {
    // ACE: userId=1, all perms true
    XincoCoreACEServer ace = new XincoCoreACEServer(0, 1, 0, 0, 0, true, true, true, true);
    com.bluecubs.xinco.server.service.XincoCoreUser user =
        new com.bluecubs.xinco.server.service.XincoCoreUser();
    user.setId(1);
    XincoCoreACE result = XincoCoreACEServer.checkAccess(user, Arrays.asList(ace));
    assertTrue(result.isReadPermission());
    assertTrue(result.isAdminPermission());
  }

  /** Covers checkAccess() group-match path — inner for loop + group-id match. */
  public void testAce_checkAccess_groupMatch() throws Exception {
    // ACE: userId=99 (no user match), groupId=1, write=true
    XincoCoreACEServer ace = new XincoCoreACEServer(0, 99, 1, 0, 0, false, true, false, false);
    com.bluecubs.xinco.server.service.XincoCoreUser user =
        new com.bluecubs.xinco.server.service.XincoCoreUser();
    user.setId(50); // doesn't match userId=99
    user.getXincoCoreGroups().add(new XincoCoreGroupServer(1, "group1", 1));
    XincoCoreACE result = XincoCoreACEServer.checkAccess(user, Arrays.asList(ace));
    assertTrue("Group match: write must be true", result.isWritePermission());
  }

  /** Covers checkAccess() public-group path — groupId==3, null user. */
  public void testAce_checkAccess_publicGroup() throws Exception {
    // ACE: groupId=3 (public), execute=true
    XincoCoreACEServer ace = new XincoCoreACEServer(0, 0, 3, 0, 0, false, false, true, false);
    XincoCoreACE result = XincoCoreACEServer.checkAccess(null, Arrays.asList(ace));
    assertTrue("Public group: execute must be true", result.isExecutePermission());
  }

  /** Covers checkAccess() no-match path — if(match_ace)==false. */
  public void testAce_checkAccess_noMatch() throws Exception {
    // ACE: userId=99, groupId=99 — neither matches user 1 with empty groups
    XincoCoreACEServer ace = new XincoCoreACEServer(0, 99, 99, 0, 0, true, true, true, true);
    com.bluecubs.xinco.server.service.XincoCoreUser user =
        new com.bluecubs.xinco.server.service.XincoCoreUser();
    user.setId(1);
    XincoCoreACE result = XincoCoreACEServer.checkAccess(user, Arrays.asList(ace));
    assertFalse("No match: read must remain false", result.isReadPermission());
  }

  // =========================================================================
  // XincoCoreNodeServer
  // =========================================================================

  /** Covers constructor(int id) happy path — loads node, fills data, nodes, ACL. */
  public void testNode_loadById() throws Exception {
    XincoCoreNodeServer ns = new XincoCoreNodeServer(1);
    assertEquals(1, ns.getId());
    assertNotNull(ns.getXincoCoreLanguage());
  }

  /** Covers constructor(int id) not-found → XincoException. */
  public void testNode_loadById_notFound() {
    try {
      new XincoCoreNodeServer(99999);
      fail("Expected XincoException for non-existent node");
    } catch (XincoException e) {
      // expected: result.size()==0 → else branch
    }
  }

  /** Covers constructor(int, int, int, String, int) — direct field assignment. */
  public void testNode_constructorFields() throws Exception {
    XincoCoreNodeServer ns = new XincoCoreNodeServer(0, 1, 1, "test-node-fields", 1);
    assertEquals(0, ns.getId());
    assertEquals("test-node-fields", ns.getDesignation());
    assertNotNull(ns.getXincoCoreLanguage());
  }

  /** Covers write2DB() update path (id > 0). */
  public void testNode_write2DB_update() throws Exception {
    XincoCoreNodeServer ns = new XincoCoreNodeServer(1);
    assertEquals(1, ns.write2DB());
  }

  /** Covers write2DB() create path (id==0) and deleteFromDB(delete_this=true). */
  public void testNode_write2DB_createDelete() throws Exception {
    XincoCoreNodeServer ns = new XincoCoreNodeServer(0, 1, 1, "test.node.srv.create", 1);
    int newId = ns.write2DB();
    assertTrue("New node id must be positive", newId > 0);
    // Reload and delete
    XincoCoreNodeServer ns2 = new XincoCoreNodeServer(newId);
    ns2.deleteFromDB(true, 1);
  }

  /** Covers fillXincoCoreData() — node 1 has seed data items 1 and 2. */
  public void testNode_fillXincoCoreData() throws Exception {
    XincoCoreNodeServer ns = new XincoCoreNodeServer(1);
    ns.fillXincoCoreData();
    assertFalse("Node 1 must have at least one data item", ns.getXincoCoreData().isEmpty());
  }

  /** Covers fillXincoCoreNodes() — node 1 has child nodes 2, 3, 4. */
  public void testNode_fillXincoCoreNodes() throws Exception {
    XincoCoreNodeServer ns = new XincoCoreNodeServer(1);
    ns.fillXincoCoreNodes();
    assertFalse("Node 1 must have child nodes", ns.getXincoCoreNodes().isEmpty());
  }

  /** Covers findXincoCoreNodes() — "xinco" prefix matches seed node 1 "xincoRoot". */
  public void testNode_findXincoCoreNodes() {
    List nodes = XincoCoreNodeServer.findXincoCoreNodes("xinco", 1);
    assertFalse("findXincoCoreNodes must find at least one match", nodes.isEmpty());
  }

  /** Covers getXincoCoreNodeParents() — node 1 is root, returns [node1]. */
  public void testNode_getXincoCoreNodeParents() {
    List parents = XincoCoreNodeServer.getXincoCoreNodeParents(1);
    assertFalse("Node 1 must appear in its own parent chain", parents.isEmpty());
  }

  // =========================================================================
  // XincoDependencyTypeServer
  // =========================================================================

  /** Covers default constructor. */
  public void testDepType_defaultConstructor() {
    assertNotNull(new XincoDependencyTypeServer());
  }

  /** Covers constructor(int, int, String) happy path — behavior found. */
  public void testDepType_constructorIdBehavior_found() throws Exception {
    XincoDependencyTypeServer dts = new XincoDependencyTypeServer(1, 1, "test-deptype-srv");
    assertEquals(1, (int) dts.getId());
    assertNotNull(dts.getXincoDependencyBehavior());
  }

  /** Covers constructor(int, int, String) not-found → XincoException. */
  public void testDepType_constructorIdBehavior_notFound() {
    try {
      new XincoDependencyTypeServer(0, 99999, "bad-type");
      fail("Expected XincoException for non-existent behavior");
    } catch (XincoException e) {
      // expected: behavior==null → else branch
    }
  }

  /** Covers constructor(int behaviorId) happy path. */
  public void testDepType_loadById_found() throws Exception {
    XincoDependencyTypeServer dts = new XincoDependencyTypeServer(1);
    assertEquals(1, (int) dts.getId());
  }

  /** Covers constructor(int behaviorId) not-found → XincoException. */
  public void testDepType_loadById_notFound() {
    try {
      new XincoDependencyTypeServer(99999);
      fail("Expected XincoException for non-existent dependency type");
    } catch (XincoException e) {
      // expected: dependency==null → else branch
    }
  }

  /** Covers write2DB() create path and deleteFromDB() success path (returns 0). */
  public void testDepType_write2DB_create() throws Exception {
    XincoDependencyTypeServer dts = new XincoDependencyTypeServer(1, 1, "test-deptype-srv-create");
    dts.setId(0);
    int newId = dts.write2DB();
    assertTrue("New dep type id must be positive", newId > 0);
    assertEquals(0, XincoDependencyTypeServer.deleteFromDB(newId));
  }

  /** Covers write2DB() update path (id > 0). */
  public void testDepType_write2DB_update() throws Exception {
    XincoDependencyTypeServer dts = new XincoDependencyTypeServer(1, 1, "test-deptype-srv-update");
    assertEquals(1, dts.write2DB());
  }

  /** Covers deleteFromDB() IllegalOrphanException catch → returns -1. */
  public void testDepType_deleteFromDB_orphan() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController typeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());

    // Create a fresh dep type, create a dep that uses it, then try to delete type → orphan
    XincoDependencyTypeServer dts = new XincoDependencyTypeServer(1, 1, "test-deptype-orphan");
    dts.setId(0);
    int typeId = dts.write2DB();

    XincoCoreData p = buildData(dataCtrl, "test.deptype.orphan.p");
    XincoCoreData c = buildData(dataCtrl, "test.deptype.orphan.c");
    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(c.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(p.getId()));
    dep.setXincoDependencyType(typeCtrl.findXincoDependencyType(typeId));
    depCtrl.create(dep);

    // Orphan → -1
    assertEquals(-1, XincoDependencyTypeServer.deleteFromDB(typeId));

    // Cleanup
    depCtrl.destroy(dep.getXincoCoreDataHasDependencyPK());
    getEntityManagerFactory().getCache().evictAll();
    new XincoDependencyTypeJpaController(getEntityManagerFactory()).destroy(typeId);
    dataCtrl.destroy(c.getId());
    dataCtrl.destroy(p.getId());
  }

  /** Covers deleteFromDB() NonexistentEntityException catch → returns -1. */
  public void testDepType_deleteFromDB_nonexistent() throws Exception {
    assertEquals(-1, XincoDependencyTypeServer.deleteFromDB(99999));
  }

  // =========================================================================
  // XincoCoreDataHasDependencyServer
  // =========================================================================

  /**
   * Covers constructor(entities), write2DB(), getRenderings(), isRendering(), constructor(3 ids),
   * and deleteFromDB() void — full lifecycle.
   */
  public void testDepServer_fullCycle() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController typeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    XincoCoreData parent = buildData(dataCtrl, "test.dep.srv.parent");
    XincoCoreData child = buildData(dataCtrl, "test.dep.srv.child");
    XincoDependencyType type = typeCtrl.findXincoDependencyType(1);

    // constructor(entities) + write2DB()
    XincoCoreDataHasDependencyServer depSrv =
        new XincoCoreDataHasDependencyServer(
            dataCtrl.findXincoCoreData(parent.getId()),
            dataCtrl.findXincoCoreData(child.getId()),
            type);
    assertEquals(0, depSrv.write2DB());

    // getRenderings(parentId) → [child]
    List<XincoCoreData> renderings = XincoCoreDataHasDependencyServer.getRenderings(parent.getId());
    assertFalse("Renderings must be non-empty after write2DB", renderings.isEmpty());

    // isRendering(childId) → true; isRendering(parentId) → false
    assertTrue(XincoCoreDataHasDependencyServer.isRendering(child.getId()));
    assertFalse(XincoCoreDataHasDependencyServer.isRendering(parent.getId()));

    // constructor(3 ids) — found path
    XincoCoreDataHasDependencyServer found =
        new XincoCoreDataHasDependencyServer(parent.getId(), child.getId(), type.getId());
    assertNotNull(found.getXincoCoreDataHasDependencyPK());

    // deleteFromDB() void
    depSrv.deleteFromDB();

    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(child.getId());
    dataCtrl.destroy(parent.getId());
  }

  /** Covers constructor(3 ids) not-found → XincoException. */
  public void testDepServer_constructor3ids_notFound() {
    try {
      new XincoCoreDataHasDependencyServer(99999, 99999, 99999);
      fail("Expected XincoException for non-existent dep");
    } catch (XincoException e) {
      // expected: result.isEmpty() → else branch
    }
  }

  /** Covers deleteFromDB(int, int, int) found path. */
  public void testDepServer_deleteFromDB_3ids() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController typeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    XincoCoreData p = buildData(dataCtrl, "test.dep.srv.del3.p");
    XincoCoreData c = buildData(dataCtrl, "test.dep.srv.del3.c");
    XincoDependencyType t = typeCtrl.findXincoDependencyType(2);

    XincoCoreDataHasDependencyServer depSrv =
        new XincoCoreDataHasDependencyServer(
            dataCtrl.findXincoCoreData(p.getId()), dataCtrl.findXincoCoreData(c.getId()), t);
    depSrv.write2DB();

    assertEquals(0, XincoCoreDataHasDependencyServer.deleteFromDB(p.getId(), c.getId(), t.getId()));

    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(c.getId());
    dataCtrl.destroy(p.getId());
  }

  // ─── Helpers ─────────────────────────────────────────────────────────────

  private XincoCoreData buildData(XincoCoreDataJpaController ctrl, String designation)
      throws Exception {
    XincoCoreData data = new XincoCoreData();
    data.setDesignation(designation);
    data.setStatusNumber(1);
    data.setXincoCoreLanguage(
        new XincoCoreLanguageJpaController(getEntityManagerFactory()).findXincoCoreLanguage(1));
    data.setXincoCoreDataType(
        new XincoCoreDataTypeJpaController(getEntityManagerFactory()).findXincoCoreDataType(1));
    data.setXincoCoreNode(
        new XincoCoreNodeJpaController(getEntityManagerFactory()).findXincoCoreNode(1));
    data.setXincoCoreAceList(new ArrayList<>());
    data.setXincoCoreLogList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    data.setXincoAddAttributeList(new ArrayList<>());
    ctrl.create(data);
    return data;
  }
}
