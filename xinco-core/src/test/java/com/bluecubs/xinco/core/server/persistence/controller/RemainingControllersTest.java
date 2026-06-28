package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import junit.framework.Test;
import junit.framework.TestSuite;

/** Covers findAll / count / findById for the remaining JPA controllers. */
public class RemainingControllersTest extends AbstractXincoDataBaseTestCase {

  public RemainingControllersTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(RemainingControllersTest.class);
  }

  public void testCoreGroupController() {
    try {
      var ctrl = new XincoCoreGroupJpaController(getEntityManagerFactory());
      assertTrue(ctrl.getXincoCoreGroupCount() > 0);
      assertFalse(ctrl.findXincoCoreGroupEntities().isEmpty());
      assertNotNull(ctrl.findXincoCoreGroup(1));
      assertFalse(ctrl.findXincoCoreGroupEntities(1, 0).isEmpty());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testCoreAceController() {
    try {
      var ctrl = new XincoCoreAceJpaController(getEntityManagerFactory());
      assertTrue(ctrl.getXincoCoreAceCount() > 0);
      assertFalse(ctrl.findXincoCoreAceEntities().isEmpty());
      assertFalse(ctrl.findXincoCoreAceEntities(1, 0).isEmpty());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testCoreDataTypeController() {
    try {
      var ctrl = new XincoCoreDataTypeJpaController(getEntityManagerFactory());
      assertTrue(ctrl.getXincoCoreDataTypeCount() > 0);
      assertFalse(ctrl.findXincoCoreDataTypeEntities().isEmpty());
      assertNotNull(ctrl.findXincoCoreDataType(1));
      assertFalse(ctrl.findXincoCoreDataTypeEntities(1, 0).isEmpty());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testCoreLogController() {
    try {
      var ctrl = new XincoCoreLogJpaController(getEntityManagerFactory());
      assertTrue(ctrl.getXincoCoreLogCount() > 0);
      assertFalse(ctrl.findXincoCoreLogEntities().isEmpty());
      assertFalse(ctrl.findXincoCoreLogEntities(1, 0).isEmpty());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testSettingController() {
    try {
      var ctrl = new XincoSettingJpaController(getEntityManagerFactory());
      assertTrue(ctrl.getXincoSettingCount() > 0);
      assertFalse(ctrl.findXincoSettingEntities().isEmpty());
      assertFalse(ctrl.findXincoSettingEntities(1, 0).isEmpty());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testIdController() {
    try {
      var ctrl = new XincoIdJpaController(getEntityManagerFactory());
      assertTrue(ctrl.getXincoIdCount() > 0);
      assertFalse(ctrl.findXincoIdEntities().isEmpty());
      assertFalse(ctrl.findXincoIdEntities(1, 0).isEmpty());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testAddAttributeController() {
    try {
      var ctrl = new XincoAddAttributeJpaController(getEntityManagerFactory());
      assertTrue(ctrl.getXincoAddAttributeCount() >= 0);
      ctrl.findXincoAddAttributeEntities();
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testDependencyTypeController() {
    try {
      var ctrl = new XincoDependencyTypeJpaController(getEntityManagerFactory());
      assertTrue(ctrl.getXincoDependencyTypeCount() >= 0);
      ctrl.findXincoDependencyTypeEntities();
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }
}
