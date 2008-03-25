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

import com.bluecubs.xinco.persistence.XincoAddAttribute;
import com.bluecubs.xinco.persistence.XincoAddAttributePK;
import com.bluecubs.xinco.core.server.persistence.XincoAddAttributeServer;
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
public class XincoAddAttributeServerTest {

    public XincoAddAttributeServerTest() {
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
     * Test of getXincoAddAttributes method, of class XincoAddAttributeServer.
     */
    @Test
    public void getXincoAddAttributes() {
        try {
            System.out.println("getXincoAddAttributes");
            int attrID = 1;
            Vector result = XincoAddAttributeServer.getXincoAddAttributes(attrID);
            assertTrue(result.size() > 0);
        } catch (Throwable ex) {
            Logger.getLogger(XincoAddAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findById method, of class XincoAddAttributeServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findById() {
        try {
            System.out.println("findById");
            HashMap parameters = new HashMap();
            parameters.put("attributeId", 1);
            parameters.put("xincoCoreDataId", 1);
            XincoAddAttributeServer instance = new XincoAddAttributeServer();
            XincoAddAttribute result = (XincoAddAttribute) instance.findById(parameters);
            assertTrue(result.getXincoAddAttributePK().getAttributeId() == 1);
        } catch (Throwable ex) {
            Logger.getLogger(XincoAddAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findWithDetails method, of class XincoAddAttributeServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findWithDetails() {
        try {
            System.out.println("findWithDetails");
            HashMap parameters = new HashMap();
            parameters.put("attribVarchar", "http://www.xinco.org");
            XincoAddAttributeServer instance = new XincoAddAttributeServer();
            XincoAddAttribute[] result = (XincoAddAttribute[]) instance.findWithDetails(parameters);
            if (result.length > 0) {
                assertTrue(result[0].getXincoAddAttributePK().getXincoCoreDataId() == 2);
            } else {
                fail("No result fetched.");
            }
        } catch (Throwable ex) {
            Logger.getLogger(XincoAddAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getParameters method, of class XincoAddAttributeServer.
     */
    @Test
    public void getParameters() {
        try {
            System.out.println("getParameters");
            XincoAddAttributeServer instance = new XincoAddAttributeServer(new XincoAddAttributePK(1, 1));
            HashMap result = instance.getParameters();
            assertTrue(result.size() > 0);
        } catch (Throwable ex) {
            Logger.getLogger(XincoAddAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getNewID method, of class XincoAddAttributeServer.
     */
    @Test
    public void getNewID() {
        try {
            System.out.println("getNewTableID");
            XincoAddAttributeServer instance = new XincoAddAttributeServer(new XincoAddAttributePK(1, 1));
            assertTrue(instance.getNewID() > 0);
        } catch (Throwable ex) {
            Logger.getLogger(XincoAddAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of write2DB method, of class XincoAddAttributeServer.
     * @throws java.lang.Exception 
     */
    @Test
    @SuppressWarnings("unchecked")
    public void write2DB() throws Exception {
        XincoAddAttribute result = null;
        try {
            System.out.println("write2DB");
            XincoAddAttributeServer instance = new XincoAddAttributeServer(1, 0, 1, 1, new Double(0), "test", "varchar", new Date(System.currentTimeMillis()));
            assertTrue(instance.write2DB());
            HashMap parameters = new HashMap();
            parameters.put("xincoCoreDataId", instance.getXincoAddAttributePK().getXincoCoreDataId());
            parameters.put("attributeId", instance.getXincoAddAttributePK().getAttributeId());
            System.out.println("Searching with parameters: " + parameters);
            result = (XincoAddAttribute) instance.findById(parameters);
            assertTrue(result.getXincoAddAttributePK().getAttributeId() == instance.getXincoAddAttributePK().getAttributeId());
        } catch (Throwable ex) {
            Logger.getLogger(XincoAddAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
        deleteFromDB(result.getXincoAddAttributePK());
    }

    /**
     * Test of deleteFromDB method, of class XincoAddAttributeServer.
     * @param pk XincoAddAttributePK
     * @throws java.lang.Exception 
     */
    public void deleteFromDB(XincoAddAttributePK pk) throws Exception {
        try {
            System.out.println("deleteFromDB");
            XincoAddAttributeServer instance = new XincoAddAttributeServer(pk);
            instance.deleteFromDB();
        } catch (Throwable ex) {
            Logger.getLogger(XincoAddAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }
}
