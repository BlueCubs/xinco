/**
 *Copyright 2010 blueCubs.com
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
 * Name:            XincoArchiveTimerTask
 *
 * Description:     handle document indexing in thread
 *
 * Original Author: Alexander Manes
 * Date:            2005/01/16
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.archive;

import static com.bluecubs.xinco.core.server.XincoCoreNodeServer.getXincoCoreNodeParents;
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;

import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.bluecubs.xinco.core.server.XincoDBManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bluecubs.xinco.core.XincoException;

/**
 * This class runs document archiving in a separate thread
 * (only one archiving thread is allowed)
 */
public class XincoArchiveTimerTask extends TimerTask {

    public Calendar firstRun = null;
    public Calendar lastRun = null;
    static XincoCoreDataServer xdata_temp = null;
    static FileInputStream fcis = null;
    static FileOutputStream fcos = null;

    @Override
    public void run() {
        long archive_period = 14400000; // Every 4 hours
        firstRun = new GregorianCalendar();
        try {
            XincoDBManager dbm = null;
            dbm = new XincoDBManager();
            archive_period = dbm.config.FileArchivePeriod;
            //exit archiver if period = 0
            if (archive_period == 0) {
                return;
            }
            boolean isDataArchived = archiveData(dbm);
            /*Logger.getLogger(XincoArchiveTimerTask.class.getSimpleName()).log(
                    Level.INFO, "isDataArchived = " + isDataArchived);*/
            lastRun = new GregorianCalendar();
            dbm.con.close();
            dbm = null;
            cancel();
        } catch (Exception e) {
            cancel();
        }
        return;
    }

    @SuppressWarnings("unchecked")
    public synchronized boolean archiveData(XincoDBManager DBM) {
        int j;
        try {
            int querycount = 0;
            String[] query = new String[2];
            query[querycount] = "SELECT DISTINCT xcd.id FROM xinco_core_data xcd, xinco_add_attribute xaa1, xinco_add_attribute xaa2 "
                    + "WHERE xcd.xinco_core_data_type_id = 1 "
                    + "AND xcd.status_number <> 3 "
                    + "AND xcd.id = xaa1.xinco_core_data_id "
                    + "AND xcd.id = xaa2.xinco_core_data_id "
                    + "AND xaa1.attribute_id = 5 "
                    + "AND xaa1.attrib_unsignedint = 1 "
                    + "AND xaa2.attribute_id = 6 "
                    + "AND xaa2.attrib_datetime < now() "
                    + "ORDER BY xcd.id";
            querycount++;

            query[querycount] = "SELECT DISTINCT xcd.id FROM xinco_core_data xcd, xinco_add_attribute xaa1, xinco_add_attribute xaa2, xinco_core_log xcl "
                    + "WHERE xcd.xinco_core_data_type_id = 1 "
                    + "AND xcd.status_number <> 3 "
                    + "AND xcd.id = xaa1.xinco_core_data_id "
                    + "AND xcd.id = xaa2.xinco_core_data_id "
                    + "AND xcd.id = xcl.xinco_core_data_id "
                    + "AND xaa1.attribute_id = 5 "
                    + "AND xaa1.attrib_unsignedint = 2 "
                    + "AND xaa2.attribute_id = 7 "
                    + "AND ADDDATE(DATE(xcl.op_datetime), xaa2.attrib_unsignedint) < now() "
                    + "ORDER BY xcd.id";
            querycount++;

            for (j = 0; j < querycount; j++) {

                Statement stmt = DBM.con.createStatement();
                //select data with expired archiving date
                ResultSet rs = stmt.executeQuery(query[j]);
                while (rs.next()) {
                    xdata_temp = new XincoCoreDataServer(rs.getInt("xcd.id"), DBM);
                    XincoArchiver.archiveData(xdata_temp,
                            getXincoCoreNodeParents(
                            xdata_temp.getXinco_core_node_id(), DBM), DBM);
                    cancel();
                }
                stmt.close();
            }
            return true;
        } catch (XincoException e) {
            getLogger(XincoArchiveTimerTask.class.getSimpleName()).log(INFO, null, e);
            try {
                if (fcis != null) {
                    fcis.close();
                }
                if (fcos != null) {
                    fcos.close();
                }
            } catch (IOException fe) {
            }
            return false;
        }
    catch (IOException e)
    {
      Logger.getLogger(XincoArchiveTimerTask.class.getSimpleName()).log(
              Level.INFO, null, e);
      try {
        if (fcis != null) {
          fcis.close();
        }
        if (fcos != null) {
          fcos.close();
        }
      } catch (Exception fe) {
      }
      return false;
    }
    catch (SQLException e)
    {
      Logger.getLogger(XincoArchiveTimerTask.class.getSimpleName()).log(
              Level.INFO, null, e);
      try {
        if (fcis != null) {
          fcis.close();
        }
        if (fcos != null) {
          fcos.close();
        }
      } catch (Exception fe) {
      }
      return false;
    }

    }
}
