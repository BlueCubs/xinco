package com.bluecubs.xinco.core.server.index;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.lucene.document.Document;

public class XincoDocumentTest extends AbstractXincoDataBaseTestCase {

  public XincoDocumentTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(XincoDocumentTest.class);
  }

  public void testGetXincoDocument_noContentIndex_returnsDocWithFields() {
    try {
      XincoCoreDataServer data = new XincoCoreDataServer(1);
      data.loadAddAttributes();
      Document doc = XincoDocument.getXincoDocument(data, false);
      assertNotNull(doc);
      assertNotNull(doc.get("id"));
      assertEquals(String.valueOf(data.getId()), doc.get("id"));
      assertNotNull(doc.get("designation"));
      assertEquals(data.getDesignation(), doc.get("designation"));
      assertNotNull(doc.get("language"));
    } catch (XincoException | java.io.FileNotFoundException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  public void testGetXincoDocument_urlDataType_noContentIndex() {
    try {
      // Item 2 is a URL type (data type 3), status 1 (open)
      XincoCoreDataServer data = new XincoCoreDataServer(2);
      data.loadAddAttributes();
      Document doc = XincoDocument.getXincoDocument(data, false);
      assertNotNull(doc);
      assertNotNull(doc.get("id"));
      assertNotNull(doc.get("designation"));
    } catch (XincoException | java.io.FileNotFoundException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail("Unexpected exception: " + e.getMessage());
    }
  }
}
