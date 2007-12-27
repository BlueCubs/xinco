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
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Remove XIncoCoreData from index
     * @param d
     * @param DBM
     * @return boolean
     */
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
                    Logger.getLogger(XincoIndexer.class.getName()).log(Level.SEVERE, null, re);
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

    /**
     * Optimize Lucene index
     * @param DBM
     * @return boolean
     */
    public static synchronized boolean optimizeIndex(XincoDBManager DBM) {
        IndexWriter writer = null;
        try {
            //optimize index
            writer = new IndexWriter(DBM.config.getFileIndexPath(), new StandardAnalyzer(), false);
            writer.optimize();
            writer.close();
        } catch (Exception e) {
            Logger.getLogger(XincoIndexer.class.getName()).log(Level.SEVERE, null, e);
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

    /**
     * FInd XincoCoreData in the index
     * @param designation
     * @param language_id
     * @param DBM
     * @return
     */
    @SuppressWarnings("unchecked")
    public static synchronized Vector findXincoCoreData(String designation, int language_id, XincoDBManager DBM) {
        Vector v = new Vector();
        try {
            int i = 0;
            Searcher searcher = null;
            try {
                searcher = new IndexSearcher(DBM.config.getFileIndexPath());
                Analyzer analyzer = new StandardAnalyzer();

                if (language_id != 0) {
                    designation += " AND language:" + language_id;
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
                            xcde.printStackTrace();
                        }
                    }
                    if (i >= DBM.config.getMaxSearchResult()) {
                        break;
                    }
                }
                searcher.close();
            } catch (Exception e) {
                Logger.getLogger(XincoIndexer.class.getName()).log(Level.SEVERE, null, e);
                try {
                    if (searcher != null) {
                        try {
                            searcher.close();
                        } catch (Exception se) {
                        }
                    }
                    if (DBM.getXincoSettingServer().getSetting("setting.enable.developermode").isBool_value()) {
                        Logger.getLogger(XincoIndexer.class.getName()).log(Level.SEVERE, null, e);
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
