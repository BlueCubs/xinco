package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Covers the edit + findAll + getCount gaps for DependencyType, DependencyBehavior, and
 * DataTypeAttribute controllers.
 */
public class ControllerEditAndFindTest extends AbstractXincoDataBaseTestCase {

  public ControllerEditAndFindTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerEditAndFindTest.class);
  }

  // ---- XincoDependencyTypeJpaController ----

  public void testDependencyTypeController_editAndFindAll() throws Exception {
    XincoDependencyTypeJpaController ctrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());
    XincoDependencyBehaviorJpaController behavCtrl =
        new XincoDependencyBehaviorJpaController(getEntityManagerFactory());

    XincoDependencyType entity = new XincoDependencyType();
    entity.setDescription("test.dep.type.edit");
    entity.setDesignation("TestDepTypeEdit");
    entity.setXincoDependencyBehavior(behavCtrl.findXincoDependencyBehavior(1));
    entity.setXincoCoreDataHasDependencyList(new ArrayList<>());

    ctrl.create(entity);
    int id = entity.getId();
    assertTrue(id > 0);

    entity.setDescription("test.dep.type.edit.updated");
    ctrl.edit(entity);
    assertEquals("test.dep.type.edit.updated", ctrl.findXincoDependencyType(id).getDescription());

    ctrl.destroy(id);
    assertNull(ctrl.findXincoDependencyType(id));

    // findAll + getCount
    assertNotNull(ctrl.findXincoDependencyTypeEntities());
    assertNotNull(ctrl.findXincoDependencyTypeEntities(10, 0));
    assertTrue(ctrl.getXincoDependencyTypeCount() >= 0);
  }

  // ---- XincoCoreDataTypeAttributeJpaController ----

  public void testDataTypeAttributeController_editAndFindAll() throws Exception {
    XincoCoreDataTypeAttributeJpaController ctrl =
        new XincoCoreDataTypeAttributeJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());

    XincoCoreDataTypeAttributePK pk = new XincoCoreDataTypeAttributePK(1, 998);
    XincoCoreDataTypeAttribute entity = new XincoCoreDataTypeAttribute();
    entity.setXincoCoreDataTypeAttributePK(pk);
    entity.setDesignation("test.attr.edit");
    entity.setDataType("varchar");
    entity.setAttrSize(100);
    entity.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));

    ctrl.create(entity);
    assertNotNull(ctrl.findXincoCoreDataTypeAttribute(pk));

    entity.setDesignation("test.attr.edit.updated");
    ctrl.edit(entity);
    assertEquals(
        "test.attr.edit.updated", ctrl.findXincoCoreDataTypeAttribute(pk).getDesignation());

    ctrl.destroy(pk);
    assertNull(ctrl.findXincoCoreDataTypeAttribute(pk));

    // findAll + getCount
    assertNotNull(ctrl.findXincoCoreDataTypeAttributeEntities());
    assertNotNull(ctrl.findXincoCoreDataTypeAttributeEntities(10, 0));
    assertTrue(ctrl.getXincoCoreDataTypeAttributeCount() >= 0);
  }

  // ---- XincoCoreDataJpaController with null collection initialization ----

  public void testDataController_nullCollections() throws Exception {
    XincoCoreDataJpaController ctrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    // Pass null collections so the create method exercises the null-initialization branches
    XincoCoreData data = new XincoCoreData();
    data.setDesignation("test.data.nullcols");
    data.setStatusNumber(1);
    data.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    data.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    data.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    // Leave all list fields null to trigger the null-check initialization code

    ctrl.create(data);
    int id = data.getId();
    assertTrue(id > 0);
    assertNotNull(ctrl.findXincoCoreData(id));

    data.setDesignation("test.data.nullcols.updated");
    ctrl.edit(data);
    assertEquals("test.data.nullcols.updated", ctrl.findXincoCoreData(id).getDesignation());

    ctrl.destroy(id);
    assertNull(ctrl.findXincoCoreData(id));
  }

  // ---- XincoCoreUserHasXincoCoreGroupJpaController edit + findAll ----

  public void testUserHasGroupController_editAndFindAll() throws Exception {
    XincoCoreUserHasXincoCoreGroupJpaController ctrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());

    // Create a fresh user for this test
    XincoCoreUser user = new XincoCoreUser();
    user.setUsername("testuser_uhg2");
    user.setUserpassword("pw2");
    user.setLastName("Uhg");
    user.setFirstName("Test2");
    user.setEmail("tuhg2@example.com");
    user.setStatusNumber(1);
    user.setAttempts(0);
    user.setLastModified(new java.util.Date());
    user.setXincoCoreAceList(new ArrayList<>());
    user.setXincoCoreLogList(new ArrayList<>());
    user.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    userCtrl.create(user);

    XincoCoreUserHasXincoCoreGroupPK pk = new XincoCoreUserHasXincoCoreGroupPK(user.getId(), 1);
    XincoCoreUserHasXincoCoreGroup mapping = new XincoCoreUserHasXincoCoreGroup();
    mapping.setXincoCoreUserHasXincoCoreGroupPK(pk);
    mapping.setStatusNumber(1);
    mapping.setXincoCoreUser(userCtrl.findXincoCoreUser(user.getId()));
    mapping.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(1));

    ctrl.create(mapping);
    assertNotNull(ctrl.findXincoCoreUserHasXincoCoreGroup(pk));

    mapping.setStatusNumber(2);
    ctrl.edit(mapping);
    assertEquals(2, (int) ctrl.findXincoCoreUserHasXincoCoreGroup(pk).getStatusNumber());

    ctrl.destroy(pk);
    assertNull(ctrl.findXincoCoreUserHasXincoCoreGroup(pk));
    userCtrl.destroy(user.getId());

    // findAll + getCount
    assertNotNull(ctrl.findXincoCoreUserHasXincoCoreGroupEntities());
    assertNotNull(ctrl.findXincoCoreUserHasXincoCoreGroupEntities(10, 0));
    assertTrue(ctrl.getXincoCoreUserHasXincoCoreGroupCount() >= 0);
  }
}
