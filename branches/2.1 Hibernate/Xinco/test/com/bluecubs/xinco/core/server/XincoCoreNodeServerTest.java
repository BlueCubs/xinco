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
public class XincoCoreNodeServerTest extends TestCase {
    
    public XincoCoreNodeServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreNodeServerTest.class);
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
     * Test of deleteFromDB method, of class XincoCoreNodeServer.
     */
    public void testDeleteFromDB_boolean_int() throws Exception {
        System.out.println("deleteFromDB");
        boolean delete_this = false;
        int userID = 0;
        XincoCoreNodeServer instance = null;
        instance.deleteFromDB(delete_this, userID);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fillXincoCoreNodes method, of class XincoCoreNodeServer.
     */
    public void testFillXincoCoreNodes() {
        System.out.println("fillXincoCoreNodes");
        XincoCoreNodeServer instance = null;
        instance.fillXincoCoreNodes();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fillXincoCoreData method, of class XincoCoreNodeServer.
     */
    public void testFillXincoCoreData() {
        System.out.println("fillXincoCoreData");
        XincoCoreNodeServer instance = null;
        instance.fillXincoCoreData();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findXincoCoreNodes method, of class XincoCoreNodeServer.
     */
    public void testFindXincoCoreNodes() {
        System.out.println("findXincoCoreNodes");
        String attrS = "";
        int attrLID = 0;
        Vector expResult = null;
        Vector result = XincoCoreNodeServer.findXincoCoreNodes(attrS, attrLID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXincoCoreNodeParents method, of class XincoCoreNodeServer.
     */
    public void testGetXincoCoreNodeParents() {
        System.out.println("getXincoCoreNodeParents");
        int attrID = 0;
        Vector expResult = null;
        Vector result = XincoCoreNodeServer.getXincoCoreNodeParents(attrID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findById method, of class XincoCoreNodeServer.
     */
    public void testFindById() throws Exception {
        System.out.println("findById");
        HashMap parameters = null;
        XincoCoreNodeServer instance = null;
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.findById(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findWithDetails method, of class XincoCoreNodeServer.
     */
    public void testFindWithDetails() throws Exception {
        System.out.println("findWithDetails");
        HashMap parameters = null;
        XincoCoreNodeServer instance = null;
        AbstractAuditableObject[] expResult = null;
        AbstractAuditableObject[] result = instance.findWithDetails(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class XincoCoreNodeServer.
     */
    public void testCreate() {
        System.out.println("create");
        AbstractAuditableObject value = null;
        XincoCoreNodeServer instance = null;
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.create(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class XincoCoreNodeServer.
     */
    public void testUpdate() {
        System.out.println("update");
        AbstractAuditableObject value = null;
        XincoCoreNodeServer instance = null;
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.update(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class XincoCoreNodeServer.
     */
    public void testDelete() {
        System.out.println("delete");
        AbstractAuditableObject value = null;
        XincoCoreNodeServer instance = null;
        instance.delete(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class XincoCoreNodeServer.
     */
    public void testGetParameters() {
        System.out.println("getParameters");
        XincoCoreNodeServer instance = null;
        HashMap expResult = null;
        HashMap result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNewID method, of class XincoCoreNodeServer.
     */
    public void testGetNewID() {
        System.out.println("getNewID");
        boolean a = false;
        XincoCoreNodeServer instance = null;
        int expResult = 0;
        int result = instance.getNewID(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write2DB method, of class XincoCoreNodeServer.
     */
    public void testWrite2DB() {
        System.out.println("write2DB");
        XincoCoreNodeServer instance = null;
        boolean expResult = false;
        boolean result = instance.write2DB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreNodeServer.
     */
    public void testDeleteFromDB_0args() {
        System.out.println("deleteFromDB");
        XincoCoreNodeServer instance = null;
        boolean expResult = false;
        boolean result = instance.deleteFromDB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXincoCoreNodes method, of class XincoCoreNodeServer.
     */
    public void testGetXincoCoreNodes() {
        System.out.println("getXincoCoreNodes");
        XincoCoreNodeServer instance = null;
        Vector expResult = null;
        Vector result = instance.getXincoCoreNodes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setXincoCoreNodes method, of class XincoCoreNodeServer.
     */
    public void testSetXincoCoreNodes() {
        System.out.println("setXincoCoreNodes");
        Vector xincoCoreNodes = null;
        XincoCoreNodeServer instance = null;
        instance.setXincoCoreNodes(xincoCoreNodes);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXincoCoreData method, of class XincoCoreNodeServer.
     */
    public void testGetXincoCoreData() {
        System.out.println("getXincoCoreData");
        XincoCoreNodeServer instance = null;
        Vector expResult = null;
        Vector result = instance.getXincoCoreData();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setXincoCoreData method, of class XincoCoreNodeServer.
     */
    public void testSetXincoCoreData() {
        System.out.println("setXincoCoreData");
        Vector xincoCoreData = null;
        XincoCoreNodeServer instance = null;
        instance.setXincoCoreData(xincoCoreData);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXincoCoreACL method, of class XincoCoreNodeServer.
     */
    public void testGetXincoCoreACL() {
        System.out.println("getXincoCoreACL");
        XincoCoreNodeServer instance = null;
        Vector expResult = null;
        Vector result = instance.getXincoCoreACL();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setXincoCoreAcl method, of class XincoCoreNodeServer.
     */
    public void testSetXincoCoreAcl() {
        System.out.println("setXincoCoreAcl");
        Vector xincoCoreAcl = null;
        XincoCoreNodeServer instance = null;
        instance.setXincoCoreAcl(xincoCoreAcl);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
