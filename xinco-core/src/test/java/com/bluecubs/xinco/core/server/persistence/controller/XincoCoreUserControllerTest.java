package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoCoreUserControllerTest extends AbstractXincoDataBaseTestCase {

  public XincoCoreUserControllerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoCoreUserControllerTest.class);
  }

  private XincoCoreUserJpaController controller() throws XincoException {
    return new XincoCoreUserJpaController(getEntityManagerFactory());
  }

  public void testFindAll_returnsUsers() {
    try {
      List<XincoCoreUser> all = controller().findXincoCoreUserEntities();
      assertNotNull(all);
      assertTrue(all.size() > 0);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testFindById_returnsAdminUser() {
    try {
      XincoCoreUser user = controller().findXincoCoreUser(1);
      assertNotNull(user);
      assertNotNull(user.getUsername());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testCount_greaterThanZero() {
    try {
      assertTrue(controller().getXincoCoreUserCount() > 0);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testFindAll_paged() {
    try {
      List<XincoCoreUser> page = controller().findXincoCoreUserEntities(1, 0);
      assertNotNull(page);
      assertTrue(page.size() <= 1);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }
}
