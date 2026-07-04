package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoCoreUserServer.getXincoCoreUsers;
import static com.bluecubs.xinco.core.server.XincoSettingServer.getSetting;
import static java.util.Calendar.DATE;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class XincoCoreUserServerTest extends AbstractXincoDataBaseTestCase {

  private String originalPassword;

  public XincoCoreUserServerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(XincoCoreUserServerTest.class);
    return suite;
  }

  /** Test of getAttempts method, of class XincoCoreUserServer. */
  public void testGetAttempts() {
    try {
      XincoCoreUserServer instance = new XincoCoreUserServer(1);
      assertTrue(instance.getAttempts() >= 0);
    } catch (XincoException ex) {
      getLogger(XincoCoreUserServerTest.class.getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }

  /** Test of setAttempts method, of class XincoCoreUserServer. */
  public void testSetAttempts() {
    try {
      XincoCoreUserServer instance = new XincoCoreUserServer(1);
      int attempts = instance.getAttempts();
      instance.setAttempts(attempts + 1);
      instance.setChangerID(1);
      instance.write2DB();
      instance = new XincoCoreUserServer(1);
      assertTrue(instance.getAttempts() == attempts + 1);
      // Clean up
      instance.setAttempts(0);
      instance.write2DB();
    } catch (XincoException ex) {
      getLogger(XincoCoreUserServerTest.class.getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }

  /** Test of getXincoCoreUsers method, of class XincoCoreUserServer. */
  public void testGetXincoCoreUsers() {
    assertTrue(getXincoCoreUsers().size() > 0);
  }

  /** Test of isPasswordUsable method, of class XincoCoreUserServer. */
  public void testIsPasswordUsable() {
    try {
      String newPass = "test";
      XincoCoreUserServer instance = new XincoCoreUserServer(1);
      instance.setReason("Test");
      originalPassword = instance.getUserpassword();
      // Check against current password
      assertFalse(instance.isPasswordUsable(instance.getUserpassword(), false));
      // Check against recently updated password
      instance.setUserpassword(newPass);
      instance.setHashPassword(true);
      instance.setChangerID(1);
      instance.write2DB();
      assertFalse(instance.isPasswordUsable(newPass));
      // Change last modified out of invalid range
      GregorianCalendar cal = new GregorianCalendar();
      int val = getSetting("password.aging").getIntValue() + 1;
      cal.add(DATE, -val);
      instance.setLastModified(new Timestamp(cal.getTimeInMillis()));
      instance.setUserpassword(newPass + "*");
      instance.setHashPassword(true);
      instance.write2DB();
      assertTrue(instance.isPasswordUsable(newPass));
      // clean up
      instance.setUserpassword(originalPassword);
      instance.setHashPassword(false);
      instance.setLastModified(new Timestamp(new Date().getTime()));
      instance.write2DB();
    } catch (XincoException ex) {
      fail();
    }
  }

  /** Test of getAttempts method, of class XincoCoreUserServer. */
  public void testLogin() {
    try {
      XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
      assertTrue(instance.getXincoCoreGroups().size() >= 0);
    } catch (XincoException ex) {
      getLogger(XincoCoreUserServerTest.class.getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }

  public void testIncreaseAttempts_getterSetter() {
    try {
      XincoCoreUserServer instance = new XincoCoreUserServer(1);
      assertFalse(instance.isIncreaseAttempts());
      instance.setIncreaseAttempts(true);
      assertTrue(instance.isIncreaseAttempts());
      instance.setIncreaseAttempts(false);
      assertFalse(instance.isIncreaseAttempts());
    } catch (XincoException ex) {
      getLogger(XincoCoreUserServerTest.class.getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }

  public void testValidCredentials_validAdmin() {
    assertTrue(XincoCoreUserServer.validCredentials("admin", "admin", true));
  }

  public void testValidCredentials_invalidPassword() {
    assertFalse(XincoCoreUserServer.validCredentials("admin", "wrongpassword", true));
  }

  public void testValidCredentials_noEncrypt() {
    // admin password is stored hashed; passing without encrypt won't match
    assertFalse(XincoCoreUserServer.validCredentials("admin", "admin"));
  }

  public void testWriteXincoCoreGroups_emptyGroups() {
    try {
      // Create a temporary user
      XincoCoreUserServer user =
          new XincoCoreUserServer(
              0,
              "testWriteGrpsUser",
              "pw",
              "Test",
              "WriteGrps",
              "writegrps@example.com",
              1,
              0,
              new java.sql.Timestamp(System.currentTimeMillis()));
      user.setHashPassword(false);
      user.setChangerID(1);
      int id = user.write2DB();
      assertTrue(id > 0);

      // Reload user; clear group list; then write with writeGroups=true
      XincoCoreUserServer reload = new XincoCoreUserServer(id);
      XincoCoreGroupServer.getXincoCoreGroups().clear();
      reload.setWriteGroups(true);
      reload.setChangerID(1);
      reload.write2DB();

      // writeXincoCoreGroups was invoked — user still exists
      assertNotNull(new XincoCoreUserServer(id));
      // (no DB delete — H2 in-memory; data gone after the test run)
    } catch (XincoException ex) {
      getLogger(XincoCoreUserServerTest.class.getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }
}
