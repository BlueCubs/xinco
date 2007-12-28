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
 * Name:            XincoCoreGroupServer
 *
 * Description:     group
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
import com.bluecubs.xinco.core.persistance.XincoCoreGroup;
import com.bluecubs.xinco.core.persistance.audit.XincoCoreGroupT;
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
 * Create group object for data structures
 * @author Alexander Manes
 */
public class XincoCoreGroupServer extends XincoCoreGroup implements XincoAuditableDAO, XincoPersistanceServerObject {

    private static XincoPersistanceManager pm = new XincoPersistanceManager();
    private static List result;

    /**
     * Create group object for data structures
     * @param id
     * @throws com.bluecubs.xinco.core.XincoException 
     */
    @SuppressWarnings("unchecked")
    public XincoCoreGroupServer(int id) throws XincoException {
        try {
            HashMap parameters = new HashMap();
            parameters.put("id", id);
            result = pm.namedQuery("XincoCoreGroup.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                XincoCoreGroup temp = (XincoCoreGroup) result.get(0);
                setId(temp.getId());
                setDesignation(temp.getDesignation());
                setStatusNumber(temp.getStatusNumber());
            } else {
                throw new XincoException();
            }
        } catch (Throwable e) {
            throw new XincoException(e.getMessage());
        }
    }

    /**
     * Create group object for data structures
     * @param id
     * @param designation
     * @param status
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreGroupServer(int id, String designation, int status) throws XincoException {
        setId(id);
        setDesignation(designation);
        setStatusNumber(status);
    }

    /**
     * Persist object
     * @return 
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public boolean write2DB() throws XincoException {
        try {
            if (getId() > 0) {
                XincoAuditingDAOHelper.update(this, new XincoCoreGroup(getId()));
            } else {
                XincoCoreGroup temp = new XincoCoreGroup();
                temp.setId(getNewID());
                temp.setChangerID(getChangerID());
                temp.setCreated(isCreated());
                temp.setDesignation(getDesignation());
                temp.setStatusNumber(getStatusNumber());
                XincoAuditingDAOHelper.create(this, temp);
            }
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
        return true;
    }

    /**
     * Create complete list of groups
     * @return A vector containing all Core Groups
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreGroups() {
        Vector coreGroups = new Vector();
        try {
            result = pm.executeQuery("select p from XincoCoreGroup p order by p.designation");
            while (!result.isEmpty()) {
                coreGroups.add((XincoCoreGroup) result.get(0));
                result.remove(0);
            }
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreGroupServer.class.getName()).log(Level.WARNING, null, e);
            }
            coreGroups.removeAllElements();
        }
        return coreGroups;
    }

    public boolean deleteFromDB() throws XincoException {
        setTransactionTime(DateRange.startingNow());
        try {
            XincoAuditingDAOHelper.delete(this, getId());
            return true;
        } catch (Throwable e) {
            if (new XincoSettingServer("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    public XincoAbstractAuditableObject findById(HashMap parameters) throws DataRetrievalFailureException {
        result = pm.namedQuery("XincoCoreGroup.findById", parameters);
        XincoCoreGroup temp = (XincoCoreGroup) result.get(0);
        temp.setTransactionTime(getTransactionTime());
        temp.setChangerID(getChangerID());
        return temp;
    }

    public XincoAbstractAuditableObject [] findWithDetails(HashMap parameters) throws DataRetrievalFailureException {
        result = pm.namedQuery("XincoCoreGroup.findByDesignation", parameters);
        XincoCoreGroup temp[] = new XincoCoreGroup[1];
        temp[0] = (XincoCoreGroup) result.get(0);
        temp[0].setTransactionTime(getTransactionTime());
        return temp;
    }

    public XincoAbstractAuditableObject create(XincoAbstractAuditableObject value) {
        XincoCoreGroup temp, newValue = new XincoCoreGroup();
        boolean exists = false;
        temp = (XincoCoreGroup) value;
        if (!value.isCreated()) {
            newValue.setId(temp.getId());
            newValue.setRecordId(temp.getRecordId());
        } else {
            newValue.setId(new XincoIDServer("xinco_setting").getNewID());
        }
        newValue.setId(temp.getId());
        newValue.setDesignation(temp.getDesignation());
        newValue.setStatusNumber(temp.getStatusNumber());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        if (!value.isCreated()) {
            if (((XincoCoreGroup) value).getId() != 0) {
                //An object for updating
                exists = true;
            } else {
                //A new object
                exists = false;
            }
        }
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, exists, true);
        return newValue;
    }

    public XincoAbstractAuditableObject update(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoCoreGroup val = (XincoCoreGroup) value;
        XincoCoreGroupT temp = new XincoCoreGroupT();
        temp.setRecordId(val.getRecordId());
        temp.setDesignation(val.getDesignation());
        temp.setStatusNumber(val.getStatusNumber());
        if (!value.isCreated()) {
            temp.setId(val.getId());
        } else {
            temp.setId(val.getRecordId());
        }
        temp.setCreated(val.isCreated());
        temp.setChangerID(val.getChangerID());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.persist(val, true, false);
        val.saveAuditData(pm);
        pm.commitAndClose();
        return val;
    }

    public void delete(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoCoreGroup val = (XincoCoreGroup) value;
        XincoCoreGroupT temp = new XincoCoreGroupT();
        temp.setRecordId(val.getRecordId());
        temp.setDesignation(val.getDesignation());
        temp.setStatusNumber(val.getStatusNumber());
        temp.setId(val.getId());
        temp.setCreated(val.isCreated());
        temp.setChangerID(val.getChangerID());
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
        return new XincoIDServer("xinco_core_group").getNewID();
    }
}
