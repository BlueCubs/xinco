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
public class XincoCoreLanguageServerTest extends TestCase {

    public XincoCoreLanguageServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreLanguageServerTest.class);
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
     * Test of write2DB method, of class XincoCoreLanguageServer.
     */
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(0, "Test", "Test description");
            int id = instance.write2DB();
            assertTrue(id > 0);
            instance = new XincoCoreLanguageServer(id);
            assertTrue(XincoCoreLanguageServer.deleteFromDB(instance, 1) == 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreLanguages method, of class XincoCoreLanguageServer.
     */
    public void testGetXincoCoreLanguages() {
        System.out.println("getXincoCoreLanguages");
        assertTrue(XincoCoreLanguageServer.getXincoCoreLanguages().size() > 0);
    }

    /**
     * Test of isLanguageUsed method, of class XincoCoreLanguageServer.
     */
    public void testIsLanguageUsed() {
        try {
            System.out.println("isLanguageUsed");
            assertFalse(XincoCoreLanguageServer.isLanguageUsed(new XincoCoreLanguageServer(3)));
            assertTrue(XincoCoreLanguageServer.isLanguageUsed(new XincoCoreLanguageServer(2)));
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
