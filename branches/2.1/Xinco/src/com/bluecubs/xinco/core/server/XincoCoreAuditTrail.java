/**
 *Copyright 2004 blueCubs.com
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
 * Name:            XincoCoreAuditTrail
 *
 * Description:     Audit trail entity
 *
 * Original Author: Javier A. Ortiz
 * Date:            2006
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates a new instance of XincoCoreAuditTrail
 * This class manages the audit trail system.
 * @author Javier A. Ortiz
 */
public class XincoCoreAuditTrail {

    /**
     * Constructor
     */
    public XincoCoreAuditTrail() {
    }

    /**
     * Update the audit trail system
     * @param table Original table name
     * @param keys keys to be used in the query as parameters in the 'where' section.
     * @param DBM XincoDBManager
     * @param reason Audit trail reason for change
     * @param id User id making the change.
     */
    public void updateAuditTrail(String table, String[] keys, XincoDBManager DBM, String reason, int id) {
        try {
            //"Copy and Paste" the original record in the audit tables
            String where = "";
            for (int i = 0; i < keys.length; i++) {
                where += keys[i];
                if (i < keys.length - 1) {
                    where += " and ";
                }
            }
            int record_ID = 0;
            String sql = "select * from " + table + " where " + where;
            ResultSet rs = DBM.executeQuery(sql);
            try {
                record_ID = DBM.getNewID("xinco_core_user_modified_record");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (rs.next()) {
                sql = "insert into " + table + "_t values('" + record_ID + "', ";
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if (rs.getString(i) == null) {
                        sql += rs.getString(i);
                    } else {
                        sql += "'" + rs.getString(i) + "'";
                    }
                    if (i < rs.getMetaData().getColumnCount()) {
                        sql += ", ";
                    } else {
                        sql += ")";
                    }
                }
                DBM.executeUpdate(sql);
            }
            sql = "insert into xinco_core_user_modified_record (id, record_id, mod_Time, " +
                    "mod_Reason) values (" + id + ", " + record_ID + ", '" +
                    new Timestamp(System.currentTimeMillis()) + "', '" + reason + "')";
            DBM.executeUpdate(sql);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreAuditTrail.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
}
