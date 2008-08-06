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
 * Name:            XincoSettingServer
 *
 * Description:     setting object
 *
 * Original Author: Javier Ortz
 * Date:            2008
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.persistence.XincoSetting;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.hibernate.audit.XincoAuditableDAO;
import com.bluecubs.xinco.core.persistence.XincoSettingT;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

/**
 *
 * @author Javier
 */
public class XincoSettingServer extends XincoSetting implements XincoAuditableDAO, PersistenceServerObject {

    private static List result;
    private static final long serialVersionUID = -6434592092601825647L;
    //create node object for data structures
    public XincoSettingServer(int attrID) throws XincoException {
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = pm.namedQuery("XincoSetting.findById", parameters);
            //throw exception if no result found
            if (!result.isEmpty()) {
                XincoSetting xs = (XincoSetting) result.get(0);
                setId(xs.getId());
                setIntValue(xs.getIntValue());
                setLongValue(xs.getLongValue());
                setBoolValue(xs.getBoolValue());
                setDescription(xs.getDescription());
                setStringValue(xs.getStringValue());
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XincoException(e.getLocalizedMessage());
        }
    }
    //create node object for data structures
    public XincoSettingServer(String desc) throws XincoException {
        try {
            parameters.clear();
            parameters.put("description", desc);
            result = pm.namedQuery("XincoSetting.findByDescription", parameters);
            //throw exception if no result found
            if (!result.isEmpty()) {
                XincoSetting xs = (XincoSetting) result.get(0);
                setId(xs.getId());
                setIntValue(xs.getIntValue());
                setLongValue(xs.getLongValue());
                setBoolValue(xs.getBoolValue());
                setDescription(xs.getDescription());
                setStringValue(xs.getStringValue());
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            throw new XincoException(e.getLocalizedMessage());
        }
    }
    //create node object for data structures
    public XincoSettingServer(int attrID, String desc, boolean bool,
            int intVal, long longVal, String stringVal) throws XincoException {
        setId(attrID);
        setIntValue(intVal);
        setLongValue(longVal);
        setBoolValue(bool);
        setDescription(desc);
        setStringValue(stringVal);
    }

    @SuppressWarnings("unchecked")
    static public XincoSetting getSetting(String desc) {
        parameters.clear();
        parameters.put("description", desc);
        result = pm.namedQuery("XincoSetting.findByDescription", parameters);
        if (result.isEmpty()) {
            Logger.getLogger(XincoSetting.class.getName()).log(Level.SEVERE,
                    ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages").
                    getString("setting.exception.notfound"));
        }
        return (XincoSetting) result.get(0);
    }

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
        result = pm.namedQuery("XincoCoreSetting.findById", parameters);
        if (result.size() > 0) {
            XincoSetting temp = (XincoSetting) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws Exception {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreSetting x WHERE ";
        if (parameters.containsKey("id")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoSetting.class.getName()).log(Level.INFO, "Searching by id");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.id = :id";
            counter++;
        }
        if (parameters.containsKey("description")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoSetting.class.getName()).log(Level.INFO, "Searching by description");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.description = :description";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoSettingServer temp[] = new XincoSettingServer[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoSettingServer) result.get(0);
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
        XincoSetting temp;
        XincoSetting newValue = new XincoSetting();
        temp = (XincoSetting) value;
        temp.setId(temp.getId());
        temp.setIntValue(getIntValue());
        temp.setLongValue(getLongValue());
        temp.setBoolValue(getBoolValue());
        temp.setDescription(getDescription());
        temp.setStringValue(getStringValue());

        newValue.setRecordId(temp.getRecordId());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoSetting.class.getName()).log(Level.INFO, "New value created: " + newValue);
        }
        return newValue;
    }

    public static Vector getXincoSettings() {
        Vector settings = new Vector();
        result = pm.namedQuery("XincoSetting.findAll", null);
        while (!result.isEmpty()) {
            settings.add((XincoSetting) result.get(0));
            result.remove(0);
        }
        return settings;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoSetting val = (XincoSetting) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoSetting.class.getName()).log(Level.INFO, "Value updated: " + val);
        }
        return val;
    }

    @SuppressWarnings("unchecked")
    public void delete(AbstractAuditableObject value) {
        try {
            XincoSetting val = (XincoSetting) value;
            XincoSettingT temp = new XincoSettingT();
            temp.setRecordId(val.getRecordId());
            temp.setId(val.getId());

            temp.setIntValue(val.getIntValue());
            temp.setLongValue(val.getLongValue());
            temp.setBoolValue(val.getBoolValue());
            temp.setDescription(val.getDescription());
            temp.setStringValue(val.getStringValue());

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
        return new XincoIDServer("XincoCoreLanguage").getNewTableID();
    }

    @SuppressWarnings("unchecked")
    public boolean write2DB() {
        try {
            if (getId() > 0) {
                AuditingDAOHelper.update(this, new XincoSetting());
            } else {
                XincoSetting temp = new XincoSetting();
                temp.setChangerID(getChangerID());
                temp.setCreated(true);

                temp.setId(getId());
                temp.setIntValue(getIntValue());
                temp.setLongValue(getLongValue());
                temp.setBoolValue(getBoolValue());
                temp.setDescription(getDescription());
                temp.setStringValue(getStringValue());

                temp = (XincoSetting) AuditingDAOHelper.create(this, temp);
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
            AuditingDAOHelper.delete(this, getId());
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoSetting.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }
}
