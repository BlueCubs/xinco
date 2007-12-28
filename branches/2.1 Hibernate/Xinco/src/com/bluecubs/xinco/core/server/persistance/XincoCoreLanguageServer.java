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
 * Name:            XincoCoreLanguageServer
 *
 * Description:     Language
 *
 * Original Author: Javier A. Ortiz
 * Date:            2007
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
import com.bluecubs.xinco.core.persistance.XincoCoreLanguage;
import com.bluecubs.xinco.core.persistance.audit.XincoCoreLanguageT;
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
 *
 * @author javydreamercsw
 */
public class XincoCoreLanguageServer extends XincoCoreLanguage implements XincoAuditableDAO, XincoPersistanceServerObject {

    private static XincoPersistanceManager pm = new XincoPersistanceManager();
    private static List result;
    private static HashMap parameters;

    /**
     * Create language object for data structures
     * @param id
     * @throws com.bluecubs.xinco.core.XincoException
     */
    @SuppressWarnings("unchecked")
    public XincoCoreLanguageServer(int id) throws XincoException {
        try {
            parameters = new HashMap();
            parameters.put("id", id);
            result = pm.namedQuery("XincoCoreLanguage.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                XincoCoreLanguage temp = (XincoCoreLanguage) result.get(0);
                setId(temp.getId());
                setSign(temp.getSign());
                setDesignation(temp.getDesignation());
            } else {
                throw new XincoException();
            }
        } catch (Throwable e) {
            if (new XincoSettingServer().getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }
    
    /**
     * Create language object for data structures
     */
    public XincoCoreLanguageServer(){
        
    }

    /**
     * Create language object for data structures
     * @param id
     * @param sign
     * @param designation
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreLanguageServer(int id, String sign, String designation) throws XincoException {
        setId(id);
        setSign(sign);
        setDesignation(designation);
    }

    /**
     * Create language object for data structures
     * @return Vector
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreLanguages() {
        Vector coreLanguages = new Vector();
        try {
            parameters = new HashMap();
            result = pm.createdQuery("SELECT p FROM XincoCoreLanguage p ORDER BY p.designation", parameters);
            while (!result.isEmpty()) {
                XincoCoreLanguage temp = (XincoCoreLanguage) result.get(0);
                coreLanguages.add(new XincoCoreLanguageServer(temp.getId(), temp.getSign(), temp.getDesignation()));
                result.remove(0);
            }
        } catch (Throwable e) {
            coreLanguages.removeAllElements();
        }
        return coreLanguages;
    }

    /**
     * Check if language is in use by other objects
     * @param xcl
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    public static boolean isLanguageUsed(XincoCoreLanguage xcl) {
        boolean is_used = false;
        try {
            parameters = new HashMap();
            parameters.put("xincoCoreLanguageId", xcl.getId());
            result = pm.namedQuery("XincoCoreNode.findByXincoCoreLanguageId", parameters);
            if (result.size() > 0) {
                is_used = true;
            }
            if (!is_used) {
                parameters.put("xincoCoreLanguageId", xcl.getId());
                result = pm.namedQuery("XincoCoreData.findByXincoCoreLanguageId", parameters);
                if (result.size() > 0) {
                    is_used = true;
                }
            }
        } catch (Throwable e) {
            is_used = true; // rather lock language in case of error!
        }
        return is_used;
    }

    public XincoAbstractAuditableObject findById(
            HashMap parameters) throws DataRetrievalFailureException {
        result = pm.namedQuery("XincoCoreLanguage.findById", parameters);
        XincoCoreLanguage temp = (XincoCoreLanguage) result.get(0);
        temp.setTransactionTime(getTransactionTime());
        temp.setChangerID(getChangerID());
        return temp;
    }

    @SuppressWarnings("unchecked")
    public XincoAbstractAuditableObject[] findWithDetails(HashMap parameters) throws DataRetrievalFailureException {
        result=pm.namedQuery("XincoCoreLanguage.findByDesignation", parameters);
        XincoCoreLanguage temp[] = new XincoCoreLanguage[1];
        temp[0] = (XincoCoreLanguage) result.get(0);
        temp[0].setTransactionTime(getTransactionTime());
        return temp;
    }

    public XincoAbstractAuditableObject create(
            XincoAbstractAuditableObject value) {
        XincoCoreLanguage temp, newValue = new XincoCoreLanguage();
        boolean exists = false;
        temp = (XincoCoreLanguage) value;
        if (!value.isCreated()) {
            newValue.setId(temp.getId());
            newValue.setRecordId(temp.getRecordId());
        } else {
            newValue.setId(temp.getRecordId());
        }
        newValue.setSign(temp.getSign());
        newValue.setDesignation(temp.getDesignation());
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
        System.out.println("Creating with id: "+newValue.getId());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, exists, true);
        return newValue;
    }

    public XincoAbstractAuditableObject update(
            XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoCoreLanguage val = (XincoCoreLanguage) value;
        XincoCoreLanguageT temp = new XincoCoreLanguageT();
        temp.setRecordId(val.getRecordId());
        if (!value.isCreated()) {
            temp.setId(val.getId());
        } else {
            temp.setId(val.getRecordId());
        }
        temp.setId(val.getId());
        temp.setSign(val.getSign());
        temp.setDesignation(val.getDesignation());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.persist(val, true, false);
        val.saveAuditData(pm);
        pm.commitAndClose();
        return val;
    }

    public void delete(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoCoreLanguage val = (XincoCoreLanguage) value;
        XincoCoreLanguageT temp = new XincoCoreLanguageT();
        temp.setRecordId(val.getRecordId());
        if (!value.isCreated()) {
            temp.setId(val.getId());
        } else {
            temp.setId(val.getRecordId());
        }
        temp.setId(val.getId());
        temp.setSign(val.getSign());
        temp.setDesignation(val.getDesignation());
        pm.startTransaction();
        pm.delete(temp, false);
        pm.persist(val, true, false);
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
        return new XincoIDServer("xinco_core_language").getNewID();
    }

    public boolean deleteFromDB() throws XincoException {
        setTransactionTime(DateRange.startingNow());
        try {
            XincoAuditingDAOHelper.delete(this, getId());
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
            if (getId() > 0) {
                XincoAuditingDAOHelper.update(this, new XincoCoreLanguage(getId()));
            } else {
                XincoCoreLanguage temp = new XincoCoreLanguage();
                temp.setId(getNewID());
                temp.setChangerID(getChangerID());
                temp.setCreated(isCreated());
                temp.setSign(getSign());
                temp.setDesignation(getDesignation());
                XincoAuditingDAOHelper.create(this, temp);
            }
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLanguageServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
        return true;
    }
}
