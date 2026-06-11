package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;

/** CRUD for the remaining T-variant controllers: DataT, AddAttributeT, UserHasGroupT, DepT. */
public class ControllerTVariantExtendedTest extends AbstractXincoDataBaseTestCase {

  public ControllerTVariantExtendedTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerTVariantExtendedTest.class);
  }

  public void testDataTController_createEditDestroy() throws Exception {
    XincoCoreDataTJpaController ctrl = new XincoCoreDataTJpaController(getEntityManagerFactory());
    XincoCoreDataT entity = new XincoCoreDataT();
    entity.setRecordId(9910);
    entity.setId(9910);
    entity.setXincoCoreNodeId(1);
    entity.setXincoCoreLanguageId(1);
    entity.setXincoCoreDataTypeId(1);
    entity.setDesignation("TestDataT");
    entity.setStatusNumber(1);
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);
    assertNotNull(ctrl.findXincoCoreDataT(rid));
    entity.setDesignation("TestDataTUpdated");
    ctrl.edit(entity);
    assertEquals("TestDataTUpdated", ctrl.findXincoCoreDataT(rid).getDesignation());
    ctrl.destroy(rid);
    assertNull(ctrl.findXincoCoreDataT(rid));
  }

  public void testDataTController_findAll() {
    XincoCoreDataTJpaController ctrl = new XincoCoreDataTJpaController(getEntityManagerFactory());
    assertNotNull(ctrl.findXincoCoreDataTEntities());
    assertNotNull(ctrl.findXincoCoreDataTEntities(10, 0));
    assertTrue(ctrl.getXincoCoreDataTCount() >= 0);
  }

  public void testAddAttributeTController_createEditDestroy() throws Exception {
    XincoAddAttributeTJpaController ctrl =
        new XincoAddAttributeTJpaController(getEntityManagerFactory());
    XincoAddAttributeT entity = new XincoAddAttributeT();
    entity.setRecordId(9911);
    entity.setXincoCoreDataId(1);
    entity.setAttributeId(1);
    entity.setAttribInt(10);
    entity.setAttribUnsignedint(0L);
    entity.setAttribDouble(1.0);
    entity.setAttribVarchar("test-t-attr");
    entity.setAttribText("test text");
    entity.setAttribDatetime(new Date());
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);
    assertNotNull(ctrl.findXincoAddAttributeT(rid));
    entity.setAttribVarchar("updated-t-attr");
    ctrl.edit(entity);
    assertEquals("updated-t-attr", ctrl.findXincoAddAttributeT(rid).getAttribVarchar());
    ctrl.destroy(rid);
    assertNull(ctrl.findXincoAddAttributeT(rid));
  }

  public void testAddAttributeTController_findAll() {
    XincoAddAttributeTJpaController ctrl =
        new XincoAddAttributeTJpaController(getEntityManagerFactory());
    assertNotNull(ctrl.findXincoAddAttributeTEntities());
    assertNotNull(ctrl.findXincoAddAttributeTEntities(10, 0));
    assertTrue(ctrl.getXincoAddAttributeTCount() >= 0);
  }

  public void testUserHasGroupTController_createDestroy() throws Exception {
    XincoCoreUserHasXincoCoreGroupTJpaController ctrl =
        new XincoCoreUserHasXincoCoreGroupTJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupT entity = new XincoCoreUserHasXincoCoreGroupT();
    entity.setRecordId(9912);
    entity.setXincoCoreUserId(1);
    entity.setXincoCoreGroupId(1);
    entity.setStatusNumber(1);
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);
    assertNotNull(ctrl.findXincoCoreUserHasXincoCoreGroupT(rid));
    ctrl.destroy(rid);
    assertNull(ctrl.findXincoCoreUserHasXincoCoreGroupT(rid));
  }

  public void testUserHasGroupTController_findAll() {
    XincoCoreUserHasXincoCoreGroupTJpaController ctrl =
        new XincoCoreUserHasXincoCoreGroupTJpaController(getEntityManagerFactory());
    assertNotNull(ctrl.findXincoCoreUserHasXincoCoreGroupTEntities());
    assertNotNull(ctrl.findXincoCoreUserHasXincoCoreGroupTEntities(10, 0));
    assertTrue(ctrl.getXincoCoreUserHasXincoCoreGroupTCount() >= 0);
  }

  public void testDataHasDependencyTController_createDestroy() throws Exception {
    XincoCoreDataHasDependencyTJpaController ctrl =
        new XincoCoreDataHasDependencyTJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyT entity = new XincoCoreDataHasDependencyT();
    entity.setRecordId(9913);
    entity.setXincoCoreDataParentId(1);
    entity.setXincoCoreDataChildrenId(2);
    entity.setDependencyTypeId(1);
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);
    assertNotNull(ctrl.findXincoCoreDataHasDependencyT(rid));
    ctrl.destroy(rid);
    assertNull(ctrl.findXincoCoreDataHasDependencyT(rid));
  }

  public void testDataHasDependencyTController_findAll() {
    XincoCoreDataHasDependencyTJpaController ctrl =
        new XincoCoreDataHasDependencyTJpaController(getEntityManagerFactory());
    assertNotNull(ctrl.findXincoCoreDataHasDependencyTEntities());
    assertNotNull(ctrl.findXincoCoreDataHasDependencyTEntities(10, 0));
    assertTrue(ctrl.getXincoCoreDataHasDependencyTCount() >= 0);
  }
}
