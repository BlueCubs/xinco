package com.bluecubs.xinco.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.nio.channels.FileChannel;
import java.util.StringTokenizer;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class Tool {

    public static int MIN_PORT_NUMBER = 1;
    public static int MAX_PORT_NUMBER = 65535;

    private Tool() {
    }

    /**
     * Compare two number strings. For example: 2.1.0 == 2.01.00
     *
     * @param first first string to compare
     * @param second second string to compare
     * @return
     */
    public static boolean compareNumberStrings(String first, String second) {
        return compareNumberStrings(first, second, ".");
    }

    /**
     * Compare two number strings. For example: 2.1.0 == 2.01.00
     *
     * @param first first string to compare
     * @param second second string to compare
     * @param separator separator of fields (i.e. for 2.1.0 is '.')
     * @return
     */
    public static boolean compareNumberStrings(String first, String second,
            String separator) {
        StringTokenizer firstST = new StringTokenizer(first, separator);
        StringTokenizer secondST = new StringTokenizer(second, separator);
        if (firstST.countTokens() != secondST.countTokens()) {
            //Different amount of fields, not equal. (i.e. 2.1 and 2.1.1
            return false;
        } else {
            try {
                while (firstST.hasMoreTokens()) {
                    int firstInt = Integer.parseInt(firstST.nextToken());
                    int secondInt = Integer.parseInt(secondST.nextToken());
                    //Both numbers let's continue
                    if (firstInt != secondInt) {
                        return false;
                    }
                }
                //Everything the same
            } catch (java.lang.NumberFormatException e) {
                //Is not a number
                return false;
            }
        }
        return true;
    }

    public static boolean isPortAvaialble(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /*
                     * should not be thrown
                     */
                }
            }
        }

        return false;
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
