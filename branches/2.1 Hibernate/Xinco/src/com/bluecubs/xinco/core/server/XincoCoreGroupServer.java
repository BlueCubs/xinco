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
 * Name:            XincoCoreGroupServer
 *
 * Description:     group
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
import java.util.Vector;
import com.bluecubs.xinco.core.persistence.XincoCoreGroup;
import com.bluecubs.xinco.core.persistence.XincoCoreGroupT;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditableDAO;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

public class XincoCoreGroupServer extends XincoCoreGroup implements AuditableDAO, PersistenceServerObject {

    private static List result;
    private static final long serialVersionUID = 6866425466359539411L;
    //create group object for data structures
    @SuppressWarnings("unchecked")
    public XincoCoreGroupServer(int attrID, XincoDBManager DBM) throws XincoException {
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = DBM.namedQuery("XincoCoreGroup.findById", parameters);
            //throw exception if no result found
            if (!result.isEmpty()) {
                XincoCoreGroup xcg = (XincoCoreGroup) result.get(0);
                setId(xcg.getId());
                setDesignation(xcg.getDesignation());
                setStatusNumber(xcg.getStatusNumber());
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            throw new XincoException(e.getMessage());
        }
    }
    //create group object for data structures
    public XincoCoreGroupServer(int attrID, String attrD, int attrSN) throws XincoException {
        setId(attrID);
        setDesignation(attrD);
        setStatusNumber(attrSN);
    }

    XincoCoreGroupServer() {
    }
    
    //create complete list of groups
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreGroups(XincoDBManager DBM) {
        Vector coreGroups = new Vector();
        try {
            result = DBM.createdQuery("SELECT x FROM XincoCoreGroup x ORDER BY x.designation", null);
            while (!result.isEmpty()) {
                coreGroups.addElement((XincoCoreGroup) result.get(0));
                result.remove(0);
            }
        } catch (Exception e) {
            coreGroups.removeAllElements();
        }
        return coreGroups;
    }

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
        result = pm.namedQuery("XincoCoreGroup.findById", parameters);
        if (result.size() > 0) {
            XincoCoreGroupServer temp = (XincoCoreGroupServer) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws Exception {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreGroup x WHERE ";
        if (parameters.containsKey("id")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.INFO, "Searching by id");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.id = :id";
            counter++;
        }
        if (parameters.containsKey("designation")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.INFO, "Searching by designation");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.designation = :designation";
            counter++;
        }
        if (parameters.containsKey("statusNumber")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.INFO, "Searching by status number");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.statusNumber = :statusNumber";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoCoreGroupServer temp[] = new XincoCoreGroupServer[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreGroupServer) result.get(0);
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
        XincoCoreGroupServer temp;
        XincoCoreGroup newValue = new XincoCoreGroup();

        temp = (XincoCoreGroupServer) value;
        temp.setId(temp.getId());
        temp.setDesignation(temp.getDesignation());
        temp.setStatusNumber(temp.getStatusNumber());

        newValue.setRecordId(temp.getRecordId());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.INFO,
                    "New value created: " + newValue);
        }
        return newValue;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoCoreGroupServer val = (XincoCoreGroupServer) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.INFO,
                    "Value updated: " + val);
        }
        return val;
    }

    @SuppressWarnings("unchecked")
    public void delete(AbstractAuditableObject value) {
        try {
            XincoCoreGroup val = (XincoCoreGroup) value;
            XincoCoreGroupT temp = new XincoCoreGroupT();
            temp.setRecordId(val.getRecordId());
            temp.setId(val.getId());

            temp.setDesignation(val.getDesignation());
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
        return new XincoIDServer("xincoCoreAce").getNewTableID();
    }

    @SuppressWarnings("unchecked")
    public boolean write2DB() {
        try {
            if (getId() > 0) {
                AuditingDAOHelper.update(this, new XincoCoreGroup());
            } else {
                XincoCoreGroup temp = new XincoCoreGroup();
                temp.setChangerID(getChangerID());
                temp.setCreated(true);

                temp.setId(getId());
                temp.setDesignation(getDesignation());
                temp.setStatusNumber(getStatusNumber());

                temp = (XincoCoreGroup) AuditingDAOHelper.create(this, temp);
                setId(temp.getId());
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.INFO, "Assigned id: " + getId());
                }
            }
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, e);
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
                Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }
}
