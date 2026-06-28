package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNode;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoCoreNodeControllerTest extends AbstractXincoDataBaseTestCase {

  public XincoCoreNodeControllerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoCoreNodeControllerTest.class);
  }

  private XincoCoreNodeJpaController controller() throws XincoException {
    return new XincoCoreNodeJpaController(getEntityManagerFactory());
  }

  public void testFindAll_returnsNodes() {
    try {
      List<XincoCoreNode> all = controller().findXincoCoreNodeEntities();
      assertNotNull(all);
      assertTrue(all.size() > 0);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testFindById_returnsRootNode() {
    try {
      XincoCoreNode node = controller().findXincoCoreNode(1);
      assertNotNull(node);
      assertNotNull(node.getDesignation());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testCount_greaterThanZero() {
    try {
      assertTrue(controller().getXincoCoreNodeCount() > 0);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testFindAll_paged() {
    try {
      List<XincoCoreNode> page = controller().findXincoCoreNodeEntities(1, 0);
      assertNotNull(page);
      assertTrue(page.size() <= 1);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }
}
