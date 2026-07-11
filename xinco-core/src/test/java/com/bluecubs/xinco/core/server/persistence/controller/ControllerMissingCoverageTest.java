package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.XincoCoreAce;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
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
