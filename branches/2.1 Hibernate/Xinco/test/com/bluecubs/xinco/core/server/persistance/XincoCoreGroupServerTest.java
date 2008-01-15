/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.server.XincoCoreGroupServer;
import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.core.XincoCoreGroup;
import com.bluecubs.xinco.core.persistence.audit.tools.XincoAbstractAuditableObject;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;
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
public class XincoCoreGroupServerTest {

    public XincoCoreGroupServerTest() {
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
     * Test of write2DB method, of class XincoCoreGroupServer.
     * @throws java.lang.Exception 
     */
    @Test
    public void write2DB() throws Exception {
        System.out.println("write2DB");
        XincoCoreGroupServer instance = new XincoCoreGroupServer(1);
        instance.setTransactionTime(DateRange.startingNow());
        boolean expResult = true;
        boolean result = instance.write2DB();
        assertEquals(expResult, result);
    }

    /**
     * Test of getXincoCoreGroups method, of class XincoCoreGroupServer.
     */
    @Test
    public void getXincoCoreGroups() {
        System.out.println("getXincoCoreGroups");
        Vector result = XincoCoreGroupServer.getXincoCoreGroups();
        assertTrue(result.size() > 0);
    }

    /**
     * Test of findById method, of class XincoCoreGroupServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findById() {
        try {
            System.out.println("findById");
            HashMap parameters = new HashMap();
            parameters.put("id", 1);
            XincoCoreGroupServer instance = new XincoCoreGroupServer(1);
            XincoAbstractAuditableObject result = instance.findById(parameters);
            assertTrue(!((XincoCoreGroup) result).getDesignation().equals(null));
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreGroupServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findWithDetails() {
        try {
            System.out.println("findWithDetails");
            HashMap parameters = new HashMap();
            parameters.put("designation", "general.group.admin");
            XincoCoreGroupServer instance = new XincoCoreGroupServer(1);
            XincoAbstractAuditableObject[] result = instance.findWithDetails(parameters);
            assertTrue(((XincoCoreGroup) result[0]).getDesignation().equals("general.group.admin"));
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreGroupServer.
     * @throws java.lang.Exception 
     */
    @Test
    public void deleteFromDB() throws Exception {
        System.out.println("deleteFromDB");
        XincoCoreGroupServer instance = new XincoCoreGroupServer(0,"Test group",1);
        instance.write2DB();
        boolean expResult = true;
        boolean result = instance.deleteFromDB();
        assertEquals(expResult, result);
    }

    /**
     * Test of getParameters method, of class XincoCoreGroupServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void getParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreGroupServer instance = new XincoCoreGroupServer(1);
            HashMap expResult = new HashMap();
            expResult.put("id", instance.getId());
            HashMap result = instance.getParameters();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }
}
