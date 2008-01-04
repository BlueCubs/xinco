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
 * Name:            XincoCoreDataServer
 *
 * Description:     data object
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
import com.bluecubs.xinco.core.persistance.XincoAddAttribute;
import com.bluecubs.xinco.core.persistance.XincoCoreACE;
import com.bluecubs.xinco.core.persistance.XincoCoreData;
import com.bluecubs.xinco.core.persistance.XincoCoreLog;
import com.bluecubs.xinco.core.persistance.audit.XincoCoreDataT;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditableDAO;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditingDAOHelper;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Create data object for data structures
 * @author Alexander Manes
 * Statuses:
 * 1 => Open
 * 2 => Locked
 * 3 => Archived
 * 4 => Checked Out
 * 5 => Published
 */
public class XincoCoreDataServer extends XincoCoreData implements XincoAuditableDAO, XincoPersistanceServerObject {

    private XincoCoreUserServer user;
    private static XincoPersistanceManager pm = new XincoPersistanceManager();
    private static List result;
    private static HashMap parameters;
    private Vector xinco_core_logs;
    private Vector xincoAddAttributes,  xincoCoreACL;

    /**
     * create data object for data structures
     * @param attrID Data id
     * @throws com.bluecubs.xinco.core.XincoException 
     */
    @SuppressWarnings("unchecked")
    public XincoCoreDataServer(int attrID) throws XincoException {
        try {
            parameters = new HashMap();
            parameters.put("id", attrID);
            result = pm.namedQuery("XincoCoreData.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                XincoCoreData temp = (XincoCoreData) result.get(0);
                setId(temp.getId());
                setXincoCoreNodeId(temp.getXincoCoreNodeId());
                setXincoCoreLanguageId(temp.getXincoCoreLanguageId());
                setXincoCoreDataTypeId(temp.getXincoCoreDataTypeId());
                //load logs
                setXincoCoreLogs(XincoCoreLogServer.getXincoCoreLogs(getId()));
                //load add attributes
                setXincoAddAttributes(XincoAddAttributeServer.getXincoAddAttributes(getId()));
                setDesignation(temp.getDesignation());
                setStatusNumber(temp.getStatusNumber());
                //load acl for this object
                setXincoCoreACL(XincoCoreACEServer.getXincoCoreACL(temp.getId(), "xincoCoredataId"));
            } else {
                throw new XincoException();
            }
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.SEVERE, null, ex);
            getXincoCoreACL().removeAllElements();
            throw new XincoException();
        }
    }

    /**
     * create data object for data structures
     * @param attrID ID
     * @param attrCNID XincoCoreNodeId
     * @param attrLID XincoCorelanguageId
     * @param attrDTID XincoCoreDataTypeId
     * @param attrD Designation
     * @param attrSN Status number
     * @param DBM XincoDBManager
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreDataServer(int attrID, int attrCNID, int attrLID, int attrDTID, String attrD, int attrSN, XincoDBManager DBM) throws XincoException {
        setId(attrID);
        setXincoCoreNodeId(attrCNID);
        setXincoCoreLanguageId(attrLID);
        setXincoCoreDataTypeId(attrDTID);
        //load logs
        setXincoCoreLogs(XincoCoreLogServer.getXincoCoreLogs(attrID));
        //load add attributes
        //security: don't load add attribute, force direct access to data including check of access rights!
        setXincoAddAttributes(new Vector());
        setDesignation(attrD);
        setStatusNumber(attrSN);
        //load acl for this object
        setXincoCoreACL(XincoCoreACEServer.getXincoCoreACL(getId(), "xincoCoredataId"));
    }

    /**
     * Set XincoCoreuser
     * @param user
     */
    public void setUser(XincoCoreUserServer user) {
        this.user = user;
    }

    /**
     * Load binay data from XincoCoreData
     * @param xincoCoredata
     * @return
     */
    public static byte[] loadBinaryData(XincoCoreData xincoCoredata) {
        byte[] binary_data = null;
        return binary_data;
    }

    /**
     * Find XincoCoreData
     * @param designation
     * @param attrLID
     * @param attrSA
     * @param attrSFD
     * @param DBM
     * @return Vector
     */
    @SuppressWarnings("unchecked")
    public static Vector findXincoCoreData(String designation, int attrLID, boolean attrSA, boolean attrSFD, XincoDBManager DBM) {
        Vector data = new Vector();
        try {
            parameters = new HashMap();
            String lang = "";
            if (attrLID != 0) {
                lang = "AND p.xincoCorelanguageId = :attrLID";
                parameters.put("attrLID", attrLID);
            }
            parameters.put("designation", designation);
            if (attrSA) {
                result = pm.createdQuery("SELECT DISTINCT p FROM XincoCoreData p, XincoAddAttribute a WHERE p.id = a.XincoCoreDataId AND p.designation LIKE '" + designation + "%' OR a.attribVarchar LIKE :designation OR a.attribText LIKE :designation " + lang + " ORDER BY p.designation, p.XincoCoreLanguageId", parameters);
            } else {
                result = pm.createdQuery("SELECT DISTINCT p FROM XincoCoreData p WHERE p.designation LIKE :designation " + lang + " ORDER BY p.designation, p.XincoCoreLanguageId", parameters);
            }
            int i = 0;
            while (!result.isEmpty()) {
                XincoCoreDataServer temp = (XincoCoreDataServer) result.get(0);
                data.add(new XincoCoreDataServer(temp.getId()));
                i++;
                if (i >= XincoPersistanceManager.config.getMaxSearchResult()) {
                    break;
                }
                result.remove(0);
            }
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.SEVERE, null, ex);
            data.removeAllElements();
        }
        return data;
    }

    /**
     * Get XincoCOreData path
     * @param attrRP
     * @param attrID
     * @param attrFN
     * @return String
     */
    public static String getXincoCoreDataPath(String attrRP, int attrID, String attrFN) {
        String path = null;
        // convert ID to String
        String path4Id = "" + attrID;
        // fill ID String with zeros
        while (path4Id.length() < 10) {
            path4Id = "0" + path4Id;
        }
        // shorten to 7 chars
        path4Id = path4Id.substring(0, 7);
        // add seperator
        for (int i = 0; i < 7; i++) {
            path4Id = path4Id.substring(0, (i * 2 + 1)) + System.getProperty("file.separator") + path4Id.substring((i * 2 + 1));
        }
        // create path if neccessary
        (new File(attrRP + path4Id)).mkdirs();
        // check if file exists at NEW location (>= xinco DMS 2.0)
        if ((new File(attrRP + path4Id + attrFN)).exists()) {
            // output NEW location
            path = attrRP + path4Id + attrFN;
        } else {
            // check if file exists at OLD location (pre xinco DMS 2.0)
            if ((new File(attrRP + attrFN)).exists()) {
                // output OLD location
                path = attrRP + attrFN;
            } else {
                // output NEW location for NEW file
                path = attrRP + path4Id + attrFN;
            }
        }
        return path;
    }

    public XincoAbstractAuditableObject findById(HashMap parameters) throws DataRetrievalFailureException {
        result = pm.namedQuery("XincoCoreData.findById", parameters);
        XincoCoreData temp = (XincoCoreData) result.get(0);
        temp.setTransactionTime(getTransactionTime());
        temp.setChangerID(getChangerID());
        return temp;
    }

    public XincoAbstractAuditableObject[] findWithDetails(HashMap parameters) throws DataRetrievalFailureException {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreData x WHERE ";
        if (parameters.containsKey("xincoCoreNodeId")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.INFO, "Searching by xincoCoreNodeId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreNodeId = :xincoCoreNodeId";
            counter++;
        }
        if (parameters.containsKey("xincoCoreLanguageId")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.INFO, "Searching by xincoCoreLanguageId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreLanguageId = :xincoCoreLanguageId";
            counter++;
        }
        if (parameters.containsKey("xincoCoreDataTypeId")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.INFO, "Searching by xincoCoreDataTypeId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreDataTypeId = :xincoCoreDataTypeId";
            counter++;
        }
        if (parameters.containsKey("designation")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.INFO, "Searching by designation");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.designation = :designation";
            counter++;
        }
        if (parameters.containsKey("statusNumber")) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.INFO, "Searching by statusNumber");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.statusNumber = :statusNumber";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoCoreData temp[] = new XincoCoreData[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreData) result.get(0);
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
        XincoCoreData temp, newValue = new XincoCoreData();
        temp = (XincoCoreData) value;
        if (!value.isCreated()) {
            newValue.setId(temp.getId());
            newValue.setRecordId(temp.getRecordId());
        } else {
            newValue.setId(getNewID());
        }
        if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.INFO, "Creating with new id: " + newValue.getId());
        }
        newValue.setDesignation(temp.getDesignation());
        newValue.setStatusNumber(temp.getStatusNumber());
        newValue.setXincoCoreNodeId(temp.getXincoCoreNodeId());
        newValue.setXincoCoreDataTypeId(temp.getXincoCoreDataTypeId());
        newValue.setXincoCoreLanguageId(temp.getXincoCoreLanguageId());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        return newValue;
    }

    public XincoAbstractAuditableObject update(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoCoreData val = (XincoCoreData) value;
        XincoCoreDataT temp = new XincoCoreDataT();
        temp.setRecordId(val.getRecordId());
        if (!value.isCreated()) {
            temp.setId(val.getId());
        } else {
            temp.setId(val.getRecordId());
        }
        temp.setDesignation(val.getDesignation());
        temp.setStatusNumber(val.getStatusNumber());
        temp.setXincoCoreNodeId(val.getXincoCoreNodeId());
        temp.setXincoCoreDataTypeId(val.getXincoCoreDataTypeId());
        temp.setXincoCoreLanguageId(val.getXincoCoreLanguageId());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.persist(val, true, false);
        val.saveAuditData(pm);
        pm.commitAndClose();
        return val;
    }

    public void delete(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoCoreData val = (XincoCoreData) value;
        XincoCoreDataT temp = new XincoCoreDataT();
        temp.setRecordId(val.getRecordId());
        temp.setId(val.getId());
        temp.setDesignation(val.getDesignation());
        temp.setStatusNumber(val.getStatusNumber());
        temp.setXincoCoreNodeId(val.getXincoCoreNodeId());
        temp.setXincoCoreDataTypeId(val.getXincoCoreDataTypeId());
        temp.setXincoCoreLanguageId(val.getXincoCoreLanguageId());
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
        return new XincoIDServer("xinco_core_data").getNewID();
    }

    @SuppressWarnings("unchecked")
    public boolean deleteFromDB() throws XincoException {
        setTransactionTime(DateRange.startingNow());
        try {
            XincoAuditingDAOHelper.delete(this, getId());
            Vector objects = new Vector();
            XincoCoreLogServer temp;
            // Get related logs
            result = pm.createdQuery("select p FROM XincoCoreLog WHERE p.xincoCoreDataId= :id", getParameters());
            while (!result.isEmpty()) {
                XincoCoreLog t = (XincoCoreLog) result.get(0);
                temp = new XincoCoreLogServer(t.getId());
                temp.setChangerID(getChangerID());
                objects.add(temp);
                result.remove(0);
            }
            while (!objects.isEmpty()) {
                ((XincoCoreLogServer) objects.get(0)).deleteFromDB();
                objects.remove(0);
            }
            objects.removeAllElements();
            XincoCoreACEServer tempACE;
            // Get related ACE's
            result = pm.createdQuery("select p FROM XincoCoreData WHERE p.xincoCoredataId= :id", getParameters());
            while (!result.isEmpty()) {
                XincoCoreACE t = (XincoCoreACE) result.get(0);
                tempACE = new XincoCoreACEServer(t.getId());
                tempACE.setChangerID(getChangerID());
                objects.add(tempACE);
            }
            while (!objects.isEmpty()) {
                ((XincoCoreACEServer) objects.get(0)).deleteFromDB();
                objects.remove(0);
            }
            objects.removeAllElements();
            XincoAddAttributeServer tempAdd;
            // Get related Attributes
            result = pm.createdQuery("select p FROM XincoAddAttribute WHERE p.xincoCoredataId= :id", getParameters());
            while (!result.isEmpty()) {
                XincoAddAttribute t = (XincoAddAttribute) result.get(0);
                tempAdd = new XincoAddAttributeServer(t.getXincoAddAttributePK());
                tempAdd.setChangerID(getChangerID());
                objects.add(tempAdd);
            }
            while (!objects.isEmpty()) {
                ((XincoAddAttributeServer) objects.get(0)).deleteFromDB();
                objects.remove(0);
            }
            objects.removeAllElements();
            //Now delete the data
            int i = 0;
            //delete file / file = 1
            if (getXincoCoreDataTypeId() == 1) {
                try {
                    (new File(XincoCoreDataServer.getXincoCoreDataPath(XincoPersistanceManager.config.getFileRepositoryPath(), getId(), "" + getId()))).delete();
                } catch (Exception dfe) {
                // continue, file might not exists
                }
                // delete revisions created upon creation or checkin
                for (i = 0; i < this.getXincoCoreLogs().size(); i++) {
                    if ((((XincoCoreLog) getXincoCoreLogs().elementAt(i)).getOpCode() == 1) || (((XincoCoreLog) getXincoCoreLogs().elementAt(i)).getOpCode() == 5)) {
                        try {
                            (new File(XincoCoreDataServer.getXincoCoreDataPath(XincoPersistanceManager.config.getFileRepositoryPath(), getId(), getId() + "-" + ((XincoCoreLog) getXincoCoreLogs().elementAt(i)).getId()))).delete();
                        } catch (Exception drfe) {
                        // continue, delete next revision
                        }
                    }
                }
            }
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
                XincoAuditingDAOHelper.update(this, new XincoCoreData(getId()));
            } else {
                XincoCoreData temp = new XincoCoreData();
                temp.setId(getId());
                temp.setChangerID(getChangerID());
                temp.setCreated(true);
                temp.setStatusNumber(getStatusNumber());
                temp.setDesignation(getDesignation());
                temp.setXincoCoreDataTypeId(getXincoCoreDataTypeId());
                temp.setXincoCoreLanguageId(getXincoCoreLanguageId());
                temp.setXincoCoreNodeId(getXincoCoreNodeId());
                temp = (XincoCoreData) XincoAuditingDAOHelper.create(this, temp);
                setId(temp.getId());
            }
            //write add attributes
            XincoAddAttributeServer xaas;
            for (int i = 0; i < getXincoAddAttributes().size(); i++) {
                ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getXincoAddAttributePK().setXincoCoreDataId(getId());
                //copy fields from XincoAddAttribute to XincoAddAttributeServer
                xaas = new XincoAddAttributeServer(((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getXincoAddAttributePK(),
                        ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttribInt(),
                        ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttribUnsignedint(),
                        ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttribDouble(),
                        ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttribVarchar(),
                        ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttribText(),
                        ((XincoAddAttribute) getXincoAddAttributes().elementAt(i)).getAttribDatetime());
                xaas.write2DB();
            }
            return true;
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreDataServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    public Vector getXincoCoreLogs() {
        return xinco_core_logs;
    }

    public void setXincoCoreLogs(Vector xinco_core_logs) {
        this.xinco_core_logs = xinco_core_logs;
    }

    public Vector getXincoAddAttributes() {
        return xincoAddAttributes;
    }

    public void setXincoAddAttributes(Vector xincoAddAttributes) {
        this.xincoAddAttributes = xincoAddAttributes;
    }

    public Vector getXincoCoreACL() {
        return xincoCoreACL;
    }

    public void setXincoCoreACL(Vector xincoCoreACL) {
        this.xincoCoreACL = xincoCoreACL;
    }
}
