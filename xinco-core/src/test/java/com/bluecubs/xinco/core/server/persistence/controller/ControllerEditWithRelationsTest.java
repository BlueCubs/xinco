package com.bluecubs.xinco.core.server.persistence.controller;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests controller edit() methods with populated relationship lists. When a non-empty list is
 * passed, the "attach new" loops inside create/edit execute — the primary coverage gap from earlier
 * tests that always passed empty collections.
 */
public class ControllerEditWithRelationsTest extends AbstractXincoDataBaseTestCase {

  public ControllerEditWithRelationsTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ControllerEditWithRelationsTest.class);
  }

  // ---- XincoCoreGroupJpaController.edit with membership in list ----

  public void testGroupController_editWithMembership() throws Exception {
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupJpaController uhgCtrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());

    // Create a fresh group (no initial memberships)
    XincoCoreGroup group = new XincoCoreGroup();
    group.setDesignation("TestGroupRelEdit");
    group.setStatusNumber(1);
    group.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    group.setXincoCoreAceList(new ArrayList<>());
    groupCtrl.create(group);
    int groupId = group.getId();
    assertTrue(groupId > 0);

    // Create a user
    XincoCoreUser user = new XincoCoreUser();
    user.setUsername("testuser_grel1");
    user.setUserpassword("pw_grel1");
    user.setLastName("Grel");
    user.setFirstName("Test");
    user.setEmail("grel1@example.com");
    user.setStatusNumber(1);
    user.setAttempts(0);
    user.setLastModified(new Date());
    user.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    user.setXincoCoreAceList(new ArrayList<>());
    user.setXincoCoreLogList(new ArrayList<>());
    user.setXincoCoreUserModifiedRecordList(new ArrayList<>());
    userCtrl.create(user);
    int userId = user.getId();
    assertTrue(userId > 0);

    // Create a group membership
    XincoCoreUserHasXincoCoreGroupPK pk = new XincoCoreUserHasXincoCoreGroupPK(userId, groupId);
    XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup();
    uhg.setXincoCoreUserHasXincoCoreGroupPK(pk);
    uhg.setStatusNumber(1);
    uhg.setXincoCoreUser(userCtrl.findXincoCoreUser(userId));
    uhg.setXincoCoreGroup(groupCtrl.findXincoCoreGroup(groupId));
    uhgCtrl.create(uhg);

    // Edit group WITH the membership in its list — triggers the attachment loop
    XincoCoreGroup toEdit = groupCtrl.findXincoCoreGroup(groupId);
    toEdit.setDesignation("TestGroupRelEdit_Updated");
    toEdit.setXincoCoreUserHasXincoCoreGroupList(
        Arrays.asList(uhgCtrl.findXincoCoreUserHasXincoCoreGroup(pk)));
    toEdit.setXincoCoreAceList(new ArrayList<>());
    groupCtrl.edit(toEdit);
    assertEquals(
        "TestGroupRelEdit_Updated", groupCtrl.findXincoCoreGroup(groupId).getDesignation());

    // Cleanup
    uhgCtrl.destroy(pk);
    userCtrl.destroy(userId);
    groupCtrl.destroy(groupId);
  }

  // ---- XincoCoreDataJpaController.edit with ACE and Log in lists ----

  public void testDataController_editWithAceAndLog() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());

    // Create a data item
    XincoCoreData data = new XincoCoreData();
    data.setDesignation("test.data.ace.log.edit");
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
    assertTrue(dataId > 0);

    // Create an ACE for this data item
    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreData(dataCtrl.findXincoCoreData(dataId));
    aceCtrl.create(ace);
    int aceId = ace.getId();
    assertTrue(aceId > 0);

    // Create a log entry for this data item
    XincoCoreLog log = new XincoCoreLog();
    log.setOpCode(1);
    log.setOpDatetime(new Date());
    log.setOpDescription("test log for edit coverage");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("");
    log.setXincoCoreData(dataCtrl.findXincoCoreData(dataId));
    logCtrl.create(log);
    int logId = log.getId();
    assertTrue(logId > 0);

    // Edit data item WITH the ACE and Log in their lists — triggers attachment loops
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(dataId);
    toEdit.setDesignation("test.data.ace.log.edit.updated");
    toEdit.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    toEdit.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    toEdit.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    toEdit.setXincoCoreAceList(Arrays.asList(aceCtrl.findXincoCoreAce(aceId)));
    toEdit.setXincoCoreLogList(Arrays.asList(logCtrl.findXincoCoreLog(logId)));
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.edit(toEdit);
    assertEquals(
        "test.data.ace.log.edit.updated", dataCtrl.findXincoCoreData(dataId).getDesignation());

    // Cleanup: must remove ACE and Log before destroying data (FK constraints)
    aceCtrl.destroy(aceId);
    logCtrl.destroy(logId);
    dataCtrl.destroy(dataId);
  }

  // ---- XincoCoreLanguageJpaController.edit with data in list ----

  public void testLanguageController_editWithData() throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    // Create a fresh language
    XincoCoreLanguage lang = new XincoCoreLanguage();
    lang.setSign("tst");
    lang.setDesignation("TestLangRelEdit");
    lang.setXincoCoreNodeList(new ArrayList<>());
    lang.setXincoCoreDataList(new ArrayList<>());
    langCtrl.create(lang);
    int langId = lang.getId();
    assertTrue(langId > 0);

    // Create a data item linked to this language
    XincoCoreData data = new XincoCoreData();
    data.setDesignation("test.lang.rel.data");
    data.setStatusNumber(1);
    data.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(langId));
    data.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    data.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    data.setXincoCoreAceList(new ArrayList<>());
    data.setXincoCoreLogList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    data.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.create(data);
    int dataId = data.getId();
    assertTrue(dataId > 0);

    // Edit language WITH data in its list — triggers data attachment loop
    XincoCoreLanguage toEdit = langCtrl.findXincoCoreLanguage(langId);
    toEdit.setDesignation("TestLangRelEdit_Updated");
    toEdit.setXincoCoreDataList(Arrays.asList(dataCtrl.findXincoCoreData(dataId)));
    toEdit.setXincoCoreNodeList(new ArrayList<>());
    langCtrl.edit(toEdit);
    assertEquals(
        "TestLangRelEdit_Updated", langCtrl.findXincoCoreLanguage(langId).getDesignation());

    // Cleanup: data must be deleted first (language is FK)
    dataCtrl.destroy(dataId);
    langCtrl.destroy(langId);
  }

  // ---- XincoCoreDataTypeJpaController.edit with data in list ----

  public void testDataTypeController_editWithData() throws Exception {
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    // Create a fresh data type
    XincoCoreDataType dt = new XincoCoreDataType();
    dt.setDesignation("test.dt.rel.edit");
    dt.setDescription("Test DataType for rel edit");
    dt.setXincoCoreDataList(new ArrayList<>());
    dt.setXincoCoreDataTypeAttributeList(new ArrayList<>());
    dtCtrl.create(dt);
    int dtId = dt.getId();
    assertTrue(dtId > 0);

    // Create a data item linked to this data type
    XincoCoreData data = new XincoCoreData();
    data.setDesignation("test.dt.rel.data");
    data.setStatusNumber(1);
    data.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    data.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(dtId));
    data.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    data.setXincoCoreAceList(new ArrayList<>());
    data.setXincoCoreLogList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    data.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.create(data);
    int dataId = data.getId();
    assertTrue(dataId > 0);

    // Edit data type WITH data in its list — triggers data attachment loop
    XincoCoreDataType toEdit = dtCtrl.findXincoCoreDataType(dtId);
    toEdit.setDesignation("test.dt.rel.edit.updated");
    toEdit.setXincoCoreDataList(Arrays.asList(dataCtrl.findXincoCoreData(dataId)));
    toEdit.setXincoCoreDataTypeAttributeList(new ArrayList<>());
    dtCtrl.edit(toEdit);
    assertEquals("test.dt.rel.edit.updated", dtCtrl.findXincoCoreDataType(dtId).getDesignation());

    // Cleanup
    dataCtrl.destroy(dataId);
    dtCtrl.destroy(dtId);
  }

  // ---- XincoCoreUserJpaController.create with membership in list ----

  public void testUserController_createWithMembership() throws Exception {
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreUserHasXincoCoreGroupJpaController uhgCtrl =
        new XincoCoreUserHasXincoCoreGroupJpaController(getEntityManagerFactory());

    // Build a group membership that we'll pass into user.create()
    // The membership references group 1 (admin group)
    XincoCoreGroup group1 = groupCtrl.findXincoCoreGroup(1);

    // Build user first so we can compute the FK
    XincoCoreUser user = new XincoCoreUser();
    user.setUsername("testuser_ucrel1");
    user.setUserpassword("pw_ucrel1");
    user.setLastName("UCRel");
    user.setFirstName("Test");
    user.setEmail("ucrel1@example.com");
    user.setStatusNumber(1);
    user.setAttempts(0);
    user.setLastModified(new Date());
    user.setXincoCoreUserModifiedRecordList(new ArrayList<>());
    user.setXincoCoreAceList(new ArrayList<>());
    user.setXincoCoreLogList(new ArrayList<>());
    user.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    // Create user with empty lists first (to get user ID)
    userCtrl.create(user);
    int userId = user.getId();
    assertTrue(userId > 0);

    // Now create membership and edit user with it in list
    XincoCoreUserHasXincoCoreGroupPK pk = new XincoCoreUserHasXincoCoreGroupPK(userId, 1);
    XincoCoreUserHasXincoCoreGroup uhg = new XincoCoreUserHasXincoCoreGroup();
    uhg.setXincoCoreUserHasXincoCoreGroupPK(pk);
    uhg.setStatusNumber(1);
    uhg.setXincoCoreUser(userCtrl.findXincoCoreUser(userId));
    uhg.setXincoCoreGroup(group1);
    uhgCtrl.create(uhg);

    // Edit user WITH the membership in its list — triggers UserHasGroup attachment loop in edit
    XincoCoreUser toEdit = userCtrl.findXincoCoreUser(userId);
    toEdit.setStatusNumber(2);
    toEdit.setXincoCoreUserHasXincoCoreGroupList(
        Arrays.asList(uhgCtrl.findXincoCoreUserHasXincoCoreGroup(pk)));
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoCoreUserModifiedRecordList(new ArrayList<>());
    userCtrl.edit(toEdit);
    assertEquals(2, (int) userCtrl.findXincoCoreUser(userId).getStatusNumber());

    // Cleanup
    uhgCtrl.destroy(pk);
    userCtrl.destroy(userId);
  }

  // ---- XincoCoreUserServer 9-arg constructor ----

  public void testUserServer_nineArgConstructor() {
    try {
      com.bluecubs.xinco.core.server.XincoCoreUserServer userServer =
          new com.bluecubs.xinco.core.server.XincoCoreUserServer(
              0,
              "testuser9arg",
              "password9arg",
              "LastName9",
              "FirstName9",
              "email9@example.com",
              1,
              0,
              new Timestamp(System.currentTimeMillis()));
      assertNotNull(userServer);
      assertEquals(0, userServer.getId());
      assertEquals("testuser9arg", userServer.getUsername());
      assertEquals("LastName9", userServer.getLastName());
      assertEquals("FirstName9", userServer.getFirstName());
      assertEquals("email9@example.com", userServer.getEmail());
      assertEquals(1, userServer.getStatusNumber());
    } catch (Exception e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail("Unexpected exception from 9-arg constructor: " + e.getMessage());
    }
  }

  // ---- XincoCoreNodeJpaController.edit with data and ACE in lists ----

  public void testNodeController_editWithDataAndAce() throws Exception {
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());

    // Create a fresh node
    XincoCoreNode node = new XincoCoreNode();
    node.setDesignation("TestNodeRelEdit");
    node.setStatusNumber(1);
    node.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    node.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1)); // parent = root node
    node.setXincoCoreDataList(new ArrayList<>());
    node.setXincoCoreNodeList(new ArrayList<>());
    node.setXincoCoreAceList(new ArrayList<>());
    nodeCtrl.create(node);
    int nodeId = node.getId();
    assertTrue(nodeId > 0);

    // Create a data item in this node (must retain in edit to pass orphan check)
    XincoCoreData data = new XincoCoreData();
    data.setDesignation("test.node.rel.data");
    data.setStatusNumber(1);
    data.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    data.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    data.setXincoCoreNode(nodeCtrl.findXincoCoreNode(nodeId));
    data.setXincoCoreAceList(new ArrayList<>());
    data.setXincoCoreLogList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    data.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.create(data);
    int dataId = data.getId();
    assertTrue(dataId > 0);

    // Create an ACE for this node
    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreNode(nodeCtrl.findXincoCoreNode(nodeId));
    aceCtrl.create(ace);
    int aceId = ace.getId();
    assertTrue(aceId > 0);

    // Edit node WITH data in data list AND ace in ace list — triggers both attachment loops
    XincoCoreNode toEdit = nodeCtrl.findXincoCoreNode(nodeId);
    toEdit.setDesignation("TestNodeRelEdit_Updated");
    toEdit.setXincoCoreDataList(Arrays.asList(dataCtrl.findXincoCoreData(dataId)));
    toEdit.setXincoCoreNodeList(new ArrayList<>());
    toEdit.setXincoCoreAceList(Arrays.asList(aceCtrl.findXincoCoreAce(aceId)));
    toEdit.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    nodeCtrl.edit(toEdit);
    assertEquals("TestNodeRelEdit_Updated", nodeCtrl.findXincoCoreNode(nodeId).getDesignation());

    // Cleanup: ACE and data first (FKs to node), then node
    aceCtrl.destroy(aceId);
    dataCtrl.destroy(dataId);
    nodeCtrl.destroy(nodeId);
  }

  // ---- XincoCoreDataJpaController.create with ACE in list ----

  public void testDataController_createWithAce() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());

    // Create a standalone ACE (no data FK initially)
    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    aceCtrl.create(ace);
    int aceId = ace.getId();
    assertTrue(aceId > 0);

    // Create a data item WITH the ACE in its list — triggers ACE attachment loop in create()
    XincoCoreData data = new XincoCoreData();
    data.setDesignation("test.data.create.withace");
    data.setStatusNumber(1);
    data.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    data.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    data.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    data.setXincoCoreAceList(Arrays.asList(aceCtrl.findXincoCoreAce(aceId)));
    data.setXincoCoreLogList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    data.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.create(data);
    int dataId = data.getId();
    assertTrue(dataId > 0);

    // Cleanup
    aceCtrl.destroy(aceId);
    dataCtrl.destroy(dataId);
  }

  // ---- XincoCoreDataJpaController.edit with XincoAddAttribute in list ----

  public void testDataController_editWithAddAttribute() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoAddAttributeJpaController attrCtrl =
        new XincoAddAttributeJpaController(getEntityManagerFactory());

    // Create a data item (type 1: file type with attributes)
    XincoCoreData data = new XincoCoreData();
    data.setDesignation("test.data.addattr.edit");
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
    assertTrue(dataId > 0);

    // Create an add-attribute record (data type 1, attribute 1 = 'general.filename', varchar)
    XincoAddAttributePK attrPK = new XincoAddAttributePK(dataId, 1);
    XincoAddAttribute attr = new XincoAddAttribute();
    attr.setXincoAddAttributePK(attrPK);
    attr.setAttribInt(0);
    attr.setAttribUnsignedint(0L);
    attr.setAttribDouble(0.0);
    attr.setAttribVarchar("test_filename.txt");
    attr.setAttribText("");
    attr.setXincoCoreData(dataCtrl.findXincoCoreData(dataId));
    attrCtrl.create(attr);
    assertNotNull(attrCtrl.findXincoAddAttribute(attrPK));

    // Edit data WITH the add-attribute in its list — triggers XincoAddAttribute attachment loop
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(dataId);
    toEdit.setDesignation("test.data.addattr.edit.updated");
    toEdit.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    toEdit.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    toEdit.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoAddAttributeList(Arrays.asList(attrCtrl.findXincoAddAttribute(attrPK)));
    dataCtrl.edit(toEdit);
    assertEquals(
        "test.data.addattr.edit.updated", dataCtrl.findXincoCoreData(dataId).getDesignation());

    // Cleanup: add-attribute first (FK to data), then data
    attrCtrl.destroy(attrPK);
    dataCtrl.destroy(dataId);
  }

  // ---- XincoCoreDataJpaController.edit — change DataType FK (covers lines 404-426) ----

  public void testDataController_editWithDataTypeChange() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    // Create data with DataType=1
    XincoCoreData data = new XincoCoreData();
    data.setDesignation("test.data.fkchange");
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
    assertTrue(dataId > 0);

    // Edit: change DataType from 1 → 2 and Language from 1 → 2
    // xincoCoreDataTypeOld != xincoCoreDataTypeNew → lines 404-410 execute
    // xincoCoreLanguageOld != xincoCoreLanguageNew → lines 412-418 execute
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(dataId);
    toEdit.setDesignation("test.data.fkchange.updated");
    toEdit.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(2));
    toEdit.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(2));
    toEdit.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.edit(toEdit);
    XincoCoreData updated = dataCtrl.findXincoCoreData(dataId);
    assertEquals(2, (int) updated.getXincoCoreDataType().getId());

    dataCtrl.destroy(dataId);
  }

  // ---- XincoCoreDataJpaController.edit — new ACE from a different data item (lines 481-494) ----

  public void testDataController_editMoveAceFromOtherData() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());

    // Create source data D1 with an ACE linked to it
    XincoCoreData d1 = new XincoCoreData();
    d1.setDesignation("test.data.aceSource");
    d1.setStatusNumber(1);
    d1.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    d1.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    d1.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    d1.setXincoCoreAceList(new ArrayList<>());
    d1.setXincoCoreLogList(new ArrayList<>());
    d1.setXincoCoreDataHasDependencyList(new ArrayList<>());
    d1.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    d1.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.create(d1);
    int d1Id = d1.getId();

    // Create ACE linked to D1
    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreData(dataCtrl.findXincoCoreData(d1Id));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Create target data D2 with no ACEs
    XincoCoreData d2 = new XincoCoreData();
    d2.setDesignation("test.data.aceTarget");
    d2.setStatusNumber(1);
    d2.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    d2.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    d2.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    d2.setXincoCoreAceList(new ArrayList<>());
    d2.setXincoCoreLogList(new ArrayList<>());
    d2.setXincoCoreDataHasDependencyList(new ArrayList<>());
    d2.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    d2.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.create(d2);
    int d2Id = d2.getId();

    // Edit D2 passing the ACE from D1 in its list.
    // xincoCoreAceListOld for D2 = empty; xincoCoreAceListNew = [aceFromD1]
    // !old.contains(aceFromD1) → TRUE → enter "new ace" IF body (lines 481-495)
    // oldData = aceFromD1.getXincoCoreData() = D1 ≠ null and ≠ D2 → nested IF runs (lines 487-494)
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(d2Id);
    toEdit.setDesignation("test.data.aceTarget.updated");
    toEdit.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    toEdit.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    toEdit.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    toEdit.setXincoCoreAceList(Arrays.asList(aceCtrl.findXincoCoreAce(aceId)));
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.edit(toEdit);

    // Cleanup
    aceCtrl.destroy(aceId);
    dataCtrl.destroy(d2Id);
    dataCtrl.destroy(d1Id);
  }

  // ---- XincoCoreLanguageJpaController.create with node and data in lists ----

  public void testLanguageController_createWithNodeAndData() throws Exception {
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());

    // Source language — holds node and data during setup
    XincoCoreLanguage srcLang = new XincoCoreLanguage();
    srcLang.setSign("tss");
    srcLang.setDesignation("TestSrcLangCreate");
    srcLang.setXincoCoreNodeList(new ArrayList<>());
    srcLang.setXincoCoreDataList(new ArrayList<>());
    langCtrl.create(srcLang);
    int srcLangId = srcLang.getId();

    // Create a node and data item that belong to the source language
    XincoCoreNode node = new XincoCoreNode();
    node.setDesignation("TestNodeForLangCreate");
    node.setStatusNumber(1);
    node.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(srcLangId));
    node.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    node.setXincoCoreNodeList(new ArrayList<>());
    node.setXincoCoreDataList(new ArrayList<>());
    node.setXincoCoreAceList(new ArrayList<>());
    nodeCtrl.create(node);
    int nodeId = node.getId();

    XincoCoreData data = new XincoCoreData();
    data.setDesignation("test.data.for.lang.create");
    data.setStatusNumber(1);
    data.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(srcLangId));
    data.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    data.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    data.setXincoCoreAceList(new ArrayList<>());
    data.setXincoCoreLogList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList(new ArrayList<>());
    data.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    data.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.create(data);
    int dataId = data.getId();

    // Create target language with the node and data in its lists.
    // create() attaches them and updates their language FK from srcLang → targetLang
    // → attachment loops + update-refs loops (with oldLang != null) all execute
    XincoCoreLanguage targetLang = new XincoCoreLanguage();
    targetLang.setSign("tgt");
    targetLang.setDesignation("TestTgtLangCreate");
    targetLang.setXincoCoreNodeList(Arrays.asList(nodeCtrl.findXincoCoreNode(nodeId)));
    targetLang.setXincoCoreDataList(Arrays.asList(dataCtrl.findXincoCoreData(dataId)));
    langCtrl.create(targetLang);
    int tgtLangId = targetLang.getId();
    assertTrue(tgtLangId > 0);

    // After create: node and data now reference targetLang. Cleanup in reverse FK order.
    dataCtrl.destroy(dataId);
    nodeCtrl.destroy(nodeId);
    langCtrl.destroy(tgtLangId);
    langCtrl.destroy(srcLangId);
  }

  // ---- XincoCoreDataJpaController.edit — change Node FK (covers lines 421-422) ----

  public void testDataController_editChangeNode() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());

    // Create a second node to move the data item to
    XincoCoreNode newNode = new XincoCoreNode();
    newNode.setDesignation("TestNodeForDataMove");
    newNode.setStatusNumber(1);
    newNode.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    newNode.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    newNode.setXincoCoreNodeList(new ArrayList<>());
    newNode.setXincoCoreDataList(new ArrayList<>());
    newNode.setXincoCoreAceList(new ArrayList<>());
    nodeCtrl.create(newNode);
    int newNodeId = newNode.getId();

    // Create data in node 1
    XincoCoreData data = new XincoCoreData();
    data.setDesignation("test.data.nodechange");
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

    // Edit data: move from node 1 to newNode
    // xincoCoreNodeOld != xincoCoreNodeNew → lines 420-426 execute
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(dataId);
    toEdit.setDesignation("test.data.nodechange.updated");
    toEdit.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    toEdit.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    toEdit.setXincoCoreNode(nodeCtrl.findXincoCoreNode(newNodeId));
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.edit(toEdit);
    assertEquals(newNodeId, (int) dataCtrl.findXincoCoreData(dataId).getXincoCoreNode().getId());

    dataCtrl.destroy(dataId);
    nodeCtrl.destroy(newNodeId);
  }

  // ---- XincoCoreDataJpaController.edit — remove ACE from data (line 477-478) ----

  public void testDataController_editRemoveAce() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());

    // Create data with an ACE linked to it
    XincoCoreData data = new XincoCoreData();
    data.setDesignation("test.data.removeace");
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

    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    ace.setXincoCoreData(dataCtrl.findXincoCoreData(dataId));
    aceCtrl.create(ace);
    int aceId = ace.getId();

    // Edit data with EMPTY ace list while persistent data has the ACE.
    // xincoCoreAceListOld = [ace], xincoCoreAceListNew = []
    // Loop at line 475: ace NOT in new list → ace.setXincoCoreData(null) → merge (lines 477-478)
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(dataId);
    toEdit.setDesignation("test.data.removeace.updated");
    toEdit.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    toEdit.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    toEdit.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    toEdit.setXincoCoreAceList(new ArrayList<>()); // empty — removes ACE from data
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.edit(toEdit);

    // Cleanup (ACE now has null data FK, can still be destroyed)
    aceCtrl.destroy(aceId);
    dataCtrl.destroy(dataId);
  }

  // ---- XincoCoreDataJpaController.edit — move Log from D1 to D2 (lines 497-513) ----

  public void testDataController_editMoveLog() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());

    // Create source data D1 with a log entry
    XincoCoreData d1 = buildEmptyData(dataCtrl, langCtrl, dtCtrl, nodeCtrl, "test.data.logSrc");
    int d1Id = d1.getId();

    XincoCoreLog log = new XincoCoreLog();
    log.setOpCode(1);
    log.setOpDatetime(new java.util.Date());
    log.setOpDescription("log for move test");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("");
    log.setXincoCoreData(dataCtrl.findXincoCoreData(d1Id));
    logCtrl.create(log);
    int logId = log.getId();

    // Create target data D2 with no logs
    XincoCoreData d2 = buildEmptyData(dataCtrl, langCtrl, dtCtrl, nodeCtrl, "test.data.logDst");
    int d2Id = d2.getId();

    // Edit D2 passing the log from D1 in its list.
    // xincoCoreLogListOld for D2 = empty; xincoCoreLogListNew = [log from D1]
    // !old.contains(log) → TRUE → enter IF body (lines 497-513)
    // oldData = log.getXincoCoreData() = D1 != null → nested IF body runs
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(d2Id);
    toEdit.setDesignation("test.data.logDst.updated");
    toEdit.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    toEdit.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    toEdit.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(Arrays.asList(logCtrl.findXincoCoreLog(logId)));
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoAddAttributeList(new ArrayList<>());
    dataCtrl.edit(toEdit);

    // Cleanup
    logCtrl.destroy(logId);
    dataCtrl.destroy(d2Id);
    dataCtrl.destroy(d1Id);
  }

  // ---- XincoCoreDataJpaController.edit — move AddAttribute from D1 to D2 (lines 515-541) ----

  public void testDataController_editMoveAddAttribute() throws Exception {
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreLanguageJpaController langCtrl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory());
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    XincoCoreNodeJpaController nodeCtrl = new XincoCoreNodeJpaController(getEntityManagerFactory());
    XincoAddAttributeJpaController attrCtrl =
        new XincoAddAttributeJpaController(getEntityManagerFactory());

    // Create source data D1 with an add-attribute
    XincoCoreData d1 = buildEmptyData(dataCtrl, langCtrl, dtCtrl, nodeCtrl, "test.data.attrSrc");
    int d1Id = d1.getId();

    XincoAddAttributePK pk1 = new XincoAddAttributePK(d1Id, 1);
    XincoAddAttribute attr = new XincoAddAttribute();
    attr.setXincoAddAttributePK(pk1);
    attr.setAttribInt(0);
    attr.setAttribUnsignedint(0L);
    attr.setAttribDouble(0.0);
    attr.setAttribVarchar("src_filename.txt");
    attr.setAttribText("");
    attr.setXincoCoreData(dataCtrl.findXincoCoreData(d1Id));
    attrCtrl.create(attr);

    // Create target data D2 with no attributes
    XincoCoreData d2 = buildEmptyData(dataCtrl, langCtrl, dtCtrl, nodeCtrl, "test.data.attrDst");
    int d2Id = d2.getId();

    // Edit D2 passing attr from D1.
    // xincoAddAttributeListOld for D2 = empty; new = [attr from D1]
    // !old.contains(attr) → TRUE → enter IF body (lines 515-541)
    // oldData = attr.getXincoCoreData() = D1 != null → nested IF body runs
    XincoCoreData toEdit = dataCtrl.findXincoCoreData(d2Id);
    toEdit.setDesignation("test.data.attrDst.updated");
    toEdit.setXincoCoreLanguage(langCtrl.findXincoCoreLanguage(1));
    toEdit.setXincoCoreDataType(dtCtrl.findXincoCoreDataType(1));
    toEdit.setXincoCoreNode(nodeCtrl.findXincoCoreNode(1));
    toEdit.setXincoCoreAceList(new ArrayList<>());
    toEdit.setXincoCoreLogList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList(new ArrayList<>());
    toEdit.setXincoCoreDataHasDependencyList1(new ArrayList<>());
    toEdit.setXincoAddAttributeList(Arrays.asList(attrCtrl.findXincoAddAttribute(pk1)));
    dataCtrl.edit(toEdit);

    // Cleanup: attr's data FK was moved to D2, destroy by PK
    attrCtrl.destroy(pk1);
    dataCtrl.destroy(d2Id);
    dataCtrl.destroy(d1Id);
  }

  /** Helper: create a data item with all empty collections. */
  private XincoCoreData buildEmptyData(
      XincoCoreDataJpaController dataCtrl,
      XincoCoreLanguageJpaController langCtrl,
      XincoCoreDataTypeJpaController dtCtrl,
      XincoCoreNodeJpaController nodeCtrl,
      String designation)
      throws Exception {
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

  // ---- XincoCoreGroupJpaController.create with ACE in list ----

  public void testGroupController_createWithAce() throws Exception {
    XincoCoreGroupJpaController groupCtrl =
        new XincoCoreGroupJpaController(getEntityManagerFactory());
    XincoCoreAceJpaController aceCtrl = new XincoCoreAceJpaController(getEntityManagerFactory());

    // Create a standalone ACE (no group FK initially)
    XincoCoreAce ace = new XincoCoreAce();
    ace.setReadPermission(true);
    ace.setWritePermission(false);
    ace.setExecutePermission(false);
    ace.setAdminPermission(false);
    aceCtrl.create(ace);
    int aceId = ace.getId();
    assertTrue(aceId > 0);

    // Create group with that ACE — triggers ACE attachment + update-refs in create()
    XincoCoreGroup group = new XincoCoreGroup();
    group.setDesignation("TestGroupCreateAce");
    group.setStatusNumber(1);
    group.setXincoCoreUserHasXincoCoreGroupList(new ArrayList<>());
    group.setXincoCoreAceList(Arrays.asList(aceCtrl.findXincoCoreAce(aceId)));
    groupCtrl.create(group);
    int groupId = group.getId();
    assertTrue(groupId > 0);

    // Cleanup
    aceCtrl.destroy(aceId);
    groupCtrl.destroy(groupId);
  }
}
