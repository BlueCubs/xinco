package com.bluecubs.xinco.tools;

import static com.bluecubs.xinco.tools.MD5.encrypt;
import static java.lang.System.out;
import static org.junit.Assert.assertNotSame;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class MD5Test {

  public MD5Test() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /** Test of encrypt method, of class MD5. */
  @Test
  public void testEncrypt() {
    out.println("encrypt");
    String text = "test";
    String result = encrypt(text);
    assertNotSame(text, result);
  }
}
