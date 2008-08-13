package com.bluecubs.xinco.core.hibernate.audit;

import com.bluecubs.xinco.core.server.*;
import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
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
    public static XincoDBManager getPm() {
        return pm;
    }

    /**
     * @param aPm the pm to set
     */
    public static void setPm(XincoDBManager aPm) {
        pm = aPm;
    }

    public Integer getRecordId() {
        if (id <= 0) {
            XincoIDServer pis = new XincoIDServer("xinco_core_user_modified_record");
            id = pis.getNewID(true);
        }
        return id;
    }

    public static XincoModifiedRecordDAOObject getModifiedRecordDAOObject() {
        mrdo = new XincoModifiedRecordDAOObject();
        mrdo.setSingleton(XincoConfigSingletonServer.getInstance());
        Logger.getLogger(XincoAbstractAuditableObject.class.getName()).
                log(Level.INFO, "Setting singleton from XincoAbstractAuditableObject");
        return (XincoModifiedRecordDAOObject) mrdo;
    }
}
