package com.bluecubs.xinco.core.hibernate.audit;

import net.sf.oness.common.model.temporal.DateRange;

/**
 * Based on @link net.sf.oness.common.model.auditing.Auditable
 * @author Javier A. Ortiz
 */
public interface Auditable {

    /**
     * The identifier of each version of each object
     * 
     * @return
     */
    public Integer getRecordId();

    /**
     * 
     * @param id
     */
    public void setRecordId(Integer id);

    /**
     * @param reason 
     */
    public void setReason(String reason);

    /**
     * @return Reason for auditable event
     */
    public String getReason();

    /**
     * 
     * @param transactionTime
     */
    public void setTransactionTime(DateRange transactionTime);

    /**
     * Range of time when the data is entered into the system
     * 
     * @return
     */
    public DateRange getTransactionTime();

    /**
     * Set if the object was created
     * 
     * @param set 
     */
    public void setCreated(boolean set);

    /**
     * Get if the object was created
     * 
     * @return Returns if the object was created.
     */
    public boolean isCreated();

    /**
     * Set if the object was modified
     * 
     * @param set 
     */
    public void setModified(boolean set);

    /**
     * Get if the object was modified
     * 
     * @return Returns if the object was modified.
     */
    public boolean isModified();

    /**
     * Set if the object was deleted
     * 
     * @param set 
     */
    public void setDeleted(boolean set);

    /**
     * Get if the object was deleted
     * 
     * @return Returns if the object was deleted.
     */
    public boolean isDeleted();

    /**
     * Cloned objects can't share collection references.
     * 
     * @return 
     * @see java.lang.Object#clone()
     */
    public Object clone();

    /**
     * Get the changer's user id
     * 
     * @return the changer's user id
     */
    public int getChangerID();

    /**
     * Set the changer's user id
     * 
     * @param changerID
     */
    public void setChangerID(int changerID);
}
