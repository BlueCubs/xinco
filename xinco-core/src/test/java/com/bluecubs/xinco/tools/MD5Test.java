package com.bluecubs.xinco.tools;

import static com.bluecubs.xinco.tools.MD5.encrypt;
import static java.lang.System.out;
import org.junit.*;
import static org.junit.Assert.assertNotSame;

/**
 *
 * @author Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com
 */
public class MD5Test {

    public MD5Test() {
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
     * Test of encrypt method, of class MD5.
     */
    @Test
    public void testEncrypt() throws Exception {
        out.println("encrypt");
        String text = "test";
        String result = encrypt(text);
        assertNotSame(text, result);
    }
}
