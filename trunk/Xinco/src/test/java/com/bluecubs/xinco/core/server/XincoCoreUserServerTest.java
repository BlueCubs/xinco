package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import static com.bluecubs.xinco.core.server.XincoCoreUserServer.getXincoCoreUsers;
import static com.bluecubs.xinco.core.server.XincoSettingServer.getSetting;
import java.sql.Timestamp;
import static java.util.Calendar.DATE;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import static java.util.logging.Level.SEVERE;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
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

    /**
     * Test of getAttempts method, of class XincoCoreUserServer.
     */
    public void testGetAttempts() {
        try {
            XincoCoreUserServer instance = new XincoCoreUserServer(1);
            assertTrue(instance.getAttempts() >= 0);
        } catch (XincoException ex) {
            getLogger(XincoCoreUserServerTest.class.getSimpleName()).log(SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of setAttempts method, of class XincoCoreUserServer.
     */
    public void testSetAttempts() {
        try {
            XincoCoreUserServer instance = new XincoCoreUserServer(1);
            int attempts = instance.getAttempts();
            instance.setAttempts(attempts + 1);
            instance.setChangerID(1);
            instance.write2DB();
            instance = new XincoCoreUserServer(1);
            assertTrue(instance.getAttempts() == attempts + 1);
            //Clean up
            instance.setAttempts(0);
            instance.write2DB();
        } catch (XincoException ex) {
            getLogger(XincoCoreUserServerTest.class.getSimpleName()).log(SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreUsers method, of class XincoCoreUserServer.
     */
    public void testGetXincoCoreUsers() {
        assertTrue(getXincoCoreUsers().size() > 0);
    }

    /**
     * Test of isPasswordUsable method, of class XincoCoreUserServer.
     */
    public void testIsPasswordUsable() {
        try {
            String newPass = "test";
            XincoCoreUserServer instance = new XincoCoreUserServer(1);
            instance.setReason("Test");
            originalPassword = instance.getUserpassword();
            //Check against current password
            assertFalse(instance.isPasswordUsable(instance.getUserpassword(), false));
            //Check against recently updated password
            instance.setUserpassword(newPass);
            instance.setHashPassword(true);
            instance.setChangerID(1);
            instance.write2DB();
            assertFalse(instance.isPasswordUsable(newPass));
            //Change last modified out of invalid range
            GregorianCalendar cal = new GregorianCalendar();
            int val = getSetting("password.aging").getIntValue() + 1;
            cal.add(DATE, -val);
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
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            assertTrue((instance.getXincoCoreGroups()).size() >= 0);
        } catch (XincoException ex) {
            getLogger(XincoCoreUserServerTest.class.getSimpleName()).log(SEVERE, null, ex);
            fail();
        }
    }
}
