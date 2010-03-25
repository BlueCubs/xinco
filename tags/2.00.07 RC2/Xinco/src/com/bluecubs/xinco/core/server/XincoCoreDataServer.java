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

import com.bluecubs.xinco.add.server.XincoAddAttributeServer;
import com.bluecubs.xinco.add.XincoAddAttribute;
import java.sql.*;
import java.util.Vector;
import java.io.File;
import com.bluecubs.xinco.core.*;

public class XincoCoreDataServer extends XincoCoreData {

    private XincoCoreUserServer user;
    //create data object for data structures

    public XincoCoreDataServer(int attrID, XincoDBManager DBM) throws XincoException {

        try {

            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_data WHERE id=" + attrID);

            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setId(rs.getInt("id"));
                setXinco_core_node_id(rs.getInt("xinco_core_node_id"));
                setXinco_core_language(new XincoCoreLanguageServer(rs.getInt("xinco_core_language_id"), DBM));
                setXinco_core_data_type(new XincoCoreDataTypeServer(rs.getInt("xinco_core_data_type_id"), DBM));
                //load logs
                setXinco_core_logs(XincoCoreLogServer.getXincoCoreLogs(rs.getInt("id"), DBM));
                //load add attributes
                setXinco_add_attributes(XincoAddAttributeServer.getXincoAddAttributes(rs.getInt("id"), DBM));
                setDesignation(rs.getString("designation"));
                setStatus_number(rs.getInt("status_number"));
                //load acl for this object
                setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(rs.getInt("id"), "xinco_core_data_id", DBM));
            }
            if (RowCount < 1) {
                throw new XincoException();
            }

