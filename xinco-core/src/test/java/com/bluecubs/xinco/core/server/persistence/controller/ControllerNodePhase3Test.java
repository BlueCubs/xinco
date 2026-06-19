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
 * Phase 3 coverage for XincoCoreNodeJpaController: create() child-node list loop, destroy()
 * child-node list loop, and edit() old/new child-node, old/new ACE, new data, and data orphan-check
 * loops.
 */
public class ControllerNodePhase3Test extends AbstractXincoDataBaseTestCase {

  public ControllerNodePhase3Test(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerNodePhase3Test.class);
  }

  // -------------------------------------------------------------------------
  // create() — child-node list loop
  // -------------------------------------------------------------------------

  /**
   * Create parent node P with child node C (pre-existing under root) in its xincoCoreNodeList.
   * Covers create() lines 86-94 (child-node attach loop) and lines 124-136 (update-refs loop with
   * inner IF: C's old parent != null → old.nodeList.remove(C)).
   */
  public void testNode_createWithChildNode() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    // C starts under root (1)
    XincoCoreNode c = buildNode(nodeCtrl, langCtrl, "TestCreateChild_C");

    // P claims C in its nodeList — C's old parent (root 1) != null → inner IF fires
    XincoCoreNode p = new XincoCoreNode();
    p.setDesignation("TestCreateChild_P");
    p.setStatusNumber(1);
    p.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    p.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    p.setXincoCoreNodeList(Arrays.asList(nodeCtrl.findXincoCoreNode(c.getId())));
    p.setXincoCoreAceList(new ArrayList<>());
    p.setXincoCoreDataList(new ArrayList<>());
    nodeCtrl.create(p);

    // C.parent = P now. Destroy C first (sets P.nodeList-entry removed), then P.
    nodeCtrl.destroy(c.getId());
    getEntityManagerFactory().getCache().evictAll();
    nodeCtrl.destroy(p.getId());
  }

  // -------------------------------------------------------------------------
  // destroy() — child-node list loop
  // -------------------------------------------------------------------------

  /**
   * Destroy a parent node P that has child node C. Covers destroy() lines 383-387: the
   * xincoCoreNodeList loop that sets each child's xincoCoreNode to null before removing P.
   */
  public void testNode_destroyParentWithChild() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreNode p = buildNode(nodeCtrl, langCtrl, "TestDestroyParent_P");

    // C created with P as parent
    XincoCoreNode c = new XincoCoreNode();
    c.setDesignation("TestDestroyParent_C");
    c.setStatusNumber(1);
    c.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    c.setXincoCoreNode(nodeCtrl.findXincoCoreNode(p.getId()));
    c.setXincoCoreNodeList(new ArrayList<>());
    c.setXincoCoreAceList(new ArrayList<>());
    c.setXincoCoreDataList(new ArrayList<>());
    nodeCtrl.create(c);

    // Destroy P → child-node loop fires: C.parent = null, then P is removed
    nodeCtrl.destroy(p.getId());
    getEntityManagerFactory().getCache().evictAll();

    // C still exists with parent = null; clean it up
    nodeCtrl.destroy(c.getId());
  }

  // -------------------------------------------------------------------------
  // edit() — child-node old loop (remove child from list)
  // -------------------------------------------------------------------------

  /**
   * Edit parent P to remove child C from its xincoCoreNodeList. Covers edit() lines 266-271: the
   * old-list loop body (C in old but not in new → C.xincoCoreNode = null).
   */
  public void testNode_editRemoveChildNode() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreNode p = buildNode(nodeCtrl, langCtrl, "TestEditRemoveChild_P");

    // C created with P as parent → P.nodeList = [C] in DB
    XincoCoreNode c = new XincoCoreNode();
    c.setDesignation("TestEditRemoveChild_C");
    c.setStatusNumber(1);
    c.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    c.setXincoCoreNode(nodeCtrl.findXincoCoreNode(p.getId()));
    c.setXincoCoreNodeList(new ArrayList<>());
    c.setXincoCoreAceList(new ArrayList<>());
    c.setXincoCoreDataList(new ArrayList<>());
    nodeCtrl.create(c);

    // Edit P with empty nodeList — C is in old but not in new → old-loop fires: C.parent = null
    XincoCoreNode editP = nodeCtrl.findXincoCoreNode(p.getId());
    editP.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    editP.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    editP.setXincoCoreNodeList(new ArrayList<>());
    editP.setXincoCoreAceList(new ArrayList<>());
    editP.setXincoCoreDataList(new ArrayList<>());
    nodeCtrl.edit(editP);

    // C.parent = null now; destroy both
    nodeCtrl.destroy(c.getId());
    nodeCtrl.destroy(p.getId());
  }

  // -------------------------------------------------------------------------
  // edit() — child-node new loop (add child to list)
  // -------------------------------------------------------------------------

  /**
   * Edit parent P to add child C (currently under root) to its xincoCoreNodeList. Covers edit()
   * lines 272-287: the new-list loop body (C in new but not in old → C.parent = P) and the inner IF
   * (C's old parent != null && != P → old.nodeList.remove(C)).
   */
  public void testNode_editAddChildNode() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreNode p = buildNode(nodeCtrl, langCtrl, "TestEditAddChild_P");
    XincoCoreNode c = buildNode(nodeCtrl, langCtrl, "TestEditAddChild_C");

    // Edit P to include C in nodeList — C is in new but not old → new-loop + inner IF fire
    XincoCoreNode editP = nodeCtrl.findXincoCoreNode(p.getId());
    editP.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    editP.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    editP.setXincoCoreNodeList(Arrays.asList(nodeCtrl.findXincoCoreNode(c.getId())));
    editP.setXincoCoreAceList(new ArrayList<>());
    editP.setXincoCoreDataList(new ArrayList<>());
    nodeCtrl.edit(editP);

    // C.parent = P now; destroy C first (removes from P.nodeList), then P
    nodeCtrl.destroy(c.getId());
    getEntityManagerFactory().getCache().evictAll();
    nodeCtrl.destroy(p.getId());
  }

  // -------------------------------------------------------------------------
  // edit() — ACE old loop (remove ACE from list)
  // -------------------------------------------------------------------------

  /**
   * Edit node N to remove ACE from its xincoCoreAceList. Covers edit() lines 288-292: the old
   * ACE-list loop body (ACE in old but not in new → ace.xincoCoreNode = null).
   */
  public void testNode_editRemoveAce() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreNode n = buildNode(nodeCtrl, langCtrl, "TestEditRemoveAce_N");

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreNode(nodeCtrl.findXincoCoreNode(n.getId()));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Edit N with empty aceList — ACE in old but not in new → old-loop fires: ace.node = null
    XincoCoreNode editN = nodeCtrl.findXincoCoreNode(n.getId());
    editN.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    editN.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    editN.setXincoCoreNodeList(new ArrayList<>());
    editN.setXincoCoreAceList(new ArrayList<>());
    editN.setXincoCoreDataList(new ArrayList<>());
    nodeCtrl.edit(editN);

    // ACE.node = null; destroy it, then N
    aceCtrl.destroy(aceId);
    nodeCtrl.destroy(n.getId());
  }

  // -------------------------------------------------------------------------
  // edit() — ACE new loop (add ACE to list + inner IF)
  // -------------------------------------------------------------------------

  /**
   * Edit node N2 to add an ACE (currently owned by N1) to its xincoCoreAceList. Covers edit() lines
   * 294-309: the new ACE-list loop body (ACE in new but not old → ace.node = N2) and the inner IF
   * (ACE's old node N1 != null && != N2 → N1.aceList.remove(ace)).
   */
  public void testNode_editAddAce() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreNode n1 = buildNode(nodeCtrl, langCtrl, "TestEditAddAce_N1");
    XincoCoreNode n2 = buildNode(nodeCtrl, langCtrl, "TestEditAddAce_N2");

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreNode(nodeCtrl.findXincoCoreNode(n1.getId()));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Edit N2 with ACE in aceList — ACE's old node N1 != null && != N2 → new-loop + inner IF fire
    XincoCoreNode editN2 = nodeCtrl.findXincoCoreNode(n2.getId());
    editN2.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    editN2.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    editN2.setXincoCoreNodeList(new ArrayList<>());
    editN2.setXincoCoreAceList(Arrays.asList(aceCtrl.findXincoCoreAce(aceId)));
    editN2.setXincoCoreDataList(new ArrayList<>());
    nodeCtrl.edit(editN2);

    // ACE.node = N2 now; destroy ACE, then N2, then N1
    aceCtrl.destroy(aceId);
    nodeCtrl.destroy(n2.getId());
    nodeCtrl.destroy(n1.getId());
  }

  // -------------------------------------------------------------------------
  // edit() — data new loop (add data to list + inner IF)
  // -------------------------------------------------------------------------

  /**
   * Edit node N to add data D (currently under root) to its xincoCoreDataList. Covers edit() lines
   * 310-325: the new data-list loop body (D in new but not old → D.node = N) and the inner IF (D's
   * old node root != null && != N → root.dataList.remove(D)).
   */
  public void testNode_editAddData() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());

    XincoCoreNode n = buildNode(nodeCtrl, langCtrl, "TestEditAddData_N");
    XincoCoreData d = buildData(dataCtrl, "test.node.editadddata");

    // Edit N with D in dataList — D in new but not old → new-loop + inner IF fire
    XincoCoreNode editN = nodeCtrl.findXincoCoreNode(n.getId());
    editN.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    editN.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    editN.setXincoCoreNodeList(new ArrayList<>());
    editN.setXincoCoreAceList(new ArrayList<>());
    editN.setXincoCoreDataList(Arrays.asList(dataCtrl.findXincoCoreData(d.getId())));
    nodeCtrl.edit(editN);

    // D.node = N now; destroy D first, then N
    dataCtrl.destroy(d.getId());
    getEntityManagerFactory().getCache().evictAll();
    nodeCtrl.destroy(n.getId());
  }

  // -------------------------------------------------------------------------
  // edit() — data orphan-check body
  // -------------------------------------------------------------------------

  /**
   * Edit node N (which owns data D) to remove D from its xincoCoreDataList. Covers edit() lines
   * 195-208: the orphan-check body (D in old but not in new → IllegalOrphanException).
   */
  public void testNode_editDataOrphanCheck() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());

    XincoCoreNode n = buildNode(nodeCtrl, langCtrl, "TestEditDataOrphan_N");

    // Create D directly under N (not root)
    XincoCoreData d = new XincoCoreData();
    d.setDesignation("test.node.editorphan.data");
    d.setStatusNumber(1);
    d.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    d.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    d.setXincoCoreNode(nodeCtrl.findXincoCoreNode(n.getId()));
    d.setXincoCoreAceList(new ArrayList<>());
    d.setXincoCoreLogList(new ArrayList<>());
    d.setXincoCoreDataHasDependencyList(new ArrayList<>());
    d.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    d.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.create(d);

    // Edit N with empty dataList — D in old but not in new → IllegalOrphanException
    XincoCoreNode editN = nodeCtrl.findXincoCoreNode(n.getId());
    editN.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    editN.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    editN.setXincoCoreNodeList(new ArrayList<>());
    editN.setXincoCoreAceList(new ArrayList<>());
    editN.setXincoCoreDataList(new ArrayList<>());
    try {
      nodeCtrl.edit(editN);
      fail("Expected IllegalOrphanException when removing data from node");
    } catch (IllegalOrphanException e) {
      // expected — orphan-check body covered
    }

    // Cleanup: destroy D, then N
    dataCtrl.destroy(d.getId());
    getEntityManagerFactory().getCache().evictAll();
    nodeCtrl.destroy(n.getId());
  }

  // -------------------------------------------------------------------------
  // destroy() — EntityNotFoundException catch (non-existent IDs)
  // -------------------------------------------------------------------------

  /**
   * Destroy a non-existent node ID. Covers destroy() EntityNotFoundException catch block →
   * NonexistentEntityException (lines 353-354 in XincoCoreNodeJpaController).
   */
  public void testNode_destroyNonExistent() {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    try {
      nodeCtrl.destroy(99999);
      fail("Expected NonexistentEntityException for non-existent node id");
    } catch (Exception e) {
      // expected
    }
  }

  /**
   * Destroy a non-existent data ID. Covers XincoCoreDataJpaController destroy()
   * EntityNotFoundException catch → NonexistentEntityException (lines 558-559).
   */
  public void testData_destroyNonExistent() {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    try {
      dataCtrl.destroy(99999);
      fail("Expected NonexistentEntityException for non-existent data id");
    } catch (Exception e) {
      // expected
    }
  }

  /**
   * Destroy a non-existent log ID. Covers XincoCoreLogJpaController destroy()
   * EntityNotFoundException catch → NonexistentEntityException (lines 157-158).
   */
  public void testLog_destroyNonExistent() {
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());
    try {
      logCtrl.destroy(99999);
      fail("Expected NonexistentEntityException for non-existent log id");
    } catch (Exception e) {
      // expected
    }
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
}
