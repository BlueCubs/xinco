/**
 *Copyright 2008 blueCubs.com
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
 * Name:            XincoIDServer
 *
 * Description:     Table ID's
 *
 * Original Author: Javier A. Ortiz
 * Date:            2008
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.exception.XincoSettingException;
import com.bluecubs.xinco.persistence.XincoCoreGroup;
import com.bluecubs.xinco.persistence.XincoId;
import com.bluecubs.xinco.persistence.audit.XincoIdT;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditableDAO;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoIDServer extends XincoId implements AuditableDAO, PersistenceServerObject {

    private List result;

    @SuppressWarnings("unchecked")
    public XincoIDServer(String table) {
        try {
            pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        XincoId temp;
        HashMap p = new HashMap();
        p.put("tablename", table);
        result = pm.namedQuery("XincoId.findByTablename", p);
        if (result.size() > 0) {
            temp = ((XincoId) result.get(0));
            setLastId(temp.getLastId());
            setTablename(temp.getTablename());
        } else {
            Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, "Parameter table name is incorrect: " + table);
        }
    }

    public XincoIDServer(String table, int lastId){
        try {
            pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        setTablename(table);
        setLastId(lastId);
    }

    public XincoIDServer() {
        try {
            pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get a new id
     * @return New last ID
     */
    @SuppressWarnings("unchecked")
    public int getNewTableID() {
        HashMap p = new HashMap();
        p.put("tablename", getTablename());
        result = pm.namedQuery("XincoId.findByTablename", p);
        if (result.size() > 0) {
            try {
                XincoId temp = (XincoId) result.get(0);
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoIDServer.class.getName()).log(Level.INFO, 
                            "Setting last id as: " + (temp.getLastId() + 1)+" for table: "+getTablename());
                }
                setLastId(temp.getLastId() + 1);
                setTablename(temp.getTablename());
                setTransactionTime(DateRange.startingNow());
                pm.persist(new XincoId(getTablename(), getLastId()), true, true);
                return getLastId();
            } catch (Throwable ex) {
                Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }
        } else {
            return -1;
        }
    }

    public AbstractAuditableObject findById(HashMap parameters) {
        result = pm.namedQuery("XincoId.findByTablename", parameters);
        if (result.size() > 0) {
            XincoId temp = (XincoId) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) {
        result = pm.createdQuery("SELECT x FROM XincoId x WHERE x.lastId = :lastId", parameters);
        if (result.size() > 0) {
            XincoId temp[] = new XincoId[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoId) result.get(0);
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
        try {
            XincoId temp;
            XincoId newValue = new XincoId();
            temp = (XincoId) value;
            newValue.setTablename(temp.getTablename());
            newValue.setRecordId(temp.getRecordId());
            newValue.setLastId(temp.getLastId());
            newValue.setCreated(temp.isCreated());
            newValue.setChangerID(temp.getChangerID());
            newValue.setTransactionTime(getTransactionTime());
            pm.persist(newValue, false, true);
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoIDServer.class.getName()).log(Level.INFO, 
                        "New value created: " + newValue);
            }
            return newValue;
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        try {
            XincoId val = (XincoId) value;
            XincoIdT temp = new XincoIdT();
            temp.setTablename(val.getTablename());
            temp.setLastId(val.getLastId());
            temp.setRecordId(val.getRecordId());
            pm.startTransaction();
            pm.persist(temp, false, false);
            pm.persist(val, true, false);
            getModifiedRecordDAOObject().saveAuditData();
            pm.commitAndClose();
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoIDServer.class.getName()).log(Level.INFO, 
                        "Value updated: " + val);
            }
            return val;
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void delete(AbstractAuditableObject value) {
        try {
            XincoId tempId = (XincoId) value;
            XincoCoreGroupServer group = new XincoCoreGroupServer();
            XincoCoreGroup g;
            parameters.clear();
            parameters.put("designation", "general.group.admin");
            //Delete only if member of admin group
            if (group.findWithDetails(parameters).length > 0) {
                g = (XincoCoreGroup) group.findWithDetails(parameters)[0];
                parameters.clear();
                parameters.put("xincoCoreUserId", tempId.getChangerID());
                parameters.put("xincoCoreGroupId", g.getId());
                if (!pm.createdQuery("SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE " +
                        "x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreUserId = :xincoCoreUserId and " +
                        "x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreGroupId = :xincoCoreGroupId", parameters).isEmpty()) {
                    XincoId val = (XincoId) value;
                    XincoIdT temp = new XincoIdT();
                    temp.setRecordId(val.getRecordId());
                    temp.setTablename(val.getTablename());
                    temp.setLastId(val.getLastId());
                    pm.startTransaction();
                    pm.persist(temp, false, false);
                    pm.delete(val, false);
                    getModifiedRecordDAOObject().saveAuditData();
                    pm.commitAndClose();
                }
            }
        } catch (Throwable ex) {
            Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("tablename", getTablename());
        return temp;
    }

    @SuppressWarnings("unchecked")
    public boolean write2DB() {
        try {
            parameters.clear();
            parameters.put("tablename", getTablename());
            result = pm.namedQuery("XincoId.findByTablename", parameters);
            if (result.size() > 0) {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoIDServer.class.getName()).log(Level.INFO, 
                        "Updating table: " + getTablename());
                }
                setTransactionTime(DateRange.startingNow());
                AuditingDAOHelper.update(this, new XincoId(getTablename(), getLastId()));
            } else {
                XincoId temp = new XincoId();
                temp.setTablename(getTablename());
                temp.setLastId(getLastId());
                temp.setChangerID(getChangerID());
                temp.setCreated(true);
                AuditingDAOHelper.create(this, temp);
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoIDServer.class.getName()).log(Level.INFO, "Id entry created for: " + getTablename());
                }
            }
            return true;
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, e);
                }
                return false;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }

    public boolean deleteFromDB() {
        setTransactionTime(DateRange.startingNow());
        try {
            AuditingDAOHelper.delete(this, getTablename());
            return true;
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, e);
                }
                return false;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public int getNewID() {
        try {
            result = pm.executeQuery("select count(p.xincoCoreUserModifiedRecordPK.recordId) from XincoCoreUserModifiedRecord p");
            Long id = (Long) (result.get(0)) + 1;
            return Integer.parseInt(id.toString());
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, e);
                }
                return -1;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }
        }
    }
}
