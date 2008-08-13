/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoVersion;
import java.util.Vector;
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
        System.out.println("setUser");
        XincoCoreUserServer user = null;
        XincoCoreLogServer instance = null;
        instance.setUser(user);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXincoCoreLogs method, of class XincoCoreLogServer.
     */
    public void testGetXincoCoreLogs() {
        System.out.println("getXincoCoreLogs");
        int attrID = 0;
        Vector expResult = null;
        Vector result = XincoCoreLogServer.getXincoCoreLogs(attrID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNewID method, of class XincoCoreLogServer.
     */
    public void testGetNewID() {
        System.out.println("getNewID");
        boolean a = false;
        XincoCoreLogServer instance = null;
        int expResult = 0;
        int result = instance.getNewID(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write2DB method, of class XincoCoreLogServer.
     */
    public void testWrite2DB() {
        System.out.println("write2DB");
        XincoCoreLogServer instance = null;
        boolean expResult = false;
        boolean result = instance.write2DB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreLogServer.
     */
    public void testDeleteFromDB() {
        System.out.println("deleteFromDB");
        XincoCoreLogServer instance = null;
        boolean expResult = false;
        boolean result = instance.deleteFromDB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVersion method, of class XincoCoreLogServer.
     */
    public void testGetVersion() {
        System.out.println("getVersion");
        XincoCoreLogServer instance = null;
        XincoVersion expResult = null;
        XincoVersion result = instance.getVersion();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setVersion method, of class XincoCoreLogServer.
     */
    public void testSetVersion() {
        System.out.println("setVersion");
        XincoVersion version = null;
        XincoCoreLogServer instance = null;
        instance.setVersion(version);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
