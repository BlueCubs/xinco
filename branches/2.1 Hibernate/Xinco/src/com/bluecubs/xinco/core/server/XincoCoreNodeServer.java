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

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.hibernate.audit.XincoAuditableDAO;
import java.util.Vector;

import com.bluecubs.xinco.core.persistence.XincoCoreData;
import com.bluecubs.xinco.core.persistence.XincoCoreNode;
import com.bluecubs.xinco.core.persistence.XincoCoreNodeT;
import com.bluecubs.xinco.index.XincoIndexer;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

public class XincoCoreNodeServer extends XincoCoreNode implements XincoAuditableDAO, PersistenceServerObject {

    private static final long serialVersionUID = 519042301570816095L;
    private static List result;
    private Vector xincoCoreNodes = new Vector(),  xincoCoreData = new Vector(),  xincoCoreACL = new Vector();
    //create node object for data structures

    @SuppressWarnings("unchecked")
    public XincoCoreNodeServer(int attrID) throws XincoException {
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = pm.namedQuery("XincoCoreNode.findById", parameters);
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
                setXincoCoreAcl(XincoCoreACEServer.getXincoCoreACL(xcn.getId(), "XincoCoreNodeId"));
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            setXincoCoreLanguageId(-1);
            getXincoCoreACL().removeAllElements();
            getXincoCoreNodes().removeAllElements();
            getXincoCoreData().removeAllElements();
            throw new XincoException(e.getLocalizedMessage());
        }

    }
    //create node object for data structures

    public XincoCoreNodeServer(int attrID, int attrCNID, int attrLID, String attrD, int attrSN) throws XincoException {

        try {
            setId(attrID);
            setXincoCoreNodeId(attrCNID);
            setXincoCoreLanguageId(attrLID);
            setDesignation(attrD);
            setStatusNumber(attrSN);
            setXincoCoreNodes(new Vector());
            setXincoCoreData(new Vector());
            //load acl for this object
            setXincoCoreAcl(XincoCoreACEServer.getXincoCoreACL(getId(), "xincoCoreNodeId"));
        } catch (Exception e) {
            setXincoCoreLanguageId(-1);
            getXincoCoreACL().removeAllElements();
            getXincoCoreNodes().removeAllElements();
            getXincoCoreData().removeAllElements();
            throw new XincoException();
        }

    }
    //delete from db

    public boolean deleteFromDB(boolean delete_this, int userID) throws XincoException {
        try {
            int i = 0;
            //fill nodes and data
            fillXincoCoreNodes();
            fillXincoCoreData();
            //start recursive deletion
            for (i = 0; i < getXincoCoreNodes().size(); i++) {
                ((XincoCoreNodeServer) getXincoCoreNodes().elementAt(i)).deleteFromDB();
            }
            for (i = 0; i < getXincoCoreData().size(); i++) {
                XincoIndexer.removeXincoCoreData((XincoCoreDataServer) getXincoCoreData().elementAt(i));
                XincoCoreDataServer.removeFromDB(userID,
                        ((XincoCoreDataServer) getXincoCoreData().elementAt(i)).getId());
                ((XincoCoreDataServer) getXincoCoreData().elementAt(i)).setChangerID(userID);
                ((XincoCoreDataServer) getXincoCoreData().elementAt(i)).deleteFromDB();
            }
            if (delete_this) {
                deleteFromDB();
            }
        } catch (Exception e) {
            throw new XincoException(e.getLocalizedMessage());
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public void fillXincoCoreNodes() {
        try {
            result = pm.createdQuery("SELECT x FROM XincoCoreNode x WHERE x.xincoCoreNodeId = " + getId() + " ORDER BY x.designation", null);
            while (!result.isEmpty()) {
                getXincoCoreNodes().addElement((XincoCoreNode) result.get(0));
                result.remove(0);
            }
        } catch (Exception e) {
            getXincoCoreNodes().removeAllElements();
        }
    }

    @SuppressWarnings("unchecked")
    public void fillXincoCoreData() {
        try {
            result = pm.createdQuery("SELECT x FROM XincoCoreData x WHERE x.xincoCoreNodeId = " + getId() + " ORDER BY x.designation", null);
            while (!result.isEmpty()) {
                getXincoCoreData().addElement((XincoCoreData) result.get(0));
                result.remove(0);
            }
        } catch (Exception e) {
            getXincoCoreData().removeAllElements();
        }

    }

    @SuppressWarnings("unchecked")
    public static Vector findXincoCoreNodes(String attrS, int attrLID) {
        Vector nodes = new Vector();
        try {
            result = pm.createdQuery("SELECT x FROM XincoCoreNode x WHERE x.xincoCoreLanguageId = " +
                    attrLID + " AND x.designation LIKE '" + attrS +
                    "%' ORDER BY x.designation, x.xincoCoreLanguageId", null);
            int i = 0;
            while (!result.isEmpty()) {
                nodes.addElement((XincoCoreNode) result.get(0));
                i++;
                if (i >= XincoDBManager.config.getMaxSearchResult()) {
                    break;
                }
                result.remove(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            nodes.removeAllElements();
        }
        return nodes;

    }

    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreNodeParents(int attrID) {
        Vector nodes = new Vector();
        int id;
        try {
            id = attrID;
            while (id > 0) {
                result = pm.createdQuery("SELECT x FROM XincoCoreNode x WHERE x.id = " + id);
                while (!result.isEmpty()) {
                    nodes.addElement((XincoCoreNode) result.get(0));
                    if (id > 1) {
                        id = ((XincoCoreNode) result.get(0)).getId();
                    } else {
                        id = 0;
                    }
                    result.remove(0);
                }
                //The result list is empty, it's time to break!
                break;
            }
        } catch (Exception e) {
            nodes.removeAllElements();
        }
        return nodes;
    }

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
        result = pm.namedQuery("XincoCoreNode.findById", parameters);
        if (result.size() > 0) {
            XincoCoreNode temp = (XincoCoreNode) result.get(0);
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
            XincoCoreNode temp[] = new XincoCoreNode[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreNode) result.get(0);
                temp[i].setTransactionTime(getTransactionTime());
                i++;
                result.remove(0);
            }
            return temp;
        } else {
            return null;
        }
    }

    @SuppressWarnings("static-access")
    public AbstractAuditableObject create(AbstractAuditableObject value) {
        XincoCoreNode temp;
        XincoCoreNode newValue = new XincoCoreNode();

        temp = (XincoCoreNode) value;
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
        XincoCoreNode val = (XincoCoreNode) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO,
                    "Value updated: " + val);
        }
        return val;
    }

    @SuppressWarnings({"unchecked", "static-access"})
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
            setModifiedRecordDAOObject(value.getModifiedRecordDAOObject());
            //Make sure all audit data is stored properly. If not undo everything
            if (!getModifiedRecordDAOObject().saveAuditData()) {
                throw new XincoException(rb.getString("error.audit_data.invalid"));
            }
            pm.commitAndClose();
        } catch (Throwable ex) {
            pm.rollback();
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
     * @param a 
     * @return New last ID
     */
    @SuppressWarnings("unchecked")
    public int getNewID(boolean a) {
        return new XincoIDServer("Xinco_Core_Node").getNewTableID(a);
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

                temp.setId(getNewID(true));
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
            AuditingDAOHelper.delete(this, getId(), getChangerID());
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
        return xincoCoreACL;
    }

    public void setXincoCoreAcl(Vector xincoCoreAcl) {
        this.xincoCoreACL = xincoCoreAcl;
    }
}
