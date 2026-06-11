package com.bluecubs.xinco.core.server.service;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;

/** DB-backed tests for XincoTreeService.getAuthorizedChildData and getAuthorizedChildNodes. */
public class XincoTreeServiceDbTest extends AbstractXincoDataBaseTestCase {

  public XincoTreeServiceDbTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoTreeServiceDbTest.class);
  }

  public void testGetAuthorizedChildData_adminOnRootNode() {
    try {
      XincoCoreUserServer admin = new XincoCoreUserServer("admin", "admin");
      XincoTreeService treeService = new XincoTreeService();
      List<?> data = treeService.getAuthorizedChildData(1, admin);
      assertNotNull(data);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testGetAuthorizedChildNodes_adminOnRootNode() {
    try {
      XincoCoreUserServer admin = new XincoCoreUserServer("admin", "admin");
      XincoTreeService treeService = new XincoTreeService();
      List<?> nodes = treeService.getAuthorizedChildNodes(1, admin);
      assertNotNull(nodes);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }
}
