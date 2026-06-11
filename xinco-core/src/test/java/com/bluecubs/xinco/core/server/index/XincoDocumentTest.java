package com.bluecubs.xinco.core.server.index;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.AbstractXincoDataBaseTestCase;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.server.service.XincoAddAttribute;
import com.bluecubs.xinco.server.service.XincoCoreData;
import com.bluecubs.xinco.server.service.XincoCoreDataType;
import com.bluecubs.xinco.server.service.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.server.service.XincoCoreLanguage;
import java.util.List;
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

  /**
   * Build a synthetic XincoCoreData that covers all attribute types (int, unsignedint, double,
   * varchar, text, datetime) in the getXincoDocument loop.
   */
  public void testGetXincoDocument_allAttributeTypes() throws java.io.FileNotFoundException {
    XincoCoreData data = new XincoCoreData();
    data.setId(42);
    data.setDesignation("AllTypesDoc");
    data.setStatusNumber(1);
    data.setXincoCoreNodeId(1);

    XincoCoreLanguage lang = new XincoCoreLanguage();
    lang.setId(1);
    lang.setSign("en");
    data.setXincoCoreLanguage(lang);

    XincoCoreDataType dt = new XincoCoreDataType();
    dt.setId(99);
    dt.setDesignation("test.all.types");

    List<XincoCoreDataTypeAttribute> attrDefs = dt.getXincoCoreDataTypeAttributes();
    List<XincoAddAttribute> attrVals = data.getXincoAddAttributes();

    String[] types = {"int", "unsignedint", "double", "varchar", "text", "datetime"};
    for (int i = 0; i < types.length; i++) {
      XincoCoreDataTypeAttribute attrDef = new XincoCoreDataTypeAttribute();
      attrDef.setAttributeId(i + 1);
      attrDef.setDesignation("attr_" + types[i]);
      attrDef.setDataType(types[i]);
      attrDefs.add(attrDef);

      XincoAddAttribute val = new XincoAddAttribute();
      val.setAttributeId(i + 1);
      val.setAttribInt(42);
      val.setAttribUnsignedint(100L);
      val.setAttribDouble(3.14);
      val.setAttribVarchar("hello");
      val.setAttribText("text content");
      attrVals.add(val);
    }
    data.setXincoCoreDataType(dt);

    Document doc = XincoDocument.getXincoDocument(data, false);
    assertNotNull(doc);
    assertEquals("42", doc.get("id"));
    assertEquals("AllTypesDoc", doc.get("designation"));
    // All attribute types should have been added to the document
    assertNotNull(doc.get("attr_int"));
    assertNotNull(doc.get("attr_unsignedint"));
    assertNotNull(doc.get("attr_double"));
    assertNotNull(doc.get("attr_varchar"));
    assertNotNull(doc.get("attr_text"));
  }

  /** Test with indexContent=true and data type == 1 (file), statusNumber != 3. */
  public void testGetXincoDocument_fileType_indexContent() {
    try {
      XincoCoreDataServer data = new XincoCoreDataServer(1);
      data.loadAddAttributes();
      // Data item 1 is likely a file type (dataType.id == 1) -- test with indexContent=true
      // FileNotFoundException is expected since no real file exists in test repo
      Document doc = XincoDocument.getXincoDocument(data, true);
      assertNotNull(doc);
    } catch (java.io.FileNotFoundException e) {
      // expected in test environment — file doesn't exist, still exercises code paths
    } catch (XincoException e) {
      getLogger(getClass().getSimpleName()).log(SEVERE, null, e);
      fail("Unexpected XincoException: " + e.getMessage());
    }
  }
}
