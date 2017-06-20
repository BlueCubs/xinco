package com.bluecubs.xinco.core.server;

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
}
