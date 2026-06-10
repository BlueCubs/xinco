package com.bluecubs.xinco.core.server;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XincoIdServerTest extends AbstractXincoDataBaseTestCase {

  public XincoIdServerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoIdServerTest.class);
  }

  public void testGetXincoId_returnsExistingTableEntry() {
    try {
      XincoIdServer id = XincoIdServer.getXincoId("xinco_core_data");
      assertNotNull(id);
      assertNotNull(id.getTablename());
      assertEquals("xinco_core_data", id.getTablename());
      assertTrue(id.getLastId() >= 0);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail("getXincoId threw: " + e.getMessage());
    }
  }

  public void testGetIds_returnsNonEmptyList() {
    try {
      List<XincoIdServer> ids = XincoIdServer.getIds();
      assertNotNull(ids);
      assertFalse(ids.isEmpty());
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail("getIds threw: " + e.getMessage());
    }
  }

  public void testGetNextId_incrementsLastId() {
    try {
      XincoIdServer before = XincoIdServer.getXincoId("xinco_core_language");
      int prevLast = before.getLastId();
      int next = XincoIdServer.getNextId("xinco_core_language");
      assertEquals(prevLast + 1, next);
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail("getNextId threw: " + e.getMessage());
    }
  }

  public void testGetXincoId_missingTable_throwsException() {
    try {
      XincoIdServer.getXincoId("NonExistentTable_99999");
      fail("Expected XincoException for unknown table");
    } catch (XincoException e) {
      assertNotNull(e.getMessage());
    }
  }

  public void testConstructor_withTableAndLastId_setsFields() {
    XincoIdServer server = new XincoIdServer("MyTable", 42);
    assertEquals("MyTable", server.getTablename());
    assertTrue(server.getLastId() == 42);
    assertTrue(server.getId() == 0);
  }
}