            stmt.close();

        } catch (Exception e) {
            getXinco_core_acl().removeAllElements();
            throw new XincoException();
        }

    }

    //create data object for data structures
    public XincoCoreDataServer(int attrID, int attrCNID, int attrLID, int attrDTID, String attrD, int attrSN, XincoDBManager DBM) throws XincoException {

        setId(attrID);
        setXinco_core_node_id(attrCNID);
        setXinco_core_language(new XincoCoreLanguageServer(attrLID, DBM));
        setXinco_core_data_type(new XincoCoreDataTypeServer(attrDTID, DBM));
        //load logs
        setXinco_core_logs(XincoCoreLogServer.getXincoCoreLogs(attrID, DBM));
        //security: don't load add attribute, force direct access to data including check of access rights!
        setXinco_add_attributes(new Vector());
        setDesignation(attrD);
        setStatus_number(attrSN);
        //load acl for this object
        setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(getId(), "xinco_core_data_id", DBM));

    }

    public void setUser(XincoCoreUserServer user) {
        this.user = user;
    }

    /**
     * This will get the latest major revision. Basically the last version
     * with a '0' as a mid version
     * @param xinco_core_data_id 
     * @param DBM
     * @return Path to last major version
     * @throws SQLException
     * @throws XincoException
     */
    public static String getLastMajorVersionDataPath(int xinco_core_data_id, XincoDBManager DBM) throws SQLException, XincoException {
        Statement stmt;
        stmt = DBM.con.createStatement();
        ResultSet rs = stmt.executeQuery("select id from xinco.xinco_core_log " +
                "where xinco_core_data_id = " + xinco_core_data_id + " and version_mid='0' " +
                "and op_code=" + (OPCode.CHECKIN.ordinal() + 1) + " order by id desc");
        if (rs.next()) {
            XincoCoreLogServer log = new XincoCoreLogServer(rs.getInt("id"), DBM);
            System.out.println("Getting path: " + XincoCoreDataServer.getXincoCoreDataPath(DBM.config.FileRepositoryPath, xinco_core_data_id, xinco_core_data_id + "-" + log.getId()));
            return XincoCoreDataServer.getXincoCoreDataPath(DBM.config.FileRepositoryPath, xinco_core_data_id, xinco_core_data_id + "-" + log.getId());
        } else {
            throw new XincoException("No major log history for XincoCoreData with id: " + xinco_core_data_id);
        }
    }

    //write to db
    public int write2DB(XincoDBManager DBM) throws XincoException {
        int i = 0;
        try {

            Statement stmt;

            if (getId() > 0) {
                stmt = DBM.con.createStatement();
                XincoCoreAuditServer audit = new XincoCoreAuditServer();
                audit.updateAuditTrail("xinco_core_data", new String[]{"id =" + getId()},
                        DBM, "audit.data.change", this.getChangerID());
                stmt.executeUpdate("UPDATE xinco_core_data SET xinco_core_node_id=" +
                        getXinco_core_node_id() + ", xinco_core_language_id=" +
                        getXinco_core_language().getId() + ", xinco_core_data_type_id=" +
                        getXinco_core_data_type().getId() + ", designation='" +
                        getDesignation().replaceAll("'", "\\\\'") + "', status_number=" +
                        getStatus_number() + " WHERE id =" + getId());
                stmt.close();

            } else {
                setId(DBM.getNewID("xinco_core_data"));
                stmt = DBM.con.createStatement();
                stmt.executeUpdate("INSERT INTO xinco_core_data VALUES (" + getId() + ", " + getXinco_core_node_id() + ", " + getXinco_core_language().getId() + ", " + getXinco_core_data_type().getId() + ", '" + getDesignation().replaceAll("'", "\\\\'") + "', " + getStatus_number() + ")");
                stmt.close();
            }

            //write add attributes
            stmt = DBM.con.createStatement();
            stmt.executeUpdate("DELETE FROM xinco_add_attribute WHERE xinco_core_data_id=" + getId());
            stmt.close();
            XincoAddAttributeServer xaas;
            for (i = 0; i < getXinco_add_attributes().size(); i++) {
                ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).setXinco_core_data_id(getId());
                //copy fields from XincoAddAttribute to XincoAddAttributeServer
                xaas = new XincoAddAttributeServer(((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getXinco_core_data_id(), ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttribute_id(), ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_int(), ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_unsignedint(), ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_double(), ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_varchar(), ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_text(), ((XincoAddAttribute) getXinco_add_attributes().elementAt(i)).getAttrib_datetime());
                xaas.write2DB(DBM);
            }
            DBM.con.commit();

        } catch (Exception e) {
            try {
                DBM.con.rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
        return getId();
    }

    public static void removeFromDB(XincoDBManager DBM, int userID, int id) throws XincoException {
        try {
            Statement stmt;
            XincoCoreAuditServer audit = new XincoCoreAuditServer();
            /*
             * Aduit Trail Table (*_t) cannot handle multiple row changes!!!
            audit.updateAuditTrail("xinco_core_log",new String [] {"id ="+id},
            DBM,"audit.general.delete",userID);
             */
            stmt = DBM.con.createStatement();
            stmt.executeUpdate("DELETE FROM xinco_core_log WHERE xinco_core_data_id=" + id);
            stmt.close();
            /*
             * Aduit Trail Table (*_t) cannot handle multiple row changes!!!
            audit.updateAuditTrail("xinco_core_ace",new String [] {"id ="+id},
            DBM,"audit.general.delete",userID);
             */
            stmt = DBM.con.createStatement();
            stmt.executeUpdate("DELETE FROM xinco_core_ace WHERE xinco_core_data_id=" + id);
            stmt.close();
            /*
             * Aduit Trail Table (*_t) cannot handle multiple row changes!!!
            audit.updateAuditTrail("xinco_add_attribute",new String [] {"id ="+id},
            DBM,"audit.general.delete",userID);
             */
            stmt = DBM.con.createStatement();
            stmt.executeUpdate("DELETE FROM xinco_add_attribute WHERE xinco_core_data_id=" + id);
            stmt.close();
        } catch (Exception e) {
            try {
                DBM.con.rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
    }

    //delete from db
    public void deleteFromDB(XincoDBManager DBM) throws XincoException {

        int i = 0;

        try {
            Statement stmt;
            XincoCoreAuditServer audit = new XincoCoreAuditServer();
            //delete file / file = 1
            if (getXinco_core_data_type().getId() == 1) {
                try {
                    (new File(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.FileRepositoryPath, getId(), "" + getId()))).delete();
                } catch (Exception dfe) {
                    // continue, file might not exists
                }
                // delete revisions created upon creation or checkin
                for (i = 0; i < this.getXinco_core_logs().size(); i++) {
                    if ((((XincoCoreLog) getXinco_core_logs().elementAt(i)).getOp_code() == 1) || (((XincoCoreLog) getXinco_core_logs().elementAt(i)).getOp_code() == 5)) {
                        try {
                            (new File(XincoCoreDataServer.getXincoCoreDataPath(DBM.config.FileRepositoryPath, getId(), getId() + "-" + ((XincoCoreLog) getXinco_core_logs().elementAt(i)).getId()))).delete();
                        } catch (Exception drfe) {
                            // continue, delete next revision
                        }
                    }
                }
            }
            audit.updateAuditTrail("xinco_core_data", new String[]{"id =" + getId()},
                    DBM, "audit.general.delete", this.getChangerID());
            stmt = DBM.con.createStatement();
            stmt.executeUpdate("DELETE FROM xinco_core_data WHERE id=" + getId());
            stmt.close();
            DBM.con.commit();
        } catch (Exception e) {
            try {
                DBM.con.rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }

    }

    public static byte[] loadBinaryData(XincoCoreData attrCD) {

        byte[] binary_data = null;

        return binary_data;

    }

    public static int saveBinaryData(XincoCoreData attrCD, byte[] attrBD) {

        return 0;

    }

    public static Vector findXincoCoreData(String attrS, int attrLID, boolean attrSA, boolean attrSFD, XincoDBManager DBM) {
        Vector data = new Vector();
        try {

            Statement stmt = DBM.con.createStatement();
            ResultSet rs;
            String lang = "";
            if (attrLID != 0) {
                lang = "AND (xinco_core_language_id = " + attrLID + ")";
            }
            if (attrSA) {
                rs = stmt.executeQuery("SELECT DISTINCT xinco_core_data.* " +
                        "FROM xinco_core_data, xinco_add_attribute WHERE " +
                        "(xinco_core_data.id = xinco_add_attribute.xinco_core_data_id) " +
                        "AND (xinco_core_data.designation LIKE '" + attrS +
                        "%' OR xinco_add_attribute.attrib_varchar LIKE '" + attrS +
                        "' OR xinco_add_attribute.attrib_text LIKE '" + attrS + "') " + lang +
                        " ORDER BY xinco_core_data.designation, xinco_core_data.xinco_core_language_id");
            } else {
                rs = stmt.executeQuery("SELECT DISTINCT xinco_core_data.* FROM xinco_core_data " +
                        "WHERE designation LIKE '" + attrS + "' " + lang +
                        " ORDER BY designation, xinco_core_language_id");
            }

            int i = 0;
            while (rs.next()) {
                data.addElement(new XincoCoreDataServer(rs.getInt("id"), DBM));
                i++;
                if (i >= DBM.config.MaxSearchResult) {
                    break;
                }
            }

            stmt.close();

        } catch (Exception e) {
            data.removeAllElements();
        }

        return data;

    }

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
            path4Id = path4Id.substring(0, (i * 2 + 1)) +
                    System.getProperty("file.separator") + path4Id.substring((i * 2 + 1));
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
