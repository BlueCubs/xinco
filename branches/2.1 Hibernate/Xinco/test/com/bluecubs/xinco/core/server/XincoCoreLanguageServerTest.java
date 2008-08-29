package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.hibernate.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.persistence.XincoCoreLanguage;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreLanguageServerTest extends TestCase {

    private int tempId = -1;
    HashMap parameters = new HashMap();

    public XincoCoreLanguageServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreLanguageServerTest.class);
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
     * Test of getXincoCoreLanguages method, of class XincoCoreLanguageServer.
     */
    public void testGetXincoCoreLanguages() {
        try {
            System.out.println("getXincoCoreLanguages");
            Vector result = XincoCoreLanguageServer.getXincoCoreLanguages();
            assertTrue(result.size() > 0);
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of isLanguageUsed method, of class XincoCoreLanguageServer.
     */
    public void testIsLanguageUsed() {
        try {
            System.out.println("isLanguageUsed");
            XincoCoreLanguageServer xcl = new XincoCoreLanguageServer(2);
            assertTrue(XincoCoreLanguageServer.isLanguageUsed(xcl));
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findById method, of class XincoCoreLanguageServer.
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public void testFindById() throws Exception {
        try {
            System.out.println("findById");
            parameters = new HashMap();
            parameters.put("id", 1);
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(1);
            XincoAbstractAuditableObject result = (XincoAbstractAuditableObject) instance.findById(parameters);
            assertEquals(1, (int) ((XincoCoreLanguage) result).getId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreLanguageServer.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testFindWithDetails() throws Exception {
        try {
            System.out.println("findWithDetails");
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(2);
            XincoCoreLanguage[] expResult = {instance};
            parameters.clear();
            parameters.put("sign", "en");
            XincoAbstractAuditableObject[] result = (XincoAbstractAuditableObject[]) instance.findWithDetails(parameters);
            assertEquals(expResult[0].getId(), ((XincoCoreLanguage) result[0]).getId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getParameters method, of class XincoCoreLanguageServer.
     */
    @SuppressWarnings("unchecked")
    public void testGetParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(1);
            HashMap expResult = new HashMap();
            expResult.put("id", 1);
            HashMap result = instance.getParameters();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getNewID method, of class XincoCoreLanguageServer.
     */
    public void testGetNewID() {
        try {
            System.out.println("getNewID");
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(1);
            int result = instance.getNewID(true);
            System.out.println("New id: " + result);
            assertTrue(result > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreLanguageServer.
     */
    public void testWrite2DBAndDelete() {
        try {
            System.out.println("write2DB");
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(0, "test", "test");
            System.out.println("Instance id before writing: " + instance.getId());
            assertTrue(instance.write2DB());
            System.out.println("Instance id after writing: " + instance.getId());
            assertTrue(instance.getId() > 0);
            instance.setChangerID(1);
            assertTrue(instance.deleteFromDB());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
