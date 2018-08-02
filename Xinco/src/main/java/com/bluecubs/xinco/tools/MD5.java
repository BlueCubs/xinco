package com.bluecubs.xinco.tools;

import com.bluecubs.xinco.core.XincoException;
import static java.lang.Integer.toHexString;
import java.security.MessageDigest;
import static java.security.MessageDigest.getInstance;
import java.security.NoSuchAlgorithmException;

/**
 * Replaces the MD5 encryption available in MySQL and makes it universal for all
 * databases.
 *
 * @author Javier A. Ortiz javier.ortiz.78@gmail.com
 */
public final class MD5 {

    private static String md5val = "";
    private static MessageDigest algorithm = null;

    private MD5() {
    }

    public static String encrypt(String text) throws XincoException {
        try {
            algorithm = getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            throw new XincoException(nsae);
        }
        byte[] defaultBytes = text.getBytes();
        algorithm.reset();
        algorithm.update(defaultBytes);
        byte messageDigest[] = algorithm.digest();
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < messageDigest.length; i++) {
            String hex = toHexString(0xFF & messageDigest[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        md5val = hexString.toString();
        return md5val;
    }
}
