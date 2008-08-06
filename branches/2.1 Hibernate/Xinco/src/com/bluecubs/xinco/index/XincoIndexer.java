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

import com.bluecubs.xinco.core.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import java.util.Vector;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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

    public static synchronized boolean indexXincoCoreData(XincoCoreDataServer d, boolean index_content) {
        IndexWriter writer = null;
        try {
            //check if document exists in index and delete
            XincoIndexer.removeXincoCoreData(d);
            //add document to index
            try {
                writer = new IndexWriter(XincoDBManager.config.getFileIndexPath(), new StandardAnalyzer(), false);
            } catch (Exception ie) {
                writer = new IndexWriter(XincoDBManager.config.getFileIndexPath(), new StandardAnalyzer(), true);
            }
            writer.addDocument(XincoDocument.getXincoDocument(d, index_content));
            //writer.optimize();
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

    public static synchronized boolean removeXincoCoreData(XincoCoreData d) {
        IndexReader reader = null;
        //check if document exists in index and delete
        try {
            reader = IndexReader.open(XincoDBManager.config.getFileIndexPath());
            reader.deleteDocuments(new Term("id", "" + d.getId()));
            reader.close();
        } catch (Exception re) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception re2) {
                }
            }
            return false;
        }
        return true;
    }

    public static synchronized boolean optimizeIndex() {
        IndexWriter writer = null;
        try {
            //optimize index
            writer = new IndexWriter(XincoDBManager.config.getFileIndexPath(), new StandardAnalyzer(), false);
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

    public static synchronized Vector findXincoCoreData(String s, int l) {
        int i = 0;
        Vector v = new Vector();
        Searcher searcher = null;
        try {
            searcher = new IndexSearcher(XincoDBManager.config.getFileIndexPath());
            Analyzer analyzer = new StandardAnalyzer();
            //add language to query
            if (l != 0) {
                s = s + " AND language:" + l;
            }
            QueryParser luceneParser = new QueryParser("designation", analyzer);
            Query query = luceneParser.parse(s);
            Hits hits = searcher.search(query);

            for (i = 0; i < hits.length(); i++) {
                try {
                    v.addElement(new XincoCoreDataServer(Integer.parseInt(hits.doc(i).get("id"))));
                } catch (Exception xcde) {
                    // don't add non-existing data
                }
                if (i >= XincoDBManager.config.getMaxSearchResult()) {
                    break;
                }
            }
            searcher.close();
        } catch (Exception e) {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (Exception se) {
                }
            }
            return null;
        }
        return v;
    }

    //private constructor to avoid instance generation with new-operator!
    private XincoIndexer() {
    }
}
