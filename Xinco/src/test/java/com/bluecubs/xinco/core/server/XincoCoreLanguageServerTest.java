package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import static com.bluecubs.xinco.core.server.XincoCoreLanguageServer.deleteFromDB;
import static com.bluecubs.xinco.core.server.XincoCoreLanguageServer.getXincoCoreLanguages;
import static com.bluecubs.xinco.core.server.XincoCoreLanguageServer.isLanguageUsed;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class XincoCoreLanguageServerTest extends AbstractXincoDataBaseTestCase {

    public XincoCoreLanguageServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreLanguageServerTest.class);
        return suite;
    }

    /**
     * Test of write2DB method, of class XincoCoreLanguageServer.
     */
    public void testWrite2DB() {
        try {
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(0, "Test", "Test description");
            int id = instance.write2DB();
            assertTrue(id > 0);
            instance = new XincoCoreLanguageServer(id);
            assertTrue(deleteFromDB(instance, 1) == 0);
        } catch (XincoException ex) {
            getLogger(XincoCoreLanguageServerTest.class.getSimpleName()).log(SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreLanguages method, of class XincoCoreLanguageServer.
     */
    public void testGetXincoCoreLanguages() {
        assertTrue(getXincoCoreLanguages().size() > 0);
    }

    /**
     * Test of isLanguageUsed method, of class XincoCoreLanguageServer.
     */
    public void testIsLanguageUsed() {
        try {
            assertFalse(isLanguageUsed(new XincoCoreLanguageServer(3)));
            assertTrue(isLanguageUsed(new XincoCoreLanguageServer(2)));
        } catch (XincoException ex) {
            getLogger(XincoCoreLanguageServerTest.class.getSimpleName()).log(SEVERE, null, ex);
            fail();
        }
    }
}
