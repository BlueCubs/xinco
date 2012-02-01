package com.bluecubs.xinco.tools;

import static org.junit.Assert.assertNotSame;
import org.junit.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
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
        System.out.println("encrypt");
        String text = "test";
        String result = MD5.encrypt(text);
        assertNotSame(text, result);
    }
}
