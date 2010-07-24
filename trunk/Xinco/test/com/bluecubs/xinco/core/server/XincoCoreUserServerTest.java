package com.bluecubs.xinco.core.server;

import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreUserServerTest extends XincoTestCase {

    private String originalPassword;

    public XincoCoreUserServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreUserServerTest.class);
        return suite;
    }
    
    /**
     * Test of getAttempts method, of class XincoCoreUserServer.
     */
    public void testGetAttempts() {
        try {
            System.out.println("getAttempts");
            XincoCoreUserServer instance = new XincoCoreUserServer(1);
            assertTrue(instance.getAttempts() >= 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of setAttempts method, of class XincoCoreUserServer.
     */
    public void testSetAttempts() {
        try {
            System.out.println("setAttempts");
            XincoCoreUserServer instance = new XincoCoreUserServer(1);
            int attempts = instance.getAttempts();
            instance.setAttempts(attempts + 1);
            instance.write2DB();
            instance = new XincoCoreUserServer(1);
            assertTrue(instance.getAttempts() == attempts + 1);
            //Clean up
            instance.setAttempts(0);
            instance.write2DB();
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreUsers method, of class XincoCoreUserServer.
     */
    public void testGetXincoCoreUsers() {
        System.out.println("getXincoCoreUsers");
        assertTrue(XincoCoreUserServer.getXincoCoreUsers().size() > 0);
    }

    /**
     * Test of isPasswordUsable method, of class XincoCoreUserServer.
     */
    public void testIsPasswordUsable() {
        try {
            System.out.println("isPasswordUsable");
            String newPass = "test";
            XincoCoreUserServer instance = new XincoCoreUserServer(1);
            instance.setReason("Test");
            originalPassword = instance.getUserpassword();
            //Check against current password
            assertFalse(instance.isPasswordUsable(instance.getUserpassword(), false));
            //Check against recently updated password
            instance.setUserpassword(newPass);
            instance.setHashPassword(true);
            instance.write2DB();
            assertFalse(instance.isPasswordUsable(newPass));
            //Change last modified out of invalid range
            GregorianCalendar cal = new GregorianCalendar();
            int val = Integer.valueOf(ResourceBundle.getBundle("com.bluecubs.xinco.settings.settings").getString("password.aging")) + 1;
            cal.add(GregorianCalendar.DATE, -val);
            instance.setLastModified(new Timestamp(cal.getTimeInMillis()));
            instance.setUserpassword(newPass + "*");
            instance.setHashPassword(true);
            instance.write2DB();
            assertTrue(instance.isPasswordUsable(newPass));
            //clean up
            instance.setUserpassword(originalPassword);
            instance.setHashPassword(false);
            instance.setLastModified(new Timestamp(new Date().getTime()));
            instance.write2DB();
        } catch (XincoException ex) {
            fail();
        }
    }

    /**
     * Test of getAttempts method, of class XincoCoreUserServer.
     */
    public void testLogin() {
        try {
            System.out.println("Login");
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            assertTrue((instance.getXinco_core_groups()).size() >= 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
