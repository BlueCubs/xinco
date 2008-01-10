/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistance.XincoCoreData;
import com.bluecubs.xinco.core.persistance.XincoCoreDataType;
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
public class XincoCoreDataServerTest {

    public XincoCoreDataServerTest() {
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
     * Test of getXincoCoreDataType method, of class XincoCoreDataServer.
     */
    @Test
    public void getXincoCoreDataType() {
        try {
            System.out.println("getXincoCoreDataType");
            XincoCoreDataServer instance = new XincoCoreDataServer(1);
            XincoCoreDataType result = instance.getXincoCoreDataType();
            assertEquals(result.getId(), 1);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findXincoCoreData method, of class XincoCoreDataServer.
     */
    @Test
    public void findXincoCoreData() {
        try {
            System.out.println("findXincoCoreData");
            String designation = "Apache License 2.0";
            int attrLID = 0;
            boolean attrSA = false;
            Vector result = XincoCoreDataServer.findXincoCoreData(designation, attrLID, attrSA);
            assertTrue(result.size() == 1);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getXincoCoreDataPath method, of class XincoCoreDataServer.
     */
    @Test
    public void getXincoCoreDataPath() {
        try {
            System.out.println("getXincoCoreDataPath");
            String attrRP = "test";
            int attrID = 0;
            String attrFN = "test";
            String result = XincoCoreDataServer.getXincoCoreDataPath(attrRP, attrID, attrFN);
            assertTrue(result.length() > 0);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findById method, of class XincoCoreDataServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findById() {
        try {
            System.out.println("findById");
            HashMap parameters = new HashMap();
            parameters.put("id", 1);
            XincoCoreDataServer instance = new XincoCoreDataServer(2);
            XincoCoreData result = (XincoCoreData) instance.findById(parameters);
            assertEquals("Apache License 2.0", result.getDesignation());
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreDataServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findWithDetails() {
        try {
            System.out.println("findWithDetails");
            HashMap parameters = new HashMap();
            parameters.put("xincoCoreNodeId", 1);
            XincoCoreDataServer instance = new XincoCoreDataServer(2);
            XincoCoreData[] result = (XincoCoreData[]) instance.findWithDetails(parameters);
            assertTrue(result[0].getDesignation().equals("Apache License 2.0"));
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getParameters method, of class XincoCoreDataServer.
     */
    @Test
    public void getParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreDataServer instance = new XincoCoreDataServer(2);
            HashMap result = instance.getParameters();
            assertTrue(!result.isEmpty());
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getNewTableID method, of class XincoCoreDataServer.
     */
    @Test
    public void getNewID() {
        try {
            System.out.println("getNewTableID");
            XincoCoreDataServer instance = new XincoCoreDataServer(2);
            assertTrue(instance.getNewID() > 1000);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreDataServer.
     * @throws java.lang.Exception 
     */
    @Test
    @SuppressWarnings("unchecked")
    public void write2DB() throws Exception {
        XincoCoreDataServer instance = null;
        try {
            System.out.println("write2DB");
            instance = new XincoCoreDataServer(0, 1, 1, 1, "test", 1);
            instance.write2DB();
            HashMap parameters = new HashMap();
            parameters.put("id", instance.getId());
            assertTrue(instance.findById(parameters) != null);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
        deleteFromDB(instance.getId());
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreDataServer.
     * @param id 
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    public void deleteFromDB(int id) throws Exception {
        try {
            System.out.println("deleteFromDB");
            XincoCoreDataServer instance = new XincoCoreDataServer(id);
            instance.deleteFromDB();
            HashMap parameters = new HashMap();
            parameters.put("id", id);
            assertTrue(instance.findById(parameters) == null);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getXincoCoreLogs method, of class XincoCoreDataServer.
     */
    @Test
    public void getXincoCoreLogs() {
        try {
            System.out.println("getXincoCoreLogs");
            XincoCoreDataServer instance = new XincoCoreDataServer(1);
            assertTrue(instance.getXincoCoreLogs().size() > 0);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of setXincoCoreLogs method, of class XincoCoreDataServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void setXincoCoreLogs() {
        try {
            System.out.println("setXincoCoreLogs");
            Vector xinco_core_logs = new Vector();
            xinco_core_logs.add("test");
            XincoCoreDataServer instance = new XincoCoreDataServer();
            instance.setXincoCoreLogs(xinco_core_logs);
            assertTrue(instance.getXincoCoreLogs().get(0).equals("test"));
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getXincoAddAttributes method, of class XincoCoreDataServer.
     */
    @Test
    public void getXincoAddAttributes() {
        try {
            System.out.println("getXincoAddAttributes");
            XincoCoreDataServer instance = new XincoCoreDataServer(1);
            assertTrue(instance.getXincoAddAttributes().size() > 0);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of setXincoAddAttributes method, of class XincoCoreDataServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void setXincoAddAttributes() {
        try {
            System.out.println("setXincoAddAttributes");
            Vector xinco_core_logs = new Vector();
            xinco_core_logs.add("test");
            XincoCoreDataServer instance = new XincoCoreDataServer();
            instance.setXincoAddAttributes(xinco_core_logs);
            assertTrue(instance.getXincoAddAttributes().get(0).equals("test"));
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getXincoCoreACL method, of class XincoCoreDataServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void getXincoCoreACL() {
        try {
            System.out.println("getXincoCoreACL");
            Vector xinco_core_logs = new Vector();
            xinco_core_logs.add("test");
            XincoCoreDataServer instance = new XincoCoreDataServer();
            instance.setXincoCoreACL(xinco_core_logs);
            assertTrue(instance.getXincoCoreACL().get(0).equals("test"));
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }
}
