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
 *                  Auditable within oness package. 
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

import com.bluecubs.xinco.core.persistance.XincoCoreUserModifiedRecord;
import com.bluecubs.xinco.core.server.persistance.XincoPersistanceManager;
import net.sf.oness.common.model.temporal.DateRange;

/**
 * Based on @link net.sf.oness.common.model.auditing.Auditable
 * @author Javier A. Ortiz
 */
public interface XincoAuditable {

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
    
    /**
     * Gets the persistance object where the audit details are to be stored
     * 
     * @return Current object's XincoCoreUserModifiedRecord
     */
    public XincoCoreUserModifiedRecord getXincoCoreUserModifiedRecord();
    
    /**
     * Sets the persistance object where the audit details are to be stored
     * 
     * @param xcumr New XincoCoreUserModifiedRecord object
     */
    public void setXincoCoreUserModifiedRecord(XincoCoreUserModifiedRecord xcumr);
    
    /**
     * Saves the XincoCoreUserModifiedRecord and the old data to the database
     * 
     * @param pm Persistancemanager
     * @return If operation was successful
     */
    public boolean saveAuditData(XincoPersistanceManager pm);
}
