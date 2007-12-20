/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

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
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {

    }

    /**
     * Test of getNewID method, of class XincoIDServer.
     */
    @Test
    public void getNewID() {
        System.out.println("getNewID");
        String table_name = "xinco_core_user";
        XincoIDServer instance = new XincoIDServer(table_name);
        int expResult = instance.getLastId() + 1;
        System.err.println("Expected result: " + expResult);
        int result = instance.getNewID();
        System.out.println("undo");
        instance.setLastId(result - 1);
        instance.write2DB(true);
        assertEquals(expResult, result);
    }
}
