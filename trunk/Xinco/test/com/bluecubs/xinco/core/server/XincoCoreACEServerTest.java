package com.bluecubs.xinco.core.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreACEServerTest extends TestCase {

    public XincoCoreACEServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreACEServerTest.class);
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
     * Test of write2DB method, of class XincoCoreACEServer.
     */
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            XincoCoreACEServer instance = new XincoCoreACEServer(0, 0, 1, 1, 0, true, true, true, true);
            int id = instance.write2DB();
            assertTrue(id > 0);
            System.out.println("deleteFromDB");
            assertTrue(XincoCoreACEServer.removeFromDB(instance, 1) == 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreACL method, of class XincoCoreACEServer.
     */
    public void testGetXincoCoreACL() {
        try {
            System.out.println("getXincoCoreACL");
            int attrID = 1;
            String attrT = "xincoCoreUserId.id";
            assertTrue(XincoCoreACEServer.getXincoCoreACL(attrID, attrT).size() > 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of checkAccess method, of class XincoCoreACEServer.
     */
    public void testCheckAccess() {
        try {
            System.out.println("checkAccess");
            XincoCoreUserServer user = new XincoCoreUserServer(1);
            XincoCoreNodeServer node = new XincoCoreNodeServer(2);
            assertTrue(XincoCoreACEServer.checkAccess(user, node.getXinco_core_acl()).isRead_permission());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
