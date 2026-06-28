package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoCoreGroupServer.getXincoCoreGroups;
import static com.bluecubs.xinco.core.server.XincoCoreLanguageServer.getXincoCoreLanguages;
import static com.bluecubs.xinco.core.server.XincoCoreLanguageServer.isLanguageUsed;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoCoreServerMiscTest extends AbstractXincoDataBaseTestCase {

  public XincoCoreServerMiscTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoCoreServerMiscTest.class);
  }

  public void testGetXincoCoreLanguages_returnsNonEmpty() {
    assertFalse(getXincoCoreLanguages().isEmpty());
  }

  public void testIsLanguageUsed_existingLanguage_returnsTrue() {
    try {
      XincoCoreLanguageServer lang = new XincoCoreLanguageServer(2);
      assertTrue(isLanguageUsed(lang));
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testIsLanguageUsed_unusedLanguage_returnsFalse() {
    try {
      // Language 13 (zh_CN) likely has no data items in seed data
      XincoCoreLanguageServer lang = new XincoCoreLanguageServer(13);
      assertFalse(isLanguageUsed(lang));
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testLanguageServer_writeAndDelete() {
    try {
      XincoCoreLanguageServer lang = new XincoCoreLanguageServer(0, "test.lang.misc", "tl");
      lang.setChangerID(1);
      int id = lang.write2DB();
      assertTrue(id > 0);
      XincoCoreLanguageServer.deleteFromDB(lang, 1);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testGetXincoCoreGroups_returnsNonEmpty() {
    assertFalse(getXincoCoreGroups().isEmpty());
  }

  public void testGroupServer_constructor() {
    try {
      XincoCoreGroupServer group = new XincoCoreGroupServer(1);
      assertNotNull(group);
      assertEquals(1, group.getId());
      assertNotNull(group.getDesignation());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testGroupServer_writeAndDelete() {
    try {
      XincoCoreGroupServer group = new XincoCoreGroupServer(0, "test.group.misc", 1);
      group.setChangerID(1);
      int id = group.write2DB();
      assertTrue(id > 0);
      XincoCoreGroupServer.deleteFromDB(group);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testGetMembersOfGroup_adminGroup() {
    try {
      // Group 1 (admin) has at least the admin user as a member
      java.util.List<?> members = XincoCoreGroupServer.getMembersOfGroup(1);
      assertNotNull(members);
      assertTrue(members.size() >= 1);
    } catch (Exception e) {
      getLogger(getClass().getSimpleName()).log(java.util.logging.Level.SEVERE, null, e);
      fail();
    }
  }

  public void testUserServer_newUserLifecycle() {
    try {
      XincoCoreUserServer user =
          new XincoCoreUserServer(
              0,
              "testmiscuser",
              "hashedpw",
              "Misc",
              "Test",
              "misc@example.com",
              1,
              0,
              new java.sql.Timestamp(System.currentTimeMillis()));
      user.setHashPassword(false);
      user.setChangerID(1);
      int id = user.write2DB();
      assertTrue(id > 0);
      XincoCoreUserServer loaded = new XincoCoreUserServer(id);
      assertEquals("testmiscuser", loaded.getUsername());
      loaded.setStatusNumber(2);
      loaded.setChangerID(1);
      loaded.write2DB();
      assertEquals(2, new XincoCoreUserServer(id).getStatusNumber());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }
}
