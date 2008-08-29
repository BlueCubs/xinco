/**
 *Copyright 2007 blueCubs.com
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

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.hibernate.audit.XincoAuditableDAO;
import java.util.HashMap;
import java.util.Vector;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.bluecubs.xinco.core.persistence.XincoAddAttribute;
import com.bluecubs.xinco.core.persistence.XincoAddAttributeT;
import com.bluecubs.xinco.core.server.XincoIDServer;
import com.bluecubs.xinco.core.server.XincoSettingServer;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

public class XincoAddAttributeServer extends XincoAddAttribute implements XincoAuditableDAO, PersistenceServerObject {
    //create add attribute object for data structures

    private static List result;
    private static final long serialVersionUID = -2238466076051443225L;

    @SuppressWarnings("unchecked")
    public XincoAddAttributeServer(int attrID1, int attrID2) throws XincoException {
        try {
            parameters.clear();
            parameters.put("xincoCoreDataId", attrID1);
            parameters.put("attributeId", attrID2);
            result = pm.createdQuery("SELECT x FROM XincoAddAttribute x WHERE " +
                    "x.xincoAddAttributePK.xincoCoreDataId = :xincoCoreDataId and " +
                    "x.xincoAddAttributePK.attributeId = :attributeId", parameters);
            if (!result.isEmpty()) {
                XincoAddAttribute xaa = (XincoAddAttribute) result.get(0);
                getXincoAddAttributePK().setXincoCoreDataId(xaa.getXincoAddAttributePK().getXincoCoreDataId());
                getXincoAddAttributePK().setAttributeId(xaa.getXincoAddAttributePK().getAttributeId());
                setAttribInt(xaa.getAttribInt());
                setAttribUnsignedint(xaa.getAttribUnsignedint());
                setAttribDouble(xaa.getAttribDouble());
                setAttribVarchar(xaa.getAttribVarchar());
                setAttribText(xaa.getAttribVarchar());
                setAttribDatetime(xaa.getAttribDatetime());
            }
        } catch (Exception e) {
            throw new XincoException();
        }
    }

    //create add attribute object for data structures
    public XincoAddAttributeServer(int attrID1, int attrID2, int attrI, int attrUI,
            double attrD, String attrVC, String attrT, Date attrDT) throws XincoException {
        getXincoAddAttributePK().setXincoCoreDataId(attrID1);
        getXincoAddAttributePK().setAttributeId(attrID2);
        setAttribInt(attrI);
        setAttribUnsignedint(attrUI);
        setAttribDouble(attrD);
        setAttribVarchar(attrVC);
        setAttribText(attrT);
        setAttribDatetime(attrDT);
    }

    public boolean write2DB() {
        String attrT = "";
        String attrVC = "";
        Date attrDT = null;
        if (getAttribText() != null) {
            attrT = getAttribText();
            attrT = attrT.replaceAll("'", "\\\\'");
        } else {
            attrT = "NULL";
        }
        if (getAttribVarchar() != null) {
            attrVC = getAttribVarchar();
            attrVC = attrVC.replaceAll("'", "\\\\'");
        } else {
            attrVC = "NULL";
        }
        if (getAttribDatetime() != null) {
            //convert clone from remote time to local time
            Calendar calO = (Calendar) getAttribDatetime().clone();
            Calendar cal = (Calendar) getAttribDatetime().clone();
            Calendar ngc = new GregorianCalendar();
            cal.add(Calendar.MILLISECOND, (ngc.get(Calendar.ZONE_OFFSET) - calO.get(Calendar.ZONE_OFFSET)) -
                    (ngc.get(Calendar.DST_OFFSET) + calO.get(Calendar.DST_OFFSET)));
            attrDT = new Date(cal.getTimeInMillis());
        }
        XincoAddAttribute xaa = new XincoAddAttribute();
        xaa.getXincoAddAttributePK().setXincoCoreDataId(getXincoAddAttributePK().getXincoCoreDataId());
        xaa.getXincoAddAttributePK().setAttributeId(getXincoAddAttributePK().getAttributeId());
        xaa.setAttribInt(getAttribInt());
        xaa.setAttribUnsignedint(getAttribUnsignedint());
        xaa.setAttribDouble(getAttribDouble());
        xaa.setAttribVarchar(attrVC);
        xaa.setAttribText(attrT);
        xaa.setAttribDatetime(attrDT);
        pm.persist(xaa, false, true);
        return true;
    }
    //create complete list of add attributes

    @SuppressWarnings("unchecked")
    public static Vector getXincoAddAttributes(int attrID) {
        Vector addAttributes = new Vector();
        try {
            parameters.clear();
            parameters.put("xincoCoreDataId", attrID);
            result = pm.createdQuery("SELECT x FROM XincoAddAttribute x WHERE " +
                    "x.xincoAddAttributePK.xincoCoreDataId = :xincoCoreDataId order by x.xincoAddAttributePK.attributeId", parameters);
            while (!result.isEmpty()) {
                addAttributes.addElement((XincoAddAttribute) result.get(0));
                result.remove(0);
            }
        } catch (Exception e) {
            addAttributes.removeAllElements();
        }
        return addAttributes;
    }

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
        result = pm.namedQuery("XincoAddAttribute.findById", parameters);
        if (result.size() > 0) {
            XincoAddAttribute temp = (XincoAddAttribute) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws Exception {
        int counter = 0;
        String sql = "SELECT x FROM XincoAddAttributeServer x WHERE ";
        if (parameters.containsKey("xincoCoreDataId")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO, "Searching by xincoCoreDataId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.XincoAddAttributePK.xincoCoreDataId = :xincoCoreDataId";
            counter++;
        }
        if (parameters.containsKey("attributeId")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO, "Searching by attributeId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.XincoAddAttributePK.attributeId = :attributeId";
            counter++;
        }
        if (parameters.containsKey("designation")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO, "Searching by designation");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.designation = :designation";
            counter++;
        }
        if (parameters.containsKey("attribInt")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO, "Searching by attribInt");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.attribInt = :attribInt";
            counter++;
        }
        if (parameters.containsKey("attribUnsignedint")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO, "Searching by attribUnsignedint");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.attribUnsignedint = :attribUnsignedint";
            counter++;
        }
        if (parameters.containsKey("attribDouble")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO, "Searching by attribDouble");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.attribDouble = :attribDouble";
            counter++;
        }
        if (parameters.containsKey("attribVarchar")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO, "Searching by attribVarchar");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.attribVarchar = :attribVarchar";
            counter++;
        }
        if (parameters.containsKey("attribDatetime")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO, "Searching by attribDatetime");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.attribDatetime = :attribDatetime";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoAddAttributeServer temp[] = new XincoAddAttributeServer[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoAddAttributeServer) result.get(0);
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
    public AbstractAuditableObject create(AbstractAuditableObject value) {
        XincoAddAttributeServer temp;
        XincoAddAttribute newValue = new XincoAddAttribute();
        temp = (XincoAddAttributeServer) value;
        newValue.getXincoAddAttributePK().setAttributeId(temp.getXincoAddAttributePK().getAttributeId());
        newValue.getXincoAddAttributePK().setXincoCoreDataId(temp.getXincoAddAttributePK().getXincoCoreDataId());
        newValue.setAttribUnsignedint(temp.getAttribUnsignedint());
        newValue.setAttribDouble(temp.getAttribDouble());
        newValue.setAttribVarchar(temp.getAttribVarchar());
        newValue.setAttribText(temp.getAttribVarchar());
        newValue.setAttribDatetime(temp.getAttribDatetime());

        newValue.setRecordId(temp.getRecordId());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO,
                    "New value created: " + newValue);
        }
        return newValue;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoAddAttribute val = (XincoAddAttribute) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.INFO,
                    "Value updated: " + val);
        }
        return val;
    }

    public void delete(AbstractAuditableObject value) {
        try {
            XincoAddAttribute val = (XincoAddAttribute) value;
            XincoAddAttributeT temp = new XincoAddAttributeT();
            temp.setRecordId(val.getRecordId());
            temp.setAttributeId(getXincoAddAttributePK().getAttributeId());
            temp.setXincoCoreDataId(getXincoAddAttributePK().getXincoCoreDataId());
            temp.setAttribUnsignedint(getAttribUnsignedint());
            temp.setAttribDouble(getAttribDouble());
            temp.setAttribVarchar(getAttribVarchar());
            temp.setAttribText(getAttribVarchar());
            temp.setAttribDatetime(getAttribDatetime());

            pm.startTransaction();
            pm.persist(temp, false, false);
            pm.delete(val, false);
            getModifiedRecordDAOObject().saveAuditData();
            pm.commitAndClose();
        } catch (Throwable ex) {
            Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("attributeId", getXincoAddAttributePK().getAttributeId());
        temp.put("xincoCoreDataId", getXincoAddAttributePK().getXincoCoreDataId());
        return temp;
    }

    public int getNewID(boolean a) {
        return new XincoIDServer("Xinco_Add_Attribute").getNewTableID(a);
    }

    public boolean deleteFromDB() {
        setTransactionTime(DateRange.startingNow());
        try {
            AuditingDAOHelper.delete(this, getXincoAddAttributePK(), getChangerID());
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoAddAttributeServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }
}
