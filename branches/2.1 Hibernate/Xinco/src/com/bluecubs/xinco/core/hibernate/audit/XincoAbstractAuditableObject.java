package com.bluecubs.xinco.core.hibernate.audit;

import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.core.server.XincoIDServer;
import com.dreamer.hibernate.audit.AbstractAuditableObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public class XincoAbstractAuditableObject extends AbstractAuditableObject {

    private static final long serialVersionUID = 3791544578403758791L;
    private static XincoDBManager pm = (XincoDBManager) XincoConfigSingletonServer.getPersistenceManager();

    /**
     * @return the pm
     */
    public static XincoDBManager getPersistenceManager() {
        return pm;
    }

    /**
     * @param aPm the pm to set
     */
    public static void setPm(XincoDBManager aPm) {
        pm = aPm;
    }

    public Integer getRecordId(boolean atomic) {
        if (id <= 0) {
            XincoIDServer pis = new XincoIDServer("xinco_core_user_modified_record");
            id = pis.getNewID(atomic);
        }
        return id;
    }
    
    @Override
    public Integer getRecordId() {
        return getRecordId(true);
    }

    public static XincoModifiedRecordDAOObject getModifiedRecordDAOObject() {
        mrdo = new XincoModifiedRecordDAOObject();
        mrdo.setSingleton(XincoConfigSingletonServer.getInstance());
        Logger.getLogger(XincoAbstractAuditableObject.class.getName()).
                log(Level.INFO, "Setting singleton from XincoAbstractAuditableObject");
        return (XincoModifiedRecordDAOObject) mrdo;
    }

    @Override
    public Object clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
