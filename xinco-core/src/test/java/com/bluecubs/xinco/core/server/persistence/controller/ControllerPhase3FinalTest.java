package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.XincoAddAttributePK;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependencyPK;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLog;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroupPK;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Phase 3 final coverage: XincoCoreDataJpaController create() dep/dep1 list loops, edit()/destroy()
 * dep1 orphan checks; XincoCoreLogJpaController edit() data FK-change; XincoCoreUserJpaController
 * edit() orphan check bodies; destroy() non-existent-ID exception paths for User, AddAttr, UHG.
 */
public class ControllerPhase3FinalTest extends AbstractXincoDataBaseTestCase {

  public ControllerPhase3FinalTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerPhase3FinalTest.class);
  }

  // -------------------------------------------------------------------------
  // XincoCoreDataJpaController — create() dep list loop (depList)
  // -------------------------------------------------------------------------

  /**
   * Create data D2 with a dependency (dep.xincoCoreData was D1) in its
   * xincoCoreDataHasDependencyList. Covers create() attach loop (lines 95-106) and update-refs loop
   * (lines 163-177) where dep's old parent D1 != null → D1.depList.remove(dep).
   */
  public void testData_createWithDepInDepList() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController typeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    // D1 is the initial "xincoCoreData" (parent side) of the dep
    XincoCoreData d1 = buildData(dataCtrl, "test.dep.deplist.d1");
    XincoCoreData dChild = buildData(dataCtrl, "test.dep.deplist.child");

    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(d1.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(dChild.getId()));
    dep.setXincoDependencyType(typeCtrl.findXincoDependencyType(1));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    // D2 claims dep in its depList — attach loop (lines 95-106) + update-refs (lines 163-177) fire
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreData d2 = new XincoCoreData();
    d2.setDesignation("test.dep.deplist.d2");
    d2.setStatusNumber(1);
    d2.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    d2.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    d2.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    d2.setXincoCoreDataHasDependencyList(
        Arrays.asList(depCtrl.findXincoCoreDataHasDependency(depPK)));
    d2.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    d2.setXincoCoreAceList(new ArrayList<>());
    d2.setXincoCoreLogList(new ArrayList<>());
    d2.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.create(d2);

    // FK is non-updatable so dep.xincoCoreData stays D1 in DB; destroy dep first.
    depCtrl.destroy(depPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(d2.getId());
    dataCtrl.destroy(d1.getId());
    dataCtrl.destroy(dChild.getId());
  }

  // -------------------------------------------------------------------------
  // XincoCoreDataJpaController — create() dep1 list loop (dep1List)
  // -------------------------------------------------------------------------

  /**
   * Create data D2 with a dependency (dep.xincoCoreData1 was D1) in its
   * xincoCoreDataHasDependencyList1. Covers create() attach loop (lines 107-119) and update-refs
   * loop (lines 178-193) where dep's old child D1 != null → D1.dep1List.remove(dep).
   */
  public void testData_createWithDepInDep1List() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController typeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    // D1 is the initial "xincoCoreData1" (child side) of the dep
    XincoCoreData d1 = buildData(dataCtrl, "test.dep.dep1list.d1");
    XincoCoreData dParent = buildData(dataCtrl, "test.dep.dep1list.parent");

    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(d1.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(dParent.getId()));
    dep.setXincoDependencyType(typeCtrl.findXincoDependencyType(1));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    // D2 claims dep in its dep1List — attach loop (lines 107-119) + update-refs (lines 178-193)
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreData d2 = new XincoCoreData();
    d2.setDesignation("test.dep.dep1list.d2");
    d2.setStatusNumber(1);
    d2.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    d2.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    d2.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    d2.setXincoCoreDataHasDependencyList(new ArrayList<>());
    d2.setXincoCoreDataHasDependencyList1(
        Arrays.asList(depCtrl.findXincoCoreDataHasDependency(depPK)));
    d2.setXincoCoreAceList(new ArrayList<>());
    d2.setXincoCoreLogList(new ArrayList<>());
    d2.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.create(d2);

    // FK non-updatable; destroy dep first.
    depCtrl.destroy(depPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(d2.getId());
    dataCtrl.destroy(d1.getId());
    dataCtrl.destroy(dParent.getId());
  }

  // -------------------------------------------------------------------------
  // XincoCoreDataJpaController — edit() dep1 orphan check
  // -------------------------------------------------------------------------

  /**
   * Edit data D (which owns dep via dep1List) removing the dep from its dep1List. Covers edit()
   * dep1-orphan-check body (lines 290-302): dep in old dep1List but not in new →
   * IllegalOrphanException.
   */
  public void testData_editOrphanCheckDep1List() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController typeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    XincoCoreData dChild = buildData(dataCtrl, "test.dep.edit.orphan1.child");
    XincoCoreData dParent = buildData(dataCtrl, "test.dep.edit.orphan1.parent");

    // dep.xincoCoreData1 = dParent → dParent.dep1List contains dep
    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(dChild.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(dParent.getId()));
    dep.setXincoDependencyType(typeCtrl.findXincoDependencyType(1));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    // Edit dParent with empty dep1List → dep1 orphan check body fires
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(dParent.getId());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    try {
      dataCtrl.edit(toEdit);
      fail("Expected IllegalOrphanException for dep1 orphan check");
    } catch (IllegalOrphanException e) {
      // expected — dep1 orphan-check body covered
    }

    depCtrl.destroy(depPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(dParent.getId());
    dataCtrl.destroy(dChild.getId());
  }

  // -------------------------------------------------------------------------
  // XincoCoreDataJpaController — destroy() dep1 orphan check
  // -------------------------------------------------------------------------

  /**
   * Destroy data D that has a dep in its dep1List. Covers destroy() dep1-orphan-check loop body
   * (lines 578-592): dep exists in D's dep1List → IllegalOrphanException.
   */
  public void testData_destroyOrphanCheckDep1List() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController typeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    XincoCoreData dChild = buildData(dataCtrl, "test.dep.destroy.orphan1.child");
    XincoCoreData dParent = buildData(dataCtrl, "test.dep.destroy.orphan1.parent");

    // dep.xincoCoreData1 = dParent → dParent.dep1List non-empty
    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(dChild.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(dParent.getId()));
    dep.setXincoDependencyType(typeCtrl.findXincoDependencyType(1));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    // Destroy dParent → dep1 orphan check fires → IllegalOrphanException
    try {
      dataCtrl.destroy(dParent.getId());
      fail("Expected IllegalOrphanException for dep1 orphan check in destroy");
    } catch (IllegalOrphanException e) {
      // expected — dep1 orphan-check body covered
    }

    depCtrl.destroy(depPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(dParent.getId());
    dataCtrl.destroy(dChild.getId());
  }

  // -------------------------------------------------------------------------
  // XincoCoreLogJpaController — edit() data FK-change
  // -------------------------------------------------------------------------

  /**
   * Edit a log to change its xincoCoreData from D1 to D2. Covers edit() lines 122-129: dataOld !=
   * null && !dataOld.equals(dataNew) → dataOld.logList.remove(log); dataNew != null &&
   * !dataNew.equals(dataOld) → dataNew.logList.add(log).
   */
  public void testLog_editChangeData() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());

    XincoCoreData d1 = buildData(dataCtrl, "test.log.edit.data.d1");
    XincoCoreData d2 = buildData(dataCtrl, "test.log.edit.data.d2");
    XincoCoreUser user = buildUser(userCtrl, "log.editdata");

    XincoCoreLog log = new XincoCoreLog();
    log.setOpCode(1);
    log.setOpDatetime(new Date());
    log.setOpDescription("log edit data FK-change test");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("");
    log.setXincoCoreData(dataCtrl.findXincoCoreData(d1.getId()));
    log.setXincoCoreUser(userCtrl.findXincoCoreUser(user.getId()));
    logCtrl.create(log);
    int logId = log.getId();

    // Edit log: change data from D1 to D2 — FK-change lines 123-124 and 127-128 fire
    XincoCoreLog toEdit = logCtrl.findXincoCoreLog(logId);
    toEdit.setXincoCoreData(dataCtrl.findXincoCoreData(d2.getId()));
    toEdit.setXincoCoreUser(userCtrl.findXincoCoreUser(user.getId()));
    logCtrl.edit(toEdit);

    // log.xincoCoreData is now D2 (updatable FK). Destroy log first, then D2, D1, user.
    logCtrl.destroy(logId);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(d2.getId());
    dataCtrl.destroy(d1.getId());
    userCtrl.destroy(user.getId());
  }

  // -------------------------------------------------------------------------
  // XincoCoreUserJpaController — edit() orphan check bodies
  // -------------------------------------------------------------------------

  /**
   * Edit user U (which has a log record) with empty log list. Covers edit() lines 235-243: log in
   * old list but not in new → IllegalOrphanException.
   */
  public void testUser_editOrphanCheckLog() throws Exception {
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());

    XincoCoreUser user = buildUser(userCtrl, "user.orphan.log");
    XincoCoreData data = buildData(dataCtrl, "test.user.orphan.log.data");

    XincoCoreLog log = new XincoCoreLog();
    log.setOpCode(1);
    log.setOpDatetime(new Date());
    log.setOpDescription("user edit orphan log test");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("");
    log.setXincoCoreData(dataCtrl.findXincoCoreData(data.getId()));
    log.setXincoCoreUser(userCtrl.findXincoCoreUser(user.getId()));
    logCtrl.create(log);
    int logId = log.getId();

    // Edit user with empty log list → log orphan-check body fires
    XincoCoreUser toEdit = userCtrl.findXincoCoreUser(user.getId());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    try {
      userCtrl.edit(toEdit);
      fail("Expected IllegalOrphanException for log orphan check");
    } catch (IllegalOrphanException e) {
      // expected
    }

    logCtrl.destroy(logId);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(data.getId());
    userCtrl.destroy(user.getId());
  }

  /**
   * Edit user U (which has a UHG record) with empty UHG list. Covers edit() lines 249-259: UHG in
   * old list but not in new → IllegalOrphanException thrown.
   */
  public void testUser_editOrphanCheckUhg() throws Exception {
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupJpaController uhgCtrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());

    XincoCoreUser user = buildUser(userCtrl, "user.orphan.uhg");

    XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup();
    XincoCoreUserHasXincoCoreGroupPK uhgPK = new XincoCoreUserHasXincoCoreGroupPK();
    uhgPK.setXincoCoreUserId(user.getId());
    uhgPK.setXincoCoreGroupId(1);
    uhg.setXincoCoreUserHasXincoCoreGroupPK(uhgPK);
    uhg.setXincoCoreUser(userCtrl.findXincoCoreUser(user.getId()));
    uhg.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(1));
    uhg.setStatusNumber(1);
    uhgCtrl.create(uhg);

    // Edit user with empty UHG list → UHG orphan-check body fires
    XincoCoreUser toEdit = userCtrl.findXincoCoreUser(user.getId());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    try {
      userCtrl.edit(toEdit);
      fail("Expected IllegalOrphanException for UHG orphan check");
    } catch (IllegalOrphanException e) {
      // expected
    }

    uhgCtrl.destroy(uhgPK);
    userCtrl.destroy(user.getId());
  }

  // -------------------------------------------------------------------------
  // destroy() — EntityNotFoundException catch paths
  // -------------------------------------------------------------------------

  /**
   * Covers XincoCoreUserJpaController destroy() EntityNotFoundException →
   * NonexistentEntityException.
   */
  public void testUser_destroyNonExistent() {
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    try {
      userCtrl.destroy(99999);
      fail("Expected NonexistentEntityException for non-existent user id");
    } catch (Exception e) {
      // expected
    }
  }

  /** Covers XincoAddAttributeJpaController destroy() EntityNotFoundException. */
  public void testAddAttr_destroyNonExistent() {
    XincoAddAttributeJpaController aaCtrl =
        new XincoAddAttributeJpaController(getEntityManagerFactory());
    XincoAddAttributePK fakePK = new XincoAddAttributePK();
    fakePK.setXincoCoreDataId(99999);
    fakePK.setAttributeId(99999);
    try {
      aaCtrl.destroy(fakePK);
      fail("Expected NonexistentEntityException for non-existent addattr pk");
    } catch (Exception e) {
      // expected
    }
  }

  /** Covers XincoCoreUserHasXincoCoreGroupJpaController destroy() EntityNotFoundException. */
  public void testUhg_destroyNonExistent() {
    XincoCoreUserHasXincoCoreGroupJpaController uhgCtrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupPK fakePK = new XincoCoreUserHasXincoCoreGroupPK();
    fakePK.setXincoCoreUserId(99999);
    fakePK.setXincoCoreGroupId(99999);
    try {
      uhgCtrl.destroy(fakePK);
      fail("Expected NonexistentEntityException for non-existent UHG pk");
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
    user.setUsername("usr3." + suffix);
    user.setUserpassword("pw_" + suffix);
    user.setLastName("Last");
    user.setFirstName("First");
    user.setEmail(suffix + "@ex3.com");
    user.setStatusNumber(1);
    user.setAttempts(0);
    user.setLastModified(new Date());
    user.setXincoCoreAceList(new ArrayList<>());
    user.setXincoCoreLogList(new ArrayList<>());
    user.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    ctrl.create(user);
    return user;
  }
}
