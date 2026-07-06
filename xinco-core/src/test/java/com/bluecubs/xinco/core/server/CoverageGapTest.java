package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataType;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttributePK;
import com.bluecubs.xinco.core.server.persistence.XincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage;
import com.bluecubs.xinco.core.server.persistence.XincoCoreNode;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUser;
import com.bluecubs.xinco.core.server.persistence.XincoDependencyType;
import com.bluecubs.xinco.core.server.persistence.XincoSetting;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreUserJpaController;
import com.bluecubs.xinco.tools.Tool;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import junit.framework.Test;
import junit.framework.TestSuite;

/** Covers instruction-coverage gaps not addressed by existing tests. */
public class CoverageGapTest extends AbstractXincoDataBaseTestCase {

  public CoverageGapTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(CoverageGapTest.class);
  }

  // ---- Entity full-arg constructors ----

  public void testEntity_XincoCoreUser_fullArgConstructor() {
    XincoCoreUser u =
        new XincoCoreUser(
            999, "gap_user", "pw", "Last", "First", "g@t.com", 1, 0, new java.util.Date());
    assertEquals(999, (int) u.getId());
    assertEquals("gap_user", u.getUsername());
    assertEquals("Last", u.getLastName());
    assertEquals("First", u.getFirstName());
    assertEquals("g@t.com", u.getEmail());
    assertEquals(1, u.getStatusNumber());
    assertEquals(0, u.getAttempts());
  }

  public void testEntity_XincoCoreGroup_fullArgConstructor() {
    XincoCoreGroup g = new XincoCoreGroup(50, "GapGroup", 1);
    assertEquals(50, (int) g.getId());
    assertEquals("GapGroup", g.getDesignation());
    assertEquals(1, g.getStatusNumber());
  }

  public void testEntity_XincoCoreNode_fullArgConstructor() {
    XincoCoreNode n = new XincoCoreNode(55, "GapNode", 1);
    assertEquals(55, (int) n.getId());
    assertEquals("GapNode", n.getDesignation());
    assertEquals(1, n.getStatusNumber());
  }

  public void testEntity_XincoCoreLanguage_fullArgConstructor() {
    XincoCoreLanguage l = new XincoCoreLanguage(88, "xx", "GapLang");
    assertEquals(88, (int) l.getId());
    assertEquals("xx", l.getSign());
    assertEquals("GapLang", l.getDesignation());
  }

  public void testEntity_XincoCoreData_fullArgConstructor() {
    XincoCoreData d = new XincoCoreData(77, "GapData", 1);
    assertEquals(77, (int) d.getId());
    assertEquals("GapData", d.getDesignation());
    assertEquals(1, d.getStatusNumber());
  }

  public void testEntity_XincoCoreDataType_fullArgConstructor() {
    XincoCoreDataType dt = new XincoCoreDataType(44, "GapType", "GapDesc");
    assertEquals(44, (int) dt.getId());
    assertEquals("GapType", dt.getDesignation());
    assertEquals("GapDesc", dt.getDescription());
  }

  public void testEntity_XincoCoreDataTypeAttribute_fullArgConstructor() {
    XincoCoreDataTypeAttributePK pk = new XincoCoreDataTypeAttributePK(1, 99);
    XincoCoreDataTypeAttribute a = new XincoCoreDataTypeAttribute(pk, "GapAttr", "varchar", 100);
    assertEquals(pk, a.getXincoCoreDataTypeAttributePK());
    assertEquals("GapAttr", a.getDesignation());
    assertEquals("varchar", a.getDataType());
    assertEquals(100, a.getAttrSize());
  }

  public void testEntity_XincoSetting_constructors() {
    XincoSetting s1 = new XincoSetting(5);
    assertEquals(5, (int) s1.getId());

    XincoSetting s2 = new XincoSetting(6, "gap.setting");
    assertEquals(6, (int) s2.getId());
    assertEquals("gap.setting", s2.getDescription());
  }

  public void testEntity_XincoDependencyType_constructors() {
    XincoDependencyType dt1 = new XincoDependencyType(7);
    assertEquals(7, (int) dt1.getId());

    XincoDependencyType dt2 = new XincoDependencyType(8, "GapDep");
    assertEquals(8, (int) dt2.getId());
    assertEquals("GapDep", dt2.getDesignation());
  }

  public void testEntity_XincoCoreDataHasDependency_fullArgConstructor() {
    XincoCoreDataHasDependency dep = new XincoCoreDataHasDependency(1, 2, 3);
    assertNotNull(dep.getXincoCoreDataHasDependencyPK());
    assertEquals(1, dep.getXincoCoreDataHasDependencyPK().getXincoCoreDataParentId());
    assertEquals(2, dep.getXincoCoreDataHasDependencyPK().getXincoCoreDataChildrenId());
    assertEquals(3, dep.getXincoCoreDataHasDependencyPK().getDependencyTypeId());
  }

  public void testEntity_XincoAddAttribute_intConstructor() {
    XincoAddAttribute aa = new XincoAddAttribute(1, 2);
    assertNotNull(aa.getXincoAddAttributePK());
    assertEquals(1, aa.getXincoAddAttributePK().getXincoCoreDataId());
    assertEquals(2, aa.getXincoAddAttributePK().getAttributeId());
  }

  // ---- Entity equals edge cases: same-id returns true, wrong-type returns false ----

  public void testEntity_XincoCoreUser_equalsEdgeCases() {
    XincoCoreUser u1 = new XincoCoreUser(10);
    XincoCoreUser u2 = new XincoCoreUser(10);
    assertTrue("same id should be equal", u1.equals(u2));
    assertFalse("wrong type should not be equal", u1.equals("not a user"));
  }

  public void testEntity_XincoCoreGroup_equalsEdgeCases() {
    XincoCoreGroup g1 = new XincoCoreGroup(20);
    XincoCoreGroup g2 = new XincoCoreGroup(20);
    assertTrue("same id should be equal", g1.equals(g2));
    assertFalse("wrong type should not be equal", g1.equals(42));
  }

  public void testEntity_XincoCoreLanguage_equalsEdgeCases() {
    XincoCoreLanguage l1 = new XincoCoreLanguage(3);
    XincoCoreLanguage l2 = new XincoCoreLanguage(3);
    assertTrue("same id should be equal", l1.equals(l2));
    assertFalse("wrong type should not be equal", l1.equals("xx"));
  }

  public void testEntity_XincoCoreData_equalsEdgeCases() {
    XincoCoreData d1 = new XincoCoreData(5);
    XincoCoreData d2 = new XincoCoreData(5);
    assertTrue("same id should be equal", d1.equals(d2));
    assertFalse("wrong type should not be equal", d1.equals(5));
  }

  public void testEntity_XincoCoreDataType_equalsEdgeCases() {
    XincoCoreDataType dt1 = new XincoCoreDataType(2);
    XincoCoreDataType dt2 = new XincoCoreDataType(2);
    assertTrue("same id should be equal", dt1.equals(dt2));
    assertFalse("wrong type should not be equal", dt1.equals(Boolean.TRUE));
  }

  public void testEntity_XincoCoreDataTypeAttribute_equalsEdgeCases() {
    XincoCoreDataTypeAttributePK pk = new XincoCoreDataTypeAttributePK(1, 1);
    XincoCoreDataTypeAttribute a1 = new XincoCoreDataTypeAttribute(pk);
    XincoCoreDataTypeAttribute a2 = new XincoCoreDataTypeAttribute(pk);
    assertTrue("same PK should be equal", a1.equals(a2));
    assertFalse("wrong type should not be equal", a1.equals("attr"));
  }

  public void testEntity_XincoCoreUser_hashCode_nullId() {
    XincoCoreUser u = new XincoCoreUser();
    u.hashCode(); // must not throw
  }

  public void testEntity_XincoCoreData_hashCode_nullId() {
    XincoCoreData d = new XincoCoreData();
    d.hashCode(); // must not throw
  }

  public void testEntity_XincoCoreDataType_hashCode_nullId() {
    XincoCoreDataType dt = new XincoCoreDataType();
    dt.hashCode(); // must not throw
  }

  // ---- XincoException additional constructors ----

  public void testXincoException_throwableConstructor() {
    RuntimeException cause = new RuntimeException("root cause");
    XincoException ex = new XincoException(cause);
    assertEquals(cause, ex.getCause());
    assertTrue(ex.toString().contains("root cause"));
  }

  public void testXincoException_listConstructor() {
    List<String> msgs = Arrays.asList("error1", "error2");
    XincoException ex = new XincoException(msgs);
    assertNotNull(ex);
    assertTrue(ex.toString().contains("error1"));
    assertTrue(ex.toString().contains("error2"));
  }

  // ---- User group management ----

  public void testUserGroups_saveGetRemove() throws Exception {
    XincoCoreUserServer u =
        new XincoCoreUserServer(
            0,
            "grpmgmt001",
            "pw",
            "LGM",
            "FGM",
            "gm001@e.com",
            1,
            0,
            new Timestamp(System.currentTimeMillis()));
    u.setHashPassword(false);
    u.setChangerID(1);
    int id = u.write2DB();
    try {
      List<XincoCoreGroupServer> before = XincoCoreGroupServer.getGroupsOfUser(id);
      assertTrue("new user has no groups", before.isEmpty());

      XincoCoreUserServer.saveUserGroups(id, Arrays.asList(1));
      List<XincoCoreGroupServer> after = XincoCoreGroupServer.getGroupsOfUser(id);
      assertFalse("user should be in group 1 after save", after.isEmpty());
      assertEquals(1, after.get(0).getId());

      XincoCoreUserServer.removeUserGroups(id);
      List<XincoCoreGroupServer> cleared = XincoCoreGroupServer.getGroupsOfUser(id);
      assertTrue("user should have no groups after remove", cleared.isEmpty());
    } finally {
      new XincoCoreUserJpaController(getEntityManagerFactory()).destroy(id);
    }
  }

  // ---- deleteFromDB removes user and their groups ----

  public void testUser_deleteFromDB() throws Exception {
    XincoCoreUserServer u =
        new XincoCoreUserServer(
            0,
            "deletetest01",
            "pw",
            "LD",
            "FD",
            "del01@e.com",
            1,
            0,
            new Timestamp(System.currentTimeMillis()));
    u.setHashPassword(false);
    u.setChangerID(1);
    int id = u.write2DB();
    XincoCoreUserServer.saveUserGroups(id, Arrays.asList(1));

    XincoCoreUserServer.deleteFromDB(id);

    try {
      new XincoCoreUserServer(id);
      fail("Expected XincoException for deleted user");
    } catch (XincoException e) {
      // expected
    }
  }

  // ---- XincoSettingServer.write2DB() update path ----

  public void testSetting_write2DB_updatePath() throws Exception {
    XincoSettingServer s = new XincoSettingServer(5);
    assertEquals(5, s.getId());
    s.write2DB();
  }

  // ---- removeFromDB with a log entry covers the log-deletion loop body ----

  public void testData_removeFromDB_withLog() throws Exception {
    XincoCoreDataServer data = new XincoCoreDataServer(0, 1, 1, 1, "rmFromDB.logCov", 1);
    data.setChangerID(1);
    int id = data.write2DB();
    new XincoCoreLogServer(id, 1, 1, null, "coverage log", 1, 0, 0, "").write2DB();
    XincoCoreDataServer.removeFromDB(1, id);
  }

  // ---- deleteFromDB exception paths: catch blocks for nonexistent entities ----

  public void testDeleteFromDB_groupNonExistent_returnsMinusOne() {
    com.bluecubs.xinco.server.service.XincoCoreGroup g =
        new com.bluecubs.xinco.server.service.XincoCoreGroup();
    g.setId(99999);
    int result = XincoCoreGroupServer.deleteFromDB(g);
    assertEquals(-1, result);
  }

  public void testDeleteFromDB_languageNonExistent_throwsXincoException() {
    com.bluecubs.xinco.server.service.XincoCoreLanguage l =
        new com.bluecubs.xinco.server.service.XincoCoreLanguage();
    l.setId(99999);
    try {
      XincoCoreLanguageServer.deleteFromDB(l, 1);
      fail("Expected XincoException for nonexistent language");
    } catch (XincoException e) {
      assertNotNull(e);
    }
  }

  public void testDeleteFromDB_dataTypeNonExistent_returnsMinusOne() {
    com.bluecubs.xinco.server.service.XincoCoreDataType dt =
        new com.bluecubs.xinco.server.service.XincoCoreDataType();
    dt.setId(99999);
    int result = XincoCoreDataTypeServer.deleteFromDB(dt);
    assertEquals(-1, result);
  }

  // ---- XincoIdServer.write2DB() create path (getId()==0) ----

  public void testXincoIdServer_write2DB_createPath() throws Exception {
    XincoIdServer fresh = new XincoIdServer("gap_test_table_cov", 0);
    assertEquals(0, (int) fresh.getId());
    int generatedId = fresh.write2DB();
    assertTrue("write2DB() should return new id > 0", generatedId > 0);
    XincoIdServer.deleteFromDB(fresh);
  }

  // ---- Tool.getImageDim — happy path with a real PNG covers the reader body ----

  public void testTool_getImageDim_validPng() throws Exception {
    BufferedImage img = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
    File tmpPng = File.createTempFile("xinco_cov", ".png");
    try {
      ImageIO.write(img, "png", tmpPng);
      Dimension dim = Tool.getImageDim(tmpPng.getAbsolutePath());
      assertNotNull("getImageDim should return dimension for valid PNG", dim);
      assertEquals(3, (int) dim.getWidth());
      assertEquals(3, (int) dim.getHeight());
    } finally {
      tmpPng.delete();
    }
  }

  // ---- Tool.getImageDim — nonexistent file exercises IOException catch path ----

  public void testTool_getImageDim_nonexistentFile_returnsNull() {
    Dimension dim = Tool.getImageDim("/tmp/does_not_exist_xinco_cov.png");
    assertNull("getImageDim should return null when file not found", dim);
  }

  // ---- removeFromDB with ACE and addAttribute covers both deletion loops ----

  public void testData_removeFromDB_withAceAndAddAttr() throws Exception {
    XincoCoreDataServer data = new XincoCoreDataServer(0, 1, 1, 3, "rmAceAttr.cov", 1);
    data.setChangerID(1);
    int id = data.write2DB();
    XincoCoreACEServer ace = new XincoCoreACEServer(0, 1, 0, 0, id, true, false, false, false);
    ace.setChangerID(1);
    ace.write2DB();
    new XincoAddAttributeServer(id, 1, 0, 0L, 0.0, "http://example.com", "", null).write2DB();
    XincoCoreDataServer.removeFromDB(1, id);
  }

  // ---- writeXincoCoreGroups delete-existing loop (user re-written with groups flag) ----

  public void testWriteXincoCoreGroups_deletesExistingGroups() throws Exception {
    XincoCoreUserServer u =
        new XincoCoreUserServer(
            0,
            "wgrpdel001",
            "pw",
            "LWD",
            "FWD",
            "wgrpdel01@e.com",
            1,
            0,
            new Timestamp(System.currentTimeMillis()));
    u.setHashPassword(false);
    u.setChangerID(1);
    int id = u.write2DB();
    try {
      // Put user in group 1 so writeXincoCoreGroups has existing memberships to delete
      XincoCoreUserServer.saveUserGroups(id, Arrays.asList(1));
      assertEquals(1, XincoCoreGroupServer.getGroupsOfUser(id).size());
      // write2DB with isWriteGroups covers the delete-existing loop in writeXincoCoreGroups;
      // getXincoCoreGroups() always returns ALL DB groups, so user ends up in all groups
      u.setHashPassword(false);
      u.setWriteGroups(true);
      u.setChangerID(1);
      u.write2DB();
      // After writeXincoCoreGroups: deleted old groups, then re-added all DB groups
      assertFalse(
          "writeXincoCoreGroups should have assigned groups",
          XincoCoreGroupServer.getGroupsOfUser(id).isEmpty());
    } finally {
      // deleteFromDB removes group memberships before destroying the user
      XincoCoreUserServer.deleteFromDB(id);
    }
  }

  // ---- XincoCoreACEServer: group-level ACE covers groupId branch + edit path + removeFromDB ----

  public void testAce_groupLevelAndEditAndRemove() throws Exception {
    XincoCoreACEServer ace = new XincoCoreACEServer(0, 0, 1, 1, 0, true, false, false, false);
    ace.setChangerID(1);
    int aceId = ace.write2DB();
    XincoCoreACEServer loaded = new XincoCoreACEServer(aceId);
    loaded.setWritePermission(true);
    loaded.setChangerID(1);
    loaded.write2DB();
    XincoCoreACEServer.removeFromDB(loaded, 1);
    try {
      new XincoCoreACEServer(aceId);
      fail("Expected XincoException for removed ACE");
    } catch (XincoException e) {
      assertNotNull(e);
    }
  }
}
