package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoCoreDataServer.getXincoCoreDataPath;
import static com.bluecubs.xinco.core.server.XincoCoreDataServer.isArchived;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoCoreDataServerAdditionalTest extends AbstractXincoDataBaseTestCase {

  public XincoCoreDataServerAdditionalTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoCoreDataServerAdditionalTest.class);
  }

  public void testGetXincoCoreDataPath() {
    // ID is zero-padded to 10 chars, first 7 used as nested dir segments
    String path = getXincoCoreDataPath("/repo/", 42, "doc.pdf");
    assertTrue(path.contains("doc.pdf"));
    assertTrue(path.startsWith("/repo/"));
  }

  public void testGetXincoCoreDataPath_containsFilename() {
    String path = getXincoCoreDataPath("/tmp/xinco/", 1, "report.xlsx");
    assertTrue(path.endsWith("report.xlsx"));
  }

  public void testIsArchivedById_knownDataNotArchived() {
    try {
      assertFalse(isArchived(1));
    } catch (Exception e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testIsArchivedByServer_knownDataNotArchived() {
    try {
      XincoCoreDataServer data = new XincoCoreDataServer(1);
      assertFalse(isArchived(data));
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testGetAttribute_returnsNullForMissingId() {
    try {
      XincoCoreDataServer data = new XincoCoreDataServer(1);
      data.loadAddAttributes();
      assertNull(data.getAttribute(9999));
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testGetOwnerID() {
    try {
      XincoCoreDataServer data = new XincoCoreDataServer(1);
      int owner = XincoCoreDataServer.getOwnerID(data);
      assertTrue(owner >= 0);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testGetCurrentVersion() {
    try {
      XincoCoreDataServer data = new XincoCoreDataServer(1);
      data.write2DB();
      var version = XincoCoreDataServer.getCurrentVersion(data.getId());
      assertNotNull(version);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }
}
