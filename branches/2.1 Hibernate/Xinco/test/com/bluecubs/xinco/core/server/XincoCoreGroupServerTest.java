package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.hibernate.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.persistence.XincoCoreGroup;
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
public class XincoCoreGroupServerTest extends TestCase {

    private int tempId = -1;
    HashMap parameters = new HashMap();

    public XincoCoreGroupServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreGroupServerTest.class);
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
     * Test of getXincoCoreGroups method, of class XincoCoreGroupServer.
     */
    public void testGetXincoCoreGroups() {
        try {
            System.out.println("getXincoCoreGroups");
            Vector result = XincoCoreGroupServer.getXincoCoreGroups();
            assertTrue(result.size() > 0);
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findById method, of class XincoCoreGroupServer.
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public void testFindById() throws Exception {
        try {
            System.out.println("findById");
            parameters = new HashMap();
            parameters.put("id", 1);
            XincoCoreGroupServer instance = new XincoCoreGroupServer(1);
            XincoAbstractAuditableObject result = (XincoAbstractAuditableObject) instance.findById(parameters);
            assertEquals(1, (int) ((XincoCoreGroup) result).getId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreGroupServer.
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public void testFindWithDetails() throws Exception {
        try {
            System.out.println("findWithDetails");
            XincoCoreGroupServer instance = new XincoCoreGroupServer(1);
            XincoCoreGroup[] expResult = {instance};
            parameters.clear();
            parameters.put("designation", "general.group.admin");
            XincoAbstractAuditableObject[] result = (XincoAbstractAuditableObject[]) instance.findWithDetails(parameters);
            assertEquals(expResult[0].getId(), ((XincoCoreGroup) result[0]).getId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getParameters method, of class XincoCoreGroupServer.
     */
    @SuppressWarnings("unchecked")
    public void testGetParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreGroupServer instance = new XincoCoreGroupServer(1);
            HashMap expResult = new HashMap();
            expResult.put("id", 1);
            HashMap result = instance.getParameters();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getNewID method, of class XincoCoreGroupServer.
     */
    public void testGetNewID() {
        try {
            System.out.println("getNewID");
            XincoCoreGroupServer instance = new XincoCoreGroupServer(1);
            int result = instance.getNewID(true);
            System.out.println("New id: " + result);
            assertTrue(result > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreGroupServer.
     */
    public void testWrite2DBAndDelete() {
        try {
            System.out.println("write2DB");
            XincoCoreGroupServer instance = new XincoCoreGroupServer(0, "test", 1);
            System.out.println("Instance id before writing: " + instance.getId());
            assertTrue(instance.write2DB());
            System.out.println("Instance id after writing: " + instance.getId());
            assertTrue(instance.getId() > 0);
            instance.setChangerID(1);
            assertTrue(instance.deleteFromDB());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
