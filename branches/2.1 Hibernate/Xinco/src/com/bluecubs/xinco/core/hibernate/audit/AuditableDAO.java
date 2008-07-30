package com.bluecubs.xinco.core.hibernate.audit;

import com.bluecubs.xinco.core.hibernate.PersistenceManager;
import com.bluecubs.xinco.core.hibernate.conf.ConfigSingletonServer;
import java.util.HashMap;

/**
 * Interface to be implemented by Data Access Objects that allow data auditing
 * Based on @link net.sf.oness.common.model.auditing.AuditableDAO
 * @author Javier A. Ortiz
 */
public interface AuditableDAO {

    PersistenceManager pm = ConfigSingletonServer.getPersistenceManager();
    @SuppressWarnings("static-access")
    static HashMap parameters = new HashMap();

    /**
     * Find an object by its identifier. This should return only the specified
     * entity, not the associated ones.
     * 
     * This method returns the same object passed as argument.
     * 
     * @param parameters in order to locate the specific record
     * @return the value with that id, may have properties not initialized
     *             if the method call creeates a Exception of any kind
     * @throws Exception 
     */
    public AbstractAuditableObject findById(HashMap parameters) throws Exception;

    /**
     * Find an object and all related entities by its identifier.
     * 
     * @param parameters in order to locate the specific record
     * @return the values with that id and all properties initialized
     *              if the method call creeates a Exception of any kind
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws Exception;

    /**
     * Create a new object, the id value will be ignored
     * 
     * @param value
     * @return the value created (with id, transactionTime and needed properties
     *         initialized)
     */
    public AbstractAuditableObject create(AbstractAuditableObject value);

    /**
     * Update an object
     * 
     * @param value
     *            value to update
     * @return the updated value
     */
    public AbstractAuditableObject update(AbstractAuditableObject value);

    /**
     * Delete an object from the working environment (keeping a copy in the audit trail tables)
     * 
     * @param value
     *            value to delete
     */
    public void delete(AbstractAuditableObject value);

    /**
     * Get all parameters in order to get find this record
     * 
     * @return The key parameters in a HashMap
     */
    @SuppressWarnings("unchecked")
    public HashMap getParameters();

    /**
     * Gets a new id for this DAO object by calling XXIDServer(<correct table name>).getNewTableID();
     * @return New id
     */
    public int getNewID();
}
