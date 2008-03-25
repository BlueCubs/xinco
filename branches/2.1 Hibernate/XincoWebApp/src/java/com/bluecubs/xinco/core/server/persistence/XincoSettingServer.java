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
 * Description:     Xinco Setings
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

import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.core.exception.XincoSettingException;
import com.bluecubs.xinco.persistence.XincoSetting;
import com.bluecubs.xinco.persistence.audit.XincoSettingT;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditableDAO;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoSettingServer extends XincoSetting implements AuditableDAO, PersistenceServerObject {

    private Vector xinco_settings = null;
    private int changerID;
    private List result;

    /** Creates a new instance of XincoSettingServer
     * @param id
     * @param description
     * @param IntValue
     * @param StringValue
     * @param BoolValue
     * @param LongValue
     * @param changerID
     * @throws com.bluecubs.xinco.core.exception.XincoException 
     */
    public XincoSettingServer(int id, java.lang.String description, int IntValue,
            java.lang.String StringValue, boolean BoolValue, BigInteger LongValue, int changerID) throws XincoException {
        pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        setId(id);
        setDescription(description);
        setIntValue(IntValue);
        setStringValue(StringValue);
        setBoolValue(BoolValue);
        setChangerID(changerID);
        setLongValue(LongValue);
    }

    @Override
    public Integer getRecordId() {
        return getId();
    }

    /**
     * Creates a new instance of XincoSettingServer
     * @param id
     */
    public XincoSettingServer(int id) {
        try {
            pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoSettingServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        XincoSetting temp;
        result = pm.executeQuery("SELECT p FROM XincoSetting p WHERE p.id ='" + id + "'");
        if (result.size() > 0) {
            temp = (XincoSetting) result.get(0);
            setId(temp.getId());
            setDescription(temp.getDescription());
            setIntValue(temp.getIntValue());
            setStringValue(temp.getStringValue());
            setBoolValue(temp.getBoolValue());
            setLongValue(temp.getLongValue());
        } else {
            Logger.getLogger(XincoSettingServer.class.getName()).log(Level.INFO, "Setting not found");
        }
    }

    /**
     * Creates a new instance of XincoSettingServer
     * @param key 
     */
    @SuppressWarnings("unchecked")
    public XincoSettingServer(String key) {
        try {
            pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoSettingServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        XincoSetting temp;
        HashMap p = new HashMap();
        p.put("description", key);
        result = pm.namedQuery("XincoSetting.findByDescription", p);
        if (result.size() > 0) {
            temp = (XincoSetting) result.get(0);
            setId(temp.getId());
            setDescription(temp.getDescription());
            setIntValue(temp.getIntValue());
            setStringValue(temp.getStringValue());
            setBoolValue(temp.getBoolValue());
            setLongValue(temp.getLongValue());
        } else {
            Logger.getLogger(XincoSettingServer.class.getName()).log(Level.INFO, "Setting not found");
        }
    }

    /**
     * Constructor
     */
    public XincoSettingServer() {
        try {
            pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoSettingServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get setting
     * @param i
     * @return @link XincoSetting
     */
    public XincoSetting getSetting(int i) {
        if (i < getXincoSettings().size()) {
            return (XincoSetting) getXincoSettings().get(i);
        } else {
            for (int j = 0; j < getXincoSettings().size(); j++) {
                if (((XincoSetting) getXincoSettings().get(j)).getId() == i) {
                    return (XincoSetting) getXincoSettings().get(j);
                }
            }
        }
        return null;
    }

    /**
     * Get setting
     * @param s
     * @return XincoSetting
     * @throws com.bluecubs.xinco.core.exception.XincoSettingException 
     * @link XincoSetting
     */
    @SuppressWarnings("unchecked")
    public static XincoSetting getSetting(String s) throws XincoSettingException {
        XincoSetting temp = null;
        try {
            parameters.clear();
            parameters.put("description", s);
            temp = (XincoSetting) pm.namedQuery("XincoSetting.findByDescription", parameters).get(0);
        } catch (Throwable e) {
            Logger.getLogger(XincoSettingServer.class.getName()).log(Level.SEVERE, "Error retrieving setting: " + s, e);
            throw new XincoSettingException("Error retrieving setting: " + s);
        }
        return temp;
    }

    @SuppressWarnings("unchecked")
    public Vector getXincoSettings() {
        if (xinco_settings == null) {
            Vector temp = new Vector();
            try {
                result = pm.executeQuery("SELECT p FROM XincoSetting p");
                while (!result.isEmpty()) {
                    temp.add((XincoSetting) result.get(0));
                    result.remove(0);
                }
                setXincoSettings(temp);
            } catch (Throwable ex) {
                Logger.getLogger(XincoSettingServer.class.getName()).log(Level.SEVERE, "Error retrieving settings.", ex);
            }
        }
        return xinco_settings;
    }

    public void setXincoSettings(Vector xinco_settings) {
        this.xinco_settings = xinco_settings;
    }

    @Override
    public int getChangerID() {
        return changerID;
    }

    @Override
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }

    public boolean deleteFromDB() {
        setTransactionTime(DateRange.startingNow());
        try {
            AuditingDAOHelper.delete(this, getId());
            return true;
        } catch (Throwable e) {
            Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public boolean write2DB() {
        setTransactionTime(DateRange.startingNow());
        try {
            //Object exists
            if (getId() != null) {
                AuditingDAOHelper.update(this, new XincoSetting(getId()));
            } else {
                XincoSetting temp = new XincoSetting();
                temp.setBoolValue(getBoolValue());
                temp.setDescription(getDescription());
                temp.setId(getId());
                temp.setIntValue(getIntValue());
                temp.setLongValue(getLongValue());
                temp.setStringValue(getStringValue());
                temp.setChangerID(getChangerID());
                AuditingDAOHelper.create(this, temp);
            }
            return true;
        } catch (Throwable e) {
            Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public AbstractAuditableObject findById(HashMap parameters) throws XincoSettingException {
        result = pm.namedQuery("XincoSetting.findById", parameters);
        if (result.size() > 0) {
            XincoSetting temp = (XincoSetting) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            throw new XincoSettingException();
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws XincoSettingException {
        result = pm.namedQuery("XincoSetting.findByDescription", parameters);
        if (result.size() > 0) {
            XincoSetting temp[] = new XincoSetting[1];
            temp[0] = (XincoSetting) result.get(0);
            temp[0].setTransactionTime(getTransactionTime());
            return temp;
        } else {
            throw new XincoSettingException();
        }
    }

    @Override
    public AbstractAuditableObject create(AbstractAuditableObject value) {
        XincoSetting temp, newValue = new XincoSetting();
        boolean exists = false;
        temp = (XincoSetting) value;
        if (!value.isCreated()) {
            newValue.setId(temp.getId());
            newValue.setRecordId(temp.getRecordId());
        } else {
            newValue.setId(getNewID());
        }
        newValue.setBoolValue(temp.getBoolValue());
        newValue.setDescription(temp.getDescription());
        newValue.setIntValue(temp.getIntValue());
        newValue.setLongValue(temp.getLongValue());
        newValue.setStringValue(temp.getStringValue());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        if (!value.isCreated()) {
            if (newValue.getId() != 0) {
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

    @Override
    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoSetting val = (XincoSetting) value;
        XincoSettingT temp = new XincoSettingT();
        temp.setRecordId(val.getRecordId());
        temp.setBoolValue(val.getBoolValue());
        temp.setDescription(val.getDescription());
        if (!value.isCreated()) {
            temp.setId(val.getId());
        } else {
            temp.setId(val.getRecordId());
        }
        temp.setIntValue(val.getIntValue());
        temp.setLongValue(val.getLongValue());
        temp.setStringValue(val.getStringValue());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.persist(val, true, false);
        getModifiedRecordDAOObject().saveAuditData();
        pm.commitAndClose();
        return val;
    }

    @Override
    public void delete(AbstractAuditableObject value) {
        XincoSetting val = (XincoSetting) value;
        XincoSettingT temp = new XincoSettingT();
        temp.setBoolValue(val.getBoolValue());
        temp.setRecordId(val.getRecordId());
        temp.setDescription(val.getDescription());
        temp.setId(val.getId());
        temp.setIntValue(val.getIntValue());
        temp.setLongValue(val.getLongValue());
        temp.setStringValue(val.getStringValue());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.delete(val, false);
        getModifiedRecordDAOObject().saveAuditData();
        pm.commitAndClose();
    }

    @SuppressWarnings("unchecked")
    @Override
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("id", getId());
        return temp;
    }

    public int getNewID() {
        return new XincoIDServer("xinco_setting").getNewTableID();
    }
}
