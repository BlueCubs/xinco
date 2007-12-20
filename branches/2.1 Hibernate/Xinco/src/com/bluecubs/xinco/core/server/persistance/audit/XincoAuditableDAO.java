/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance.audit;

import java.util.HashMap;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Interface to be implemented by Data Access Objects that allow data auditing
 * 
 * @author Javier A. Ortiz
 */
public interface XincoAuditableDAO {

    /**
     * Find an object by its identifier. This should return only the specified
     * entity, not the associated ones.
     * 
     * This method returns the same object passed as argument.
     * 
     * @param parameters in order to locate the specific record
     * @return the value with that id, may have properties not initialized
     * @throws org.springframework.dao.DataRetrievalFailureException
     *             if an object with that id doesn't exist
     */
    public XincoAbstractAuditableObject findById(HashMap parameters)throws DataRetrievalFailureException;
    
    /**
     * Find an object and all related entities by its identifier.
     * 
     * @param parameters in order to locate the specific record
     * @return the value with that id and all properties initialized
     * @throws org.springframework.dao.DataRetrievalFailureException
     *             if an object with that id doesn't exist
     */
    public XincoAbstractAuditableObject findWithDetails(HashMap parameters) throws DataRetrievalFailureException;

    /**
     * Create a new object, the id value will be ignored
     * 
     * @param value
     * @return the value created (with id, transactionTime and needed properties
     *         initialized)
     */
    public XincoAbstractAuditableObject create(XincoAbstractAuditableObject value);

    /**
     * Update an object
     * 
     * @param value
     *            value to update
     * @return the updated value
     * @throws org.springframework.dao.OptimisticLockingFailureException
     *             if the value has been already updated or deleted
     */
    public XincoAbstractAuditableObject update(XincoAbstractAuditableObject value)throws OptimisticLockingFailureException;
    
    /**
     * Delete an object from the working environment (keeping a copy in the audit trail tables)
     * 
     * @param value
     *            value to delete
     * @throws org.springframework.dao.OptimisticLockingFailureException
     *             if the value has been already updated or deleted
     */
    public void delete(XincoAbstractAuditableObject value)throws OptimisticLockingFailureException;
    
    /**
     * Get all parameters in order to get find this record
     * 
     * @return The parameters in a HashMap
     */
    public HashMap getParameters();
}
