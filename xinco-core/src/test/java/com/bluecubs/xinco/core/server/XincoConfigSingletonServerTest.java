package com.bluecubs.xinco.core.server;

import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoConfigSingletonServerTest extends AbstractXincoDataBaseTestCase {

  public XincoConfigSingletonServerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoConfigSingletonServerTest.class);
  }

  public void testGetInstance_returnsSameInstance() {
    XincoConfigSingletonServer a = XincoConfigSingletonServer.getInstance();
    XincoConfigSingletonServer b = XincoConfigSingletonServer.getInstance();
    assertNotNull(a);
    assertSame(a, b);
  }

  public void testLoadSettings_completesWithoutException() {
    try {
      XincoConfigSingletonServer cfg = XincoConfigSingletonServer.getInstance();
      cfg.loadSettings();
      assertNotNull(cfg.fileRepositoryPath);
      assertNotNull(cfg.fileIndexPath);
      assertNotNull(cfg.fileArchivePath);
      assertTrue(cfg.getMaxSearchResult() > 0);
    } catch (Exception e) {
      fail("loadSettings threw unexpected exception: " + e.getMessage());
    }
  }

  public void testGetters_afterLoadSettings() {
    XincoConfigSingletonServer cfg = XincoConfigSingletonServer.getInstance();
    cfg.loadSettings();
    assertNotNull(cfg.getIndexFileTypesClass());
    assertNotNull(cfg.getIndexFileTypesExt());
    assertNotNull(cfg.getIndexNoIndex());
    assertTrue(cfg.getFileIndexerCount() >= 0);
    assertTrue(cfg.getFileIndexOptimizerPeriod() > 0);
  }
}
