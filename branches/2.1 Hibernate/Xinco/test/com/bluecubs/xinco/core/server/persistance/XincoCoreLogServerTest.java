/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.server.XincoCoreLogServer;
import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.core.XincoVersion;
import com.bluecubs.xinco.core.XincoCoreLog;
import com.bluecubs.xinco.core.persistence.audit.tools.XincoAbstractAuditableObject;
import java.util.Date;
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
public class XincoCoreLogServerTest {

    public XincoCoreLogServerTest() {
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
     * Test of getXincoCoreLogs method, of class XincoCoreLogServer.
     */
    @Test
    public void getXincoCoreLogs() {
        try {
            System.out.println("getXincoCoreLogs");
            int id = 1;
            Vector result = XincoCoreLogServer.getXincoCoreLogs(id);
            assertTrue(result.size() > 0);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreLogServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getVersion method, of class XincoCoreLogServer.
     */
    @Test
    public void getVersion() {
        try {
            System.out.println("getVersion");
            XincoCoreLogServer instance = new XincoCoreLogServer(1);
            XincoVersion result = instance.getVersion();
            assertTrue(result != null);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLogServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findById method, of class XincoCoreLogServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findById() {
         try {
            System.out.println("findById");
            HashMap parameters = new HashMap();
            parameters.put("id", 1);
            XincoCoreLogServer instance = new XincoCoreLogServer(2);
            XincoAbstractAuditableObject result = instance.findById(parameters);
            assertTrue(((XincoCoreLog) result).getId() == 1);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLogServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreLogServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findWithDetails() {
         try {
            System.out.println("findWithDetails");
            HashMap parameters = new HashMap();
            parameters.put("xincoCoreUserId", 1);
            XincoCoreLogServer instance = new XincoCoreLogServer(2);
            XincoCoreLog[] result = (XincoCoreLog[]) instance.findWithDetails(parameters);
            assertTrue(result[0].getOpDescription().equals("audit.general.create"));
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreLogServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getParameters method, of class XincoCoreLogServer.
     */
    @Test
    public void getParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreLogServer instance = new XincoCoreLogServer(2);
            HashMap result = instance.getParameters();
            assertTrue(!result.isEmpty());
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreLogServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getNewID method, of class XincoCoreLogServer.
     */
    @Test
    public void getNewID() {
        try {
            System.out.println("getNewTableID");
            XincoCoreLogServer instance = new XincoCoreLogServer(2);
            assertTrue(instance.getNewID() > 1000);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreLogServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreLogServer.
     * @throws java.lang.Exception 
     */
    @Test
    public void write2DB() throws Exception {
        try {
            System.out.println("write2DB");
            XincoCoreLogServer instance = new XincoCoreLogServer(0, 1,1,1,new Date(),"Test",1,0,0,"Test");
            assertTrue(instance.write2DB());
            deleteFromDB(instance.getId());
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreLogServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }
    
    /**
     * Test of deleteFromDB method, of class XincoCoreLogServer.
     * @param id
     * @throws java.lang.Exception 
     */
    public void deleteFromDB(int id) throws Exception {
        try {
            System.out.println("deleteFromDB");
            XincoCoreLogServer instance = new XincoCoreLogServer(id);
            instance.deleteFromDB();
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreLogServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

}
