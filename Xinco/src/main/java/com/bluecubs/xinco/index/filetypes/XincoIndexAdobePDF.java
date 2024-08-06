/**
 * Copyright 2010 blueCubs.com
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * <p>************************************************************ This project supports the
 * blueCubs vision of giving back to the community in exchange for free software! More information
 * on: http://www.bluecubs.org ************************************************************
 *
 * <p>Name: XincoIndexText
 *
 * <p>Description: indexing Adobe PDF files
 *
 * <p>Original Author: Alexander Manes Date: 2005/02/05
 *
 * <p>Modifications:
 *
 * <p>Who? When? What? - - -
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.index.filetypes;

import java.io.File;
import java.io.Reader;
import org.apache.lucene.document.Document;
import org.apache.pdfbox.lucene.LucenePDFDocument;

public class XincoIndexAdobePDF implements XincoIndexFileType {

  public XincoIndexAdobePDF() {
    super();
  }

  public Reader getFileContentReader(File f) {
    Reader reader = null;
    Document temp_doc = null;
    try {
      temp_doc = LucenePDFDocument.getDocument(f);
      reader = temp_doc.getField("contents").readerValue();
    } catch (Exception pdfe) {
      reader = null;
    }
    return reader;
  }

  public String getFileContentString(File f) {
    return null;
  }
}
