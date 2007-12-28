/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.persistance.XincoCoreACE;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAbstractAuditableObject;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author javydreamercsw
 */
public class XincoCoreACEServerTest {

    public XincoCoreACEServerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getXincoCoreACL method, of class XincoCoreACEServer.
     */
    @Test
    public void getXincoCoreACL() {
        System.out.println("getXincoCoreACL");
        int attrID = 1;
        String attrT = "xincoCoreDataId";
        Vector result = XincoCoreACEServer.getXincoCoreACL(attrID, attrT);
        assertTrue(result.size()>0);
    }

    /**
     * Test of checkAccess method, of class XincoCoreACEServer.
     */
    @Test
    public void checkAccess() {
        try {
            System.out.println("checkAccess");
            XincoCoreUserServer attrU = new XincoCoreUserServer(1);
            XincoCoreNodeServer node = new XincoCoreNodeServer(1);
            XincoCoreACE result = XincoCoreACEServer.checkAccess(attrU, node.getXincoCoreACL());
            assertTrue(result!=null);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findById method, of class XincoCoreACEServer.
     */
    @Test
    public void findById() {
        System.out.println("findById");
        HashMap parameters = null;
        XincoCoreACEServer instance = null;
        XincoAbstractAuditableObject expResult = null;
        XincoAbstractAuditableObject result = instance.findById(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findWithDetails method, of class XincoCoreACEServer.
     */
    @Test
    public void findWithDetails() {
        System.out.println("findWithDetails");
        HashMap parameters = null;
        XincoCoreACEServer instance = null;
        XincoAbstractAuditableObject[] expResult = null;
        XincoAbstractAuditableObject[] result = instance.findWithDetails(parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class XincoCoreACEServer.
     */
    @Test
    public void create() {
        System.out.println("create");
        XincoAbstractAuditableObject value = null;
        XincoCoreACEServer instance = null;
        XincoAbstractAuditableObject expResult = null;
        XincoAbstractAuditableObject result = instance.create(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class XincoCoreACEServer.
     */
    @Test
    public void update() {
        System.out.println("update");
        XincoAbstractAuditableObject value = null;
        XincoCoreACEServer instance = null;
        XincoAbstractAuditableObject expResult = null;
        XincoAbstractAuditableObject result = instance.update(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class XincoCoreACEServer.
     */
    @Test
    public void delete() {
        System.out.println("delete");
        XincoAbstractAuditableObject value = null;
        XincoCoreACEServer instance = null;
        instance.delete(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class XincoCoreACEServer.
     */
    @Test
    public void getParameters() {
        System.out.println("getParameters");
        XincoCoreACEServer instance = null;
        HashMap expResult = null;
        HashMap result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNewID method, of class XincoCoreACEServer.
     */
    @Test
    public void getNewID() {
        System.out.println("getNewID");
        XincoCoreACEServer instance = null;
        int expResult = 0;
        int result = instance.getNewID();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreACEServer.
     * @throws java.lang.Exception 
     */
    @Test
    public void deleteFromDB() throws Exception {
        System.out.println("deleteFromDB");
        XincoCoreACEServer instance = null;
        boolean expResult = false;
        boolean result = instance.deleteFromDB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of write2DB method, of class XincoCoreACEServer.
     * @throws java.lang.Exception 
     */
    @Test
    public void write2DB() throws Exception {
        System.out.println("write2DB");
        XincoCoreACEServer instance = null;
        boolean expResult = false;
        boolean result = instance.write2DB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
