package com.bluecubs.xinco.tools;

import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class XincoFileIconManagerTest {

  public XincoFileIconManagerTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /** Test null extension — returns null without filesystem access. */
  @Test
  public void testGetIcon_null() {
    XincoFileIconManager instance = new XincoFileIconManager();
    assertNull(instance.getIcon(null));
  }

  /** Test extension shorter than 3 chars — returns null without filesystem access. */
  @Test
  public void testGetIcon_shortExtension() {
    XincoFileIconManager instance = new XincoFileIconManager();
    assertNull(instance.getIcon("tx"));
  }

  /** Test extension with dot-only prefix shorter than 3 chars after stripping dot. */
  @Test
  public void testGetIcon_dotShortExtension() {
    XincoFileIconManager instance = new XincoFileIconManager();
    assertNull(instance.getIcon(".tx"));
  }
}
