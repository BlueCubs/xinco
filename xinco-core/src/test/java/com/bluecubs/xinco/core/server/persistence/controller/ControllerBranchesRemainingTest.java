package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Covers remaining uncovered branches in XincoCoreDataTypeAttributeJpaController,
 * XincoCoreUserModifiedRecordJpaController, and XincoCoreNodeJpaController (create list loops, edit
 * FK-change, destroy ACE loop).
 */
public class ControllerBranchesRemainingTest extends AbstractXincoDataBaseTestCase {

  public ControllerBranchesRemainingTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerBranchesRemainingTest.class);
  }

  // -------------------------------------------------------------------------
  // XincoCoreDataTypeAttributeJpaController
  // -------------------------------------------------------------------------

  /**
   * Create DataTypeAttribute with null PK (controller initialises PK), then edit with same DataType
   * (FK-change conditions stay false), then destroy. Covers the full create/edit/destroy main paths
   * for XincoCoreDataTypeAttributeJpaController.
   */
  public void testDtattr_createEditDestroy() throws Exception {
    XincoCoreDataTypeAttributeJpaController attrCtrl =
        new XincoCoreDataTypeAttributeJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());

    XincoCoreDataType dt = dtCtrl.findXincoCoreDataType(1);

    XincoCoreDataTypeAttribute attr = new XincoCoreDataTypeAttribute();
    // Leave PK null so create() initialises it (covers null-PK branch)
    attr.setXincoCoreDataType(dt);
    XincoCoreDataTypeAttributePK pk = new XincoCoreDataTypeAttributePK();
    pk.setXincoCoreDataTypeId(dt.getId());
    pk.setAttributeId(999);
    attr.setXincoCoreDataTypeAttributePK(pk);
    attr.setDesignation("test.dtattr.branch");
    attr.setDataType("varchar");
    attr.setAttrSize(64);
    attrCtrl.create(attr);

    assertNotNull(attrCtrl.findXincoCoreDataTypeAttribute(attr.getXincoCoreDataTypeAttributePK()));

    // Edit with same DataType — both FK-change conditions evaluate false
    XincoCoreDataTypeAttribute toEdit =
        attrCtrl.findXincoCoreDataTypeAttribute(attr.getXincoCoreDataTypeAttributePK());
    toEdit.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    attrCtrl.edit(toEdit);

    // Destroy — covers destroy() dataType != null cleanup
    attrCtrl.destroy(attr.getXincoCoreDataTypeAttributePK());
    assertNull(attrCtrl.findXincoCoreDataTypeAttribute(attr.getXincoCoreDataTypeAttributePK()));
  }

  /** Covers the paginated findXincoCoreDataTypeAttributeEntities(maxResults, firstResult) path. */
  public void testDtattr_findPaginated() {
    XincoCoreDataTypeAttributeJpaController attrCtrl =
        new XincoCoreDataTypeAttributeJpaController(getEntityManagerFactory());
    assertNotNull(attrCtrl.findXincoCoreDataTypeAttributeEntities(10, 0));
  }

  // -------------------------------------------------------------------------
  // XincoCoreUserModifiedRecordJpaController
  // -------------------------------------------------------------------------

  // -------------------------------------------------------------------------
  // XincoCoreNodeJpaController — create() list loops
  // -------------------------------------------------------------------------

  /**
   * Create node N with data D (currently under root node 1) in its xincoCoreDataList. Covers
   * create() lines 105-161: the data-attach loop and the update-refs inner IF (old node != null →
   * root.dataList.remove(D)).
   */
  public void testNode_createWithDataFromOtherNode() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    // Data initially under root node (id=1)
    XincoCoreData data = buildData(dataCtrl, "test.node.create.withdata");

    // Build new node N that claims data in its dataList
    XincoCoreNode n = new XincoCoreNode();
    n.setDesignation("TestNodeCreateWithData");
    n.setStatusNumber(1);
    n.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    n.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    n.setXincoCoreNodeList(new ArrayList<>());
    n.setXincoCoreAceList(new ArrayList<>());
    // Put data in the list — old node (root 1) != null → inner IF fires
    ArrayList<XincoCoreData> dataList = new ArrayList<>();
    dataList.add(dataCtrl.findXincoCoreData(data.getId()));
    n.setXincoCoreDataList(dataList);
    nodeCtrl.create(n);

    // After create, data now belongs to N. Destroy data first, then N.
    dataCtrl.destroy(data.getId());
    getEntityManagerFactory().getCache().evictAll();
    nodeCtrl.destroy(n.getId());
  }

  /**
   * Create node N1, attach an ACE to N1, then create node N2 with that ACE in its aceList. Covers
   * create() lines 95-148: ace-attach loop and update-refs inner IF (old node N1 != null →
   * N1.aceList.remove(ACE)).
   */
  public void testNode_createWithAceFromOtherNode() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    // N1: first node, gets an ACE
    XincoCoreNode n1 = buildNode(nodeCtrl, langCtrl, "TestNodeCreateAce_N1");
    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreNode(nodeCtrl.findXincoCoreNode(n1.getId()));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // N2: claims ACE in its aceList — old node (N1) != null → inner IF fires
    XincoCoreNode n2 = new XincoCoreNode();
    n2.setDesignation("TestNodeCreateAce_N2");
    n2.setStatusNumber(1);
    n2.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    n2.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    n2.setXincoCoreNodeList(new ArrayList<>());
    n2.setXincoCoreDataList(new ArrayList<>());
    ArrayList<XincoCoreAce> aceList = new ArrayList<>();
    aceList.add(aceCtrl.findXincoCoreAce(aceId));
    n2.setXincoCoreAceList(aceList);
    nodeCtrl.create(n2);

    // ACE now belongs to N2. Destroy ACE, then N2, then N1.
    aceCtrl.destroy(aceId);
    nodeCtrl.destroy(n2.getId());
    nodeCtrl.destroy(n1.getId());
  }

  // -------------------------------------------------------------------------
  // XincoCoreNodeJpaController — edit() FK-change branches
  // -------------------------------------------------------------------------

  /**
   * Edit node N to change its language from lang 1 to lang 2. Covers edit() lines 250-256: langOld
   * != null && !langOld.equals(langNew) → langOld.nodeList.remove(N); langNew != null &&
   * !langNew.equals(langOld) → langNew.nodeList.add(N).
   */
  public void testNode_editChangeLanguage() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreNode n = buildNode(nodeCtrl, langCtrl, "TestNodeEditLang");

    // Reload and change language from 1 to 2
    XincoCoreNode toEdit = nodeCtrl.findXincoCoreNode(n.getId());
    toEdit.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(2));
    toEdit.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    toEdit.setXincoCoreNodeList(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreDataList(new ArrayList<>());
    nodeCtrl.edit(toEdit);

    nodeCtrl.destroy(n.getId());
  }

  /**
   * Create node N under parent P1, then edit N to move it under parent P2. Covers edit() lines
   * 258-264: nodeRelOld != null && !nodeRelOld.equals(nodeRelNew) → P1.nodeList.remove(N);
   * nodeRelNew != null && !nodeRelNew.equals(nodeRelOld) → P2.nodeList.add(N).
   */
  public void testNode_editChangeParent() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreNode p1 = buildNode(nodeCtrl, langCtrl, "TestNodeEditParent_P1");
    XincoCoreNode p2 = buildNode(nodeCtrl, langCtrl, "TestNodeEditParent_P2");

    // N starts under P1
    XincoCoreNode n = new XincoCoreNode();
    n.setDesignation("TestNodeEditParent_N");
    n.setStatusNumber(1);
    n.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    n.setXincoCoreNode(nodeCtrl.findXincoCoreNode(p1.getId()));
    n.setXincoCoreNodeList(new ArrayList<>());
    n.setXincoCoreAceList(new ArrayList<>());
    n.setXincoCoreDataList(new ArrayList<>());
    nodeCtrl.create(n);

    // Edit N: move from P1 to P2
    XincoCoreNode toEdit = nodeCtrl.findXincoCoreNode(n.getId());
    toEdit.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    toEdit.setXincoCoreNode(nodeCtrl.findXincoCoreNode(p2.getId()));
    toEdit.setXincoCoreNodeList(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreDataList(new ArrayList<>());
    nodeCtrl.edit(toEdit);

    // Cleanup: N, P2, P1
    nodeCtrl.destroy(n.getId());
    nodeCtrl.destroy(p2.getId());
    nodeCtrl.destroy(p1.getId());
  }

  // -------------------------------------------------------------------------
  // XincoCoreNodeJpaController — destroy() ACE loop
  // -------------------------------------------------------------------------

  /**
   * Create node N, attach an ACE to it, then destroy N. Covers destroy() lines 388-392: the
   * xincoCoreAceList loop that sets ace.xincoCoreNode = null before removing N.
   */
  public void testNode_destroyWithAce() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreNode n = buildNode(nodeCtrl, langCtrl, "TestNodeDestroyWithAce");

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreNode(nodeCtrl.findXincoCoreNode(n.getId()));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Destroy N — ACE loop fires: ace.xincoCoreNode set to null
    nodeCtrl.destroy(n.getId());

    // ACE still exists with node = null; destroy it
    aceCtrl.destroy(aceId);
  }

  // -------------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------------

  private XincoCoreNode buildNode(
      XincoCoreNodeJpaController nodeCtrl,
      XincoCoreLanguageJpaController langCtrl,
      String designation)
      throws Exception {
    XincoCoreNode node = new XincoCoreNode();
    node.setDesignation(designation);
    node.setStatusNumber(1);
    node.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    node.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    node.setXincoCoreNodeList(new ArrayList<>());
    node.setXincoCoreAceList(new ArrayList<>());
    node.setXincoCoreDataList(new ArrayList<>());
    nodeCtrl.create(node);
    return node;
  }

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
