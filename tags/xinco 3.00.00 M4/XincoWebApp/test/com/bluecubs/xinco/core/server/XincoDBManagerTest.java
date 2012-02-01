package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.db.DBState;
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

    public void testReadFileAsString() {
        try {
            ArrayList<String> statements =
                    XincoDBManager.readFileAsString("script/init.sql", DBState.class);
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
