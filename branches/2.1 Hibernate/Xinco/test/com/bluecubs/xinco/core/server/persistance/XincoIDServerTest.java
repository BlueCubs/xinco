/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.server.XincoIDServer;
import com.bluecubs.xinco.core.XincoId;
import java.util.HashMap;
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
public class XincoIDServerTest {

    public XincoIDServerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        try {
            XincoIDServer instance = new XincoIDServer("xinco_core_user");
            System.err.println("undo");
            instance.setLastId(1000);
            instance.write2DB();
        } catch (Throwable e) {
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.SEVERE, null, e);
            fail("Something went wrong.");
        }
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getNewTableID method, of class XincoIDServer.
     */
    @Test
    public void getNewTableID() {
        try {
            System.err.println("getNewTableID");
            XincoIDServer instance = new XincoIDServer("xinco_core_user");
            int expResult = instance.getLastId() + 1;
            int result = instance.getNewTableID();
            assertEquals(expResult, result);
        } catch (Throwable e) {
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.SEVERE, null, e);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of write2DB method, of class XincoIDServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void write2DB() {
        XincoIDServer instance = null;
        try {
            System.err.println("write2DB");
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.INFO, "Creating new XincoId...");
            instance = new XincoIDServer("test", 10);
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.INFO, "Writing new XincoId...");
            instance.write2DB();
            HashMap parameters = new HashMap();
            parameters.put("tablename", "test");
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.INFO, "Searching for created ID...");
            assertTrue(((XincoId)instance.findById(parameters)).getLastId()==10);
            deleteFromDB("test");
        } catch (Throwable e) {
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.SEVERE, null, e);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findById method, of class XincoIDServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findById() {
        try {
            System.err.println("findById");
            XincoIDServer instance = new XincoIDServer();
            HashMap parameters = new HashMap();
            parameters.put("tablename", "xinco_core_user");
            assertTrue(instance.findById(parameters) != null);
        } catch (Throwable e) {
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.SEVERE, null, e);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findWithDetails method, of class XincoIDServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findWithDetails() {
        try {
            System.err.println("findWithDetails");
            HashMap parameters = new HashMap();
            parameters.put("lastId", new XincoIDServer("xinco_core_user").getLastId());
            XincoIDServer instance = new XincoIDServer();
            XincoId[] result = (XincoId[]) instance.findWithDetails(parameters);
            if (result.length > 0) {
                assertTrue(result[0].getTablename().equals("xinco_core_user"));
            } else {
                fail("No result fetched.");
            }
        } catch (Throwable e) {
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.SEVERE, null, e);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getParameters method, of class XincoIDServer.
     */
    @Test
    public void getParameters() {
        try {
            System.err.println("getParameters");
            XincoIDServer instance = new XincoIDServer();
            HashMap result = instance.getParameters();
            assertTrue(!result.isEmpty());
        } catch (Throwable e) {
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.SEVERE, null, e);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoIDServer.
     * @param t
     * @throws java.lang.Exception 
     */
    @SuppressWarnings("unchecked")
    public void deleteFromDB(String t) throws Exception {
        try {
            System.err.println("deleteFromDB");
            XincoIDServer instance = new XincoIDServer("test");
            instance.setChangerID(1);
            instance.deleteFromDB();
            HashMap parameters = new HashMap();
            parameters.put("tablename", "test");
            assertTrue(instance.findById(parameters) == null);
        } catch (Throwable e) {
            Logger.getLogger(XincoIDServerTest.class.getName()).log(Level.SEVERE, null, e);
            fail("Something went wrong.");
        }
    }
}
