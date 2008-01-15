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
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.core.exception.XincoSettingException;
import com.bluecubs.xinco.core.persistance.XincoCoreNode;
import com.bluecubs.xinco.core.persistance.XincoCoreData;
import com.bluecubs.xinco.core.persistance.XincoCoreLanguage;
import com.bluecubs.xinco.core.persistance.audit.XincoCoreNodeT;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditableDAO;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditingDAOHelper;
import com.bluecubs.xinco.index.persistance.XincoIndexer;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoCoreNodeServer extends XincoCoreNode implements XincoAuditableDAO, XincoPersistanceServerObject {

    private static List result;
    private Vector xincoCoreNodes = null,  xincoCoreData = null,  xincoCoreACL = null;
    private XincoCoreLanguage xincoCoreLanguage;
    private boolean delete = false;

    /**
     * Create node object for data structures
     * @param id
     * @throws com.bluecubs.xinco.core.exception.XincoException
     */
    @SuppressWarnings("unchecked")
    public XincoCoreNodeServer(int id) throws XincoException {
        pm.setDeveloperMode(new XincoSettingServer("setting.enable.developermode").getBoolValue());
        try {
            parameters.clear();
            parameters.put("id", id);
            result = pm.namedQuery("XincoCoreNode.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                XincoCoreNode temp = (XincoCoreNode) result.get(0);
                setId(temp.getId());
                setXincoCoreNodeId(temp.getXincoCoreNodeId());
                setXincoCoreLanguageId(temp.getXincoCoreLanguageId());
                setXincoCoreLanguage(new XincoCoreLanguageServer(temp.getXincoCoreLanguageId()));
                setDesignation(temp.getDesignation());
                setStatusNumber(temp.getStatusNumber());
                setXincoCoreNodes(new Vector());
                setXincoCoreData(new Vector());
                //load acl for this object
                setXincoCoreACL(XincoCoreACEServer.getXincoCoreACL(temp.getId(), "xincoCoreNodeId"));
            } else {
                throw new XincoException();
            }
        } catch (Throwable e) {
            setXincoCoreLanguage(null);
            getXincoCoreACL().removeAllElements();
            getXincoCoreNodes().removeAllElements();
            getXincoCoreData().removeAllElements();
            if (new XincoSettingServer().getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, "Error creating node.", e);
            }
            throw new XincoException();
        }
    }

    /**
     * Create node object for data structures
     * @param id
     * @param core_node_id
     * @param language_id 
     * @param designation 
     * @param status_number
     * @throws com.bluecubs.xinco.core.exception.XincoException
     */
    public XincoCoreNodeServer(int id, int core_node_id, int language_id,
            String designation, int status_number) throws XincoException {
        pm.setDeveloperMode(new XincoSettingServer("setting.enable.developermode").getBoolValue());
        try {
            setId(id);
            setXincoCoreNodeId(core_node_id);
            setXincoCoreLanguage(new XincoCoreLanguageServer(language_id));
            setXincoCoreLanguageId(language_id);
            setDesignation(designation);
            setStatusNumber(status_number);
            setXincoCoreNodes(new Vector());
            setXincoCoreData(new Vector());
            //load acl for this object
            setXincoCoreACL(XincoCoreACEServer.getXincoCoreACL(getId(), "xincoCoreNodeId"));
        } catch (Throwable e) {
            setXincoCoreLanguage(null);
            getXincoCoreACL().removeAllElements();
            getXincoCoreNodes().removeAllElements();
            getXincoCoreData().removeAllElements();
            if (new XincoSettingServer().getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, "Error creating node.", e);
            }
            throw new XincoException();
        }
    }

    public XincoCoreNodeServer() {
        pm.setDeveloperMode(new XincoSettingServer("setting.enable.developermode").getBoolValue());
    }

    /**
     * Fill XincoCoreNodes vector for this Node
     */
    @SuppressWarnings("unchecked")
    public void fillXincoCoreNodes() {
        try {
            parameters.clear();
            parameters.put("id", getId());
            result = pm.createdQuery("SELECT x FROM XincoCoreNode x WHERE x.xincoCoreNodeId = :id ORDER BY x.designation", parameters);
            if (!result.isEmpty()) {
                XincoCoreNode temp = (XincoCoreNode) result.get(0);
                getXincoCoreNodes().add(new XincoCoreNodeServer(temp.getId(),
                        temp.getXincoCoreNodeId(), temp.getXincoCoreLanguageId(),
                        temp.getDesignation(), temp.getStatusNumber()));
            }
        } catch (Throwable e) {
            try {
                if (new XincoSettingServer().getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, "Error getting node.", e);
                }
                getXincoCoreNodes().removeAllElements();
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Fill XIncoCoreData vector for this Node
     */
    @SuppressWarnings("unchecked")
    public void fillXincoCoreData() {
        try {
            parameters.clear();
            parameters.put("id", getId());
            result = pm.createdQuery("SELECT x FROM XincoCoreData x WHERE x.xincoCoreNodeId = :id ORDER BY x.designation", parameters);
            if (!result.isEmpty()) {
                XincoCoreData temp = (XincoCoreData) result.get(0);
                getXincoCoreData().add(new XincoCoreDataServer((int) temp.getId(), temp.getXincoCoreNodeId(),
                        temp.getXincoCoreLanguageId(), temp.getXincoCoreDataTypeId(),
                        temp.getDesignation(), temp.getStatusNumber()));
            }
        } catch (Throwable e) {
            try {
                if (new XincoSettingServer().getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, "Error filling data.", e);
                }
                getXincoCoreData().removeAllElements();
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Find XincoCoreNodes based on parameters
     * @param designation
     * @param language_id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Vector findXincoCoreNodes(String designation, int language_id) {
        Vector nodes = new Vector();
        parameters.clear();
        parameters.put("languageId", language_id);
        try {
            result = pm.createdQuery("SELECT x FROM XincoCoreNode x WHERE x.xincoCoreLanguageId = :languageId  AND x.designation like '%" + designation + "%' ORDER BY x.designation, x.xincoCoreLanguageId", parameters);
            int i = 0;
            while (!result.isEmpty()) {
                nodes.add((XincoCoreNode) result.get(0));
                i++;
                if (i >= XincoPersistanceManager.config.getMaxSearchResult()) {
                    break;
                }
            }
        } catch (Throwable e) {
            try {
                if (new XincoSettingServer().getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, "Error finding node. " + parameters, e);
                }
                nodes.removeAllElements();
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return nodes;
    }

    /**
     * Get parents of this node
     * @param id 
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreNodeParents(int id) {
        Vector nodes = new Vector();
        try {
            parameters.clear();
            parameters.put("id", id);
            result = pm.namedQuery("XincoCoreNode.findById", parameters);
            while (!result.isEmpty()) {
                XincoCoreNode temp = (XincoCoreNode) result.get(0);
                nodes.addElement(new XincoCoreNodeServer(temp.getId()));
                result.remove(0);
            }
        } catch (Throwable e) {
            try {
                if (new XincoSettingServer().getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, "Error getting node parents. " + parameters, e);
                }
                nodes.removeAllElements();
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return nodes;
    }

    public Vector getXincoCoreNodes() {
        if (xincoCoreNodes == null) {
            xincoCoreNodes = new Vector();
        }
        return xincoCoreNodes;
    }

    public void setXincoCoreNodes(Vector xincoCoreNodes) {
        this.xincoCoreNodes = xincoCoreNodes;
    }

    public Vector getXincoCoreData() {
        if (xincoCoreData == null) {
            xincoCoreData = new Vector();
        }
        return xincoCoreData;
    }

    public void setXincoCoreData(Vector xincoCoreData) {
        this.xincoCoreData = xincoCoreData;
    }

    public Vector getXincoCoreACL() {
        if (xincoCoreACL == null) {
            xincoCoreACL = new Vector();
        }
        return xincoCoreACL;
    }

    public void setXincoCoreACL(Vector xincoCoreACL) {
        this.xincoCoreACL = xincoCoreACL;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public XincoAbstractAuditableObject findById(HashMap parameters) throws DataRetrievalFailureException {
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

    public XincoAbstractAuditableObject[] findWithDetails(HashMap parameters) throws DataRetrievalFailureException {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreNode x WHERE ";
        if (parameters.containsKey("xincoCoreNodeId")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Searching by xincoCoreNodeId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreNodeId = :xincoCoreNodeId";
            counter++;
        }
        if (parameters.containsKey("xincoCoreLanguageId")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Searching by xincoCoreLanguageId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreLanguageId = :xincoCoreLanguageId";
            counter++;
        }
        if (parameters.containsKey("designation")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Searching by designation");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.designation = :designation";
            counter++;
        }
        if (parameters.containsKey("statusNumber")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Searching by statusNumber");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.statusNumber = :statusNumber";
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

    public XincoAbstractAuditableObject create(XincoAbstractAuditableObject value) {
        XincoCoreNode temp, newValue = new XincoCoreNode();
        temp = (XincoCoreNode) value;
        if (!value.isCreated()) {
            newValue.setId(temp.getId());
            newValue.setRecordId(temp.getRecordId());
        } else {
            newValue.setId(getNewID());
        }
        if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Creating with new id: " + newValue.getId());
        }
        newValue.setStatusNumber(temp.getStatusNumber());
        newValue.setXincoCoreLanguageId(temp.getXincoCoreLanguageId());
        newValue.setXincoCoreNodeId(temp.getXincoCoreNodeId());
        newValue.setDesignation(temp.getDesignation());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        return newValue;
    }

    public XincoAbstractAuditableObject update(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoCoreNode val = (XincoCoreNode) value;
        XincoCoreNodeT temp = new XincoCoreNodeT();
        temp.setRecordId(val.getRecordId());
        if (!value.isCreated()) {
            temp.setId(val.getId());
        } else {
            temp.setId(val.getRecordId());
        }
        temp.setStatusNumber(val.getStatusNumber());
        temp.setXincoCoreLanguageId(val.getXincoCoreLanguageId());
        temp.setXincoCoreNodeId(val.getXincoCoreNodeId());
        temp.setDesignation(val.getDesignation());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.persist(val, true, false);
        val.saveAuditData(pm);
        pm.commitAndClose();
        return val;
    }

    @SuppressWarnings("unchecked")
    public void delete(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        int i = 0;
        try {
            //fill nodes and data
            fillXincoCoreNodes();
            fillXincoCoreData();
            //start recursive deletion
            for (i = 0; i < getXincoCoreNodes().size(); i++) {
                ((XincoCoreNodeServer) getXincoCoreNodes().elementAt(i)).setDelete(true);
                ((XincoCoreNodeServer) getXincoCoreNodes().elementAt(i)).deleteFromDB();
            }
            for (i = 0; i < getXincoCoreData().size(); i++) {
                XincoIndexer.removeXincoCoreData((XincoCoreData) getXincoCoreData().elementAt(i));
                ((XincoCoreDataServer) getXincoCoreData().elementAt(i)).setChangerID(getChangerID());
                ((XincoCoreDataServer) getXincoCoreData().elementAt(i)).deleteFromDB();
            }
            if (isDelete()) {
                parameters.clear();
                parameters.put("id", getId());
                result = pm.namedQuery("XincoCoreNode.findById", parameters);
                while (!result.isEmpty()) {
                    XincoCoreNode temp = (XincoCoreNode) result.get(0);
                    XincoCoreNodeServer temp2 = new XincoCoreNodeServer(temp.getId());
                    temp2.deleteFromDB();
                    result.remove(0);
                }
            }
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, null, e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("id", getId());
        return temp;
    }

    public int getNewID() {
        return new XincoIDServer("xinco_core_node").getNewTableID();
    }

    public boolean deleteFromDB(){
        setTransactionTime(DateRange.startingNow());
        try {
            XincoAuditingDAOHelper.delete(this, getId());
            return true;
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public boolean write2DB(){
        try {
            if (getId() > 0) {
                XincoAuditingDAOHelper.update(this, new XincoCoreNode(getId()));
            } else {
                XincoCoreNode temp = new XincoCoreNode();
                temp.setId(getId());
                temp.setChangerID(getChangerID());
                temp.setCreated(true);
                temp.setId(getId());
                temp.setXincoCoreNodeId(getXincoCoreNodeId() == 0 ? null : getXincoCoreNodeId());
                temp.setXincoCoreLanguageId(getXincoCoreLanguageId());
                temp.setDesignation(getDesignation());
                temp.setStatusNumber(getStatusNumber());
                temp = (XincoCoreNode) XincoAuditingDAOHelper.create(this, temp);
                setId(temp.getId());
                if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Assigned id: " + getId());
                }
            }
            return true;
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public XincoCoreLanguage getXincoCoreLanguage() {
        return xincoCoreLanguage;
    }

    public void setXincoCoreLanguage(XincoCoreLanguage xincoCoreLanguage) {
        this.xincoCoreLanguage = xincoCoreLanguage;
    }
}
