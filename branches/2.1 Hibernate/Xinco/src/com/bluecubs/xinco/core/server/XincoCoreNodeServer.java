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
import com.bluecubs.xinco.core.persistence.XincoCoreData;
import com.bluecubs.xinco.core.persistence.XincoCoreNode;
import com.bluecubs.xinco.core.persistence.XincoCoreNodeT;
import com.bluecubs.xinco.index.*;
import com.bluecubs.xinco.core.hibernate.audit.AbstractAuditableObject;
import com.bluecubs.xinco.core.hibernate.audit.AuditableDAO;
import com.bluecubs.xinco.core.hibernate.audit.AuditingDAOHelper;
import com.bluecubs.xinco.core.hibernate.audit.PersistenceServerObject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

public class XincoCoreNodeServer extends XincoCoreNode implements AuditableDAO, PersistenceServerObject {

    private static final long serialVersionUID = 519042301570816095L;
    private static List result;
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
            getXincoCoreACL().removeAllElements();
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
            setXincoCoreLanguageId(attrLID);
            setDesignation(attrD);
            setStatusNumber(attrSN);
            setXincoCoreNodes(new Vector());
            setXincoCoreData(new Vector());
            //load acl for this object
            setXincoCoreAcl(XincoCoreACEServer.getXincoCoreACL(getId(), "xincoCoreNodeId", DBM));
        } catch (Exception e) {
            setXincoCoreLanguageId(-1);
            getXincoCoreACL().removeAllElements();
            getXincoCoreNodes().removeAllElements();
            getXincoCoreData().removeAllElements();
            throw new XincoException();
        }

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
                ((XincoCoreNodeServer) getXincoCoreNodes().elementAt(i)).deleteFromDB();
            }
            for (i = 0; i < getXincoCoreData().size(); i++) {
                XincoIndexer.removeXincoCoreData((XincoCoreDataServer) getXincoCoreData().elementAt(i), DBM);
                XincoCoreDataServer.removeFromDB(DBM, userID,
                        ((XincoCoreDataServer) getXincoCoreData().elementAt(i)).getId());
                ((XincoCoreDataServer) getXincoCoreData().elementAt(i)).setChangerID(userID);
                ((XincoCoreDataServer) getXincoCoreData().elementAt(i)).deleteFromDB(DBM);
            }
            if (delete_this) {
                deleteFromDB();
            }
        } catch (Exception e) {
            throw new XincoException();
        }

    }

    public void fillXincoCoreNodes(XincoDBManager DBM) {
        try {
            result = DBM.createdQuery("SELECT x FROM XincoCoreNode x WHERE x.xincoCoreNodeId = " + getId() + " ORDER BY x.designation", null);
            while (!result.isEmpty()) {
                getXincoCoreNodes().addElement((XincoCoreNode) result.get(0));
                result.remove(0);
            }
        } catch (Exception e) {
            getXincoCoreNodes().removeAllElements();
        }
    }

    public void fillXincoCoreData(XincoDBManager DBM) {
        try {
            result = DBM.createdQuery("SELECT x FROM XincoCoreData x WHERE x.xincoCoreNodeId = " + getId() + " ORDER BY x.designation", null);
            while (!result.isEmpty()) {
                getXincoCoreData().addElement((XincoCoreData) result.get(0));
                result.remove(0);
            }
        } catch (Exception e) {
            getXincoCoreData().removeAllElements();
        }

    }

    public static Vector findXincoCoreNodes(String attrS, int attrLID, XincoDBManager DBM) {
        Vector nodes = null;
        try {
            result = DBM.createdQuery("SELECT x FROM XincoCoreNode x WHERE x.xincoCoreLanguageId = " +
                    attrLID + " AND x.designation LIKE '" + attrS +
                    "%' ORDER BY x.designation, x.xincoCoreLanguageId", null);
            int i = 0;
            while (!result.isEmpty()) {
                nodes.addElement((XincoCoreNode) result.get(0));
                i++;
                if (i >= DBM.config.getMaxSearchResult()) {
                    break;
                }
                result.remove(0);
            }
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
            while (id > 0) {
                result = DBM.createdQuery("SELECT x FROM XincoCoreNode x WHERE x.id = " + id);
                while (!result.isEmpty()) {
                    nodes.addElement((XincoCoreNode) result.get(0));
                    if (id > 1) {
                        id = ((XincoCoreNode) result.get(0)).getId();
                    } else {
                        id = 0;
                    }
                }
                result.remove(0);
            }
        } catch (Exception e) {
            nodes.removeAllElements();
        }
        return nodes;
    }

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
        result = pm.namedQuery("XincoCoreNode.findById", parameters);
        if (result.size() > 0) {
            XincoCoreNodeServer temp = (XincoCoreNodeServer) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws Exception {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreNode x WHERE ";
        if (parameters.containsKey("id")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Searching by id");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.id = :id";
            counter++;
        }
        if (parameters.containsKey("designation")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Searching by designation");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.designation = :designation";
            counter++;
        }
        if (parameters.containsKey("statusNumber")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Searching by status number");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.statusNumber = :statusNumber";
            counter++;
        }
        if (parameters.containsKey("xincoCoreNodeId")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Searching by status xincoCoreNodeId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreNodeId = :xincoCoreNodeId";
            counter++;
        }
        if (parameters.containsKey("xincoCoreLanguageId")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Searching by status xincoCoreLanguageId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreLanguageId = :xincoCoreLanguageId";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoCoreNodeServer temp[] = new XincoCoreNodeServer[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreNodeServer) result.get(0);
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
        XincoCoreNodeServer temp;
        XincoCoreNode newValue = new XincoCoreNode();

        temp = (XincoCoreNodeServer) value;
        newValue.setId(temp.getId());
        newValue.setDesignation(temp.getDesignation());
        newValue.setXincoCoreNodeId(temp.getXincoCoreNodeId());
        newValue.setXincoCoreLanguageId(temp.getXincoCoreLanguageId());
        newValue.setStatusNumber(temp.getStatusNumber());

        newValue.setRecordId(temp.getRecordId());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO,
                    "New value created: " + newValue);
        }
        return newValue;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoCoreNodeServer val = (XincoCoreNodeServer) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO,
                    "Value updated: " + val);
        }
        return val;
    }

    @SuppressWarnings("unchecked")
    public void delete(AbstractAuditableObject value) {
        try {
            XincoCoreNode val = (XincoCoreNode) value;
            XincoCoreNodeT temp = new XincoCoreNodeT();
            temp.setRecordId(val.getRecordId());
            temp.setId(val.getId());

            temp.setDesignation(val.getDesignation());
            temp.setXincoCoreNodeId(val.getXincoCoreNodeId());
            temp.setXincoCoreLanguageId(val.getXincoCoreLanguageId());
            temp.setDesignation(val.getDesignation());
            temp.setStatusNumber(val.getStatusNumber());

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
        return new XincoIDServer("XincoCoreNode").getNewTableID();
    }

    @SuppressWarnings("unchecked")
    public boolean write2DB() {
        try {
            if (getId() > 0) {
                AuditingDAOHelper.update(this, new XincoCoreNode());
            } else {
                XincoCoreNode temp = new XincoCoreNode();
                temp.setChangerID(getChangerID());
                temp.setCreated(true);

                temp.setId(getId());
                temp.setDesignation(getDesignation());
                temp.setXincoCoreNodeId(getXincoCoreNodeId());
                temp.setXincoCoreLanguageId(getXincoCoreLanguageId());
                temp.setDesignation(getDesignation());
                temp.setStatusNumber(getStatusNumber());

                temp = (XincoCoreNode) AuditingDAOHelper.create(this, temp);
                setId(temp.getId());
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Assigned id: " + getId());
                }
            }
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, null, e);
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
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, null, e);
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

    public Vector getXincoCoreACL() {
        return xincoCoreAcl;
    }

    public void setXincoCoreAcl(Vector xincoCoreAcl) {
        this.xincoCoreAcl = xincoCoreAcl;
    }
}
