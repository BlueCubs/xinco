/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.XincoException;

/**
 *
 * @author Javier A. Ortiz
 */
public interface XincoPersistanceServerObject {
    /**
     * Writes a record on the DB.
     * @return True if write was successful
     * @throws com.bluecubs.xinco.core.XincoException 
     */
    public boolean write2DB() throws XincoException;
    
    /**
     * Deletes a record on the DB.
     * @return True if deletion was successful
     * @throws com.bluecubs.xinco.core.XincoException 
     */
    public boolean deleteFromDB() throws XincoException;

}
