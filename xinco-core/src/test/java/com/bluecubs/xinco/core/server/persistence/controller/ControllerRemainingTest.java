package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;

/** CRUD for DependencyBehavior, UserModifiedRecord, DataHasDependency, AddAttribute controllers. */
public class ControllerRemainingTest extends AbstractXincoDataBaseTestCase {

  public ControllerRemainingTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerRemainingTest.class);
  }

  // ---- XincoDependencyBehavior ----

  public void testDependencyBehaviorController_createEditDestroy() throws Exception {
    XincoDependencyBehaviorJpaController ctrl =
        new XincoDependencyBehaviorJpaController(getEntityManagerFactory());
    XincoDependencyBehavior entity = new XincoDependencyBehavior();
    entity.setDesignation("test.dep.behavior.crud");
    entity.setDescription("test behavior description");
    entity.setXincoDependencyTypeList(new ArrayList<>());
    ctrl.create(entity);
    int id = entity.getId();
    assertTrue(id > 0);
    assertNotNull(ctrl.findXincoDependencyBehavior(id));
    entity.setDescription("updated behavior description");
    ctrl.edit(entity);
    assertEquals(
        "updated behavior description", ctrl.findXincoDependencyBehavior(id).getDescription());
    ctrl.destroy(id);
    assertNull(ctrl.findXincoDependencyBehavior(id));
  }

  public void testDependencyBehaviorController_findAll() {
    XincoDependencyBehaviorJpaController ctrl =
        new XincoDependencyBehaviorJpaController(getEntityManagerFactory());
    assertNotNull(ctrl.findXincoDependencyBehaviorEntities());
    assertNotNull(ctrl.findXincoDependencyBehaviorEntities(10, 0));
    assertTrue(ctrl.getXincoDependencyBehaviorCount() >= 0);
  }

  // ---- XincoCoreUserModifiedRecord ----

  public void testUserModifiedRecordController_createDestroy() throws Exception {
    XincoCoreUserModifiedRecordJpaController ctrl =
        new XincoCoreUserModifiedRecordJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());

    XincoCoreUserModifiedRecord entity = new XincoCoreUserModifiedRecord();
    // PK uses (userId, recordId) - recordId is auto-assigned
    XincoCoreUserModifiedRecordPK pk = new XincoCoreUserModifiedRecordPK();
    pk.setId(1);
    pk.setRecordId(0);
    entity.setXincoCoreUserModifiedRecordPK(pk);
    entity.setModTime(new Date());
    entity.setModReason("test modification");
    entity.setXincoCoreUser(userCtrl.findXincoCoreUser(1));

    ctrl.create(entity);
    XincoCoreUserModifiedRecordPK createdPk = entity.getXincoCoreUserModifiedRecordPK();
    assertNotNull(createdPk);
    assertNotNull(ctrl.findXincoCoreUserModifiedRecord(createdPk));

    ctrl.destroy(createdPk);
    assertNull(ctrl.findXincoCoreUserModifiedRecord(createdPk));
  }

  public void testUserModifiedRecordController_findAll() {
    XincoCoreUserModifiedRecordJpaController ctrl =
        new XincoCoreUserModifiedRecordJpaController(getEntityManagerFactory());
    assertNotNull(ctrl.findXincoCoreUserModifiedRecordEntities());
    assertNotNull(ctrl.findXincoCoreUserModifiedRecordEntities(10, 0));
    assertTrue(ctrl.getXincoCoreUserModifiedRecordCount() >= 0);
  }

  // ---- XincoCoreDataHasDependency ----

  public void testDataHasDependencyController_createDestroy() throws Exception {
    XincoCoreDataHasDependencyJpaController ctrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController depTypeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    // Use data items 1 and 2 with existing dependency type 1
    XincoCoreDataHasDependencyPK pk = new XincoCoreDataHasDependencyPK(1, 2, 1);
    XincoCoreDataHasDependency entity = new XincoCoreDataHasDependency();
    entity.setXincoCoreDataHasDependencyPK(pk);
    entity.setXincoCoreData(dataCtrl.findXincoCoreData(1));
    entity.setXincoCoreData1(dataCtrl.findXincoCoreData(2));
    entity.setXincoDependencyType(depTypeCtrl.findXincoDependencyType(1));

    ctrl.create(entity);
    assertNotNull(ctrl.findXincoCoreDataHasDependency(pk));
    ctrl.destroy(pk);
    assertNull(ctrl.findXincoCoreDataHasDependency(pk));
  }

  public void testDataHasDependencyController_findAll() {
    XincoCoreDataHasDependencyJpaController ctrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    assertNotNull(ctrl.findXincoCoreDataHasDependencyEntities());
    assertNotNull(ctrl.findXincoCoreDataHasDependencyEntities(10, 0));
    assertTrue(ctrl.getXincoCoreDataHasDependencyCount() >= 0);
  }

  // ---- XincoAddAttribute ----

  public void testAddAttributeController_createEditDestroy() throws Exception {
    XincoAddAttributeJpaController ctrl =
        new XincoAddAttributeJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    // Use data ID 1, attribute ID 999 (high to avoid conflict)
    XincoAddAttributePK pk = new XincoAddAttributePK(1, 999);
    XincoAddAttribute entity = new XincoAddAttribute();
    entity.setXincoAddAttributePK(pk);
    entity.setAttribInt(42);
    entity.setAttribUnsignedint(0L);
    entity.setAttribDouble(3.14);
    entity.setAttribVarchar("test-attr");
    entity.setAttribText("test attribute text");
    entity.setXincoCoreData(dataCtrl.findXincoCoreData(1));

    ctrl.create(entity);
    assertNotNull(ctrl.findXincoAddAttribute(pk));
    assertEquals(42, (int) ctrl.findXincoAddAttribute(pk).getAttribInt());

    entity.setAttribVarchar("updated-attr");
    ctrl.edit(entity);
    assertEquals("updated-attr", ctrl.findXincoAddAttribute(pk).getAttribVarchar());

    ctrl.destroy(pk);
    assertNull(ctrl.findXincoAddAttribute(pk));
  }

  public void testAddAttributeController_findAll() {
    XincoAddAttributeJpaController ctrl =
        new XincoAddAttributeJpaController(getEntityManagerFactory());
    assertNotNull(ctrl.findXincoAddAttributeEntities());
    assertNotNull(ctrl.findXincoAddAttributeEntities(10, 0));
    assertTrue(ctrl.getXincoAddAttributeCount() > 0);
  }
}
