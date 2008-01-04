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
 * Name:            XincoCoreDataTypeAttributeServer
 *
 * Description:     data type attribute
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
import com.bluecubs.xinco.core.persistance.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.persistance.XincoCoreDataTypeAttributePK;
import com.bluecubs.xinco.core.persistance.audit.XincoCoreDataTypeAttributeT;
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
 * Create data type attribute object for data structures
 * @author Alexander Manes
 */
public class XincoCoreDataTypeAttributeServer extends XincoCoreDataTypeAttribute implements XincoAuditableDAO, XincoPersistanceServerObject {

    private static XincoPersistanceManager pm = new XincoPersistanceManager();
    private static List result;
    private static HashMap parameters;

    /**
     * Create data type attribute object for data structures
     * @param pk XincoCoreDataTypeAttributePK
     * @throws com.bluecubs.xinco.core.XincoException 
     */
    @SuppressWarnings("unchecked")
    public XincoCoreDataTypeAttributeServer(XincoCoreDataTypeAttributePK pk) throws XincoException {
        try {
            parameters = new HashMap();
            parameters.put("xincoCoreDataTypeId", pk.getXincoCoreDataTypeId());
            parameters.put("attributeId", pk.getAttributeId());
            result = pm.createdQuery("SELECT p FROM XincoCoreDataTypeAttribute p " +
                    "WHERE p.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId= :xincoCoreDataTypeId " +
                    "AND p.xincoCoreDataTypeAttributePK.attributeId= :attributeId", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                XincoCoreDataTypeAttribute temp = (XincoCoreDataTypeAttribute) result.get(0);
                setXincoCoreDataTypeAttributePK(temp.getXincoCoreDataTypeAttributePK());
                setDesignation(temp.getDesignation());
                setDataType(temp.getDataType());
                setSize(temp.getSize());
            } else {
                throw new XincoException();
            }
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    /**
     * Create data type attribute object for data structures
     * @param xincoCoreDataTypeId
     * @param attributeId
     * @param designation
     * @param dataType
     * @param size
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreDataTypeAttributeServer(int xincoCoreDataTypeId, int attributeId, String designation, String dataType, int size) throws XincoException {
        setXincoCoreDataTypeAttributePK(new XincoCoreDataTypeAttributePK(xincoCoreDataTypeId, attributeId));
        setDesignation(designation);
        setDataType(dataType);
        setSize(size);
    }

    /**
     * Create data type attribute object for data structures
     * @throws com.bluecubs.xinco.core.XincoException 
     */
    @SuppressWarnings("unchecked")
    public XincoCoreDataTypeAttributeServer() throws XincoException {
    }

    /**
     * Create complete list of data type attributes
     * @param xincoCoreDataTypeId 
     * @return Vector containing Xinco COre Data Attributes for the specified Xinco Core Data Type
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreDataTypeAttributes(int xincoCoreDataTypeId) {
        Vector coreDataTypeAttributes = new Vector();
        parameters = new HashMap();
        parameters.put("xincoCoreDataTypeId", xincoCoreDataTypeId);
        try {
            result = pm.createdQuery("SELECT p FROM XincoCoreDataTypeAttribute p WHERE p.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId= :xincoCoreDataTypeId ORDER BY p.xincoCoreDataTypeAttributePK.attributeId", parameters);
            while (!result.isEmpty()) {
                XincoCoreDataTypeAttribute temp = (XincoCoreDataTypeAttribute) result.get(0);
                coreDataTypeAttributes.add(new XincoCoreDataTypeAttributeServer(temp.getXincoCoreDataTypeAttributePK()));
                result.remove(0);
            }
        } catch (Throwable e) {
            Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.SEVERE, null, e);
            coreDataTypeAttributes.removeAllElements();
        }
        return coreDataTypeAttributes;
    }

    public XincoAbstractAuditableObject findById(HashMap parameters) throws DataRetrievalFailureException {
        result = pm.createdQuery("SELECT p FROM XincoCoreDataTypeAttribute p WHERE p.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId= :xincoCoreDataTypeId AND p.xincoCoreDataTypeAttributePK.attributeId= :attributeId", parameters);
        if (result.size() > 0) {
            XincoCoreDataTypeAttribute temp = (XincoCoreDataTypeAttribute) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public XincoAbstractAuditableObject[] findWithDetails(HashMap parameters) throws DataRetrievalFailureException {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreDataTypeAttribute x WHERE ";
        if (parameters.containsKey("xincoCoreDataTypeId")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by xincoCoreDataTypeId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId = :xincoCoreDataTypeId";
            counter++;
        }
        if (parameters.containsKey("attributeId")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by attributeId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreDataTypeAttributePK.attributeId = :attributeId";
            counter++;
        }
        if (parameters.containsKey("designation")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by designation");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.designation = :designation";
            counter++;
        }
        if (parameters.containsKey("dataType")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by dataType");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.dataType = :dataType";
            counter++;
        }
        if (parameters.containsKey("size")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by size");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.size = :size";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoCoreDataTypeAttribute temp[] = new XincoCoreDataTypeAttribute[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreDataTypeAttribute) result.get(0);
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
        XincoCoreDataTypeAttribute temp, newValue = new XincoCoreDataTypeAttribute();
        temp = (XincoCoreDataTypeAttribute) value;
        if (!value.isCreated()) {
            newValue.setXincoCoreDataTypeAttributePK(temp.getXincoCoreDataTypeAttributePK());
            newValue.setRecordId(temp.getRecordId());
        } else {
            newValue.setXincoCoreDataTypeAttributePK(new XincoCoreDataTypeAttributePK(temp.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId(), getNewID()));
        }
        if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Creating with new id: " + newValue.getXincoCoreDataTypeAttributePK());
        }
        newValue.setDataType(temp.getDataType());
        newValue.setDesignation(temp.getDesignation());
        newValue.setSize(temp.getSize());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        return newValue;
    }

    public XincoAbstractAuditableObject update(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoCoreDataTypeAttribute val = (XincoCoreDataTypeAttribute) value;
        XincoCoreDataTypeAttributeT temp = new XincoCoreDataTypeAttributeT();
        temp.setRecordId(val.getRecordId());
        if (!value.isCreated()) {
            temp.setAttributeId(val.getXincoCoreDataTypeAttributePK().getAttributeId());
            temp.setXincoCoreDataTypeId(val.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
            temp.setRecordId(val.getRecordId());
        } else {
            temp.setAttributeId(val.getRecordId());
            temp.setXincoCoreDataTypeId(val.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
        }
        temp.setDataType(val.getDataType());
        temp.setDesignation(val.getDesignation());
        temp.setSize(val.getSize());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.persist(val, true, false);
        val.saveAuditData(pm);
        pm.commitAndClose();
        return val;
    }

    public void delete(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoCoreDataTypeAttribute val = (XincoCoreDataTypeAttribute) value;
        XincoCoreDataTypeAttributeT temp = new XincoCoreDataTypeAttributeT();
        temp.setRecordId(val.getRecordId());
        temp.setAttributeId(val.getXincoCoreDataTypeAttributePK().getAttributeId());
        temp.setXincoCoreDataTypeId(val.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
        temp.setDataType(val.getDataType());
        temp.setDesignation(val.getDesignation());
        temp.setSize(val.getSize());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.delete(val, false);
        val.saveAuditData(pm);
        pm.commitAndClose();
    }

    @SuppressWarnings("unchecked")
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("xincoCoreDataTypeId", getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
        temp.put("attributeId", getXincoCoreDataTypeAttributePK().getAttributeId());
        return temp;
    }

    @SuppressWarnings("unchecked")
    public int getNewID() {
        try {
            HashMap temp = new HashMap();
            temp.put("xincoCoreDataTypeId", getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
            result = pm.createdQuery("select max(p.xincoCoreDataTypeAttributePK.attributeId) from XincoCoreDataTypeAttribute p where p.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId= :xincoCoreDataTypeId", temp);
            int id = (Integer) result.get(0) + 1;
            return id;
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return -1;
        }
    }

    public boolean deleteFromDB() throws XincoException {
        setTransactionTime(DateRange.startingNow());
        try {
            XincoAuditingDAOHelper.delete(this, getXincoCoreDataTypeAttributePK().getAttributeId());
            return true;
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    public boolean write2DB() throws XincoException {
        try {
            if (getXincoCoreDataTypeAttributePK().getAttributeId() > 0) {
                if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Updating: " + getXincoCoreDataTypeAttributePK());
                }
                XincoAuditingDAOHelper.update(this, new XincoCoreDataTypeAttribute(getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId(),
                        getXincoCoreDataTypeAttributePK().getAttributeId()));
            } else {
                if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Creating: " + getXincoCoreDataTypeAttributePK());
                }
                XincoCoreDataTypeAttribute temp = new XincoCoreDataTypeAttribute();
                temp.setXincoCoreDataTypeAttributePK(getXincoCoreDataTypeAttributePK());
                temp.setChangerID(getChangerID());
                temp.setCreated(true);
                temp.setDataType(getDataType());
                temp.setDesignation(getDesignation());
                temp.setSize(getSize());
                temp = (XincoCoreDataTypeAttribute) XincoAuditingDAOHelper.create(this, temp);
                setXincoCoreDataTypeAttributePK(temp.getXincoCoreDataTypeAttributePK());
            }
            return true;
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }
}
