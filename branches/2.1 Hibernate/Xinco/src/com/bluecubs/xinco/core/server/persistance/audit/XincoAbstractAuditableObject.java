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
 * Name:            XincoAuditable
 *
 * Description:     Helper for Auditable Data Access Objects. Adapted from 
 *                  AbstractAuditableObject within oness package. 
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
package com.bluecubs.xinco.core.server.persistance.audit;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistance.XincoCoreUserModifiedRecord;
import com.bluecubs.xinco.core.server.persistance.XincoCoreACEServer;
import com.bluecubs.xinco.core.server.persistance.XincoPersistanceManager;
import com.bluecubs.xinco.core.server.persistance.XincoIDServer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;
import net.sf.oness.common.all.BaseObject;

/**
 * Based on @link net.sf.oness.common.model.auditing.AbstractAuditableObject
 * @author Javier A. Ortiz
 */
public abstract class XincoAbstractAuditableObject extends BaseObject implements XincoAuditable {

    private DateRange transactionTime = DateRange.startingNow();
    private boolean created = false,  deleted = false,  modified = false;
    private int changerID=0,  id = 0;
    private String reason;
    private XincoCoreUserModifiedRecord xcumr;
    private static XincoPersistanceManager pm;

    /**
     * @see net.sf.oness.common.model.auditing.Auditable#getRecordId()
     */
    public Integer getRecordId() {
        if (id == 0) {
            XincoIDServer xis = new XincoIDServer("xinco_core_user_modified_record");
            id = xis.getNewTableID();
        }
        return id;
    }

    /**
     * @see net.sf.oness.common.model.auditing.Auditable#setRecordId()
     */
    public void setRecordId(Integer id) {
        this.id = id;
    }

    public void setTransactionTime(DateRange transactionTime) {
        this.transactionTime = transactionTime;
    }

    /**
     * @see net.sf.oness.common.model.auditing.Auditable#getTransactionTime()
     */
    public DateRange getTransactionTime() {
        return transactionTime;
    }

    public void setCreated(boolean set) {
        this.created = set;
    }

    /**
     * @see net.sf.oness.common.model.auditing.Auditable#isCreated()
     */
    public boolean isCreated() {
        return created;
    }

    public void setDeleted(boolean set) {
        this.deleted = set;
    }

    /**
     * @see net.sf.oness.common.model.auditing.Auditable#isDeleted()
     */
    public boolean isDeleted() {
        return deleted;
    }

    public void setModified(boolean set) {
        this.modified = set;
    }

    /**
     * @see net.sf.oness.common.model.auditing.Auditable#isModified()
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * @see com.bluecubs.xinco.core.server.persistance.audit.XincoAuditable#getChangerID()
     */
    public int getChangerID() {
        return changerID;
    }

    /**
     * @see com.bluecubs.xinco.core.server.persistance.audit.XincoAuditable#setChangerID()
     */
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }

    /**
     * @see com.bluecubs.xinco.core.server.persistance.audit.XincoAuditable#setReason()
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @see com.bluecubs.xinco.core.server.persistance.audit.XincoAuditable#getReason()
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * @see com.bluecubs.xinco.core.server.persistance.audit.XincoAuditable#getXincoCoreUserModifiedRecord()
     */
    public XincoCoreUserModifiedRecord getXincoCoreUserModifiedRecord() {
        return xcumr;
    }

    /**
     * @see com.bluecubs.xinco.core.server.persistance.audit.XincoAuditable#setXincoCoreUserModifiedRecord()
     */
    public void setXincoCoreUserModifiedRecord(XincoCoreUserModifiedRecord xcumr) {
        this.xcumr = xcumr;
    }

    /**
     * @see com.bluecubs.xinco.core.server.persistance.audit.XincoAuditable#saveAuditData()
     */
    public boolean saveAuditData(XincoPersistanceManager pm) {
        pm.persist(getXincoCoreUserModifiedRecord(), false, false);
        return pm.isTransactionOk();
    }
    
    /**
     * Remove from DB (static)
     * @param o XincoAbstractAuditableObject
     * @param userID User ID
     * @return int
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public static boolean removeFromDB(XincoAbstractAuditableObject o, int userID) throws XincoException {
        try {
            o.setChangerID(userID);
            pm.delete(o, true);
        } catch (Throwable e) {
                Logger.getLogger(XincoAbstractAuditableObject.class.getName()).log(Level.SEVERE, null, e);
                throw new XincoException();
        }
        return true;
    }
}
