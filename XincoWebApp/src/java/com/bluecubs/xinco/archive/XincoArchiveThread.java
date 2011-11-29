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
 * Name: XincoArchiveThread
 *
 * Description: handle document indexing in thread
 *
 * Original Author: Alexander Manes Date: 2005/01/16
 *
 *************************************************************
 */
package com.bluecubs.xinco.archive;

import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class runs document archiving in a separate thread (only one archiving
 * thread is allowed)
 */
public class XincoArchiveThread extends Thread {

    public static XincoArchiveThread instance = null;
    public Calendar firstRun = null;
    public Calendar lastRun = null;
    static FileInputStream fcis = null;
    static FileOutputStream fcos = null;
    private static List result;
    private static final Logger logger = Logger.getLogger(XincoArchiveThread.class.getSimpleName());

    @Override
    public void run() {
        long archive_period;
        firstRun = new GregorianCalendar();
        while (true) {
            try {
                archive_period = XincoDBManager.config.FileArchivePeriod;
                //exit archiver if period = 0
                if (archive_period == 0) {
                    break;
                }
                archiveData();
                lastRun = new GregorianCalendar();
            } catch (Exception e) {
                logger.log(Level.SEVERE, null, e);
                //continue, wait and try again...
                archive_period = 14400000;
            }
            try {
                sleep(archive_period);
            } catch (Exception se) {
                logger.log(Level.SEVERE, null, se);
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

    private XincoArchiveThread() {
    }

    @SuppressWarnings("unchecked")
    public static synchronized boolean archiveData() {
        int j;
        try {
            int querycount = 0;
            String[] query = new String[2];
            query[querycount] = "SELECT DISTINCT xcd FROM XincoCoreData xcd "
                    + "WHERE xcd.xincoCoreDataType.id = 1 "
                    + "AND xcd.statusNumber <> 3 "
                    + "ORDER BY xcd.id";
            querycount++;

            query[querycount] = "SELECT DISTINCT xcd FROM XincoCoreData xcd, XincoAddAttribute xaa1, XincoCoreLog xcl "
                    + "WHERE xcd.xincoCoreDataType.id = 1 "
                    + "AND xcd.statusNumber <> 3 "
                    + "AND xcd.id = xaa1.xincoCoreData.id "
                    + "AND xcd.id = xcl.xincoCoreData.id "
                    + "AND xaa1.xincoAddAttributePK.attributeId = 5 "
                    + "AND xaa1.attribUnsignedint = 2 "
                    + "ORDER BY xcd.id";
            querycount++;

            for (j = 0; j < querycount; j++) {
                //select data with expired archiving date
                if (j == 1) {
                    result = XincoDBManager.createdQuery(query[j]);
                    //Now process the date part of the query in plain JAVA
                    int delay = 0;
                    for (Object o : result) {
                        XincoCoreData data = (XincoCoreData) o;
                        //Get the amount of days for archival
                        for (XincoAddAttribute attr : data.getXincoAddAttributeList()) {
                            if (attr.getXincoAddAttributePK().getAttributeId() == 7) {
                                delay = (int) attr.getAttribUnsignedint();
                                break;
                            }
                        }
                        for (XincoCoreLog log : data.getXincoCoreLogList()) {
                            GregorianCalendar c = new GregorianCalendar();
                            c.setTime(log.getOpDatetime());
                            c.add(Calendar.DAY_OF_YEAR, delay);
                            if (c.getTime().before(new Date(System.currentTimeMillis()))) {
                                XincoArchiver.archiveData(new XincoCoreDataServer(data.getId()),
                                        XincoCoreNodeServer.getXincoCoreNodeParents(data.getXincoCoreNode().getId()));
                                break;
                            }
                        }
                    }
                } else {
                    result = XincoDBManager.createdQuery(query[j]);
                    for (Object o : result) {
                        XincoCoreData data = (XincoCoreData) o;
                        for (XincoAddAttribute attr : data.getXincoAddAttributeList()) {
                            if (attr.getXincoAddAttributePK().getAttributeId() == 6
                                    && attr.getAttribDatetime().before(new Date(System.currentTimeMillis()))) {
                                XincoArchiver.archiveData(new XincoCoreDataServer(data.getId()),
                                        XincoCoreNodeServer.getXincoCoreNodeParents(data.getXincoCoreNode().getId()));
                                break;
                            }
                        }
                        sleep(10000);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            try {
                if (fcis != null) {
                    fcis.close();
                }
                if (fcos != null) {
                    fcos.close();
                }
            } catch (IOException fe) {
                logger.log(Level.SEVERE, null, fe);
            }
            return false;
        }
    }
}
