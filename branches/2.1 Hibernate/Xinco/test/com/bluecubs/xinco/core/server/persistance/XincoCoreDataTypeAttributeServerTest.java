/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistance.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.persistance.XincoCoreDataTypeAttributePK;
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
 * @author Javier A. Ortiz
 */
public class XincoCoreDataTypeAttributeServerTest {

    public XincoCoreDataTypeAttributeServerTest() {
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
     * Test of getXincoCoreDataTypeAttributes method, of class XincoCoreDataTypeAttributeServer.
     */
    @Test
    public void getXincoCoreDataTypeAttributes() {
        System.out.println("getXincoCoreDataTypeAttributes");
        try {
            Vector result = XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(1);
            assertTrue(result.size() > 0);
        } catch (Throwable e) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, e);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findById method, of class XincoCoreDataTypeAttributeServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findById() {
        try {
            System.out.println("findById");
            HashMap parameters = new HashMap();
            parameters.put("attributeId", 1);
            parameters.put("xincoCoreDataTypeId", 1);
            XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer();
            XincoCoreDataTypeAttribute result = (XincoCoreDataTypeAttribute) instance.findById(parameters);
            assertTrue(result.getXincoCoreDataTypeAttributePK().getAttributeId() == 1);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreDataTypeAttributeServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findWithDetails() {
        try {
            System.out.println("findById");
            HashMap parameters = new HashMap();
            parameters.put("xincoCoreDataTypeId", 1);
            XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer();
            XincoCoreDataTypeAttribute[] result = (XincoCoreDataTypeAttribute[]) instance.findWithDetails(parameters);
            assertTrue(result[0].getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId() == 1);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getParameters method, of class XincoCoreDataTypeAttributeServer.
     */
    @Test
    public void getParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer(new XincoCoreDataTypeAttributePK(1, 1));
            HashMap result = instance.getParameters();
            assertTrue(result.size() > 0);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getNewID method, of class XincoCoreDataTypeAttributeServer.
     */
    @Test
    public void getNewID() {
        try {
            System.out.println("getNewTableID");
            XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer(new XincoCoreDataTypeAttributePK(1, 1));
            assertTrue(instance.getNewID() > 0);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreDataTypeAttributeServer.
     * @throws java.lang.Exception 
     */
    @Test
    @SuppressWarnings("unchecked")
    public void write2DB() throws Exception {
        XincoCoreDataTypeAttribute result = null;
        try {
            System.out.println("write2DB");
            XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer(1,0,"test","varchar",255);
            instance.write2DB();
            HashMap parameters = new HashMap();
            parameters.put("xincoCoreDataTypeId", instance.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
            parameters.put("attributeId", instance.getXincoCoreDataTypeAttributePK().getAttributeId());
            System.out.println("Searching with parameters: "+parameters);
            result = (XincoCoreDataTypeAttribute) instance.findById(parameters);
            assertTrue(result.getXincoCoreDataTypeAttributePK().getAttributeId() == instance.getXincoCoreDataTypeAttributePK().getAttributeId());
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
        deleteFromDB(result.getXincoCoreDataTypeAttributePK());
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreDataTypeAttributeServer.
     * @param pk XincoCoreDataTypeAttributePK
     * @throws java.lang.Exception
     */
    public void deleteFromDB(XincoCoreDataTypeAttributePK pk) throws Exception {
        try {
            System.out.println("deleteFromDB");
            XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer(pk);
            instance.deleteFromDB();
            XincoCoreDataTypeAttribute result = (XincoCoreDataTypeAttribute) instance.findById(instance.getParameters());
            assertTrue(result == null);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }
}
