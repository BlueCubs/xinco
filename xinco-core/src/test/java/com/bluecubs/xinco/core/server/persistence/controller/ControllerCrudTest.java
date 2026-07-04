package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestSuite;

/** Full create/edit/destroy CRUD cycle for the simpler JPA controllers. */
public class ControllerCrudTest extends AbstractXincoDataBaseTestCase {

  public ControllerCrudTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerCrudTest.class);
  }

  // ---- XincoCoreGroup ----

  public void testGroupController_createEditDestroy() throws Exception {
    XincoCoreGroupJpaController ctrl = new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreGroup entity = new XincoCoreGroup();
    entity.setDesignation("test.group.crud");
    entity.setStatusNumber(1);
    entity.setXincoCoreAceList(new ArrayList<>());
    entity.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());

    ctrl.create(entity);
    int id = entity.getId();
    assertTrue("id must be assigned", id > 0);
    assertNotNull(ctrl.findXincoCoreGroup(id));

    entity.setDesignation("test.group.crud.updated");
    ctrl.edit(entity);
    assertEquals("test.group.crud.updated", ctrl.findXincoCoreGroup(id).getDesignation());

    ctrl.destroy(id);
    assertNull(ctrl.findXincoCoreGroup(id));
  }

  // ---- XincoSetting ----

  public void testSettingController_createEditDestroy() throws Exception {
    XincoSettingJpaController ctrl = new XincoSettingJpaController(getEntityManagerFactory());
    XincoSetting entity = new XincoSetting();
    entity.setDescription("test.setting.crud");
    entity.setIntValue(7);
    entity.setLongValue(0L);
    entity.setStringValue("hello");
    entity.setBoolValue(false);

    ctrl.create(entity);
    int id = entity.getId();
    assertTrue(id > 0);
    assertNotNull(ctrl.findXincoSetting(id));

    entity.setIntValue(99);
    entity.setStringValue("updated");
    ctrl.edit(entity);
    XincoSetting reloaded = ctrl.findXincoSetting(id);
    assertEquals(99, (int) reloaded.getIntValue());
    assertEquals("updated", reloaded.getStringValue());

    ctrl.destroy(id);
    assertNull(ctrl.findXincoSetting(id));
  }

  // ---- XincoCoreDataType ----

  public void testDataTypeController_createEditDestroy() throws Exception {
    XincoCoreDataTypeJpaController ctrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreDataType entity = new XincoCoreDataType();
    entity.setDesignation("test.datatype.crud");
    entity.setDescription("test description");
    entity.setXincoCoreDataList(new ArrayList<>());
    entity.setXincoCoreDataTypeAttributeList(new ArrayList<>());

    ctrl.create(entity);
    int id = entity.getId();
    assertTrue(id > 0);
    assertNotNull(ctrl.findXincoCoreDataType(id));

    entity.setDescription("updated description");
    ctrl.edit(entity);
    assertEquals("updated description", ctrl.findXincoCoreDataType(id).getDescription());

    ctrl.destroy(id);
    assertNull(ctrl.findXincoCoreDataType(id));
  }

  // ---- XincoCoreLanguage ----

  public void testLanguageController_createEditDestroy() throws Exception {
    XincoCoreLanguageJpaController ctrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreLanguage entity = new XincoCoreLanguage();
    entity.setDesignation("language.test.crud");
    entity.setSign("tc");
    entity.setXincoCoreDataList(new ArrayList<>());
    entity.setXincoCoreNodeList(new ArrayList<>());

    ctrl.create(entity);
    int id = entity.getId();
    assertTrue(id > 0);
    assertNotNull(ctrl.findXincoCoreLanguage(id));

    entity.setSign("td");
    ctrl.edit(entity);
    assertEquals("td", ctrl.findXincoCoreLanguage(id).getSign());

    ctrl.destroy(id);
    assertNull(ctrl.findXincoCoreLanguage(id));
  }

  // ---- XincoCoreLanguage destroy nonexistent ----

  public void testLanguageController_destroyNonexistent_throwsException() {
    try {
      XincoCoreLanguageJpaController ctrl =
          new XincoCoreLanguageJpaController(getEntityManagerFactory());
      ctrl.destroy(Integer.MAX_VALUE);
      fail("Expected NonexistentEntityException");
    } catch (NonexistentEntityException e) {
      assertNotNull(e.getMessage());
    } catch (Exception e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail("Wrong exception: " + e.getClass().getSimpleName());
    }
  }

  // ---- XincoDependencyType ----

  public void testDependencyTypeController_createDestroy() throws Exception {
    XincoDependencyTypeJpaController ctrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());
    XincoDependencyType entity = new XincoDependencyType();
    entity.setDesignation("test.dep.type.crud");
    entity.setDescription("test.dep.type.crud");
    entity.setXincoDependencyBehavior(
        new XincoDependencyBehaviorJpaController(getEntityManagerFactory())
            .findXincoDependencyBehavior(1));
    entity.setXincoCoreDataHasDependencyList(new ArrayList<>());

    ctrl.create(entity);
    int id = entity.getId();
    assertTrue(id > 0);
    assertNotNull(ctrl.findXincoDependencyType(id));

    ctrl.destroy(id);
    assertNull(ctrl.findXincoDependencyType(id));
  }
}
