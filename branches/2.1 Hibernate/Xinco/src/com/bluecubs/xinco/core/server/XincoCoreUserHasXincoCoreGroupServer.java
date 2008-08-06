/**
 *Copyright 2008 blueCubs.com
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
 * Name:            XincoCoreUserServer
 *
 * Description:     user
 *
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 * Date:            2008
 *
 * Modifications:
 *
 * Who?             When?             What?
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.hibernate.audit.XincoAuditableDAO;
import com.bluecubs.xinco.core.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.persistence.XincoCoreUserHasXincoCoreGroupT;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreUserHasXincoCoreGroupServer extends XincoCoreUserHasXincoCoreGroup implements XincoAuditableDAO, PersistenceServerObject {
    private static final long serialVersionUID = -6641477448478703235L;

    private List result;
    //create data type attribute object for data structures
    public XincoCoreUserHasXincoCoreGroupServer(int xincoCoreUserId, int xincoCoreGroupId) throws XincoException {
        try {
            parameters.clear();
            parameters.put("xincoCoreUserId", xincoCoreUserId);
            parameters.put("xincoCoreGroupId", xincoCoreGroupId);
            result = pm.createdQuery("SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE " +
                    "x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreUserId = :xincoCoreUserId and " +
                    "x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreGroupId = :xincoCoreGroupId", parameters);
            if (!result.isEmpty()) {
                XincoCoreUserHasXincoCoreGroup xcdt = (XincoCoreUserHasXincoCoreGroup) result.get(0);
                getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreGroupId(xcdt.getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId());
                getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreUserId(xcdt.getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreUserId());
                setStatusNumber(xcdt.getStatusNumber());
            } else {
                throw new XincoException();
            }
        } catch (Exception e) {
            throw new XincoException();
        }

    }
    //create data type attribute object for data structures
    public XincoCoreUserHasXincoCoreGroupServer(int xincoCoreUserId, int xincoCoreGroupId, int statusNumber) throws XincoException {
        getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreGroupId(xincoCoreGroupId);
        getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreUserId(xincoCoreUserId);
        setStatusNumber(statusNumber);
    }

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
        result = pm.createdQuery("SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE " +
                "x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreUserId = :xincoCoreUserId and " +
                "x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreGroupId = :xincoCoreGroupId", parameters);
        if (result.size() > 0) {
            XincoCoreUserHasXincoCoreGroup temp = (XincoCoreUserHasXincoCoreGroup) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws Exception {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE ";
        if (parameters.containsKey("xincoCoreUserId")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.INFO, "Searching by xincoCoreUserId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreUserId = :xincoCoreUserId";
            counter++;
        }
        if (parameters.containsKey("xincoCoreGroupId")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.INFO, "Searching by xincoCoreGroupId");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreGroupId = :xincoCoreGroupId";
            counter++;
        }
        if (parameters.containsKey("statusNumber")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.INFO, "Searching by statusNumber");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.statusNumber = :statusNumber";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoCoreUserHasXincoCoreGroupServer temp[] = new XincoCoreUserHasXincoCoreGroupServer[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreUserHasXincoCoreGroupServer) result.get(0);
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
        XincoCoreUserHasXincoCoreGroupServer temp;
        XincoCoreUserHasXincoCoreGroup newValue = new XincoCoreUserHasXincoCoreGroup();
        temp = (XincoCoreUserHasXincoCoreGroupServer) value;
        newValue.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreGroupId(temp.getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId());
        newValue.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreUserId(temp.getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreUserId());
        newValue.setStatusNumber(temp.getStatusNumber());

        newValue.setRecordId(temp.getRecordId());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.INFO,
                    "New value created: " + newValue);
        }
        return newValue;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoCoreUserHasXincoCoreGroupServer val = (XincoCoreUserHasXincoCoreGroupServer) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.INFO,
                    "Value updated: " + val);
        }
        return val;
    }

    public void delete(AbstractAuditableObject value) {
        try {
            XincoCoreUserHasXincoCoreGroupServer val = (XincoCoreUserHasXincoCoreGroupServer) value;
            result = pm.createdQuery("SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE " +
                    "x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreUserId = :xincoCoreUserId and " +
                    "x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreGroupId = :xincoCoreGroupId", val.getParameters());
            pm.startTransaction();
            while (!result.isEmpty()) {

                XincoCoreUserHasXincoCoreGroupT temp = new XincoCoreUserHasXincoCoreGroupT();
                temp.setRecordId(((XincoCoreUserHasXincoCoreGroup) result.get(0)).getRecordId());

                temp.setXincoCoreGroupId(val.getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId());
                temp.setXincoCoreUserId(val.getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreUserId());
                temp.setStatusNumber(val.getStatusNumber());

                pm.persist(temp, false, false);
                pm.delete(val, false);
                getModifiedRecordDAOObject().saveAuditData();
                result.remove(0);
            }
            pm.commitAndClose();
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("xincoCoreGroupId", getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId());
        temp.put("xincoCoreUserId", getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreUserId());
        return temp;
    }

    public int getNewID() {
        return new XincoIDServer("XincoCoreUserHasXincoCoreGroup").getNewTableID();
    }

    public boolean write2DB() {
        try {
            if (getXincoCoreUserHasXincoCoreGroupPK() != null) {
                AuditingDAOHelper.update(this, new XincoCoreUserHasXincoCoreGroup());
            } else {
                XincoCoreUserHasXincoCoreGroup temp = new XincoCoreUserHasXincoCoreGroup();
                temp.setChangerID(getChangerID());
                temp.setCreated(true);

                temp.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreGroupId(getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId());
                temp.getXincoCoreUserHasXincoCoreGroupPK().setXincoCoreUserId(getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreUserId());
                temp.setStatusNumber(getStatusNumber());

                temp = (XincoCoreUserHasXincoCoreGroup) AuditingDAOHelper.create(this, temp);
                setXincoCoreUserHasXincoCoreGroupPK(temp.getXincoCoreUserHasXincoCoreGroupPK());
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.INFO, "Assigned id: " + getXincoCoreUserHasXincoCoreGroupPK());
                }
            }
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    public boolean deleteFromDB() {
        setTransactionTime(DateRange.startingNow());
        try {
            AuditingDAOHelper.delete(this, getXincoCoreUserHasXincoCoreGroupPK());
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }
}
