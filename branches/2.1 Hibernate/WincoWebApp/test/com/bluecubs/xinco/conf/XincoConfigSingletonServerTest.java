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
package com.bluecubs.xinco.conf;

import java.math.BigInteger;
import java.util.Vector;
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
public class XincoConfigSingletonServerTest {

    public XincoConfigSingletonServerTest() {
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
     * Test of getInstance method, of class XincoConfigSingletonServer.
     */
    @Test
    public void getInstance() {
        System.out.println("getInstance");
        XincoConfigSingletonServer result = XincoConfigSingletonServer.getInstance();
        assertTrue(result != null);
    }

    /**
     * Test of init method, of class XincoConfigSingletonServer.
     */
    @Test
    public void init() {
        System.out.println("init");
        XincoConfigSingletonServer instance = XincoConfigSingletonServer.getInstance();
        assertTrue(instance.init());
    }

    /**
     * Test of getFileArchivePath method, of class XincoConfigSingletonServer.
     */
    @Test
    public void getFileArchivePath() {
        System.out.println("getFileArchivePath");
        XincoConfigSingletonServer instance = XincoConfigSingletonServer.getInstance();
        String result = instance.getFileArchivePath();
        String expResult = "C:\\Temp\\xinco\\file_repository\\archive\\";
        assertEquals(expResult, result);
    }

    /**
     * Test of getFileRepositoryPath method, of class XincoConfigSingletonServer.
     */
    @Test
    public void getFileRepositoryPath() {
        System.out.println("getFileRepositoryPath");
        XincoConfigSingletonServer instance = XincoConfigSingletonServer.getInstance();
        String expResult = "C:\\Temp\\xinco\\file_repository\\";
        String result = instance.getFileRepositoryPath();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFileIndexPath method, of class XincoConfigSingletonServer.
     */
    @Test
    public void getFileIndexPath() {
        System.out.println("getFileIndexPath");
        XincoConfigSingletonServer instance = XincoConfigSingletonServer.getInstance();
        String expResult = "C:\\Temp\\xinco\\file_repository\\index\\";
        String result = instance.getFileIndexPath();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFileArchivePeriod method, of class XincoConfigSingletonServer.
     */
    @Test
    public void getFileArchivePeriod() {
        System.out.println("getFileArchivePeriod");
        XincoConfigSingletonServer instance = XincoConfigSingletonServer.getInstance();
        BigInteger expResult = BigInteger.valueOf(14400000);
        BigInteger result = instance.getFileArchivePeriod();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFileIndexerCount method, of class XincoConfigSingletonServer.
     */
    @Test
    public void getFileIndexerCount() {
        System.out.println("getFileIndexerCount");
        XincoConfigSingletonServer instance = XincoConfigSingletonServer.getInstance();
        int expResult = 0;
        int result = instance.getFileIndexerCount();
        assertTrue(result > expResult);
    }

    /**
     * Test of getIndexFileTypesClass method, of class XincoConfigSingletonServer.
     */
    @Test
    public void getIndexFileTypesClass() {
        System.out.println("getIndexFileTypesClass");
        XincoConfigSingletonServer instance = XincoConfigSingletonServer.getInstance();
        Vector result = instance.getIndexFileTypesClass();
        assertTrue(result.size() > 0);
    }

    /**
     * Test of getIndexFileTypesExt method, of class XincoConfigSingletonServer.
     */
    @Test
    public void getIndexFileTypesExt() {
        System.out.println("getIndexFileTypesExt");
        XincoConfigSingletonServer instance = XincoConfigSingletonServer.getInstance();
        Vector result = instance.getIndexFileTypesExt();
        assertTrue(result.size() > 0);
    }

    /**
     * Test of getIndexNoIndex method, of class XincoConfigSingletonServer.
     */
    @Test
    public void getIndexNoIndex() {
        System.out.println("getIndexNoIndex");
        XincoConfigSingletonServer instance = XincoConfigSingletonServer.getInstance();
        String[] result = instance.getIndexNoIndex();
        assertTrue(result.length > 0);
    }

    /**
     * Test of getJNDIDB method, of class XincoConfigSingletonServer.
     */
    @Test
    public void getJNDIDB() {
        System.out.println("getJNDIDB");
        XincoConfigSingletonServer instance = XincoConfigSingletonServer.getInstance();
        String expResult = "java:comp/env/jdbc/XincoDB";
        String result = instance.getJNDIDB();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMaxSearchResult method, of class XincoConfigSingletonServer.
     */
    @Test
    public void getMaxSearchResult() {
        System.out.println("getMaxSearchResult");
        XincoConfigSingletonServer instance = XincoConfigSingletonServer.getInstance();
        int expResult = 100;
        int result = instance.getMaxSearchResult();
        assertEquals(expResult, result);
    }

    /**
     * Test of isAllowOutsideLinks method, of class XincoConfigSingletonServer.
     */
    @Test
    public void isAllowOutsideLinks() {
        System.out.println("isAllowOutsideLinks");
        XincoConfigSingletonServer instance = XincoConfigSingletonServer.getInstance();
        boolean expResult = true;
        boolean result = instance.isAllowOutsideLinks();
        assertEquals(expResult, result);
    }
}