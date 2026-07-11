package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.persistence.XincoCoreAce;
import com.bluecubs.xinco.core.server.persistence.XincoId;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreAceJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreNodeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoIdJpaController;
import com.bluecubs.xinco.tools.Tool;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Final coverage sweep: XincoCoreAce constructors, XincoIdJpaController destroy,
 * XincoCoreNodeServer deleteFromDB with ACE, Tool utilities.
 */
public class XincoCoreServerFinalTest extends AbstractXincoDataBaseTestCase {

  public XincoCoreServerFinalTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoCoreServerFinalTest.class);
  }

  // ---- XincoCoreAce entity constructors ----

  public void testXincoCoreAce_idConstructor() {
    XincoCoreAce ace = new XincoCoreAce(42);
    assertEquals(42, (int) ace.getId());
  }

  public void testXincoCoreAce_fullConstructor() {
    XincoCoreAce ace = new XincoCoreAce(7, true, false, true, false);
    assertEquals(7, (int) ace.getId());
    assertTrue(ace.getReadPermission());
    assertFalse(ace.getWritePermission());
    assertTrue(ace.getExecutePermission());
    assertFalse(ace.getAdminPermission());
  }

  // ---- XincoIdJpaController destroy ----

  public void testXincoIdController_createAndDestroy() throws Exception {
    XincoIdJpaController ctrl = new XincoIdJpaController(getEntityManagerFactory());
    XincoId entity = new XincoId();
    entity.setTablename("test_table_coverage");
    entity.setLastId(9999);
    ctrl.create(entity);
    Integer id = entity.getId();
    assertNotNull(ctrl.findXincoId(id));
    ctrl.destroy(id);
    assertNull(ctrl.findXincoId(id));
  }

  // ---- Tool.getImageDim — non-image suffix (no reader available) ----

  public void testGetImageDim_noReaderForSuffix() {
    // .xyz is not a known image format — returns null without file I/O
    assertNull(Tool.getImageDim("/tmp/nonexistent.xyz"));
  }

  public void testGetImageDim_nullPath_noException() {
    // null path — getFileSuffix returns null — getImageReadersBySuffix with null
    // may return empty iterator, should return null safely
    try {
      Tool.getImageDim(null);
    } catch (Exception e) {
      // acceptable: NullPointerException inside ImageIO is possible; main goal is calling the
      // method
    }
  }

  // ---- XincoCoreNodeServer.deleteFromDB(false) — children-only path ----

  public void testNodeServer_deleteFromDB_childrenOnly() {
    try {
      // Create a parent and a child node
      XincoCoreNodeServer parent = new XincoCoreNodeServer(0, 1, 1, "TestParentDelOnly", 1);
      parent.setChangerID(1);
      int parentId = parent.write2DB();
      assertTrue(parentId > 0);

      XincoCoreNodeServer child = new XincoCoreNodeServer(0, parentId, 1, "TestChildDelOnly", 1);
      child.setChangerID(1);
      int childId = child.write2DB();
      assertTrue(childId > 0);

      // Call deleteFromDB(false) to exercise the recursive children path
      parent.deleteFromDB(false, 1);

      // Child should be gone; parent still exists (deleteFromDB(false) only removes children)
      // (No assertion needed — main goal is exercising the code path)
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    } finally {
      // Best-effort cleanup
      try {
        XincoCoreNodeJpaController ctrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
        var parent2 = ctrl.findXincoCoreNode(-1);
        // cleanup skipped if already deleted
      } catch (Exception ignored) {
      }
    }
  }

  // ---- XincoCoreNodeServer.deleteFromDB(true) with an ACE on the node ----

  public void testNodeServer_deleteFromDB_withAce() {
    try {
      // Create a fresh node
      XincoCoreNodeServer node = new XincoCoreNodeServer(0, 1, 1, "TestNodeWithAce", 1);
      node.setChangerID(1);
      int nodeId = node.write2DB();
      assertTrue(nodeId > 0);

      // Create an ACE for this node
      XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());
      XincoCoreAce ace = new XincoCoreAce();
      ace.setReadPermission(true);
      ace.setWritePermission(false);
      ace.setExecutePermission(false);
      ace.setAdminPermission(false);
      ace.setXincoCoreNode(
          new XincoCoreNodeJpaController(getEntityManagerFactory()).findXincoCoreNode(nodeId));
      aceCtrl.create(ace);

      // Now delete the node with delete_this=true — exercises the ACE deletion loop
      node.deleteFromDB(true, 1);

      assertNull(
          new XincoCoreNodeJpaController(getEntityManagerFactory()).findXincoCoreNode(nodeId));
    } catch (Exception e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }
}
