package com.bluecubs.xinco.tools;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class ToolTest {
    
    public ToolTest() {
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
     * Test of compareNumberStrings method, of class Tool.
     */
    @Test
    public void testCompareNumberStrings() {
        System.out.println("compareNumberStrings");
        String first = "2.1.0";
        String second = "2.01.00";
        assertTrue(Tool.compareNumberStrings(first, second));
        second = "3.1.0";
        assertFalse(Tool.compareNumberStrings(first, second));
        second = "2.1";
        assertFalse(Tool.compareNumberStrings(first, second));
        second = "M";
        assertFalse(Tool.compareNumberStrings(first, second));
    }

    /**
     * Test of compareNumberStrings method, of class Tool.
     */
    @Test
    public void testCompareNumberStrings3() {
        System.out.println("conpareNumberStrings2");
        String first = "2,1,0";
        String second = "2,01,00";
        assertTrue(Tool.compareNumberStrings(first, second, ","));
    }
}
