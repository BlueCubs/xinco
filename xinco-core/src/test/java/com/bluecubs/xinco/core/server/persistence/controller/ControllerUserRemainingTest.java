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
 * Covers the remaining uncovered branches in XincoCoreUserJpaController: UMR create inner-IF, UMR
 * and UHG edit FK-change inner-IFs, and UMR / Log / UHG destroy orphan-checks.
 */
public class ControllerUserRemainingTest extends AbstractXincoDataBaseTestCase {

  public ControllerUserRemainingTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerUserRemainingTest.class);
  }

  // -------------------------------------------------------------------------
  // create() — UMR update-refs inner IF (lines ~123-137)
  // -------------------------------------------------------------------------

  /**
   * Create U2 with a UMR (originally linked to U1) in its list. Covers User.create() lines
   * ~123-137: UMR loop runs, oldUser = U1 != null → inner IF body executes.
   */
  public void testUser_createWithUMRFromOtherUser() throws Exception {
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreUserModifiedRecordJpaController umrCtrl =
        new XincoCoreUserModifiedRecordJpaController(getEntityManagerFactory());

    XincoCoreUser u1 = buildUser(userCtrl, "u1.umr.create.src");

    // Create UMR linked to U1 (recordId=9910 to avoid PK conflicts)
    XincoCoreUserModifiedRecord umr = new XincoCoreUserModifiedRecord();
    XincoCoreUserModifiedRecordPK umrPK = new XincoCoreUserModifiedRecordPK();
    umrPK.setRecordId(9910);
    umr.setXincoCoreUserModifiedRecordPK(umrPK);
    umr.setXincoCoreUser(userCtrl.findXincoCoreUser(u1.getId()));
    umr.setModTime(new Date());
    umr.setModReason("create inner-if test");
    umrCtrl.create(umr); // sets pk.id = u1.getId()
    XincoCoreUserModifiedRecordPK createdPK = umr.getXincoCoreUserModifiedRecordPK();

    // Create U2 with UMR in its list: UMR.xincoCoreUser = U1 != null → inner IF runs
    XincoCoreUser u2 = new XincoCoreUser();
    u2.setUsername("u2.umr.create.dst");
    u2.setUserpassword("pw_u2umrcreate");
    u2.setLastName("UmrDst");
    u2.setFirstName("U2");
    u2.setEmail("u2.umr.create.dst@example.com");
    u2.setStatusNumber(1);
    u2.setAttempts(0);
    u2.setLastModified(new Date());
    u2.setXincoCoreUserModifiedRecordList(
        Arrays.asList(umrCtrl.findXincoCoreUserModifiedRecord(createdPK)));
    u2.setXincoCoreAceList(new ArrayList<>());
    u2.setXincoCoreLogList(new ArrayList<>());
    u2.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    userCtrl.create(u2);
    int u2Id = u2.getId();
    assertTrue(u2Id > 0);

    // Cleanup: destroy UMR first (FK still points to u1; destroy it before destroying u1)
    umrCtrl.destroy(createdPK);
    userCtrl.destroy(u2Id);
    userCtrl.destroy(u1.getId());
  }

  // -------------------------------------------------------------------------
  // edit() — UMR FK-change inner IF (lines ~313-336)
  // -------------------------------------------------------------------------

  /**
   * Edit U2 adding a UMR that was previously linked to U1. Covers User.edit() lines ~313-336:
   * !old.contains(umr) → body, oldUser = U1 != null and != U2 → inner IF runs.
   */
  public void testUser_editMoveUMRFromOtherUser() throws Exception {
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreUserModifiedRecordJpaController umrCtrl =
        new XincoCoreUserModifiedRecordJpaController(getEntityManagerFactory());

    XincoCoreUser u1 = buildUser(userCtrl, "u1.umr.edit.src");
    XincoCoreUser u2 = buildUser(userCtrl, "u2.umr.edit.dst");

    // Create UMR linked to U1 (recordId=9911)
    XincoCoreUserModifiedRecord umr = new XincoCoreUserModifiedRecord();
    XincoCoreUserModifiedRecordPK umrPK = new XincoCoreUserModifiedRecordPK();
    umrPK.setRecordId(9911);
    umr.setXincoCoreUserModifiedRecordPK(umrPK);
    umr.setXincoCoreUser(userCtrl.findXincoCoreUser(u1.getId()));
    umr.setModTime(new Date());
    umr.setModReason("edit inner-if test");
    umrCtrl.create(umr);
    XincoCoreUserModifiedRecordPK createdPK = umr.getXincoCoreUserModifiedRecordPK();

    // Edit U2 with UMR (from U1) in list: UMR not in U2's old list, oldUser = U1 != U2 → inner IF
    XincoCoreUser toEdit = userCtrl.findXincoCoreUser(u2.getId());
    toEdit.setXincoCoreUserModifiedRecordList(
        Arrays.asList(umrCtrl.findXincoCoreUserModifiedRecord(createdPK)));
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    userCtrl.edit(toEdit);

    // Cleanup: UMR DB FK still points to u1 (non-updatable); destroy UMR before users
    umrCtrl.destroy(createdPK);
    userCtrl.destroy(u2.getId());
    userCtrl.destroy(u1.getId());
  }

  // -------------------------------------------------------------------------
  // edit() — UHG FK-change inner IF (lines ~376-399)
  // -------------------------------------------------------------------------

  /**
   * Edit U2 adding a UHG (user-group membership) that was previously linked to U1. Covers
   * User.edit() lines ~376-399: !old.contains(uhg) → body, oldUser = U1 != null and != U2 → inner
   * IF runs.
   */
  public void testUser_editMoveUHGFromOtherUser() throws Exception {
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupJpaController uhgCtrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());

    XincoCoreGroup g1 = buildGroup(groupCtrl, "TestGrpUhgEditMove");
    XincoCoreUser u1 = buildUser(userCtrl, "u1.uhg.edit.src");
    XincoCoreUser u2 = buildUser(userCtrl, "u2.uhg.edit.dst");

    // Create UHG linking U1 to G1
    XincoCoreUserHasXincoCoreGroupPK pk =
        new XincoCoreUserHasXincoCoreGroupPK(u1.getId(), g1.getId());
    XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup();
    uhg.setXincoCoreUserHasXincoCoreGroupPK(pk);
    uhg.setStatusNumber(1);
    uhg.setXincoCoreUser(userCtrl.findXincoCoreUser(u1.getId()));
    uhg.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(g1.getId()));
    uhgCtrl.create(uhg);

    // Edit U2 with UHG (from U1): UHG not in U2's old list, oldUser = U1 != U2 → inner IF runs
    XincoCoreUser toEdit = userCtrl.findXincoCoreUser(u2.getId());
    toEdit.setXincoCoreUserModifiedRecordList(new ArrayList<>());
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoCoreUserHasXincoCoreGroupList(
        Arrays.asList(uhgCtrl.findXincoCoreUserHasXincoCoreGroup(pk)));
    userCtrl.edit(toEdit);

    // Cleanup: UHG DB FK still points to u1 (non-updatable); destroy UHG before users
    uhgCtrl.destroy(pk);
    userCtrl.destroy(u2.getId());
    userCtrl.destroy(u1.getId());
    groupCtrl.destroy(g1.getId());
  }

  // -------------------------------------------------------------------------
  // destroy() orphan checks (lines ~433-475)
  // -------------------------------------------------------------------------

  /**
   * Destroy a user that has an existing UMR. Covers User.destroy() lines ~434-448: UMR orphan-check
   * loop fires and IllegalOrphanException is thrown.
   */
  public void testUser_destroyOrphanCheckWithUMR() throws Exception {
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreUserModifiedRecordJpaController umrCtrl =
        new XincoCoreUserModifiedRecordJpaController(getEntityManagerFactory());

    XincoCoreUser user = buildUser(userCtrl, "user.destroy.umr.orphan");
    int userId = user.getId();

    // Create UMR linked to the user (recordId=9912)
    XincoCoreUserModifiedRecord umr = new XincoCoreUserModifiedRecord();
    XincoCoreUserModifiedRecordPK umrPK = new XincoCoreUserModifiedRecordPK();
    umrPK.setRecordId(9912);
    umr.setXincoCoreUserModifiedRecordPK(umrPK);
    umr.setXincoCoreUser(userCtrl.findXincoCoreUser(userId));
    umr.setModTime(new Date());
    umr.setModReason("orphan check test");
    umrCtrl.create(umr);
    XincoCoreUserModifiedRecordPK createdPK = umr.getXincoCoreUserModifiedRecordPK();

    try {
      userCtrl.destroy(userId); // user has UMR → orphan check fires
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    // Cleanup
    umrCtrl.destroy(createdPK);
    userCtrl.destroy(userId);
  }

  /**
   * Destroy a user that has an existing log. Covers User.destroy() lines ~449-460: Log orphan-check
   * loop fires and IllegalOrphanException is thrown.
   */
  public void testUser_destroyOrphanCheckWithLog() throws Exception {
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    XincoCoreUser user = buildUser(userCtrl, "user.destroy.log.orphan");
    int userId = user.getId();

    // Create a data item (needed for log)
    XincoCoreData data = new XincoCoreData();
    data.setDesignation("test.data.for.user.log.orphan");
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
    int dataId = data.getId();

    // Create log linked to the user
    XincoCoreLog log = new XincoCoreLog();
    log.setOpCode(1);
    log.setOpDatetime(new Date());
    log.setOpDescription("orphan check log");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("");
    log.setXincoCoreData(dataCtrl.findXincoCoreData(dataId));
    log.setXincoCoreUser(userCtrl.findXincoCoreUser(userId));
    logCtrl.create(log);
    int logId = log.getId();

    try {
      userCtrl.destroy(userId); // user has log → orphan check fires
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    // Cleanup
    logCtrl.destroy(logId);
    dataCtrl.destroy(dataId);
    userCtrl.destroy(userId);
  }

  /**
   * Destroy a user that has an existing group membership. Covers User.destroy() lines ~461-475: UHG
   * orphan-check loop fires and IllegalOrphanException is thrown.
   */
  public void testUser_destroyOrphanCheckWithUHG() throws Exception {
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupJpaController uhgCtrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());

    XincoCoreUser user = buildUser(userCtrl, "user.destroy.uhg.orphan");
    int userId = user.getId();
    XincoCoreGroup group = buildGroup(groupCtrl, "TestGrpDestroyUhgOrphan");
    int groupId = group.getId();

    XincoCoreUserHasXincoCoreGroupPK pk = new XincoCoreUserHasXincoCoreGroupPK(userId, groupId);
    XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup();
    uhg.setXincoCoreUserHasXincoCoreGroupPK(pk);
    uhg.setStatusNumber(1);
    uhg.setXincoCoreUser(userCtrl.findXincoCoreUser(userId));
    uhg.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(groupId));
    uhgCtrl.create(uhg);

    try {
      userCtrl.destroy(userId); // user has UHG membership → orphan check fires
      fail("Expected IllegalOrphanException");
    } catch (IllegalOrphanException e) {
      // expected
    }

    // Cleanup
    uhgCtrl.destroy(pk);
    userCtrl.destroy(userId);
    groupCtrl.destroy(groupId);
  }

  // -------------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------------

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
    user.setXincoCoreUserModifiedRecordList(new ArrayList<>());
    user.setXincoCoreAceList(new ArrayList<>());
    user.setXincoCoreLogList(new ArrayList<>());
    user.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    ctrl.create(user);
    return user;
  }

  private XincoCoreGroup buildGroup(XincoCoreGroupJpaController ctrl, String name)
      throws Exception {
    XincoCoreGroup group = new XincoCoreGroup();
    group.setDesignation(name);
    group.setStatusNumber(1);
    group.setXincoCoreAceList(new ArrayList<>());
    group.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    ctrl.create(group);
    return group;
  }
}
