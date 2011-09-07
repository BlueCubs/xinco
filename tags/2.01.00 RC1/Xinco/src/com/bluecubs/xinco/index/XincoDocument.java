//
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

import com.bluecubs.xinco.add.XincoAddAttribute;
import java.io.File;
import java.io.Reader;
import java.io.FileInputStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.core.server.*;
import com.bluecubs.xinco.index.filetypes.*;

/** A utility for making Lucene Documents from a File. */
public class XincoDocument {

    public static Document getXincoDocument(XincoCoreData d, boolean index_content, XincoDBManager DBM) throws java.io.FileNotFoundException {

        int i, j, l;
        int i2, j2;
        short k, k2;
        FileInputStream is = null;
        Document doc = null;
        Document temp_doc = null;
        int file_type = 0;
        int file_ext_index = 0;
        String file_ext = "";

        doc = new Document();

        //add XincoCoreData information
        doc.add(new Field("id", (new Integer(d.getId())).toString(),
                Field.Store.COMPRESS, Field.Index.TOKENIZED));
        doc.add(new Field("designation", d.getDesignation(), Field.Store.COMPRESS, Field.Index.TOKENIZED));
        doc.add(new Field("language", (new Integer(d.getXinco_core_language().getId())).toString(),
                Field.Store.COMPRESS, Field.Index.TOKENIZED));
        //add content of file
        if (index_content) {
            if ((d.getXinco_core_data_type().getId() == 1) && (d.getStatus_number() != 3)) { //process non-archived file
                //extract file extension from file name
                file_ext_index = ((XincoAddAttribute) d.getXinco_add_attributes().elementAt(0)).getAttrib_varchar().lastIndexOf(".");
                if (file_ext_index == -1) {
                    file_ext = "";
                } else {
                    if (file_ext_index >= ((XincoAddAttribute) d.getXinco_add_attributes().elementAt(0)).getAttrib_varchar().length() - 1) {
                        file_ext = "";
                    } else {
                        file_ext = ((XincoAddAttribute) d.getXinco_add_attributes().elementAt(0)).getAttrib_varchar().substring(file_ext_index + 1);
                    }
                }
                //check which indexer to use for file extension
                file_type = 0; // default: index as TEXT
                for (l = 0; l < DBM.config.getFileIndexerCount(); l++) {
                    for (i = 0; i < ((String[]) DBM.config.getIndexFileTypesExt().elementAt(l)).length; i++) {
                        if (((String[]) DBM.config.getIndexFileTypesExt().elementAt(l))[i].compareTo(file_ext) == 0) {
                            file_type = l + 1; // file-type specific indexing
                            break;
                        }
                    }
                    if (file_type > 0) {
                        break;
                    }
                }
                if (file_type == 0) {
                    for (i = 0; i < DBM.config.getIndexNoIndex().length; i++) {
                        if (DBM.config.getIndexNoIndex()[i].compareTo(file_ext) == 0) {
                            file_type = -1; // NO indexing
                            break;
                        }
                    }
                }
                // call actual indexing classes
                XincoIndexFileType xift = null;
                Reader ContentReader = null;
                String ContentString = null;
                if (file_type == 0) {
                    // index as TEXT
                    xift = new XincoIndexText();
                    doc.add(new Field("file", xift.getFileContentReader(new File(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.getFileRepositoryPath(), d.getId(), "" + d.getId())))));
                } else if (file_type > 0) {
                    // file-type specific indexing
                    try {
                        xift = (XincoIndexFileType) Class.forName((String) DBM.config.getIndexFileTypesClass().elementAt(file_type - 1)).newInstance();
                        ContentReader = xift.getFileContentReader(new File(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.getFileRepositoryPath(), d.getId(), "" + d.getId())));
                        if (ContentReader != null) {
                            doc.add(new Field("file", ContentReader));
                        } else {
                            ContentString = xift.getFileContentString(new File(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.getFileRepositoryPath(), d.getId(), "" + d.getId())));
                            if (ContentString != null) {
                                doc.add(new Field("file", ContentString, Field.Store.COMPRESS, Field.Index.TOKENIZED));
                            }
                        }
                    } catch (Exception ie) {
                    }
                }
            }
        }

        //add attributes
        for (i = 0; i < d.getXinco_add_attributes().size(); i++) {
            if (((XincoCoreDataTypeAttribute) d.getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().toLowerCase().compareTo("int") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) d.getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getDesignation(),
                        "" + ((XincoAddAttribute) d.getXinco_add_attributes().elementAt(i)).getAttrib_int(), Field.Store.COMPRESS, Field.Index.NO));
            }
            if (((XincoCoreDataTypeAttribute) d.getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().toLowerCase().compareTo("unsignedint") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) d.getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getDesignation(), "" + ((XincoAddAttribute) d.getXinco_add_attributes().elementAt(i)).getAttrib_unsignedint(), Field.Store.COMPRESS, Field.Index.NO));
            }
            if (((XincoCoreDataTypeAttribute) d.getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().toLowerCase().compareTo("double") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) d.getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getDesignation(), "" +
                        ((XincoAddAttribute) d.getXinco_add_attributes().elementAt(i)).getAttrib_double(), Field.Store.COMPRESS, Field.Index.NO));
            }
            if (((XincoCoreDataTypeAttribute) d.getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().toLowerCase().compareTo("varchar") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) d.getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getDesignation(),
                        ((XincoAddAttribute) d.getXinco_add_attributes().elementAt(i)).getAttrib_varchar(), Field.Store.COMPRESS, Field.Index.NO));
            }
            if (((XincoCoreDataTypeAttribute) d.getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().toLowerCase().compareTo("text") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) d.getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getDesignation(),
                        ((XincoAddAttribute) d.getXinco_add_attributes().elementAt(i)).getAttrib_text(), Field.Store.COMPRESS, Field.Index.NO));
            }
            if (((XincoCoreDataTypeAttribute) d.getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getData_type().toLowerCase().compareTo("datetime") == 0) {
                doc.add(new Field(((XincoCoreDataTypeAttribute) d.getXinco_core_data_type().getXinco_core_data_type_attributes().elementAt(i)).getDesignation(), "" +
                        ((XincoAddAttribute) d.getXinco_add_attributes().elementAt(i)).getAttrib_datetime(), Field.Store.COMPRESS, Field.Index.NO));
            }
        }
        return doc;
    }

    private XincoDocument() {
    }
}