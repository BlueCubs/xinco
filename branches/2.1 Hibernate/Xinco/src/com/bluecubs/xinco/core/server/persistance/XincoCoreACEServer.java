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
 * Name:            XincoCoreACEServer
 *
 * Description:     access control entry
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

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistance.XincoCoreACE;
import com.bluecubs.xinco.core.persistance.XincoCoreGroup;
import com.bluecubs.xinco.core.persistance.audit.XincoCoreACET;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditableDAO;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditingDAOHelper;
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
public class XincoCoreACEServer extends XincoCoreACE implements XincoAuditableDAO, XincoPersistanceServerObject {

    private static XincoPersistanceManager pm = new XincoPersistanceManager();
    private static List result;
    private static HashMap parameters;

    /**
     * create single ace object for data structures
     * @param attrID XincoCoreACE id
     * @throws com.bluecubs.xinco.core.XincoException
     */
    @SuppressWarnings("unchecked")
    public XincoCoreACEServer(int attrID) throws XincoException {
        try {
            parameters = new HashMap();
            parameters.put("id", attrID);
            result = pm.namedQuery("XincoCoreACE.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                XincoCoreACE temp = (XincoCoreACE) result.get(0);
                setId(temp.getId());
                setXincoCoreUserId(temp.getXincoCoreUserId());
                setXincoCoreGroupId(temp.getXincoCoreGroupId());
                setXincoCoreNodeId(temp.getXincoCoreNodeId());
                setXincoCoreDataId(temp.getXincoCoreDataId());
                setReadPermission(temp.getReadPermission());
                setWritePermission(temp.getWritePermission());
                setExecutePermission(temp.getExecutePermission());
                setAdminPermission(temp.getAdminPermission());
            } else {
                if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Error looking for ACE with id: " + attrID);
                }
                throw new XincoException();
            }
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    /**
     * Create single ace object for data structures
     * @param attrID XincoCoreUserid
     * @param attrUID Attribute id
     * @param attrGID XincoCoregroupId
     * @param attrNID XincoCorenodeId
     * @param attrDID XincoCoredataId
     * @param attrRP ReadPermission
     * @param attrWP WritePermission
     * @param attrEP ExecutePermission
     * @param attrAP AdminPermission
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreACEServer(int attrID, int attrUID, int attrGID, int attrNID, int attrDID, boolean attrRP, boolean attrWP, boolean attrEP, boolean attrAP) throws XincoException {
        setId(attrID);
        setXincoCoreUserId(attrUID);
        setXincoCoreGroupId(attrGID);
        setXincoCoreNodeId(attrNID);
        setXincoCoreDataId(attrDID);
        setReadPermission(attrRP);
        setWritePermission(attrWP);
        setExecutePermission(attrEP);
        setAdminPermission(attrAP);
    }

    /**
     * Create single ace object for data structures
     */
    public XincoCoreACEServer() {

    }

    /**
     * create complete ACL for node or data
     * @param attrID Attribute id
     * @param attrT Attribute name (String)
     * @return Vector
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreACL(int attrID, String attrT) {
        Vector core_acl = new Vector();
        try {
            if (parameters == null) {
                parameters = new HashMap();
            } else {
                parameters.clear();
            }
            parameters.put("id", attrID);
            result = pm.createdQuery("SELECT p FROM XincoCoreACE p WHERE p." + attrT + "= :id" +
                    " ORDER BY p.xincoCoreUserId, p.xincoCoreGroupId, p.xincoCoreNodeId, p.xincoCoreDataId", parameters);
            while (!result.isEmpty()) {
                core_acl.add(new XincoCoreACEServer(((XincoCoreACE) result.get(0)).getId()));
                result.remove(0);
            }
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Error getting ACL's. (" +
                        attrT + "=" + attrID + ")", e);
            }
            core_acl.removeAllElements();
        }
        return core_acl;
    }

    /**
     * Check access by comparing user / user groups to ACL and return permissions
     * @param attrU XincoCoreUser
     * @param attrACL ACL
     * @return
     */
    public static XincoCoreACE checkAccess(XincoCoreUserServer attrU, Vector attrACL) {
        int i = 0;
        int j = 0;
        boolean match_ace = false;
        XincoCoreACE core_ace = new XincoCoreACE();
        for (i = 0; i < attrACL.size(); i++) {
            //reset match_ace
            match_ace = false;
            //check if user is mentioned in ACE
            if (((XincoCoreACE) attrACL.elementAt(i)).getXincoCoreUserId() == attrU.getId()) {
                match_ace = true;
            }
            //check if group of user is mentioned in ACE
            if (!match_ace) {
                for (j = 0; j < attrU.getXincoCoreGroups().size(); j++) {
                    if (((XincoCoreACE) attrACL.elementAt(i)).getXincoCoreGroupId() == ((XincoCoreGroup) attrU.getXincoCoreGroups().elementAt(j)).getId()) {
                        match_ace = true;
                        break;
                    }
                }
            }
            //add to rights
            if (match_ace) {
                //modify read permission
                if (!core_ace.getReadPermission()) {
                    core_ace.setReadPermission(((XincoCoreACE) attrACL.elementAt(i)).getReadPermission());
                }
                //modify write permission
                if (!core_ace.getWritePermission()) {
                    core_ace.setWritePermission(((XincoCoreACE) attrACL.elementAt(i)).getWritePermission());
                }
                //modify execute permission
                if (!core_ace.getExecutePermission()) {
                    core_ace.setExecutePermission(((XincoCoreACE) attrACL.elementAt(i)).getExecutePermission());
                }
                //modify admin permission
                if (!core_ace.getAdminPermission()) {
                    core_ace.setAdminPermission(((XincoCoreACE) attrACL.elementAt(i)).getAdminPermission());
                }
            }
        }
        return core_ace;
    }

    public XincoAbstractAuditableObject findById(HashMap parameters) throws DataRetrievalFailureException {
        result = pm.namedQuery("XincoCoreACE.findById", parameters);
        if (result.size() > 0) {
            XincoCoreACE temp = (XincoCoreACE) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public XincoAbstractAuditableObject[] findWithDetails(HashMap parameters) throws DataRetrievalFailureException {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreACE x WHERE ";
        if (parameters.containsKey("xincoCoreUserId")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by xincoCoreUserId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreUserId = :xincoCoreUserId";
            counter++;
        }
        if (parameters.containsKey("xincoCoreGroupId")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by xincoCoreGroupId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreGroupId = :xincoCoreGroupId";
            counter++;
        }
        if (parameters.containsKey("xincoCoreNodeId")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by xincoCoreNodeId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreNodeId = :xincoCoreNodeId";
            counter++;
        }
        if (parameters.containsKey("xincoCoreDataId")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by xincoCoreDataId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreDataId = :xincoCoreDataId";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoCoreACE temp[] = new XincoCoreACE[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreACE) result.get(0);
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
        XincoCoreACE temp, newValue = new XincoCoreACE();
        temp = (XincoCoreACE) value;
        if (!value.isCreated()) {
            newValue.setId(temp.getId());
            newValue.setRecordId(temp.getRecordId());
        } else {
            newValue.setId(getNewID());
        }
        if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Creating with new id: " + newValue.getId());
        }
        newValue.setXincoCoreUserId(temp.getXincoCoreUserId());
        newValue.setXincoCoreGroupId(temp.getXincoCoreGroupId());
        newValue.setXincoCoreNodeId(temp.getXincoCoreNodeId());
        newValue.setXincoCoreDataId(temp.getXincoCoreDataId());
        newValue.setReadPermission(temp.getReadPermission());
        newValue.setWritePermission(temp.getWritePermission());
        newValue.setExecutePermission(temp.getExecutePermission());
        newValue.setAdminPermission(temp.getAdminPermission());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        return newValue;
    }

    public XincoAbstractAuditableObject update(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoCoreACE val = (XincoCoreACE) value;
        XincoCoreACET temp = new XincoCoreACET();
        temp.setRecordId(val.getRecordId());
        if (!value.isCreated()) {
            temp.setId(val.getId());
        } else {
            temp.setId(val.getRecordId());
        }
        temp.setXincoCoreUserId(val.getXincoCoreUserId());
        temp.setXincoCoreGroupId(val.getXincoCoreGroupId());
        temp.setXincoCoreNodeId(val.getXincoCoreNodeId());
        temp.setXincoCoreDataId(val.getXincoCoreDataId());
        temp.setReadPermission(val.getReadPermission());
        temp.setWritePermission(val.getWritePermission());
        temp.setExecutePermission(val.getExecutePermission());
        temp.setAdminPermission(val.getAdminPermission());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.persist(val, true, false);
        val.saveAuditData(pm);
        pm.commitAndClose();
        return val;
    }

    public void delete(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoCoreACE val = (XincoCoreACE) value;
        XincoCoreACET temp = new XincoCoreACET();
        temp.setRecordId(val.getRecordId());
        temp.setId(val.getId());
        temp.setXincoCoreUserId(val.getXincoCoreUserId());
        temp.setXincoCoreGroupId(val.getXincoCoreGroupId());
        temp.setXincoCoreNodeId(val.getXincoCoreNodeId());
        temp.setXincoCoreDataId(val.getXincoCoreDataId());
        temp.setReadPermission(val.getReadPermission());
        temp.setWritePermission(val.getWritePermission());
        temp.setExecutePermission(val.getExecutePermission());
        temp.setAdminPermission(val.getAdminPermission());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.delete(val, false);
        val.saveAuditData(pm);
        pm.commitAndClose();
    }

    @SuppressWarnings("unchecked")
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("id", getId());
        return temp;
    }

    public int getNewID() {
        return new XincoIDServer("xinco_core_ace").getNewTableID();
    }

    public boolean deleteFromDB() throws XincoException {
        setTransactionTime(DateRange.startingNow());
        try {
            XincoAuditingDAOHelper.delete(this, getId());
            return true;
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    public boolean write2DB() throws XincoException {
        try {
            if (getId() > 0) {
                XincoAuditingDAOHelper.update(this, new XincoCoreACE(getId()));
            } else {
                XincoCoreACE temp = new XincoCoreACE();
                temp.setId(getId());
                temp.setChangerID(getChangerID());
                temp.setCreated(true);
                temp.setXincoCoreUserId(getXincoCoreUserId());
                temp.setXincoCoreGroupId(getXincoCoreGroupId());
                temp.setXincoCoreNodeId(getXincoCoreNodeId());
                temp.setXincoCoreDataId(getXincoCoreDataId());
                temp.setReadPermission(getReadPermission());
                temp.setWritePermission(getWritePermission());
                temp.setExecutePermission(getExecutePermission());
                temp.setAdminPermission(getAdminPermission());
                temp = (XincoCoreACE) XincoAuditingDAOHelper.create(this, temp);
                setId(temp.getId());
                if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Assigned id: " + getId());
                }
            }
            return true;
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }
}
