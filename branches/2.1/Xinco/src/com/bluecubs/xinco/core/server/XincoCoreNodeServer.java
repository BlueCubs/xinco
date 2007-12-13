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
 * Name:            XincoCoreNodeServer
 *
 * Description:     node object
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
import com.bluecubs.xinco.index.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Create node object for data structures
 * @author Alexander Manes
 */
public class XincoCoreNodeServer extends XincoCoreNode {

    /**
     * Create node object for data structures
     * @param attrID
     * @param DBM
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreNodeServer(int attrID, XincoDBManager DBM) throws XincoException {
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_node WHERE id=" + attrID);
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setId(rs.getInt("id"));
                setXinco_core_node_id(rs.getInt("xinco_core_node_id"));
                setXinco_core_language(new XincoCoreLanguageServer(rs.getInt("xinco_core_language_id"), DBM));
                setDesignation(rs.getString("designation"));
                setStatus_number(rs.getInt("status_number"));
                setXinco_core_nodes(new Vector());
                setXinco_core_data(new Vector());
                //load acl for this object
                setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(rs.getInt("id"), "xinco_core_node_id", DBM));
            }
            if (RowCount < 1) {
                throw new XincoException();
            }
        } catch (Throwable e) {
            setXinco_core_language(null);
            getXinco_core_acl().removeAllElements();
            getXinco_core_nodes().removeAllElements();
            getXinco_core_data().removeAllElements();
            e.printStackTrace();
            throw new XincoException();
        }
    }

    /**
     * Create node object for data structures
     * @param attrID
     * @param attrCNID
     * @param attrLID 
     * @param attrD 
     * @param attrSN
     * @param DBM
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreNodeServer(int attrID, int attrCNID, int attrLID, String attrD, int attrSN, XincoDBManager DBM) throws XincoException {
        try {
            setId(attrID);
            setXinco_core_node_id(attrCNID);
            setXinco_core_language(new XincoCoreLanguageServer(attrLID, DBM));
            setDesignation(attrD);
            setStatus_number(attrSN);
            setXinco_core_nodes(new Vector());
            setXinco_core_data(new Vector());
            //load acl for this object
            setXinco_core_acl(XincoCoreACEServer.getXincoCoreACL(getId(), "xinco_core_node_id", DBM));
        } catch (Throwable e) {
            setXinco_core_language(null);
            getXinco_core_acl().removeAllElements();
            getXinco_core_nodes().removeAllElements();
            getXinco_core_data().removeAllElements();
            if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.WARNING, "Error creating node.",e);
            }
            throw new XincoException();
        }
    }

    /**
     * Write to DB
     * @param DBM
     * @return int
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public int write2DB(XincoDBManager DBM) throws XincoException {
        try {
            String xcnid = "";
            if (getId() > 0) {
                //set values of nullable attributes
                if (getXinco_core_node_id() == 0) {
                    xcnid = "NULL";
                } else {
                    xcnid = "" + getXinco_core_node_id();
                }
                XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
                audit.updateAuditTrail("xinco_core_node", new String[]{"id =" + getId()},
                        DBM, "audit.corenode.change", this.getChangerID());
                DBM.executeUpdate("UPDATE xinco_core_node SET xinco_core_node_id=" + xcnid + ", xinco_core_language_id=" + getXinco_core_language().getId() + ", designation='" + getDesignation().replaceAll("'", "\\\\'") + "', status_number=" + getStatus_number() + " WHERE id=" + getId());
            } else {
                setId(DBM.getNewID("xinco_core_node"));
                //set values of nullable attributes
                if (getXinco_core_node_id() == 0) {
                    xcnid = "NULL";
                } else {
                    xcnid = "" + getXinco_core_node_id();
                }
                DBM.executeUpdate("INSERT INTO xinco_core_node VALUES (" + getId() + ", " + getXinco_core_node_id() + ", " + getXinco_core_language().getId() + ", '" + getDesignation().replaceAll("'", "\\\\'") + "', " + getStatus_number() + ")");
            }
        } catch (Throwable e) {
            throw new XincoException();
        }
        return getId();
    }

    /**
     * Remove from DB
     * @param delete_this
     * @param DBM
     * @param userID
     * @throws com.bluecubs.xinco.core.XincoException
     */
    @SuppressWarnings("static-access")
    public void removeFromDB(boolean delete_this, XincoDBManager DBM, int userID) throws XincoException {
        int i = 0;
        try {
            //fill nodes and data
            fillXincoCoreNodes(DBM);
            fillXincoCoreData(DBM);
            //start recursive deletion
            for (i = 0; i < getXinco_core_nodes().size(); i++) {
                ((XincoCoreNodeServer) getXinco_core_nodes().elementAt(i)).removeFromDB(true, DBM, userID);
            }
            for (i = 0; i < getXinco_core_data().size(); i++) {
                XincoIndexer.removeXincoCoreData((XincoCoreDataServer) getXinco_core_data().elementAt(i), DBM);
                ((XincoCoreDataServer) getXinco_core_data().elementAt(i)).setChangerID(userID);
                ((XincoCoreDataServer) getXinco_core_data().elementAt(i)).removeFromDB(DBM, userID,
                        ((XincoCoreDataServer) getXinco_core_data().elementAt(i)).getId());
            }
            if (delete_this) {
                XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
                ResultSet rs = DBM.executeQuery("select id FROM xinco_core_ace WHERE xinco_core_node_id=" + getId());
                while (rs.next()) {
                    XincoCoreACEServer temp = new XincoCoreACEServer(rs.getInt(1), DBM);
                    temp.removeFromDB(temp, DBM, userID);
                }
                audit.updateAuditTrail("xinco_core_node", new String[]{"id =" + getId()},
                        DBM, "audit.general.delete", userID);
                DBM.executeUpdate("DELETE FROM xinco_core_node WHERE id=" + getId());
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new XincoException();
        }
    }

    /**
     * Fill XIncoCoreNodes vector for this Node
     * @param DBM
     */
    @SuppressWarnings("unchecked")
    public void fillXincoCoreNodes(XincoDBManager DBM) {
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_node WHERE xinco_core_node_id = " + getId() + " ORDER BY designation");
            while (rs.next()) {
                getXinco_core_nodes().add(new XincoCoreNodeServer(rs.getInt("id"), rs.getInt("xinco_core_node_id"), rs.getInt("xinco_core_language_id"), rs.getString("designation"), rs.getInt("status_number"), DBM));
            }
        } catch (Throwable e) {
            getXinco_core_nodes().removeAllElements();
        }
    }

