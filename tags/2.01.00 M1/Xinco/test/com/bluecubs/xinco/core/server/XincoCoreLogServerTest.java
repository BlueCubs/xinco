package com.bluecubs.xinco.core.server;

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
            XincoCoreUserServer user = new XincoCoreUserServer(2);
            XincoCoreLogServer instance = new XincoCoreLogServer(1);
            instance.setUser(user);
            instance.write2DB();
            instance = new XincoCoreLogServer(1);
            assertTrue(instance.getXinco_core_user_id()==user.getId());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLogServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreLogs method, of class XincoCoreLogServer.
     */
    public void testGetXincoCoreLogs() {
        System.out.println("getXincoCoreLogs");
        assertTrue(XincoCoreLogServer.getXincoCoreLogs(1).size()>0);
    }
}
