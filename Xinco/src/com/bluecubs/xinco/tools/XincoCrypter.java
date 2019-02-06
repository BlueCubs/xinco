/**
 *Copyright 2010 blueCubs.com
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

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;
import static org.apache.axis.encoding.Base64.decode;
import static org.apache.axis.encoding.Base64.encode;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

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
public class XincoCrypter
{

  private Cipher ecipher;
  private Cipher dcipher;
  // 8-byte Salt
  byte[] salt =
  {
    (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
    (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
  };
  // Iteration count
  int iterationCount = 19;

  /**
   * Constructor
   *
   * @param passPhrase
   */
  public XincoCrypter(String passPhrase)
  {
    try
    {
      // Create the key
      KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
      SecretKey key = SecretKeyFactory.getInstance(
              "PBEWithMD5AndDES").generateSecret(keySpec);
      ecipher = Cipher.getInstance(key.getAlgorithm());
      dcipher = Cipher.getInstance(key.getAlgorithm());

      // Prepare the parameter to the ciphers
      AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

      // Create the ciphers
      ecipher.init(ENCRYPT_MODE, key, paramSpec);
      dcipher.init(DECRYPT_MODE, key, paramSpec);
    }
    catch (java.security.InvalidAlgorithmParameterException e)
    {
      e.printStackTrace(System.err);
    }
    catch (java.security.spec.InvalidKeySpecException e)
    {
      e.printStackTrace(System.err);
    }
    catch (javax.crypto.NoSuchPaddingException e)
    {
      e.printStackTrace(System.err);
    }
    catch (java.security.NoSuchAlgorithmException e)
    {
      e.printStackTrace(System.err);
    }
    catch (java.security.InvalidKeyException e)
    {
      e.printStackTrace(System.err);
    }
  }

  /**
   * Encript string
   *
   * @param str
   * @return String
   */
  public String encrypt(String str)
  {
    try
    {
      // Encode the string into bytes using utf-8
      byte[] utf8 = str.getBytes("UTF8");

      // Encrypt
      byte[] enc = ecipher.doFinal(utf8);

      // Encode bytes to base64 to get a string
      return encode(enc);
    }
    catch (javax.crypto.BadPaddingException e)
    {
      e.printStackTrace(System.err);
    }
    catch (IllegalBlockSizeException e)
    {
      e.printStackTrace(System.err);
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace(System.err);
    }
    catch (java.io.IOException e)
    {
      e.printStackTrace(System.err);
    }
    return null;
  }

  /**
   * Decrypt
   *
   * @param str
   * @return
   */
  public String decrypt(String str)
  {
    try
    {
      // Decode base64 to get bytes
      byte[] dec = decode(str);

      // Decrypt
      byte[] utf8 = dcipher.doFinal(dec);
      // Decode using utf-8
      return new String(utf8, "UTF8");
    }
    catch (javax.crypto.BadPaddingException e)
    {
      e.printStackTrace();
    }
    catch (IllegalBlockSizeException e)
    {
      e.printStackTrace();
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    catch (java.io.IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }
}
