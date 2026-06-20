package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import java.util.ArrayList;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Covers orphan-check loop bodies in XincoCoreDataJpaController.edit() (depList, logList,
 * addAttrList) and destroy() (logList, addAttrList), plus EntityNotFoundException catch blocks in
 * destroy() for ACE, DataTypeAttribute, UMR, Dep, Group, DataType, and DepBehavior controllers.
 */
public class ControllerOrphanAndNonExistentTest extends AbstractXincoDataBaseTestCase {

  public ControllerOrphanAndNonExistentTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerOrphanAndNonExistentTest.class);
  }

  // -------------------------------------------------------------------------
  // XincoCoreDataJpaController.edit() orphan-check loop bodies
  // -------------------------------------------------------------------------

  /**
   * Edit data D (which has a dep where dep.xincoCoreData=D) with an empty depList. Covers edit()
   * lines 281-287: dep in old depList not in new → if(null)=true → new ArrayList() → add().
   */
  public void testData_editOrphanCheckDepList() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController dtypeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    XincoCoreData d = buildData(dataCtrl, "test.edit.orphan.deplist.d");
    XincoCoreData dParent = buildData(dataCtrl, "test.edit.orphan.deplist.parent");

    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(d.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(dParent.getId()));
    dep.setXincoDependencyType(dtypeCtrl.findXincoDependencyType(1));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    XincoCoreData toEdit = dataCtrl.findXincoCoreData(d.getId());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    try {
      dataCtrl.edit(toEdit);
      fail("Expected IllegalOrphanException for depList orphan check");
    } catch (IllegalOrphanException e) {
      // expected: dep in old depList not in new → orphan
    }

    depCtrl.destroy(depPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(d.getId());
    dataCtrl.destroy(dParent.getId());
  }

  /**
   * Edit data D (which has a log where log.xincoCoreData=D) with an empty logList. Covers edit()
   * lines 305-311: log in old logList not in new → if(null)=true → new ArrayList() → add().
   */
  public void testData_editOrphanCheckLogList() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());

    XincoCoreData d = buildData(dataCtrl, "test.edit.orphan.loglist.d");
    XincoCoreUser user = buildUser(userCtrl, "edit.orphan.loglist");

    XincoCoreLog log = new XincoCoreLog();
    log.setOpCode(1);
    log.setOpDatetime(new Date());
    log.setOpDescription("edit logList orphan-check test");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("");
    log.setXincoCoreData(dataCtrl.findXincoCoreData(d.getId()));
    log.setXincoCoreUser(userCtrl.findXincoCoreUser(user.getId()));
    logCtrl.create(log);
    int logId = log.getId();

    XincoCoreData toEdit = dataCtrl.findXincoCoreData(d.getId());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    try {
      dataCtrl.edit(toEdit);
      fail("Expected IllegalOrphanException for logList orphan check");
    } catch (IllegalOrphanException e) {
      // expected: log in old logList not in new → orphan
    }

    logCtrl.destroy(logId);
    dataCtrl.destroy(d.getId());
    userCtrl.destroy(user.getId());
  }

  /**
   * Edit data D (which has an addAttr where addAttr.xincoCoreData=D) with an empty addAttrList.
   * Covers edit() lines 316-322: addAttr in old addAttrList not in new → if(null)=true → add().
   */
  public void testData_editOrphanCheckAddAttrList() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoAddAttributeJpaController aaCtrl =
        new XincoAddAttributeJpaController(getEntityManagerFactory());

    XincoCoreData d = buildData(dataCtrl, "test.edit.orphan.addattrlist.d");

    XincoAddAttribute addAttr = new XincoAddAttribute();
    XincoAddAttributePK attrPK = new XincoAddAttributePK();
    attrPK.setAttributeId(1);
    addAttr.setXincoAddAttributePK(attrPK);
    addAttr.setXincoCoreData(dataCtrl.findXincoCoreData(d.getId()));
    addAttr.setAttribVarchar("test");
    addAttr.setAttribInt(0);
    addAttr.setAttribUnsignedint(0);
    addAttr.setAttribDouble(0.0);
    aaCtrl.create(addAttr);
    XincoAddAttributePK createdPK = addAttr.getXincoAddAttributePK();

    XincoCoreData toEdit = dataCtrl.findXincoCoreData(d.getId());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    try {
      dataCtrl.edit(toEdit);
      fail("Expected IllegalOrphanException for addAttrList orphan check");
    } catch (IllegalOrphanException e) {
      // expected: addAttr in old addAttrList not in new → orphan
    }

    aaCtrl.destroy(createdPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(d.getId());
  }

  // -------------------------------------------------------------------------
  // XincoCoreDataJpaController.destroy() orphan-check loop bodies
  // -------------------------------------------------------------------------

  /**
   * Destroy data D that has a log (log.xincoCoreData=D). Covers destroy() lines 595-603: log in
   * xincoCoreLogListOrphanCheck → if(null)=true → new ArrayList() → add().
   */
  public void testData_destroyOrphanCheckLogList() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());

    XincoCoreData d = buildData(dataCtrl, "test.destroy.orphan.loglist.d");
    XincoCoreUser user = buildUser(userCtrl, "destroy.orphan.loglist");

    XincoCoreLog log = new XincoCoreLog();
    log.setOpCode(1);
    log.setOpDatetime(new Date());
    log.setOpDescription("destroy logList orphan-check test");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("");
    log.setXincoCoreData(dataCtrl.findXincoCoreData(d.getId()));
    log.setXincoCoreUser(userCtrl.findXincoCoreUser(user.getId()));
    logCtrl.create(log);
    int logId = log.getId();

    try {
      dataCtrl.destroy(d.getId());
      fail("Expected IllegalOrphanException for logList orphan check in destroy");
    } catch (IllegalOrphanException e) {
      // expected: log in D's logList → D cannot be destroyed
    }

    logCtrl.destroy(logId);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(d.getId());
    userCtrl.destroy(user.getId());
  }

  /**
   * Destroy data D that has an addAttr (addAttr.xincoCoreData=D). Covers destroy() lines 607-617:
   * addAttr in xincoAddAttributeListOrphanCheck → if(null)=true → new ArrayList() → add().
   */
  public void testData_destroyOrphanCheckAddAttrList() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoAddAttributeJpaController aaCtrl =
        new XincoAddAttributeJpaController(getEntityManagerFactory());

    XincoCoreData d = buildData(dataCtrl, "test.destroy.orphan.addattrlist.d");

    XincoAddAttribute addAttr = new XincoAddAttribute();
    XincoAddAttributePK attrPK = new XincoAddAttributePK();
    attrPK.setAttributeId(1);
    addAttr.setXincoAddAttributePK(attrPK);
    addAttr.setXincoCoreData(dataCtrl.findXincoCoreData(d.getId()));
    addAttr.setAttribVarchar("destroy-test");
    addAttr.setAttribInt(0);
    addAttr.setAttribUnsignedint(0);
    addAttr.setAttribDouble(0.0);
    aaCtrl.create(addAttr);
    XincoAddAttributePK createdPK = addAttr.getXincoAddAttributePK();

    try {
      dataCtrl.destroy(d.getId());
      fail("Expected IllegalOrphanException for addAttrList orphan check in destroy");
    } catch (IllegalOrphanException e) {
      // expected: addAttr in D's addAttrList → D cannot be destroyed
    }

    aaCtrl.destroy(createdPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(d.getId());
  }

  // -------------------------------------------------------------------------
  // destroy() EntityNotFoundException catches for controllers lacking tests
  // -------------------------------------------------------------------------

  /** Covers XincoCoreAceJpaController destroy() EntityNotFoundException catch (~8 instr). */
  public void testAce_destroyNonExistent() {
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    try {
      aceCtrl.destroy(99999);
      fail("Expected exception for non-existent ACE id");
    } catch (Exception e) {
      // expected
    }
  }

  /**
   * Covers XincoCoreDataTypeAttributeJpaController destroy() EntityNotFoundException catch (~9
   * instr).
   */
  public void testDta_destroyNonExistent() {
    XincoCoreDataTypeAttributeJpaController dtaCtrl =
        new XincoCoreDataTypeAttributeJpaController(getEntityManagerFactory());
    XincoCoreDataTypeAttributePK fakePK = new XincoCoreDataTypeAttributePK();
    fakePK.setXincoCoreDataTypeId(99999);
    fakePK.setAttributeId(99999);
    try {
      dtaCtrl.destroy(fakePK);
      fail("Expected exception for non-existent DataTypeAttribute pk");
    } catch (Exception e) {
      // expected
    }
  }

  /**
   * Covers XincoCoreUserModifiedRecordJpaController destroy() EntityNotFoundException catch (~9
   * instr).
   */
  public void testUmr_destroyNonExistent() {
    XincoCoreUserModifiedRecordJpaController umrCtrl =
        new XincoCoreUserModifiedRecordJpaController(getEntityManagerFactory());
    XincoCoreUserModifiedRecordPK fakePK = new XincoCoreUserModifiedRecordPK();
    fakePK.setId(99999);
    fakePK.setRecordId(99999);
    try {
      umrCtrl.destroy(fakePK);
      fail("Expected exception for non-existent UMR pk");
    } catch (Exception e) {
      // expected
    }
  }

  /**
   * Covers XincoCoreDataHasDependencyJpaController destroy() EntityNotFoundException catch (~9
   * instr).
   */
  public void testDep_destroyNonExistent() {
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyPK fakePK = new XincoCoreDataHasDependencyPK();
    fakePK.setXincoCoreDataParentId(99999);
    fakePK.setXincoCoreDataChildrenId(99999);
    fakePK.setDependencyTypeId(99999);
    try {
      depCtrl.destroy(fakePK);
      fail("Expected exception for non-existent Dep pk");
    } catch (Exception e) {
      // expected
    }
  }

  /** Covers XincoCoreGroupJpaController destroy() EntityNotFoundException catch (~8 instr). */
  public void testGroup_destroyNonExistent() {
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    try {
      groupCtrl.destroy(99999);
      fail("Expected exception for non-existent Group id");
    } catch (Exception e) {
      // expected
    }
  }

  /** Covers XincoCoreDataTypeJpaController destroy() EntityNotFoundException catch (~8 instr). */
  public void testDataType_destroyNonExistent() {
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    try {
      dtCtrl.destroy(99999);
      fail("Expected exception for non-existent DataType id");
    } catch (Exception e) {
      // expected
    }
  }

  /**
   * Covers XincoDependencyBehaviorJpaController destroy() EntityNotFoundException catch (~8 instr).
   */
  public void testDepBehavior_destroyNonExistent() {
    XincoDependencyBehaviorJpaController depBehavCtrl =
        new XincoDependencyBehaviorJpaController(getEntityManagerFactory());
    try {
      depBehavCtrl.destroy(99999);
      fail("Expected exception for non-existent DepBehavior id");
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
    user.setUsername("usr4." + suffix);
    user.setUserpassword("pw4_" + suffix);
    user.setLastName("Last");
    user.setFirstName("First");
    user.setEmail(suffix + "@ex4.com");
    user.setStatusNumber(1);
    user.setAttempts(0);
    user.setLastModified(new Date());
    user.setXincoCoreUserModifiedRecordList(new ArrayList<>());
    user.setXincoCoreAceList(new ArrayList<>());
    user.setXincoCoreLogList(new ArrayList<>());
    user.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    ctrl.create(user);
    return user;
  }
}
