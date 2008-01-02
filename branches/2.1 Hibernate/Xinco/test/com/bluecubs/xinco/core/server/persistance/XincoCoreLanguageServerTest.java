/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistance.XincoCoreLanguage;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAbstractAuditableObject;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoCoreLanguageServerTest {

    public XincoCoreLanguageServerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getXincoCoreLanguages method, of class XincoCoreLanguageServer.
     */
    @Test
    public void getXincoCoreLanguages() {
        System.out.println("getXincoCoreLanguages");
        Vector result = XincoCoreLanguageServer.getXincoCoreLanguages();
        assertTrue(result.size() > 0);
    }

    /**
     * Test of isLanguageUsed method, of class XincoCoreLanguageServer.
     */
    @Test
    public void isLanguageUsed() {
        System.out.println("isLanguageUsed");
        XincoCoreLanguage xcl = new XincoCoreLanguage(1);
        assertTrue(XincoCoreLanguageServer.isLanguageUsed(xcl));
    }

    /**
     * Test of findById method, of class XincoCoreLanguageServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findById() {
        try {
            System.out.println("findById");
            HashMap parameters = new HashMap();
            parameters.put("id", 1);
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(1);
            XincoAbstractAuditableObject result = instance.findById(parameters);
            assertTrue(((XincoCoreLanguage) result).getSign().equals("n/a"));
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreLanguageServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findWithDetails() {
        try {
            System.out.println("findWithDetails");
            HashMap parameters = new HashMap();
            parameters.put("designation", "unknown");
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(1);
            XincoAbstractAuditableObject[] result = instance.findWithDetails(parameters);
            assertTrue(((XincoCoreLanguage) result[0]).getSign().equals("n/a"));
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreLanguageServer.
     * @throws java.lang.Exception 
     */
    @Test
    public void write2DB() throws Exception {
        try {
            System.out.println("write2DB");
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(0, "t", "test");
            assertTrue(instance.write2DB());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreLanguageServer.
     * @throws java.lang.Exception 
     */
    @Test
    @SuppressWarnings("unchecked")
    public void deleteFromDB() throws Exception {
        try {
            System.out.println("deleteFromDB");
            HashMap parameters = new HashMap();
            parameters.put("designation", "test");
            int id = ((XincoCoreLanguage) new XincoCoreLanguageServer().findWithDetails(parameters)[0]).getId();
            System.out.println("Deleting: " + id);
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(id);
            instance.deleteFromDB();
            parameters.clear();
            parameters.put("id", id);
            assertTrue(instance.findById(parameters) == null);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }
}
