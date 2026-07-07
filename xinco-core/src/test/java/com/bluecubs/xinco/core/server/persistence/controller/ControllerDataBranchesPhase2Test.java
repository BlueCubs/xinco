package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.IllegalOrphanException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Phase 2 coverage for XincoCoreDataJpaController: create() list-loop bodies (ace, log, addattr),
 * null-list initialisation branches, and edit() orphan-check loop bodies (dep, log, addattr).
 */
public class ControllerDataBranchesPhase2Test extends AbstractXincoDataBaseTestCase {

  public ControllerDataBranchesPhase2Test(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerDataBranchesPhase2Test.class);
  }

  // -------------------------------------------------------------------------
  // create() — null-list initialisation
  // -------------------------------------------------------------------------

  /**
   * Create data without pre-setting any list fields. The controller's null-checks (lines 58-72)
   * initialise each list, covering the "list == null → new ArrayList<>()" branches for all 5 lists.
   */
  public void testData_createWithNullLists() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    XincoCoreData data = new XincoCoreData();
    data.setDesignation("test.data.nulllists");
    data.setStatusNumber(1);
    data.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    data.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    data.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    // All 5 list fields intentionally left null — controller sets them to new ArrayList<>()
    dataCtrl.create(data);

    dataCtrl.destroy(data.getId());
  }

  // -------------------------------------------------------------------------
  // create() — ace list loop
  // -------------------------------------------------------------------------

  /**
   * Create data D2 with an ACE (whose xincoCoreData was D1) in its aceList. Covers create() lines
   * 120-205: the ace-attach loop and the update-refs inner IF (ace's old data D1 != null →
   * D1.aceList.remove(ace)). Destroying D2 (which now owns the ACE) covers destroy() lines 637-641:
   * the ACE cleanup loop.
   */
  public void testData_createWithAceFromOtherData() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());

    XincoCoreData d1 = buildData(dataCtrl, "test.data.create.ace.src");
    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreData(dataCtrl.findXincoCoreData(d1.getId()));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Build D2 with ACE already pointing to D1 in its aceList
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreData d2 = new XincoCoreData();
    d2.setDesignation("test.data.create.ace.dst");
    d2.setStatusNumber(1);
    d2.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    d2.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    d2.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    d2.setXincoCoreDataHasDependencyList(new ArrayList<>());
    d2.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    d2.setXincoCoreLogList(new ArrayList<>());
    d2.setXincoAddAttributeList(new ArrayList<>());
    // ACE points to D1 — create() attach+update-refs loops run; inner IF fires (old data D1 !=
    // null)
    d2.setXincoCoreAceList(Arrays.asList(aceCtrl.findXincoCoreAce(aceId)));
    dataCtrl.create(d2);

    // D2 now owns the ACE. Destroy D2 → destroy() ACE-cleanup loop fires (ace.data → null).
    dataCtrl.destroy(d2.getId());
    // ACE now has no data reference; destroy it cleanly, then D1.
    aceCtrl.destroy(aceId);
    dataCtrl.destroy(d1.getId());
  }

  // -------------------------------------------------------------------------
  // create() — log list loop
  // -------------------------------------------------------------------------

  /**
   * Create data D2 with a log (whose xincoCoreData was D1) in its logList. Covers create() lines
   * 130-218: the log-attach loop and the update-refs inner IF (log's old data D1 != null →
   * D1.logList.remove(log)).
   */
  public void testData_createWithLogFromOtherData() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());

    XincoCoreData d1 = buildData(dataCtrl, "test.data.create.log.src");
    XincoCoreUser user = buildUser(userCtrl, "data.create.log");

    XincoCoreLog log = new XincoCoreLog();
    log.setOpCode(1);
    log.setOpDatetime(new Date());
    log.setOpDescription("create log loop test");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("");
    log.setXincoCoreData(dataCtrl.findXincoCoreData(d1.getId()));
    log.setXincoCoreUser(userCtrl.findXincoCoreUser(user.getId()));
    logCtrl.create(log);
    int logId = log.getId();

    // Build D2 with log (whose data was D1) in its logList
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreData d2 = new XincoCoreData();
    d2.setDesignation("test.data.create.log.dst");
    d2.setStatusNumber(1);
    d2.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    d2.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    d2.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    d2.setXincoCoreDataHasDependencyList(new ArrayList<>());
    d2.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    d2.setXincoCoreAceList(new ArrayList<>());
    d2.setXincoAddAttributeList(new ArrayList<>());
    // Log points to D1 — attach loop + update-refs inner IF fire
    d2.setXincoCoreLogList(Arrays.asList(logCtrl.findXincoCoreLog(logId)));
    dataCtrl.create(d2);

    // Log now points to D2 in DB (FK is updatable). Destroy log first to avoid orphan, then D2.
    logCtrl.destroy(logId);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(d2.getId());
    dataCtrl.destroy(d1.getId());
    userCtrl.destroy(user.getId());
  }

  // -------------------------------------------------------------------------
  // create() — addattr list loop
  // -------------------------------------------------------------------------

  /**
   * Create data D2 with an addattr (whose xincoCoreData was D1) in its addattrList. Covers create()
   * lines 140-232: the addattr-attach loop and the update-refs inner IF (old data D1 != null).
   * AddAttr FK is non-updatable, so the DB reference stays on D1; only the in-memory Java path is
   * covered.
   */
  public void testData_createWithAddAttrFromOtherData() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoAddAttributeJpaController aaCtrl =
        new XincoAddAttributeJpaController(getEntityManagerFactory());

    XincoCoreData d1 = buildData(dataCtrl, "test.data.create.addattr.src");

    XincoAddAttribute aa = new XincoAddAttribute();
    XincoAddAttributePK aaPK = new XincoAddAttributePK();
    aaPK.setAttributeId(1);
    aa.setXincoAddAttributePK(aaPK);
    aa.setXincoCoreData(dataCtrl.findXincoCoreData(d1.getId()));
    aa.setAttribVarchar("create loop test");
    aa.setAttribInt(0);
    aa.setAttribUnsignedint(0);
    aa.setAttribDouble(0.0);
    aaCtrl.create(aa);
    XincoAddAttributePK createdPK = aa.getXincoAddAttributePK();

    // Build D2 with addattr (pointing to D1) in its addattrList
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreData d2 = new XincoCoreData();
    d2.setDesignation("test.data.create.addattr.dst");
    d2.setStatusNumber(1);
    d2.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    d2.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    d2.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    d2.setXincoCoreDataHasDependencyList(new ArrayList<>());
    d2.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    d2.setXincoCoreAceList(new ArrayList<>());
    d2.setXincoCoreLogList(new ArrayList<>());
    // AddAttr points to D1 — attach loop + update-refs inner IF fire in-memory
    d2.setXincoAddAttributeList(Arrays.asList(aaCtrl.findXincoAddAttribute(createdPK)));
    dataCtrl.create(d2);

    // FK is non-updatable: addattr still points to D1 in DB. Destroy addattr, evict, then D2, D1.
    aaCtrl.destroy(createdPK);
    getEntityManagerFactory().getCache().evictAll();
    dataCtrl.destroy(d2.getId());
    dataCtrl.destroy(d1.getId());
  }

  // -------------------------------------------------------------------------
  // edit() — orphan-check loop bodies
  // -------------------------------------------------------------------------

  /**
   * Edit D_child removing its dep from the dep list. Covers edit() lines 277-289: dep is in old
   * depList but not in new → illegalOrphanMessages populated → IllegalOrphanException thrown.
   */
  public void testData_editOrphanCheckDep() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreDataHasDependencyJpaController depCtrl =
        new XincoCoreDataHasDependencyJpaController(getEntityManagerFactory());
    XincoDependencyTypeJpaController typeCtrl =
        new XincoDependencyTypeJpaController(getEntityManagerFactory());

    XincoCoreData dChild = buildData(dataCtrl, "test.data.edit.orphan.dep.child");
    XincoCoreData dParent = buildData(dataCtrl, "test.data.edit.orphan.dep.parent");

    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency();
    dep.setXincoCoreData(dataCtrl.findXincoCoreData(dChild.getId()));
    dep.setXincoCoreData1(dataCtrl.findXincoCoreData(dParent.getId()));
    dep.setXincoDependencyType(typeCtrl.findXincoDependencyType(1));
    depCtrl.create(dep);
    XincoCoreDataHasDependencyPK depPK = dep.getXincoCoreDataHasDependencyPK();

    // Edit dChild with empty dep list → dep in old depList not in new → orphan check body fires
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(dChild.getId());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    try {
      dataCtrl.edit(toEdit);
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
   * Edit data removing its log from the log list. Covers edit() lines 303-313: log is in old
   * logList but not in new → IllegalOrphanException thrown.
   */
  public void testData_editOrphanCheckLog() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());

    XincoCoreData data = buildData(dataCtrl, "test.data.edit.orphan.log");
    XincoCoreUser user = buildUser(userCtrl, "data.edit.orphan.log");

    XincoCoreLog log = new XincoCoreLog();
    log.setOpCode(1);
    log.setOpDatetime(new Date());
    log.setOpDescription("edit orphan log test");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("");
    log.setXincoCoreData(dataCtrl.findXincoCoreData(data.getId()));
    log.setXincoCoreUser(userCtrl.findXincoCoreUser(user.getId()));
    logCtrl.create(log);
    int logId = log.getId();

    // Edit data with empty log list → log in old logList not in new → orphan check body fires
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(data.getId());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    try {
      dataCtrl.edit(toEdit);
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    logCtrl.destroy(logId);
    dataCtrl.destroy(data.getId());
    userCtrl.destroy(user.getId());
  }

  /**
   * Edit data removing its addattr from the addattr list. Covers edit() lines 314-324: addattr is
   * in old addattrList but not in new → IllegalOrphanException thrown.
   */
  public void testData_editOrphanCheckAddAttr() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoAddAttributeJpaController aaCtrl =
        new XincoAddAttributeJpaController(getEntityManagerFactory());

    XincoCoreData data = buildData(dataCtrl, "test.data.edit.orphan.addattr");

    XincoAddAttribute aa = new XincoAddAttribute();
    XincoAddAttributePK aaPK = new XincoAddAttributePK();
    aaPK.setAttributeId(1);
    aa.setXincoAddAttributePK(aaPK);
    aa.setXincoCoreData(dataCtrl.findXincoCoreData(data.getId()));
    aa.setAttribVarchar("edit orphan addattr test");
    aa.setAttribInt(0);
    aa.setAttribUnsignedint(0);
    aa.setAttribDouble(0.0);
    aaCtrl.create(aa);
    XincoAddAttributePK createdPK = aa.getXincoAddAttributePK();

    // Edit data with empty addattr list → addattr in old list not in new → orphan check body fires
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(data.getId());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    try {
      dataCtrl.edit(toEdit);
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    aaCtrl.destroy(createdPK);
    dataCtrl.destroy(data.getId());
  }

  // -------------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------------

  private XincoCoreData buildData(XincoCoreDataJpaController ctrl, String designation)
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
