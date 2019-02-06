package com.bluecubs.xinco.tools;

import static com.bluecubs.xinco.tools.Tool.MAX_PORT_NUMBER;
import static com.bluecubs.xinco.tools.Tool.compareNumberStrings;
import static com.bluecubs.xinco.tools.Tool.copyFile;
import static com.bluecubs.xinco.tools.Tool.isPortAvaialble;
import static com.bluecubs.xinco.tools.Tool.isValidEmailAddress;
import java.io.File;
import java.io.IOException;
import static java.lang.System.out;
import java.net.ServerSocket;
import java.util.Random;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com
 */
public class ToolTest {

    public ToolTest() {
    }

    /**
     * Test of compareNumberStrings method, of class Tool.
     */
    @Test
    public void testCompareNumberStrings() {
        String first = "2.1.0";
        String second = "2.01.00";
        assertTrue(compareNumberStrings(first, second));
        second = "3.1.0";
        assertFalse(compareNumberStrings(first, second));
        second = "2.1";
        assertFalse(compareNumberStrings(first, second));
        second = "M";
        assertFalse(compareNumberStrings(first, second));
    }

    /**
     * Test of compareNumberStrings method, of class Tool.
     */
    @Test
    public void testCompareNumberStrings3() {
        String first = "2,1,0";
        String second = "2,01,00";
        assertTrue(compareNumberStrings(first, second, ","));
    }

    /**
     * Test of compareNumberStrings method, of class Tool.
     */
    @Test
    public void testCompareNumberStrings_3args() {
        out.println("compareNumberStrings");
        String first = "2,1,0";
        String second = "2,01,00";
        String separator = ",";
        assertTrue(compareNumberStrings(first, second, separator));
        separator = ".";
        assertFalse(compareNumberStrings(first, second, separator));
        second = "3.1.0";
        assertFalse(compareNumberStrings(first, second, separator));
        second = "2.1";
        assertFalse(compareNumberStrings(first, second, separator));
        second = "M";
        assertFalse(compareNumberStrings(first, second, separator));
    }

    /**
     * Test of isPortAvaialble method, of class Tool.
     */
    @Test
    public void testIsPortAvaialble() {
        ServerSocket ss = null;
        int port = 0;
        try {
            out.println("isPortAvaialble");
            Random random = new Random();
            while (port == 0 || !isPortAvaialble(port)) {
                port = random.nextInt(MAX_PORT_NUMBER);
            }
            out.println("Using port: " + port + " for test...");
            assertTrue(isPortAvaialble(port));
            ss = new ServerSocket(port);
            assertFalse(isPortAvaialble(port));
        } catch (IOException ex) {
            getLogger(ToolTest.class.getName()).log(SEVERE, null, ex);
            fail();
        } finally {
            try {
                if (ss != null) {
                    ss.close();
                    assertTrue(isPortAvaialble(port));
                }
            } catch (IOException ex) {
                getLogger(ToolTest.class.getName()).log(SEVERE, null, ex);
            }
        }
    }

    /**
     * Test of copyFile method, of class Tool.
     */
    @Test
    public void testCopyFile() throws Exception {
        out.println("copyFile");
        File sourceFile = new File("demo.txt");
        sourceFile.createNewFile();
        sourceFile.deleteOnExit();
        assertTrue(sourceFile.exists());
        File destFile = new File("demo2.txt");
        assertFalse(destFile.exists());
        copyFile(sourceFile, destFile);
        destFile.deleteOnExit();
        assertTrue(destFile.exists());
    }

    /**
     * Test of isValidEmailAddress method, of class Tool.
     */
    @Test
    public void testIsValidEmailAddress() {
        out.println("isValidEmailAddress");
        String email = "javier.ortiz.78@gmail.com";
        assertTrue(isValidEmailAddress(email));
        email = "javier.ortiz.78@";
        assertFalse(isValidEmailAddress(email));
        email = "javier.ortiz.78 gmail.com";
        assertFalse(isValidEmailAddress(email));
        email = "javier.ortiz.78@@";
        assertFalse(isValidEmailAddress(email));
        email = "@gmail.com";
        assertFalse(isValidEmailAddress(email));
    }
}
