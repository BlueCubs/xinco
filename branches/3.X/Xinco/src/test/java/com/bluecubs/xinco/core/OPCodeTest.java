package com.bluecubs.xinco.core;

import static com.bluecubs.xinco.core.OPCode.CHECKIN;
import static com.bluecubs.xinco.core.OPCode.getOPCode;
import static com.bluecubs.xinco.core.OPCode.valueOf;
import static com.bluecubs.xinco.core.OPCode.values;
import static java.lang.System.out;
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
        out.println("values");
        assertTrue(values().length > 0);
    }

    /**
     * Test of getOPCode method, of class OPCode.
     */
    @Test
    public void testGetOPCode() {
        out.println("getOPCode");
        assertTrue(getOPCode(1) != null);
        assertTrue(getOPCode(0) == null);
    }

    /**
     * Test of getName method, of class OPCode.
     */
    @Test
    public void testGetName() {
        out.println("getName");
        assertTrue(getOPCode(1).getName() != null);
    }

    /**
     * Test of valueOf method, of class OPCode.
     */
    @Test
    public void testValueOf() {
        out.println("valueOf");
        OPCode expResult = CHECKIN;
        OPCode result = valueOf(CHECKIN.name());
        assertTrue(expResult == result);
    }
}
