package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Covers the edit + findAll + getCount gaps in T-variant controllers and the XincoCoreAce edit
 * path.
 */
public class ControllerMissingCoverageTest extends AbstractXincoDataBaseTestCase {

  public ControllerMissingCoverageTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerMissingCoverageTest.class);
  }

  // ---- XincoCoreAceTJpaController ----

  public void testAceTController_edit() throws Exception {
    XincoCoreAceTJpaController ctrl = new XincoCoreAceTJpaController(getEntityManagerFactory());
    XincoCoreAceT entity = new XincoCoreAceT();
    entity.setRecordId(9930);
    entity.setId(9930);
    entity.setXincoCoreUserId(1);
    entity.setXincoCoreNodeId(1);
    entity.setReadPermission(false);
    entity.setWritePermission(false);
    entity.setExecutePermission(false);
    entity.setAdminPermission(false);
    ctrl.create(entity);
    entity.setReadPermission(true);
    ctrl.edit(entity);
    XincoCoreAceT reloaded = ctrl.findXincoCoreAceT(entity.getRecordId());
    assertTrue(reloaded.getReadPermission());
    ctrl.destroy(entity.getRecordId());
  }

  public void testAceTController_findAllAndCount() {
    XincoCoreAceTJpaController ctrl = new XincoCoreAceTJpaController(getEntityManagerFactory());
    assertNotNull(ctrl.findXincoCoreAceTEntities());
    assertNotNull(ctrl.findXincoCoreAceTEntities(10, 0));
    assertTrue(ctrl.getXincoCoreAceTCount() >= 0);
  }

  // ---- XincoCoreLanguageTJpaController ----

  public void testLanguageTController_editAndFindAll() throws Exception {
    XincoCoreLanguageTJpaController ctrl =
        new XincoCoreLanguageTJpaController(getEntityManagerFactory());
    XincoCoreLanguageT entity = new XincoCoreLanguageT();
    entity.setRecordId(9931);
    entity.setId(9931);
    entity.setDesignation("lang.t.edit");
    entity.setSign("le");
    ctrl.create(entity);
    entity.setSign("lf");
    ctrl.edit(entity);
    assertEquals("lf", ctrl.findXincoCoreLanguageT(entity.getRecordId()).getSign());
    ctrl.destroy(entity.getRecordId());

    assertNotNull(ctrl.findXincoCoreLanguageTEntities());
    assertNotNull(ctrl.findXincoCoreLanguageTEntities(10, 0));
    assertTrue(ctrl.getXincoCoreLanguageTCount() >= 0);
  }

  // ---- XincoCoreDataTypeTJpaController ----

  public void testDataTypeTController_editAndFindAll() throws Exception {
    XincoCoreDataTypeTJpaController ctrl =
        new XincoCoreDataTypeTJpaController(getEntityManagerFactory());
    XincoCoreDataTypeT entity = new XincoCoreDataTypeT();
    entity.setRecordId(9932);
    entity.setId(9932);
    entity.setDesignation("dt.t.edit");
    entity.setDescription("original");
    ctrl.create(entity);
    entity.setDescription("updated");
    ctrl.edit(entity);
    assertEquals("updated", ctrl.findXincoCoreDataTypeT(entity.getRecordId()).getDescription());
    ctrl.destroy(entity.getRecordId());

    assertNotNull(ctrl.findXincoCoreDataTypeTEntities());
    assertNotNull(ctrl.findXincoCoreDataTypeTEntities(10, 0));
    assertTrue(ctrl.getXincoCoreDataTypeTCount() >= 0);
  }

  // ---- XincoCoreNodeTJpaController ----

  public void testNodeTController_editAndFindAll() throws Exception {
    XincoCoreNodeTJpaController ctrl = new XincoCoreNodeTJpaController(getEntityManagerFactory());
    XincoCoreNodeT entity = new XincoCoreNodeT();
    entity.setRecordId(9933);
    entity.setId(9933);
    entity.setDesignation("node.t.edit");
    entity.setStatusNumber(1);
    entity.setXincoCoreLanguageId(1);
    entity.setXincoCoreNodeId(1);
    ctrl.create(entity);
    entity.setDesignation("node.t.edit.updated");
    ctrl.edit(entity);
    assertEquals(
        "node.t.edit.updated", ctrl.findXincoCoreNodeT(entity.getRecordId()).getDesignation());
    ctrl.destroy(entity.getRecordId());

    assertNotNull(ctrl.findXincoCoreNodeTEntities());
    assertNotNull(ctrl.findXincoCoreNodeTEntities(10, 0));
    assertTrue(ctrl.getXincoCoreNodeTCount() >= 0);
  }

  // ---- XincoSettingTJpaController ----

  public void testSettingTController_editAndFindAll() throws Exception {
    XincoSettingTJpaController ctrl = new XincoSettingTJpaController(getEntityManagerFactory());
    XincoSettingT entity = new XincoSettingT();
    entity.setRecordId(9934);
    entity.setId(9934);
    entity.setDescription("setting.t.edit");
    entity.setIntValue(1);
    entity.setLongValue(0L);
    entity.setBoolValue(false);
    ctrl.create(entity);
    entity.setIntValue(99);
    ctrl.edit(entity);
    assertEquals(99, (int) ctrl.findXincoSettingT(entity.getRecordId()).getIntValue());
    ctrl.destroy(entity.getRecordId());

    assertNotNull(ctrl.findXincoSettingTEntities());
    assertNotNull(ctrl.findXincoSettingTEntities(10, 0));
    assertTrue(ctrl.getXincoSettingTCount() >= 0);
  }

  // ---- XincoCoreAceJpaController edit ----

  public void testAceController_editUserAce() throws Exception {
    XincoCoreAceJpaController ctrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(false);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreUser(userCtrl.findXincoCoreUser(2));
    ace.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));

    ctrl.create(ace);
    int id = ace.getId();
    assertTrue(id > 0);

    ace.setReadPermission(true);
    ace.setWritePermission(true);
    ctrl.edit(ace);
    XincoCoreAce reloaded = ctrl.findXincoCoreAce(id);
    assertTrue(reloaded.getReadPermission());
    assertTrue(reloaded.getWritePermission());

    ctrl.destroy(id);
    assertNull(ctrl.findXincoCoreAce(id));
  }

  // ---- XincoCoreAce with group assignment ----

  public void testAceController_groupAce_createDestroy() throws Exception {
    XincoCoreAceJpaController ctrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(1));
    ace.setXincoCoreNode(nodeCtrl.findXincoCoreNode(2));

    ctrl.create(ace);
    int id = ace.getId();
    assertTrue(id > 0);
    assertNotNull(ctrl.findXincoCoreAce(id));

    ctrl.destroy(id);
    assertNull(ctrl.findXincoCoreAce(id));
  }

  // ---- XincoCoreDataJpaController create/edit/destroy ----

  public void testDataController_createEditDestroy() throws Exception {
    XincoCoreDataJpaController ctrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    XincoCoreData data = new XincoCoreData();
    data.setDesignation("test.data.crud");
    data.setStatusNumber(1);
    data.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    data.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    data.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    data.setXincoCoreDataHasDependencyList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    data.setXincoCoreAceList(new ArrayList<>());
    data.setXincoCoreLogList(new ArrayList<>());
    data.setXincoAddAttributeList(new ArrayList<>());

    ctrl.create(data);
    int id = data.getId();
    assertTrue("id must be > 0", id > 0);
    assertNotNull(ctrl.findXincoCoreData(id));
    assertEquals("test.data.crud", ctrl.findXincoCoreData(id).getDesignation());

    data.setDesignation("test.data.crud.updated");
    ctrl.edit(data);
    assertEquals("test.data.crud.updated", ctrl.findXincoCoreData(id).getDesignation());

    ctrl.destroy(id);
    assertNull(ctrl.findXincoCoreData(id));
  }
}
