package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.*;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoDBManagerTest extends AbstractXincoDataBaseTestCase {

  public XincoDBManagerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoDBManagerTest.class);
  }

  public void testGetVersionNumber_formatIsValid() {
    String version = getVersionNumber();
    assertNotNull(version);
    assertTrue(version.matches("\\d+\\.\\d+\\.\\d+.*"));
  }

  public void testGetVersion_includesVersionNumber() {
    String version = getVersion();
    assertNotNull(version);
    assertTrue(version.startsWith(getVersionNumber()));
  }

  public void testGetDBVersion_returnsVersion() {
    try {
      String version = getDBVersion();
      assertNotNull(version);
    } catch (Exception e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testDisplayDBStatus_returnsMessage() {
    String status = displayDBStatus();
    assertNotNull(status);
    assertFalse(status.isEmpty());
  }

  public void testIsLocked_defaultFalse() {
    assertFalse(isLocked());
  }

  public void testSetLocked_togglesState() {
    assertFalse(isLocked());
    setLocked(true);
    assertTrue(isLocked());
    setLocked(false);
    assertFalse(isLocked());
  }

  public void testIsDemo_defaultFalse() {
    assertFalse(isDemo());
  }

  public void testGetDemoResetPeriod_defaultZero() {
    assertEquals(0L, getDemoResetPeriod());
  }

  public void testSetContents_writesLines() throws IOException {
    File f = Files.createTempFile("xinco-test-", ".txt").toFile();
    f.deleteOnExit();
    ArrayList<String> lines = new ArrayList<>();
    lines.add("line1");
    lines.add("line2");
    setContents(f, lines);
    String content = Files.readString(f.toPath());
    assertTrue(content.contains("line1"));
    assertTrue(content.contains("line2"));
  }

  public void testSetContents_nullFileThrows() {
    try {
      setContents(null, new ArrayList<>());
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
    } catch (IOException e) {
      fail("Wrong exception: " + e.getMessage());
    }
  }

  public void testSetContents_missingFileThrows() throws IOException {
    try {
      setContents(new File("/no/such/path/file.txt"), new ArrayList<>());
      fail("Expected exception");
    } catch (Exception e) {
      // expected — file doesn't exist
    }
  }

  public void testNamedQuery_returnsResults() {
    try {
      var results = namedQuery("XincoCoreLanguage.findAll");
      assertNotNull(results);
      assertFalse(results.isEmpty());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testCreatedQuery_withParameters() {
    try {
      var params = new java.util.HashMap<String, Object>();
      params.put("id", 1);
      var results = createdQuery(
          "select l from XincoCoreLanguage l where l.id = :id", params);
      assertNotNull(results);
      assertEquals(1, results.size());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }
}
