/**
 * Copyright 2012 blueCubs.com
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
 * <p>Name: XincoArchiveThread
 *
 * <p>Description: handle document indexing in thread
 *
 * <p>Original Author: Alexander Manes Date: 2005/01/16
 *
 * <p>************************************************************
 */
package com.bluecubs.xinco.archive;

import static com.bluecubs.xinco.core.server.XincoCoreNodeServer.getXincoCoreNodeParents;
import static com.bluecubs.xinco.core.server.XincoDBManager.CONFIG;
import static com.bluecubs.xinco.core.server.XincoDBManager.createdQuery;
import static java.lang.System.currentTimeMillis;
import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import com.bluecubs.xinco.core.server.persistence.XincoCoreLog;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class runs document archiving in a separate thread (only one archiving thread is allowed)
 */
public final class XincoArchiveThread extends Thread {

  public static XincoArchiveThread instance = null;
  public Calendar firstRun = null;
  public Calendar lastRun = null;
  static FileInputStream fcis = null;
  static FileOutputStream fcos = null;
  private static List result;
  private static final Logger LOG = getLogger(XincoArchiveThread.class.getSimpleName());

  @Override
  public void run() {
    long archive_period;
    firstRun = new GregorianCalendar();
    while (true) {
      try {
        archive_period = CONFIG.fileArchivePeriod;
        // exit archiver if period = 0
        if (archive_period == 0) {
          break;
        }
        archiveData();
        lastRun = new GregorianCalendar();
      } catch (Exception e) {
        LOG.log(SEVERE, null, e);
        // continue, wait and try again...
        archive_period = 14_400_000;
      }
      try {
        sleep(archive_period);
      } catch (InterruptedException se) {
        LOG.log(SEVERE, null, se);
        break;
      }
    }
  }

  public static XincoArchiveThread getInstance() {
    if (instance == null) {
      instance = new XincoArchiveThread();
    }
    return instance;
  }

  private XincoArchiveThread() {}

  @SuppressWarnings("unchecked")
  public static synchronized boolean archiveData() {
    int j;
    try {
      int querycount = 0;
      String[] query = new String[2];
      query[querycount] =
          "SELECT DISTINCT xcd FROM XincoCoreData xcd "
              + "WHERE xcd.xincoCoreDataType.id = 1 "
              + "AND xcd.statusNumber <> 3 "
              + "ORDER BY xcd.id";
      querycount++;

      query[querycount] =
          "SELECT DISTINCT xcd FROM XincoCoreData xcd, XincoAddAttribute xaa1, XincoCoreLog xcl "
              + "WHERE xcd.xincoCoreDataType.id = 1 "
              + "AND xcd.statusNumber <> 3 "
              + "AND xcd.id = xaa1.xincoCoreData.id "
              + "AND xcd.id = xcl.xincoCoreData.id "
              + "AND xaa1.xincoAddAttributePK.attributeId = 5 "
              + "AND xaa1.attribUnsignedint = 2 "
              + "ORDER BY xcd.id";
      querycount++;

      for (j = 0; j < querycount; j++) {
        // select data with expired archiving date
        if (j == 1) {
          result = createdQuery(query[j]);
          // Now process the date part of the query in plain JAVA
          int delay = 0;
          for (Object o : result) {
            XincoCoreData data = (XincoCoreData) o;
            // Get the amount of days for archival
            for (XincoAddAttribute attr : data.getXincoAddAttributeList()) {
              if (attr.getXincoAddAttributePK().getAttributeId() == 7) {
                delay = (int) attr.getAttribUnsignedint();
                break;
              }
            }
            for (XincoCoreLog log : data.getXincoCoreLogList()) {
              GregorianCalendar c = new GregorianCalendar();
              c.setTime(log.getOpDatetime());
              c.add(DAY_OF_YEAR, delay);
              if (c.getTime().before(new Date(currentTimeMillis()))) {
                XincoArchiver.archiveData(
                    new XincoCoreDataServer(data.getId()),
                    getXincoCoreNodeParents(data.getXincoCoreNode().getId()));
                break;
              }
            }
          }
        } else {
          result = createdQuery(query[j]);
          for (Object o : result) {
            XincoCoreData data = (XincoCoreData) o;
            for (XincoAddAttribute attr : data.getXincoAddAttributeList()) {
              if (attr.getXincoAddAttributePK().getAttributeId() == 6
                  && attr.getAttribDatetime().before(new Date(currentTimeMillis()))) {
                XincoArchiver.archiveData(
                    new XincoCoreDataServer(data.getId()),
                    getXincoCoreNodeParents(data.getXincoCoreNode().getId()));
                break;
              }
            }
          }
        }
      }
      return true;
    } catch (IOException e) {
      LOG.log(SEVERE, null, e);
      return false;
    } finally {
      try {
        if (fcis != null) {
          fcis.close();
        }
        if (fcos != null) {
          fcos.close();
        }
      } catch (IOException fe) {
        LOG.log(SEVERE, null, fe);
      }
    }
  }
}