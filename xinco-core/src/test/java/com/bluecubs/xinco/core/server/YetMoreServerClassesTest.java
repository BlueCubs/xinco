package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.createdQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static com.bluecubs.xinco.core.server.XincoDBManager.getTransaction;
import static com.bluecubs.xinco.core.server.XincoDBManager.isLocked;
import static com.bluecubs.xinco.core.server.XincoDBManager.namedQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.nativeQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.protectedCreatedQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.protectedNamedQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.reload;
import static com.bluecubs.xinco.core.server.XincoDBManager.setContents;
import static com.bluecubs.xinco.core.server.XincoDBManager.setLocked;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreUserHasXincoCoreGroupJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreUserJpaController;
import java.io.File;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityTransaction;
import junit.framework.Test;
import junit.framework.TestSuite;

public class YetMoreServerClassesTest extends AbstractXincoDataBaseTestCase {

  public YetMoreServerClassesTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(YetMoreServerClassesTest.class);
  }

  // ---- XincoCoreNodeServer additional paths ----

  /** Node 2 has parent node 1 → covers the setXincoCoreNodeId() branch. */
  public void testNode_loadById_nodeWithParent() throws Exception {
    XincoCoreNodeServer ns = new XincoCoreNodeServer(2);
    assertEquals(2, ns.getId());
    assertTrue("node 2 should have a parent", ns.getXincoCoreNodeId() > 0);
  }

  /** Bad language id → triggers the catch block in the 5-arg constructor. */
  public void testNode_constructor5args_badLanguage() {
    try {
      new XincoCoreNodeServer(0, 1, 99999, "bad-lang-node", 1);
      fail("Expected XincoException for bad language id");
    } catch (XincoException e) {
      /* expected */
    }
  }

  /** getXincoCoreNodeParents(2) walks node 2 → node 1, covering the id > 1 chain branch. */
  public void testNode_getXincoCoreNodeParents_chain() {
    List parents = XincoCoreNodeServer.getXincoCoreNodeParents(2);
    assertTrue("parent chain for node 2 should have at least 2 entries", parents.size() >= 2);
  }

  /** deleteFromDB(false, userId) covers the recursive-children else branch. */
  public void testNode_deleteFromDB_recursiveChildren() throws Exception {
    XincoCoreNodeServer parentSrv = new XincoCoreNodeServer(0, 1, 1, "test.delfal.parent", 1);
    int parentId = parentSrv.write2DB();
    assertTrue(parentId > 0);

    XincoCoreNodeServer childSrv = new XincoCoreNodeServer(0, parentId, 1, "test.delfal.child", 1);
    int childId = childSrv.write2DB();
    assertTrue(childId > 0);

    // delete children only (not parent)
    new XincoCoreNodeServer(parentId).deleteFromDB(false, 1);

    // child should be gone
    try {
      new XincoCoreNodeServer(childId);
      fail("child node should have been deleted");
    } catch (XincoException e) {
      /* expected */
    }
    // parent should still exist
    assertNotNull(new XincoCoreNodeServer(parentId));

    // cleanup parent
    new XincoCoreNodeServer(parentId).deleteFromDB(true, 1);
  }

  // ---- XincoCoreUserServer additional paths ----

  /** Entity-based constructor (persistence entity → server object). */
  public void testUser_entityConstructor() throws Exception {
    XincoCoreUserJpaController ctrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    com.bluecubs.xinco.core.server.persistence.XincoCoreUser entity = ctrl.findXincoCoreUser(1);
    XincoCoreUserServer srv = new XincoCoreUserServer(entity);
    assertEquals(1, srv.getId());
    assertNotNull(srv.getUsername());
    assertFalse(srv.isHashPassword());
  }

  /** 8-arg constructor: covers the full-fields happy path and setHashPassword(true). */
  public void testUser_fullFieldsConstructor() throws Exception {
    XincoCoreUserServer u =
        new XincoCoreUserServer(
            0,
            "testFullFields",
            "pw",
            "Last",
            "First",
            "ff@example.com",
            1,
            0,
            new Timestamp(System.currentTimeMillis()));
    assertEquals(0, u.getId());
    assertEquals("testFullFields", u.getUsername());
    assertTrue(u.isHashPassword());
  }

  /** Wrong password for a valid username exercises the increaseAttempts branch twice. */
  public void testUser_login_wrongPassword() throws Exception {
    XincoCoreUserServer adminBefore = new XincoCoreUserServer(1);
    int originalAttempts = adminBefore.getAttempts();
    try {
      new XincoCoreUserServer("admin", "definitelywrong_test_xyz");
    } catch (XincoException e) {
      /* constructor may or may not throw depending on inner-catch outcome */
    }
    XincoCoreUserServer adminAfter = new XincoCoreUserServer(1);
    assertTrue(
        "attempts should have increased after wrong-password login",
        adminAfter.getAttempts() > originalAttempts);
    // Restore
    adminAfter.setAttempts(0);
    adminAfter.setChangerID(1);
    adminAfter.write2DB();
  }

  /** statusNumber == 4 in write2DB() clears status to 1 (aged-password recovery). */
  public void testUser_write2DB_status4Recovery() throws Exception {
    XincoCoreUserServer u = new XincoCoreUserServer(1);
    int originalStatus = u.getStatusNumber();
    u.setStatusNumber(4);
    u.setChangerID(1);
    u.write2DB();
    XincoCoreUserServer reloaded = new XincoCoreUserServer(1);
    assertEquals(1, reloaded.getStatusNumber());
    // Restore
    reloaded.setStatusNumber(originalStatus);
    reloaded.setChangerID(1);
    reloaded.write2DB();
  }

  /** Exceeding the attempts limit in write2DB() sets statusNumber to 2 (locked). */
  public void testUser_write2DB_lockAccount() throws Exception {
    XincoCoreUserServer u =
        new XincoCoreUserServer(
            0,
            "testLockUser",
            "lockpw",
            "Lock",
            "Test",
            "lock@example.com",
            1,
            0,
            new Timestamp(System.currentTimeMillis()));
    u.setHashPassword(false);
    u.setChangerID(1);
    int id = u.write2DB();
    assertTrue(id > 0);
    try {
      int limit = XincoSettingServer.getSetting("password.attempts").getIntValue();
      XincoCoreUserServer locku = new XincoCoreUserServer(id);
      locku.setAttempts(limit + 1);
      locku.setChangerID(1);
      locku.write2DB();
      XincoCoreUserServer locked = new XincoCoreUserServer(id);
      assertEquals(2, locked.getStatusNumber());
    } finally {
      new XincoCoreUserJpaController(getEntityManagerFactory()).destroy(id);
    }
  }

  /** isChange() == true in write2DB() updates the lastModified timestamp. */
  public void testUser_write2DB_withChangeTrue() throws Exception {
    XincoCoreUserServer u = new XincoCoreUserServer(1);
    u.setChange(true);
    u.setChangerID(1);
    u.write2DB();
    XincoCoreUserServer reloaded = new XincoCoreUserServer(1);
    assertNotNull(reloaded.getLastModified());
  }

  /** writeXincoCoreGroups() is invoked when setWriteGroups(true). */
  public void testUser_writeXincoCoreGroups_withGroup() throws Exception {
    XincoCoreUserServer u =
        new XincoCoreUserServer(
            0,
            "testWriteGrpUser2",
            "pw2",
            "Last",
            "First",
            "wg2@example.com",
            1,
            0,
            new Timestamp(System.currentTimeMillis()));
    u.setHashPassword(false);
    u.setChangerID(1);
    int id = u.write2DB();
    assertTrue(id > 0);
    try {
      XincoCoreUserServer reload = new XincoCoreUserServer(id);
      XincoCoreGroupServer.getXincoCoreGroups().clear();
      XincoCoreGroupServer.getXincoCoreGroups().add(new XincoCoreGroupServer(1, "group1", 1));
      reload.setWriteGroups(true);
      reload.setChangerID(1);
      reload.write2DB();
      assertNotNull(new XincoCoreUserServer(id));
      // Remove ALL group memberships for this user before destroying
      destroyAllGroupMemberships(id);
    } finally {
      new XincoCoreUserJpaController(getEntityManagerFactory()).destroy(id);
    }
  }

  // ---- XincoDBManager additional paths ----

  /** setLoc(null) triggers the NullPointerException catch path. */
  public void testDB_setLoc_null() throws Exception {
    XincoDBManager mgr = XincoDBManager.get();
    mgr.setLoc(null); // should not throw; catch falls back to default locale
  }

  /** md5('..') in a nativeQuery triggers the md5→encrypt substitution loop. */
  public void testDB_nativeQuery_withMd5Replacement() throws Exception {
    // 'x'=md5('noop') → md5 is replaced with encrypt('noop'); affects 0 rows (id=-1)
    nativeQuery("UPDATE xinco_core_language SET sign=sign WHERE id=-1 AND 'x'=md5('noop')");
  }

  /** protectedCreatedQuery with locked param=true uses getProtectedEntityManager(). */
  public void testDB_protectedCreatedQuery_lockedBranch() throws Exception {
    setLocked(true);
    try {
      List<Object> results = protectedCreatedQuery("select l from XincoCoreLanguage l", null, true);
      assertNotNull(results);
    } finally {
      setLocked(false);
    }
  }

  /** protectedNamedQuery with locked param=true uses getProtectedEntityManager(). */
  public void testDB_protectedNamedQuery_lockedBranch() throws Exception {
    setLocked(true);
    try {
      List<Object> results = protectedNamedQuery("XincoCoreLanguage.findAll", null, true);
      assertNotNull(results);
    } finally {
      setLocked(false);
    }
  }

  /** getTransaction() when isLocked()=true uses getProtectedEntityManager(). */
  public void testDB_getTransaction_lockedBranch() throws Exception {
    setLocked(true);
    try {
      EntityTransaction tx = getTransaction();
      assertNotNull(tx);
    } finally {
      setLocked(false);
    }
  }

  /** createdQuery() when locked triggers getEntityManager() throwing (locked path). */
  public void testDB_getEntityManager_throwsWhenLocked() {
    setLocked(true);
    try {
      createdQuery("select l from XincoCoreLanguage l");
      fail("Expected exception when DB is locked");
    } catch (Exception e) {
      // expected: MissingResourceException from lrb.getString("message.locked") or XincoException
    } finally {
      setLocked(false);
    }
  }

  /** setContents() with a directory argument throws IllegalArgumentException. */
  public void testDB_setContents_directory() throws Exception {
    File dir = Files.createTempDirectory("xinco-dir-test-").toFile();
    dir.deleteOnExit();
    try {
      setContents(dir, new ArrayList<>());
      fail("Expected IllegalArgumentException for directory input");
    } catch (IllegalArgumentException e) {
      /* expected */
    }
  }

  /** reload(true) covers the close() call inside reload when close param is true. */
  public void testDB_reload_withClose() throws Exception {
    reload(true);
    assertFalse(isLocked());
  }

  private void destroyAllGroupMemberships(int userId) throws Exception {
    HashMap<String, Object> params = new HashMap<>();
    params.put("xincoCoreUserId", userId);
    List<Object> memberships =
        namedQuery("XincoCoreUserHasXincoCoreGroup.findByXincoCoreUserId", params);
    XincoCoreUserHasXincoCoreGroupJpaController grpCtrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());
    for (Object o : memberships) {
      try {
        grpCtrl.destroy(((XincoCoreUserHasXincoCoreGroup) o).getXincoCoreUserHasXincoCoreGroupPK());
      } catch (Exception ignored) {
        // already gone
      }
    }
    getEntityManagerFactory().getCache().evictAll();
  }
}
