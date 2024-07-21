/**
 * Copyright 2012 blueCubs.com
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
 * <p>Name: XincoDocument
 *
 * <p>Description: convert XincoCoreData to Lucene Documents
 *
 * <p>Original Author: Alexander Manes Date: 2004/10/31
 *
 * <p>Modifications:
 *
 * <p>Who? When? What? - - -
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.core.server.index;

import static com.bluecubs.xinco.core.server.XincoCoreDataServer.getXincoCoreDataPath;
import static com.bluecubs.xinco.core.server.XincoDBManager.CONFIG;
import static java.lang.Class.forName;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static org.apache.lucene.document.Field.Index.ANALYZED;
import static org.apache.lucene.document.Field.Index.NOT_ANALYZED;
import static org.apache.lucene.document.Field.Store.YES;

import com.bluecubs.xinco.core.server.index.filetypes.XincoIndexFileType;
import com.bluecubs.xinco.core.server.index.filetypes.XincoIndexGenericFile;
import com.bluecubs.xinco.core.server.index.filetypes.XincoIndexText;
import com.bluecubs.xinco.server.service.XincoAddAttribute;
import com.bluecubs.xinco.server.service.XincoCoreData;
import com.bluecubs.xinco.server.service.XincoCoreDataTypeAttribute;
import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/** A utility for making Lucene Documents from a File. */
public class XincoDocument {

  private static final Logger LOG = getLogger(XincoDocument.class.getSimpleName());

