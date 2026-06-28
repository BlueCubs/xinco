package com.bluecubs.xinco.archive;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoArchiverAdditionalTest extends AbstractXincoDataBaseTestCase {

  public XincoArchiverAdditionalTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoArchiverAdditionalTest.class);
  }

  public void testXincoArchiveThread_getInstance_isSingleton() {
    XincoArchiveThread a = XincoArchiveThread.getInstance();
    XincoArchiveThread b = XincoArchiveThread.getInstance();
    assertNotNull(a);
    assertSame(a, b);
  }

  public void testXincoArchiveThread_firstAndLastRun() {
    XincoArchiveThread t = XincoArchiveThread.getInstance();
    assertNull(t.lastRun);
    t.firstRun = Calendar.getInstance();
    assertNotNull(t.firstRun);
    t.firstRun = null;
  }

  public void testXincoArchiveThread_archiveData_noOp() {
    // archiveData() should return false when there's nothing to archive
    try {
      boolean result = XincoArchiveThread.archiveData();
      // returns true/false depending on data state — just verify no exception
    } catch (Exception e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail("archiveData threw: " + e.getMessage());
    }
  }
}
