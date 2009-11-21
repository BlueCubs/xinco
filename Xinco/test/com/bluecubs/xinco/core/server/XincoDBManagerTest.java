package com.bluecubs.xinco.core.server;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoDBManagerTest extends TestCase {

    public XincoDBManagerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoDBManagerTest.class);
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
     * Test of getNewID method, of class XincoDBManager.
     * @throws Exception
     */
    public void testGetNewID() throws Exception {
        System.out.println("getNewID");
        try {
            String attrTN = "xinco_core_data";
            assertTrue(XincoDBManager.getNewID(attrTN) > 0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
