package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import static com.bluecubs.xinco.core.server.XincoDependencyTypeServer.deleteFromDB;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import org.junit.Test;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoDependencyTypeServerTest extends AbstractXincoDataBaseTestCase {

    public XincoDependencyTypeServerTest(String testName) {
        super(testName);
    }

    /**
     * Test of write2DB and deleteFromDB methods, of class
     * XincoDependencyTypeServer.
     */
    @Test
    public void testWriteandDelete() {
        try {
            XincoDependencyTypeServer instance = new XincoDependencyTypeServer(0, 1, "test");
            assertTrue(instance.write2DB() > 0);
            assertEquals(deleteFromDB(instance.getId()), 0);
        } catch (XincoException ex) {
            getLogger(XincoDependencyTypeServerTest.class.getName()).log(SEVERE, null, ex);
        }
    }
}
