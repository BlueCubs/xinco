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
 * Name:            XincoCoreLanguageServer
 *
 * Description:     language
 *
 * Original Author: Alexander Manes
 * Date:            2004
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import java.sql.*;
import java.util.Vector;

import com.bluecubs.xinco.core.*;
import java.util.ResourceBundle;

/**
 * Create language object for data structures
 * @author Alexander Manes
 */
public class XincoCoreLanguageServer extends XincoCoreLanguage {

    /**
     * Create language object for data structures
     * @param id
     * @param DBM
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreLanguageServer(int id, XincoDBManager DBM) throws XincoException {
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_language WHERE id=" + id);
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setId(rs.getInt("id"));
                setSign(rs.getString("sign"));
                setDesignation(rs.getString("designation"));
            }
            if (RowCount < 1) {
                throw new XincoException();
            }
        } catch (Throwable e) {
            throw new XincoException();
        }
    }

    /**
     * Create language object for data structures
     * @param id
     * @param sign
     * @param designation
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreLanguageServer(int id, String sign, String designation) throws XincoException {
        setId(id);
        setSign(sign);
        setDesignation(designation);
    }

    /**
     * Write to DB
     * @param DBM
     * @return int
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public int write2DB(XincoDBManager DBM) throws XincoException {
        try {
            if (getId() > 0) {
                XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
                ResourceBundle xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
                audit.updateAuditTrail("xinco_core_language", new String[]{"id =" + getId()},
                        DBM, xerb.getString("audit.language.change"), this.getChangerID());
                DBM.executeUpdate("UPDATE xinco_core_language SET sign='" + getSign().replaceAll("'", "\\\\'") + "', designation='" + getDesignation().replaceAll("'", "\\\\'") + "' WHERE id=" + getId());
            } else {
                setId(DBM.getNewID("xinco_core_language"));
                DBM.executeUpdate("INSERT INTO xinco_core_language VALUES (" + getId() + ", '" + getSign().replaceAll("'", "\\\\'") + "', '" + getDesignation().replaceAll("'", "\\\\'") + "')");
            }
        } catch (Throwable e) {
            throw new XincoException();
        }
        return getId();
    }

    /**
     * Remove from DB
     * @param xinco_core_language
     * @param DBM
     * @param userID
     * @return
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public static int removeFromDB(XincoCoreLanguage xinco_core_language, XincoDBManager DBM, int userID) throws XincoException {
        try {
            XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
            audit.updateAuditTrail("xinco_core_language", new String[]{"id =" + xinco_core_language.getId()},
                    DBM, "audit.general.delete", userID);
            DBM.executeUpdate("DELETE FROM xinco_core_language WHERE id=" + xinco_core_language.getId());
        } catch (Throwable e) {
            throw new XincoException();
        }
        return 0;
    }

    /**
     * Create language object for data structures
     * @param DBM
     * @return Vector
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreLanguages(XincoDBManager DBM) {
        Vector coreLanguages = new Vector();
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_language ORDER BY designation");
            while (rs.next()) {
                coreLanguages.add(new XincoCoreLanguageServer(rs.getInt("id"), rs.getString("sign"), rs.getString("designation")));
            }
        } catch (Throwable e) {
            coreLanguages.removeAllElements();
        }
        return coreLanguages;
    }

    /**
     * Check if language is in use by other objects
     * @param xcl
     * @param DBM
     * @return boolean
     */
    public static boolean isLanguageUsed(XincoCoreLanguage xcl, XincoDBManager DBM) {
        boolean is_used = false;
        try {
            ResultSet rs = null;
            rs = DBM.executeQuery("SELECT 1 FROM xinco_core_node WHERE xinco_core_language_id = " + xcl.getId());
            while (rs.next()) {
                is_used = true;
            }
            if (!is_used) {
                rs = DBM.executeQuery("SELECT 1 FROM xinco_core_data WHERE xinco_core_language_id = " + xcl.getId());
                while (rs.next()) {
                    is_used = true;
                }
            }
        } catch (Throwable e) {
            is_used = true; // rather lock language in case of error!
        }
        return is_used;
    }
}
