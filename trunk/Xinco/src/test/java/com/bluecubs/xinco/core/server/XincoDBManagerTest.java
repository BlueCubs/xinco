package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import static com.bluecubs.xinco.core.server.XincoDBManager.readFileAsString;
import java.io.File;
import java.io.IOException;
import static java.lang.System.getProperty;
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
            File initFile = new File(getProperty("user.dir")
                    + getProperty("file.separator") + "src"
                    + getProperty("file.separator") + "main"
                    + getProperty("file.separator") + "resources"
                    + getProperty("file.separator") + "db"
                    + getProperty("file.separator") + "migration"
                    + getProperty("file.separator")
                    + "V1_1__Base_Version.sql");
            ArrayList<String> statements
                    = readFileAsString(initFile.getAbsolutePath(), null);
            assertTrue(!statements.isEmpty());
        } catch (IOException | XincoException ex) {
            getLogger(XincoDBManager.class.getName()).log(SEVERE, null, ex);
            fail();
        }
    }
}
