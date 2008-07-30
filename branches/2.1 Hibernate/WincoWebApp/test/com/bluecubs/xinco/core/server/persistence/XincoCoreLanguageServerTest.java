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
import com.bluecubs.xinco.persistence.XincoCoreLanguage;
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
public class XincoCoreLanguageServerTest {

    public XincoCoreLanguageServerTest() {
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
     * Test of getXincoCoreLanguages method, of class XincoCoreLanguageServer.
     */
    @Test
    public void getXincoCoreLanguages() {
        System.out.println("getXincoCoreLanguages");
        Vector result = XincoCoreLanguageServer.getXincoCoreLanguages();
        assertTrue(result.size() > 0);
    }

    /**
     * Test of isLanguageUsed method, of class XincoCoreLanguageServer.
     */
    @Test
    public void isLanguageUsed() {
        try {
            System.out.println("isLanguageUsed");
            XincoCoreLanguageServer xcl = new XincoCoreLanguageServer(1);
            assertTrue(XincoCoreLanguageServer.isLanguageUsed(xcl));
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findById method, of class XincoCoreLanguageServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findById() {
        try {
            System.out.println("findById");
            HashMap parameters = new HashMap();
            parameters.put("id", 1);
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(1);
            AbstractAuditableObject result = instance.findById(parameters);
            assertTrue(((XincoCoreLanguage) result).getSign().equals("n/a"));
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreLanguageServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void findWithDetails() {
        try {
            System.out.println("findWithDetails");
            HashMap parameters = new HashMap();
            parameters.put("designation", "unknown");
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(1);
            AbstractAuditableObject[] result = instance.findWithDetails(parameters);
            assertTrue(((XincoCoreLanguage) result[0]).getSign().equals("n/a"));
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreLanguageServer.
     * @throws java.lang.Exception 
     */
    @Test
    public void write2DB() throws Exception {
        try {
            System.out.println("write2DB");
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(0, "t", "test");
            assertTrue(instance.write2DB());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreLanguageServer.
     * @throws java.lang.Exception 
     */
    @Test
    @SuppressWarnings("unchecked")
    public void deleteFromDB() throws Exception {
        try {
            System.out.println("deleteFromDB");
            HashMap parameters = new HashMap();
            parameters.put("designation", "test");
            int id = ((XincoCoreLanguage) new XincoCoreLanguageServer().findWithDetails(parameters)[0]).getId();
            System.out.println("Deleting: " + id);
            XincoCoreLanguageServer instance = new XincoCoreLanguageServer(id);
            instance.deleteFromDB();
            parameters.clear();
            parameters.put("id", id);
            assertTrue(instance.findById(parameters) == null);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreLanguageServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }
}
