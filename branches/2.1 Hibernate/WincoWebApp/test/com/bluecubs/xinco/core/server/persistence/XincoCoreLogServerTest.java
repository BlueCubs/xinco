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

import com.bluecubs.xinco.core.XincoVersion;
import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.persistence.XincoCoreLog;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
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
            AbstractAuditableObject result = instance.findById(parameters);
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
    @SuppressWarnings("unchecked")
    public void write2DB() throws Exception {
        try {
            System.out.println("write2DB");
            XincoCoreLogServer instance = new XincoCoreLogServer(0, 1, 1, 1, new Date(), "Test", 1, 0, 0, "Test");
            assertTrue(instance.write2DB());
            HashMap parameters = new HashMap();
            parameters.put("id", instance.getId());
            System.out.println("Searching with parameters: " + parameters);
            XincoCoreLog result = (XincoCoreLog) instance.findById(parameters);
            assertTrue(result != null);
            assertTrue(result.getId() > 0);
            assertTrue(instance.getId() > 0);
            assertEquals(result.getId(), instance.getId());
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
            System.out.println("deleteFromDB id: " + id);
            XincoCoreLogServer instance = new XincoCoreLogServer(id);
            instance.deleteFromDB();
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreLogServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }
}
