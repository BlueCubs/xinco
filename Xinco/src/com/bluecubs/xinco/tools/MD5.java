package com.bluecubs.xinco.tools;

import static java.lang.Integer.toHexString;
import static java.lang.System.out;
import static java.security.MessageDigest.getInstance;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz
 */
public class MD5 {

    private static String md5val = "";
    private static MessageDigest algorithm = null;

    public static void main(String[] args) {
        if (args.length != 0) {
      for (String arg : args) {
        try {
          out.println("MD5 (" + arg + ") = " + encrypt(arg));
        }
        catch (XincoException ex) {
          getLogger(MD5.class.getName()).log(SEVERE, null, ex);
        }
      }
        } else {
            try {
                out.println("MD5 (admin) = " + encrypt("admin"));
            } catch (XincoException ex) {
                getLogger(MD5.class.getName()).log(SEVERE, null, ex);
            }
        }
    }

    public static String encrypt(String text) throws XincoException {
        try {
            algorithm = getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            throw new XincoException("Cannot find digest algorithm");
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
