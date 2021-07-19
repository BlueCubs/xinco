package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoCoreGroupServer.deleteFromDB;
import static com.bluecubs.xinco.core.server.XincoCoreGroupServer.getXincoCoreGroups;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import junit.framework.Test;
import junit.framework.TestSuite;

/** @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com */
public class XincoCoreGroupServerTest extends AbstractXincoDataBaseTestCase {

  public XincoCoreGroupServerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(XincoCoreGroupServerTest.class);
    return suite;
  }

  /** Test of write2DB method, of class XincoCoreGroupServer. */
  public void testWrite2DB() {
    try {
      XincoCoreGroupServer instance = new XincoCoreGroupServer(0, "Test", 1);
      assertTrue(instance.write2DB() > 0);
      deleteFromDB(instance);
    } catch (XincoException ex) {
      getLogger(XincoCoreGroupServerTest.class.getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }

  /** Test of getXincoCoreGroups method, of class XincoCoreGroupServer. */
  public void testGetXincoCoreGroups() {
    assertTrue(getXincoCoreGroups().size() > 0);
  }
}