  public static Document getXincoDocument(XincoCoreData d, boolean indexContent)
      throws java.io.FileNotFoundException {
    int i, l;
    Document doc;
    int fileType;
    int file_extIndex;
    String file_ext;

    doc = new Document();

    // add XincoCoreData information
    doc.add(new Field("id", (Integer.valueOf(d.getId())).toString(), YES, NOT_ANALYZED));
    doc.add(new Field("designation", d.getDesignation(), YES, ANALYZED));
    doc.add(
        new Field(
            "language",
            (Integer.valueOf(d.getXincoCoreLanguage().getId())).toString(),
            YES,
            NOT_ANALYZED));

    // add content of file
    if (indexContent
        && (d.getXincoCoreDataType().getId() == 1)
        && (d.getStatusNumber() != 3)) { // process non-archived file
      // extract file extension from file name
      file_extIndex =
          ((XincoAddAttribute) ((ArrayList) d.getXincoAddAttributes()).get(0))
              .getAttribVarchar()
              .lastIndexOf('.');
      if (file_extIndex == -1) {
        file_ext = "";
      } else {
        if (file_extIndex
            >= ((XincoAddAttribute) ((ArrayList) d.getXincoAddAttributes()).get(0))
                    .getAttribVarchar()
                    .length()
                - 1) {
          file_ext = "";
        } else {
          file_ext =
              ((XincoAddAttribute) ((ArrayList) d.getXincoAddAttributes()).get(0))
                  .getAttribVarchar()
                  .substring(file_extIndex + 1);
        }
      }
      // check which indexer to use for file extension
      fileType = 0; // default: index as TEXT
      for (l = 0; l < CONFIG.getFileIndexerCount(); l++) {
        for (i = 0; i < ((String[]) CONFIG.getIndexFileTypesExt().get(l)).length; i++) {
          if (((String[]) CONFIG.getIndexFileTypesExt().get(l))[i].compareTo(file_ext) == 0) {
            fileType = l + 1; // file-type specific indexing
            break;
          }
        }
        if (fileType > 0) {
          break;
        }
      }
      if (fileType == 0) {
        for (i = 0; i < CONFIG.getIndexNoIndex().length; i++) {
          if (CONFIG.getIndexNoIndex()[i].compareTo(file_ext) == 0) {
            fileType = -1; // NO indexing
            break;
          }
        }
      }
      // call actual indexing classes
      XincoIndexFileType xift;
      Reader ContentReader;
      String ContentString;
      if (fileType == 0) {
        // index as TEXT
        xift = new XincoIndexText();
        doc.add(
            new Field(
                "file",
                xift.getFileContentReader(
                    new File(
                        getXincoCoreDataPath(
                            CONFIG.fileRepositoryPath, d.getId(), "" + d.getId())))));
      } else if (fileType > 0) {
        try {
          // file-type specific indexing
          xift =
              (XincoIndexFileType)
                  forName((String) CONFIG.getIndexFileTypesClass().get(fileType - 1)).newInstance();
          ContentReader =
              xift.getFileContentReader(
                  new File(
                      getXincoCoreDataPath(CONFIG.fileRepositoryPath, d.getId(), "" + d.getId())));
          if (ContentReader != null) {
            doc.add(new Field("file", ContentReader));
          } else {
            ContentString =
                xift.getFileContentString(
                    new File(
                        getXincoCoreDataPath(
                            CONFIG.fileRepositoryPath, d.getId(), "" + d.getId())));
            if (ContentString != null) {
              doc.add(new Field("file", ContentString, YES, ANALYZED));
            }
          }
        } catch (ClassNotFoundException ex) {
          // Try the generic method
          XincoIndexGenericFile generic = new XincoIndexGenericFile();
          ContentString =
              generic.getFileContentString(
                  new File(
                      getXincoCoreDataPath(CONFIG.fileRepositoryPath, d.getId(), "" + d.getId())));
          if (ContentString != null) {
            doc.add(new Field("file", ContentString, YES, ANALYZED));
          }
        } catch (IllegalAccessException | InstantiationException ie) {
          LOG.log(SEVERE, d.toString(), ie);
        }
      }
    }

    // add attributes
    for (i = 0; i < ((ArrayList) d.getXincoAddAttributes()).size(); i++) {
      if (((XincoCoreDataTypeAttribute)
                  ((ArrayList) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes()).get(i))
              .getDataType()
              .toLowerCase()
              .compareTo("int")
          == 0) {
        doc.add(
            new Field(
                ((XincoCoreDataTypeAttribute)
                        ((ArrayList) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes())
                            .get(i))
                    .getDesignation(),
                ""
                    + ((XincoAddAttribute) ((ArrayList) d.getXincoAddAttributes()).get(i))
                        .getAttribInt(),
                YES,
                NOT_ANALYZED));
      }
      if (((XincoCoreDataTypeAttribute)
                  ((ArrayList) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes()).get(i))
              .getDataType()
              .toLowerCase()
              .compareTo("unsignedint")
          == 0) {
        doc.add(
            new Field(
                ((XincoCoreDataTypeAttribute)
                        ((ArrayList) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes())
                            .get(i))
                    .getDesignation(),
                ""
                    + ((XincoAddAttribute) ((ArrayList) d.getXincoAddAttributes()).get(i))
                        .getAttribUnsignedint(),
                YES,
                NOT_ANALYZED));
      }
      if (((XincoCoreDataTypeAttribute)
                  ((ArrayList) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes()).get(i))
              .getDataType()
              .toLowerCase()
              .compareTo("double")
          == 0) {
        doc.add(
            new Field(
                ((XincoCoreDataTypeAttribute)
                        ((ArrayList) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes())
                            .get(i))
                    .getDesignation(),
                ""
                    + ((XincoAddAttribute) ((ArrayList) d.getXincoAddAttributes()).get(i))
                        .getAttribDouble(),
                YES,
                NOT_ANALYZED));
      }
      if (((XincoCoreDataTypeAttribute)
                  ((ArrayList) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes()).get(i))
              .getDataType()
              .toLowerCase()
              .compareTo("varchar")
          == 0) {
        doc.add(
            new Field(
                ((XincoCoreDataTypeAttribute)
                        ((ArrayList) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes())
                            .get(i))
                    .getDesignation(),
                ((XincoAddAttribute) ((ArrayList) d.getXincoAddAttributes()).get(i))
                    .getAttribVarchar(),
                YES,
                ANALYZED));
      }
      if (((XincoCoreDataTypeAttribute)
                  ((ArrayList) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes()).get(i))
              .getDataType()
              .toLowerCase()
              .compareTo("text")
          == 0) {
        doc.add(
            new Field(
                ((XincoCoreDataTypeAttribute)
                        ((ArrayList) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes())
                            .get(i))
                    .getDesignation(),
                ((XincoAddAttribute) ((ArrayList) d.getXincoAddAttributes()).get(i))
                    .getAttribText(),
                YES,
                ANALYZED));
      }
      if (((XincoCoreDataTypeAttribute)
                  ((ArrayList) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes()).get(i))
              .getDataType()
              .toLowerCase()
              .compareTo("datetime")
          == 0) {
        doc.add(
            new Field(
                ((XincoCoreDataTypeAttribute)
                        ((ArrayList) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes())
                            .get(i))
                    .getDesignation(),
                ""
                    + ((XincoAddAttribute) ((ArrayList) d.getXincoAddAttributes()).get(i))
                        .getAttribDatetime(),
                YES,
                NOT_ANALYZED));
      }
    }
    return doc;
  }

  private XincoDocument() {}
}
