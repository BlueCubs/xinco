package com.bluecubs.xinco.tools;

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
public class XincoCrypterTest {
    
    public XincoCrypterTest() {
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
     * Test of encrypt method, of class XincoCrypter.
     */
    @Test
    public void testEncrypt() {
        System.out.println("encrypt");
        String str = "test1234567890";
        XincoCrypter instance = new XincoCrypter("xinco");
        String result = instance.encrypt(str);
        assertNotSame(str, result);
        System.out.println("decrypt");
        result = instance.decrypt(result);
        assertEquals(str, result);
    }
}
