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
 * Name:            XincoCoreDataTypeServer
 *
 * Description:     data type 
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
import com.bluecubs.xinco.persistence.XincoCoreDataType;
import com.bluecubs.xinco.persistence.audit.XincoCoreDataTypeT;
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
 * Create data type object for data structures
 * @author Alexander Manes
 */
public class XincoCoreDataTypeServer extends XincoCoreDataType implements AuditableDAO, PersistenceServerObject {

    private static List result;
    private Vector XincoCoreDataTypeAttributes;

    /**
     * Create data type object for data structures
     * @param attrID 
     * @throws com.bluecubs.xinco.core.exception.XincoException
     */
    @SuppressWarnings("unchecked")
    public XincoCoreDataTypeServer(int attrID) throws XincoException {
        pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = pm.namedQuery("XincoCoreDataType.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                XincoCoreDataType temp = (XincoCoreDataType) result.get(0);
                setId(temp.getId());
                setDesignation(temp.getDesignation());
                setDescription(temp.getDescription());
                setXincoCoreDataTypeAttributes(XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(getId()));
            } else {
                throw new XincoException();
            }
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    /**
     * Create data type object for data structures
     * @param id
     * @param attrD
     * @param attrDESC
     * @param attrA
     * @throws com.bluecubs.xinco.core.exception.XincoException
     */
    public XincoCoreDataTypeServer(int id, String attrD, String attrDESC, Vector attrA) throws XincoException {
        pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        setId(id);
        setDesignation(attrD);
        setDescription(attrDESC);
        setXincoCoreDataTypeAttributes(attrA);
    }

    public Vector getXincoCoreDataTypeAttributes() {
        return XincoCoreDataTypeAttributes;
    }

    public void setXincoCoreDataTypeAttributes(Vector XincoCoreDataTypeAttributes) {
        this.XincoCoreDataTypeAttributes = XincoCoreDataTypeAttributes;
    }

    /**
     * Create complete list of data types
     * @return Vector
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreDataTypes() {
        Vector coreDataTypes = new Vector();
        try {
            result = pm.executeQuery("SELECT p FROM XincoCoreDataType p ORDER BY p.designation");
            while (!result.isEmpty()) {
                XincoCoreDataType temp = (XincoCoreDataType) result.get(0);
                coreDataTypes.add(new XincoCoreDataTypeServer(temp.getId(), temp.getDesignation(), temp.getDescription(), XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(temp.getId())));
                result.remove(0);
            }
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.SEVERE, null, e);
                }
                coreDataTypes.removeAllElements();
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return coreDataTypes;
    }

    public AbstractAuditableObject findById(HashMap parameters) throws XincoException {
        result = pm.namedQuery("XincoCoreDataType.findById", parameters);
        if (result.size() > 0) {
            XincoCoreDataType temp = (XincoCoreDataType) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws XincoException, XincoSettingException {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreDataType x WHERE ";
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
        if (parameters.containsKey("description")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.INFO, "Searching by description");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.description = :description";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoCoreDataType temp[] = new XincoCoreDataType[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreDataType) result.get(0);
                temp[i].setTransactionTime(getTransactionTime());
                i++;
                result.remove(0);
            }
            return temp;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("id", getId());
        return temp;
    }

    public int getNewID() {
        return new XincoIDServer("xinco_core_data_type").getNewTableID();
    }

    public boolean deleteFromDB() {
        setTransactionTime(DateRange.startingNow());
        try {
            AuditingDAOHelper.delete(this, getId());
            return true;
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.SEVERE, null, e);
                }
                return false;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }

    public boolean write2DB() {
        try {
            if (getId() > 0) {
                AuditingDAOHelper.update(this, new XincoCoreDataType(getId()));
            } else {
                XincoCoreDataType temp = new XincoCoreDataType();
                temp.setId(getId());
                temp.setChangerID(getChangerID());
                temp.setCreated(true);
                temp.setDescription(getDescription());
                temp.setDesignation(getDesignation());
                temp = (XincoCoreDataType) AuditingDAOHelper.create(this, temp);
                setId(temp.getId());
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.INFO, "Assigned id: " + getId());
                }
            }
            return true;
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.SEVERE, null, e);
                }
                return false;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }

    public AbstractAuditableObject create(AbstractAuditableObject value) {
        try {
            XincoCoreDataType temp;
            XincoCoreDataType newValue = new XincoCoreDataType();
            temp = (XincoCoreDataType) value;
            if (!value.isCreated()) {
                newValue.setId(temp.getId());
                newValue.setRecordId(temp.getRecordId());
            } else {
                newValue.setId(getNewID());
            }
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.INFO, "Creating with new id: " + newValue.getId());
            }
            newValue.setDescription(temp.getDescription());
            newValue.setDesignation(temp.getDesignation());
            newValue.setCreated(temp.isCreated());
            newValue.setChangerID(temp.getChangerID());
            newValue.setTransactionTime(getTransactionTime());
            pm.persist(newValue, false, true);
            return newValue;
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoCoreDataType val = (XincoCoreDataType) value;
        XincoCoreDataTypeT temp = new XincoCoreDataTypeT();
        temp.setRecordId(val.getRecordId());
        if (!value.isCreated()) {
            temp.setId(val.getId());
        } else {
            temp.setId(val.getRecordId());
        }
        temp.setId(val.getId());
        temp.setDescription(val.getDescription());
        temp.setDesignation(val.getDesignation());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.persist(val, true, false);
        getModifiedRecordDAOObject().saveAuditData();
        pm.commitAndClose();
        return val;
    }

    public void delete(AbstractAuditableObject value) {
        XincoCoreDataType val = (XincoCoreDataType) value;
        XincoCoreDataTypeT temp = new XincoCoreDataTypeT();
        temp.setRecordId(val.getRecordId());
        temp.setId(val.getId());
        temp.setDescription(val.getDescription());
        temp.setDesignation(val.getDesignation());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.delete(val, false);
        getModifiedRecordDAOObject().saveAuditData();
        pm.commitAndClose();
    }
}
