package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreGroupServerTest extends XincoTestCase {
    
    public XincoCoreGroupServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreGroupServerTest.class);
        return suite;
    }
    
    /**
     * Test of write2DB method, of class XincoCoreGroupServer.
     */
    public void testWrite2DB(){
        try {
            XincoCoreGroupServer instance = new XincoCoreGroupServer(0,"Test",1);
            assertTrue(instance.write2DB()>0);
            XincoCoreGroupServer.deleteFromDB(instance);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreGroupServerTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreGroups method, of class XincoCoreGroupServer.
     */
    public void testGetXincoCoreGroups() {
        assertTrue(XincoCoreGroupServer.getXincoCoreGroups().size()>0);
    }
}
