package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import static com.bluecubs.xinco.core.server.XincoCoreDataTypeServer.deleteFromDB;
import static com.bluecubs.xinco.core.server.XincoCoreDataTypeServer.getXincoCoreDataTypes;
import com.bluecubs.xinco.core.server.service.XincoCoreDataTypeAttribute;
import java.util.ArrayList;
import java.util.logging.Level;
import static java.util.logging.Level.SEVERE;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoCoreDataTypeServerTest extends AbstractXincoDataBaseTestCase {

    public XincoCoreDataTypeServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreDataTypeServerTest.class);
        return suite;
    }

    /**
     * Test of getXincoCoreDataTypes method, of class XincoCoreDataTypeServer.
     */
    public void testWrite2DB() {
        try {
            XincoCoreDataTypeServer instance = new XincoCoreDataTypeServer(0, "Test", "Test desc", new ArrayList<XincoCoreDataTypeAttribute>());
            assertTrue(instance.write2DB() > 0);
            deleteFromDB(instance);
        } catch (XincoException ex) {
            getLogger(XincoCoreGroupServerTest.class.getSimpleName()).log(SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreDataTypes method, of class XincoCoreDataTypeServer.
     */
    public void testGetXincoCoreDataTypes() {
        assertTrue(getXincoCoreDataTypes().size() > 0);
    }
}
