/**
 *Copyright 2008 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community in exchange for free software!
 * More information on: http://www.bluecubs.org
 *************************************************************
 *
 * Name:            Test
 *
 * Description:     Test
 *
 * Original Author: Javier A. Ortiz
 * Date:            2008
 *
 * Modifications:
 *
 * Who?             When?             What?
 * 
 *************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.persistence.XincoCoreGroup;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
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
            AbstractAuditableObject result = instance.findById(parameters);
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
            AbstractAuditableObject[] result = instance.findWithDetails(parameters);
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
        XincoCoreGroupServer instance = new XincoCoreGroupServer(0, "Test group", 1);
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
