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
 * Name:            XincoCoreDataServer
 *
 * Description:     data object
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

import com.bluecubs.xinco.add.XincoAddAttribute;
import com.bluecubs.xinco.add.server.XincoAddAttributeServer;
import java.sql.*;
import java.util.Vector;
import java.io.File;
import com.bluecubs.xinco.core.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Create data object for data structures
 * @author Alexander Manes
 */
public class XincoCoreDataServer extends XincoCoreData {

    private XincoCoreUserServer user;

    /**
     * create data object for data structures
     * @param attrID Data id
     * @param DBM XincoDBManager
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreDataServer(int attrID, XincoDBManager DBM) throws XincoException {
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_data WHERE id=" + attrID);

            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setId(rs.getInt("id"));
                setXinco_core_node_id(rs.getInt("xinco_core_node_id"));
                setXinco_core_language(new XincoCoreLanguageServer(rs.getInt("xinco_core_language_id"), DBM));
                setXinco_core_data_type(new XincoCoreDataTypeServer(rs.getInt("xinco_core_data_type_id"), DBM));
                //load logs
                setXinco_core_logs(XincoCoreLogServer.getXincoCoreLogs(getId(), DBM));
                //load add attributes
                setXinco_add_attributes(XincoAddAttributeServer.getXincoAddAttributes(getId(), DBM));
                setDesignation(rs.getString("designation"));
                setStatus_number(rs.getInt("status_number"));
                //load acl for this object
                setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(rs.getInt("id"), "xinco_core_data_id", DBM));
            }

            if (RowCount < 1) {
                throw new XincoException();
            }
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.SEVERE, null, ex);
            getXinco_core_acl().removeAllElements();
            throw new XincoException();
        }
    }

    /**
     * create data object for data structures
     * @param attrID ID
     * @param attrCNID Xinco_core_node_id
     * @param attrLID Xinco_core_language_id
     * @param attrDTID Xinco_core_data_type_id
     * @param attrD Designation
     * @param attrSN Status number
     * @param DBM XincoDBManager
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreDataServer(int attrID, int attrCNID, int attrLID, int attrDTID, String attrD, int attrSN, XincoDBManager DBM) throws XincoException {
        setId(attrID);
        setXinco_core_node_id(attrCNID);
        setXinco_core_language(new XincoCoreLanguageServer(attrLID, DBM));
        setXinco_core_data_type(new XincoCoreDataTypeServer(attrDTID, DBM));
        //load logs
        setXinco_core_logs(XincoCoreLogServer.getXincoCoreLogs(attrID, DBM));
        //load add attributes
        //security: don't load add attribute, force direct access to data including check of access rights!
        setXinco_add_attributes(new Vector());
        setDesignation(attrD);
        setStatus_number(attrSN);
        //load acl for this object
        setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(getId(), "xinco_core_data_id", DBM));
    }

    /**
     * Set XincoCoreuser
     * @param user
     */
    public void setUser(XincoCoreUserServer user) {
        this.user = user;
    }

