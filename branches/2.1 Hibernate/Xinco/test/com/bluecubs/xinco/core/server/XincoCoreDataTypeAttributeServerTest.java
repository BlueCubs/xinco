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
public class XincoCoreDataTypeAttributeServerTest extends TestCase {
    
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
     * Test of deleteFromDB method, of class XincoCoreDataTypeAttributeServer.
     */
    public void testDeleteFromDB_XincoCoreDataTypeAttributeServer_int() throws Exception {
        System.out.println("deleteFromDB");
        XincoCoreDataTypeAttributeServer attrCDTA = null;
        int userID = 0;
        int expResult = 0;
        int result = XincoCoreDataTypeAttributeServer.deleteFromDB(attrCDTA, userID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXincoCoreDataTypeAttributes method, of class XincoCoreDataTypeAttributeServer.
     */
    public void testGetXincoCoreDataTypeAttributes() {
        System.out.println("getXincoCoreDataTypeAttributes");
        int attrID = 0;
        Vector expResult = null;
        Vector result = XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(attrID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findById method, of class XincoCoreDataTypeAttributeServer.
     */
    public void testFindById() throws Exception {
        System.out.println("findById");
        HashMap parameters = null;
        XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer();
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.findById(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findWithDetails method, of class XincoCoreDataTypeAttributeServer.
     */
    public void testFindWithDetails() throws Exception {
        System.out.println("findWithDetails");
        HashMap parameters = null;
        XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer();
        AbstractAuditableObject[] expResult = null;
        AbstractAuditableObject[] result = instance.findWithDetails(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class XincoCoreDataTypeAttributeServer.
     */
    public void testCreate() {
        System.out.println("create");
        AbstractAuditableObject value = null;
        XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer();
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.create(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class XincoCoreDataTypeAttributeServer.
     */
    public void testUpdate() {
        System.out.println("update");
        AbstractAuditableObject value = null;
        XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer();
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.update(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class XincoCoreDataTypeAttributeServer.
     */
    public void testDelete() {
        System.out.println("delete");
        AbstractAuditableObject value = null;
        XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer();
        instance.delete(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class XincoCoreDataTypeAttributeServer.
     */
    public void testGetParameters() {
        System.out.println("getParameters");
        XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer();
        HashMap expResult = null;
        HashMap result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNewID method, of class XincoCoreDataTypeAttributeServer.
     */
    public void testGetNewID() {
        System.out.println("getNewID");
        boolean a = false;
        XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer();
        int expResult = 0;
        int result = instance.getNewID(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write2DB method, of class XincoCoreDataTypeAttributeServer.
     */
    public void testWrite2DB() {
        System.out.println("write2DB");
        XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer();
        boolean expResult = false;
        boolean result = instance.write2DB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreDataTypeAttributeServer.
     */
    public void testDeleteFromDB_0args() {
        System.out.println("deleteFromDB");
        XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer();
        boolean expResult = false;
        boolean result = instance.deleteFromDB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
