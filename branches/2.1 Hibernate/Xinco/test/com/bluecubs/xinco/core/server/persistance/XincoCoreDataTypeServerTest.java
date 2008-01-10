/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.persistance.XincoCoreDataType;
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
public class XincoCoreDataTypeServerTest {

    public XincoCoreDataTypeServerTest() {
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
     * Test of getXincoCoreDataTypes method, of class XincoCoreDataTypeServer.
     */
    @Test
    public void getXincoCoreDataTypes() {
        System.out.println("getXincoCoreDataTypes");
        Vector result = XincoCoreDataTypeServer.getXincoCoreDataTypes();
        assertTrue(result.size() > 0);
    }

    /**
     * Test of findById method, of class XincoCoreDataTypeServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findById() {
        try {
            System.out.println("findById");
            HashMap parameters = new HashMap();
            parameters.put("id", 1);
            XincoCoreDataTypeServer instance = new XincoCoreDataTypeServer(2);
            XincoCoreDataType result = (XincoCoreDataType) instance.findById(parameters);
            assertTrue(result.getDescription() != null);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreDataTypeServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findWithDetails() {
        try {
            System.out.println("findWithDetails");
            HashMap parameters = new HashMap();
            parameters.put("designation", "general.data.type.file");
            XincoCoreDataTypeServer instance = new XincoCoreDataTypeServer(2);
            XincoCoreDataType result = (XincoCoreDataType) instance.findWithDetails(parameters)[0];
            assertTrue(result.getDesignation().equals("general.data.type.file"));
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getParameters method, of class XincoCoreDataTypeServer.
     */
    @Test
    public void getParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreDataTypeServer instance = new XincoCoreDataTypeServer(1);
            HashMap result = instance.getParameters();
            assertTrue(result.size() > 0);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getNewID method, of class XincoCoreDataTypeServer.
     */
    @Test
    public void getNewID() {
        try {
            System.out.println("getNewTableID");
            XincoCoreDataTypeServer instance = new XincoCoreDataTypeServer(1);
            assertTrue(instance.getNewID() > 1000);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreDataTypeServer.
     * @throws java.lang.Exception 
     */
    @Test
    @SuppressWarnings("unchecked")
    public void write2DB() throws Exception {
        try {
            System.out.println("write2DB");
            XincoCoreDataTypeServer instance = new XincoCoreDataTypeServer(0, "test", "test", new Vector());
            assertTrue(instance.write2DB());
            deleteFromDB(instance.getId());
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }
    
    /**
     * Test of deleteFromDB method, of class XincoCoreDataTypeServer.
     * @param id 
     * @throws java.lang.Exception
     */
    public void deleteFromDB(int id) throws Exception {
        try {
            System.out.println("deleteFromDB");
            XincoCoreDataTypeServer instance = new XincoCoreDataTypeServer(id);
            instance.deleteFromDB();
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }
}
