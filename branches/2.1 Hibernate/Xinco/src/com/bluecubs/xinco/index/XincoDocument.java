/**
 *Copyright 2004 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community in exchange for free software!
 * More information on: http://www.bluecubs.org
 *************************************************************
 *
 * Name:            XincoDocument
 *
 * Description:     convert XincoCoreData to Lucene Documents 
 *
 * Original Author: Alexander Manes
 * Date:            2004/10/31
 *
 * Modifications:
 * 
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.index;

import java.io.File;
import java.io.Reader;
import java.io.FileInputStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import com.bluecubs.xinco.core.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreDataTypeServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.index.filetypes.XincoIndexFileType;
import com.bluecubs.xinco.index.filetypes.XincoIndexText;

/** A utility for making Lucene Documents from a File. */
public class XincoDocument {

    public static Document getXincoDocument(XincoCoreDataServer d, boolean indexContent) throws java.io.FileNotFoundException {

        int i, l;
        FileInputStream is = null;
        Document doc = null;
        int fileType = 0;
        int file_ext_index = 0;
        String file_ext = "";

        doc = new Document();

        //add XincoCoreData information
        doc.add(new Field("id", (new Integer(d.getId())).toString(),
                Field.Store.YES, Field.Index.TOKENIZED));
        doc.add(new Field("designation", d.getDesignation(), Field.Store.YES, Field.Index.TOKENIZED));
        doc.add(new Field("language", String.valueOf(d.getXincoCoreLanguageId()),
                Field.Store.YES, Field.Index.TOKENIZED));
        //add content of file
        if (indexContent) {
            if ((d.getXincoCoreDataTypeId() == 1) && (d.getStatusNumber() != 3)) {
                //process non-archived file
                //extract file extension from file name
                file_ext_index = ((XincoAddAttribute) d.getXincoAddAttributes().elementAt(0)).getAttribVarchar().lastIndexOf(".");
                if (file_ext_index == -1) {
                    file_ext = "";
                } else {
                    if (file_ext_index >= ((XincoAddAttribute) d.getXincoAddAttributes().elementAt(0)).getAttribVarchar().length() - 1) {
                        file_ext = "";
                    } else {
                        file_ext = ((XincoAddAttribute) d.getXincoAddAttributes().elementAt(0)).getAttribVarchar().substring(file_ext_index + 1);
                    }
                }
                //check which indexer to use for file extension
                fileType = 0; // default: index as TEXT
                for (l = 0; l < XincoDBManager.config.getFileIndexerCount(); l++) {
                    for (i = 0; i < ((String[]) XincoDBManager.config.getIndexFileTypesExt().elementAt(l)).length; i++) {
                        if (((String[]) XincoDBManager.config.getIndexFileTypesExt().elementAt(l))[i].compareTo(file_ext) == 0) {
                            fileType = l + 1; // file-type specific indexing
                            break;
                        }
                    }
                    if (fileType > 0) {
                        break;
                    }
                }
                if (fileType == 0) {
                    for (i = 0; i < XincoDBManager.config.getIndexNoIndex().length; i++) {
                        if (XincoDBManager.config.getIndexNoIndex()[i].compareTo(file_ext) == 0) {
                            fileType = -1; // NO indexing
                            break;
                        }
                    }
                }
                // call actual indexing classes
                XincoIndexFileType xift = null;
                Reader ContentReader = null;
                String ContentString = null;
                if (fileType == 0) {
                    // index as TEXT
                    xift = new XincoIndexText();
                    doc.add(new Field("file", xift.getFileContentReader(new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.getFileRepositoryPath(), d.getId(), "" + d.getId())))));
                } else if (fileType > 0) {
                    // file-type specific indexing
                    try {
                        xift = (XincoIndexFileType) Class.forName((String) XincoDBManager.config.getIndexFileTypesClass().elementAt(fileType - 1)).newInstance();
                        ContentReader = xift.getFileContentReader(new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.getFileRepositoryPath(), d.getId(), "" + d.getId())));
                        if (ContentReader != null) {
                            doc.add(new Field("file", ContentReader));
                        } else {
                            ContentString = xift.getFileContentString(new File(XincoCoreDataServer.getXincoCoreDataPath(XincoDBManager.config.getFileRepositoryPath(), d.getId(), "" + d.getId())));
                            if (ContentString != null) {
                                doc.add(new Field("file", ContentString, Field.Store.YES, Field.Index.TOKENIZED));
                            }
                        }
                    } catch (Exception ie) {
                    }
                }
            }
        }

        //add attributes
        for (i = 0; i < d.getXincoAddAttributes().size(); i++) {
            if (((XincoCoreDataTypeAttribute) ((XincoCoreDataTypeServer) d.getXincoAddAttributes().get(i)).getXincoCoreDataTypeAttributes().elementAt(i)).getDataType().toLowerCase().compareTo("int") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) ((XincoCoreDataTypeServer) d.getXincoAddAttributes().get(i)).getXincoCoreDataTypeAttributes().elementAt(i)).getDesignation(),
                        String.valueOf(((XincoAddAttribute) d.getXincoAddAttributes().elementAt(i)).getAttribInt()), Field.Store.YES, Field.Index.NO));
            }
            if (((XincoCoreDataTypeAttribute) ((XincoCoreDataTypeServer) d.getXincoAddAttributes().get(i)).getXincoCoreDataTypeAttributes().elementAt(i)).getDataType().toLowerCase().compareTo("unsignedint") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) ((XincoCoreDataTypeServer) d.getXincoAddAttributes().get(i)).getXincoCoreDataTypeAttributes().elementAt(i)).getDesignation(),
                        String.valueOf(((XincoAddAttribute) d.getXincoAddAttributes().elementAt(i)).getAttribUnsignedint()), Field.Store.YES, Field.Index.NO));
            }
            if (((XincoCoreDataTypeAttribute) ((XincoCoreDataTypeServer) d.getXincoAddAttributes().get(i)).getXincoCoreDataTypeAttributes().elementAt(i)).getDataType().toLowerCase().compareTo("double") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) ((XincoCoreDataTypeServer) d.getXincoAddAttributes().get(i)).getXincoCoreDataTypeAttributes().elementAt(i)).getDesignation(),
                        String.valueOf(((XincoAddAttribute) d.getXincoAddAttributes().elementAt(i)).getAttribDouble()), Field.Store.YES, Field.Index.NO));
            }
            if (((XincoCoreDataTypeAttribute) ((XincoCoreDataTypeServer) d.getXincoAddAttributes().get(i)).getXincoCoreDataTypeAttributes().elementAt(i)).getDataType().toLowerCase().compareTo("varchar") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) ((XincoCoreDataTypeServer) d.getXincoAddAttributes().get(i)).getXincoCoreDataTypeAttributes().elementAt(i)).getDesignation(),
                        String.valueOf(((XincoAddAttribute) d.getXincoAddAttributes().elementAt(i)).getAttribVarchar()), Field.Store.YES, Field.Index.NO));
            }
            if (((XincoCoreDataTypeAttribute) ((XincoCoreDataTypeServer) d.getXincoAddAttributes().get(i)).getXincoCoreDataTypeAttributes().elementAt(i)).getDataType().toLowerCase().compareTo("text") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) ((XincoCoreDataTypeServer) d.getXincoAddAttributes().get(i)).getXincoCoreDataTypeAttributes().elementAt(i)).getDesignation(),
                        String.valueOf(((XincoAddAttribute) d.getXincoAddAttributes().elementAt(i)).getAttribText()), Field.Store.YES, Field.Index.NO));
            }
            if (((XincoCoreDataTypeAttribute) ((XincoCoreDataTypeServer) d.getXincoAddAttributes().get(i)).getXincoCoreDataTypeAttributes().elementAt(i)).getDataType().toLowerCase().compareTo("datetime") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) ((XincoCoreDataTypeServer) d.getXincoAddAttributes().get(i)).getXincoCoreDataTypeAttributes().elementAt(i)).getDesignation(),
                        String.valueOf(((XincoAddAttribute) d.getXincoAddAttributes().elementAt(i)).getAttribDatetime()), Field.Store.YES, Field.Index.NO));
            }
        }
        return doc;
    }

    private XincoDocument() {
    }
}
