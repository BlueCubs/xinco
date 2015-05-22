package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoDBManagerTest extends AbstractXincoDataBaseTestCase {

    public XincoDBManagerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoDBManagerTest.class);
        return suite;
    }

    public void testReadFileAsString() {
        try {
            File initFile = new File(System.getProperty("user.dir")
                    + System.getProperty("file.separator") + "src"
                    + System.getProperty("file.separator") + "main"
                    + System.getProperty("file.separator") + "resources"
                    + System.getProperty("file.separator") + "db"
                    + System.getProperty("file.separator") + "migration"
                    + System.getProperty("file.separator")
                    + "V1_1__Base_Version.sql");
            ArrayList<String> statements
                    = XincoDBManager.readFileAsString(initFile.getAbsolutePath(), null);
            assertTrue(!statements.isEmpty());
        } catch (IOException | XincoException ex) {
            Logger.getLogger(XincoDBManager.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
