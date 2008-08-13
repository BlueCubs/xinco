/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server;

import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import java.util.HashMap;
import java.util.Vector;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreDataTypeServerTest extends TestCase {
    
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
     * Test of getXincoCoreDataTypes method, of class XincoCoreDataTypeServer.
     */
    public void testGetXincoCoreDataTypes() {
        System.out.println("getXincoCoreDataTypes");
        Vector expResult = null;
        Vector result = XincoCoreDataTypeServer.getXincoCoreDataTypes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreDataTypeServer.
     */
    public void testDeleteFromDB_XincoCoreDataTypeAttributeServer_int() throws Exception {
        System.out.println("deleteFromDB");
        XincoCoreDataTypeAttributeServer attrCDTA = null;
        int userID = 0;
        int expResult = 0;
        int result = XincoCoreDataTypeServer.deleteFromDB(attrCDTA, userID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXincoCoreDataTypeAttributeServerTypeAttributes method, of class XincoCoreDataTypeServer.
     */
    public void testGetXincoCoreDataTypeAttributeServerTypeAttributes() {
        System.out.println("getXincoCoreDataTypeAttributeServerTypeAttributes");
        int attrID = 0;
        Vector expResult = null;
        Vector result = XincoCoreDataTypeServer.getXincoCoreDataTypeAttributeServerTypeAttributes(attrID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findById method, of class XincoCoreDataTypeServer.
     */
    public void testFindById() throws Exception {
        System.out.println("findById");
        HashMap parameters = null;
        XincoCoreDataTypeServer instance = null;
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.findById(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findWithDetails method, of class XincoCoreDataTypeServer.
     */
    public void testFindWithDetails() throws Exception {
        System.out.println("findWithDetails");
        HashMap parameters = null;
        XincoCoreDataTypeServer instance = null;
        AbstractAuditableObject[] expResult = null;
        AbstractAuditableObject[] result = instance.findWithDetails(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class XincoCoreDataTypeServer.
     */
    public void testCreate() {
        System.out.println("create");
        AbstractAuditableObject value = null;
        XincoCoreDataTypeServer instance = null;
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.create(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class XincoCoreDataTypeServer.
     */
    public void testUpdate() {
        System.out.println("update");
        AbstractAuditableObject value = null;
        XincoCoreDataTypeServer instance = null;
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.update(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class XincoCoreDataTypeServer.
     */
    public void testDelete() {
        System.out.println("delete");
        AbstractAuditableObject value = null;
        XincoCoreDataTypeServer instance = null;
        instance.delete(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class XincoCoreDataTypeServer.
     */
    public void testGetParameters() {
        System.out.println("getParameters");
        XincoCoreDataTypeServer instance = null;
        HashMap expResult = null;
        HashMap result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNewID method, of class XincoCoreDataTypeServer.
     */
    public void testGetNewID() {
        System.out.println("getNewID");
        boolean a = false;
        XincoCoreDataTypeServer instance = null;
        int expResult = 0;
        int result = instance.getNewID(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write2DB method, of class XincoCoreDataTypeServer.
     */
    public void testWrite2DB() {
        System.out.println("write2DB");
        XincoCoreDataTypeServer instance = null;
        boolean expResult = false;
        boolean result = instance.write2DB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreDataTypeServer.
     */
    public void testDeleteFromDB_0args() {
        System.out.println("deleteFromDB");
        XincoCoreDataTypeServer instance = null;
        boolean expResult = false;
        boolean result = instance.deleteFromDB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXincoCoreDataTypeAttributes method, of class XincoCoreDataTypeServer.
     */
    public void testGetXincoCoreDataTypeAttributes() {
        System.out.println("getXincoCoreDataTypeAttributes");
        XincoCoreDataTypeServer instance = null;
        Vector expResult = null;
        Vector result = instance.getXincoCoreDataTypeAttributes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setXincoCoreDataTypeAttributes method, of class XincoCoreDataTypeServer.
     */
    public void testSetXincoCoreDataTypeAttributes() {
        System.out.println("setXincoCoreDataTypeAttributes");
        Vector xincoCoreDataTypeAttributes = null;
        XincoCoreDataTypeServer instance = null;
        instance.setXincoCoreDataTypeAttributes(xincoCoreDataTypeAttributes);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
