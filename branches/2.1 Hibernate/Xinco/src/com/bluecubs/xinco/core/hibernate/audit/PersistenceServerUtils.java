package com.bluecubs.xinco.core.hibernate.audit;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public class PersistenceServerUtils {
    
    public PersistenceServerUtils(){
        
    }
    
     /**
     * Remove from DB (static)
     * @param o AbstractAuditableObject
     * @param userID User ID
     * @return int
     * @throws java.lang.Exception 
     */
    public static boolean removeFromDB(AuditableDAO o, int userID) throws Exception {
        try {
            AuditingDAOHelper.delete(o, userID);
        } catch (Throwable e) {
            Logger.getLogger(AbstractAuditableObject.class.getName()).log(Level.SEVERE, null, e);
            throw new Exception();
        }
        return true;
    }
}
