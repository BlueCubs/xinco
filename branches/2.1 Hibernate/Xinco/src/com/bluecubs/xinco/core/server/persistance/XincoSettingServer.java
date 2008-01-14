/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.XincoSettingException;
import com.bluecubs.xinco.core.persistance.XincoSetting;
import com.bluecubs.xinco.core.persistance.audit.XincoSettingT;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditableDAO;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditingDAOHelper;
import java.math.BigInteger;
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
public class XincoSettingServer extends XincoSetting implements XincoAuditableDAO, XincoPersistanceServerObject {

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
     * @throws com.bluecubs.xinco.core.XincoException 
     */
    public XincoSettingServer(int id, java.lang.String description, int IntValue,
            java.lang.String StringValue, boolean BoolValue, BigInteger LongValue, int changerID) throws XincoException {
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
    }

    /**
     * Get setting
     * @param i
     * @return @link XincoSetting
     */
    public XincoSetting getSetting(int i) {
        if (i < getXinco_settings().size()) {
            return (XincoSetting) getXinco_settings().get(i);
        } else {
            for (int j = 0; j < getXinco_settings().size(); j++) {
                if (((XincoSetting) getXinco_settings().get(j)).getId() == i) {
                    return (XincoSetting) getXinco_settings().get(j);
                }
            }
        }
        return null;
    }

    /**
     * Get setting
     * @param s
     * @return @link XincoSetting
     * @throws com.bluecubs.xinco.core.XincoSettingException
     */
    public XincoSetting getSetting(String s) throws XincoSettingException {
        for (int i = 0; i < getXinco_settings().size(); i++) {
            if (((XincoSetting) getXinco_settings().get(i)).getDescription().equals(s)) {
                return (XincoSetting) getXinco_settings().get(i);
            }
        }
        throw new XincoSettingException();
    }

    @SuppressWarnings("unchecked")
    public Vector getXinco_settings() {
        if (xinco_settings == null) {
            Vector temp = new Vector();
            try {
                result = pm.executeQuery("SELECT p FROM XincoSetting p");
                for (int i = 0; i < result.size(); i++) {
                    temp.add((XincoSetting) result.get(i));
                }
                setXinco_settings(temp);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return xinco_settings;
    }

    public void setXinco_settings(Vector xinco_settings) {
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

    public boolean deleteFromDB() throws XincoException {
        setTransactionTime(DateRange.startingNow());
        try {
            XincoAuditingDAOHelper.delete(this, getId());
            return true;
        } catch (Throwable e) {
            Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, e);
            throw new XincoException();
        }
    }

    public boolean write2DB() throws XincoException {
        setTransactionTime(DateRange.startingNow());
        try {
            //Object exists
            if (getId() != null) {
                XincoAuditingDAOHelper.update(this, new XincoSetting(getId()));
            } else {
                XincoSetting temp = new XincoSetting();
                temp.setBoolValue(getBoolValue());
                temp.setDescription(getDescription());
                temp.setId(getId());
                temp.setIntValue(getIntValue());
                temp.setLongValue(getLongValue());
                temp.setStringValue(getStringValue());
                temp.setChangerID(getChangerID());
                XincoAuditingDAOHelper.create(this, temp);
            }
            return true;
        } catch (Throwable e) {
            Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, e);
            throw new XincoException();
        }
    }

    @Override
    public XincoAbstractAuditableObject findById(HashMap parameters) {
        result = pm.namedQuery("XincoSetting.findById", parameters);
        if (result.size() > 0) {
            XincoSetting temp = (XincoSetting) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public XincoAbstractAuditableObject[] findWithDetails(HashMap parameters) throws DataRetrievalFailureException {
        result = pm.namedQuery("XincoSetting.findByDescription", parameters);
        if (result.size() > 0) {
            XincoSetting temp[] = new XincoSetting[1];
            temp[0] = (XincoSetting) result.get(0);
            temp[0].setTransactionTime(getTransactionTime());
            return temp;
        } else {
            return null;
        }
    }

    @Override
    public XincoAbstractAuditableObject create(XincoAbstractAuditableObject value) {
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
    public XincoAbstractAuditableObject update(XincoAbstractAuditableObject value) {
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
        temp.setCreated(val.isCreated());
        temp.setChangerID(val.getChangerID());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.persist(val, true, false);
        val.saveAuditData(pm);
        pm.commitAndClose();
        return val;
    }

    @Override
    public void delete(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoSetting val = (XincoSetting) value;
        XincoSettingT temp = new XincoSettingT();
        temp.setBoolValue(val.getBoolValue());
        temp.setRecordId(val.getRecordId());
        temp.setDescription(val.getDescription());
        temp.setId(val.getId());
        temp.setIntValue(val.getIntValue());
        temp.setLongValue(val.getLongValue());
        temp.setStringValue(val.getStringValue());
        temp.setCreated(val.isCreated());
        temp.setChangerID(val.getChangerID());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.delete(val, false);
        val.saveAuditData(pm);
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
