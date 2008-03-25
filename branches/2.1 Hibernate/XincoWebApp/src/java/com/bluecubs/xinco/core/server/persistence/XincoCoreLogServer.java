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
 * Name:            XincoCoreLogServer
 *
 * Description:     log
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
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.XincoVersion;
import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.core.exception.XincoSettingException;
import com.bluecubs.xinco.persistence.XincoCoreLog;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditableDAO;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

public class XincoCoreLogServer extends XincoCoreLog implements AuditableDAO, PersistenceServerObject {

    private static List result;

    /**
     * Create single log object for data structures
     * @param id
     * @throws com.bluecubs.xinco.core.exception.XincoException 
     */
    @SuppressWarnings("unchecked")
    public XincoCoreLogServer(int id) throws XincoException {
        pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        try {
            parameters.clear();
            parameters.put("id", id);
            result = pm.namedQuery("XincoCoreLog.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                XincoCoreLog temp = (XincoCoreLog) result.get(0);
                setId(temp.getId());
                setXincoCoreDataId(temp.getXincoCoreDataId());
                setXincoCoreUserId(temp.getXincoCoreUserId());
                setOpCode(temp.getOpCode());
                setOpDatetime(temp.getOpDatetime());
                setOpDescription(temp.getOpDescription());
                setVersionHigh(temp.getVersionHigh());
                setVersionMid(temp.getVersionMid());
                setVersionLow(temp.getVersionLow());
                setVersionPostfix(temp.getVersionPostfix());
            } else {
                throw new XincoException();
            }
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.INFO, null, e);
            }
            throw new XincoException();
        }
    }

    /**
     * Create single log object for data structures
     * @param attrID
     * @param attrCDID
     * @param attrUID
     * @param attrOC
     * @param attrODT
     * @param attrOD
     * @param attrVH
     * @param attrVM
     * @param attrVL
     * @param attrVP
     * @throws com.bluecubs.xinco.core.exception.XincoException 
     */
    public XincoCoreLogServer(int attrID, int attrCDID, int attrUID, int attrOC, Date attrODT, String attrOD, int attrVH, int attrVM, int attrVL, String attrVP) throws XincoException {
        pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        setId(attrID);
        setXincoCoreDataId(attrCDID);
        setXincoCoreUserId(attrUID);
        setOpCode(attrOC);
        setOpDatetime(attrODT);
        setOpDescription(attrOD);
        setVersionHigh(attrVH);
        setVersionMid(attrVM);
        setVersionLow(attrVL);
        setVersionPostfix(attrVP);
    }

    /**
     * Create complete log list for data
     * @param id
     * @return Vector containing all XincoCoreLogs for the specified id.
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreLogs(int id) {
        Vector coreLog = new Vector();
        try {
            parameters.clear();
            parameters.put("id", id);
            result = pm.namedQuery("XincoCoreLog.findById", parameters);
            while (!result.isEmpty()) {
                XincoCoreLog temp = (XincoCoreLog) result.get(0);
                coreLog.addElement(new XincoCoreLogServer(temp.getId(),
                        temp.getXincoCoreDataId(), temp.getXincoCoreUserId(),
                        temp.getOpCode(), temp.getOpDatetime(), temp.getOpDescription(),
                        temp.getVersionHigh(), temp.getVersionMid(), temp.getVersionLow(),
                        temp.getVersionPostfix()));
                result.remove(0);
            }
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.INFO, null, e);
                }
                coreLog.removeAllElements();
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return coreLog;
    }

    /**
     * Get Version
     * @return XincoVersion
     */
    public XincoVersion getVersion() {
        XincoVersion temp = new XincoVersion();
        temp.setVersionHigh(getVersionHigh());
        temp.setVersionMid(getVersionMid());
        temp.setVersionLow(getVersionLow());
        temp.setVersionPostfix(getVersionPostfix());
        return temp;
    }

    /**
     * Set Version
     * @param version XincoVersion
     */
    public void setVersion(XincoVersion version) {
        setVersionHigh(version.getVersionHigh());
        setVersionMid(version.getVersionMid());
        setVersionLow(version.getVersionLow());
        setVersionPostfix(version.getVersionPostfix());
    }

    public AbstractAuditableObject findById(HashMap parameters) throws XincoException {
        result = pm.namedQuery("XincoCoreLog.findById", parameters);
        if (result.size() > 0) {
            XincoCoreLog temp = (XincoCoreLog) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws XincoException, XincoSettingException {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreLog x WHERE ";
        if (parameters.containsKey("xincoCoreUserId")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.INFO, "Searching by xincoCoreUserId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreUserId = :xincoCoreUserId";
            counter++;
        }
        if (parameters.containsKey("xincoCoreDataId")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.INFO, "Searching by xincoCoreDataId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreDataId = :xincoCoreDataId";
            counter++;
        }
        if (parameters.containsKey("opCode")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.INFO, "Searching by opCode");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.opCode = :opCode";
            counter++;
        }
        if (parameters.containsKey("opDatetime")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.INFO, "Searching by opDatetime");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.opDatetime = :opDatetime";
            counter++;
        }
        if (parameters.containsKey("opDescription")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.INFO, "Searching by opDescription");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.opDescription = :opDescription";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoCoreLog temp[] = new XincoCoreLog[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreLog) result.get(0);
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
        try {
            XincoCoreLog temp;
            XincoCoreLog newValue = new XincoCoreLog();
            temp = (XincoCoreLog) value;
            if (!value.isCreated()) {
                newValue.setId(temp.getId());
                newValue.setRecordId(temp.getRecordId());
            } else {
                newValue.setId(getNewID());
            }
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.INFO, "Creating with new id: " + newValue.getId());
            }
            newValue.setXincoCoreUserId(temp.getXincoCoreUserId());
            newValue.setXincoCoreDataId(temp.getXincoCoreDataId());
            newValue.setOpCode(temp.getOpCode());
            newValue.setOpDatetime(temp.getOpDatetime());
            newValue.setOpDescription(temp.getOpDescription());
            newValue.setVersionHigh(temp.getVersionHigh());
            newValue.setVersionMid(temp.getVersionMid());
            newValue.setVersionLow(temp.getVersionLow());
            newValue.setVersionPostfix(temp.getVersionPostfix());
            newValue.setCreated(temp.isCreated());
            newValue.setChangerID(temp.getChangerID());
            newValue.setTransactionTime(getTransactionTime());
            pm.persist(newValue, false, true);
            return newValue;
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        //XincoCoreLogs are an Audit table so can't be updated. Return same object.
        return value;
    }

    public void delete(AbstractAuditableObject value) {
        XincoCoreLog val = (XincoCoreLog) value;
        pm.startTransaction();
        pm.delete(val, false);
        getModifiedRecordDAOObject().saveAuditData();
        pm.commitAndClose();
    }

    @SuppressWarnings("unchecked")
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("id", getId());
        return temp;
    }

    public int getNewID() {
        return new XincoIDServer("xinco_core_log").getNewTableID();
    }

    public boolean deleteFromDB() {
        setTransactionTime(DateRange.startingNow());
        try {
            AuditingDAOHelper.delete(this, getId());
            return true;
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.SEVERE, null, e);
                }
                return false;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }

    public boolean write2DB() {
        try {
            if (getId() > 0) {
                AuditingDAOHelper.update(this, new XincoCoreLog(getId()));
            } else {
                XincoCoreLog temp = new XincoCoreLog();
                temp.setId(getId());
                temp.setChangerID(getChangerID());
                temp.setCreated(true);
                temp.setXincoCoreUserId(temp.getXincoCoreUserId());
                temp.setId(getId());
                temp.setXincoCoreDataId(getXincoCoreDataId());
                temp.setOpCode(getOpCode());
                temp.setOpDatetime(getOpDatetime());
                temp.setOpDescription(getOpDescription());
                temp.setVersionHigh(getVersionHigh());
                temp.setVersionMid(getVersionMid());
                temp.setVersionLow(getVersionLow());
                temp.setVersionPostfix(getVersionPostfix());
                temp.setCreated(isCreated());
                temp.setChangerID(getChangerID());
                temp.setTransactionTime(getTransactionTime());
                temp = (XincoCoreLog) AuditingDAOHelper.create(this, temp);
                setId(temp.getId());
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.INFO, "Assigned id: " + getId());
                }
            }
            return true;
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.SEVERE, null, e);
                }
                return false;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoCoreLogServer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }
}
