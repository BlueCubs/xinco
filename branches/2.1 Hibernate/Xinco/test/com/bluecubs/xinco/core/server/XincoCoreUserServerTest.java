/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server;

import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.PersistenceManager;
import java.util.HashMap;
import java.util.Vector;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreUserServerTest extends TestCase {
    
    public XincoCoreUserServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreUserServerTest.class);
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
     * Test of getAttempts method, of class XincoCoreUserServer.
     */
    public void testGetAttempts() {
        System.out.println("getAttempts");
        XincoCoreUserServer instance = null;
        int expResult = 0;
        int result = instance.getAttempts();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAttempts method, of class XincoCoreUserServer.
     */
    public void testSetAttempts() {
        System.out.println("setAttempts");
        int attempts = 0;
        XincoCoreUserServer instance = null;
        instance.setAttempts(attempts);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write2DB method, of class XincoCoreUserServer.
     * @throws Exception 
     */
    public void testWrite2DB_PersistenceManager() throws Exception {
        System.out.println("write2DB");
        PersistenceManager DBM = null;
        XincoCoreUserServer instance = null;
        boolean expResult = false;
        boolean result = instance.write2DB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXincoCoreUsers method, of class XincoCoreUserServer.
     */
    public void testGetXincoCoreUsers() {
        System.out.println("getXincoCoreUsers");
        Vector expResult = null;
        Vector result = XincoCoreUserServer.getXincoCoreUsers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isPasswordUsable method, of class XincoCoreUserServer.
     */
    public void testIsPasswordUsable() {
        System.out.println("isPasswordUsable");
        String newPass = "";
        XincoCoreUserServer instance = null;
        boolean expResult = false;
        boolean result = instance.isPasswordUsable(newPass);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXincoCoreGroups method, of class XincoCoreUserServer.
     */
    public void testGetXincoCoreGroups() {
        System.out.println("getXincoCoreGroups");
        XincoCoreUserServer instance = null;
        Vector expResult = null;
        Vector result = instance.getXincoCoreGroups();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setXincoCoreGroups method, of class XincoCoreUserServer.
     */
    public void testSetXincoCoreGroups() {
        System.out.println("setXincoCoreGroups");
        Vector xinco_core_groups = null;
        XincoCoreUserServer instance = null;
        instance.setXincoCoreGroups(xinco_core_groups);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findById method, of class XincoCoreUserServer.
     * @throws Exception
     */
    public void testFindById() throws Exception {
        System.out.println("findById");
        HashMap parameters = null;
        XincoCoreUserServer instance = null;
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.findById(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findWithDetails method, of class XincoCoreUserServer.
     * @throws Exception 
     */
    public void testFindWithDetails() throws Exception {
        System.out.println("findWithDetails");
        HashMap parameters = null;
        XincoCoreUserServer instance = null;
        AbstractAuditableObject[] expResult = null;
        AbstractAuditableObject[] result = instance.findWithDetails(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class XincoCoreUserServer.
     */
    public void testCreate() {
        System.out.println("create");
        AbstractAuditableObject value = null;
        XincoCoreUserServer instance = null;
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.create(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class XincoCoreUserServer.
     */
    public void testUpdate() {
        System.out.println("update");
        AbstractAuditableObject value = null;
        XincoCoreUserServer instance = null;
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.update(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class XincoCoreUserServer.
     */
    public void testDelete() {
        System.out.println("delete");
        AbstractAuditableObject value = null;
        XincoCoreUserServer instance = null;
        instance.delete(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class XincoCoreUserServer.
     */
    public void testGetParameters() {
        System.out.println("getParameters");
        XincoCoreUserServer instance = null;
        HashMap expResult = null;
        HashMap result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNewID method, of class XincoCoreUserServer.
     */
    public void testGetNewID() {
        System.out.println("getNewID");
        boolean a = false;
        XincoCoreUserServer instance = null;
        int expResult = 0;
        int result = instance.getNewID(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write2DB method, of class XincoCoreUserServer.
     */
    public void testWrite2DB_0args() {
        System.out.println("write2DB");
        XincoCoreUserServer instance = null;
        boolean expResult = false;
        boolean result = instance.write2DB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreUserServer.
     */
    public void testDeleteFromDB() {
        System.out.println("deleteFromDB");
        XincoCoreUserServer instance = null;
        boolean expResult = false;
        boolean result = instance.deleteFromDB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isChange method, of class XincoCoreUserServer.
     */
    public void testIsChange() {
        System.out.println("isChange");
        XincoCoreUserServer instance = null;
        boolean expResult = false;
        boolean result = instance.isChange();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setChange method, of class XincoCoreUserServer.
     */
    public void testSetChange() {
        System.out.println("setChange");
        boolean change = false;
        XincoCoreUserServer instance = null;
        instance.setChange(change);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isWriteGroups method, of class XincoCoreUserServer.
     */
    public void testIsWriteGroups() {
        System.out.println("isWriteGroups");
        XincoCoreUserServer instance = null;
        boolean expResult = false;
        boolean result = instance.isWriteGroups();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setWriteGroups method, of class XincoCoreUserServer.
     */
    public void testSetWriteGroups() {
        System.out.println("setWriteGroups");
        boolean writeGroups = false;
        XincoCoreUserServer instance = null;
        instance.setWriteGroups(writeGroups);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
