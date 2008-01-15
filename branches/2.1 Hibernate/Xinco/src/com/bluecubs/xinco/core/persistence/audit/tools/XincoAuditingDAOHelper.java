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
 * Name:            XincoAuditingDAOHelper
 *
 * Description:     Helper for Auditable Data Access Objects. Adapted from 
 *                  AuditingDAOHelper within oness package. 
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
package com.bluecubs.xinco.core.persistence.audit.tools;

import com.bluecubs.xinco.core.persistence.XincoCoreUserModifiedRecord;
import com.bluecubs.xinco.core.persistence.XincoCoreUserModifiedRecordPK;
import com.bluecubs.xinco.core.server.XincoIDServer;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;
import net.sf.oness.common.model.util.CollectionCloneConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.hibernate.mapping.Collection;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Based on @link net.sf.oness.common.model.auditing.AuditingDAOHelper
 * @author Javier A. Ortiz
 */
public class XincoAuditingDAOHelper {

    private static XincoCoreUserModifiedRecord xcumr;
    private static XincoIDServer xis = new XincoIDServer("xinco_core_user_modified_record");

    static {
        ConvertUtils.register(new CollectionCloneConverter(), Collection.class);
    }

    /**
     * Set creation date, code and createdBy before saving
     * 
     * @param dao 
     * @param value 
     * @return Modified object
     * @see net.sf.oness.common.model.dao.XincoAuditableDAO#create(net.sf.oness.common.model.bo.Auditable)
     */
    public static XincoAbstractAuditableObject create(XincoAuditableDAO dao,
            XincoAbstractAuditableObject value) {
        try {
            value.setTransactionTime(DateRange.startingNow());
            value.setCreated(true);
            value.setRecordId(dao.getNewID());
            xcumr = new XincoCoreUserModifiedRecord(new XincoCoreUserModifiedRecordPK(value.getChangerID(), xis.getNewID()));
            xcumr.setModReason("audit.general.create");
            xcumr.setModTime(DateRange.startingNow().getStart().getTime());
            XincoAbstractAuditableObject newValue = dao.create(value);
            newValue.setXincoCoreUserModifiedRecord(xcumr);
            newValue.setReason("audit.general.create");
            newValue.setCreated(false);
            dao.update(newValue);
            return newValue;
        } catch (Throwable e) {
            Logger.getLogger(XincoAuditingDAOHelper.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * Save a new object with createdBy and curent date as creation date. Update
     * deletion date and deletedBy in the old value.
     * 
     * @param dao 
     * @param value
     * @return the value returned by {@link XincoAuditableDAO#create(Auditable)}
     * @throws OptimisticLockingFailureException
     *             if the value has been already updated or deleted
     * 
     * @see net.sf.oness.common.model.dao.XincoAuditableDAO#update(net.sf.oness.common.model.bo.Auditable)
     */
    public static XincoAbstractAuditableObject update(XincoAuditableDAO dao,
            XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        try {
            XincoAbstractAuditableObject oldValue = dao.findById(dao.getParameters());
            DateRange range = oldValue.getTransactionTime();
            if (!range.isOpen()) {
                throw new OptimisticLockingFailureException("Value with the provided parameters has been already updated or deleted. Parameters:" + dao.getParameters());
            }
            range.endNow();
            XincoAbstractAuditableObject newValue = clone(oldValue);
            newValue.setTransactionTime(DateRange.startingOn(range.getEnd()));
            newValue.setReason("audit.general.modified");
            newValue.setChangerID(value.getChangerID());
            xcumr = new XincoCoreUserModifiedRecord(new XincoCoreUserModifiedRecordPK(newValue.getChangerID(), newValue.getRecordId()));
            xcumr.setModReason(newValue.getReason());
            xcumr.setModTime(range.getEnd().getTime());
            newValue.setModified(true);
            newValue.setCreated(false);
            newValue.setXincoCoreUserModifiedRecord(xcumr);
            newValue.setChangerID(value.getChangerID());
            return dao.create(newValue);
        } catch (Throwable e) {
            Logger.getLogger(XincoAuditingDAOHelper.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    private static XincoAbstractAuditableObject clone(
            XincoAbstractAuditableObject value) throws OptimisticLockingFailureException {
        XincoAbstractAuditableObject newValue = null;
        try {
            newValue = (XincoAbstractAuditableObject) BeanUtils.cloneBean(value);
        } catch (IllegalAccessException e) {
            throw new InvalidDataAccessApiUsageException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new InvalidDataAccessApiUsageException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new InvalidDataAccessApiUsageException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new InvalidDataAccessApiUsageException(e.getMessage(), e);
        }
        return newValue;
    }

    /**
     * Set deletion date and deletedBy
     * 
     * @param dao 
     * @param id 
     * @throws org.springframework.dao.OptimisticLockingFailureException
     *             if the value has been already updated or deleted
     * @see net.sf.oness.common.model.dao.XincoAuditableDAO#delete(java.io.Serializable)
     */
    public static void delete(XincoAuditableDAO dao, Serializable id) throws OptimisticLockingFailureException {
        try {
            XincoAbstractAuditableObject oldValue = dao.findById(dao.getParameters());
            DateRange range = oldValue.getTransactionTime();
            if (!range.isOpen()) {
                throw new OptimisticLockingFailureException("Value with id " + id + " has been already updated or deleted");
            }
            range.endNow();
            oldValue.setDeleted(true);
            oldValue.setReason("audit.general.delete");
            xcumr = new XincoCoreUserModifiedRecord(new XincoCoreUserModifiedRecordPK(oldValue.getChangerID(), xis.getNewID()));
            xcumr.setModReason(oldValue.getReason());
            xcumr.setModTime(range.getEnd().getTime());
            oldValue.setXincoCoreUserModifiedRecord(xcumr);
            dao.delete(oldValue);
        } catch (Throwable e) {
            Logger.getLogger(XincoAuditingDAOHelper.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
