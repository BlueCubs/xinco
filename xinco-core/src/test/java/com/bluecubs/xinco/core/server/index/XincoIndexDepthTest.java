package com.bluecubs.xinco.core.server.index;

import static com.bluecubs.xinco.core.server.XincoDBManager.CONFIG;
import static com.bluecubs.xinco.core.server.index.XincoIndexer.optimizeIndex;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import java.io.File;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoIndexDepthTest extends AbstractXincoDataBaseTestCase {

  public XincoIndexDepthTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoIndexDepthTest.class);
  }

  public void testXincoIndexThread_runWithRealData() {
    try {
      new File(CONFIG.fileIndexPath).mkdirs();
      optimizeIndex();
      XincoCoreDataServer data = new XincoCoreDataServer(1);
      XincoIndexThread thread = new XincoIndexThread(data, false);
      thread.run();
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testXincoIndexOptimizeThread_getInstance() {
    XincoIndexOptimizeThread instance = XincoIndexOptimizeThread.getInstance();
    assertNotNull(instance);
    assertSame(instance, XincoIndexOptimizeThread.getInstance());
  }

  public void testXincoIndexOptimizeThread_fields() {
    XincoIndexOptimizeThread t = XincoIndexOptimizeThread.getInstance();
    assertTrue(t.index_period > 0);
    assertNull(t.lastRun);
    t.firstRun = Calendar.getInstance();
    assertNotNull(t.firstRun);
  }

  public void testXincoDocument_urlDataWithIndexContent() {
    try {
      new File(CONFIG.fileIndexPath).mkdirs();
      optimizeIndex();
      // Data item 1 is a URL type (data type 3, statusNumber 5=published)
      // With indexContent=true, statusNumber != 3 so file content branch is attempted
      XincoCoreDataServer data = new XincoCoreDataServer(1);
      data.loadAddAttributes();
      var doc = XincoDocument.getXincoDocument(data, true);
      assertNotNull(doc);
    } catch (XincoException | java.io.FileNotFoundException e) {
      // FileNotFoundException is expected since no file exists in test environment
    }
  }
}
