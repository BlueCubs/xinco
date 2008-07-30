package com.bluecubs.xinco.core.hibernate.audit;

/**
 *
 * @author Javier A. Ortiz
 */
public interface PersistenceServerObject {

    public PersistenceServerUtils util = new PersistenceServerUtils();

    /**
     * Writes a record on the DB.
     * @return True if write was successful
     */
    public abstract boolean write2DB();

    /**
     * Deletes a record on the DB.
     * @return True if deletion was successful
     */
    public abstract boolean deleteFromDB();
}
