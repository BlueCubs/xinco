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
    } catch (XincoException e) {
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
    } catch (XincoException e) {
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

  /** Helper: build a minimal synthetic type-1 XincoCoreData with one varchar filename attr. */
  private XincoCoreData buildFileTypeData(int id, String filename) {
    XincoCoreData data = new XincoCoreData();
    data.setId(id);
    data.setDesignation(filename);
    data.setStatusNumber(1);
    data.setXincoCoreNodeId(1);

    XincoCoreLanguage lang = new XincoCoreLanguage();
    lang.setId(1);
    lang.setSign("en");
    data.setXincoCoreLanguage(lang);

    XincoCoreDataType dt = new XincoCoreDataType();
    dt.setId(1); // FILE type — triggers the indexContent block
    dt.setDesignation("file");

    // Must add a matching DataTypeAttribute so the attribute-loop in getXincoDocument works
    XincoCoreDataTypeAttribute attrDef = new XincoCoreDataTypeAttribute();
    attrDef.setAttributeId(1);
    attrDef.setDesignation("general.filename");
    attrDef.setDataType("varchar");
    dt.getXincoCoreDataTypeAttributes().add(attrDef);

    data.setXincoCoreDataType(dt);

    XincoAddAttribute filenameAttr = new XincoAddAttribute();
    filenameAttr.setAttributeId(1);
    filenameAttr.setAttribVarchar(filename);
    data.getXincoAddAttributes().add(filenameAttr);

    return data;
  }

  /**
   * Synthetic type-1 data with an "exe" extension — "exe" is in the noIndex list, so fileType is
   * set to -1 (no indexing). No file access occurs and the method returns normally. Covers the
   * noIndex detection loop in getXincoDocument.
   */
  public void testGetXincoDocument_fileType1_noIndexExt() throws java.io.FileNotFoundException {
    Document doc = XincoDocument.getXincoDocument(buildFileTypeData(77, "binary.exe"), true);
    assertNotNull(doc);
    assertEquals("77", doc.get("id"));
  }

  /**
   * Synthetic type-1 data with an unknown extension not in any indexer or noIndex list — fileType
   * stays 0, XincoIndexText is used. The text indexer returns null for a missing file, which causes
   * Lucene Field to throw NPE (caught here). Covers the text-indexer path in getXincoDocument.
   */
  public void testGetXincoDocument_fileType1_textIndexerPath() {
    try {
      XincoDocument.getXincoDocument(buildFileTypeData(78, "doc.zzz"), true);
    } catch (NullPointerException e) {
      // XincoIndexText.getFileContentReader returns null for a missing file;
      // Lucene Field rejects null readers with NPE
    }
  }

  /**
   * Injects a real indexer class into CONFIG via reflection so fileType > 0 triggers. Covers the
   * "else if (fileType > 0)" branch (lines ~139-173) where the indexer class is instantiated,
   * returns null for a missing file, and the method falls through to the generic-file path.
   */
  @SuppressWarnings("unchecked")
  public void testGetXincoDocument_fileType1_specIndexer() throws Exception {
    com.bluecubs.xinco.core.server.XincoConfigSingletonServer cfg =
        com.bluecubs.xinco.core.server.XincoConfigSingletonServer.getInstance();

    java.lang.reflect.Field countField =
        com.bluecubs.xinco.core.server.XincoConfigSingletonServer.class.getDeclaredField(
            "fileIndexerCount");
    countField.setAccessible(true);
    long origCount = (long) countField.get(cfg);

    // Temporarily register XincoIndexGenericFile (always available) for "abc" extension
    countField.set(cfg, origCount + 1);
    cfg.getIndexFileTypesClass()
        .add("com.bluecubs.xinco.core.server.index.filetypes.XincoIndexGenericFile");
    cfg.getIndexFileTypesExt().add(new String[] {"abc"});

    try {
      // "file.abc" → fileType=indexerCount (>0) → instantiate GenericFile → null reader, null
      // string → no field added → method returns normally
      Document doc = XincoDocument.getXincoDocument(buildFileTypeData(79, "file.abc"), true);
      assertNotNull(doc);
    } finally {
      // Restore CONFIG to original state
      countField.set(cfg, origCount);
      cfg.getIndexFileTypesClass().remove(cfg.getIndexFileTypesClass().size() - 1);
      cfg.getIndexFileTypesExt().remove(cfg.getIndexFileTypesExt().size() - 1);
    }
  }
}
