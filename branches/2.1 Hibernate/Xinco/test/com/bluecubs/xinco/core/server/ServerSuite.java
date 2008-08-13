/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class ServerSuite extends TestCase {
    
    public ServerSuite(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("ServerSuite");
        suite.addTest(new TestSuite(com.bluecubs.xinco.core.server.XincoCoreUserServerTest.class));
        suite.addTest(new TestSuite(com.bluecubs.xinco.core.server.XincoCoreDataTypeServerTest.class));
        suite.addTest(new TestSuite(com.bluecubs.xinco.core.server.XincoCoreGroupServerTest.class));
        suite.addTest(new TestSuite(com.bluecubs.xinco.core.server.XincoCoreDataServerTest.class));
        suite.addTest(new TestSuite(com.bluecubs.xinco.core.server.XincoIDServerTest.class));
        suite.addTest(new TestSuite(com.bluecubs.xinco.core.server.XincoCoreACEServerTest.class));
        suite.addTest(new TestSuite(com.bluecubs.xinco.core.server.XincoCoreDataTypeAttributeServerTest.class));
        suite.addTest(new TestSuite(com.bluecubs.xinco.core.server.XincoCoreNodeServerTest.class));
        suite.addTest(new TestSuite(com.bluecubs.xinco.core.server.XincoCoreLogServerTest.class));
        suite.addTest(new TestSuite(com.bluecubs.xinco.core.server.XincoCoreLanguageServerTest.class));
        suite.addTest(new TestSuite(com.bluecubs.xinco.core.server.XincoDBManagerTest.class));
        suite.addTest(new TestSuite(com.bluecubs.xinco.core.server.XincoCoreUserHasXincoCoreGroupServerTest.class));
        suite.addTest(new TestSuite(com.bluecubs.xinco.core.server.XincoSettingServerTest.class));
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

}
