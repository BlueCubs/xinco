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
 * Name:            XincoCoreUserHasXincoCoreGroupServer
 *
 * Description:     XincoCoreUserHasXincoCoreGroupServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            2008
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.core.exception.XincoSettingException;
import com.bluecubs.xinco.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.persistence.XincoCoreUserHasXincoCoreGroupPK;
import com.bluecubs.xinco.persistence.audit.XincoCoreUserHasXincoCoreGroupT;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditableDAO;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;



/**
 *
 * @author Javier A. Ortiz
 */
public class XincoCoreUserHasXincoCoreGroupServer extends XincoCoreUserHasXincoCoreGroup implements AuditableDAO, PersistenceServerObject {

    private static List result;

    @SuppressWarnings("unchecked")
    public XincoCoreUserHasXincoCoreGroupServer(XincoCoreUserHasXincoCoreGroupPK pk) throws XincoException {
        super(pk);
        pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        try {
            parameters.clear();
            parameters.put("xincoCoreUserId", pk.getXincoCoreUserId());
            parameters.put("xincoCoreGroupId", pk.getXincoCoreGroupId());
            result = pm.createdQuery("SELECT x FROM XincoCoreUserHasXincoCoreGroup x " +
                    "WHERE x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreUserId = :xincoCoreUserId and " +
                    "x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreGroupId = :xincoCoreGroupId", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                XincoCoreUserHasXincoCoreGroup temp = (XincoCoreUserHasXincoCoreGroup) result.get(0);
                setXincoCoreUserHasXincoCoreGroupPK(temp.getXincoCoreUserHasXincoCoreGroupPK());
                setStatusNumber(temp.getStatusNumber());
            } else {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.INFO, "Error looking for XincoCoreUserHasXincoCoreGroupServer with parameters: " + parameters);
                }
                throw new XincoException();
            }
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    public XincoCoreUserHasXincoCoreGroupServer(XincoCoreUserHasXincoCoreGroupPK pk, int statusNumber) {
        super(pk, statusNumber);
        try {
            pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public AbstractAuditableObject findById(HashMap parameters) throws XincoException {
        result = pm.createdQuery("SELECT x FROM XincoCoreUserHasXincoCoreGroup x " +
                "WHERE x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreUserId = :xincoCoreUserId and " +
                "x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreGroupId = :xincoCoreGroupId", parameters);
        if (result.size() > 0) {
            XincoCoreUserHasXincoCoreGroup temp = (XincoCoreUserHasXincoCoreGroup) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            return temp;
        } else {
            return null;
        }
    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws XincoException {
        result = pm.namedQuery("XincoCoreUserHasXincoCoreGroup.findByStatusNumber", parameters);
        if (result.size() > 0) {
            XincoCoreUserHasXincoCoreGroup temp[] = new XincoCoreUserHasXincoCoreGroup[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreUserHasXincoCoreGroup) result.get(0);
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
        XincoCoreUserHasXincoCoreGroup temp, newValue = new XincoCoreUserHasXincoCoreGroup();
        temp = (XincoCoreUserHasXincoCoreGroup) value;
        if (!value.isCreated()) {
            newValue.setXincoCoreUserHasXincoCoreGroupPK(temp.getXincoCoreUserHasXincoCoreGroupPK());
            newValue.setRecordId(temp.getRecordId());
            newValue.setStatusNumber(temp.getStatusNumber());
            newValue.setCreated(temp.isCreated());
            newValue.setChangerID(temp.getChangerID());
            newValue.setTransactionTime(getTransactionTime());
            pm.persist(newValue, false, true);
        }
        return newValue;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoCoreUserHasXincoCoreGroup val = (XincoCoreUserHasXincoCoreGroup) value;
        XincoCoreUserHasXincoCoreGroupT temp = new XincoCoreUserHasXincoCoreGroupT();
        temp.setRecordId(val.getRecordId());
        temp.setXincoCoreGroupId(val.getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId());
        temp.setXincoCoreUserId(val.getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreUserId());
        temp.setStatusNumber(val.getStatusNumber());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.persist(val, true, false);
        getModifiedRecordDAOObject().saveAuditData();
        pm.commitAndClose();
        return val;
    }

    public void delete(AbstractAuditableObject value) {
        XincoCoreUserHasXincoCoreGroup val = (XincoCoreUserHasXincoCoreGroup) value;
        XincoCoreUserHasXincoCoreGroupT temp = new XincoCoreUserHasXincoCoreGroupT();
        temp.setRecordId(val.getRecordId());
        temp.setXincoCoreGroupId(val.getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId());
        temp.setXincoCoreUserId(val.getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreUserId());
        temp.setStatusNumber(val.getStatusNumber());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.delete(val, false);
        getModifiedRecordDAOObject().saveAuditData();
        pm.commitAndClose();
    }

    @SuppressWarnings("unchecked")
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("xincoCoreUserId", getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreUserId());
        temp.put("xincoCoreGroupId", getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId());
        return temp;
    }

    public int getNewID() {
        //Not used since both keys must exist. This is ignored in the create method.
        return -1;
    }

    public boolean write2DB() {
        try {
            if (findById(getParameters())!=null) {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.INFO, "Updating: " + getXincoCoreUserHasXincoCoreGroupPK());
                }
                AuditingDAOHelper.update(this, new XincoCoreUserHasXincoCoreGroup(getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreUserId(),
                        getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId()));
            } else {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.INFO, "Creating: " + getXincoCoreUserHasXincoCoreGroupPK());
                }
                XincoCoreUserHasXincoCoreGroup temp = new XincoCoreUserHasXincoCoreGroup();
                temp.setXincoCoreUserHasXincoCoreGroupPK(getXincoCoreUserHasXincoCoreGroupPK());
                temp.setChangerID(getChangerID());
                temp.setCreated(true);
                temp.setStatusNumber(getStatusNumber());
                temp = (XincoCoreUserHasXincoCoreGroup) AuditingDAOHelper.create(this, temp);
                setXincoCoreUserHasXincoCoreGroupPK(temp.getXincoCoreUserHasXincoCoreGroupPK());
            }
            return true;
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, e);
                }
                return false;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }

    public boolean deleteFromDB() {
        setTransactionTime(DateRange.startingNow());
        try {
            AuditingDAOHelper.delete(this, getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId());
            return true;
        } catch (Throwable e) {
            try {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, e);
                }
                return false;
            } catch (XincoSettingException ex) {
                Logger.getLogger(XincoCoreUserHasXincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }
}
