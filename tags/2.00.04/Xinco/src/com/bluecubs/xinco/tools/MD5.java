/**
 *Copyright 2009 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community in exchange for free software!
 * More information on: http://www.bluecubs.org
 *************************************************************
 *
 * Name:            MD5
 *
 * Description:     MD5 algorithm
 *
 * Original Author: Javier A. Ortiz
 * Date:            2008
 *
 * Modifications:
 *
 * Who?             When?             What?
 * 
 *************************************************************
 */
package com.bluecubs.xinco.tools;

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
            for (int i = 0; i < args.length; i++) {
                try {
                    System.out.println("MD5 (" + args[i] + ") = " + encrypt(args[i]));
                } catch (XincoException ex) {
                    Logger.getLogger(MD5.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            try {
                System.out.println("MD5 (admin) = " + encrypt("admin"));
            } catch (XincoException ex) {
                Logger.getLogger(MD5.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

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
