package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.hibernate.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttributePK;
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
public class XincoCoreDataTypeAttributeServerTest extends TestCase {

    HashMap parameters = new HashMap();
    private XincoCoreDataTypeAttributePK tempId;

    public XincoCoreDataTypeAttributeServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreDataTypeAttributeServerTest.class);
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
     * Test of getXincoCoreDataTypeAttributes method, of class XincoCoreDataTypeAttributeServer.
     */
    public void testGetXincoCoreDataTypeAttributes() {
        try {
            System.out.println("getXincoCoreDataTypeAttributes");
            Vector result = XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(1);
            assertTrue(result.size() > 0);
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findById method, of class XincoCoreDataTypeAttributeServer.
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public void testFindById() throws Exception {
        try {
            System.out.println("findById");
            parameters = new HashMap();
            parameters.put("xincoCoreDataTypeId", 1);
            parameters.put("attributeId", 1);
            XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer(1, 1);
            XincoAbstractAuditableObject result = (XincoAbstractAuditableObject) instance.findById(parameters);
            assertEquals(1, ((XincoCoreDataTypeAttribute) result).getXincoCoreDataTypeAttributePK().getAttributeId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreDataTypeAttributeServer.
     * @throws Exception 
     */
    public void testFindWithDetails() throws Exception {
        try {
            System.out.println("findWithDetails");
            XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer(1, 1);
            XincoCoreDataTypeAttribute[] expResult = {instance};
            XincoAbstractAuditableObject[] result = (XincoAbstractAuditableObject[]) instance.findWithDetails(instance.getParameters());
            assertEquals(expResult[0].getXincoCoreDataTypeAttributePK().getAttributeId(), ((XincoCoreDataTypeAttribute) result[0]).getXincoCoreDataTypeAttributePK().getAttributeId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getParameters method, of class XincoCoreDataTypeAttributeServer.
     */
    @SuppressWarnings("unchecked")
    public void testGetParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer(1, 1);
            HashMap expResult = new HashMap();
            expResult.put("attributeId", 1);
            expResult.put("xincoCoreDataTypeId", 1);
            HashMap result = instance.getParameters();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getNewID method, of class XincoCoreDataTypeAttributeServer.
     */
    public void testGetNewID() {
        System.out.println("getNewID");
        try {
            XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer();
            assertTrue(instance.getNewID(true) == 0);
        } catch (UnsupportedOperationException e) {
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreDataTypeAttributeServer.
     */
    @SuppressWarnings({"unchecked", "static-access"})
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer(1, 100, "test", "", 0);
            assertTrue(instance.write2DB());
            parameters.clear();
            parameters.put("designation", instance.getDesignation());
            assertTrue(instance.pm.namedQuery("XincoCoreDataTypeAttribute.findByDesignation", parameters).size() > 0);
            //Blame the admin :P
            assertTrue(XincoCoreDataTypeAttributeServer.deleteFromDB((XincoCoreDataTypeAttribute) instance, 1));
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreDataTypeAttributeServer.
     */
    @SuppressWarnings("unchecked")
    public void testDeleteFromDB_0args() {
        try {
            System.out.println("deleteFromDB");
            XincoCoreDataTypeAttributeServer acl = new XincoCoreDataTypeAttributeServer(1, 100, "test", "", 0);
            acl.write2DB();
            tempId = acl.getXincoCoreDataTypeAttributePK();
            XincoCoreDataTypeAttributeServer value = new XincoCoreDataTypeAttributeServer(1, 100);
            //Blame the admin :P
            value.setChangerID(1);
            assertTrue(value.deleteFromDB());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