    /**
     * Fill XIncoCoreData vector for this Node
     * @param DBM
     */
    @SuppressWarnings("unchecked")
    public void fillXincoCoreData(XincoDBManager DBM) {
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_data WHERE xinco_core_node_id = " + getId() + " ORDER BY designation");
            while (rs.next()) {
                getXinco_core_data().add(new XincoCoreDataServer(rs.getInt("id"), rs.getInt("xinco_core_node_id"), rs.getInt("xinco_core_language_id"), rs.getInt("xinco_core_data_type_id"), rs.getString("designation"), rs.getInt("status_number"), DBM));
            }
        } catch (Throwable e) {
            getXinco_core_data().removeAllElements();
        }
    }

    /**
     * Find XincoCoreNodes based on parameters
     * @param designation
     * @param language_id
     * @param DBM
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Vector findXincoCoreNodes(String designation, int language_id, XincoDBManager DBM) {
        Vector nodes = null;
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_node WHERE xinco_core_language_id = " + language_id + " AND designation LIKE '" + designation + "%' ORDER BY designation, xinco_core_language_id");
            int i = 0;
            while (rs.next()) {
                nodes.add(new XincoCoreNodeServer(rs.getInt("id"), rs.getInt("xinco_core_node_id"), rs.getInt("xinco_core_language_id"), rs.getString("designation"), rs.getInt("status_number"), DBM));
                i++;
                if (i >= DBM.config.getMaxSearchResult()) {
                    break;
                }
            }
        } catch (Throwable e) {
            nodes.removeAllElements();
        }
        return nodes;
    }

    /**
     * Get parents of this node
     * @param id 
     * @param DBM
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreNodeParents(int id, XincoDBManager DBM) {
        Vector nodes = new Vector();
        try {
            ResultSet rs = null;
            while (id > 0) {
                rs = DBM.executeQuery("SELECT * FROM xinco_core_node WHERE id = " + id);
                while (rs.next()) {
                    nodes.addElement(new XincoCoreNodeServer(rs.getInt("id"), DBM));
                    if (id > 1) {
                        id = rs.getInt("xinco_core_node_id");
                    } else {
                        id = 0;
                    }
                }
            }
        } catch (Throwable e) {
            nodes.removeAllElements();
        }
        return nodes;
    }
}
