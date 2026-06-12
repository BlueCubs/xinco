package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import java.util.ArrayList;
import java.util.Arrays;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Covers Language and DataType FK-change branches (create/edit update-refs inner IFs) and
 * orphan-check paths not yet exercised by prior test files.
 */
public class ControllerLanguageDataTypeTest extends AbstractXincoDataBaseTestCase {

  public ControllerLanguageDataTypeTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerLanguageDataTypeTest.class);
  }

  // ─── Language: create with populated lists ──────────────────────────────────

  /**
   * Language.create() with node in list. Covers create() update-refs loop lines ~92-104: node's
   * oldLang (L1) != null → inner IF body runs.
   */
  public void testLanguage_createWithNodeInList() throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    XincoCoreNode node = buildNode(nodeCtrl, langCtrl, "TestNodeForLangCreate", 1, 1);
    int nodeId = node.getId();

    XincoCoreLanguage lNew = new XincoCoreLanguage();
    lNew.setSign("tc1");
    lNew.setDesignation("TestLangCreateWithNode");
    lNew.setXincoCoreNodeList(Arrays.asList(nodeCtrl.findXincoCoreNode(nodeId)));
    lNew.setXincoCoreDataList(new ArrayList<>());
    langCtrl.create(lNew);
    int lNewId = lNew.getId();
    assertTrue(lNewId > 0);

    nodeCtrl.destroy(nodeId);
    langCtrl.destroy(lNewId);
  }

  /**
   * Language.create() with data in list. Covers create() update-refs loop lines ~106-118: data's
   * oldLang (L1) != null → inner IF body runs.
   */
  public void testLanguage_createWithDataInList() throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoCoreData data = buildData(dataCtrl, langCtrl, "test.data.lang.create.list");
    int dataId = data.getId();

    XincoCoreLanguage lNew = new XincoCoreLanguage();
    lNew.setSign("tc2");
    lNew.setDesignation("TestLangCreateWithData");
    lNew.setXincoCoreNodeList(new ArrayList<>());
    lNew.setXincoCoreDataList(Arrays.asList(dataCtrl.findXincoCoreData(dataId)));
    langCtrl.create(lNew);
    int lNewId = lNew.getId();
    assertTrue(lNewId > 0);

    dataCtrl.destroy(dataId);
    langCtrl.destroy(lNewId);
  }

  // ─── Language: edit FK-change branches ─────────────────────────────────────

  /**
   * Language.edit() with node moved from L1. Covers edit() update-refs loop lines ~193-207: node
   * not in old list, oldLang (L1) != null and != lNew → inner IF runs.
   */
  public void testLanguage_editAddNodeFromOtherLanguage() throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    XincoCoreNode node = buildNode(nodeCtrl, langCtrl, "TestNodeForLangEdit", 1, 1);
    int nodeId = node.getId();

    XincoCoreLanguage lNew = buildLanguage(langCtrl, "te1", "TestLangEditAddNode");
    int lNewId = lNew.getId();

    XincoCoreLanguage toEdit = langCtrl.findXincoCoreLanguage(lNewId);
    toEdit.setXincoCoreNodeList(Arrays.asList(nodeCtrl.findXincoCoreNode(nodeId)));
    toEdit.setXincoCoreDataList(new ArrayList<>());
    langCtrl.edit(toEdit);

    nodeCtrl.destroy(nodeId);
    langCtrl.destroy(lNewId);
  }

  /**
   * Language.edit() with data moved from L1. Covers edit() update-refs loop lines ~210-225: data
   * not in old list, oldLang (L1) != null and != lNew → inner IF runs.
   */
  public void testLanguage_editAddDataFromOtherLanguage() throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoCoreData data = buildData(dataCtrl, langCtrl, "test.data.lang.edit.add");
    int dataId = data.getId();

    XincoCoreLanguage lNew = buildLanguage(langCtrl, "te2", "TestLangEditAddData");
    int lNewId = lNew.getId();

    XincoCoreLanguage toEdit = langCtrl.findXincoCoreLanguage(lNewId);
    toEdit.setXincoCoreNodeList(new ArrayList<>());
    toEdit.setXincoCoreDataList(Arrays.asList(dataCtrl.findXincoCoreData(dataId)));
    langCtrl.edit(toEdit);

    dataCtrl.destroy(dataId);
    langCtrl.destroy(lNewId);
  }

  // ─── Language: orphan checks ────────────────────────────────────────────────

  /**
   * Language.edit() removing node from its list. Covers orphan-check loop lines ~147-156: node in
   * old list but not new → IllegalOrphanException.
   */
  public void testLanguage_editOrphanCheckNode() throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    XincoCoreLanguage lTest = buildLanguage(langCtrl, "to1", "TestLangOrphanNode");
    int lTestId = lTest.getId();

    XincoCoreNode node = buildNode(nodeCtrl, langCtrl, "TestNodeOrphanLang", 1, lTestId);
    int nodeId = node.getId();

    XincoCoreLanguage toEdit = langCtrl.findXincoCoreLanguage(lTestId);
    toEdit.setXincoCoreNodeList(new ArrayList<>());
    toEdit.setXincoCoreDataList(new ArrayList<>());
    try {
      langCtrl.edit(toEdit);
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    nodeCtrl.destroy(nodeId);
    langCtrl.destroy(lTestId);
  }

  /**
   * Language.edit() removing data from its list. Covers orphan-check loop lines ~158-167: data in
   * old list but not new → IllegalOrphanException.
   */
  public void testLanguage_editOrphanCheckData() throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoCoreLanguage lTest = buildLanguage(langCtrl, "to2", "TestLangOrphanData");
    int lTestId = lTest.getId();

    XincoCoreData data = buildData(dataCtrl, langCtrl, "test.data.lang.orphan.edit", lTestId, 1);
    int dataId = data.getId();

    XincoCoreLanguage toEdit = langCtrl.findXincoCoreLanguage(lTestId);
    toEdit.setXincoCoreNodeList(new ArrayList<>());
    toEdit.setXincoCoreDataList(new ArrayList<>());
    try {
      langCtrl.edit(toEdit);
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    dataCtrl.destroy(dataId);
    langCtrl.destroy(lTestId);
  }

  /**
   * Language.destroy() with node still linked. Covers destroy() orphan-check loop lines ~259-270 →
   * IllegalOrphanException.
   */
  public void testLanguage_destroyOrphanCheckWithNode() throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    XincoCoreLanguage lTest = buildLanguage(langCtrl, "td1", "TestLangDestroyWithNode");
    int lTestId = lTest.getId();

    XincoCoreNode node = buildNode(nodeCtrl, langCtrl, "TestNodeForLangDestroy", 1, lTestId);
    int nodeId = node.getId();

    try {
      langCtrl.destroy(lTestId);
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    nodeCtrl.destroy(nodeId);
    langCtrl.destroy(lTestId);
  }

  /**
   * Language.destroy() with data still linked. Covers destroy() orphan-check loop lines ~271-282 →
   * IllegalOrphanException.
   */
  public void testLanguage_destroyOrphanCheckWithData() throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoCoreLanguage lTest = buildLanguage(langCtrl, "td2", "TestLangDestroyWithData");
    int lTestId = lTest.getId();

    XincoCoreData data = buildData(dataCtrl, langCtrl, "test.data.lang.destroy.orphan", lTestId, 1);
    int dataId = data.getId();

    try {
      langCtrl.destroy(lTestId);
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    dataCtrl.destroy(dataId);
    langCtrl.destroy(lTestId);
  }

  // ─── DataType: create/edit with attribute list ──────────────────────────────

  /**
   * DataType.create() with existing attribute in list. Covers create() update-refs loop: attr's
   * oldDt (dt1) != null → inner IF body runs (attr FK is insertable=false, no actual DB change).
   */
  public void testDataType_createWithAttrInList() throws Exception {
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreDataTypeAttributeJpaController attrCtrl =
        new XincoCoreDataTypeAttributeJpaController(getEntityManagerFactory());

    XincoCoreDataType dt1 = buildDataType(dtCtrl, "test.dt.create.attr.src");
    int dt1Id = dt1.getId();

    XincoCoreDataTypeAttribute attr = new XincoCoreDataTypeAttribute();
    attr.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(dt1Id));
    XincoCoreDataTypeAttributePK attrPK = new XincoCoreDataTypeAttributePK();
    attrPK.setAttributeId(9991);
    attr.setXincoCoreDataTypeAttributePK(attrPK);
    attr.setDesignation("test.attr.9991");
    attr.setDataType("varchar");
    attr.setAttrSize(255);
    attrCtrl.create(attr);
    XincoCoreDataTypeAttributePK createdPK = attr.getXincoCoreDataTypeAttributePK();

    XincoCoreDataType dt2 = new XincoCoreDataType();
    dt2.setDesignation("test.dt.create.attr.dst");
    dt2.setDescription("test");
    dt2.setXincoCoreDataTypeAttributeList(
        Arrays.asList(attrCtrl.findXincoCoreDataTypeAttribute(createdPK)));
    dt2.setXincoCoreDataList(new ArrayList<>());
    dtCtrl.create(dt2);
    int dt2Id = dt2.getId();
    assertTrue(dt2Id > 0);

    // Destroy attr first; then evict DT2 from L2 cache so destroy() reads from DB (empty list)
    attrCtrl.destroy(createdPK);
    getEntityManagerFactory().getCache().evict(XincoCoreDataType.class, dt2Id);
    dtCtrl.destroy(dt2Id);
    dtCtrl.destroy(dt1Id);
  }

  /**
   * DataType.edit() with attribute moved from another DataType. Covers edit() update-refs loop
   * lines ~211-234: !old.contains(attr) → body runs, oldDt != dt2 → inner IF runs.
   */
  public void testDataType_editAddAttrFromOtherDataType() throws Exception {
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreDataTypeAttributeJpaController attrCtrl =
        new XincoCoreDataTypeAttributeJpaController(getEntityManagerFactory());

    XincoCoreDataType dt1 = buildDataType(dtCtrl, "test.dt.edit.attr.src");
    int dt1Id = dt1.getId();
    XincoCoreDataType dt2 = buildDataType(dtCtrl, "test.dt.edit.attr.dst");
    int dt2Id = dt2.getId();

    XincoCoreDataTypeAttribute attr = new XincoCoreDataTypeAttribute();
    attr.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(dt1Id));
    XincoCoreDataTypeAttributePK attrPK = new XincoCoreDataTypeAttributePK();
    attrPK.setAttributeId(9992);
    attr.setXincoCoreDataTypeAttributePK(attrPK);
    attr.setDesignation("test.attr.9992");
    attr.setDataType("varchar");
    attr.setAttrSize(255);
    attrCtrl.create(attr);
    XincoCoreDataTypeAttributePK createdPK = attr.getXincoCoreDataTypeAttributePK();

    XincoCoreDataType toEdit = dtCtrl.findXincoCoreDataType(dt2Id);
    toEdit.setXincoCoreDataTypeAttributeList(
        Arrays.asList(attrCtrl.findXincoCoreDataTypeAttribute(createdPK)));
    toEdit.setXincoCoreDataList(new ArrayList<>());
    dtCtrl.edit(toEdit);

    // Destroy attr first; then evict DT2 from L2 cache so destroy() reads from DB (empty list)
    attrCtrl.destroy(createdPK);
    getEntityManagerFactory().getCache().evict(XincoCoreDataType.class, dt2Id);
    dtCtrl.destroy(dt2Id);
    dtCtrl.destroy(dt1Id);
  }

  /**
   * DataType.edit() with data moved from another DataType. Covers edit() update-refs loop lines
   * ~236-251: !old.contains(data) → body, oldDt != dt2 → inner IF runs (data FK is updatable).
   */
  public void testDataType_editAddDataFromOtherDataType() throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoCoreDataType dt1 = buildDataType(dtCtrl, "test.dt.edit.data.src");
    int dt1Id = dt1.getId();
    XincoCoreDataType dt2 = buildDataType(dtCtrl, "test.dt.edit.data.dst");
    int dt2Id = dt2.getId();

    XincoCoreData data = buildData(dataCtrl, langCtrl, "test.data.dt.edit.move", 1, dt1Id);
    int dataId = data.getId();

    XincoCoreDataType toEdit = dtCtrl.findXincoCoreDataType(dt2Id);
    toEdit.setXincoCoreDataTypeAttributeList(new ArrayList<>());
    toEdit.setXincoCoreDataList(Arrays.asList(dataCtrl.findXincoCoreData(dataId)));
    dtCtrl.edit(toEdit);

    dataCtrl.destroy(dataId);
    dtCtrl.destroy(dt2Id);
    dtCtrl.destroy(dt1Id);
  }

  // ─── DataType: orphan checks ────────────────────────────────────────────────

  /**
   * DataType.edit() removing attribute from its list. Covers orphan-check loop lines ~163-173: attr
   * in old list but not new → IllegalOrphanException.
   */
  public void testDataType_editOrphanCheckAttr() throws Exception {
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreDataTypeAttributeJpaController attrCtrl =
        new XincoCoreDataTypeAttributeJpaController(getEntityManagerFactory());

    XincoCoreDataType dtTest = buildDataType(dtCtrl, "test.dt.orphan.check.attr");
    int dtTestId = dtTest.getId();

    XincoCoreDataTypeAttribute attr = new XincoCoreDataTypeAttribute();
    attr.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(dtTestId));
    XincoCoreDataTypeAttributePK attrPK = new XincoCoreDataTypeAttributePK();
    attrPK.setAttributeId(9993);
    attr.setXincoCoreDataTypeAttributePK(attrPK);
    attr.setDesignation("test.attr.9993");
    attr.setDataType("varchar");
    attr.setAttrSize(255);
    attrCtrl.create(attr);
    XincoCoreDataTypeAttributePK createdPK = attr.getXincoCoreDataTypeAttributePK();

    XincoCoreDataType toEdit = dtCtrl.findXincoCoreDataType(dtTestId);
    toEdit.setXincoCoreDataTypeAttributeList(new ArrayList<>());
    toEdit.setXincoCoreDataList(new ArrayList<>());
    try {
      dtCtrl.edit(toEdit);
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    attrCtrl.destroy(createdPK);
    dtCtrl.destroy(dtTestId);
  }

  /**
   * DataType.edit() removing data from its list. Covers orphan-check loop lines ~175-186: data in
   * old list but not new → IllegalOrphanException.
   */
  public void testDataType_editOrphanCheckData() throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoCoreDataType dtTest = buildDataType(dtCtrl, "test.dt.orphan.check.data");
    int dtTestId = dtTest.getId();

    XincoCoreData data = buildData(dataCtrl, langCtrl, "test.data.dt.orphan.edit", 1, dtTestId);
    int dataId = data.getId();

    XincoCoreDataType toEdit = dtCtrl.findXincoCoreDataType(dtTestId);
    toEdit.setXincoCoreDataTypeAttributeList(new ArrayList<>());
    toEdit.setXincoCoreDataList(new ArrayList<>());
    try {
      dtCtrl.edit(toEdit);
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    dataCtrl.destroy(dataId);
    dtCtrl.destroy(dtTestId);
  }

  /**
   * DataType.destroy() with seed attribute (DT1 has seed attributes). Covers destroy() orphan-check
   * loop lines ~284-299 → IllegalOrphanException. DT1 is NOT deleted.
   */
  public void testDataType_destroyOrphanCheckWithAttr() {
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    try {
      dtCtrl.destroy(1);
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected — DT1 has seed attributes
    } catch (Exception e) {
      fail("Expected IllegalOrphanException but got: " + e.getClass().getSimpleName());
    }
  }

  /**
   * DataType.destroy() with data still linked. Covers destroy() orphan-check loop lines ~300-311 →
   * IllegalOrphanException.
   */
  public void testDataType_destroyOrphanCheckWithData() throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());

    XincoCoreDataType dtTest = buildDataType(dtCtrl, "test.dt.destroy.with.data");
    int dtTestId = dtTest.getId();

    XincoCoreData data = buildData(dataCtrl, langCtrl, "test.data.dt.destroy.orphan", 1, dtTestId);
    int dataId = data.getId();

    try {
      dtCtrl.destroy(dtTestId);
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    dataCtrl.destroy(dataId);
    dtCtrl.destroy(dtTestId);
  }

  // ─── Helpers ────────────────────────────────────────────────────────────────

  private XincoCoreLanguage buildLanguage(
      XincoCoreLanguageJpaController ctrl, String sign, String designation) throws Exception {
    XincoCoreLanguage lang = new XincoCoreLanguage();
    lang.setSign(sign);
    lang.setDesignation(designation);
    lang.setXincoCoreNodeList(new ArrayList<>());
    lang.setXincoCoreDataList(new ArrayList<>());
    ctrl.create(lang);
    return lang;
  }

  private XincoCoreNode buildNode(
      XincoCoreNodeJpaController nodeCtrl,
      XincoCoreLanguageJpaController langCtrl,
      String designation,
      int parentId,
      int langId)
      throws Exception {
    XincoCoreNode node = new XincoCoreNode();
    node.setDesignation(designation);
    node.setStatusNumber(1);
    node.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(langId));
    node.setXincoCoreNode(nodeCtrl.findXincoCoreNode(parentId));
    node.setXincoCoreNodeList(new ArrayList<>());
    node.setXincoCoreAceList(new ArrayList<>());
    node.setXincoCoreDataList(new ArrayList<>());
    nodeCtrl.create(node);
    return node;
  }

  private XincoCoreDataType buildDataType(XincoCoreDataTypeJpaController ctrl, String designation)
      throws Exception {
    XincoCoreDataType dt = new XincoCoreDataType();
    dt.setDesignation(designation);
    dt.setDescription("test");
    dt.setXincoCoreDataTypeAttributeList(new ArrayList<>());
    dt.setXincoCoreDataList(new ArrayList<>());
    ctrl.create(dt);
    return dt;
  }

  private XincoCoreData buildData(
      XincoCoreDataJpaController dataCtrl,
      XincoCoreLanguageJpaController langCtrl,
      String designation)
      throws Exception {
    return buildData(dataCtrl, langCtrl, designation, 1, 1);
  }

  private XincoCoreData buildData(
      XincoCoreDataJpaController dataCtrl,
      XincoCoreLanguageJpaController langCtrl,
      String designation,
      int langId,
      int dtId)
      throws Exception {
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
    dataCtrl.create(data);
    return data;
  }
}
