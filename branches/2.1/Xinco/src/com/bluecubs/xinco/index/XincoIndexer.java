///**
// *Copyright 2005 blueCubs.com
// *
// *Licensed under the Apache License, Version 2.0 (the "License");
// *you may not use this file except in compliance with the License.
// *You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// *Unless required by applicable law or agreed to in writing, software
// *distributed under the License is distributed on an "AS IS" BASIS,
// *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *See the License for the specific language governing permissions and
// *limitations under the License.
// *
// *************************************************************
// * This project supports the blueCubs vision of giving back
// * to the community in exchange for free software!
// * More information on: http://www.bluecubs.org
// *************************************************************
// *
// * Name:            XincoIndexer
// *
// * Description:     handle document indexing 
// *
// * Original Author: Alexander Manes
// * Date:            2004/10/31
// *
// * Modifications:
// * 
// * Who?             When?             What?
// * -                -                 -
// *
// *************************************************************
// */
//package com.bluecubs.xinco.index;
//
//import java.util.Vector;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.index.*;
//import org.apache.lucene.search.*;
//import org.apache.lucene.queryParser.QueryParser;
//import org.apache.lucene.analysis.*;
//import com.bluecubs.xinco.core.*;
//import com.bluecubs.xinco.core.server.*;
//
///**
// * This class handles document indexing for xinco.
// * Edit configuration values in context.xml
// */
//public class XincoIndexer {
//
//    public static synchronized boolean indexXincoCoreData(XincoCoreData d, boolean index_content, XincoDBManager DBM) {
//        IndexWriter writer = null;
//        try {
//            //check if document exists in index and delete
//            XincoIndexer.removeXincoCoreData(d, DBM);
//            //add document to index
//            try {
//                writer = new IndexWriter(DBM.config.getFileIndexPath(), new StandardAnalyzer(), false);
//            } catch (Exception ie) {
//                writer = new IndexWriter(DBM.config.getFileIndexPath(), new StandardAnalyzer(), true);
//            }
//            writer.addDocument(XincoDocument.getXincoDocument(d, index_content, DBM));
//            //writer.optimize();
//            writer.close();
//        } catch (Throwable e) {
//            if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
//                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, null, e);
//            }
//            if (writer != null) {
//                try {
//                    writer.close();
//                } catch (Exception we) {
//                }
//            }
//            return false;
//        }
//        return true;
//    }
//
//    public static synchronized boolean removeXincoCoreData(XincoCoreData d, XincoDBManager DBM) {
//        IndexReader reader = null;
//        //check if document exists in index and delete
//        try {
//            reader = IndexReader.open(DBM.config.getFileIndexPath());
//            reader.delete(new Term("id", "" + d.getId()));
//            reader.close();
//        } catch (Exception re) {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (Exception re2) {
//                }
//            }
//            return false;
//        }
//        return true;
//    }
//
//    public static synchronized boolean optimizeIndex(XincoDBManager dbm) {
//        IndexWriter writer = null;
//        try {
//            //optimize index
//            writer = new IndexWriter(dbm.config.getFileIndexPath(), new StandardAnalyzer(), false);
//            writer.optimize();
//            writer.close();
//        } catch (Throwable e) {
//            if (writer != null) {
//                try {
//                    writer.close();
//                } catch (Exception we) {
//                }
//            }
//            return false;
//        }
//        return true;
//    }
//
//    public static synchronized Vector findXincoCoreData(String designation, int l, XincoDBManager dbm) {
//        int i = 0;
//        Vector v = new Vector();
//        Searcher searcher = null;
//        try {
//            searcher = new IndexSearcher(dbm.config.getFileIndexPath());
//            Analyzer analyzer = new StandardAnalyzer();
//            //add language to query
//            if (l != 0) {
//                designation = designation + " AND language:" + l;
//            }
//            Query query = QueryParser.parse(designation, "designation", analyzer);
//
//            Hits hits = searcher.search(query);
//            for (i = 0; i < hits.length(); i++) {
//                try {
//                    v.addElement(new XincoCoreDataServer(Integer.parseInt(hits.doc(i).get("id")), dbm));
//                } catch (Exception xcde) {
//                // don't add non-existing data
//                }
//                if (i >= dbm.config.getMaxSearchResult()) {
//                    break;
//                }
//            }
//            searcher.close();
//        } catch (Throwable e) {
//            if (searcher != null) {
//                try {
//                    searcher.close();
//                } catch (Exception se) {
//                }
//            }
//            return null;
//        }
//        return v;
//    }
//
//    //private constructor to avoid instance generation with new-operator!
//    private XincoIndexer() {
//    }
//}
/**
 *Copyright 2005 blueCubs.com
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
 * Name:            XincoIndexer
 *
 * Description:     handle document indexing
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

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.analysis.*;
import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.core.server.*;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 * This class handles document indexing for xinco.
 * Edit configuration values in context.xml
 */
