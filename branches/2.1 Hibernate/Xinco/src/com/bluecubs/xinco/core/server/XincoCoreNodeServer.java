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
import com.bluecubs.xinco.core.persistence.XincoCoreLanguage;
import com.bluecubs.xinco.core.persistence.XincoCoreLanguageT;
import com.bluecubs.xinco.core.persistence.XincoCoreNode;
import com.bluecubs.xinco.index.*;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditableDAO;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XincoCoreNodeServer extends XincoCoreNode implements AuditableDAO, PersistenceServerObject {

    private static final long serialVersionUID = 519042301570816095L;
    private List result;
    private Vector xincoCoreNodes,  xincoCoreData,  xincoCoreAcl;
    //create node object for data structures
    public XincoCoreNodeServer(int attrID, XincoDBManager DBM) throws XincoException {
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = DBM.namedQuery("XincoCoreLanguage.findById", parameters);
            //throw exception if no result found
            if (!result.isEmpty()) {
                XincoCoreNode xcn = (XincoCoreNode) result.get(0);
                setId(xcn.getId());
                setXincoCoreNodeId(xcn.getXincoCoreNodeId());
                setXincoCoreLanguageId(xcn.getXincoCoreLanguageId());
                setDesignation(xcn.getDesignation());
                setStatusNumber(xcn.getStatusNumber());
                setXincoCoreNodes(new Vector());
                setXincoCoreData(new Vector());
                //load acl for this object
                setXincoCoreAcl(XincoCoreACEServer.getXincoCoreACL(xcn.getId(), "XincoCoreNodeId", DBM));
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            setXincoCoreLanguageId(-1);
            getXincoCoreAcl().removeAllElements();
            getXincoCoreNodes().removeAllElements();
            getXincoCoreData().removeAllElements();
            throw new XincoException();
        }

    }
    //create node object for data structures
    public XincoCoreNodeServer(int attrID, int attrCNID, int attrLID, String attrD, int attrSN, XincoDBManager DBM) throws XincoException {

        try {
            setId(attrID);
            setXincoCoreNodeId(attrCNID);
            setXincoCoreLanguage(new XincoCoreLanguageServer(attrLID, DBM));
            setDesignation(attrD);
            setStatusNumber(attrSN);
            setXincoCoreNodes(new Vector());
            setXincoCoreData(new Vector());
            //load acl for this object
            setXincoCoreAcl(XincoCoreACEServer.getXincoCoreACL(getId(), "xincoCoreNodeId", DBM));
        } catch (Exception e) {
            setXincoCoreLanguage(null);
            getXincoCoreAcl().removeAllElements();
            getXincoCoreNodes().removeAllElements();
            getXincoCoreData().removeAllElements();
            throw new XincoException();
        }

    }
    //write to db
    public int write2DB(XincoDBManager DBM) throws XincoException {

        try {

            Statement stmt;
            String xcnid = "";

            if (getId() > 0) {
                stmt = DBM.con.createStatement();
                //set values of nullable attributes
                if (getXincoCoreNodeId() == 0) {
                    xcnid = "NULL";
                } else {
                    xcnid = "" + getXincoCoreNodeId();
                }
                XincoCoreAuditServer audit = new XincoCoreAuditServer();
                audit.updateAuditTrail("xincoCoreNode", new String[]{"id =" + getId()},
                        DBM, "audit.corenode.change", this.getChangerID());
                stmt.executeUpdate("UPDATE xincoCoreNode SET xincoCoreNodeId=" + xcnid + ", xincoCoreLanguageId=" + getXincoCoreLanguage().getId() + ", designation='" + getDesignation().replaceAll("'", "\\\\'") + "', statusNumber=" + getStatusNumber() + " WHERE id=" + getId());
                stmt.close();
            } else {
                setId(DBM.getNewID("xincoCoreNode"));

                stmt = DBM.con.createStatement();
                //set values of nullable attributes
                if (getXincoCoreNodeId() == 0) {
                    xcnid = "NULL";
                } else {
                    xcnid = "" + getXincoCoreNodeId();
                }
                stmt.executeUpdate("INSERT INTO xincoCoreNode VALUES (" + getId() + ", " + getXincoCoreNodeId() + ", " + getXincoCoreLanguage().getId() + ", '" + getDesignation().replaceAll("'", "\\\\'") + "', " + getStatusNumber() + ")");
                stmt.close();
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
    //delete from db
    public void deleteFromDB(boolean delete_this, XincoDBManager DBM, int userID) throws XincoException {

        int i = 0;

        try {

            Statement stmt;

            //fill nodes and data
            fillXincoCoreNodes(DBM);
            fillXincoCoreData(DBM);
            //start recursive deletion
            for (i = 0; i < getXincoCoreNodes().size(); i++) {
                ((XincoCoreNodeServer) getXincoCoreNodes().elementAt(i)).deleteFromDB(true, DBM, userID);
            }
            for (i = 0; i < getXincoCoreData().size(); i++) {
                XincoIndexer.removeXincoCoreData((XincoCoreDataServer) getXincoCoreData().elementAt(i), DBM);
                XincoCoreDataServer.removeFromDB(DBM, userID,
                        ((XincoCoreDataServer) getXincoCoreData().elementAt(i)).getId());
                ((XincoCoreDataServer) getXincoCoreData().elementAt(i)).setChangerID(userID);
                ((XincoCoreDataServer) getXincoCoreData().elementAt(i)).deleteFromDB(DBM);
            }
            if (delete_this) {
                XincoCoreAuditServer audit = new XincoCoreAuditServer();
                /*
                 * Aduit Trail Table (*_t) cannot handle multiple row changes!!!
                audit.updateAuditTrail("xincoCoreAce",new String [] {"id ="+getId()},
                DBM,"audit.general.delete",userID);
                 */
                stmt = DBM.con.createStatement();
                stmt.executeUpdate("DELETE FROM xincoCoreAce WHERE xincoCoreNodeId=" + getId());
                stmt.close();
                audit.updateAuditTrail("xincoCoreNode", new String[]{"id =" + getId()},
                        DBM, "audit.general.delete", userID);
                stmt = DBM.con.createStatement();
                stmt.executeUpdate("DELETE FROM xincoCoreNode WHERE id=" + getId());
                stmt.close();
            }
            DBM.con.commit();
        } catch (Exception e) {
            try {
                DBM.con.rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }

    }

    public void fillXincoCoreNodes(XincoDBManager DBM) {

        try {

            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xincoCoreNode WHERE xincoCoreNodeId = " + getId() + " ORDER BY designation");

            while (rs.next()) {
                getXincoCoreNodes().addElement(new XincoCoreNodeServer(rs.getInt("id"), rs.getInt("xincoCoreNodeId"), rs.getInt("xincoCoreLanguageId"), rs.getString("designation"), rs.getInt("statusNumber"), DBM));
            }

            stmt.close();

        } catch (Exception e) {
            getXincoCoreNodes().removeAllElements();
        }

    }

    public void fillXincoCoreData(XincoDBManager DBM) {

        try {

            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xincoCoreData WHERE xincoCoreNodeId = " + getId() + " ORDER BY designation");

            while (rs.next()) {
                getXincoCoreData().addElement(new XincoCoreDataServer(rs.getInt("id"), rs.getInt("xincoCoreNodeId"), rs.getInt("xincoCoreLanguageId"), rs.getInt("xincoCoreData_typeId"), rs.getString("designation"), rs.getInt("statusNumber"), DBM));
            }

            stmt.close();

        } catch (Exception e) {
            getXincoCoreData().removeAllElements();
        }

    }

    public static Vector findXincoCoreNodes(String attrS, int attrLID, XincoDBManager DBM) {

        Vector nodes = null;

        try {

            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xincoCoreNode WHERE xincoCoreLanguageId = " + attrLID + " AND designation LIKE '" + attrS + "%' ORDER BY designation, xincoCoreLanguageId");

            int i = 0;
            while (rs.next()) {
                nodes.addElement(new XincoCoreNodeServer(rs.getInt("id"), rs.getInt("xincoCoreNodeId"), rs.getInt("xincoCoreLanguageId"), rs.getString("designation"), rs.getInt("statusNumber"), DBM));
                i++;
                if (i >= DBM.config.getMaxSearchResult()) {
                    break;
                }
            }

            stmt.close();

        } catch (Exception e) {
            nodes.removeAllElements();
        }

        return nodes;

    }

    public static Vector getXincoCoreNodeParents(int attrID, XincoDBManager DBM) {

        Vector nodes = new Vector();
        int id;

        try {

            id = attrID;

            Statement stmt;
            ResultSet rs;

            while (id > 0) {
                stmt = DBM.con.createStatement();
                rs = stmt.executeQuery("SELECT * FROM xincoCoreNode WHERE id = " + id);
                while (rs.next()) {
                    nodes.addElement(new XincoCoreNodeServer(rs.getInt("id"), DBM));
                    if (id > 1) {
                        id = rs.getInt("xincoCoreNodeId");
                    } else {
                        id = 0;
                    }
                }
                stmt.close();
            }

        } catch (Exception e) {
            nodes.removeAllElements();
        }

        return nodes;

    }

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
        result = pm.namedQuery("XincoCoreLanguage.findById", parameters);
        if (result.size() > 0) {
            XincoCoreLanguageServer temp = (XincoCoreLanguageServer) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws Exception {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreLanguage x WHERE ";
        if (parameters.containsKey("id")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.INFO, "Searching by id");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.id = :id";
            counter++;
        }
        if (parameters.containsKey("designation")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.INFO, "Searching by designation");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.designation = :designation";
            counter++;
        }
        if (parameters.containsKey("sign")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.INFO, "Searching by sign");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.sign = :sign";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoCoreLanguageServer temp[] = new XincoCoreLanguageServer[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreLanguageServer) result.get(0);
                temp[i].setTransactionTime(getTransactionTime());
                i++;
                result.remove(0);
            }
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject create(AbstractAuditableObject value) {
        XincoCoreLanguageServer temp;
        XincoCoreLanguage newValue = new XincoCoreLanguage();

        temp = (XincoCoreLanguageServer) value;
        temp.setId(temp.getId());
        temp.setDesignation(temp.getDesignation());
        temp.setSign(temp.getSign());

        newValue.setRecordId(temp.getRecordId());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.INFO,
                    "New value created: " + newValue);
        }
        return newValue;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoCoreLanguageServer val = (XincoCoreLanguageServer) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.INFO,
                    "Value updated: " + val);
        }
        return val;
    }

    @SuppressWarnings("unchecked")
    public void delete(AbstractAuditableObject value) {
        try {
            XincoCoreLanguage val = (XincoCoreLanguage) value;
            XincoCoreLanguageT temp = new XincoCoreLanguageT();
            temp.setRecordId(val.getRecordId());
            temp.setId(val.getId());

            temp.setDesignation(val.getDesignation());
            temp.setDesignation(val.getDesignation());
            temp.setSign(val.getSign());

            pm.startTransaction();
            pm.persist(temp, false, false);
            pm.delete(val, false);
            getModifiedRecordDAOObject().saveAuditData();
            pm.commitAndClose();
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("id", getId());
        return temp;
    }

    /**
     * Get a new newID
     * @return New last ID
     */
    @SuppressWarnings("unchecked")
    public int getNewID() {
        return new XincoIDServer("xincoCoreLanguage").getNewTableID();
    }

    @SuppressWarnings("unchecked")
    public boolean write2DB() {
        try {
            if (getId() > 0) {
                AuditingDAOHelper.update(this, new XincoCoreLanguage());
            } else {
                XincoCoreLanguage temp = new XincoCoreLanguage();
                temp.setChangerID(getChangerID());
                temp.setCreated(true);

                temp.setId(getId());
                temp.setDesignation(getDesignation());
                temp.setSign(getSign());

                temp = (XincoCoreLanguage) AuditingDAOHelper.create(this, temp);
                setId(temp.getId());
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.INFO, "Assigned id: " + getId());
                }
            }
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public boolean deleteFromDB() {
        setTransactionTime(DateRange.startingNow());
        try {
            AuditingDAOHelper.delete(this, getId());
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public Vector getXincoCoreNodes() {
        return xincoCoreNodes;
    }

    public void setXincoCoreNodes(Vector xincoCoreNodes) {
        this.xincoCoreNodes = xincoCoreNodes;
    }

    public Vector getXincoCoreData() {
        return xincoCoreData;
    }

    public void setXincoCoreData(Vector xincoCoreData) {
        this.xincoCoreData = xincoCoreData;
    }

    public Vector getXincoCoreAcl() {
        return xincoCoreAcl;
    }

    public void setXincoCoreAcl(Vector xincoCoreAcl) {
        this.xincoCoreAcl = xincoCoreAcl;
    }
}
