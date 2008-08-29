package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.hibernate.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.persistence.XincoCoreDataType;
import com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttribute;
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
public class XincoCoreDataTypeServerTest extends TestCase {

    HashMap parameters = new HashMap();
    private int tempId = 0;

    public XincoCoreDataTypeServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreDataTypeServerTest.class);
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
     * Test of getXincoCoreDataTypeTypes method, of class XincoCoreDataType.
     */
    public void testGetXincoCoreDataTypeTypes() {
        System.out.println("getXincoCoreDataTypeTypes");
        try {
            Vector result = XincoCoreDataTypeServer.getXincoCoreDataTypes();
            assertTrue(result.size() > 0);
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreDataTypeTypeAttributeServerTypeAttributes method, of class XincoCoreDataType.
     */
    public void testGetXincoCoreDataTypeTypeAttributeServerTypeAttributes() {
        try {
            System.out.println("getXincoCoreDataTypeTypeAttributeServerTypeAttributes");
            int attrID = 1;
            Vector result = XincoCoreDataTypeServer.getXincoCoreDataTypeAttributeServerTypeAttributes(attrID);
            assertEquals(((XincoCoreDataTypeAttribute) result.get(0)).getDesignation(), "File_Name");
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findById method, of class XincoCoreDataType.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testFindById() throws Exception {
        try {
            System.out.println("findById");
            parameters = new HashMap();
            parameters.put("id", 1);
            XincoCoreDataTypeServer instance = new XincoCoreDataTypeServer(1);
            XincoAbstractAuditableObject result = (XincoAbstractAuditableObject) instance.findById(parameters);
            assertEquals(1, (int) ((XincoCoreDataType) result).getId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreDataType.
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public void testFindWithDetails() throws Exception {
        try {
            System.out.println("findWithDetails");
            XincoCoreDataTypeServer instance = new XincoCoreDataTypeServer(1);
            XincoCoreDataType[] expResult = {instance};
            parameters.clear();
            parameters.put("designation", "general.data.type.file");
            XincoAbstractAuditableObject[] result = (XincoAbstractAuditableObject[]) instance.findWithDetails(parameters);
            assertEquals(expResult[0].getId(), ((XincoCoreDataType) result[0]).getId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getParameters method, of class XincoCoreDataType.
     */
    @SuppressWarnings("unchecked")
    public void testGetParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreDataTypeServer instance = new XincoCoreDataTypeServer(1);
            HashMap expResult = new HashMap();
            expResult.put("id", 1);
            HashMap result = instance.getParameters();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getNewID method, of class XincoCoreDataType.
     */
    public void testGetNewID() {
        try {
            System.out.println("getNewID");
            XincoCoreDataTypeServer instance = new XincoCoreDataTypeServer(1);
            int result = instance.getNewID(true);
            System.out.println("New id: " + result);
            assertTrue(result > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreDataType.
     */
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            XincoCoreDataTypeServer instance = new XincoCoreDataTypeServer(0, "test", "test", null);
            System.out.println("Instance id before writing: " + instance.getId());
            assertTrue(instance.write2DB());
            System.out.println("Instance id after writing: " + instance.getId());
            assertTrue(instance.getId() > 0);
            tempId = instance.getId();
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreDataType.
     */
    public void testDeleteFromDB_0args() {
        try {
            System.out.println("deleteFromDB");
            XincoCoreDataTypeServer instance = new XincoCoreDataTypeServer(0, "test", "test", null);
            instance.write2DB();
            boolean expResult = true;
            //Blame the admin
            instance.setChangerID(1);
            boolean result = instance.deleteFromDB();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreDataTypeTypeAttributes method, of class XincoCoreDataType.
     */
    public void testGetXincoCoreDataTypeTypeAttributes() {
        try {
            System.out.println("getXincoCoreDataTypeTypeAttributes");
            XincoCoreDataTypeServer instance = new XincoCoreDataTypeServer(1);
            Vector result = instance.getXincoCoreDataTypeAttributes();
            assertTrue(result.size() > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of setXincoCoreDataTypeTypeAttributes method, of class XincoCoreDataType.
     */
    public void testSetXincoCoreDataTypeTypeAttributes() {
        try {
            System.out.println("setXincoCoreDataTypeTypeAttributes");
            Vector XincoCoreDataTypeTypeAttributes = new Vector();
            XincoCoreDataTypeServer instance = new XincoCoreDataTypeServer(1);
            instance.setXincoCoreDataTypeAttributes(XincoCoreDataTypeTypeAttributes);
            assertEquals(instance.getXincoCoreDataTypeAttributes(), XincoCoreDataTypeTypeAttributes);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreDataType.
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public void testDeleteFromDB_XincoCoreDataTypeTypeAttributeServer_int() throws Exception {
        try {
            System.out.println("deleteFromDB");
            XincoCoreDataTypeAttributeServer xdts = new XincoCoreDataTypeAttributeServer(1, 100, "test", "test", 1);
            xdts.write2DB();
            assertTrue(XincoCoreDataTypeServer.deleteFromDB(xdts, 1));
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
