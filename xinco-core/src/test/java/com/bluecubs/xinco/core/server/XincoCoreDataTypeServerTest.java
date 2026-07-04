package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoCoreDataTypeServer.deleteFromDB;
import static com.bluecubs.xinco.core.server.XincoCoreDataTypeServer.getXincoCoreDataTypes;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class XincoCoreDataTypeServerTest extends AbstractXincoDataBaseTestCase {

  public XincoCoreDataTypeServerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(XincoCoreDataTypeServerTest.class);
    return suite;
  }

  /** Test of getXincoCoreDataTypes method, of class XincoCoreDataTypeServer. */
  public void testWrite2DB() {
    try {
      XincoCoreDataTypeServer instance =
          new XincoCoreDataTypeServer(0, "Test", "Test desc", new ArrayList<>());
      assertTrue(instance.write2DB() > 0);
      deleteFromDB(instance);
    } catch (XincoException ex) {
      getLogger(XincoCoreGroupServerTest.class.getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }

  /** Test of getXincoCoreDataTypes method, of class XincoCoreDataTypeServer. */
  public void testGetXincoCoreDataTypes() {
    assertTrue(getXincoCoreDataTypes().size() > 0);
  }

  public void testGetXincoCoreDataType_byId() {
    XincoCoreDataTypeServer dt = XincoCoreDataTypeServer.getXincoCoreDataType(1);
    assertNotNull(dt);
    assertTrue(dt.getId() == 1);
  }

  public void testGetXincoCoreDataType_notFound() {
    XincoCoreDataTypeServer dt = XincoCoreDataTypeServer.getXincoCoreDataType(Integer.MAX_VALUE);
    assertNull(dt);
  }

  public void testDataTypeAttributeServer_loadFromDB() {
    try {
      // Data type 1, attribute 1 always exists in seed data
      XincoCoreDataTypeAttributeServer attr = new XincoCoreDataTypeAttributeServer(1, 1);
      assertNotNull(attr);
      assertEquals(1, attr.getXincoCoreDataTypeId());
      assertEquals(1, attr.getAttributeId());
    } catch (XincoException ex) {
      getLogger(XincoCoreDataTypeServerTest.class.getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }

  public void testDataTypeAttributeServer_5argConstructor() {
    try {
      XincoCoreDataTypeAttributeServer attr =
          new XincoCoreDataTypeAttributeServer(1, 99, "TestAttr5", "varchar", 200);
      assertNotNull(attr);
      assertEquals(1, attr.getXincoCoreDataTypeId());
      assertEquals(99, attr.getAttributeId());
      assertEquals("TestAttr5", attr.getDesignation());
    } catch (XincoException ex) {
      getLogger(XincoCoreDataTypeServerTest.class.getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }

  public void testDataTypeAttributeServer_write2DBAndDelete() {
    try {
      XincoCoreDataTypeServer dt =
          new XincoCoreDataTypeServer(0, "TestDTAttrWrite", "desc", new ArrayList<>());
      int dtId = dt.write2DB();
      assertTrue(dtId > 0);

      // Attribute for the newly created data type (attributeId=1, first for this type)
      XincoCoreDataTypeAttributeServer attr =
          new XincoCoreDataTypeAttributeServer(dtId, 1, "TestAttr", "varchar", 100);
      int id = attr.write2DB();
      assertTrue(id > 0);

      XincoCoreDataTypeAttributeServer.deleteFromDB(attr, 1);
      deleteFromDB(dt);
    } catch (XincoException ex) {
      getLogger(XincoCoreDataTypeServerTest.class.getSimpleName()).log(SEVERE, null, ex);
      fail();
    }
  }
}
