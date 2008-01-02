/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance.audit;

import com.bluecubs.xinco.core.persistance.XincoCoreUserModifiedRecord;
import com.bluecubs.xinco.core.persistance.XincoCoreUserModifiedRecordPK;
import com.bluecubs.xinco.core.server.persistance.XincoIDServer;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import net.sf.oness.common.model.temporal.DateRange;
import net.sf.oness.common.model.util.CollectionCloneConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.hibernate.mapping.Collection;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 *
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
    }
}
