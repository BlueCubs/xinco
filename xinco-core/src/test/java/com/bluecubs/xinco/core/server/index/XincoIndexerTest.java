package com.bluecubs.xinco.core.server.index;

import static com.bluecubs.xinco.core.server.XincoDBManager.CONFIG;
import static com.bluecubs.xinco.core.server.index.XincoIndexer.findXincoCoreData;
import static com.bluecubs.xinco.core.server.index.XincoIndexer.indexXincoCoreData;
import static com.bluecubs.xinco.core.server.index.XincoIndexer.optimizeIndex;
import static com.bluecubs.xinco.core.server.index.XincoIndexer.removeXincoCoreData;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.server.service.XincoCoreData;
import java.io.File;
import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoIndexerTest extends AbstractXincoDataBaseTestCase {

  public XincoIndexerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoIndexerTest.class);
  }

  public void testOptimizeIndex_existingDir_returnsTrue() {
    assertTrue("fileIndexPath must be set", CONFIG.fileIndexPath != null);
    new File(CONFIG.fileIndexPath).mkdirs();
    assertTrue(optimizeIndex());
  }

  public void testOptimizeIndex_noSuchDir_returnsFalse() {
    String savedPath = CONFIG.fileIndexPath;
    try {
      CONFIG.fileIndexPath = "/no/such/path/that/exists/xinco-index-test/";
      assertFalse(optimizeIndex());
    } finally {
      CONFIG.fileIndexPath = savedPath;
    }
  }

  public void testRemoveXincoCoreData_noDirExists_returnsTrue() {
    String savedPath = CONFIG.fileIndexPath;
    try {
      CONFIG.fileIndexPath = "/no/such/path/that/exists/xinco-index-test/";
      XincoCoreData d = new XincoCoreData();
      d.setId(9999);
      assertTrue(removeXincoCoreData(d));
    } finally {
      CONFIG.fileIndexPath = savedPath;
    }
  }

  public void testRemoveXincoCoreData_existingIndex_returnsTrue() {
    new File(CONFIG.fileIndexPath).mkdirs();
    optimizeIndex();
    XincoCoreData d = new XincoCoreData();
    d.setId(9999);
    assertTrue(removeXincoCoreData(d));
  }

  public void testFindXincoCoreData_emptyQuery_returnsEmptyList() {
    new File(CONFIG.fileIndexPath).mkdirs();
    optimizeIndex();
    ArrayList result = findXincoCoreData("designation:xincoRoot", 0);
    assertNotNull(result);
  }

  public void testIndexXincoCoreData_realDataItem() {
    try {
      new File(CONFIG.fileIndexPath).mkdirs();
      optimizeIndex();
      XincoCoreDataServer data = new XincoCoreDataServer(1);
      boolean indexed = indexXincoCoreData(data, false);
      assertTrue(indexed);
      assertTrue(removeXincoCoreData(data));
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail("Unexpected exception: " + e.getMessage());
    }
  }
}
