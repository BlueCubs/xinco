package com.bluecubs.xinco.core.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoDBManagerTest extends XincoTestCase {

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
    
    public void testReadFileAsString(){
        try {
            ArrayList<String> statements=
                    XincoDBManager.readFileAsString("xinco_MySQL.sql",getClass());
            assertTrue(!statements.isEmpty());
        } catch (IOException ex) {
            Logger.getLogger(XincoDBManager.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (XincoException ex) {
            Logger.getLogger(XincoDBManager.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
