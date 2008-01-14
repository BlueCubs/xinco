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
 * Name:            XincoIDServer
 *
 * Description:     Table ID's
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

import com.bluecubs.xinco.core.server.persistance.XincoPersistanceManager;
import com.bluecubs.xinco.core.server.persistance.XincoSettingServer;
import java.util.HashMap;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Interface to be implemented by Data Access Objects that allow data auditing
 * Based on @link net.sf.oness.common.model.auditing.AuditableDAO
 * @author Javier A. Ortiz
 */
public interface XincoAuditableDAO {

    XincoPersistanceManager pm = new XincoPersistanceManager();
    static HashMap parameters= new HashMap();

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
    public XincoAbstractAuditableObject findById(HashMap parameters) throws DataRetrievalFailureException;

    /**
     * Find an object and all related entities by its identifier.
     * 
     * @param parameters in order to locate the specific record
     * @return the values with that id and all properties initialized
     * @throws org.springframework.dao.DataRetrievalFailureException
     *             if an object with that id doesn't exist
     */
    @SuppressWarnings("unchecked")
    public XincoAbstractAuditableObject[] findWithDetails(HashMap parameters) throws DataRetrievalFailureException;

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
    public XincoAbstractAuditableObject update(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException;

    /**
     * Delete an object from the working environment (keeping a copy in the audit trail tables)
     * 
     * @param value
     *            value to delete
     * @throws org.springframework.dao.OptimisticLockingFailureException
     *             if the value has been already updated or deleted
     */
    public void delete(XincoAbstractAuditableObject value) throws OptimisticLockingFailureException;

    /**
     * Get all parameters in order to get find this record
     * 
     * @return The key parameters in a HashMap
     */
    @SuppressWarnings("unchecked")
    public HashMap getParameters();

    /**
     * Gets a new id for this DAO object by calling XincoIDServer(<correct table name>).getNewID();
     * @return New id
     */
    public int getNewID();
}
