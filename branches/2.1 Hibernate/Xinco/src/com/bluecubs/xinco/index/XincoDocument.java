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

import com.bluecubs.xinco.core.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreDataTypeAttributeServer;
import com.bluecubs.xinco.core.server.XincoPersistenceManager;
import com.bluecubs.xinco.index.filetypes.XincoIndexFileType;
import com.bluecubs.xinco.index.filetypes.XincoIndexText;
import java.io.File;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/** A utility for making Lucene Documents from a File. */
public class XincoDocument {

    /**
     * Get Xinco Document
     * @param d
     * @param indexContent
     * @return
     * @throws java.io.FileNotFoundException
     */
    public static Document getXincoDocument(XincoCoreDataServer d, boolean indexContent) throws java.io.FileNotFoundException {
        int i, l;
        Document doc = null;
        int fileType = 0;
        int file_extIndex = 0;
        String file_ext = "";
        doc = new Document();
        //add XincoCoreData information
        doc.add(new Field("id", (new Integer(d.getId())).toString(),
                Field.Store.COMPRESS, Field.Index.TOKENIZED));
        doc.add(new Field("designation", d.getDesignation(), Field.Store.COMPRESS, Field.Index.TOKENIZED));
        doc.add(new Field("language", (new Integer(d.getXincoCoreLanguageId())).toString(),
                Field.Store.COMPRESS, Field.Index.TOKENIZED));
        //add content of file
        if (indexContent) {
            if ((d.getXincoCoreDataTypeId() == 1) && (d.getStatusNumber() != 3)) { //process non-archived file
                //extract file extension from file name
                file_extIndex = ((XincoAddAttribute) d.getXincoAddAttributes().elementAt(0)).getAttribVarchar().lastIndexOf(".");
                if (file_extIndex == -1) {
                    file_ext = "";
                } else {
                    if (file_extIndex >= ((XincoAddAttribute) d.getXincoAddAttributes().elementAt(0)).getAttribVarchar().length() - 1) {
                        file_ext = "";
                    } else {
                        file_ext = ((XincoAddAttribute) d.getXincoAddAttributes().elementAt(0)).getAttribVarchar().substring(file_extIndex + 1);
                    }
                }
                //check which indexer to use for file extension
                fileType = 0; // default: index as TEXT
                for (l = 0; l < XincoPersistenceManager.config.getFileIndexerCount(); l++) {
                    if (((String) XincoPersistenceManager.config.getIndexFileTypesExt().elementAt(l)).compareTo(file_ext) == 0) {
                        fileType = l + 1; // file-type specific indexing
                        break;
                    }
                    if (fileType > 0) {
                        break;
                    }
                }
                if (fileType == 0) {
                    for (i = 0; i < XincoPersistenceManager.config.getIndexNoIndex().length; i++) {
                        if (XincoPersistenceManager.config.getIndexNoIndex()[i].compareTo(file_ext) == 0) {
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
                    doc.add(new Field("file", xift.getFileContentReader(new File(XincoCoreDataServer.getXincoCoreDataPath(XincoPersistenceManager.config.getFileRepositoryPath(), d.getId(), "" + d.getId())))));
                } else if (fileType > 0) {
                    try {
                        xift = (XincoIndexFileType) Class.forName((String) XincoPersistenceManager.config.getIndexFileTypesClass().elementAt(fileType - 1)).newInstance();
                        ContentReader = xift.getFileContentReader(new File(XincoCoreDataServer.getXincoCoreDataPath(XincoPersistenceManager.config.getFileRepositoryPath(), d.getId(), "" + d.getId())));
                        if (ContentReader != null) {
                            doc.add(new Field("file", ContentReader));
                        } else {
                            ContentString = xift.getFileContentString(new File(XincoCoreDataServer.getXincoCoreDataPath(XincoPersistenceManager.config.getFileRepositoryPath(), d.getId(), "" + d.getId())));
                            if (ContentString != null) {
                                doc.add(new Field("file", ContentString, Field.Store.COMPRESS, Field.Index.TOKENIZED));
                            }
                        }
                    } catch (Throwable ex) {
                        Logger.getLogger(XincoDocument.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        //add attributes
        for (i = 0; i < d.getXincoAddAttributes().size(); i++) {
            if (((XincoCoreDataTypeAttributeServer) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes().elementAt(i)).getDataType().toLowerCase().compareTo("int") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes().elementAt(i)).getDesignation(),
                        "" + ((XincoAddAttribute) d.getXincoAddAttributes().elementAt(i)).getAttribInt(), Field.Store.COMPRESS, Field.Index.NO));
            }
            if (((XincoCoreDataTypeAttribute) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes().elementAt(i)).getDataType().toLowerCase().compareTo("unsignedint") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes().elementAt(i)).getDesignation(), "" +
                        ((XincoAddAttribute) d.getXincoAddAttributes().elementAt(i)).getAttribUnsignedint(), Field.Store.COMPRESS, Field.Index.NO));
            }
            if (((XincoCoreDataTypeAttribute) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes().elementAt(i)).getDataType().toLowerCase().compareTo("double") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes().elementAt(i)).getDesignation(), "" +
                        ((XincoAddAttribute) d.getXincoAddAttributes().elementAt(i)).getAttribDouble(), Field.Store.COMPRESS, Field.Index.NO));
            }
            if (((XincoCoreDataTypeAttribute) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes().elementAt(i)).getDataType().toLowerCase().compareTo("varchar") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes().elementAt(i)).getDesignation(),
                        ((XincoAddAttribute) d.getXincoAddAttributes().elementAt(i)).getAttribVarchar(), Field.Store.COMPRESS, Field.Index.NO));
            }
            if (((XincoCoreDataTypeAttribute) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes().elementAt(i)).getDataType().toLowerCase().compareTo("text") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes().elementAt(i)).getDesignation(),
                        ((XincoAddAttribute) d.getXincoAddAttributes().elementAt(i)).getAttribText(), Field.Store.COMPRESS, Field.Index.NO));
            }
            if (((XincoCoreDataTypeAttribute) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes().elementAt(i)).getDataType().toLowerCase().compareTo("datetime") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) d.getXincoCoreDataType().getXincoCoreDataTypeAttributes().elementAt(i)).getDesignation(), "" +
                        ((XincoAddAttribute) d.getXincoAddAttributes().elementAt(i)).getAttribDatetime(), Field.Store.COMPRESS, Field.Index.NO));
            }
        }
        return doc;
    }

    private XincoDocument() {
    }
}
