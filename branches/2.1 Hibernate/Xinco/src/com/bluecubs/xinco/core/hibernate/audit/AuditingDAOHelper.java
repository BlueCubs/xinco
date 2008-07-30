package com.bluecubs.xinco.core.hibernate.audit;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;
import net.sf.oness.common.model.util.CollectionCloneConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.hibernate.mapping.Collection;

/**
 * Based on @link net.sf.oness.common.model.auditing.AuditingDAOHelper
 * @author Javier A. Ortiz
 */
public class AuditingDAOHelper {

    static {
        ConvertUtils.register(new CollectionCloneConverter(), Collection.class);
    }

    /**
     * Set creation date, code and createdBy before saving
     * 
     * @param dao 
     * @param value 
     * @return Modified object
     * @see net.sf.oness.common.model.dao.AuditableDAO#create(net.sf.oness.common.model.bo.Auditable)
     */
    @SuppressWarnings("static-access")
    public static AbstractAuditableObject create(AuditableDAO dao,
            AbstractAuditableObject value) {
        try {
            value.setTransactionTime(DateRange.startingNow());
            value.setCreated(true);
            value.setRecordId(dao.getNewID());
            AbstractAuditableObject newValue = dao.create(value);
            newValue.setModifiedRecordDAOObject(new ModifiedRecordDAOObject());
            newValue.getModifiedRecordDAOObject().setModReason("audit.general.update");
            newValue.getModifiedRecordDAOObject().setModTime(DateRange.startingNow().getStart().getTime());
            newValue.setReason("audit.general.create");
            newValue.setCreated(false);
            dao.update(newValue);
            return newValue;
        } catch (Throwable e) {
            Logger.getLogger(AuditingDAOHelper.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * Save a new object with createdBy and curent date as creation date. Update
     * deletion date and deletedBy in the old value.
     * 
     * @param dao 
     * @param value
     * @return the value returned by {@link AuditableDAO#create(Auditable)}
     * @throws java.lang.Exception 
     *             if the value has been already updated or delete.
     * @see net.sf.oness.common.model.dao.AuditableDAO#update(net.sf.oness.common.model.bo.Auditable)
     */
    @SuppressWarnings("static-access")
    public static AbstractAuditableObject update(AuditableDAO dao,
            AbstractAuditableObject value) throws Exception {
        try {
            AbstractAuditableObject oldValue = dao.findById(dao.getParameters());
            DateRange range = oldValue.getTransactionTime();
            if (!range.isOpen()) {
                throw new Exception("Value with the provided parameters has been already updated or deleted. Parameters:" + dao.getParameters());
            }
            range.endNow();
            AbstractAuditableObject newValue = clone(oldValue);
            newValue.setModifiedRecordDAOObject(new ModifiedRecordDAOObject());
            newValue.getModifiedRecordDAOObject().setModReason("audit.general.update");
            newValue.getModifiedRecordDAOObject().setModTime(DateRange.startingNow().getStart().getTime());
            newValue.setTransactionTime(DateRange.startingOn(range.getEnd()));
            newValue.setReason("audit.general.modified");
            newValue.setChangerID(value.getChangerID());
            newValue.setModified(true);
            newValue.setCreated(false);
            newValue.setChangerID(value.getChangerID());
            return dao.create(newValue);
        } catch (Throwable e) {
            Logger.getLogger(AuditingDAOHelper.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    private static AbstractAuditableObject clone(
            AbstractAuditableObject value) throws Exception {
        AbstractAuditableObject newValue = null;
        try {
            newValue = (AbstractAuditableObject) BeanUtils.cloneBean(value);
        } catch (IllegalAccessException e) {
            throw new Exception(e.getMessage());
        } catch (InvocationTargetException e) {
            throw new Exception(e.getMessage());
        } catch (InstantiationException e) {
            throw new Exception(e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new Exception(e.getMessage());
        }
        return newValue;
    }

    /**
     * Set deletion date and deletedBy
     * 
     * @param dao 
     * @param id 
     * @throws Exception
     *             if the value has been already updated or deleted
     * @see net.sf.oness.common.model.dao.AuditableDAO#delete(java.io.Serializable)
     */
    @SuppressWarnings("static-access")
    public static void delete(AuditableDAO dao, Serializable id) throws Exception {
        try {
            AbstractAuditableObject oldValue = dao.findById(dao.getParameters());
            DateRange range = oldValue.getTransactionTime();
            if (!range.isOpen()) {
                throw new Exception("Value with id " + id + " has been already updated or deleted");
            }
            range.endNow();
            oldValue.setModifiedRecordDAOObject(new ModifiedRecordDAOObject());
            oldValue.getModifiedRecordDAOObject().setModReason("audit.general.delete");
            oldValue.getModifiedRecordDAOObject().setModTime(DateRange.startingNow().getStart().getTime());
            oldValue.setDeleted(true);
            oldValue.setReason("audit.general.delete");
            dao.delete(oldValue);
        } catch (Throwable e) {
            Logger.getLogger(AuditingDAOHelper.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
