/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

/**
 *
 * @author Javier A. Ortiz
 */
public interface XincoPersistanceServerObject {

    /**
     * Writes a record on the DB.
     * @return True if write was successful
     */
    public boolean write2DB();

    /**
     * Deletes a record on the DB.
     * @return True if deletion was successful
     */
    public boolean deleteFromDB();
}
