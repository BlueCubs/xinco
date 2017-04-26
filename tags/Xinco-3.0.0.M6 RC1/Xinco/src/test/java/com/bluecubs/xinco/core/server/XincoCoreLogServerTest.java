package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoCoreLogServerTest extends AbstractXincoDataBaseTestCase {

    public XincoCoreLogServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreLogServerTest.class);
        return suite;
    }

    /**
     * Test of setUser method, of class XincoCoreLogServer.
     */
    public void testSetUser() {
        try {
            XincoCoreUserServer user = new XincoCoreUserServer(2);
            XincoCoreLogServer instance = new XincoCoreLogServer(1);
            instance.setUser(user);
            instance.write2DB();
            instance = new XincoCoreLogServer(1);
            assertTrue(instance.getXincoCoreUserId() == user.getId());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLogServerTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreLogs method, of class XincoCoreLogServer.
     */
    public void testGetXincoCoreLogs() {
        assertTrue(XincoCoreLogServer.getXincoCoreLogs(1).size() > 0);
    }
}