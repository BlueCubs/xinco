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
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.add.server.XincoAddAttributeServer;
import java.util.Vector;

import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.core.persistence.XincoCoreDataType;
import com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.persistence.XincoCoreDataTypeT;
import com.bluecubs.xinco.core.hibernate.audit.AbstractAuditableObject;
import com.bluecubs.xinco.core.hibernate.audit.AuditableDAO;
import com.bluecubs.xinco.core.hibernate.audit.AuditingDAOHelper;
import com.bluecubs.xinco.core.hibernate.audit.PersistenceServerObject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

public class XincoCoreDataTypeServer extends XincoCoreDataType implements AuditableDAO, PersistenceServerObject {

    private static final long serialVersionUID = -3765171134170526537L;
    private Vector xincoCoreDataTypeAttributes;
    private static List result;
    //create data type object for data structures
    public XincoCoreDataTypeServer(int attrID, XincoDBManager DBM) throws XincoException {
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = DBM.namedQuery("XincoCoreDataType.findById", parameters);
            if (!result.isEmpty()) {
                XincoCoreDataType xcdt = (XincoCoreDataType) result.get(0);
                setId(xcdt.getId());
                setDesignation(xcdt.getDesignation());
                setDescription(xcdt.getDescription());
                setXincoCoreDataTypeAttributes(XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(getId(), DBM));
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            throw new XincoException();
        }

    }

    //create data type object for data structures
    public XincoCoreDataTypeServer(int attrID, String attrD, String attrDESC, Vector attrA) throws XincoException {

        setId(attrID);
        setDesignation(attrD);
        setDescription(attrDESC);
        setXincoCoreDataTypeAttributes(attrA);

    }
    //create complete list of data types
    public static Vector getXincoCoreDataTypes(XincoDBManager DBM) {
        Vector coreDataTypes = new Vector();
        try {
            result = DBM.createdQuery("SELECT x FROM XincoCoreDataType x ORDER BY x.designation");
            while (!result.isEmpty()) {
                coreDataTypes.addElement((XincoCoreDataType) result.get(0));
            }
        } catch (Exception e) {
            coreDataTypes.removeAllElements();
        }

        return coreDataTypes;
    }
    //delete from db
    public static int deleteFromDB(XincoCoreDataTypeAttributeServer attrCDTA, int userID) throws XincoException {
        try {
            result = pm.namedQuery("select x from XincoAddAttribute X WHERE x.xincoCoreDataTypeAttributePK.attributeId=" +
                    attrCDTA.getXincoCoreDataTypeAttributePK().getAttributeId() + " AND x.xincoCoreDataTypeAttributePK.xincoCoreDataId IN (SELECT xcd.id FROM XincoCoreData xcd WHERE xcd.xincoCoreDataTypeId=" +
                    attrCDTA.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId() + ")", null);
            while (!result.isEmpty()) {
                XincoAddAttributeServer xaa = (XincoAddAttributeServer) result.get(0);
                xaa.deleteFromDB();
                result.remove(0);
            }
        } catch (Exception e) {
            throw new XincoException();
        }

        return 0;

    }
    //create complete list of data type attributes
    public static Vector getXincoCoreDataTypeAttributeServerTypeAttributes(int attrID, XincoDBManager DBM) {
        Vector coreDataTypeAttributes = new Vector();
        try {
            result = pm.createdQuery("SELECT x FROM XincoCoreDataTypeAttribute x WHERE " +
                    "x.xincoCoreDataTypeAttributePK.xincoCoreDataId =" + attrID +
                    " ORDER BY x.xincoCoreDataTypeAttributePK.attributeId", null);
            while (!result.isEmpty()) {
                coreDataTypeAttributes.addElement((XincoCoreDataTypeAttribute) result.get(0));
                result.remove(0);
            }
        } catch (Exception e) {
            coreDataTypeAttributes.removeAllElements();
        }

        return coreDataTypeAttributes;
    }

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
        result = pm.namedQuery("XincoCoreDataType.findById", parameters);
        if (result.size() > 0) {
            XincoCoreDataTypeAttributeServer temp = (XincoCoreDataTypeAttributeServer) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws Exception {
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
            XincoCoreDataTypeAttributeServer temp[] = new XincoCoreDataTypeAttributeServer[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreDataTypeAttributeServer) result.get(0);
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
        XincoCoreDataTypeAttributeServer temp;
        XincoCoreDataTypeAttribute newValue = new XincoCoreDataTypeAttribute();
        temp = (XincoCoreDataTypeAttributeServer) value;
        newValue.getXincoCoreDataTypeAttributePK().setAttributeId(temp.getXincoCoreDataTypeAttributePK().getAttributeId());
        newValue.getXincoCoreDataTypeAttributePK().setXincoCoreDataTypeId(temp.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
        newValue.setDataType(temp.getDataType());
        newValue.setSize(temp.getSize());
        newValue.setDesignation(temp.getDesignation());

        newValue.setRecordId(temp.getRecordId());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.INFO,
                    "New value created: " + newValue);
        }
        return newValue;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoCoreDataTypeServer val = (XincoCoreDataTypeServer) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.INFO,
                    "Value updated: " + val);
        }
        return val;
    }

    @SuppressWarnings("unchecked")
    public void delete(AbstractAuditableObject value) {
        try {
            XincoCoreDataType val = (XincoCoreDataType) value;
            XincoCoreDataTypeT temp = new XincoCoreDataTypeT();
            temp.setRecordId(val.getRecordId());
            temp.setId(val.getId());

            temp.setDesignation(val.getDesignation());
            temp.setDescription(val.getDescription());

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
        return new XincoIDServer("XincoCoreDataType").getNewTableID();
    }

    @SuppressWarnings("unchecked")
    public boolean write2DB() {
        try {
            if (getId() > 0) {
                AuditingDAOHelper.update(this, new XincoCoreDataType());
            } else {
                XincoCoreDataType temp = new XincoCoreDataType();
                temp.setChangerID(getChangerID());
                temp.setCreated(true);

                temp.setId(getId());
                temp.setDesignation(getDesignation());
                temp.setDescription(getDescription());

                temp = (XincoCoreDataType) AuditingDAOHelper.create(this, temp);
                setId(temp.getId());
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.INFO, "Assigned id: " + getId());
                }
            }
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.SEVERE, null, e);
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
                Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public Vector getXincoCoreDataTypeAttributes() {
        return xincoCoreDataTypeAttributes;
    }

    public void setXincoCoreDataTypeAttributes(Vector xincoCoreDataTypeAttributes) {
        this.xincoCoreDataTypeAttributes = xincoCoreDataTypeAttributes;
    }
}
