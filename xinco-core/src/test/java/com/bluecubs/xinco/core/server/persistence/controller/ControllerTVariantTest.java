package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import junit.framework.Test;
import junit.framework.TestSuite;

/** CRUD tests for the audit-trail T-variant JPA controllers. */
public class ControllerTVariantTest extends AbstractXincoDataBaseTestCase {

  public ControllerTVariantTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerTVariantTest.class);
  }

  public void testGroupTController_createEditDestroy() throws Exception {
    XincoCoreGroupTJpaController ctrl = new XincoCoreGroupTJpaController(getEntityManagerFactory());
    XincoCoreGroupT entity = new XincoCoreGroupT();
    entity.setRecordId(9901);
    entity.setId(9901);
    entity.setDesignation("test.group.t.crud");
    entity.setStatusNumber(1);
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);
    assertNotNull(ctrl.findXincoCoreGroupT(rid));
    entity.setDesignation("test.group.t.updated");
    ctrl.edit(entity);
    assertEquals("test.group.t.updated", ctrl.findXincoCoreGroupT(rid).getDesignation());
    ctrl.destroy(rid);
    assertNull(ctrl.findXincoCoreGroupT(rid));
  }

  public void testLanguageTController_createDestroy() throws Exception {
    XincoCoreLanguageTJpaController ctrl =
        new XincoCoreLanguageTJpaController(getEntityManagerFactory());
    XincoCoreLanguageT entity = new XincoCoreLanguageT();
    entity.setRecordId(9902);
    entity.setId(9902);
    entity.setDesignation("language.t.crud");
    entity.setSign("tt");
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);
    assertNotNull(ctrl.findXincoCoreLanguageT(rid));
    ctrl.destroy(rid);
    assertNull(ctrl.findXincoCoreLanguageT(rid));
  }

  public void testDataTypeTController_createDestroy() throws Exception {
    XincoCoreDataTypeTJpaController ctrl =
        new XincoCoreDataTypeTJpaController(getEntityManagerFactory());
    XincoCoreDataTypeT entity = new XincoCoreDataTypeT();
    entity.setRecordId(9903);
    entity.setId(9903);
    entity.setDesignation("test.datatype.t.crud");
    entity.setDescription("test t description");
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);
    assertNotNull(ctrl.findXincoCoreDataTypeT(rid));
    ctrl.destroy(rid);
    assertNull(ctrl.findXincoCoreDataTypeT(rid));
  }

  public void testNodeTController_createDestroy() throws Exception {
    XincoCoreNodeTJpaController ctrl = new XincoCoreNodeTJpaController(getEntityManagerFactory());
    XincoCoreNodeT entity = new XincoCoreNodeT();
    entity.setRecordId(9904);
    entity.setId(9904);
    entity.setDesignation("TestNodeT");
    entity.setStatusNumber(1);
    entity.setXincoCoreLanguageId(1);
    entity.setXincoCoreNodeId(1);
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);
    assertNotNull(ctrl.findXincoCoreNodeT(rid));
    ctrl.destroy(rid);
    assertNull(ctrl.findXincoCoreNodeT(rid));
  }

  public void testAceTController_createDestroy() throws Exception {
    XincoCoreAceTJpaController ctrl = new XincoCoreAceTJpaController(getEntityManagerFactory());
    XincoCoreAceT entity = new XincoCoreAceT();
    entity.setRecordId(9905);
    entity.setId(9905);
    entity.setXincoCoreUserId(1);
    entity.setXincoCoreNodeId(1);
    entity.setReadPermission(true);
    entity.setWritePermission(false);
    entity.setExecutePermission(false);
    entity.setAdminPermission(false);
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);
    assertNotNull(ctrl.findXincoCoreAceT(rid));
    ctrl.destroy(rid);
    assertNull(ctrl.findXincoCoreAceT(rid));
  }

  public void testSettingTController_createDestroy() throws Exception {
    XincoSettingTJpaController ctrl = new XincoSettingTJpaController(getEntityManagerFactory());
    XincoSettingT entity = new XincoSettingT();
    entity.setRecordId(9906);
    entity.setId(9906);
    entity.setDescription("test.setting.t.crud");
    entity.setIntValue(42);
    entity.setLongValue(0L);
    entity.setBoolValue(false);
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);
    assertNotNull(ctrl.findXincoSettingT(rid));
    ctrl.destroy(rid);
    assertNull(ctrl.findXincoSettingT(rid));
  }

  public void testGroupTController_findAll_count() throws Exception {
    XincoCoreGroupTJpaController ctrl = new XincoCoreGroupTJpaController(getEntityManagerFactory());
    assertTrue(ctrl.getXincoCoreGroupTCount() >= 0);
    assertNotNull(ctrl.findXincoCoreGroupTEntities());
    assertNotNull(ctrl.findXincoCoreGroupTEntities(10, 0));
  }
}
