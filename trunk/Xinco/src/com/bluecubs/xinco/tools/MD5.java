package com.bluecubs.xinco.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Replaces the MD5 encryption available in MySQL and makes it universal for all
 * databases.
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public class MD5 {

    private static String md5val = "";
    private static MessageDigest algorithm = null;

    public static void main(String[] args) {
        if (args.length != 0) {
            for (int i = 0; i < args.length; i++) {
                try {
                    System.out.println("MD5 (" + args[i] + ") = " + encrypt(args[i]));
                } catch (Exception ex) {
                    Logger.getLogger(MD5.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            try {
                System.out.println("MD5 (admin) = " + encrypt("admin"));
            } catch (Exception ex) {
                Logger.getLogger(MD5.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static String encrypt(String text) throws Exception {
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            throw new Exception("Cannot find digest algorithm");
        }
        byte[] defaultBytes = text.getBytes();
        algorithm.reset();
        algorithm.update(defaultBytes);
        byte messageDigest[] = algorithm.digest();
        StringBuffer hexString = new StringBuffer();

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
