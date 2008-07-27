/**
 *Copyright 2007 blueCubs.com
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
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.PersistenceServerUtils;
import java.util.HashMap;
import java.util.Vector;

import com.bluecubs.xinco.core.persistence.XincoCoreACE;
import com.bluecubs.xinco.core.persistence.XincoCoreACET;
import com.bluecubs.xinco.core.persistence.XincoCoreGroup;
import com.dreamer.Hibernate.Audit.AuditableDAO;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

public class XincoCoreACEServer extends XincoCoreACE implements AuditableDAO, PersistenceServerObject {

    private static final long serialVersionUID = -1577261990104543756L;
    private int userID = 1;
    private static List result;
    //create single ace object for data structures
    @SuppressWarnings("unchecked")
    public XincoCoreACEServer(int attrID, XincoDBManager DBM) throws XincoException {
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = DBM.namedQuery("XincoCoreACE.findById", parameters);

            //throw exception if no result found
            if (!result.isEmpty()) {
                XincoCoreACE xca = ((XincoCoreACE) result.get(0));
                setId(xca.getId());
                setXincoCoreUserId(xca.getXincoCoreUserId());
                setXincoCoreGroupId(xca.getXincoCoreGroupId());
                setXincoCoreNodeId(xca.getXincoCoreNodeId());
                setXincoCoreDataId(xca.getXincoCoreDataId());
                setReadPermission(xca.getReadPermission());
                setWritePermission(xca.getWritePermission());
                setExecutePermission(xca.getExecutePermission());
                setAdminPermission(xca.getAdminPermission());
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            throw new XincoException();
        }

    }
    //create single ace object for data structures
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
    //write to db
    public int write2DB(XincoDBManager DBM) throws XincoException {

        try {

            String xcuid = "";
            String xcgid = "";
            String xcnid = "";
            String xcdid = "";

            int rp = 0;
            int wp = 0;
            int xp = 0;
            int ap = 0;

            //set values of nullable attributes
            if (getXincoCoreUserId() == 0) {
                xcuid = "NULL";
            } else {
                xcuid = "" + getXincoCoreUserId();
            }
            if (getXincoCoreGroupId() == 0) {
                xcgid = "NULL";
            } else {
                xcgid = "" + getXincoCoreGroupId();
            }
            if (getXincoCoreNodeId() == 0) {
                xcnid = "NULL";
            } else {
                xcnid = "" + getXincoCoreNodeId();
            }
            if (getXincoCoreDataId() == 0) {
                xcdid = "NULL";
            } else {
                xcdid = "" + getXincoCoreDataId();
            }

            //convert boolean to 0/1
            if (getReadPermission()) {
                rp = 1;
            }
            if (getWritePermission()) {
                wp = 1;
            }
            if (getExecutePermission()) {
                xp = 1;
            }
            if (getAdminPermission()) {
                ap = 1;
            }

            XincoCoreAuditServer audit = new XincoCoreAuditServer();
            if (getId() > 0) {
                AuditingDAOHelper.create(this, this);
            } else {
                setId(DBM.getNewID("xinco_core_ace"));
                AuditingDAOHelper.create(this, new XincoCoreACE());
            }
        } catch (Exception e) {
            throw new XincoException();
        }

        return getId();

    }
    //remove from db
    public static int removeFromDB(XincoCoreACE attrCACE, XincoDBManager DBM, int userID) throws XincoException, Exception {
        PersistenceServerUtils.removeFromDB((AuditableDAO) attrCACE, userID);
        return 1;
    }
    //create complete ACL for node or data
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreACL(int attrID, String attrT, XincoDBManager DBM) {
        Vector core_acl = new Vector();
        try {
            parameters.clear();

            result = DBM.createdQuery("SELECT xca FROM XincoCoreACE xca WHERE xca." + attrT + "=" +
                    attrID + " ORDER BY xca.xincoCoreUserId, xca.xincoCoreGroupId, xca.xincoCoreNodeId, " +
                    "xca.xincoCoreDataId", parameters);
            while (!result.isEmpty()) {
                core_acl.addElement((XincoCoreACEServer) result.get(0));
                result.remove(0);
            }
        } catch (Exception e) {
            core_acl.removeAllElements();
        }

        return core_acl;
    }
    //check access by comparing user / user groups to ACL and return permissions
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

    public void setUserId(int i) {
        this.userID = i;
    }

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
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

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws Exception {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreACEServer x WHERE ";
        if (parameters.containsKey("id")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by id");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.id = :id";
            counter++;
        }
        if (parameters.containsKey("xincoCoreUserId")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by xincoCoreUserId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreUserId = :xincoCoreUserId";
            counter++;
        }
        if (parameters.containsKey("xincoCoreGroupId")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by xincoCoreGroupId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreGroupId = :xincoCoreGroupId";
            counter++;
        }
        if (parameters.containsKey("xincoCoreNodeId")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by xincoCoreNodeId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreNodeId = :xincoCoreNodeId";
            counter++;
        }
        if (parameters.containsKey("xincoCoreDataId")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
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

    public AbstractAuditableObject create(AbstractAuditableObject value) {
        XincoCoreACE temp;
        XincoCoreACE newValue = new XincoCoreACE();
        temp = (XincoCoreACE) value;
        newValue.setId(temp.getId());
        newValue.setAdminPermission(temp.getAdminPermission());
        newValue.setExecutePermission(temp.getExecutePermission());
        newValue.setReadPermission(temp.getReadPermission());
        newValue.setWritePermission(temp.getWritePermission());
        newValue.setXincoCoreDataId(temp.getXincoCoreDataId());
        newValue.setXincoCoreGroupId(temp.getXincoCoreGroupId());
        newValue.setXincoCoreNodeId(temp.getXincoCoreNodeId());
        newValue.setXincoCoreUserId(temp.getXincoCoreUserId());

        newValue.setRecordId(temp.getRecordId());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO,
                    "New value created: " + newValue);
        }
        return newValue;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoCoreACE val = (XincoCoreACE) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO,
                    "Value updated: " + val);
        }
        return val;
    }

    @SuppressWarnings("unchecked")
    public void delete(AbstractAuditableObject value) {
        try {
            XincoCoreACE val = (XincoCoreACE) value;
            XincoCoreACET temp = new XincoCoreACET();
            temp.setRecordId(val.getRecordId());
            temp.setId(val.getId());

            temp.setAdminPermission(val.getAdminPermission());
            temp.setExecutePermission(val.getExecutePermission());
            temp.setReadPermission(val.getReadPermission());
            temp.setWritePermission(val.getWritePermission());
            temp.setXincoCoreDataId(val.getXincoCoreDataId());
            temp.setXincoCoreGroupId(val.getXincoCoreGroupId());
            temp.setXincoCoreNodeId(val.getXincoCoreNodeId());
            temp.setXincoCoreUserId(val.getXincoCoreUserId());

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
        return new XincoIDServer("xinco_core_ace").getNewTableID();
    }

    @SuppressWarnings("unchecked")
    public boolean write2DB() {
        try {
            if (getId() > 0) {
                AuditingDAOHelper.update(this, new XincoCoreACE(getId()));
            } else {
                XincoCoreACE temp = new XincoCoreACE();
                temp.setId(getId());
                temp.setChangerID(getChangerID());
                temp.setCreated(true);

                temp.setId(getId());
                temp.setAdminPermission(getAdminPermission());
                temp.setExecutePermission(getExecutePermission());
                temp.setReadPermission(getReadPermission());
                temp.setWritePermission(getWritePermission());
                temp.setXincoCoreDataId(getXincoCoreDataId());
                temp.setXincoCoreGroupId(getXincoCoreGroupId());
                temp.setXincoCoreNodeId(getXincoCoreNodeId());
                temp.setXincoCoreUserId(getXincoCoreUserId());

                temp = (XincoCoreACE) AuditingDAOHelper.create(this, temp);
                setId(temp.getId());
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Assigned id: " + getId());
                }
            }
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.SEVERE, null, e);
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
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }
}