    /**
     * Persist oject to DB
     * @param DBM XincoDBManager
     * @return int
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public int write2DB(XincoDBManager DBM) throws XincoException {
        int i = 0;
        try {
            XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
            if (getId() > 0) {
                audit.updateAuditTrail("xinco_core_data", new String[]{"id =" + getId()},
                        DBM, "audit.data.change", this.getChangerID());
                DBM.executeUpdate("UPDATE xinco_core_data SET xinco_core_node_id=" +
                        getXinco_core_node_id() + ", xinco_core_language_id=" +
                        getXinco_core_language().getId() + ", xinco_core_data_type_id=" +
                        getXinco_core_data_type().getId() + ", designation='" +
                        getDesignation().replaceAll("'", "\\\\'") + "', status_number=" +
                        getStatus_number() + " WHERE id =" + getId());
            } else {
                setId(DBM.getNewID("xinco_core_data"));
                DBM.executeUpdate("INSERT INTO xinco_core_data VALUES (" + getId() + ", " + getXinco_core_node_id() + ", " + getXinco_core_language().getId() + ", " + getXinco_core_data_type().getId() + ", '" + getDesignation().replaceAll("'", "\\\\'") + "', " + getStatus_number() + ")");
                audit.updateAuditTrail("xinco_core_data", new String[]{"id =" + getId()},
                        DBM, "audit.data.create", this.getChangerID());
            }
            //write add attributes
            XincoAddAttributeServer xaas;
            for (i = 0; i < getXinco_add_attributes().size(); i++) {
                ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).setXinco_core_data_id(getId());
                //copy fields from XincoAddAttribute to XincoAddAttributeServer
                xaas = new XincoAddAttributeServer(((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getXinco_core_data_id(), ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttribute_id(), ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_int(), ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_unsignedint(), ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_double(), ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_varchar(), ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_text(), ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_datetime());
                xaas.write2DB(DBM);
            }
            DBM.getConnection().commit();
        } catch (Throwable e) {
            try {
                DBM.getConnection().rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
        return getId();
    }

    /**
     * Removes object from DB
     * @param DBM XincoDBManager
     * @param userID User id
     * @param id 
     * @throws com.bluecubs.xinco.core.XincoException
     */
    @SuppressWarnings("unchecked")
    public void removeFromDB(XincoDBManager DBM, int userID, int id) throws XincoException {
        try {
            ResultSet rs;
            Vector objects = new Vector();
            XincoCoreLogServer temp;
            // Get related logs
            rs = DBM.executeQuery("select id FROM xinco_core_log WHERE xinco_core_data_id=" + id);
            while (rs.next()) {
                temp = new XincoCoreLogServer(rs.getInt(1), DBM);
                temp.setChangerID(userID);
                objects.add(temp);
            }
            while (!objects.isEmpty()) {
                ((XincoCoreLogServer) objects.get(0)).removeFromDB(DBM);
                objects.remove(0);
            }
            objects.removeAllElements();
            XincoCoreACEServer tempACE;
            // Get related ACE's
            rs = DBM.executeQuery("select id FROM xinco_core_ace WHERE xinco_core_data_id=" + id);
            while (rs.next()) {
                tempACE = new XincoCoreACEServer(rs.getInt(1), DBM);
                tempACE.setChangerID(userID);
                objects.add(tempACE);
            }
            while (!objects.isEmpty()) {
                XincoCoreACEServer.removeFromDB(((XincoCoreACE) objects.get(0)), DBM, userID);
                objects.remove(0);
            }
            objects.removeAllElements();
            XincoAddAttributeServer tempAdd;
            // Get related ACE's
            rs = DBM.executeQuery("select attribute_id FROM xinco_add_attribute WHERE xinco_core_data_id=" + id);
            while (rs.next()) {
                tempAdd = new XincoAddAttributeServer(id, rs.getInt(1), DBM);
                tempAdd.setChangerID(userID);
                objects.add(tempAdd);
            }
            while (!objects.isEmpty()) {
                XincoAddAttributeServer.removeFromDB(((XincoAddAttributeServer) objects.get(0)).getAttribute_id(),
                        ((XincoAddAttributeServer) objects.get(0)).getXinco_core_data_id(), DBM);
                objects.remove(0);
            }
            objects.removeAllElements();
            //Now delete the data
            int i = 0;
            XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
            //delete file / file = 1
            if (getXinco_core_data_type().getId() == 1) {
                try {
                    (new File(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.getFileRepositoryPath(), getId(), "" + getId()))).delete();
                } catch (Exception dfe) {
                // continue, file might not exists
                }
                // delete revisions created upon creation or checkin
                for (i = 0; i < this.getXinco_core_logs().size(); i++) {
                    if ((((XincoCoreLog) getXinco_core_logs().elementAt(i)).getOp_code() == 1) || (((XincoCoreLog) getXinco_core_logs().elementAt(i)).getOp_code() == 5)) {
                        try {
                            (new File(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.getFileRepositoryPath(), getId(), getId() + "-" + ((XincoCoreLog) getXinco_core_logs().elementAt(i)).getId()))).delete();
                        } catch (Exception drfe) {
                        // continue, delete next revision
                        }
                    }
                }
            }
            audit.updateAuditTrail("xinco_core_data", new String[]{"id =" + getId()},
                    DBM, "audit.general.delete", this.getChangerID());
            DBM.executeUpdate("DELETE FROM xinco_core_data WHERE id=" + getId());
        } catch (Throwable e) {
            try {
                DBM.getConnection().rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
    }

    /**
     * Load binay data from XincoCoreData
     * @param xinco_core_data
     * @return
     */
    public static byte[] loadBinaryData(XincoCoreData xinco_core_data) {
        byte[] binary_data = null;
        return binary_data;
    }

    /**
     * Find XincoCoreData
     * @param designation
     * @param attrLID
     * @param attrSA
     * @param attrSFD
     * @param DBM
     * @return Vector
     */
    @SuppressWarnings("unchecked")
    public static Vector findXincoCoreData(String designation, int attrLID, boolean attrSA, boolean attrSFD, XincoDBManager DBM) {
        Vector data = new Vector();
        try {
            ResultSet rs;
            String lang = "";
            if (attrLID != 0) {
                lang = "AND (xinco_core_language_id = " + attrLID + ")";
            }
            if (attrSA) {
                rs = DBM.executeQuery("SELECT DISTINCT xinco_core_data.* FROM xinco_core_data, xinco_add_attribute WHERE (xinco_core_data.id = xinco_add_attribute.xinco_core_data_id) AND (xinco_core_data.designation LIKE '" + designation + "%' OR xinco_add_attribute.attrib_varchar LIKE '" + designation + "' OR xinco_add_attribute.attrib_text LIKE '" + designation + "') " + lang + " ORDER BY xinco_core_data.designation, xinco_core_data.xinco_core_language_id");
            } else {
                rs = DBM.executeQuery("SELECT DISTINCT xinco_core_data.* FROM xinco_core_data WHERE designation LIKE '" + designation + "' " + lang + " ORDER BY designation, xinco_core_language_id");
            }
            int i = 0;
            while (rs.next()) {
                data.add(new XincoCoreDataServer(rs.getInt("id"), DBM));
                i++;
                if (i >= DBM.config.getMaxSearchResult()) {
                    break;
                }
            }
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.SEVERE, null, ex);
            data.removeAllElements();
        }
        return data;
    }

    /**
     * Get XincoCOreData path
     * @param attrRP
     * @param attrID
     * @param attrFN
     * @return String
     */
    public static String getXincoCoreDataPath(String attrRP, int attrID, String attrFN) {
        String path = null;
        // convert ID to String
        String path4Id = "" + attrID;
        // fill ID String with zeros
        while (path4Id.length() < 10) {
            path4Id = "0" + path4Id;
        }
        // shorten to 7 chars
        path4Id = path4Id.substring(0, 7);
        // add seperator
        for (int i = 0; i < 7; i++) {
            path4Id = path4Id.substring(0, (i * 2 + 1)) + System.getProperty("file.separator") + path4Id.substring((i * 2 + 1));
        }
        // create path if neccessary
        (new File(attrRP + path4Id)).mkdirs();
        // check if file exists at NEW location (>= xinco DMS 2.0)
        if ((new File(attrRP + path4Id + attrFN)).exists()) {
            // output NEW location
            path = attrRP + path4Id + attrFN;
        } else {
            // check if file exists at OLD location (pre xinco DMS 2.0)
            if ((new File(attrRP + attrFN)).exists()) {
                // output OLD location
                path = attrRP + attrFN;
            } else {
                // output NEW location for NEW file
                path = attrRP + path4Id + attrFN;
            }
        }
        return path;
    }
}
