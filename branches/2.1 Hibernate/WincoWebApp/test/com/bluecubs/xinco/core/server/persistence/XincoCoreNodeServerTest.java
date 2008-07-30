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
import com.bluecubs.xinco.persistence.XincoCoreNode;
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
public class XincoCoreNodeServerTest {

    public XincoCoreNodeServerTest() {
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
     * Test of fillXincoCoreNodes method, of class XincoCoreNodeServer.
     */
    @Test
    public void fillXincoCoreNodes() {
        try {
            System.out.println("fillXincoCoreNodes");
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            instance.fillXincoCoreNodes();
            assertTrue(instance.getXincoCoreNodes().size() > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of fillXincoCoreData method, of class XincoCoreNodeServer.
     */
    @Test
    public void fillXincoCoreData() {
        try {
            System.out.println("fillXincoCoreData");
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            instance.fillXincoCoreData();
            assertTrue(instance.getXincoCoreData().size() > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findXincoCoreNodes method, of class XincoCoreNodeServer.
     */
    @Test
    public void findXincoCoreNodes() {
        try {
            System.out.println("findXincoCoreNodes");
            String designation = "xincoRoot";
            int language_id = 1;
            Vector result = XincoCoreNodeServer.findXincoCoreNodes(designation, language_id);
            assertTrue(result.size() > 0);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getXincoCoreNodeParents method, of class XincoCoreNodeServer.
     */
    @Test
    public void getXincoCoreNodeParents() {
        try {
            System.out.println("getXincoCoreNodeParents");
            int id = 1;
            Vector result = XincoCoreNodeServer.getXincoCoreNodeParents(id);
            assertTrue(result.size() == 1);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getXincoCoreNodes method, of class XincoCoreNodeServer.
     */
    @Test
    public void getXincoCoreNodes() {
        try {
            System.out.println("getXincoCoreNodes");
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            instance.fillXincoCoreNodes();
            Vector result = instance.getXincoCoreNodes();
            assertTrue(result.size() > 0);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getXincoCoreData method, of class XincoCoreNodeServer.
     */
    @Test
    public void getXincoCoreData() {
        try {
            System.out.println("getXincoCoreData");
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            instance.fillXincoCoreData();
            Vector result = instance.getXincoCoreData();
            assertTrue(result.size() > 0);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getXincoCoreACL method, of class XincoCoreNodeServer.
     */
    @Test
    public void getXincoCoreACL() {
        try {
            System.out.println("getXincoCoreACL");
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            Vector result = instance.getXincoCoreACL();
            assertTrue(result.size() > 0);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findById method, of class XincoCoreNodeServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findById() {
        try {
            System.out.println("findById");
            HashMap parameters = new HashMap();
            parameters.put("id", 1);
            XincoCoreNodeServer instance = new XincoCoreNodeServer();
            XincoCoreNode result = (XincoCoreNode) instance.findById(parameters);
            assertTrue(result.getId() == 1);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreNodeServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findWithDetails() {
        try {
            System.out.println("findWithDetails");
            HashMap parameters = new HashMap();
            parameters.put("xincoCoreLanguageId", 1);
            XincoCoreNodeServer instance = new XincoCoreNodeServer();
            AbstractAuditableObject[] result = instance.findWithDetails(parameters);
            assertTrue(result.length > 0);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getParameters method, of class XincoCoreNodeServer.
     */
    @Test
    public void getParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            HashMap result = instance.getParameters();
            assertTrue(!result.isEmpty());
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreNodeServer.
     * @param id 
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    public void deleteFromDB(int id) throws Exception {
        try {
            System.out.println("deleteFromDB");
            XincoCoreNodeServer instance = new XincoCoreNodeServer(id);
            assertTrue(instance.deleteFromDB());
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreNodeServer.
     * @throws java.lang.Exception 
     */
    @Test
    @SuppressWarnings("unchecked")
    public void write2DB() throws Exception {
        XincoCoreNodeServer instance = null;
        try {
            System.out.println("write2DB");
            instance = new XincoCoreNodeServer(0, 1, 1, "test", 1);
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
}
