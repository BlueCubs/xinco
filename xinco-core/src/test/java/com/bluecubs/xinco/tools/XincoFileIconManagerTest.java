package com.bluecubs.xinco.tools;

import static org.junit.Assert.assertNull;

import java.io.IOException;
import org.junit.*;

/** @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com */
public class XincoFileIconManagerTest {

  public XincoFileIconManagerTest() {}

  @BeforeClass
  public static void setUpClass() throws Exception {}

  @AfterClass
  public static void tearDownClass() throws Exception {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /** Test null extension — returns null without filesystem access. */
  @Test
  public void testGetIcon_null() throws IOException {
    XincoFileIconManager instance = new XincoFileIconManager();
    assertNull(instance.getIcon(null));
  }

  /** Test extension shorter than 3 chars — returns null without filesystem access. */
  @Test
  public void testGetIcon_shortExtension() throws IOException {
    XincoFileIconManager instance = new XincoFileIconManager();
    assertNull(instance.getIcon("tx"));
  }

  /** Test extension with dot-only prefix shorter than 3 chars after stripping dot. */
  @Test
  public void testGetIcon_dotShortExtension() throws IOException {
    XincoFileIconManager instance = new XincoFileIconManager();
    assertNull(instance.getIcon(".tx"));
  }
}
