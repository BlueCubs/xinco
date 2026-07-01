package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttributeT;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserT;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyBehaviorT;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyTypeT;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;

/** CRUD for the last T-variant controllers: DependencyBehaviorT, DependencyTypeT, UserT, AttrT. */
public class ControllerTVariantFinalTest extends AbstractXincoDataBaseTestCase {

  public ControllerTVariantFinalTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerTVariantFinalTest.class);
  }

  public void testDependencyBehaviorTController_createEditDestroy() throws Exception {
    XincoDependencyBehaviorTJpaController ctrl =
        new XincoDependencyBehaviorTJpaController(getEntityManagerFactory());
    XincoDependencyBehaviorT entity = new XincoDependencyBehaviorT();
    entity.setRecordId(9920);
    entity.setId(9920);
    entity.setDesignation("test.dep.behavior.t");
    entity.setDescription("behavior t description");
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);
    assertNotNull(ctrl.findXincoDependencyBehaviorT(rid));
    entity.setDescription("behavior t updated");
    ctrl.edit(entity);
    assertEquals("behavior t updated", ctrl.findXincoDependencyBehaviorT(rid).getDescription());
    ctrl.destroy(rid);
    assertNull(ctrl.findXincoDependencyBehaviorT(rid));
  }

  public void testDependencyBehaviorTController_findAll() {
    XincoDependencyBehaviorTJpaController ctrl =
        new XincoDependencyBehaviorTJpaController(getEntityManagerFactory());
    assertNotNull(ctrl.findXincoDependencyBehaviorTEntities());
    assertNotNull(ctrl.findXincoDependencyBehaviorTEntities(10, 0));
    assertTrue(ctrl.getXincoDependencyBehaviorTCount() >= 0);
  }

  public void testDependencyTypeTController_createEditDestroy() throws Exception {
    XincoDependencyTypeTJpaController ctrl =
        new XincoDependencyTypeTJpaController(getEntityManagerFactory());
    XincoDependencyTypeT entity = new XincoDependencyTypeT();
    entity.setRecordId(9921);
    entity.setId(9921);
    entity.setXincoDependencyBehaviorId(1);
    entity.setDesignation("test.dep.type.t");
    entity.setDescription("type t description");
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);
    assertNotNull(ctrl.findXincoDependencyTypeT(rid));
    entity.setDescription("type t updated");
    ctrl.edit(entity);
    assertEquals("type t updated", ctrl.findXincoDependencyTypeT(rid).getDescription());
    ctrl.destroy(rid);
    assertNull(ctrl.findXincoDependencyTypeT(rid));
  }

  public void testDependencyTypeTController_findAll() {
    XincoDependencyTypeTJpaController ctrl =
        new XincoDependencyTypeTJpaController(getEntityManagerFactory());
    assertNotNull(ctrl.findXincoDependencyTypeTEntities());
    assertNotNull(ctrl.findXincoDependencyTypeTEntities(10, 0));
    assertTrue(ctrl.getXincoDependencyTypeTCount() >= 0);
  }

  public void testUserTController_createEditDestroy() throws Exception {
    XincoCoreUserTJpaController ctrl = new XincoCoreUserTJpaController(getEntityManagerFactory());
    XincoCoreUserT entity = new XincoCoreUserT();
    entity.setRecordId(9922);
    entity.setId(9922);
    entity.setUsername("testuserT");
    entity.setUserpassword("hash");
    entity.setLastName("T");
    entity.setFirstName("User");
    entity.setEmail("usert@example.com");
    entity.setStatusNumber(1);
    entity.setAttempts(0);
    entity.setLastModified(new Date());
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);
    assertNotNull(ctrl.findXincoCoreUserT(rid));
    entity.setEmail("updated.t@example.com");
    ctrl.edit(entity);
    assertEquals("updated.t@example.com", ctrl.findXincoCoreUserT(rid).getEmail());
    ctrl.destroy(rid);
    assertNull(ctrl.findXincoCoreUserT(rid));
  }

  public void testUserTController_findAll() {
    XincoCoreUserTJpaController ctrl = new XincoCoreUserTJpaController(getEntityManagerFactory());
    assertNotNull(ctrl.findXincoCoreUserTEntities());
    assertNotNull(ctrl.findXincoCoreUserTEntities(10, 0));
    assertTrue(ctrl.getXincoCoreUserTCount() >= 0);
  }

  public void testDataTypeAttributeTController_createEditDestroy() throws Exception {
    XincoCoreDataTypeAttributeTJpaController ctrl =
        new XincoCoreDataTypeAttributeTJpaController(getEntityManagerFactory());
    XincoCoreDataTypeAttributeT entity = new XincoCoreDataTypeAttributeT();
    entity.setRecordId(9923);
    entity.setXincoCoreDataTypeId(1);
    entity.setAttributeId(999);
    entity.setDesignation("test.attr.t");
    entity.setDataType("varchar");
    entity.setAttrSize(255);
    ctrl.create(entity);
    Integer rid = entity.getRecordId();
    assertNotNull(rid);
    assertNotNull(ctrl.findXincoCoreDataTypeAttributeT(rid));
    entity.setDesignation("test.attr.t.updated");
    ctrl.edit(entity);
    assertEquals("test.attr.t.updated", ctrl.findXincoCoreDataTypeAttributeT(rid).getDesignation());
    ctrl.destroy(rid);
    assertNull(ctrl.findXincoCoreDataTypeAttributeT(rid));
  }

  public void testDataTypeAttributeTController_findAll() {
    XincoCoreDataTypeAttributeTJpaController ctrl =
        new XincoCoreDataTypeAttributeTJpaController(getEntityManagerFactory());
    assertNotNull(ctrl.findXincoCoreDataTypeAttributeTEntities());
    assertNotNull(ctrl.findXincoCoreDataTypeAttributeTEntities(10, 0));
    assertTrue(ctrl.getXincoCoreDataTypeAttributeTCount() >= 0);
  }
}
