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

import com.bluecubs.xinco.add.server.XincoAddAttributeServer;
import com.bluecubs.xinco.core.XincoException;
import java.util.Vector;

import com.bluecubs.xinco.core.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttributeT;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditableDAO;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

public class XincoCoreDataTypeAttributeServer extends XincoCoreDataTypeAttribute implements AuditableDAO, PersistenceServerObject {

    private static List result;
    private static final long serialVersionUID = -4308712617323530780L;
    //create data type attribute object for data structures
    public XincoCoreDataTypeAttributeServer(int attrID1, int attrID2, XincoDBManager DBM) throws XincoException {
        try {
            result = DBM.createdQuery("SELECT x FROM XincoCoreDataTypeAttribute x " +
                    "WHERE x.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId=" + attrID1 +
                    " AND x.xincoCoreDataTypeAttributePK.attributeId=" + attrID2);
            if (!result.isEmpty()) {
                XincoCoreDataTypeAttribute xcdt = (XincoCoreDataTypeAttribute) result.get(0);
                getXincoCoreDataTypeAttributePK().setXincoCoreDataTypeId(xcdt.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
                getXincoCoreDataTypeAttributePK().setAttributeId(xcdt.getXincoCoreDataTypeAttributePK().getAttributeId());
                setDesignation(xcdt.getDesignation());
                setDataType(xcdt.getDataType());
                setSize(xcdt.getSize());
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            throw new XincoException();
        }

    }
    //create data type attribute object for data structures
    public XincoCoreDataTypeAttributeServer(int attrID1, int attrID2, String attrD, String attrDT, int attrS) throws XincoException {
        getXincoCoreDataTypeAttributePK().setXincoCoreDataTypeId(attrID1);
        getXincoCoreDataTypeAttributePK().setAttributeId(attrID2);
        setDesignation(attrD);
        setDataType(attrDT);
        setSize(attrS);
    }
    //delete from db
    public static int deleteFromDB(XincoCoreDataTypeAttributeServer attrCDTA, int userID) throws XincoException {
        try {
            result = pm.createdQuery("select x from XincoAddAttribute X WHERE x.xincoCoreDataTypeAttributePK.attributeId=" +
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
    public static Vector getXincoCoreDataTypeAttributes(int attrID, XincoDBManager DBM) {
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
        result = pm.createdQuery("SELECT x FROM XincoCoreDataTypeAttribute x WHERE " +
                "x.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId = :xincoCoreDataTypeId and " +
                "x.xincoCoreDataTypeAttributePK.attributeId = :attributeId", parameters);
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
        String sql = "SELECT x FROM XincoCoreDataTypeAttributeServer x WHERE ";
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
            Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.INFO,
                    "New value created: " + newValue);
        }
        return newValue;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoCoreDataTypeAttributeServer val = (XincoCoreDataTypeAttributeServer) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.INFO,
                    "Value updated: " + val);
        }
        return val;
    }

    @SuppressWarnings("unchecked")
    public void delete(AbstractAuditableObject value) {
        try {
            XincoCoreDataTypeAttributeServer val = (XincoCoreDataTypeAttributeServer) value;

            result = pm.createdQuery("select x from XincoAddAttribute X WHERE x.xincoCoreDataTypeAttributePK.attributeId=" +
                    val.getXincoCoreDataTypeAttributePK().getAttributeId() + " AND x.xincoCoreDataTypeAttributePK.xincoCoreDataId IN (SELECT xcd.id FROM XincoCoreData xcd WHERE xcd.xincoCoreDataTypeId=" +
                    val.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId() + ")", null);
            pm.startTransaction();
            while (!result.isEmpty()) {

                XincoCoreDataTypeAttributeT temp = new XincoCoreDataTypeAttributeT();
                temp.setRecordId(((XincoAddAttribute) result.get(0)).getRecordId());

                temp.setAttributeId(val.getXincoCoreDataTypeAttributePK().getAttributeId());
                temp.setXincoCoreDataTypeId(val.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
                temp.setDataType(val.getDataType());
                temp.setSize(val.getSize());
                temp.setDesignation(val.getDesignation());

                pm.persist(temp, false, false);
                pm.delete(val, false);
                getModifiedRecordDAOObject().saveAuditData();
                result.remove(0);
            }
            pm.commitAndClose();
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("attributeId", getXincoCoreDataTypeAttributePK().getAttributeId());
        temp.put("xincoCoreDataTypeId", getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
        return temp;
    }

    /**
     * Get a new newID
     * @return New last ID
     */
    @SuppressWarnings("unchecked")
    public int getNewID() {
        return new XincoIDServer("xincoCoreAce").getNewTableID();
    }

    @SuppressWarnings("unchecked")
    public boolean write2DB() {
        try {
            if (getXincoCoreDataTypeAttributePK() != null) {
                AuditingDAOHelper.update(this, new XincoCoreDataTypeAttribute());
            } else {
                XincoCoreDataTypeAttribute temp = new XincoCoreDataTypeAttribute();
                temp.setChangerID(getChangerID());
                temp.setCreated(true);

                temp.getXincoCoreDataTypeAttributePK().setXincoCoreDataTypeId(getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId());
                temp.getXincoCoreDataTypeAttributePK().setAttributeId(getXincoCoreDataTypeAttributePK().getAttributeId());
                temp.setDesignation(getDesignation());
                temp.setDataType(getDataType());
                temp.setSize(getSize());

                temp = (XincoCoreDataTypeAttribute) AuditingDAOHelper.create(this, temp);
                setXincoCoreDataTypeAttributePK(temp.getXincoCoreDataTypeAttributePK());
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.INFO, "Assigned id: " + getXincoCoreDataTypeAttributePK());
                }
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
            AuditingDAOHelper.delete(this, getXincoCoreDataTypeAttributePK());
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataTypeAttributeServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }
}
