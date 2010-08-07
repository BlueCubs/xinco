package com.bluecubs.xinco.tools;

import com.bluecubs.xinco.core.XincoException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Replaces the MD5 encryption available in MySQL and makes it universal for all
 * databases.
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public class MD5 {

    private static String md5val = "";
    private static MessageDigest algorithm = null;

    public static String encrypt(String text) throws XincoException {
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            throw new XincoException("Cannot find digest algorithm");
        }
        byte[] defaultBytes = text.getBytes();
        algorithm.reset();
        algorithm.update(defaultBytes);
        byte messageDigest[] = algorithm.digest();
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xFF & messageDigest[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        md5val = hexString.toString();
        return md5val;
    }
}
