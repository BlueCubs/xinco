package com.bluecubs.xinco.core;

import org.junit.*;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Javier A. Ortiz Bultron<javier.ortiz.78@gmail.com>
 */
public class OPCodeTest {

    public OPCodeTest() {
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
     * Test of values method, of class OPCode.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        assertTrue(OPCode.values().length > 0);
    }

    /**
     * Test of getOPCode method, of class OPCode.
     */
    @Test
    public void testGetOPCode() {
        System.out.println("getOPCode");
        assertTrue(OPCode.getOPCode(1) != null);
        assertTrue(OPCode.getOPCode(0) == null);
    }

    /**
     * Test of getName method, of class OPCode.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        assertTrue(OPCode.getOPCode(1).getName() != null);
    }

    /**
     * Test of valueOf method, of class OPCode.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        OPCode expResult = OPCode.CHECKIN;
        OPCode result = OPCode.valueOf(OPCode.CHECKIN.name());
        assertTrue(expResult == result);
    }
}
