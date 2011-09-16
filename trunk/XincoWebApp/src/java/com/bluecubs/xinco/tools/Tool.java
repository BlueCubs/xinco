package com.bluecubs.xinco.tools;

import java.util.StringTokenizer;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class Tool {

    private Tool() {
    }

    /**
     * Compare two number strings.
     * For example:
     * 2.1.0 == 2.01.00
     * 
     * @param first first string to compare
     * @param second second string to compare
     * @return 
     */
    public static boolean compareNumberStrings(String first, String second) {
        return compareNumberStrings(first, second, ".");
    }

    /**
     * Compare two number strings.
     * For example:
     * 2.1.0 == 2.01.00
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
}
