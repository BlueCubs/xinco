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
 * Name:            XincoIDServer
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

import com.bluecubs.xinco.core.hibernate.audit.XincoAuditableDAO;
import com.bluecubs.xinco.core.persistence.XincoID;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

/**
 *
 * @author Javier
 */
public class XincoIDServer extends XincoID implements XincoAuditableDAO, PersistenceServerObject {

    private static final long serialVersionUID = 5985918988923778727L;
    private List result;

    public XincoIDServer(String tablename, int id, int lastId) {
        setTablename(tablename);
        setId(id);
        setLastId(lastId);
    }

    @SuppressWarnings("unchecked")
    public XincoIDServer(String tablename) {
        setTablename(tablename.toLowerCase());
        parameters.clear();
        parameters.put("tablename", tablename);
        result = pm.namedQuery("XincoID.findByTablename", parameters);
        if (result.size() > 0) {
            XincoID temp = (XincoID) result.get(0);
            setLastId(temp.getLastId());
            setId(temp.getId());
        }
    }

    public XincoIDServer(Integer id, String tablename, int lastID) {
        super(id, tablename, lastID);
    }

    XincoIDServer() {
        super();
    }

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
        result = pm.namedQuery("XincoID.findById", parameters);
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
        int counter = 0;
        String sql = "SELECT x FROM XincoID x WHERE ";
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
        if (parameters.containsKey("lastID")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Searching by lastID");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.lastID = :lastID";
            counter++;
        }
        if (parameters.containsKey("tablename")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreNodeServer.class.getName()).log(Level.INFO, "Searching by status tablename");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.tablename = :tablename";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
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

    @SuppressWarnings("static-access")
    public AbstractAuditableObject create(AbstractAuditableObject value) throws Exception {
        XincoID temp;
        XincoID newValue = new XincoID();

        temp = (XincoID) value;
        newValue.setId(temp.getId());
        newValue.setTablename(temp.getTablename());
        newValue.setRecordId(temp.getRecordId());
        newValue.setLastId(temp.getLastId());

        newValue.setRecordId(temp.getRecordId());
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

    public AbstractAuditableObject update(AbstractAuditableObject value) throws Exception {
        XincoID val = (XincoID) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoIDServer.class.getName()).log(Level.INFO,
                    "Value updated: " + val);
        }
        return val;
    }

    @SuppressWarnings({"unchecked", "static-access"})
    public boolean delete(AbstractAuditableObject value) throws Exception {
        XincoID val = null;
        try {
            val = (XincoID) value;
            XincoCoreUserHasXincoCoreGroupServer x = new XincoCoreUserHasXincoCoreGroupServer(1, val.getChangerID());
            return pm.delete(val, true);
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.WARNING,
                    "User with id: " + val.getChangerID() + " has no permission to delete this record.", ex);
            return false;
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
     * @param atomic
     * @return New last ID
     */
    @SuppressWarnings("unchecked")
    public int getNewTableID(boolean atomic) {
        HashMap p = new HashMap();
        p.put("tablename", getTablename());
        result = pm.namedQuery("XincoID.findByTablename", p);
        if (result.size() > 0) {
            try {
                XincoID temp = (XincoID) result.get(0);
                setId(temp.getId());
                setLastId(temp.getLastId() + 1);
                setTablename(temp.getTablename());
                setTransactionTime(DateRange.startingNow());
                pm.persist(new XincoID(getId(), getTablename(), getLastId()), true, atomic);
                return getLastId();
            } catch (Throwable ex) {
                Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE,
                        rb.getString("error.xinco_id.wrong_table").replaceAll("%t", getTablename()), ex);
                return -1;
            }
        } else {
            Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE,
                    rb.getString("error.xinco_id.wrong_table").replaceAll("%t", getTablename()));
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
                temp.setChangerID(getChangerID());
                temp.setCreated(true);

                temp.setTablename(getTablename());
                temp.setLastId(getLastId());
                System.out.println(temp);
                temp = (XincoID) AuditingDAOHelper.create(this, temp);
                setId(temp.getId());
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
            return AuditingDAOHelper.delete(this, getId(), getChangerID());
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoIDServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public int getNewID(boolean a) {
        return getNewTableID(a);
    }

    public Object transform() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
