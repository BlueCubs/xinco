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
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistance.XincoCoreGroup;
import com.bluecubs.xinco.core.persistance.XincoId;
import com.bluecubs.xinco.core.persistance.audit.XincoIdT;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditableDAO;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditingDAOHelper;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoIDServer extends XincoId implements XincoAuditableDAO, XincoPersistanceServerObject {

    private List result;

    @SuppressWarnings("unchecked")
    public XincoIDServer(String table) {
        pm.setDeveloperMode(new XincoSettingServer("setting.enable.developermode").getBoolValue());
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

    public XincoIDServer(String table, int lastId) {
        pm.setDeveloperMode(new XincoSettingServer("setting.enable.developermode").getBoolValue());
        setTablename(table);
        setLastId(lastId);
    }

    public XincoIDServer() {
        pm.setDeveloperMode(new XincoSettingServer("setting.enable.developermode").getBoolValue());
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
                if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                    System.out.println("Setting last id as: " + (temp.getLastId() + 1));
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

    public XincoAbstractAuditableObject findById(HashMap parameters) throws DataRetrievalFailureException {
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

    public XincoAbstractAuditableObject[] findWithDetails(HashMap parameters) throws DataRetrievalFailureException {
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

    public XincoAbstractAuditableObject create(XincoAbstractAuditableObject value) {
        XincoId temp, newValue = new XincoId();
        temp = (XincoId) value;
        newValue.setTablename(temp.getTablename());
        newValue.setRecordId(temp.getRecordId());
        newValue.setLastId(temp.getLastId());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
            System.out.println("New value created: " + newValue);
        }
        return newValue;
    }

    public XincoAbstractAuditableObject update(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoId val = (XincoId) value;
        XincoIdT temp = new XincoIdT();
        temp.setTablename(val.getTablename());
        temp.setLastId(val.getLastId());
        temp.setRecordId(val.getRecordId());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.persist(val, true, false);
        val.saveAuditData(pm);
        pm.commitAndClose();
        if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
            System.out.println("Value updated: " + val);
        }
        return val;
    }

    @SuppressWarnings("unchecked")
    public void delete(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
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
                    val.saveAuditData(pm);
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
    public boolean write2DB() throws XincoException {
        try {
            parameters.clear();
            parameters.put("tablename", getTablename());
            result = pm.namedQuery("XincoId.findByTablename", parameters);
            if (result.size() > 0) {
                if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                    System.out.println("Updating table: " + getTablename());
                }
                setTransactionTime(DateRange.startingNow());
                XincoAuditingDAOHelper.update(this, new XincoId(getTablename(), getLastId()));
            } else {
                XincoId temp = new XincoId();
                temp.setTablename(getTablename());
                temp.setLastId(getLastId());
                temp.setChangerID(getChangerID());
                temp.setCreated(true);
                XincoAuditingDAOHelper.create(this, temp);
                if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoIDServer.class.getName()).log(Level.INFO, "Id entry created for: " + getTablename());
                }
            }
            return true;
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    public boolean deleteFromDB() throws XincoException {
        setTransactionTime(DateRange.startingNow());
        try {
            XincoAuditingDAOHelper.delete(this, getTablename());
            return true;
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    @SuppressWarnings("unchecked")
    public int getNewID() {
        try {
            result = pm.executeQuery("select count(p.xincoCoreUserModifiedRecordPK.recordId) from XincoCoreUserModifiedRecord p");
            Long id = (Long) (result.get(0)) + 1;
            return Integer.parseInt(id.toString());
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return -1;
        }
    }
}
