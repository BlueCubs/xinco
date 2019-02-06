package com.bluecubs.xinco.core;

import static java.lang.System.out;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class XincoExceptionTest {

    public XincoExceptionTest() {
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
     * Test of toString method, of class XincoException.
     */
    @Test
    public void testToString() {
        out.println("toString");
        String expResult = "test";
        XincoException instance = new XincoException(expResult);
        String result = instance.toString();
        assertEquals(expResult, result);
        instance = new XincoException();
        result = instance.toString();
        assertEquals("", result);
        instance = new XincoException(new ArrayList<>(
                asList("1", "2", "3", "4", "5")));
        assertTrue(!instance.toString().isEmpty());
    }
}
