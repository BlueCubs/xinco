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
import com.bluecubs.xinco.persistence.XincoCoreACE;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
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
        assertTrue(result.size() > 0);
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
            assertTrue(result != null);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findById method, of class XincoCoreACEServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findById() {
        try {
            System.out.println("findById");
            HashMap parameters = new HashMap();
            parameters.put("id", 1);
            XincoCoreACEServer instance = new XincoCoreACEServer(2);
            AbstractAuditableObject result = instance.findById(parameters);
            assertTrue(((XincoCoreACE) result).getId() == 1);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreACEServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findWithDetails() {
        try {
            boolean passed = false;
            System.out.println("findWithDetails");
            HashMap parameters = new HashMap();
            XincoCoreACEServer instance = new XincoCoreACEServer();
            parameters.put("xincoCoreDataId", 1);
            AbstractAuditableObject[] result = instance.findWithDetails(parameters);
            if ((((XincoCoreACE) result[0]).getXincoCoreDataId() == 1) == true) {
                passed = true;
            }
            parameters.clear();
            parameters.put("xincoCoreNodeId", 1);
            result = instance.findWithDetails(parameters);
            if ((((XincoCoreACE) result[0]).getXincoCoreNodeId() == 1) == true) {
                passed = true;
            }
            parameters.clear();
            parameters.put("xincoCoreUserId", 1);
            result = instance.findWithDetails(parameters);
            if ((((XincoCoreACE) result[0]).getXincoCoreUserId() == 1) == true) {
                passed = true;
            }
            assertTrue(passed);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getParameters method, of class XincoCoreACEServer.
     */
    @Test
    public void getParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreACEServer instance = new XincoCoreACEServer(1);
            HashMap result = instance.getParameters();
            assertTrue(result.size() > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getNewID method, of class XincoCoreACEServer.
     */
    @Test
    public void getNewID() {
        try {
            System.out.println("getNewTableID");
            XincoCoreACEServer instance = new XincoCoreACEServer();
            int result = instance.getNewID();
            assertTrue(result > 1000);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreACEServer.
     * @throws java.lang.Exception 
     */
    @Test
    public void write2DB() throws Exception {
        try {
            System.out.println("write2DB");
            XincoCoreACEServer instance = new XincoCoreACEServer(0, 1, 1, 1, 1, true, true, true, true);
            assertTrue(instance.write2DB());
            deleteFromDB(instance.getId());
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreACEServer.
     * @param id 
     * @throws java.lang.Exception
     */
    public void deleteFromDB(int id) throws Exception {
        try {
            System.out.println("deleteFromDB: " + id);
            XincoCoreACEServer instance = new XincoCoreACEServer(id);
            assertTrue(instance.deleteFromDB());
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }
}
