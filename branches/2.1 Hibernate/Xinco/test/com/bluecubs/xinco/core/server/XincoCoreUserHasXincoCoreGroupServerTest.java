/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server;

import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import java.util.HashMap;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreUserHasXincoCoreGroupServerTest extends TestCase {
    
    public XincoCoreUserHasXincoCoreGroupServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreUserHasXincoCoreGroupServerTest.class);
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
     * Test of findById method, of class XincoCoreUserHasXincoCoreGroupServer.
     */
    public void testFindById() throws Exception {
        System.out.println("findById");
        HashMap parameters = null;
        XincoCoreUserHasXincoCoreGroupServer instance = null;
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.findById(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findWithDetails method, of class XincoCoreUserHasXincoCoreGroupServer.
     */
    public void testFindWithDetails() throws Exception {
        System.out.println("findWithDetails");
        HashMap parameters = null;
        XincoCoreUserHasXincoCoreGroupServer instance = null;
        AbstractAuditableObject[] expResult = null;
        AbstractAuditableObject[] result = instance.findWithDetails(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class XincoCoreUserHasXincoCoreGroupServer.
     */
    public void testCreate() {
        System.out.println("create");
        AbstractAuditableObject value = null;
        XincoCoreUserHasXincoCoreGroupServer instance = null;
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.create(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class XincoCoreUserHasXincoCoreGroupServer.
     */
    public void testUpdate() {
        System.out.println("update");
        AbstractAuditableObject value = null;
        XincoCoreUserHasXincoCoreGroupServer instance = null;
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.update(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class XincoCoreUserHasXincoCoreGroupServer.
     */
    public void testDelete() {
        System.out.println("delete");
        AbstractAuditableObject value = null;
        XincoCoreUserHasXincoCoreGroupServer instance = null;
        instance.delete(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class XincoCoreUserHasXincoCoreGroupServer.
     */
    public void testGetParameters() {
        System.out.println("getParameters");
        XincoCoreUserHasXincoCoreGroupServer instance = null;
        HashMap expResult = null;
        HashMap result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNewID method, of class XincoCoreUserHasXincoCoreGroupServer.
     */
    public void testGetNewID() {
        System.out.println("getNewID");
        boolean a = false;
        XincoCoreUserHasXincoCoreGroupServer instance = null;
        int expResult = 0;
        int result = instance.getNewID(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write2DB method, of class XincoCoreUserHasXincoCoreGroupServer.
     */
    public void testWrite2DB() {
        System.out.println("write2DB");
        XincoCoreUserHasXincoCoreGroupServer instance = null;
        boolean expResult = false;
        boolean result = instance.write2DB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreUserHasXincoCoreGroupServer.
     */
    public void testDeleteFromDB() {
        System.out.println("deleteFromDB");
        XincoCoreUserHasXincoCoreGroupServer instance = null;
        boolean expResult = false;
        boolean result = instance.deleteFromDB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
