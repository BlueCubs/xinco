package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;

/** Extends CRUD coverage to Node, User, Log, ACE, UserHasGroup, DataTypeAttribute controllers. */
public class ControllerCrudExtendedTest extends AbstractXincoDataBaseTestCase {

  public ControllerCrudExtendedTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerCrudExtendedTest.class);
  }

  // ---- XincoCoreNode ----

  public void testNodeController_createEditDestroy() throws Exception {
    XincoCoreNodeJpaController ctrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreNode node = new XincoCoreNode();
    node.setDesignation("TestNodeCrud");
    node.setStatusNumber(1);
    node.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    node.setXincoCoreNode(ctrl.findXincoCoreNode(1));
    node.setXincoCoreNodeList(new ArrayList<>());
    node.setXincoCoreAceList(new ArrayList<>());
    node.setXincoCoreDataList(new ArrayList<>());

    ctrl.create(node);
    int id = node.getId();
    assertTrue(id > 0);
    assertNotNull(ctrl.findXincoCoreNode(id));
    assertEquals("TestNodeCrud", ctrl.findXincoCoreNode(id).getDesignation());

    node.setDesignation("TestNodeCrudUpdated");
    ctrl.edit(node);
    assertEquals("TestNodeCrudUpdated", ctrl.findXincoCoreNode(id).getDesignation());

    ctrl.destroy(id);
    assertNull(ctrl.findXincoCoreNode(id));
  }

  // ---- XincoCoreUser ----

  public void testUserController_createEditDestroy() throws Exception {
    XincoCoreUserJpaController ctrl = new XincoCoreUserJpaController(getEntityManagerFactory());

    XincoCoreUser user = new XincoCoreUser();
    user.setUsername("testuser_crud");
    user.setUserpassword("hashed_pw");
    user.setLastName("Crud");
    user.setFirstName("Test");
    user.setEmail("testcrud@example.com");
    user.setStatusNumber(1);
    user.setAttempts(0);
    user.setLastModified(new Date());
    user.setXincoCoreUserModifiedRecordList(new ArrayList<>());
    user.setXincoCoreAceList(new ArrayList<>());
    user.setXincoCoreLogList(new ArrayList<>());
    user.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());

    ctrl.create(user);
    int id = user.getId();
    assertTrue(id > 0);
    assertNotNull(ctrl.findXincoCoreUser(id));

    user.setEmail("updated_crud@example.com");
    ctrl.edit(user);
    assertEquals("updated_crud@example.com", ctrl.findXincoCoreUser(id).getEmail());

    ctrl.destroy(id);
    assertNull(ctrl.findXincoCoreUser(id));
  }

  // ---- XincoCoreLog ----

  public void testLogController_createEditDestroy() throws Exception {
    XincoCoreLogJpaController ctrl = new XincoCoreLogJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoCoreLog log = new XincoCoreLog();
    log.setOpCode(1);
    log.setOpDatetime(new Date());
    log.setOpDescription("test log entry");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("");
    log.setXincoCoreUser(userCtrl.findXincoCoreUser(1));
    log.setXincoCoreData(dataCtrl.findXincoCoreData(1));

    ctrl.create(log);
    int id = log.getId();
    assertTrue(id > 0);
    assertNotNull(ctrl.findXincoCoreLog(id));

    log.setOpDescription("updated log entry");
    ctrl.edit(log);
    assertEquals("updated log entry", ctrl.findXincoCoreLog(id).getOpDescription());

    ctrl.destroy(id);
    assertNull(ctrl.findXincoCoreLog(id));
  }

  // ---- XincoCoreAce (user-based) ----

  public void testAceController_createDestroy() throws Exception {
    XincoCoreAceJpaController ctrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreUser(userCtrl.findXincoCoreUser(2));
    ace.setXincoCoreNode(nodeCtrl.findXincoCoreNode(2));

    ctrl.create(ace);
    int id = ace.getId();
    assertTrue(id > 0);
    assertNotNull(ctrl.findXincoCoreAce(id));
    assertTrue(ctrl.findXincoCoreAce(id).getReadPermission());

    ctrl.destroy(id);
    assertNull(ctrl.findXincoCoreAce(id));
  }

  // ---- XincoCoreDataTypeAttribute ----

  public void testDataTypeAttributeController_createDestroy() throws Exception {
    XincoCoreDataTypeAttributeJpaController ctrl =
        new XincoCoreDataTypeAttributeJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());

    XincoCoreDataTypeAttribute attr = new XincoCoreDataTypeAttribute();
    // use a high attributeId to avoid clashing with existing seed data
    XincoCoreDataTypeAttributePK pk = new XincoCoreDataTypeAttributePK(1, 999);
    attr.setXincoCoreDataTypeAttributePK(pk);
    attr.setDesignation("test.attr.crud");
    attr.setDataType("varchar");
    attr.setAttrSize(255);
    attr.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));

    ctrl.create(attr);
    assertNotNull(ctrl.findXincoCoreDataTypeAttribute(pk));
    assertEquals("test.attr.crud", ctrl.findXincoCoreDataTypeAttribute(pk).getDesignation());

    ctrl.destroy(pk);
    assertNull(ctrl.findXincoCoreDataTypeAttribute(pk));
  }

  // ---- XincoCoreUserHasXincoCoreGroup ----

  public void testUserHasGroupController_createDestroy() throws Exception {
    XincoCoreUserHasXincoCoreGroupJpaController ctrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());

    // Create a new user to avoid PK conflicts with existing group memberships
    XincoCoreUser user = new XincoCoreUser();
    user.setUsername("testuser_hg");
    user.setUserpassword("pw");
    user.setLastName("HG");
    user.setFirstName("Test");
    user.setEmail("testhg@example.com");
    user.setStatusNumber(1);
    user.setAttempts(0);
    user.setLastModified(new Date());
    user.setXincoCoreUserModifiedRecordList(new ArrayList<>());
    user.setXincoCoreAceList(new ArrayList<>());
    user.setXincoCoreLogList(new ArrayList<>());
    user.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    userCtrl.create(user);

    XincoCoreUserHasXincoCoreGroupPK pk = new XincoCoreUserHasXincoCoreGroupPK(user.getId(), 2);
    XincoCoreUserHasXincoCoreGroup mapping = new XincoCoreUserHasXincoCoreGroup();
    mapping.setXincoCoreUserHasXincoCoreGroupPK(pk);
    mapping.setStatusNumber(1);
    mapping.setXincoCoreUser(userCtrl.findXincoCoreUser(user.getId()));
    mapping.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(2));

    ctrl.create(mapping);
    assertNotNull(ctrl.findXincoCoreUserHasXincoCoreGroup(pk));

    ctrl.destroy(pk);
    assertNull(ctrl.findXincoCoreUserHasXincoCoreGroup(pk));

    userCtrl.destroy(user.getId());
  }
}
