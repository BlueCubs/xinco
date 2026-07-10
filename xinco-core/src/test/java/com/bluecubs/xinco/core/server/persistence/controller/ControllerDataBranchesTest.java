package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.server.persistence.XincoAddAttributePK;
import com.bluecubs.xinco.core.server.persistence.XincoCoreAce;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependencyPK;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataType;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLog;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNode;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Covers FK-change inner-IFs in XincoCoreDataJpaController.edit() and all four orphan-check loops
 * in destroy() — the top remaining coverage gap after session 3.
 */
public class ControllerDataBranchesTest extends AbstractXincoDataBaseTestCase {

  public ControllerDataBranchesTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerDataBranchesTest.class);
  }

  // ─── edit() DataType / Language / Node FK-change IFs ─────────────────────

  /**
   * Edit data to change its DataType from the seed DT (id=1) to a new DT2. Covers Data.edit() lines
   * ~404-411: xincoCoreDataTypeOld != new → both IFs run.
   */
  public void testData_editChangesDataType() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());

    XincoCoreData data = buildData(dataCtrl, "test.data.dt.change", 1, 1);
    int dataId = data.getId();

    XincoCoreDataType dt2 = new XincoCoreDataType();
    dt2.setDesignation("test.dt.for.data.edit");
    dt2.setDescription("test");
    dt2.setXincoCoreDataList(new ArrayList<>());
    dt2.setXincoCoreDataTypeAttributeList(new ArrayList<>());
    dtCtrl.create(dt2);
    int dt2Id = dt2.getId();

    // Edit data: change DT from seed(1) to DT2 — both FK-change IFs fire
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(dataId);
    toEdit.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(dt2Id));
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.edit(toEdit);

    // Cleanup: destroy data first (removes data from DT2's list), then DT2
    dataCtrl.destroy(dataId);
    dtCtrl.destroy(dt2Id);
  }

  /**
   * Edit data to change its Language from the seed lang (id=1) to a new Lang2. Covers Data.edit()
   * lines ~412-419: xincoCoreLanguageOld != new → both IFs run.
   */
  public void testData_editChangesLanguage() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreData data = buildData(dataCtrl, "test.data.lang.change", 1, 1);
    int dataId = data.getId();

    XincoCoreLanguage lang2 = new XincoCoreLanguage();
    lang2.setSign("ld2");
    lang2.setDesignation("TestLangDataEdit");
    lang2.setXincoCoreNodeList(new ArrayList<>());
    lang2.setXincoCoreDataList(new ArrayList<>());
    langCtrl.create(lang2);
    int lang2Id = lang2.getId();

    XincoCoreData toEdit = dataCtrl.findXincoCoreData(dataId);
    toEdit.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(lang2Id));
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.edit(toEdit);

    dataCtrl.destroy(dataId);
    langCtrl.destroy(lang2Id);
  }

  /**
   * Edit data to change its Node from the seed node (id=1) to a new Node2. Covers Data.edit() lines
   * ~420-427: xincoCoreNodeOld != new → both IFs run.
   */
  public void testData_editChangesNode() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreData data = buildData(dataCtrl, "test.data.node.change", 1, 1);
    int dataId = data.getId();

    XincoCoreNode node2 = new XincoCoreNode();
    node2.setDesignation("TestNodeForDataEdit");
    node2.setStatusNumber(1);
    node2.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    node2.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    node2.setXincoCoreNodeList(new ArrayList<>());
    node2.setXincoCoreAceList(new ArrayList<>());
    node2.setXincoCoreDataList(new ArrayList<>());
    nodeCtrl.create(node2);
    int node2Id = node2.getId();

    XincoCoreData toEdit = dataCtrl.findXincoCoreData(dataId);
    toEdit.setXincoCoreNode(nodeCtrl.findXincoCoreNode(node2Id));
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.edit(toEdit);

    dataCtrl.destroy(dataId);
    nodeCtrl.destroy(node2Id);
  }

  // ─── edit() DataHasDependency FK-change inner IFs ────────────────────────

  /**
   * Edit D2 adding a dep (from D1 as child) to its xincoCoreDataHasDependencyList. Covers
   * Data.edit() lines ~428-449: dep not in D2's old list, dep.xincoCoreData (old child) = D1 !=
   * null and != D2 → inner IF runs.
   */
  public void testData_editMoveDepChildFromOtherData() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController dtypeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    XincoCoreData d1 = buildData(dataCtrl, "test.dep.edit.child.d1", 1, 1);
    XincoCoreData dParent = buildData(dataCtrl, "test.dep.edit.child.parent", 1, 1);
    XincoCoreData d2 = buildData(dataCtrl, "test.dep.edit.child.d2", 1, 1);

    // Create dep: d1=child, dParent=parent
    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(d1.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(dParent.getId()));
    dep.setXincoDependencyType(dtypeCtrl.findXincoDependencyType(1));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    // Edit D2 with dep (from D1) in its dep-child list: inner IF runs
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(d2.getId());
    toEdit.setXincoCoreDataHasDependencyList(
        Arrays.asList(depCtrl.findXincoCoreDataHasDependency(depPK)));
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.edit(toEdit);

    // Dep FK is non-updatable; destroy dep then clear full L2 cache before destroying D2
    depCtrl.destroy(depPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(d2.getId());
    dataCtrl.destroy(d1.getId());
    dataCtrl.destroy(dParent.getId());
  }

  /**
   * Edit D2 adding a dep (from D1 as parent) to its xincoCoreDataHasDependencyList1. Covers
   * Data.edit() lines ~450-473: dep not in D2's old list, dep.xincoCoreData1 (old parent) = D1 !=
   * null and != D2 → inner IF runs.
   */
  public void testData_editMoveDepParentFromOtherData() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController dtypeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    XincoCoreData dChild = buildData(dataCtrl, "test.dep.edit.parent.child", 1, 1);
    XincoCoreData d1 = buildData(dataCtrl, "test.dep.edit.parent.d1", 1, 1);
    XincoCoreData d2 = buildData(dataCtrl, "test.dep.edit.parent.d2", 1, 1);

    // Create dep: dChild=child, d1=parent
    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(dChild.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(d1.getId()));
    dep.setXincoDependencyType(dtypeCtrl.findXincoDependencyType(1));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    // Edit D2 with dep (from D1) in its dep-parent list: inner IF runs
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(d2.getId());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(
        Arrays.asList(depCtrl.findXincoCoreDataHasDependency(depPK)));
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.edit(toEdit);

    // Dep FK is non-updatable; destroy dep then clear full L2 cache before destroying D2
    depCtrl.destroy(depPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(d2.getId());
    dataCtrl.destroy(d1.getId());
    dataCtrl.destroy(dChild.getId());
  }

  // ─── edit() ACE FK-change ─────────────────────────────────────────────────

  /**
   * Edit data removing its ACE. Covers Data.edit() lines ~475-479: ace not in new list →
   * ace.setXincoCoreData(null).
   */
  public void testData_editRemoveAce() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());

    XincoCoreData data = buildData(dataCtrl, "test.data.ace.remove", 1, 1);
    int dataId = data.getId();

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreData(dataCtrl.findXincoCoreData(dataId));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Edit data with empty ACE list → ace.setXincoCoreData(null) runs
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(dataId);
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.edit(toEdit);

    aceCtrl.destroy(aceId);
    dataCtrl.destroy(dataId);
  }

  /**
   * Edit D2 adding an ACE (from D1) to its list. Covers Data.edit() lines ~481-496: ace not in D2's
   * old list, ace.xincoCoreData (old) = D1 != null and != D2 → inner IF runs.
   */
  public void testData_editMoveAceFromOtherData() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());

    XincoCoreData d1 = buildData(dataCtrl, "test.data.ace.move.src", 1, 1);
    XincoCoreData d2 = buildData(dataCtrl, "test.data.ace.move.dst", 1, 1);

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreData(dataCtrl.findXincoCoreData(d1.getId()));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Edit D2 with ACE from D1: inner IF runs
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(d2.getId());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoCoreAceList(Arrays.asList(aceCtrl.findXincoCoreAce(aceId)));
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.edit(toEdit);

    aceCtrl.destroy(aceId);
    dataCtrl.destroy(d2.getId());
    dataCtrl.destroy(d1.getId());
  }

  // ─── edit() Log FK-change ─────────────────────────────────────────────────

  /**
   * Edit D2 adding a log (from D1) to its list. Covers Data.edit() lines ~497-512: log not in D2's
   * old list, log.xincoCoreData = D1 != null and != D2 → inner IF runs.
   */
  public void testData_editMoveLogFromOtherData() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());

    XincoCoreData d1 = buildData(dataCtrl, "test.data.log.move.src", 1, 1);
    XincoCoreData d2 = buildData(dataCtrl, "test.data.log.move.dst", 1, 1);
    XincoCoreUser user = buildUser(userCtrl, "data.log.move.user");

    XincoCoreLog log = new XincoCoreLog();
    log.setOpCode(1);
    log.setOpDatetime(new Date());
    log.setOpDescription("log for data move test");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("");
    log.setXincoCoreData(dataCtrl.findXincoCoreData(d1.getId()));
    log.setXincoCoreUser(userCtrl.findXincoCoreUser(user.getId()));
    logCtrl.create(log);
    int logId = log.getId();

    // Edit D2 with log from D1: log.xincoCoreData = D1 != D2 → inner IF runs
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(d2.getId());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(Arrays.asList(logCtrl.findXincoCoreLog(logId)));
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.edit(toEdit);

    // Log FK is updatable: log now belongs to D2; destroy log before D2
    logCtrl.destroy(logId);
    dataCtrl.destroy(d2.getId());
    dataCtrl.destroy(d1.getId());
    userCtrl.destroy(user.getId());
  }

  // ─── edit() AddAttribute FK-change ───────────────────────────────────────

  /**
   * Edit D2 adding an AddAttribute (from D1) to its list. Covers Data.edit() lines ~513-530:
   * addattr not in D2's old list, addattr.xincoCoreData = D1 != null and != D2 → inner IF runs.
   */
  public void testData_editMoveAddAttrFromOtherData() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoAddAttributeJpaController addAttrCtrl =
        new XincoAddAttributeJpaController(getEntityManagerFactory());

    XincoCoreData d1 = buildData(dataCtrl, "test.data.addattr.move.src", 1, 1);
    XincoCoreData d2 = buildData(dataCtrl, "test.data.addattr.move.dst", 1, 1);

    // Create addattr linked to D1 (attributeId=1, xincoCoreDataId=d1.getId() auto-set)
    XincoAddAttribute addAttr = new XincoAddAttribute();
    XincoAddAttributePK attrPK = new XincoAddAttributePK();
    attrPK.setAttributeId(1);
    addAttr.setXincoAddAttributePK(attrPK);
    addAttr.setXincoCoreData(dataCtrl.findXincoCoreData(d1.getId()));
    addAttr.setAttribVarchar("test");
    addAttr.setAttribInt(0);
    addAttr.setAttribUnsignedint(0);
    addAttr.setAttribDouble(0.0);
    addAttrCtrl.create(addAttr);
    XincoAddAttributePK createdPK = addAttr.getXincoAddAttributePK();

    // Edit D2 with addattr from D1: inner IF runs
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(d2.getId());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoAddAttributeList(Arrays.asList(addAttrCtrl.findXincoAddAttribute(createdPK)));
    dataCtrl.edit(toEdit);

    // AddAttr FK is non-updatable; destroy addattr then clear full L2 cache before destroying D2
    addAttrCtrl.destroy(createdPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(d2.getId());
    dataCtrl.destroy(d1.getId());
  }

  // ─── destroy() orphan-check loops ────────────────────────────────────────

  /**
   * Destroy data that is the CHILD in a dep relationship. Covers Data.destroy() lines ~562-577:
   * xincoCoreDataHasDependencyList is non-empty → IllegalOrphanException thrown.
   */
  public void testData_destroyOrphanCheckDepChild() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController dtypeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    XincoCoreData dParent = buildData(dataCtrl, "test.dep.orphan.child.parent", 1, 1);
    XincoCoreData dChild = buildData(dataCtrl, "test.dep.orphan.child.child", 1, 1);

    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(dChild.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(dParent.getId()));
    dep.setXincoDependencyType(dtypeCtrl.findXincoDependencyType(1));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    try {
      dataCtrl.destroy(dChild.getId()); // dep in child's list → orphan check fires
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    depCtrl.destroy(depPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(dChild.getId());
    dataCtrl.destroy(dParent.getId());
  }

  /**
   * Destroy data that is the PARENT in a dep relationship. Covers Data.destroy() lines ~578-592:
   * xincoCoreDataHasDependencyList1 is non-empty → IllegalOrphanException thrown.
   */
  public void testData_destroyOrphanCheckDepParent() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController dtypeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    XincoCoreData dChild = buildData(dataCtrl, "test.dep.orphan.parent.child", 1, 1);
    XincoCoreData dParent = buildData(dataCtrl, "test.dep.orphan.parent.parent", 1, 1);

    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(dChild.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(dParent.getId()));
    dep.setXincoDependencyType(dtypeCtrl.findXincoDependencyType(1));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    try {
      dataCtrl.destroy(dParent.getId()); // dep in parent's list → orphan check fires
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    depCtrl.destroy(depPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(dParent.getId());
    dataCtrl.destroy(dChild.getId());
  }

  /**
   * Destroy data that has a log. Covers Data.destroy() lines ~593-604: xincoCoreLogList is
   * non-empty → IllegalOrphanException thrown.
   */
  public void testData_destroyOrphanCheckLog() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());

    XincoCoreData data = buildData(dataCtrl, "test.data.log.orphan", 1, 1);
    int dataId = data.getId();
    XincoCoreUser user = buildUser(userCtrl, "data.log.orphan.user");

    XincoCoreLog log = new XincoCoreLog();
    log.setOpCode(1);
    log.setOpDatetime(new Date());
    log.setOpDescription("orphan check log");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("");
    log.setXincoCoreData(dataCtrl.findXincoCoreData(dataId));
    log.setXincoCoreUser(userCtrl.findXincoCoreUser(user.getId()));
    logCtrl.create(log);
    int logId = log.getId();

    try {
      dataCtrl.destroy(dataId); // data has log → orphan check fires
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    logCtrl.destroy(logId);
    dataCtrl.destroy(dataId);
    userCtrl.destroy(user.getId());
  }

  /**
   * Destroy data that has an AddAttribute. Covers Data.destroy() lines ~605-618:
   * xincoAddAttributeList is non-empty → IllegalOrphanException thrown.
   */
  public void testData_destroyOrphanCheckAddAttr() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoAddAttributeJpaController addAttrCtrl =
        new XincoAddAttributeJpaController(getEntityManagerFactory());

    XincoCoreData data = buildData(dataCtrl, "test.data.addattr.orphan", 1, 1);
    int dataId = data.getId();

    XincoAddAttribute addAttr = new XincoAddAttribute();
    XincoAddAttributePK attrPK = new XincoAddAttributePK();
    attrPK.setAttributeId(1);
    addAttr.setXincoAddAttributePK(attrPK);
    addAttr.setXincoCoreData(dataCtrl.findXincoCoreData(dataId));
    addAttr.setAttribVarchar("orphan test");
    addAttr.setAttribInt(0);
    addAttr.setAttribUnsignedint(0);
    addAttr.setAttribDouble(0.0);
    addAttrCtrl.create(addAttr);
    XincoAddAttributePK createdPK = addAttr.getXincoAddAttributePK();

    try {
      dataCtrl.destroy(dataId); // data has addattr → orphan check fires
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    addAttrCtrl.destroy(createdPK);
    dataCtrl.destroy(dataId);
  }

  // ─── Helpers ─────────────────────────────────────────────────────────────

  private XincoCoreData buildData(
      XincoCoreDataJpaController ctrl, String designation, int langId, int dtId) throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreData data = new XincoCoreData();
    data.setDesignation(designation);
    data.setStatusNumber(1);
    data.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(langId));
    data.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(dtId));
    data.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    data.setXincoCoreAceList(new ArrayList<>());
    data.setXincoCoreLogList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    data.setXincoAddAttributeList(new ArrayList<>());
    ctrl.create(data);
    return data;
  }

  private XincoCoreUser buildUser(XincoCoreUserJpaController ctrl, String suffix) throws Exception {
    XincoCoreUser user = new XincoCoreUser();
    user.setUsername("user." + suffix);
    user.setUserpassword("pw_" + suffix);
    user.setLastName("Last");
    user.setFirstName("First");
    user.setEmail(suffix + "@example.com");
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
