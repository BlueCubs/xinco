package com.bluecubs.xinco.core.hibernate.audit;

import com.bluecubs.xinco.core.hibernate.PersistenceManager;
import com.bluecubs.xinco.core.hibernate.conf.ConfigSingletonServer;
import net.sf.oness.common.model.temporal.DateRange;
import net.sf.oness.common.all.BaseObject;

/**
 * Based on @link net.sf.oness.common.model.auditing.AbstractAuditableObject
 * @author Javier A. Ortiz
 */
public abstract class AbstractAuditableObject extends BaseObject implements Auditable {

    private static final long serialVersionUID = -7137977370367868111L;
    private DateRange transactionTime = DateRange.startingNow();
    private boolean created = false,  deleted = false,  modified = false;
    private int changerID = 0;
    protected int id = 0;
    private String reason;
    private static PersistenceManager pm = ConfigSingletonServer.getPersistenceManager();
    private static ModifiedRecordDAOObject mrdo;

    /**
     * @param id 
     * @see net.sf.oness.common.model.auditing.Auditable#setRecordId()
     */
    public void setRecordId(Integer id) {
        this.id = id;
    }

    public void setTransactionTime(DateRange transactionTime) {
        this.transactionTime = transactionTime;
    }

    /**
     * @return 
     * @see net.sf.oness.common.model.auditing.Auditable#getTransactionTime()
     */
    public DateRange getTransactionTime() {
        return transactionTime;
    }

    public void setCreated(boolean set) {
        this.created = set;
    }

    /**
     * @return 
     * @see net.sf.oness.common.model.auditing.Auditable#isCreated()
     */
    public boolean isCreated() {
        return created;
    }

    public void setDeleted(boolean set) {
        this.deleted = set;
    }

    /**
     * @return 
     * @see net.sf.oness.common.model.auditing.Auditable#isDeleted()
     */
    public boolean isDeleted() {
        return deleted;
    }

    public void setModified(boolean set) {
        this.modified = set;
    }

    /**
     * @return 
     * @see net.sf.oness.common.model.auditing.Auditable#isModified()
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * @return 
     * @see com.bluecubs.xinco.core.hibernate.audit.Auditable#getChangerID()
     */
    public int getChangerID() {
        return changerID;
    }

    /**
     * @param changerID 
     * @see com.bluecubs.xinco.core.hibernate.audit.Auditable#setChangerID()
     */
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }

    /**
     * @param reason 
     * @see com.bluecubs.xinco.core.hibernate.audit.Auditable#setReason()
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return 
     * @see com.bluecubs.xinco.core.hibernate.audit.Auditable#getReason()
     */
    public String getReason() {
        return this.reason;
    }

    public static ModifiedRecordDAOObject getModifiedRecordDAOObject() {
        return mrdo;
    }

    public static void setModifiedRecordDAOObject(ModifiedRecordDAOObject aMrdo) {
        mrdo = aMrdo;
    }
}
