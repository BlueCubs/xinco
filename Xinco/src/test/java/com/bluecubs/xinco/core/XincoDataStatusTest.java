/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core;

import static com.bluecubs.xinco.core.XincoDataStatus.valueOf;
import static com.bluecubs.xinco.core.XincoDataStatus.values;
import static java.lang.System.out;
import org.junit.*;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
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
        out.println("values");
        assertTrue(values().length > 0);
    }

    /**
     * Test of valueOf method, of class XincoDataStatus.
     */
    @Test
    public void testValueOf() {
        out.println("valueOf");
        assertTrue(valueOf(values()[0].name()) != null);
    }
}
