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
public class XincoCoreGroupServerTest extends TestCase {
    
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
        System.out.println("getXincoCoreGroups");
        Vector expResult = null;
        Vector result = XincoCoreGroupServer.getXincoCoreGroups();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findById method, of class XincoCoreGroupServer.
     */
    public void testFindById() throws Exception {
        System.out.println("findById");
        HashMap parameters = null;
        XincoCoreGroupServer instance = new XincoCoreGroupServer();
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.findById(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findWithDetails method, of class XincoCoreGroupServer.
     */
    public void testFindWithDetails() throws Exception {
        System.out.println("findWithDetails");
        HashMap parameters = null;
        XincoCoreGroupServer instance = new XincoCoreGroupServer();
        AbstractAuditableObject[] expResult = null;
        AbstractAuditableObject[] result = instance.findWithDetails(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class XincoCoreGroupServer.
     */
    public void testCreate() {
        System.out.println("create");
        AbstractAuditableObject value = null;
        XincoCoreGroupServer instance = new XincoCoreGroupServer();
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.create(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class XincoCoreGroupServer.
     */
    public void testUpdate() {
        System.out.println("update");
        AbstractAuditableObject value = null;
        XincoCoreGroupServer instance = new XincoCoreGroupServer();
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.update(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class XincoCoreGroupServer.
     */
    public void testDelete() {
        System.out.println("delete");
        AbstractAuditableObject value = null;
        XincoCoreGroupServer instance = new XincoCoreGroupServer();
        instance.delete(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class XincoCoreGroupServer.
     */
    public void testGetParameters() {
        System.out.println("getParameters");
        XincoCoreGroupServer instance = new XincoCoreGroupServer();
        HashMap expResult = null;
        HashMap result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNewID method, of class XincoCoreGroupServer.
     */
    public void testGetNewID() {
        System.out.println("getNewID");
        boolean a = false;
        XincoCoreGroupServer instance = new XincoCoreGroupServer();
        int expResult = 0;
        int result = instance.getNewID(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write2DB method, of class XincoCoreGroupServer.
     */
    public void testWrite2DB() {
        System.out.println("write2DB");
        XincoCoreGroupServer instance = new XincoCoreGroupServer();
        boolean expResult = false;
        boolean result = instance.write2DB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreGroupServer.
     */
    public void testDeleteFromDB() {
        System.out.println("deleteFromDB");
        XincoCoreGroupServer instance = new XincoCoreGroupServer();
        boolean expResult = false;
        boolean result = instance.deleteFromDB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
