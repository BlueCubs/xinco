/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance.audit;

import com.bluecubs.xinco.core.persistance.XincoCoreUserModifiedRecord;
import com.bluecubs.xinco.core.server.persistance.XincoPersistanceManager;
import com.bluecubs.xinco.core.server.persistance.XincoIDServer;
import net.sf.oness.common.model.temporal.DateRange;
import net.sf.oness.common.all.BaseObject;

/**
 *
 * @author Javier A. Ortiz
 */
public abstract class XincoAbstractAuditableObject extends BaseObject implements XincoAuditable {

    private DateRange transactionTime = DateRange.startingOn(null);
    private boolean created=false,  deleted=false,  modified=false;
    private int changerID,  id=0;
    private String reason;
    private XincoCoreUserModifiedRecord xcumr;

    /**
     * @see net.sf.oness.common.model.auditing.Auditable#getRecordId()
     */
    public Integer getRecordId() {
        if(id==0){
            XincoIDServer xis= new XincoIDServer("xinco_core_user_modified_record");
            this.id=xis.getNewID();
        }
        return this.id;
    }
    
    /**
     * @see net.sf.oness.common.model.auditing.Auditable#setRecordId()
     */
    public void setRecordId(Integer id) {
        this.id=id;
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
    public void setReason(String reason){
        this.reason=reason;
    }
    
    /**
     * @see com.bluecubs.xinco.core.server.persistance.audit.XincoAuditable#getReason()
     */
    public String getReason(){
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
    public boolean saveAuditData(XincoPersistanceManager pm){
        pm.persist(getXincoCoreUserModifiedRecord(), false, false);
        return pm.isTransactionOk();
    }
}
