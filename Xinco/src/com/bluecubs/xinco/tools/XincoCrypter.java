/**
 *Copyright 2011 blueCubs.com
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
 * Name:            XincoCrypter
 *
 * Description:     Xinco Crypter
 *
 * Original Author: Javier A. Ortiz
 * Date:            March 6, 2006, 3:43 PM
 *
 * Modifications:
 *
 * Who?             When?             What?
 * 
 *************************************************************
 */
package com.bluecubs.xinco.tools;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 *
 * @author Javier A. Ortiz
 */
/**
 * Creates a new instance of XincoCrypter
 */
public class XincoCrypter {

    /*
     * Cipher
     */
    private Cipher ecipher;
    /*
     * Decipher
     */
    private Cipher dcipher;
    /*
     * 8-byte Salt
     */
    private final byte[] salt = {
        (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
        (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };
    /**
     * Iteration count
     */
    int iterationCount = 19;

    /**
     * Constructor
     * @param passPhrase
     */
    public XincoCrypter(final String passPhrase) {
        try {
            // Create the key
            final KeySpec keySpec =
                    new PBEKeySpec(passPhrase.toCharArray(),
                    salt, iterationCount);
            final SecretKey key = SecretKeyFactory.getInstance(
                    "PBEWithMD5AndDES").generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            // Prepare the parameter to the ciphers
            final AlgorithmParameterSpec paramSpec = new PBEParameterSpec(
                    salt, iterationCount);

            // Create the ciphers
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (java.security.InvalidAlgorithmParameterException e) {
            Logger.getLogger(XincoCrypter.class.getName()).log(
                    Level.SEVERE, null, e);
        } catch (java.security.spec.InvalidKeySpecException e) {
            Logger.getLogger(XincoCrypter.class.getName()).log(
                    Level.SEVERE, null, e);
        } catch (javax.crypto.NoSuchPaddingException e) {
            Logger.getLogger(XincoCrypter.class.getName()).log(
                    Level.SEVERE, null, e);
        } catch (java.security.NoSuchAlgorithmException e) {
            Logger.getLogger(XincoCrypter.class.getName()).log(
                    Level.SEVERE, null, e);
        } catch (java.security.InvalidKeyException e) {
            Logger.getLogger(XincoCrypter.class.getName()).log(
                    Level.SEVERE, null, e);
        }
    }

    /**
     * Encrypt string
     * @param str
     * @return String
     */
    public String encrypt(String str) {
        String result = null;
        try {
            // Encode the string into bytes using utf-8
            final byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            final byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            result = Base64.encode(enc).toString();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(XincoCrypter.class.getSimpleName()).log(
                    Level.SEVERE, null, ex);
        } catch (javax.crypto.BadPaddingException e) {
            Logger.getLogger(XincoCrypter.class.getName()).log(
                    Level.SEVERE, null, e);
        } catch (IllegalBlockSizeException e) {
            Logger.getLogger(XincoCrypter.class.getName()).log(
                    Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * Decrypt
     * @param str
     * @return Decrypted screen
     */
    public final String decrypt(final String str) {
        String result = null;
        try {
            // Decode base64 to get bytes
            final byte[] dec = Base64.decode(str.getBytes());

            // Decrypt
            final byte[] utf8 = dcipher.doFinal(dec);
            // Decode using utf-8
            result = new String(utf8, "UTF8");
        } catch (Base64DecodingException ex) {
            Logger.getLogger(XincoCrypter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.crypto.BadPaddingException e) {
            Logger.getLogger(XincoCrypter.class.getName()).log(
                    Level.SEVERE, null, e);
        } catch (IllegalBlockSizeException e) {
            Logger.getLogger(XincoCrypter.class.getName()).log(
                    Level.SEVERE, null, e);
        } catch (UnsupportedEncodingException e) {
            Logger.getLogger(XincoCrypter.class.getName()).log(
                    Level.SEVERE, null, e);
        } catch (java.io.IOException e) {
            Logger.getLogger(XincoCrypter.class.getName()).log(
                    Level.SEVERE, null, e);
        }
        return result;
    }
}
