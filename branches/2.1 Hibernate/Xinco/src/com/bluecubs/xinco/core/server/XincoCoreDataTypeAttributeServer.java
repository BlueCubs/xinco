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
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.hibernate.audit.XincoAuditableDAO;
import java.util.Vector;

import com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttributePK;
import com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttributeT;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

public class XincoCoreDataTypeAttributeServer extends XincoCoreDataTypeAttribute implements XincoAuditableDAO,
        PersistenceServerObject {

    private static List result;
    private static final long serialVersionUID = -4308712617323530780L;
    private int attributeId,  xincoCoreDataTypeId;

    //create data type attribute object for data structures
    public XincoCoreDataTypeAttributeServer(int xincoCoreDataTypeId, int attributeId) throws XincoException {
        try {
            result = pm.createdQuery("SELECT x FROM XincoCoreDataTypeAttribute x " +
                    "WHERE x.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId=" + xincoCoreDataTypeId +
                    " AND x.xincoCoreDataTypeAttributePK.attributeId=" + attributeId);
            if (!result.isEmpty()) {
                this.xincoCoreDataTypeId = xincoCoreDataTypeId;
                this.attributeId = attributeId;
                setXincoCoreDataTypeAttributePK(new XincoCoreDataTypeAttributePK());
                XincoCoreDataTypeAttribute xcdt = (XincoCoreDataTypeAttribute) result.get(0);
                getXincoCoreDataTypeAttributePK().setXincoCoreDataTypeId(xcdt.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
                getXincoCoreDataTypeAttributePK().setAttributeId(xcdt.getXincoCoreDataTypeAttributePK().getAttributeId());
                setDesignation(xcdt.getDesignation());
                setDataType(xcdt.getDataType());
                setAttrSize(xcdt.getAttrSize());
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            throw new XincoException(e.getLocalizedMessage());
        }
    }
    //create data type attribute object for data structures

    public XincoCoreDataTypeAttributeServer(int xincoCoreDataTypeId,
            int attributeId, String attrD, String attrDT, int attrS) throws XincoException {
        this.xincoCoreDataTypeId = xincoCoreDataTypeId;
        this.attributeId = attributeId;
        setDesignation(attrD);
        setDataType(attrDT);
        setAttrSize(attrS);
        setCreated(true);
    }

    //create data type attribute object for data structures
    public XincoCoreDataTypeAttributeServer() {
    }

    XincoCoreDataTypeAttributeServer(XincoCoreDataTypeAttributePK pk) throws XincoException {
        try {
            System.out.println("Looking for " + pk);
            result = pm.createdQuery("SELECT x FROM XincoCoreDataTypeAttribute x " +
                    "WHERE x.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId=" + pk.getXincoCoreDataTypeId() +
                    " AND x.xincoCoreDataTypeAttributePK.attributeId=" + pk.getAttributeId());
            if (!result.isEmpty()) {
                this.xincoCoreDataTypeId = pk.getXincoCoreDataTypeId();
                this.attributeId = pk.getAttributeId();
                setXincoCoreDataTypeAttributePK(pk);
                XincoCoreDataTypeAttribute xcdt = (XincoCoreDataTypeAttribute) result.get(0);
                setDesignation(xcdt.getDesignation());
                setDataType(xcdt.getDataType());
                setAttrSize(xcdt.getAttrSize());
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            throw new XincoException(e.getLocalizedMessage());
        }
    }

    @Override
    public XincoCoreDataTypeAttributePK getXincoCoreDataTypeAttributePK() {
        if (xincoCoreDataTypeAttributePK == null) {
            xincoCoreDataTypeAttributePK = new XincoCoreDataTypeAttributePK();
        }
        return xincoCoreDataTypeAttributePK;
    }

    //delete from db
    public static boolean deleteFromDB(XincoCoreDataTypeAttribute attrCDTA, int userID) throws XincoException {
        try {
            result = pm.createdQuery("select x from XincoCoreDataTypeAttribute X WHERE x.xincoCoreDataTypeAttributePK.attributeId=" +
                    attrCDTA.getXincoCoreDataTypeAttributePK().getAttributeId() + " AND x.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId " +
                    "IN (SELECT xcd.xincoCoreDataTypeId FROM XincoCoreData xcd WHERE xcd.xincoCoreDataTypeId=" +
                    attrCDTA.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId() + ")", null);
            while (!result.isEmpty()) {
                XincoCoreDataTypeAttributeServer xaa =
                        new XincoCoreDataTypeAttributeServer(((XincoCoreDataTypeAttribute) result.get(0)).getXincoCoreDataTypeAttributePK());
                xaa.setChangerID(userID);
                xaa.deleteFromDB();
                if (result.size() > 0) {
                    result.remove(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XincoException();
        }
        return true;
    }
    //create complete list of data type attributes

    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreDataTypeAttributes(int attrID) {
        Vector coreDataTypeAttributes = new Vector();
        try {
            result = pm.createdQuery("SELECT x FROM XincoCoreDataTypeAttribute x WHERE " +
                    "x.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId =" + attrID +
                    " ORDER BY x.xincoCoreDataTypeAttributePK.attributeId", null);
            while (!result.isEmpty()) {
                coreDataTypeAttributes.addElement((XincoCoreDataTypeAttribute) result.get(0));
                result.remove(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            coreDataTypeAttributes.removeAllElements();
        }

        return coreDataTypeAttributes;
    }

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
        result = pm.createdQuery("SELECT x FROM XincoCoreDataTypeAttribute x WHERE " +
                "x.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId = :xincoCoreDataTypeId and " +
                "x.xincoCoreDataTypeAttributePK.attributeId = :attributeId", parameters);
        if (result.size() > 0) {
            XincoCoreDataTypeAttribute temp = (XincoCoreDataTypeAttribute) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws Exception {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreDataTypeAttribute x WHERE ";
        if (parameters.containsKey("xincoCoreDataTypeId")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.INFO, "Searching by xincoCoreDataTypeId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId = :xincoCoreDataTypeId";
            counter++;
        }
        if (parameters.containsKey("attributeId")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.INFO, "Searching by attributeId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreDataTypeAttributePK.attributeId = :attributeId";
            counter++;
        }
        if (parameters.containsKey("designation")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.INFO, "Searching by designation");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.designation = :designation";
            counter++;
        }
        if (parameters.containsKey("dataType")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.INFO, "Searching by dataType");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.dataType = :dataType";
            counter++;
        }
        if (parameters.containsKey("size")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.INFO, "Searching by size");
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

    @SuppressWarnings("static-access")
    public AbstractAuditableObject create(AbstractAuditableObject value) throws Exception {
        XincoCoreDataTypeAttribute temp;
        XincoCoreDataTypeAttribute newValue = new XincoCoreDataTypeAttribute();
        temp = (XincoCoreDataTypeAttribute) value;
        newValue.setXincoCoreDataTypeAttributePK(new XincoCoreDataTypeAttributePK());
        newValue.getXincoCoreDataTypeAttributePK().setAttributeId(temp.getXincoCoreDataTypeAttributePK().getAttributeId());
        newValue.getXincoCoreDataTypeAttributePK().setXincoCoreDataTypeId(temp.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
        newValue.setDataType(temp.getDataType());
        newValue.setAttrSize(temp.getAttrSize());
        newValue.setDesignation(temp.getDesignation());

        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.INFO,
                    "New value created: " + newValue);
        }
        return newValue;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) throws Exception {
        XincoCoreDataTypeAttribute val = (XincoCoreDataTypeAttribute) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.INFO,
                    "Value updated: " + val);
        }
        return val;
    }

    @SuppressWarnings({"unchecked", "static-access"})
    public boolean delete(AbstractAuditableObject value) throws Exception {
        try {
            XincoCoreDataTypeAttribute val = (XincoCoreDataTypeAttribute) value;
            result = pm.createdQuery("select x from XincoCoreDataTypeAttribute X WHERE x.xincoCoreDataTypeAttributePK.attributeId=" +
                    val.getXincoCoreDataTypeAttributePK().getAttributeId() + " AND x.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId " +
                    "IN (SELECT xcd.xincoCoreDataTypeId FROM XincoCoreData xcd WHERE xcd.xincoCoreDataTypeId=" +
                    val.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId() + ")");
            pm.startTransaction();
            int i = 0;
            while (!result.isEmpty()) {
                XincoCoreDataTypeAttributeT temp = new XincoCoreDataTypeAttributeT();
                i++;
                temp.setRecordId(((XincoCoreDataTypeAttribute) result.get(0)).getRecordId(false));

                temp.setAttributeId(val.getXincoCoreDataTypeAttributePK().getAttributeId());
                temp.setXincoCoreDataTypeId(val.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
                temp.setDataType(val.getDataType());
                temp.setAttrSize(val.getAttrSize());
                temp.setDesignation(val.getDesignation());

                pm.persist(temp, false, false);
                pm.delete(val, false);
                setModifiedRecordDAOObject(value.getModifiedRecordDAOObject());
                //Make sure all audit data is stored properly. If not undo everything
                if (!getModifiedRecordDAOObject().saveAuditData()) {
                    throw new XincoException(rb.getString("error.audit_data.invalid"));
                }
                result.remove(0);
            }
            return pm.commitAndClose();
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.SEVERE, null, ex);
            pm.rollback();
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("attributeId", getXincoCoreDataTypeAttributePK().getAttributeId());
        temp.put("xincoCoreDataTypeId", getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
        return temp;
    }

    @SuppressWarnings("unchecked")
    public boolean write2DB() {
        try {
            if (getXincoCoreDataTypeAttributePK() != null &&
                    getXincoCoreDataTypeAttributePK().getAttributeId() > 0 &&
                    getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId() > 0 &&
                    !isCreated()) {
                AuditingDAOHelper.update(this, new XincoCoreDataTypeAttribute());
            } else {
                XincoCoreDataTypeAttribute temp = new XincoCoreDataTypeAttribute();

                //Make sure our PK is correct
                setXincoCoreDataTypeAttributePK(new XincoCoreDataTypeAttributePK());
                getXincoCoreDataTypeAttributePK().setXincoCoreDataTypeId(xincoCoreDataTypeId);
                getXincoCoreDataTypeAttributePK().setAttributeId(attributeId);

                temp.setXincoCoreDataTypeAttributePK(new XincoCoreDataTypeAttributePK());
                temp.getXincoCoreDataTypeAttributePK().setXincoCoreDataTypeId(xincoCoreDataTypeId);
                temp.getXincoCoreDataTypeAttributePK().setAttributeId(attributeId);

                temp.setChangerID(getChangerID());
                temp.setCreated(true);

                temp.setXincoCoreDataTypeAttributePK(getXincoCoreDataTypeAttributePK());
                temp.setDesignation(getDesignation());
                temp.setDataType(getDataType());
                temp.setAttrSize(getAttrSize());

                temp = (XincoCoreDataTypeAttribute) AuditingDAOHelper.create(this, temp);
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.INFO,
                            "Assigned id: " + temp.getXincoCoreDataTypeAttributePK());
                }
                setXincoCoreDataTypeAttributePK(temp.getXincoCoreDataTypeAttributePK());
            }
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public boolean deleteFromDB() {
        setTransactionTime(DateRange.startingNow());
        try {
            AuditingDAOHelper.delete(this, getXincoCoreDataTypeAttributePK(), getChangerID());
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public int getNewID(boolean atomic) {
        /* Return 0 to "fool" the audit system.
         * This parameter is not used for this class audit related methods.
         * This class is not meant to generate keys automatically since it's 
         * formed by a foreign key relationship.
         */
        return 0;
    }

    public Object transform() throws Exception {
        return (com.bluecubs.xinco.core.XincoCoreDataTypeAttribute)AuditingDAOHelper.clone(this);
    }
}
