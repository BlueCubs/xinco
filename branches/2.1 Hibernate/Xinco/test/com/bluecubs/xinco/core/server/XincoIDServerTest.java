package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.hibernate.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.persistence.XincoID;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoIDServerTest extends TestCase {

    HashMap parameters = new HashMap();

    public XincoIDServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoIDServerTest.class);
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
     * Test of findById method, of class XincoIDServer.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testFindById() throws Exception {
        try {
            System.out.println("findById");
            parameters = new HashMap();
            parameters.put("id", 1);
            XincoIDServer instance = new XincoIDServer("xinco_core_ace");
            XincoAbstractAuditableObject result = (XincoAbstractAuditableObject) instance.findById(parameters);
            assertEquals(1, (int) ((XincoID) result).getId());
        } catch (Exception ex) {
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findWithDetails method, of class XincoIDServer.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testFindWithDetails() throws Exception {
        try {
            System.out.println("findWithDetails");
            XincoIDServer instance = new XincoIDServer("xinco_core_ace");
            parameters.clear();
            parameters.put("tablename", "xinco_core_ace");
            XincoAbstractAuditableObject[] result = (XincoAbstractAuditableObject[]) instance.findWithDetails(parameters);
            assertEquals(instance.getTablename(), ((XincoID) result[0]).getTablename());
        } catch (Exception ex) {
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getParameters method, of class XincoIDServer.
     */
    @SuppressWarnings("unchecked")
    public void testGetParameters() {
        try {
            System.out.println("getParameters");
            XincoIDServer instance = new XincoIDServer("xinco_core_ace");
            HashMap expResult = new HashMap();
            expResult.put("id", 1);
            HashMap result = instance.getParameters();
            assertEquals(expResult, result);
        } catch (Exception ex) {
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getNewTableID method, of class XincoIDServer.
     */
    public void testGetNewTableID() {
        try {
            System.out.println("getNewTableID");
            XincoIDServer instance = new XincoIDServer("xinco_core_ace");
            assertTrue(instance.getNewTableID(true) > 0);
        } catch (Exception ex) {
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of write2DB method, of class XincoIDServer.
     */
    public void testWrite2DBAndDelete() {
        try {
            System.out.println("write2DB");
            XincoIDServer instance = new XincoIDServer("test", 0,1000);
            System.out.println("Instance id before writing: " + instance.getId());
            assertTrue(instance.write2DB());
            System.out.println("Instance id after writing: " + instance.getId());
            assertTrue(instance.getId() > 0);
            instance.setChangerID(1);
            assertTrue(instance.deleteFromDB());
        } catch (Exception ex) {
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoIDServer.
     */
    public void testDeleteFromDB() {
        try {
            System.out.println("deleteFromDB");
            XincoIDServer instance = new XincoIDServer("test", 0,1000);
            instance.write2DB();
            instance.setChangerID(2);
            assertTrue(!instance.deleteFromDB());
        } catch (Exception ex) {
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getNewID method, of class XincoIDServer.
     */
    public void testGetNewID() {
        try {
            System.out.println("getNewID");
            XincoIDServer instance = new XincoIDServer("xinco_core_ace");
            assertTrue(instance.getNewTableID(true) > 0);
        } catch (Exception ex) {
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }

    }
}
