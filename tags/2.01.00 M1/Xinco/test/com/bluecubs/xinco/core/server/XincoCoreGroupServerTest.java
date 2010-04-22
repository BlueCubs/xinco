package com.bluecubs.xinco.core.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultr�n <javier.ortiz.78@gmail.com>
 */
public class XincoCoreGroupServerTest extends TestCase {
    
    public XincoCoreGroupServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreGroupServerTest.class);
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
     * Test of write2DB method, of class XincoCoreGroupServer.
     */
    public void testWrite2DB(){
        try {
            System.out.println("write2DB");
            XincoCoreGroupServer instance = new XincoCoreGroupServer(0,"Test",1);
            int result = instance.write2DB();
            assertTrue(result>0);
            XincoCoreGroupServer.deleteFromDB(instance);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreGroups method, of class XincoCoreGroupServer.
     */
    public void testGetXincoCoreGroups() {
        System.out.println("getXincoCoreGroups");
        assertTrue(XincoCoreGroupServer.getXincoCoreGroups().size()>0);
    }
}