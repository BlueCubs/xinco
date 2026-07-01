package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.persistence.*;
import com.bluecubs.xinco.core.server.persistence.controller.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the Server wrapper classes (XincoCoreGroupServer, XincoCoreLanguageServer,
 * XincoCoreDataTypeServer, XincoCoreDataTypeAttributeServer, XincoCoreLogServer) covering
 * constructors, write2DB(), deleteFromDB(), and static list/utility methods.
 */
public class ServerClassesTest extends AbstractXincoDataBaseTestCase {

  public ServerClassesTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ServerClassesTest.class);
  }

  // =========================================================================
  // XincoCoreGroupServer
  // =========================================================================

  /** Covers constructor(int id) happy path — namedQuery hit, fields populated. */
  public void testGroupServer_loadById() {
    XincoCoreGroupServer gs = new XincoCoreGroupServer(1);
    assertEquals(1, gs.getId());
  }

  /** Covers constructor(int id) else-branch + outer catch → XincoException. */
  public void testGroupServer_loadById_notFound() {
    try {
      new XincoCoreGroupServer(99999);
      fail("Expected XincoException for non-existent group");
    } catch (XincoException e) {
      // expected: result.size()==0 → throw XincoException() → caught → rethrow
    }
  }

  /** Covers constructor(int, String, int) — direct field assignment. */
  public void testGroupServer_constructorFields() {
    XincoCoreGroupServer gs = new XincoCoreGroupServer(1, "field-grp", 1);
    assertEquals(1, gs.getId());
  }

  /** Covers constructor(persistence.XincoCoreGroup) — from entity. */
  public void testGroupServer_constructorFromEntity() {
    com.bluecubs.xinco.core.server.persistence.XincoCoreGroup xcg =
        new XincoCoreGroupJpaController(getEntityManagerFactory()).findXincoCoreGroup(1);
    XincoCoreGroupServer gs = new XincoCoreGroupServer(xcg);
    assertEquals(1, gs.getId());
  }

  /** Covers write2DB() update path (id > 0). */
  public void testGroupServer_write2DB_update() {
    XincoCoreGroupServer gs = new XincoCoreGroupServer(1);
    int id = gs.write2DB();
    assertEquals(1, id);
  }

  /** Covers write2DB() create path (id == 0) and deleteFromDB() success path. */
  public void testGroupServer_write2DB_create() {
    XincoCoreGroupServer gs = new XincoCoreGroupServer(0, "test.srv.grp", 1);
    int newId = gs.write2DB();
    assertTrue("New group id must be positive", newId > 0);
    int result = XincoCoreGroupServer.deleteFromDB(gs);
    assertEquals(0, result);
  }

  /** Covers getXincoCoreGroups() static list method. */
  public void testGroupServer_getXincoCoreGroups() {
    List<XincoCoreGroupServer> groups = XincoCoreGroupServer.getXincoCoreGroups();
    assertFalse("Seed data must have at least one group", groups.isEmpty());
  }

  /** Covers getMembersOfGroup() — seed user 1 is in group 1. */
  public void testGroupServer_getMembersOfGroup() {
    List<XincoCoreUserServer> members = XincoCoreGroupServer.getMembersOfGroup(1);
    assertFalse("Group 1 must have at least one member", members.isEmpty());
  }

  // =========================================================================
  // XincoCoreLanguageServer
  // =========================================================================

  /** Covers constructor(int id) happy path. */
  public void testLangServer_loadById() {
    XincoCoreLanguageServer ls = new XincoCoreLanguageServer(1);
    assertEquals(1, ls.getId());
  }

  /** Covers constructor(int id) else-branch + catch → XincoException. */
  public void testLangServer_loadById_notFound() {
    try {
      new XincoCoreLanguageServer(99999);
      fail("Expected XincoException for non-existent language");
    } catch (XincoException e) {
      // expected
    }
  }

  /** Covers constructor(int, String, String) — direct fields. */
  public void testLangServer_constructorFields() {
    new XincoCoreLanguageServer(1, "en", "English");
  }

  /** Covers constructor(persistence.XincoCoreLanguage). */
  public void testLangServer_constructorFromEntity() {
    com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage xcl =
        new XincoCoreLanguageJpaController(getEntityManagerFactory()).findXincoCoreLanguage(1);
    XincoCoreLanguageServer ls = new XincoCoreLanguageServer(xcl);
    assertEquals(1, ls.getId());
  }

  /** Covers write2DB() update path (id > 0). */
  public void testLangServer_write2DB_update() {
    XincoCoreLanguageServer ls = new XincoCoreLanguageServer(1);
    ls.write2DB();
  }

  /** Covers write2DB() create path + deleteFromDB(). */
  public void testLangServer_write2DB_create() {
    XincoCoreLanguageServer ls = new XincoCoreLanguageServer(0, "xq", "TestLangSrv");
    int newId = ls.write2DB();
    assertTrue("New language id must be positive", newId > 0);
    XincoCoreLanguageServer.deleteFromDB(ls, 1);
  }

  /** Covers getXincoCoreLanguages() static list method. */
  public void testLangServer_getXincoCoreLanguages() {
    List<XincoCoreLanguageServer> langs = XincoCoreLanguageServer.getXincoCoreLanguages();
    assertFalse("Must have at least one language", langs.isEmpty());
  }

  /**
   * Covers isLanguageUsed() with lang 2 (used only by data, not nodes) — first node-count query
   * returns 0 → enters if(!is_used) block → data-count query returns >0 → true.
   */
  public void testLangServer_isLanguageUsed_true() {
    XincoCoreLanguageServer ls = new XincoCoreLanguageServer(2);
    assertTrue(XincoCoreLanguageServer.isLanguageUsed(ls));
  }

  /** Covers isLanguageUsed() false path — fresh language has no references. */
  public void testLangServer_isLanguageUsed_false() {
    XincoCoreLanguageServer ls = new XincoCoreLanguageServer(0, "xa", "Unused");
    ls.write2DB();
    assertFalse(XincoCoreLanguageServer.isLanguageUsed(ls));
    XincoCoreLanguageServer.deleteFromDB(ls, 1);
  }

  // =========================================================================
  // XincoCoreDataTypeServer
  // =========================================================================

  /** Covers constructor(int id) happy path. */
  public void testDtServer_loadById() {
    XincoCoreDataTypeServer dts = new XincoCoreDataTypeServer(1);
    assertEquals(1, dts.getId());
  }

  /** Covers constructor(int id) exception paths. */
  public void testDtServer_loadById_notFound() {
    try {
      new XincoCoreDataTypeServer(99999);
      fail("Expected XincoException for non-existent DataType");
    } catch (XincoException e) {
      // expected
    }
  }

  /** Covers constructor(int, String, String, ArrayList) — direct fields. */
  public void testDtServer_constructorFields() {
    new XincoCoreDataTypeServer(1, "test-dt", "desc", new ArrayList<>());
  }

  /** Covers getXincoCoreDataTypes() static list. */
  public void testDtServer_getXincoCoreDataTypes() {
    List<XincoCoreDataTypeServer> types = XincoCoreDataTypeServer.getXincoCoreDataTypes();
    assertFalse("Must have at least one data type", types.isEmpty());
  }

  /** Covers getXincoCoreDataType(int) found path. */
  public void testDtServer_getXincoCoreDataType_found() {
    XincoCoreDataTypeServer dt = XincoCoreDataTypeServer.getXincoCoreDataType(1);
    assertNotNull(dt);
  }

  /** Covers getXincoCoreDataType(int) not-found path — returns null. */
  public void testDtServer_getXincoCoreDataType_notFound() {
    XincoCoreDataTypeServer dt = XincoCoreDataTypeServer.getXincoCoreDataType(99999);
    assertNull(dt);
  }

  /** Covers write2DB() update path (id > 0). */
  public void testDtServer_write2DB_update() {
    XincoCoreDataTypeServer dts = new XincoCoreDataTypeServer(1);
    dts.write2DB();
  }

  /** Covers write2DB() create path + deleteFromDB(). */
  public void testDtServer_write2DB_create() {
    XincoCoreDataTypeServer dts =
        new XincoCoreDataTypeServer(0, "test-srv-dt", "srv-desc", new ArrayList<>());
    int newId = dts.write2DB();
    assertTrue("New DataType id must be positive", newId > 0);
    int result = XincoCoreDataTypeServer.deleteFromDB(dts);
    assertEquals(0, result);
  }

  // =========================================================================
  // XincoCoreDataTypeAttributeServer
  // =========================================================================

  /** Covers constructor(int, int) not-found path — DataType 1 has no seed attributes. */
  public void testDtaServer_loadById_notFound() {
    try {
      new XincoCoreDataTypeAttributeServer(1, 99999);
      fail("Expected XincoException for non-existent DataTypeAttribute");
    } catch (XincoException e) {
      // expected
    }
  }

  /** Covers constructor(int, int, String, String, int) — direct fields. */
  public void testDtaServer_constructorFields() {
    new XincoCoreDataTypeAttributeServer(1, 1, "test-attr", "String", 10);
  }

  /** Covers getXincoCoreDataTypeAttributes() — returns empty for seed DataType with no attrs. */
  public void testDtaServer_getXincoCoreDataTypeAttributes() {
    List<XincoCoreDataTypeAttributeServer> attrs =
        XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(1);
    assertNotNull(attrs);
  }

  /**
   * Covers write2DB() + constructor(persistence) via getXincoCoreDataTypeAttributes() +
   * deleteFromDB loop body.
   */
  public void testDtaServer_write2DB_and_delete() throws Exception {
    XincoCoreDataTypeJpaController dtCtrl =
        new XincoCoreDataTypeJpaController(getEntityManagerFactory());
    com.bluecubs.xinco.core.server.persistence.XincoCoreDataType dt =
        new com.bluecubs.xinco.core.server.persistence.XincoCoreDataType();
    dt.setDesignation("test.srv.dta.dt");
    dt.setDescription("srv-dta-test");
    dt.setXincoCoreDataList(new ArrayList<>());
    dt.setXincoCoreDataTypeAttributeList(new ArrayList<>());
    dtCtrl.create(dt);
    int dtId = dt.getId();

    XincoCoreDataTypeAttributeServer dtas =
        new XincoCoreDataTypeAttributeServer(dtId, 1, "srv-attr", "String", 10);
    int attrId = dtas.write2DB();
    assertEquals(1, attrId);

    // getXincoCoreDataTypeAttributes covers constructor(persistence entity) path
    List<XincoCoreDataTypeAttributeServer> attrs =
        XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(dtId);
    assertFalse("Must have at least one attribute", attrs.isEmpty());

    // deleteFromDB covers the DTA destroy loop body
    XincoCoreDataTypeAttributeServer.deleteFromDB(dtas, 1);

    getEntityManagerFactory().getCache().evictAll();
    dtCtrl.destroy(dtId);
  }

  // =========================================================================
  // XincoCoreLogServer
  // =========================================================================

  /** Covers constructor(int id) else-branch → XincoException. */
  public void testLogServer_loadById_notFound() {
    try {
      new XincoCoreLogServer(99999);
      fail("Expected XincoException for non-existent log");
    } catch (XincoException e) {
      // expected
    }
  }

  /** Covers constructor(int id) happy path + private constructor via getXincoCoreLogs(). */
  public void testLogServer_loadById_andList() throws Exception {
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());
    XincoCoreDataJpaController dataCtrl = new XincoCoreDataJpaController(getEntityManagerFactory());
    XincoCoreUserJpaController userCtrl = new XincoCoreUserJpaController(getEntityManagerFactory());

    XincoCoreLog log = new XincoCoreLog();
    log.setOpCode(1);
    log.setOpDatetime(new Date());
    log.setOpDescription("server-test");
    log.setVersionHigh(1);
    log.setVersionMid(0);
    log.setVersionLow(0);
    log.setVersionPostfix("");
    log.setXincoCoreData(dataCtrl.findXincoCoreData(1));
    log.setXincoCoreUser(userCtrl.findXincoCoreUser(1));
    logCtrl.create(log);
    int logId = log.getId();

    // constructor(int id) happy path
    XincoCoreLogServer ls = new XincoCoreLogServer(logId);
    assertEquals(logId, ls.getId());

    // getXincoCoreLogs covers private constructor(XincoCoreLog) via the list method
    List<XincoCoreLogServer> logs = XincoCoreLogServer.getXincoCoreLogs(1);
    assertFalse("Must have at least one log for data 1", logs.isEmpty());

    logCtrl.destroy(logId);
  }

  /** Covers constructor(fields) and write2DB() both create and update paths. */
  public void testLogServer_write2DB() throws Exception {
    XincoCoreLogJpaController logCtrl = new XincoCoreLogJpaController(getEntityManagerFactory());

    // constructor(fields) — attrODT=null → uses new Date()
    XincoCoreLogServer ls = new XincoCoreLogServer(1, 1, 1, null, "srv-test", 1, 0, 0, "");

    // write2DB() create path (id == 0)
    int createdId = ls.write2DB();
    assertTrue("New log id must be positive", createdId > 0);

    // write2DB() update path (id > 0 after create)
    ls.write2DB();

    logCtrl.destroy(createdId);
  }
}
