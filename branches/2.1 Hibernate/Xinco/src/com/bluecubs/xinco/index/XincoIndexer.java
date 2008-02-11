package com.bluecubs.xinco.index;

import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import com.bluecubs.xinco.core.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeServer;
import com.bluecubs.xinco.core.server.persistence.XincoSettingServer;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;

/**
 * This class handles document indexing for xinco.
 * Edit configuration values in context.xml
 */
public class XincoIndexer {

    private static XincoConfigSingletonServer config = XincoConfigSingletonServer.getInstance();

    public static synchronized boolean indexXincoCoreData(XincoCoreData d, boolean index_content) {

        IndexWriter writer = null;
        try {
            //check if document exists in index and delete
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.INFO, "Removing data from index if it exists...");
            }
            XincoIndexer.removeXincoCoreData(d);
            //add document to index
            try {
                writer = new IndexWriter(config.getFileIndexPath(), new StandardAnalyzer(), false);
            } catch (Exception ie) {
                writer = new IndexWriter(config.getFileIndexPath(), new StandardAnalyzer(), true);
            }
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                System.out.println("Indexing...");
            }
            Document temp = XincoDocument.getXincoDocument(new XincoCoreDataServer(d.getId()), index_content);
            List l = temp.getFields();
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                for (int i = 0; i < l.size(); i++) {
                    System.out.println(((Field) l.get(i)).toString());
                }
            }
            writer.addDocument(temp);
            writer.flush();
            writer.close();
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
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

    /**
     * Remove XIncoCoreData from index
     * @param d
     * @return boolean
     */
    public static synchronized boolean removeXincoCoreData(XincoCoreData d) {
        IndexReader reader = null;
        //check if document exists in index and delete
        try {
            if (IndexReader.indexExists(config.getFileIndexPath())) {
                reader = IndexReader.open(config.getFileIndexPath());
                reader.deleteDocuments(new Term("id", "" + d.getId()));
                reader.close();
            }
        } catch (Exception re) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoIndexer.class.getName()).log(Level.SEVERE, null, re);
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception re2) {
                    }
                }
                return false;
            } catch (Throwable e) {
                Logger.getLogger(XincoIndexer.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return true;
    }

    /**
     * Optimize Lucene index
     * @return boolean
     */
    public static synchronized boolean optimizeIndex() {
        IndexWriter writer = null;
        try {
            //optimize index
            writer = new IndexWriter(config.getFileIndexPath(), new StandardAnalyzer(), false);
            writer.optimize();
            writer.close();
        } catch (Exception e) {
            Logger.getLogger(XincoIndexer.class.getName()).log(Level.INFO, null, e);
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception we) {
                    Logger.getLogger(XincoIndexer.class.getName()).log(Level.INFO, null, we);
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
     * @return
     */
    @SuppressWarnings("unchecked")
    public static synchronized Vector findXincoCoreData(String designation, int language_id) {
        Vector v = new Vector();
        try {
            int i = 0;
            Searcher searcher = null;
            try {
                searcher = new IndexSearcher(config.getFileIndexPath());
                Analyzer analyzer = new StandardAnalyzer();

                if (language_id != 0) {
                    designation += " AND language:" + language_id;
                }
                QueryParser parser = new QueryParser("designation", analyzer);
                Query query = parser.parse(designation);
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    System.out.println("Query: " + designation);
                }
                Hits hits = searcher.search(query);
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    System.out.println("Hits: " + hits.length());
                }
                for (i = 0; i < hits.length(); i++) {
                    try {
                        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                            System.out.println("Document found: " + hits.doc(i).get("designation"));
                        }
                        v.addElement(new XincoCoreDataServer(Integer.parseInt(hits.doc(i).get("id"))));
                    } catch (Exception xcde) {
                        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                            xcde.printStackTrace();
                        }
                    }
                    if (i >= config.getMaxSearchResult()) {
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
                            Logger.getLogger(XincoIndexer.class.getName()).log(Level.SEVERE, null, se);
                        }
                    }
                    if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                        Logger.getLogger(XincoIndexer.class.getName()).log(Level.SEVERE, null, e);
                    }
                    return null;
                } catch (Throwable ex) {
                    Logger.getLogger(XincoIndexer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                System.out.println("Returning " + v.size() + " results.");
            }
        } catch (Throwable ex) {
            Logger.getLogger(XincoIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return v;
    }

    //private constructor to avoid instance generation with new-operator!
    private XincoIndexer() {
    }
}
