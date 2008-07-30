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
 * Name:            XincoCoreNodeServer
 *
 * Description:     id object
 *
 * Original Author: Javier Ortz
 * Date:            2007
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.persistence.XincoCoreGroup;
import com.bluecubs.xinco.core.persistence.XincoID;
import com.bluecubs.xinco.core.hibernate.audit.AbstractAuditableObject;
import com.bluecubs.xinco.core.hibernate.audit.AuditableDAO;
import com.bluecubs.xinco.core.hibernate.audit.AuditingDAOHelper;
import com.bluecubs.xinco.core.hibernate.audit.PersistenceServerObject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

/**
 *
 * @author Javier
 */
public class XincoIDServer extends XincoID implements AuditableDAO, PersistenceServerObject {

    private static final long serialVersionUID = 5985918988923778727L;
    private List result;

    public XincoIDServer(String tablename, Integer id) {
        super(id);
        setTablename(tablename);
    }

    public XincoIDServer(String tablename) {
        setTablename(tablename);
        result = pm.namedQuery("XincoID.findByTablename", getParameters());
        if (result.size() > 0) {
            XincoID temp = (XincoID) result.get(0);
            setLastId(temp.getLastId());
        }
    }

    public XincoIDServer(Integer id, String tablename, int lastID) {
        super(id, tablename, lastID);
    }

    XincoIDServer() {
        super();
    }

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
        result = pm.namedQuery("XincoID.findByTablename", parameters);
        if (result.size() > 0) {
            XincoID temp = (XincoID) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws Exception {
        result = pm.createdQuery("SELECT x FROM XincoID x WHERE x.lastID = :lastID", parameters);
        if (result.size() > 0) {
            XincoID temp[] = new XincoID[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoID) result.get(0);
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
        XincoID temp;
        XincoID newValue = new XincoID();
        temp = (XincoID) value;
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
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoID val = (XincoID) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoIDServer.class.getName()).log(Level.INFO,
                    "Value updated: " + val);
        }
        return val;
    }

    @SuppressWarnings("unchecked")
    public void delete(AbstractAuditableObject value) {
        try {
            XincoID tempID = (XincoID) value;
            XincoCoreGroupServer group = new XincoCoreGroupServer();
            XincoCoreGroup g;
            parameters.clear();
            parameters.put("designation", "general.group.admin");
            //Delete only if member of admin group
            if (group.findWithDetails(parameters).length > 0) {
                g = (XincoCoreGroup) group.findWithDetails(parameters)[0];
                parameters.clear();
                parameters.put("XincoCoreUserID", tempID.getChangerID());
                parameters.put("XincoCoreGroupID", g.getId());
                if (!pm.createdQuery("SELECT x FROM XincoCoreUserHasXincoGroup x WHERE " +
                        "x.XincoCoreUserHasXincoGroupPK.XincoUserID = :XincoCoreUserID and " +
                        "x.XincoCoreUserHasXincoGroupPK.XincoGroupID = :XincoCoreGroupID", parameters).isEmpty()) {
                    XincoID val = (XincoID) value;
                    pm.delete(val, true);
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

    /**
     * Get a new newID
     * @return New last ID
     */
    @SuppressWarnings("unchecked")
    public int getNewTableID() {
        HashMap p = new HashMap();
        p.put("tablename", getTablename());
        result = pm.namedQuery("XincoID.findByTablename", p);
        if (result.size() > 0) {
            try {
                XincoID temp = (XincoID) result.get(0);
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoIDServer.class.getName()).log(Level.INFO,
                            "Setting last id as: " + (temp.getLastId() + 1) + " for table: " + getTablename());
                }
                setLastId(temp.getLastId() + 1);
                setTablename(temp.getTablename());
                setTransactionTime(DateRange.startingNow());
                pm.persist(new XincoID(getId(), getTablename(), getLastId()), true, true);
                return getLastId();
            } catch (Throwable ex) {
                Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }
        } else {
            return -1;
        }
    }

    @SuppressWarnings("unchecked")
    public boolean write2DB() {
        try {
            parameters.clear();
            parameters.put("tablename", getTablename());
            result = pm.namedQuery("XincoID.findByTablename", parameters);
            if (result.size() > 0) {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoIDServer.class.getName()).log(Level.INFO,
                            "Updating table: " + getTablename());
                }
                setTransactionTime(DateRange.startingNow());
                AuditingDAOHelper.update(this, new XincoID(getId(), getTablename(), getLastId()));
            } else {
                XincoID temp = new XincoID();
                temp.setTablename(getTablename());
                temp.setLastId(getLastId());
                temp.setChangerID(getChangerID());
                temp.setCreated(true);
                AuditingDAOHelper.create(this, temp);
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoIDServer.class.getName()).log(Level.INFO, "ID entry created for: " + getTablename());
                }
            }
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public boolean deleteFromDB() {
        setTransactionTime(DateRange.startingNow());
        try {
            AuditingDAOHelper.delete(this, getParameters());
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public int getNewID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
