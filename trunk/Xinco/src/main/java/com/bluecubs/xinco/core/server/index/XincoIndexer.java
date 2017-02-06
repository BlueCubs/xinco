/**
 * Copyright 2012 blueCubs.com
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
 * $Author$
 * $Date$
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server.index;

import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import static com.bluecubs.xinco.core.server.XincoDBManager.CONFIG;
import static com.bluecubs.xinco.core.server.index.XincoDocument.getXincoDocument;
import com.bluecubs.xinco.core.server.service.XincoCoreData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import static org.apache.lucene.index.IndexReader.open;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import static org.apache.lucene.search.TopScoreDocCollector.create;
import static org.apache.lucene.store.FSDirectory.open;
import org.apache.lucene.util.Version;
import static org.apache.lucene.util.Version.LUCENE_34;

/**
 * This class handles document indexing for Xinco. Edit configuration values in
 * database
 */
public class XincoIndexer {

    private static final Logger LOG
            = getLogger(XincoIndexer.class.getSimpleName());
    private static final Version VERSION = LUCENE_34;

    public static synchronized boolean indexXincoCoreData(XincoCoreData d,
            boolean index_content) {
        IndexWriter writer = null;
        try {
            //check if document exists in index and delete
            removeXincoCoreData(d);
            //add document to index
            writer = new IndexWriter(
                    open(new File(CONFIG.fileIndexPath)),
                    new IndexWriterConfig(VERSION,
                            new StandardAnalyzer(VERSION)));
            writer.addDocument(getXincoDocument(d, index_content));
            //writer.optimize();
            writer.close();
        } catch (IOException e) {
            LOG.log(SEVERE, null, e);
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException we) {
                    LOG.log(SEVERE, null, we);
                }
            }
            return false;
        }
        return true;
    }

    public static synchronized boolean removeXincoCoreData(XincoCoreData d) {
        IndexReader reader = null;
        IndexWriter writer = null;
        File indexDirectory = new File(CONFIG.fileIndexPath);
        if (indexDirectory.exists()) {
            //check if document exists in index and delete
            try {
                reader = open(open(new File(CONFIG.fileIndexPath)));
                writer = new IndexWriter(
                        open(new File(CONFIG.fileIndexPath)),
                        new IndexWriterConfig(VERSION,
                                new StandardAnalyzer(VERSION)));
                writer.deleteDocuments(new Term("id", "" + d.getId()));
                reader.close();
                writer.close();
            } catch (IOException re) {
                LOG.log(SEVERE, null, re);
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException re2) {
                        LOG.log(SEVERE, null, re2);
                    }
                }
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException re2) {
                        LOG.log(SEVERE, null, re2);
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
            writer = new IndexWriter(
                    open(new File(CONFIG.fileIndexPath)),
                    new IndexWriterConfig(VERSION,
                            new StandardAnalyzer(VERSION)));
            writer.close();
            result = true;
        } catch (FileNotFoundException e) {
            LOG.log(INFO, "No index found at: {0}. Nothing to index.",
                    CONFIG.fileIndexPath);
        } catch (IOException e) {
            LOG.log(SEVERE, null, e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException we) {
                    LOG.log(SEVERE, null, we);
                }
            }
        }
        return result;
    }

    public static synchronized ArrayList findXincoCoreData(String queryString,
            int l) {
        int i;
        ArrayList<XincoCoreData> v = new ArrayList<>();
        IndexSearcher searcher = null;
        IndexReader reader = null;
        try {
            reader = open(open(new File(CONFIG.fileIndexPath)));
            searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer(VERSION);

            //add language to query
            if (l != 0) {
                queryString = queryString + " AND language:" + l;
            }
            QueryParser parser = new QueryParser(VERSION, "designation",
                    analyzer);

            Query query = parser.parse(queryString);

            TopScoreDocCollector collector = create(CONFIG.getMaxSearchResult(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            for (i = 0; i < hits.length; i++) {
                try {
                    Document doc = searcher.doc(hits[i].doc);
                    v.add(new XincoCoreDataServer(parseInt(doc.get("id"))));
                } catch (IOException | NumberFormatException xcde) {
                    // don't add non-existing data
                    LOG.log(WARNING, null, xcde);
                }
                if (i >= CONFIG.getMaxSearchResult()) {
                    break;
                }
            }
            searcher.close();

        } catch (IOException | ParseException e) {
            LOG.log(SEVERE, null, e);
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException re2) {
                    LOG.log(SEVERE, null, re2);
                }
            }
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (IOException se) {
                    LOG.log(SEVERE, null, se);
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
