package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.XincoVersion;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreLogServerTest extends TestCase {

    public XincoCoreLogServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreLogServerTest.class);
        return suite;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of setUser method, of class XincoCoreLogServer.
     */
    public void testSetUser() {
        try {
            System.out.println("setUser");
            XincoCoreUserServer user = new XincoCoreUserServer(1);
            XincoCoreLogServer instance = new XincoCoreLogServer(1);
            instance.setUser(user);
            assertTrue(instance.user.getId() == 1);
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreLogServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreLogs method, of class XincoCoreLogServer.
     */
    public void testGetXincoCoreLogs() {
        try {
            System.out.println("getXincoCoreLogs");
            int attrID = 1;
            Vector result = XincoCoreLogServer.getXincoCoreLogs(attrID);
            assertTrue(result.size() > 0);
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreLogServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getNewID method, of class XincoCoreLogServer.
     */
    public void testGetNewID() {
        try {
            System.out.println("getNewID");
            XincoCoreLogServer instance = new XincoCoreLogServer(1);
            int result = instance.getNewID(true);
            System.out.println("New id: " + result);
            assertTrue(result > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLogServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreLogServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreLogServer.
     */
    public void testWrite2DBAndDelete() {
        try {
            System.out.println("write2DB");
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(0, "test", "test");
            System.out.println("Instance id before writing: " + instance.getId());
            assertTrue(instance.write2DB());
            System.out.println("Instance id after writing: " + instance.getId());
            assertTrue(instance.getId() > 0);
            instance.setChangerID(1);
            assertTrue(instance.deleteFromDB());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getVersion method, of class XincoCoreLogServer.
     */
    public void testGetVersion() {
        try {
            System.out.println("getVersion");
            XincoCoreLogServer instance = new XincoCoreLogServer(1);
            XincoVersion result = instance.getVersion();
            assertTrue(result.getVersionHigh() > 0);
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
