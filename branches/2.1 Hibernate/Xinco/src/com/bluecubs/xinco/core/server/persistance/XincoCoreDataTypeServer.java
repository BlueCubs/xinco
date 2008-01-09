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
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistance.XincoCoreDataType;
import com.bluecubs.xinco.core.persistance.audit.XincoCoreDataTypeT;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditableDAO;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditingDAOHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Create data type object for data structures
 * @author Alexander Manes
 */
public class XincoCoreDataTypeServer extends XincoCoreDataType implements XincoAuditableDAO, XincoPersistanceServerObject {

    private static XincoPersistanceManager pm = new XincoPersistanceManager();
    private static List result;
    private static HashMap parameters;
    private Vector XincoCoreDataTypeAttributes;

    /**
     * Create data type object for data structures
     * @param attrID 
     * @throws com.bluecubs.xinco.core.XincoException
     */
    @SuppressWarnings("unchecked")
    public XincoCoreDataTypeServer(int attrID) throws XincoException {
        try {
            parameters = new HashMap();
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
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
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
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreDataTypeServer(int id, String attrD, String attrDESC, Vector attrA) throws XincoException {
        setId(id);
        setDesignation(attrD);
        setDescription(attrDESC);
        setXincoCoreDataTypeAttributes(attrA);
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
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.SEVERE, null, e);
            }
            coreDataTypes.removeAllElements();
        }
        return coreDataTypes;
    }

    public XincoAbstractAuditableObject findById(HashMap parameters) throws DataRetrievalFailureException {
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

    public XincoAbstractAuditableObject[] findWithDetails(HashMap parameters) throws DataRetrievalFailureException {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreDataType x WHERE ";
        if (parameters.containsKey("designation")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.INFO, "Searching by designation");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.designation = :designation";
            counter++;
        }
        if (parameters.containsKey("description")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
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

    public XincoAbstractAuditableObject create(XincoAbstractAuditableObject value) {
        XincoCoreDataType temp, newValue = new XincoCoreDataType();
        temp = (XincoCoreDataType) value;
        if (!value.isCreated()) {
            newValue.setId(temp.getId());
            newValue.setRecordId(temp.getRecordId());
        } else {
            newValue.setId(getNewID());
        }
        if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.INFO, "Creating with new id: " + newValue.getId());
        }
        newValue.setDescription(temp.getDescription());
        newValue.setDesignation(temp.getDesignation());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        return newValue;
    }

    public XincoAbstractAuditableObject update(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
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
        val.saveAuditData(pm);
        pm.commitAndClose();
        return val;
    }

    public void delete(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoCoreDataType val = (XincoCoreDataType) value;
        XincoCoreDataTypeT temp = new XincoCoreDataTypeT();
        temp.setRecordId(val.getRecordId());
        temp.setId(val.getId());
        temp.setDescription(val.getDescription());
        temp.setDesignation(val.getDesignation());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.delete(val, false);
        val.saveAuditData(pm);
        pm.commitAndClose();
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

    public boolean deleteFromDB() throws XincoException {
        setTransactionTime(DateRange.startingNow());
        try {
            XincoAuditingDAOHelper.delete(this, getId());
            return true;
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    public boolean write2DB() throws XincoException {
        try {
            if (getId() > 0) {
                XincoAuditingDAOHelper.update(this, new XincoCoreDataType(getId()));
            } else {
                XincoCoreDataType temp = new XincoCoreDataType();
                temp.setId(getId());
                temp.setChangerID(getChangerID());
                temp.setCreated(true);
                temp.setDescription(getDescription());
                temp.setDesignation(getDesignation());
                temp = (XincoCoreDataType) XincoAuditingDAOHelper.create(this, temp);
                setId(temp.getId());
                if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.INFO, "Assigned id: " + getId());
                }
            }
            return true;
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    public Vector getXincoCoreDataTypeAttributes() {
        return XincoCoreDataTypeAttributes;
    }

    public void setXincoCoreDataTypeAttributes(Vector XincoCoreDataTypeAttributes) {
        this.XincoCoreDataTypeAttributes = XincoCoreDataTypeAttributes;
    }
}
