package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoCoreACEServer.checkAccess;
import static com.bluecubs.xinco.core.server.XincoCoreACEServer.getXincoCoreACL;
import static com.bluecubs.xinco.core.server.XincoCoreDataHasDependencyServer.getRenderings;
import static com.bluecubs.xinco.core.server.XincoCoreDataHasDependencyServer.isRendering;
import static com.bluecubs.xinco.core.server.XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes;
import static com.bluecubs.xinco.core.server.XincoCoreNodeServer.getXincoCoreNodeParents;
import static com.bluecubs.xinco.core.server.XincoDBManager.createdQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.get;
import static com.bluecubs.xinco.core.server.XincoDBManager.protectedCreatedQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.updateDBState;
import static com.bluecubs.xinco.core.server.XincoDBManager.waitForDB;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataHasDependencyJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoDependencyTypeJpaController;
import java.util.ArrayList;
import java.util.HashMap;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoCoreServerDepth2Test extends AbstractXincoDataBaseTestCase {

  public XincoCoreServerDepth2Test(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoCoreServerDepth2Test.class);
  }

  public void testCheckAccess_groupAclType() {
    try {
      XincoCoreUserServer user = new XincoCoreUserServer(1);
      var groupAcl = getXincoCoreACL(1, "xincoCoreGroup.id");
      assertNotNull(groupAcl);
      // checkAccess with group-based ACL (covers group permission branch)
      var ace = checkAccess(user, new ArrayList<>(groupAcl));
      assertNotNull(ace);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testCheckAccess_nodeAclForDataAce() {
    try {
      // user 1 on node 1 (root) covers the admin path
      XincoCoreUserServer user = new XincoCoreUserServer(1);
      XincoCoreNodeServer node = new XincoCoreNodeServer(1);
      var ace = checkAccess(user, (ArrayList) node.getXincoCoreAcl());
      assertNotNull(ace);
      assertTrue(ace.isAdminPermission());
      assertTrue(ace.isReadPermission());
      assertTrue(ace.isWritePermission());
      assertTrue(ace.isExecutePermission());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testGetRenderings_noRenderingsForUrlData() {
    try {
      var renderings = getRenderings(1);
      assertNotNull(renderings);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testIsRendering_regularData() {
    try {
      assertFalse(isRendering(1));
      assertFalse(isRendering(2));
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testGetXincoCoreDataTypeAttributes_returnsNonEmpty() {
    var attrs = getXincoCoreDataTypeAttributes(1);
    assertNotNull(attrs);
    assertFalse(attrs.isEmpty());
  }

  public void testCreatedQuery_withHashMap() {
    try {
      HashMap<String, Object> params = new HashMap<>();
      params.put("id", 1);
      var result = createdQuery("SELECT l FROM XincoCoreLanguage l WHERE l.id = :id", params);
      assertNotNull(result);
      assertEquals(1, result.size());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testProtectedCreatedQuery_returnsResults() {
    try {
      var params = new java.util.HashMap<String, Object>();
      var result =
          protectedCreatedQuery("SELECT l FROM XincoCoreLanguage l WHERE l.id > 0", params, false);
      assertNotNull(result);
      assertFalse(result.isEmpty());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testDBManager_get_returnsInstance() {
    try {
      assertNotNull(get());
    } catch (Exception e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testUpdateDBState_doesNotThrow() {
    try {
      updateDBState();
    } catch (Exception e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testWaitForDB_doesNotThrow() {
    try {
      waitForDB();
    } catch (Exception e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testGetXincoCoreNodeParents_multipleParents() {
    try {
      var parents = getXincoCoreNodeParents(2);
      assertNotNull(parents);
      assertTrue(parents.size() >= 1);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testDependencyTypeServer_constructor() {
    try {
      XincoDependencyTypeServer server = new XincoDependencyTypeServer(1);
      assertNotNull(server);
      assertTrue(server.getId() > 0);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testDataHasDependencyServer_write2DBAndDelete() {
    try {
      XincoCoreDataHasDependencyJpaController ctrl =
          new XincoCoreDataHasDependencyJpaController(
              com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory());
      XincoCoreDataJpaController dataCtrl =
          new XincoCoreDataJpaController(
              com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory());
      XincoDependencyTypeJpaController depTypeCtrl =
          new XincoDependencyTypeJpaController(
              com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory());

      XincoCoreDataHasDependencyServer dep =
          new XincoCoreDataHasDependencyServer(
              dataCtrl.findXincoCoreData(1),
              dataCtrl.findXincoCoreData(2),
              depTypeCtrl.findXincoDependencyType(1));
      assertTrue(dep.write2DB() >= 0);
      dep.deleteFromDB();
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }
}
