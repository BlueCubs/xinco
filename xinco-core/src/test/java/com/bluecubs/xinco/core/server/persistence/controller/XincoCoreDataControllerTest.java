package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoCoreDataControllerTest extends AbstractXincoDataBaseTestCase {

  public XincoCoreDataControllerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoCoreDataControllerTest.class);
  }

  private XincoCoreDataJpaController controller() throws XincoException {
    return new XincoCoreDataJpaController(getEntityManagerFactory());
  }

  public void testFindAll_returnsData() {
    try {
      List<XincoCoreData> all = controller().findXincoCoreDataEntities();
      assertNotNull(all);
      assertTrue(all.size() > 0);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testFindById_returnsKnownData() {
    try {
      XincoCoreData data = controller().findXincoCoreData(1);
      assertNotNull(data);
      assertNotNull(data.getDesignation());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testCount_greaterThanZero() {
    try {
      assertTrue(controller().getXincoCoreDataCount() > 0);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testFindAll_paged() {
    try {
      List<XincoCoreData> page = controller().findXincoCoreDataEntities(1, 0);
      assertNotNull(page);
      assertTrue(page.size() <= 1);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }
}
