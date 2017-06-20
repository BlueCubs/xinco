package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import static com.bluecubs.xinco.core.server.XincoCoreACEServer.checkAccess;
import static com.bluecubs.xinco.core.server.XincoCoreACEServer.getXincoCoreACL;
import static com.bluecubs.xinco.core.server.XincoCoreACEServer.removeFromDB;
import static java.lang.System.out;
import java.util.ArrayList;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoCoreACEServerTest extends AbstractXincoDataBaseTestCase {

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
            assertTrue(getXincoCoreACL(attrID, attrT).size() > 0);
        } catch (Exception e) {
            getLogger(XincoCoreACEServerTest.class.getSimpleName()).log(SEVERE, null, e);
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
            assertTrue(checkAccess(user, (ArrayList) node.getXincoCoreAcl()).isReadPermission());
        } catch (XincoException ex) {
            getLogger(XincoCoreACEServerTest.class.getSimpleName()).log(SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreACEServer.
     */
    public void testWrite2DB() {
        try {
            out.println("write2DB");
            XincoCoreACEServer instance = new XincoCoreACEServer(0, 0, 1, 1, 0, true, true, true, true);
            instance.setChangerID(1);
            int id = instance.write2DB();
            assertTrue(id > 0);
            assertTrue(removeFromDB(instance, 1) == 0);
        } catch (XincoException ex) {
            getLogger(XincoCoreACEServerTest.class.getSimpleName()).log(SEVERE, null, ex);
            fail();
        }
    }
}
