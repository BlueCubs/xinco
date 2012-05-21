package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoCoreACEServerTest extends XincoTestCase {

    public XincoCoreACEServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreACEServerTest.class);
        return suite;
    }

    /**
     * Test of getXincoCoreACL method, of class XincoCoreACEServer.
     */
    public void testGetXincoCoreACL() {
        try {
            int attrID = 1;
            String attrT = "xincoCoreUser.id";
            assertTrue(XincoCoreACEServer.getXincoCoreACL(attrID, attrT).size() > 0);
        } catch (Exception e) {
            Logger.getLogger(XincoCoreACEServerTest.class.getSimpleName()).log(Level.SEVERE, null, e);
            fail();
        }
    }

    /**
     * Test of checkAccess method, of class XincoCoreACEServer.
     */
    public void testCheckAccess() {
        try {
            XincoCoreUserServer user = new XincoCoreUserServer(1);
            XincoCoreNodeServer node = new XincoCoreNodeServer(2);
            assertTrue(XincoCoreACEServer.checkAccess(user, (ArrayList) node.getXincoCoreAcl()).isReadPermission());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreACEServer.
     */
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            XincoCoreACEServer instance = new XincoCoreACEServer(0, 0, 1, 1, 0, true, true, true, true);
            instance.setChangerID(1);
            int id = instance.write2DB();
            assertTrue(id > 0);
            assertTrue(XincoCoreACEServer.removeFromDB(instance, 1) == 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
