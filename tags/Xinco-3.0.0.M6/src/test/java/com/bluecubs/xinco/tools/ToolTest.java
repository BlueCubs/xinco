package com.bluecubs.xinco.tools;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
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
        String first = "2,1,0";
        String second = "2,01,00";
        assertTrue(Tool.compareNumberStrings(first, second, ","));
    }

    /**
     * Test of compareNumberStrings method, of class Tool.
     */
    @Test
    public void testCompareNumberStrings_3args() {
        System.out.println("compareNumberStrings");
        String first = "2,1,0";
        String second = "2,01,00";
        String separator = ",";
        assertTrue(Tool.compareNumberStrings(first, second, separator));
        separator = ".";
        assertFalse(Tool.compareNumberStrings(first, second, separator));
        second = "3.1.0";
        assertFalse(Tool.compareNumberStrings(first, second, separator));
        second = "2.1";
        assertFalse(Tool.compareNumberStrings(first, second, separator));
        second = "M";
        assertFalse(Tool.compareNumberStrings(first, second, separator));
    }

    /**
     * Test of isPortAvaialble method, of class Tool.
     */
    @Test
    public void testIsPortAvaialble() {
        ServerSocket ss = null;
        int port = 0;
        try {
            System.out.println("isPortAvaialble");
            Random random = new Random();
            while (port == 0 || !Tool.isPortAvaialble(port)) {
                port = random.nextInt(Tool.MAX_PORT_NUMBER);
            }
            System.out.println("Using port: " + port + " for test...");
            assertTrue(Tool.isPortAvaialble(port));
            ss = new ServerSocket(port);
            assertFalse(Tool.isPortAvaialble(port));
        } catch (IOException ex) {
            Logger.getLogger(ToolTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } finally {
            try {
                if (ss != null) {
                    ss.close();
                    assertTrue(Tool.isPortAvaialble(port));
                }
            } catch (IOException ex) {
                Logger.getLogger(ToolTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Test of copyFile method, of class Tool.
     */
    @Test
    public void testCopyFile() throws Exception {
        System.out.println("copyFile");
        File sourceFile = new File("demo.txt");
        sourceFile.createNewFile();
        sourceFile.deleteOnExit();
        assertTrue(sourceFile.exists());
        File destFile = new File("demo2.txt");
        assertFalse(destFile.exists());
        Tool.copyFile(sourceFile, destFile);
        destFile.deleteOnExit();
        assertTrue(destFile.exists());
    }

    /**
     * Test of isValidEmailAddress method, of class Tool.
     */
    @Test
    public void testIsValidEmailAddress() {
        System.out.println("isValidEmailAddress");
        String email = "javier.ortiz.78@gmail.com";
        assertTrue(Tool.isValidEmailAddress(email));
        email = "javier.ortiz.78@";
        assertFalse(Tool.isValidEmailAddress(email));
        email = "javier.ortiz.78 gmail.com";
        assertFalse(Tool.isValidEmailAddress(email));
        email = "javier.ortiz.78@@";
        assertFalse(Tool.isValidEmailAddress(email));
        email = "@gmail.com";
        assertFalse(Tool.isValidEmailAddress(email));
    }
}
