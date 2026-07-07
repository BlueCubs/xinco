package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Triggers create() catch blocks in Group, DataType, Language, Node, and Log controllers by
 * re-persisting entities that already have a TABLE-strategy-assigned PK (causes PK violation →
 * findXxx(id) != null → PreexistingEntityException). Also triggers the Log.edit() catch block by
 * passing a non-existent log id (null em.find → NPE → catch → rethrow).
 */
public class ControllerCreateDuplicateTest extends AbstractXincoDataBaseTestCase {

  public ControllerCreateDuplicateTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerCreateDuplicateTest.class);
  }

  /** Covers XincoCoreGroupJpaController.create() catch block (~13 instr). */
  public void testGroup_createDuplicate() throws Exception {
    XincoCoreGroupJpaController ctrl = new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreGroup group = new XincoCoreGroup();
    group.setDesignation("test.dup.grp");
    group.setStatusNumber(1);
    group.setXincoCoreAceList(new ArrayList<>());
    group.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    ctrl.create(group);
    int id = group.getId();
    try {
      ctrl.create(group); // entity already has TABLE-assigned id → PK violation
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException e) {
      // expected
    }
    ctrl.destroy(id);
  }

  /**
   * Covers XincoCoreDataTypeJpaController.create() catch block (~15 instr, includes
   * ex.printStackTrace()).
   */
  public void testDataType_createDuplicate() throws Exception {
    XincoCoreDataTypeJpaController ctrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreDataType dt = new XincoCoreDataType();
    dt.setDesignation("test.dup.dt");
    dt.setDescription("dup-test");
    dt.setXincoCoreDataList(new ArrayList<>());
    dt.setXincoCoreDataTypeAttributeList(new ArrayList<>());
    ctrl.create(dt);
    int id = dt.getId();
    try {
      ctrl.create(dt); // PK violation → PreexistingEntityException
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException e) {
      // expected
    }
    ctrl.destroy(id);
  }

  /** Covers XincoCoreLanguageJpaController.create() catch block (~13 instr). */
  public void testLanguage_createDuplicate() throws Exception {
    XincoCoreLanguageJpaController ctrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreLanguage lang = new XincoCoreLanguage();
    lang.setSign("xd");
    lang.setDesignation("TestDupLanguage");
    lang.setXincoCoreNodeList(new ArrayList<>());
    lang.setXincoCoreDataList(new ArrayList<>());
    ctrl.create(lang);
    int id = lang.getId();
    try {
      ctrl.create(lang); // PK violation → PreexistingEntityException
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException e) {
      // expected
    }
    ctrl.destroy(id);
  }

  /** Covers XincoCoreNodeJpaController.create() catch block (~13 instr). */
  public void testNode_createDuplicate() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreNode node = new XincoCoreNode();
    node.setDesignation("test.dup.node");
    node.setStatusNumber(1);
    node.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    node.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    node.setXincoCoreNodeList(new ArrayList<>());
    node.setXincoCoreAceList(new ArrayList<>());
    node.setXincoCoreDataList(new ArrayList<>());
    nodeCtrl.create(node);
    int id = node.getId();
    try {
      nodeCtrl.create(node); // PK violation → PreexistingEntityException
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException e) {
      // expected
    }
    nodeCtrl.destroy(id);
  }

  /** Covers XincoCoreLogJpaController.create() catch block (~13 instr). */
  public void testLog_createDuplicate() throws Exception {
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());

    XincoCoreUser user = buildUser(userCtrl, "log.dup");
    XincoCoreData data = buildData(dataCtrl, "test.log.dup.data");

    XincoCoreLog log = buildLog(logCtrl, dataCtrl, userCtrl, data.getId(), user.getId());
    int logId = log.getId();

    try {
      logCtrl.create(log); // PK violation → PreexistingEntityException
      fail("Expected PreexistingEntityException");
    } catch (PreexistingEntityException e) {
      // expected
    }

    logCtrl.destroy(logId);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(data.getId());
    userCtrl.destroy(user.getId());
  }

  /**
   * Covers XincoCoreLogJpaController.edit() catch lines 131-133 + 140 (~11 instr). A non-existent
   * id causes em.find to return null, then getXincoCoreUser() throws NPE with a non-null message
   * (Java 14+), so the inner null-check body is skipped and the exception is rethrown.
   */
  public void testLog_editNonExistent() {
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());
    XincoCoreLog log = new XincoCoreLog();
    log.setId(99999);
    try {
      logCtrl.edit(log);
      fail("Expected exception for non-existent log id");
    } catch (Exception e) {
      // expected
    }
  }

  // -------------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------------

  private XincoCoreData buildData(XincoCoreDataJpaController dataCtrl, String designation)
      throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreData data = new XincoCoreData();
    data.setDesignation(designation);
    data.setStatusNumber(1);
    data.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    data.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    data.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    data.setXincoCoreAceList(new ArrayList<>());
    data.setXincoCoreLogList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    data.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.create(data);
    return data;
  }

  private XincoCoreUser buildUser(XincoCoreUserJpaController ctrl, String suffix) throws Exception {
    XincoCoreUser user = new XincoCoreUser();
    user.setUsername("usr5." + suffix);
    user.setUserpassword("pw5_" + suffix);
    user.setLastName("Last");
    user.setFirstName("First");
    user.setEmail(suffix + "@ex5.com");
    user.setStatusNumber(1);
    user.setAttempts(0);
    user.setLastModified(new Date());
    user.setXincoCoreAceList(new ArrayList<>());
    user.setXincoCoreLogList(new ArrayList<>());
    user.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    ctrl.create(user);
    return user;
  }

  private XincoCoreLog buildLog(
      XincoCoreLogJpaController logCtrl,
      XincoCoreDataJpaController dataCtrl,
      XincoCoreUserJpaController userCtrl,
      int dataId,
      int userId)
      throws Exception {
    XincoCoreLog log = new XincoCoreLog();
    log.setOpCode(1);
    log.setOpDatetime(new Date());
    log.setOpDescription("dup-test");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("");
    log.setXincoCoreData(dataCtrl.findXincoCoreData(dataId));
    log.setXincoCoreUser(userCtrl.findXincoCoreUser(userId));
    logCtrl.create(log);
    return log;
  }
}
