/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.email;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoMailerTest extends TestCase {
    
    public XincoMailerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoMailerTest.class);
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

    /**
     * Test of main method, of class XincoMailer.
     */
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        XincoMailer.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setHost method, of class XincoMailer.
     */
    public void testSetHost() {
        System.out.println("setHost");
        String host = "";
        XincoMailer instance = new XincoMailer();
        instance.setHost(host);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAuthenticationUser method, of class XincoMailer.
     */
    public void testSetAuthenticationUser() {
        System.out.println("setAuthenticationUser");
        String user = "";
        XincoMailer instance = new XincoMailer();
        instance.setAuthenticationUser(user);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAuthenticationPassword method, of class XincoMailer.
     */
    public void testSetAuthenticationPassword() {
        System.out.println("setAuthenticationPassword");
        String pass = "";
        XincoMailer instance = new XincoMailer();
        instance.setAuthenticationPassword(pass);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of postMail method, of class XincoMailer.
     */
    public void testPostMail() throws Exception {
        System.out.println("postMail");
        String[] recipients = null;
        String subject = "";
        String message = "";
        String from = "";
        XincoMailer instance = new XincoMailer();
        instance.postMail(recipients, subject, message, from);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
