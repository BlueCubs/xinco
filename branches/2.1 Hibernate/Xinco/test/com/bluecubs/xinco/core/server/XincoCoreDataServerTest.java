/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.persistence.XincoCoreData;
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
public class XincoCoreDataServerTest extends TestCase {
    
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
     * Test of setUser method, of class XincoCoreDataServer.
     */
    public void testSetUser() {
        System.out.println("setUser");
        XincoCoreUserServer user = null;
        XincoCoreDataServer instance = null;
        instance.setUser(user);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeFromDB method, of class XincoCoreDataServer.
     */
    public void testRemoveFromDB() throws Exception {
        System.out.println("removeFromDB");
        int userID = 0;
        int id = 0;
        XincoCoreDataServer.removeFromDB(userID, id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadBinaryData method, of class XincoCoreDataServer.
     */
    public void testLoadBinaryData() {
        System.out.println("loadBinaryData");
        XincoCoreData attrCD = null;
        byte[] expResult = null;
        byte[] result = XincoCoreDataServer.loadBinaryData(attrCD);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of saveBinaryData method, of class XincoCoreDataServer.
     */
    public void testSaveBinaryData() {
        System.out.println("saveBinaryData");
        XincoCoreData attrCD = null;
        byte[] attrBD = null;
        int expResult = 0;
        int result = XincoCoreDataServer.saveBinaryData(attrCD, attrBD);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findXincoCoreData method, of class XincoCoreDataServer.
     */
    public void testFindXincoCoreData() {
        System.out.println("findXincoCoreData");
        String attrS = "";
        int attrLID = 0;
        boolean attrSA = false;
        boolean attrSFD = false;
        Vector expResult = null;
        Vector result = XincoCoreDataServer.findXincoCoreData(attrS, attrLID, attrSA, attrSFD);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXincoCoreDataPath method, of class XincoCoreDataServer.
     */
    public void testGetXincoCoreDataPath() {
        System.out.println("getXincoCoreDataPath");
        String attrRP = "";
        int attrID = 0;
        String attrFN = "";
        String expResult = "";
        String result = XincoCoreDataServer.getXincoCoreDataPath(attrRP, attrID, attrFN);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findById method, of class XincoCoreDataServer.
     */
    public void testFindById() throws Exception {
        System.out.println("findById");
        HashMap parameters = null;
        XincoCoreDataServer instance = null;
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.findById(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findWithDetails method, of class XincoCoreDataServer.
     */
    public void testFindWithDetails() throws Exception {
        System.out.println("findWithDetails");
        HashMap parameters = null;
        XincoCoreDataServer instance = null;
        AbstractAuditableObject[] expResult = null;
        AbstractAuditableObject[] result = instance.findWithDetails(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class XincoCoreDataServer.
     */
    public void testCreate() {
        System.out.println("create");
        AbstractAuditableObject value = null;
        XincoCoreDataServer instance = null;
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.create(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class XincoCoreDataServer.
     */
    public void testUpdate() {
        System.out.println("update");
        AbstractAuditableObject value = null;
        XincoCoreDataServer instance = null;
        AbstractAuditableObject expResult = null;
        AbstractAuditableObject result = instance.update(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class XincoCoreDataServer.
     */
    public void testDelete() {
        System.out.println("delete");
        AbstractAuditableObject value = null;
        XincoCoreDataServer instance = null;
        instance.delete(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class XincoCoreDataServer.
     */
    public void testGetParameters() {
        System.out.println("getParameters");
        XincoCoreDataServer instance = null;
        HashMap expResult = null;
        HashMap result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNewID method, of class XincoCoreDataServer.
     */
    public void testGetNewID() {
        System.out.println("getNewID");
        boolean a = false;
        XincoCoreDataServer instance = null;
        int expResult = 0;
        int result = instance.getNewID(a);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write2DB method, of class XincoCoreDataServer.
     */
    public void testWrite2DB() {
        System.out.println("write2DB");
        XincoCoreDataServer instance = null;
        boolean expResult = false;
        boolean result = instance.write2DB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreDataServer.
     */
    public void testDeleteFromDB() {
        System.out.println("deleteFromDB");
        XincoCoreDataServer instance = null;
        boolean expResult = false;
        boolean result = instance.deleteFromDB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXincoCoreLogs method, of class XincoCoreDataServer.
     */
    public void testGetXincoCoreLogs() {
        System.out.println("getXincoCoreLogs");
        XincoCoreDataServer instance = null;
        Vector expResult = null;
        Vector result = instance.getXincoCoreLogs();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setXincoCoreLogs method, of class XincoCoreDataServer.
     */
    public void testSetXincoCoreLogs() {
        System.out.println("setXincoCoreLogs");
        Vector xincoCoreLogs = null;
        XincoCoreDataServer instance = null;
        instance.setXincoCoreLogs(xincoCoreLogs);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXincoAddAttributes method, of class XincoCoreDataServer.
     */
    public void testGetXincoAddAttributes() {
        System.out.println("getXincoAddAttributes");
        XincoCoreDataServer instance = null;
        Vector expResult = null;
        Vector result = instance.getXincoAddAttributes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setXincoAddAttributes method, of class XincoCoreDataServer.
     */
    public void testSetXincoAddAttributes() {
        System.out.println("setXincoAddAttributes");
        Vector xincoAddAttributes = null;
        XincoCoreDataServer instance = null;
        instance.setXincoAddAttributes(xincoAddAttributes);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXincoCoreACL method, of class XincoCoreDataServer.
     */
    public void testGetXincoCoreACL() {
        System.out.println("getXincoCoreACL");
        XincoCoreDataServer instance = null;
        Vector expResult = null;
        Vector result = instance.getXincoCoreACL();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setXincoCoreACL method, of class XincoCoreDataServer.
     */
    public void testSetXincoCoreACL() {
        System.out.println("setXincoCoreACL");
        Vector xincoCoreACL = null;
        XincoCoreDataServer instance = null;
        instance.setXincoCoreACL(xincoCoreACL);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
