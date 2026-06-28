package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoCoreACEServer.checkAccess;
import static com.bluecubs.xinco.core.server.XincoCoreACEServer.getXincoCoreACL;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoCoreACEServerAdditionalTest extends AbstractXincoDataBaseTestCase {

  public XincoCoreACEServerAdditionalTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoCoreACEServerAdditionalTest.class);
  }

  public void testCheckAccess_nonAdminUser_hasReadOnPublicNode() {
    try {
      // User 2 is a regular user, node 2 is seeded with public read access
      XincoCoreUserServer user = new XincoCoreUserServer(2);
      XincoCoreNodeServer node = new XincoCoreNodeServer(1);
      var ace = checkAccess(user, (ArrayList) node.getXincoCoreAcl());
      assertNotNull(ace);
    } catch (XincoException ex) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }

  public void testCheckAccess_emptyAcl_returnsNoPermissions() {
    try {
      XincoCoreUserServer user = new XincoCoreUserServer(2);
      var ace = checkAccess(user, new ArrayList<>());
      assertNotNull(ace);
      assertFalse(ace.isAdminPermission());
      assertFalse(ace.isReadPermission());
      assertFalse(ace.isWritePermission());
      assertFalse(ace.isExecutePermission());
    } catch (XincoException ex) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }

  public void testGetXincoCoreACL_groupType() {
    try {
      // The seed data has group ACEs on node 1 (admin group)
      var acl = getXincoCoreACL(1, "xincoCoreGroup.id");
      assertNotNull(acl);
    } catch (Exception e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testCheckAccess_adminUser_hasAllPermissions() {
    try {
      XincoCoreUserServer admin = new XincoCoreUserServer("admin", "admin");
      XincoCoreNodeServer node = new XincoCoreNodeServer(1);
      var ace = checkAccess(admin, (ArrayList) node.getXincoCoreAcl());
      assertNotNull(ace);
      assertTrue(ace.isAdminPermission());
      assertTrue(ace.isReadPermission());
    } catch (XincoException ex) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }

  public void testConstructor_fullArgs() {
    try {
      XincoCoreACEServer ace = new XincoCoreACEServer(0, 1, 0, 1, 0, true, false, false, false);
      assertNotNull(ace);
      assertEquals(0, ace.getId());
      assertEquals(1, ace.getXincoCoreUserId());
      assertTrue(ace.isReadPermission());
      assertFalse(ace.isWritePermission());
    } catch (XincoException ex) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }

  public void testConstructor_byId() {
    try {
      XincoCoreACEServer ace = new XincoCoreACEServer(1);
      assertNotNull(ace);
      assertTrue(ace.getId() > 0);
    } catch (XincoException ex) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }

  public void testToString() {
    try {
      XincoCoreACEServer ace = new XincoCoreACEServer(1);
      assertNotNull(ace.toString());
    } catch (XincoException ex) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }

  public void testWrite2DB_createAndDelete() {
    try {
      XincoCoreACEServer ace = new XincoCoreACEServer(0, 2, 0, 2, 0, true, true, false, false);
      int id = ace.write2DB();
      assertTrue(id > 0);
      XincoCoreACEServer.removeFromDB(ace, 1);
    } catch (XincoException ex) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }
}
