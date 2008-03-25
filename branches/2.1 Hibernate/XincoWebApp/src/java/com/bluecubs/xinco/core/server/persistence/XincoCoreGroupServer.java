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
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.core.exception.XincoSettingException;
import com.bluecubs.xinco.persistence.XincoCoreGroup;
import com.bluecubs.xinco.persistence.audit.XincoCoreGroupT;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditableDAO;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

/**
 * Create group object for data structures
 * @author Alexander Manes
 */
public class XincoCoreGroupServer extends XincoCoreGroup implements AuditableDAO, PersistenceServerObject {

    private static List result;

    /**
     * Create group object for data structures
     * @param id
     * @throws com.bluecubs.xinco.core.exception.XincoException 
     */
    @SuppressWarnings("unchecked")
    public XincoCoreGroupServer(int id) throws XincoException {
        pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        try {
            parameters.clear();
            parameters.put("id", id);
            result = pm.namedQuery("XincoCoreGroup.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                XincoCoreGroup temp = (XincoCoreGroup) result.get(0);
                setId(temp.getId());
                setDesignation(temp.getDesignation());
                setStatusNumber(temp.getStatusNumber());
            } else {
                throw new XincoException();
            }
        } catch (Throwable e) {
            throw new XincoException(e.getMessage());
        }
    }

    public XincoCoreGroupServer() {
        try {
            pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Create group object for data structures
     * @param id
     * @param designation
     * @param status
     * @throws com.bluecubs.xinco.core.exception.XincoException
     */
    public XincoCoreGroupServer(int id, String designation, int status) throws XincoException {
        pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        setId(id);
        setDesignation(designation);
        setStatusNumber(status);
    }

    /**
     * Create complete list of groups
     * @return A vector containing all Core Groups
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreGroups() {
        Vector coreGroups = new Vector();
        try {
            result = pm.executeQuery("select p from XincoCoreGroup p order by p.designation");
            while (!result.isEmpty()) {
                coreGroups.add((XincoCoreGroup) result.get(0));
                result.remove(0);
            }
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.INFO, null, e);
                }
                coreGroups.removeAllElements();
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return coreGroups;
    }

    public AbstractAuditableObject findById(HashMap parameters) throws XincoException {
        result = pm.namedQuery("XincoCoreGroup.findById", parameters);
        if (result.size() > 0) {
            XincoCoreGroup temp = (XincoCoreGroup) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws XincoException, XincoSettingException {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreGroup x WHERE ";
        if (parameters.containsKey("designation")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.INFO, "Searching by designation");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.designation = :designation";
            counter++;
        }
        if (parameters.containsKey("statusNumber")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.INFO, "Searching by statusNumber");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.statusNumber = :statusNumber";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoCoreGroup temp[] = new XincoCoreGroup[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreGroup) result.get(0);
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
        XincoCoreGroup temp, newValue = new XincoCoreGroup();
        boolean exists = false;
        temp = (XincoCoreGroup) value;
        if (!value.isCreated()) {
            newValue.setId(temp.getId());
            newValue.setRecordId(temp.getRecordId());
        } else {
            newValue.setId(new XincoIDServer("xinco_setting").getNewTableID());
        }
        newValue.setId(temp.getId());
        newValue.setDesignation(temp.getDesignation());
        newValue.setStatusNumber(temp.getStatusNumber());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        if (!value.isCreated()) {
            if (((XincoCoreGroup) value).getId() != 0) {
                //An object for updating
                exists = true;
            } else {
                //A new object
                exists = false;
            }
        }
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, exists, true);
        return newValue;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoCoreGroup val = (XincoCoreGroup) value;
        XincoCoreGroupT temp = new XincoCoreGroupT();
        temp.setRecordId(val.getRecordId());
        temp.setDesignation(val.getDesignation());
        temp.setStatusNumber(val.getStatusNumber());
        if (!value.isCreated()) {
            temp.setId(val.getId());
        } else {
            temp.setId(val.getRecordId());
        }
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.persist(val, true, false);
        getModifiedRecordDAOObject().saveAuditData();
        pm.commitAndClose();
        return val;
    }

    public void delete(AbstractAuditableObject value) {
        XincoCoreGroup val = (XincoCoreGroup) value;
        XincoCoreGroupT temp = new XincoCoreGroupT();
        temp.setRecordId(val.getRecordId());
        temp.setDesignation(val.getDesignation());
        temp.setStatusNumber(val.getStatusNumber());
        temp.setId(val.getId());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.delete(val, false);
        getModifiedRecordDAOObject().saveAuditData();
        pm.commitAndClose();
    }

    @SuppressWarnings("unchecked")
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("id", getId());
        return temp;
    }

    public int getNewID() {
        return new XincoIDServer("xinco_core_group").getNewTableID();
    }

    /**
     * Persist object
     * @return 
     */
    public boolean write2DB() {
        try {
            if (getId() > 0) {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.INFO, "Updating XincoCoreGroup: " + getDesignation());
                }
                AuditingDAOHelper.update(this, new XincoCoreGroup(getId()));
            } else {
                XincoCoreGroup temp = new XincoCoreGroup();
                temp.setId(getId());
                temp.setChangerID(getChangerID());
                temp.setCreated(true);
                temp.setDesignation(getDesignation());
                temp.setStatusNumber(getStatusNumber());
                temp = (XincoCoreGroup) AuditingDAOHelper.create(this, temp);
                setId(temp.getId());
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.INFO, "Creating XincoCoreGroup: " + getDesignation());
                }
            }
            return true;
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, e);
                }
                return false;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }

    public boolean deleteFromDB() {
        setTransactionTime(DateRange.startingNow());
        try {
            AuditingDAOHelper.delete(this, getId());
            return true;
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, e);
                }
                return false;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }
}
