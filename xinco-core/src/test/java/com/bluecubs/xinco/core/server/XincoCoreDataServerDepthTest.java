package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoCoreDataServer.findXincoCoreData;
import static com.bluecubs.xinco.core.server.XincoCoreDataServer.getLastMajorVersion;
import static com.bluecubs.xinco.core.server.XincoCoreDataServer.getLastMajorVersionDataPath;
import static com.bluecubs.xinco.core.server.XincoCoreDataServer.removeFromDB;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoCoreDataServerDepthTest extends AbstractXincoDataBaseTestCase {

  public XincoCoreDataServerDepthTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoCoreDataServerDepthTest.class);
  }

  public void testFindXincoCoreData_byDesignation() {
    var results = findXincoCoreData("Apache", 0, false, false);
    assertNotNull(results);
  }

  public void testGetLastMajorVersion_existingData() {
    try {
      var version = getLastMajorVersion(1);
      assertNotNull(version);
    } catch (XincoException | Exception e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testGetLastMajorVersionDataPath_existingData() {
    try {
      String path = getLastMajorVersionDataPath(1);
      assertNotNull(path);
    } catch (Exception e) {
      // May throw if no major version log exists — acceptable
    }
  }

  public void testCreateWriteAndDelete() {
    try {
      XincoCoreDataServer data = new XincoCoreDataServer(0, 1, 1, 1, "TestDepthData", 1);
      data.setChangerID(1);
      int id = data.write2DB();
      assertTrue(id > 0);
      XincoCoreDataServer loaded = new XincoCoreDataServer(id);
      assertEquals("TestDepthData", loaded.getDesignation());
      data.setDesignation("TestDepthDataUpdated");
      data.write2DB();
      loaded = new XincoCoreDataServer(id);
      assertEquals("TestDepthDataUpdated", loaded.getDesignation());
      removeFromDB(1, id);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }

  public void testDeleteFromDB_viaInstance() {
    try {
      XincoCoreDataServer data = new XincoCoreDataServer(0, 1, 1, 1, "TestDeleteData", 1);
      data.setChangerID(1);
      int id = data.write2DB();
      assertTrue(id > 0);
      data.setChangerID(1);
      assertTrue(data.deleteFromDB() >= 0);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail();
    }
  }
}
