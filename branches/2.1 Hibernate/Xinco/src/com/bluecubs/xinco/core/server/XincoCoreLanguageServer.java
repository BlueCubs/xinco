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
 * Name:            XincoCoreLanguageServer
 *
 * Description:     language
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

import com.bluecubs.xinco.core.persistence.XincoCoreLanguageT;
import com.bluecubs.xinco.core.persistence.XincoCoreLanguage;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

public class XincoCoreLanguageServer extends XincoCoreLanguage implements XincoAuditableDAO, PersistenceServerObject {

    private static final long serialVersionUID = 607986769219212765L;
    private static List result;
    //create language object for data structures

    @SuppressWarnings("unchecked")
    public XincoCoreLanguageServer(int attrID) throws XincoException {
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = pm.namedQuery("XincoCoreLanguage.findById", parameters);
            //throw exception if no result found
            if (!result.isEmpty()) {
                XincoCoreLanguage xcg = (XincoCoreLanguage) result.get(0);
                setId(xcg.getId());
                setDesignation(xcg.getDesignation());
                setSign(xcg.getSign());
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            throw new XincoException(e.getLocalizedMessage());
        }
    }
    //create language object for data structures

    public XincoCoreLanguageServer(int attrID, String attrS, String attrD) throws XincoException {

        setId(attrID);
        setSign(attrS);
        setDesignation(attrD);

    }
    //create complete list of languages

    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreLanguages() {
        Vector coreLanguages = new Vector();
        try {
            result = pm.createdQuery("SELECT x FROM XincoCoreLanguage x ORDER BY x.designation", null);
            while (!result.isEmpty()) {
                coreLanguages.addElement((XincoCoreLanguage) result.get(0));
                result.remove(0);
            }
        } catch (Exception e) {
            coreLanguages.removeAllElements();
        }
        return coreLanguages;
    }
    //check if language is in use by other objects

    public static boolean isLanguageUsed(XincoCoreLanguage xcl) {
        boolean is_used = false;
        try {
            result = pm.createdQuery("SELECT x FROM XincoCoreNode x where x.xincoCoreLanguageId=" + xcl.getId(), null);
            if (!result.isEmpty()) {
                result = pm.createdQuery("SELECT x FROM XincoCoreData x where x.xincoCoreLanguageId=" + xcl.getId(), null);
                is_used = !result.isEmpty();
            }
        } catch (Exception e) {
            return true;// rather lock language in case of error!
        }
        return is_used;
    }

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
        result = pm.namedQuery("XincoCoreLanguage.findById", parameters);
        if (result.size() > 0) {
            XincoCoreLanguage temp = (XincoCoreLanguage) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws Exception {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreLanguage x WHERE ";
        if (parameters.containsKey("id")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.INFO, "Searching by id");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.id = :id";
            counter++;
        }
        if (parameters.containsKey("designation")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.INFO, "Searching by designation");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.designation = :designation";
            counter++;
        }
        if (parameters.containsKey("sign")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.INFO, "Searching by sign");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.sign = :sign";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoCoreLanguage temp[] = new XincoCoreLanguage[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreLanguage) result.get(0);
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
    public AbstractAuditableObject create(AbstractAuditableObject value) throws Exception{
        XincoCoreLanguage temp;
        XincoCoreLanguage newValue = new XincoCoreLanguage();

        temp = (XincoCoreLanguage) value;
        newValue.setId(temp.getRecordId());
        newValue.setDesignation(temp.getDesignation());
        newValue.setSign(temp.getSign());

        newValue.setRecordId(temp.getRecordId());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.INFO,
                    "New value created: " + newValue);
        }
        return newValue;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) throws Exception{
        XincoCoreLanguage val = (XincoCoreLanguage) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.INFO,
                    "Value updated: " + val);
        }
        return val;
    }

    @SuppressWarnings({"unchecked", "static-access"})
    public boolean delete(AbstractAuditableObject value) throws Exception{
        try {
            XincoCoreLanguage val = (XincoCoreLanguage) value;
            XincoCoreLanguageT temp = new XincoCoreLanguageT();
            temp.setRecordId(val.getRecordId());
            temp.setId(val.getId());

            temp.setDesignation(val.getDesignation());
            temp.setSign(val.getSign());

            pm.startTransaction();
            pm.persist(temp, false, false);
            pm.delete(val, false);
            setModifiedRecordDAOObject(value.getModifiedRecordDAOObject());
            //Make sure all audit data is stored properly. If not undo everything
            if (!getModifiedRecordDAOObject().saveAuditData()) {
                throw new XincoException(rb.getString("error.audit_data.invalid"));
            }
            return pm.commitAndClose();
        } catch (Throwable ex) {
            pm.rollback();
            Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
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
     * @param a 
     * @return New last ID
     */
    @SuppressWarnings("unchecked")
    public int getNewID(boolean a) {
        return new XincoIDServer("Xinco_Core_Language").getNewTableID(a);
    }

    @SuppressWarnings("unchecked")
    public boolean write2DB() {
        try {
            if (getId() > 0) {
                AuditingDAOHelper.update(this, new XincoCoreLanguage());
            } else {
                XincoCoreLanguage temp = new XincoCoreLanguage();
                temp.setChangerID(getChangerID());
                temp.setCreated(true);

                temp.setId(getId());
                temp.setDesignation(getDesignation());
                temp.setSign(getSign());

                temp = (XincoCoreLanguage) AuditingDAOHelper.create(this, temp);
                setId(temp.getId());
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreDataTypeServer.class.getName()).log(Level.INFO, "Assigned id: " + getId());
                }
            }
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public boolean deleteFromDB() {
        setTransactionTime(DateRange.startingNow());
        try {
            AuditingDAOHelper.delete(this, getId(), getChangerID());
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }
}
