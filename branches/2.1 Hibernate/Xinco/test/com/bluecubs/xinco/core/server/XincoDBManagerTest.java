/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoDBManagerTest extends TestCase {
    
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

    /**
     * Test of getNewID method, of class XincoDBManager.
     */
    public void testGetNewID() throws Exception {
        System.out.println("getNewID");
        String attrTN = "";
        XincoDBManager instance = new XincoDBManager();
        int expResult = 0;
        int result = instance.getNewID(attrTN);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of drawTable method, of class XincoDBManager.
     */
    public void testDrawTable() {
        System.out.println("drawTable");
        ResultSet rs = null;
        PrintWriter out = null;
        String header = "";
        String title = "";
        int columnAsLink = 0;
        boolean details = false;
        int linkType = 0;
        XincoDBManager instance = new XincoDBManager();
        instance.drawTable(rs, out, header, title, columnAsLink, details, linkType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getColumnNamesList method, of class XincoDBManager.
     */
    public void testGetColumnNamesList() {
        System.out.println("getColumnNamesList");
        ResultSet rs = null;
        XincoDBManager instance = new XincoDBManager();
        StringTokenizer expResult = null;
        StringTokenizer result = instance.getColumnNamesList(rs);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getColumnNames method, of class XincoDBManager.
     */
    public void testGetColumnNames() {
        System.out.println("getColumnNames");
        ResultSet rs = null;
        XincoDBManager instance = new XincoDBManager();
        String expResult = "";
        String result = instance.getColumnNames(rs);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createdQuery method, of class XincoDBManager.
     */
    public void testCreatedQuery() {
        System.out.println("createdQuery");
        String query = "";
        XincoDBManager instance = new XincoDBManager();
        List expResult = null;
        List result = instance.createdQuery(query);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWebBlockRightClickScript method, of class XincoDBManager.
     */
    public void testGetWebBlockRightClickScript() {
        System.out.println("getWebBlockRightClickScript");
        XincoDBManager instance = new XincoDBManager();
        String expResult = "";
        String result = instance.getWebBlockRightClickScript();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of localizeString method, of class XincoDBManager.
     */
    public void testLocalizeString() {
        System.out.println("localizeString");
        String s = "";
        XincoDBManager instance = new XincoDBManager();
        String expResult = "";
        String result = instance.localizeString(s);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getResourceBundle method, of class XincoDBManager.
     */
    public void testGetResourceBundle() {
        System.out.println("getResourceBundle");
        XincoDBManager instance = new XincoDBManager();
        ResourceBundle expResult = null;
        ResourceBundle result = instance.getResourceBundle();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createAndLoadLocale method, of class XincoDBManager.
     */
    public void testCreateAndLoadLocale() {
        System.out.println("createAndLoadLocale");
        String locale = "";
        XincoDBManager instance = new XincoDBManager();
        instance.createAndLoadLocale(locale);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLocale method, of class XincoDBManager.
     */
    public void testSetLocale() {
        System.out.println("setLocale");
        Locale loc = null;
        XincoDBManager instance = new XincoDBManager();
        instance.setLocale(loc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setResourceBundle method, of class XincoDBManager.
     */
    public void testSetResourceBundle() {
        System.out.println("setResourceBundle");
        ResourceBundle lrb = null;
        XincoDBManager instance = new XincoDBManager();
        instance.setResourceBundle(lrb);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isTesting method, of class XincoDBManager.
     */
    public void testIsTesting() {
        System.out.println("isTesting");
        XincoDBManager instance = new XincoDBManager();
        boolean expResult = false;
        boolean result = instance.isTesting();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTesting method, of class XincoDBManager.
     */
    public void testSetTesting() {
        System.out.println("setTesting");
        boolean testing = false;
        XincoDBManager instance = new XincoDBManager();
        instance.setTesting(testing);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
