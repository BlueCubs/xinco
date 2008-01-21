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
 * Name:            XincoAddAttributeServer
 *
 * Description:     additional attributes of a data object 
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
package com.bluecubs.xinco.add.server;

import com.bluecubs.xinco.add.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.exception.XincoSettingException;
import com.bluecubs.xinco.core.server.persistence.*;
import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.add.persistence.XincoAddAttributePK;
import com.bluecubs.xinco.core.persistence.audit.XincoAddAttributeT;
import com.bluecubs.xinco.core.server.persistence.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.server.persistence.audit.XincoAuditableDAO;
import com.bluecubs.xinco.core.server.persistence.audit.XincoAuditingDAOHelper;
import java.util.Date;
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
 * @author Alexander Manes
 */
public class XincoAddAttributeServer extends XincoAddAttribute implements XincoAuditableDAO, XincoPersistenceServerObject {

    private static List result;

    /**
     * Create add attribute object for data structures
     * @param pk XincoAddAttributePK
     * @throws com.bluecubs.xinco.core.exception.XincoException
     */
    @SuppressWarnings("unchecked")
    public XincoAddAttributeServer(XincoAddAttributePK pk) throws XincoException {
        
        try {
            parameters.clear();
            parameters.put("xincoCoreDataId", pk.getXincoCoreDataId());
            parameters.put("attributeId", pk.getAttributeId());
            result = pm.createdQuery("SELECT p FROM XincoAddAttribute p " +
                    "WHERE p.xincoAddAttributePK.xincoCoreDataId= :xincoCoreDataId AND p.xincoAddAttributePK.attributeId= :attributeId", parameters);
            if (!result.isEmpty()) {
                XincoAddAttribute temp = (XincoAddAttribute) result.get(0);
                setXincoAddAttributePK(temp.getXincoAddAttributePK());
                setAttribInt(temp.getAttribInt());
                setAttribUnsignedint(temp.getAttribUnsignedint());
                setAttribDouble(temp.getAttribDouble());
                setAttribVarchar(temp.getAttribVarchar());
                setAttribText(temp.getAttribText());
                setAttribDatetime(temp.getAttribDatetime());
            }
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    /**
     * Create add attribute object for data structures
     * @param xincoCoreDataId 
     * @param attributeId 
     * @param attrI int attribute
     * @param attrUI long attribute
     * @param attrD double attribute
     * @param attrVC varchar attribute
     * @param attrT string attribute
     * @param attrDT date time attribute
     * @throws com.bluecubs.xinco.core.exception.XincoException
     */
    public XincoAddAttributeServer(int xincoCoreDataId, int attributeId, int attrI, int attrUI, double attrD, String attrVC, String attrT, Date attrDT) throws XincoException {
        
        setXincoAddAttributePK(new XincoAddAttributePK(xincoCoreDataId, attributeId));
        setAttribInt(attrI);
        setAttribUnsignedint(attrUI);
        setAttribDouble(attrD);
        setAttribVarchar(attrVC);
        setAttribText(attrT);
        setAttribDatetime(attrDT);
    }

    /**
     * Create add attribute object for data structures
     */
    public XincoAddAttributeServer() {
        
    }

    /**
     * create complete list of add attributes
     * @param attrID xincoCoreDataId
     * @return vector containing attributes
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoAddAttributes(int attrID) {
        Vector addAttributes = new Vector();
        try {
            parameters.clear();
            parameters.put("xincoCoreDataId", attrID);
            result = pm.createdQuery("SELECT p FROM XincoAddAttribute p WHERE " +
                    "p.xincoAddAttributePK.xincoCoreDataId = :xincoCoreDataId ORDER " +
                    "BY p.xincoAddAttributePK.attributeId", parameters);
            while (!result.isEmpty()) {
                XincoAddAttribute temp = (XincoAddAttribute) result.get(0);
                addAttributes.addElement(new XincoAddAttributeServer(temp.getXincoAddAttributePK().getXincoCoreDataId(),
                        temp.getXincoAddAttributePK().getAttributeId(),
                        temp.getAttribInt() == null ? 0 : temp.getAttribInt(),
                        temp.getAttribUnsignedint() == null ? 0 : temp.getAttribUnsignedint(),
                        temp.getAttribDouble() == null ? new Double(0) : temp.getAttribDouble(),
                        temp.getAttribVarchar(),
                        temp.getAttribText(), temp.getAttribDatetime()));
                result.remove(0);
            }
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, e);
                }
                addAttributes.removeAllElements();
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return addAttributes;
    }

    public XincoAbstractAuditableObject findById(HashMap parameters) throws DataRetrievalFailureException {
        result = pm.createdQuery("SELECT x FROM XincoAddAttribute x WHERE " +
                "x.xincoAddAttributePK.xincoCoreDataId = :xincoCoreDataId and " +
                "x.xincoAddAttributePK.attributeId = :attributeId", parameters);
        if (result.size() > 0) {
            XincoAddAttribute temp = (XincoAddAttribute) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public XincoAbstractAuditableObject[] findWithDetails(HashMap parameters) throws DataRetrievalFailureException {
        int counter = 0;
        String sql = "SELECT x FROM XincoAddAttribute x WHERE ";
        if (parameters.containsKey("attribInt")) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO, "Searching by attribInt");
                }
                if (counter > 0) {
                    sql += " and ";
                }
                sql += "x.attribInt = :attribInt";
                counter++;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (parameters.containsKey("attribUnsignedint")) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO, "Searching by attribUnsignedint");
                }
                if (counter > 0) {
                    try {
                        sql += " and ";
                    } catch (Throwable ex) {
                        Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                sql += "x.attribUnsignedint = :attribUnsignedint";
                counter++;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (parameters.containsKey("attribDouble")) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO, "Searching by attribDouble");
                }
                if (counter > 0) {
                    sql += " and ";
                }
                sql += "x.attribDouble = :attribDouble";
                counter++;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (parameters.containsKey("attribVarchar")) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO, "Searching by attribVarchar");
                }
                if (counter > 0) {
                    sql += " and ";
                }
                sql += "x.attribVarchar = :attribVarchar";
                counter++;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (parameters.containsKey("attribDatetime")) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO, "Searching by attribDatetime");
                }
                if (counter > 0) {
                    sql += " and ";
                }
                sql += "x.attribDatetime = :attribDatetime";
                counter++;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoAddAttribute temp[] = new XincoAddAttribute[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoAddAttribute) result.get(0);
                temp[i].setTransactionTime(getTransactionTime());
                i++;
                result.remove(0);
            }
            return temp;
        } else {
            return null;
        }
    }

    public XincoAbstractAuditableObject create(
            XincoAbstractAuditableObject value) {
        try {
            XincoAddAttribute temp;
            XincoAddAttribute newValue = new XincoAddAttribute();
            temp = (XincoAddAttribute) value;
            if (!value.isCreated()) {
                newValue.setXincoAddAttributePK(temp.getXincoAddAttributePK());
                newValue.setRecordId(temp.getRecordId());
            } else {
                newValue.setXincoAddAttributePK(new XincoAddAttributePK(temp.getXincoAddAttributePK().getXincoCoreDataId(), getNewID()));
            }
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Creating with new id: " + newValue.getXincoAddAttributePK());
            }
            newValue.setAttribDatetime(temp.getAttribDatetime());
            newValue.setAttribDouble(temp.getAttribDouble());
            newValue.setAttribInt(temp.getAttribInt());
            newValue.setAttribText(temp.getAttribText());
            newValue.setAttribUnsignedint(temp.getAttribUnsignedint());
            newValue.setAttribVarchar(temp.getAttribVarchar());
            newValue.setCreated(temp.isCreated());
            newValue.setChangerID(temp.getChangerID());
            newValue.setTransactionTime(getTransactionTime());
            pm.persist(newValue, false, true);
            return newValue;
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public XincoAbstractAuditableObject update(
            XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoAddAttribute val = (XincoAddAttribute) value;
        XincoAddAttributeT temp = new XincoAddAttributeT();
        temp.setRecordId(val.getRecordId());
        if (!value.isCreated()) {
            temp.setAttributeId(val.getXincoAddAttributePK().getAttributeId());
            temp.setXincoCoreDataId(val.getXincoAddAttributePK().getXincoCoreDataId());
            temp.setRecordId(val.getRecordId());
        } else {
            temp.setAttributeId(val.getRecordId());
            temp.setXincoCoreDataId(val.getXincoAddAttributePK().getXincoCoreDataId());
        }
        temp.setAttribDatetime(val.getAttribDatetime());
        temp.setAttribDouble(val.getAttribDouble());
        temp.setAttribInt(val.getAttribInt());
        temp.setAttribText(val.getAttribText());
        temp.setAttribUnsignedint(val.getAttribUnsignedint());
        temp.setAttribVarchar(val.getAttribVarchar());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.persist(val, true, false);
        val.saveAuditData(pm);
        pm.commitAndClose();
        return val;
    }

    public void delete(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoAddAttribute val = (XincoAddAttribute) value;
        XincoAddAttributeT temp = new XincoAddAttributeT();
        temp.setRecordId(val.getRecordId());
        temp.setAttributeId(val.getXincoAddAttributePK().getAttributeId());
        temp.setXincoCoreDataId(val.getXincoAddAttributePK().getXincoCoreDataId());
        temp.setAttribDatetime(val.getAttribDatetime());
        temp.setAttribDouble(val.getAttribDouble());
        temp.setAttribInt(val.getAttribInt());
        temp.setAttribText(val.getAttribText());
        temp.setAttribUnsignedint(val.getAttribUnsignedint());
        temp.setAttribVarchar(val.getAttribVarchar());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.delete(val, false);
        val.saveAuditData(pm);
        pm.commitAndClose();
    }

    @SuppressWarnings("unchecked")
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("xincoCoreDataId", getXincoAddAttributePK().getXincoCoreDataId());
        temp.put("attributeId", getXincoAddAttributePK().getAttributeId());
        return temp;
    }

    @SuppressWarnings("unchecked")
    public int getNewID() {
        try {
            HashMap temp = new HashMap();
            temp.put("xincoCoreDataId", getXincoAddAttributePK().getXincoCoreDataId());
            result =
                    pm.createdQuery("select max(p.xincoAddAttributePK.attributeId) from XincoAddAttribute p where p.xincoAddAttributePK.xincoCoreDataId= :xincoCoreDataId", temp);
            int id = (Integer) result.get(0) + 1;
            return id;
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.SEVERE, null, e);
                }
                return -1;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, ex);
                return -1;
            }
        }
    }

    public boolean deleteFromDB() {
        setTransactionTime(DateRange.startingNow());
        try {
            XincoAuditingDAOHelper.delete(this, getXincoAddAttributePK().getAttributeId());
            return true;
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, e);
                }
                return false;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }

    public boolean write2DB() {
        try {
            if (getXincoAddAttributePK().getAttributeId() > 0) {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO, "Updating: " + getXincoAddAttributePK());
                }
                XincoAuditingDAOHelper.update(this, new XincoAddAttribute(getXincoAddAttributePK().getXincoCoreDataId(),
                        getXincoAddAttributePK().getAttributeId()));
            } else {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO, "Creating: " + getXincoAddAttributePK());
                }
                XincoAddAttribute temp = new XincoAddAttribute();
                temp.setXincoAddAttributePK(getXincoAddAttributePK());
                temp.setAttribDatetime(getAttribDatetime());
                temp.setAttribDouble(getAttribDouble());
                temp.setAttribInt(getAttribInt());
                temp.setAttribText(getAttribText());
                temp.setAttribUnsignedint(getAttribUnsignedint());
                temp.setAttribVarchar(getAttribVarchar());
                temp = (XincoAddAttribute) XincoAuditingDAOHelper.create(this, temp);
                setXincoAddAttributePK(temp.getXincoAddAttributePK());
            }
            return true;
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.SEVERE, null, e);
                }
                return false;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }
}
