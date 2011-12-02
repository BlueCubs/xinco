/**
 * Copyright 2011 blueCubs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 *
 * Name: XincoIndexer
 *
 * Description: handle document indexing
 *
 * Original Author: Alexander Manes Date: 2004/10/31
 *
 * Modifications:
 *
 * Who? When? What? - - -
 *
 *************************************************************
 */
package com.bluecubs.xinco.index;

import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.core.server.service.XincoCoreData;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * This class handles document indexing for Xinco. Edit configuration values in
 * database
 */
public class XincoIndexer {

    private static final Logger logger = Logger.getLogger(XincoIndexer.class.getSimpleName());

    public static synchronized boolean indexXincoCoreData(XincoCoreData d, boolean index_content) {
        IndexWriter writer = null;
        try {
            //check if document exists in index and delete
            XincoIndexer.removeXincoCoreData(d);
            //add document to index
            writer = new IndexWriter(FSDirectory.open(new File(XincoDBManager.config.FileIndexPath)), new IndexWriterConfig(Version.LUCENE_34, new StandardAnalyzer(Version.LUCENE_34)));
            writer.addDocument(XincoDocument.getXincoDocument(d, index_content));
            //writer.optimize();
            writer.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception we) {
                    logger.log(Level.SEVERE, null, we);
                }
            }
            return false;
        }
        return true;
    }

    public static synchronized boolean removeXincoCoreData(XincoCoreData d) {
        IndexReader reader = null;
        File indexDirectory = new File(XincoDBManager.config.FileIndexPath);
        if (indexDirectory.exists()) {
            //check if document exists in index and delete
            try {
                reader = IndexReader.open(FSDirectory.open(new File(XincoDBManager.config.FileIndexPath)), false);
                reader.deleteDocuments(new Term("id", "" + d.getId()));
                reader.close();
            } catch (Exception re) {
                logger.log(Level.SEVERE, null, re);
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception re2) {
                        logger.log(Level.SEVERE, null, re2);
                    }
                }
                return false;
            }
        }
        return true;
    }

    public static synchronized boolean optimizeIndex() {
        IndexWriter writer = null;
        boolean result = false;
        try {
            //optimize index
            writer = new IndexWriter(FSDirectory.open(new File(XincoDBManager.config.FileIndexPath)), new IndexWriterConfig(Version.LUCENE_34, new StandardAnalyzer(Version.LUCENE_34)));
            writer.optimize();
            writer.close();
            result = true;
        } catch (FileNotFoundException e) {
            logger.log(Level.INFO, "No index found at: {0}. Nothing to index.",
                    XincoDBManager.config.FileIndexPath);
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception we) {
                    logger.log(Level.SEVERE, null, we);
                }
            }
        }
        return result;
    }

    public static synchronized ArrayList findXincoCoreData(String queryString, int l) {
        int i;
        ArrayList<XincoCoreData> v = new ArrayList<XincoCoreData>();
        IndexSearcher searcher = null;
        try {
            searcher = new IndexSearcher(FSDirectory.open(new File(XincoDBManager.config.FileIndexPath)));
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_34);

            //add language to query
            if (l != 0) {
                queryString = queryString + " AND language:" + l;
            }
            QueryParser parser = new QueryParser(Version.LUCENE_34, "designation", analyzer);

            Query query = parser.parse(queryString);

            TopScoreDocCollector collector = TopScoreDocCollector.create(
                    XincoDBManager.config.MaxSearchResult, true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            for (i = 0; i < hits.length; i++) {
                try {
                    Document doc = searcher.doc(hits[i].doc);
                    v.add(new XincoCoreDataServer(Integer.parseInt(doc.get("id"))));
                } catch (Exception xcde) {
                    // don't add non-existing data
                }
                if (i >= XincoDBManager.config.MaxSearchResult) {
                    break;
                }
            }
            searcher.close();

        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (Exception se) {
                    logger.log(Level.SEVERE, null, se);
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
