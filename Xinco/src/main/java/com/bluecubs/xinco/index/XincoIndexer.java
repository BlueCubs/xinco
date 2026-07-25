/**
 * Copyright 2010 blueCubs.com
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
 * <p>Name: XincoIndexer
 *
 * <p>Description: handle document indexing
 *
 * <p>Original Author: Alexander Manes Date: 2004/10/31
 *
 * <p>Modifications:
 *
 * <p>Who? When? What? - - -
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.index;

import com.bluecubs.xinco.core.XincoCoreData;
import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import java.util.Vector;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;

/** This class handles document indexing for xinco. Edit configuration values in context.xml */
public class XincoIndexer {

  public static synchronized boolean indexXincoCoreData(
      XincoCoreData d, boolean index_content, XincoDBManager dbm) {

    IndexWriter writer = null;

    try {

      // check if document exists in index and delete
      XincoIndexer.removeXincoCoreData(d, dbm);

      // add document to index
      try {
        writer = new IndexWriter(dbm.config.FileIndexPath, new StandardAnalyzer(), false);
      } catch (Exception ie) {
        writer = new IndexWriter(dbm.config.FileIndexPath, new StandardAnalyzer(), true);
      }
      writer.addDocument(XincoDocument.getXincoDocument(d, index_content, dbm));
      // writer.optimize();
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

  public static synchronized boolean removeXincoCoreData(XincoCoreData d, XincoDBManager dbm) {

    IndexReader reader = null;

    // check if document exists in index and delete
    try {
      reader = IndexReader.open(dbm.config.FileIndexPath);
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

  public static synchronized boolean optimizeIndex(XincoDBManager dbm) {

    IndexWriter writer = null;

    try {

      // optimize index
      writer = new IndexWriter(dbm.config.FileIndexPath, new StandardAnalyzer(), false);
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

  public static synchronized Vector findXincoCoreData(String s, int l, XincoDBManager dbm) {

    int i = 0;
    Vector v = new Vector();
    Searcher searcher = null;

    try {

      searcher = new IndexSearcher(dbm.config.FileIndexPath);
      Analyzer analyzer = new StandardAnalyzer();

      // add language to query
      if (l != 0) {
        s = s + " AND language:" + l;
      }
      Query query = new QueryParser("designation", analyzer).parse(s);

      Hits hits = searcher.search(query);

      for (i = 0; i < hits.length(); i++) {
        try {
          v.addElement(new XincoCoreDataServer(Integer.parseInt(hits.doc(i).get("id")), dbm));
        } catch (Exception xcde) {
          // don't add non-existing data
        }
        if (i >= dbm.config.MaxSearchResult) {
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

  // private constructor to avoid instance generation with new-operator!
  private XincoIndexer() {}
}
