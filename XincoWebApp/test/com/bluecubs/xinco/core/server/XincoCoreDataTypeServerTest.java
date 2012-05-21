package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.service.XincoCoreDataTypeAttribute;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoCoreDataTypeServerTest extends XincoTestCase {

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
            XincoCoreDataTypeServer.deleteFromDB(instance);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreGroupServerTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreDataTypes method, of class XincoCoreDataTypeServer.
     */
    public void testGetXincoCoreDataTypes() {
        assertTrue(XincoCoreDataTypeServer.getXincoCoreDataTypes().size() > 0);
    }
}