public class XincoIndexer {

    public static synchronized boolean indexXincoCoreData(XincoCoreData d, boolean index_content, XincoDBManager DBM) {
        IndexWriter writer = null;
        try {
            //check if document exists in index and delete
            if (DBM.getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                System.out.println("Removing data from index if it exists...");
            }
            XincoIndexer.removeXincoCoreData(d, DBM);
            //add document to index
            try {
                writer = new IndexWriter(DBM.config.getFileIndexPath(), new StandardAnalyzer(), false);
            } catch (Exception ie) {
                writer = new IndexWriter(DBM.config.getFileIndexPath(), new StandardAnalyzer(), true);
            }
            if (DBM.getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                System.out.println("Indexing...");
            }
            Document temp = XincoDocument.getXincoDocument(d, index_content, DBM);
            List l = temp.getFields();
            if (DBM.getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                for (int i = 0; i < l.size(); i++) {
                    System.out.println(((Field) l.get(i)).toString());
                }
            }
            writer.addDocument(temp);
            writer.flush();
            writer.close();
            if (DBM.getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                System.out.println("Indexing complete!");
            }
        } catch (Exception e) {
            Logger.getLogger(XincoIndexer.class.getName()).log(Level.SEVERE, null, e);
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception we) {
                    Logger.getLogger(XincoIndexer.class.getName()).log(Level.SEVERE, null, we);
                }
            }
            return false;
        }
        return true;
    }

    public static synchronized boolean removeXincoCoreData(XincoCoreData d, XincoDBManager DBM) {
        IndexReader reader = null;
        //check if document exists in index and delete
        try {
            if (IndexReader.indexExists(DBM.config.getFileIndexPath())) {
                reader = IndexReader.open(DBM.config.getFileIndexPath());
                reader.deleteDocuments(new Term("id", "" + d.getId()));
                reader.close();
            }
        } catch (Exception re) {
            try {
                if (DBM.getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                    Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, null, re);
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception re2) {
                    }
                }
                return false;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoIndexer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }

    public static synchronized boolean optimizeIndex(XincoDBManager DBM) {
        IndexWriter writer = null;
        try {
            //optimize index
            writer = new IndexWriter(DBM.config.getFileIndexPath(), new StandardAnalyzer(), false);
            writer.optimize();
            writer.close();
        } catch (Exception e) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception we) {
                }
            }
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static synchronized Vector findXincoCoreData(String designation, int l, XincoDBManager DBM) {
        Vector v = new Vector();
        try {
            int i = 0;
            Searcher searcher = null;
            try {
                searcher = new IndexSearcher(DBM.config.getFileIndexPath());
                Analyzer analyzer = new StandardAnalyzer();

                if (l != 0) {
                    designation += " AND language:" + l;
                }
                QueryParser parser = new QueryParser("designation", analyzer);
                Query query = parser.parse(designation);
                if (DBM.getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                    System.out.println("Query: " + designation);
                }
                Hits hits = searcher.search(query);
                if (DBM.getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                    System.out.println("Hits: " + hits.length());
                }
                for (i = 0; i < hits.length(); i++) {
                    try {
                        if (DBM.getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                            System.out.println("Document found: " + hits.doc(i).get("designation"));
                        }
                        v.addElement(new XincoCoreDataServer(Integer.parseInt(hits.doc(i).get("id")), DBM));
                    } catch (Exception xcde) {
                        if (DBM.getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                            Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, null, xcde);
                        }
                    }
                    if (i >= DBM.config.getMaxSearchResult()) {
                        break;
                    }
                }
                searcher.close();
            } catch (Exception e) {
                try {
                    if (searcher != null) {
                        try {
                            searcher.close();
                        } catch (Exception se) {
                        }
                    }
                    if (DBM.getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                        Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, null, e);
                    }
                    return null;
                } catch (XincoSettingException ex) {
                    Logger.getLogger(XincoIndexer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (DBM.getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                System.out.println("Returning " + v.size() + " results.");
            }
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return v;
    }

    //private constructor to avoid instance generation with new-operator!
    private XincoIndexer() {
    }
}
