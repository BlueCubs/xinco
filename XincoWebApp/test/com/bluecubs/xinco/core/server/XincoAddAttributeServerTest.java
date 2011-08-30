/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
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
     * Test of write2DB method, of class XincoAddAttributeServer.
     */
    @Test
    public void testWrite2DB() throws Exception {
        System.out.println("write2DB");
        XincoAddAttributeServer instance = null;
        int expResult = 0;
        int result = instance.write2DB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXincoAddAttributes method, of class XincoAddAttributeServer.
     */
    @Test
    public void testGetXincoAddAttributes() {
        System.out.println("getXincoAddAttributes");
        int attrID = 0;
        ArrayList expResult = null;
        ArrayList result = XincoAddAttributeServer.getXincoAddAttributes(attrID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
