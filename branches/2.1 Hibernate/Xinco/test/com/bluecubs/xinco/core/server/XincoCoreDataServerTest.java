package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.hibernate.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.persistence.XincoCoreData;
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
public class XincoCoreDataServerTest extends TestCase {

    HashMap parameters = new HashMap();
    private int tempId;

    public XincoCoreDataServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreDataServerTest.class);
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
     * Test of findXincoCoreData method, of class XincoCoreDataServer.
     */
    public void testFindXincoCoreData() {
        try {
            System.out.println("findXincoCoreData");
            String in0 = "Apache License 2.0";
            //search on database all
            java.util.Vector tv = XincoCoreDataServer.findXincoCoreData(in0, 0, true);
            assertTrue(tv.size() > 0);
            //search on database only designation
            tv = XincoCoreDataServer.findXincoCoreData(in0, 0, false);
            assertTrue(tv.size() > 0);
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findById method, of class XincoCoreDataServer.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testFindById() throws Exception {
        try {
            System.out.println("findById");
            parameters = new HashMap();
            parameters.put("id", 1);
            XincoCoreDataServer instance = new XincoCoreDataServer(1);
            XincoAbstractAuditableObject result = (XincoAbstractAuditableObject) instance.findById(parameters);
            assertEquals(1, (int) ((XincoCoreData) result).getId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreDataServer.
     * @throws Exception 
     */
    public void testFindWithDetails() throws Exception {
        try {
            System.out.println("findWithDetails");
            XincoCoreDataServer instance = new XincoCoreDataServer(1);
            XincoCoreData[] expResult = {instance};
            XincoAbstractAuditableObject[] result = (XincoAbstractAuditableObject[]) instance.findWithDetails(instance.getParameters());
            assertEquals(expResult[0].getId(), ((XincoCoreData) result[0]).getId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getParameters method, of class XincoCoreDataServer.
     */
    @SuppressWarnings("unchecked")
    public void testGetParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreDataServer instance = new XincoCoreDataServer(1);
            HashMap expResult = new HashMap();
            expResult.put("id", 1);
            HashMap result = instance.getParameters();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getNewID method, of class XincoCoreDataServer.
     */
    public void testGetNewID() {
        try {
            System.out.println("getNewID");
            XincoCoreDataServer instance = new XincoCoreDataServer(1);
            int result = instance.getNewID(true);
            System.out.println("New id: " + result);
            assertTrue(result > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreDataServer.
     */
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            XincoCoreDataServer instance = new XincoCoreDataServer(0, 1, 1, 2, "test", 1);
            System.out.println("Instance id before writing: " + instance.getId());
            assertTrue(instance.write2DB());
            System.out.println("Instance id after writing: " + instance.getId());
            assertTrue(instance.getId() > 0);
            tempId = instance.getId();
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreDataServer.
     */
    @SuppressWarnings("unchecked")
    public void testDeleteFromDB() {
        try {
            System.out.println("deleteFromDB");
            XincoCoreDataServer instance = new XincoCoreDataServer(1),
                    acl = new XincoCoreDataServer(0, 1, 1, 2, "test", 1);
            acl.write2DB();
            tempId = acl.getId();
            parameters = new HashMap();
            parameters.put("id", tempId);
            XincoCoreData[] result = (XincoCoreData[]) instance.findWithDetails(parameters);
            XincoCoreDataServer value = new XincoCoreDataServer((result[0]).getId());
            //Blame the admin :P
            value.setChangerID(1);
            assertTrue(value.deleteFromDB());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreLogs method, of class XincoCoreDataServer.
     */
    public void testGetXincoCoreLogs() {
        try {
            System.out.println("getXincoCoreLogs");
            XincoCoreDataServer instance = new XincoCoreDataServer(1);
            Vector result = instance.getXincoCoreLogs();
            assertTrue(result.size() > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoAddAttributes method, of class XincoCoreDataServer.
     */
    public void testGetXincoAddAttributes() {
        try {
            System.out.println("getXincoAddAttributes");
            XincoCoreDataServer instance = new XincoCoreDataServer(1);
            Vector result = instance.getXincoAddAttributes();
            assertTrue(result.size() > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreACL method, of class XincoCoreDataServer.
     */
    public void testGetXincoCoreACL() {
        try {
            System.out.println("getXincoCoreACL");
            XincoCoreDataServer instance = new XincoCoreDataServer(1);
            Vector result = instance.getXincoCoreACL();
            assertTrue(result.size() > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of removeFromDB method, of class XincoCoreDataServer.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testRemoveFromDB() throws Exception {
        try {
            System.out.println("removeFromDB");
            int userID = 1;
            XincoCoreDataServer.removeFromDB(userID, tempId);
            parameters.clear();
            parameters.put("id", tempId);
            assertTrue(XincoCoreDataServer.pm.namedQuery("XincoCoreData.findById", parameters).size() == 0);
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
