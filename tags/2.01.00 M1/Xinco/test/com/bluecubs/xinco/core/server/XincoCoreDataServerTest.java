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
public class XincoCoreDataServerTest extends TestCase {

    public XincoCoreDataServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreDataServerTest.class);
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
     * Test of write2DB method, of class XincoCoreDataServer.
     */
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            XincoCoreDataServer instance = new XincoCoreDataServer(0, 1, 1, 1, "Test Data", 1);
            assertTrue(instance.write2DB() > 0);
            System.out.println("deleteFromDB");
            assertTrue(instance.deleteFromDB() == 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findXincoCoreData method, of class XincoCoreDataServer.
     */
    public void testFindXincoCoreData() {
        System.out.println("findXincoCoreData");
        String attrS = "xinco.org";
        int attrLID = 2;
        boolean attrSA = false;
        boolean attrSFD = false;
        assertTrue(XincoCoreDataServer.findXincoCoreData(attrS, attrLID, attrSA, attrSFD).size()>0);
        attrSA = true;
        assertTrue(XincoCoreDataServer.findXincoCoreData(attrS, attrLID, attrSA, attrSFD).size()>0);
        attrSFD = true;
        assertTrue(XincoCoreDataServer.findXincoCoreData(attrS, attrLID, attrSA, attrSFD).size()>0);
        attrS = "none";
        assertTrue(XincoCoreDataServer.findXincoCoreData(attrS, attrLID, attrSA, attrSFD).size()==0);
    }
}