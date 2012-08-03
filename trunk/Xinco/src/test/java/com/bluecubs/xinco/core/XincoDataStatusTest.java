/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core;

import org.junit.*;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Javier A. Ortiz Bultron<javier.ortiz.78@gmail.com>
 */
public class XincoDataStatusTest {

    public XincoDataStatusTest() {
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
     * Test of values method, of class XincoDataStatus.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        assertTrue(XincoDataStatus.values().length > 0);
    }

    /**
     * Test of valueOf method, of class XincoDataStatus.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        assertTrue(XincoDataStatus.valueOf(XincoDataStatus.values()[0].name()) != null);
    }
}
