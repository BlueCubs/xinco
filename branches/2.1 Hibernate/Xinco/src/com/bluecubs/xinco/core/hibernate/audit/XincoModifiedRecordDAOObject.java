package com.bluecubs.xinco.core.hibernate.audit;

import com.bluecubs.xinco.conf.XincoConfigSingletonServer;
import com.bluecubs.xinco.core.persistence.XincoCoreUserModifiedRecord;
import com.bluecubs.xinco.core.persistence.XincoCoreUserModifiedRecordPK;
import com.bluecubs.xinco.core.server.XincoIDServer;
import com.dreamer.Hibernate.Audit.ModifiedRecordDAOObject;
import com.dreamer.Hibernate.conf.ConfigSingletonServer;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoModifiedRecordDAOObject extends ModifiedRecordDAOObject {

    private static final long serialVersionUID = -6434592092601825645L;

    @Override
    public ConfigSingletonServer getSingleton() {
        if (singleton == null) {
            singleton = XincoConfigSingletonServer.getInstance();
        }
        return singleton;
    }

    @SuppressWarnings("static-access")
    @Override
    public boolean saveAuditData() {
        if(getChangerID()<=0){
            Logger.getLogger(XincoModifiedRecordDAOObject.class.getName()).log(Level.WARNING,
                    "Invalid changer id: "+getChangerID()+". The id was not properly set when deleting.");
            return false;
        }
        try {
            XincoCoreUserModifiedRecord t =
                    new XincoCoreUserModifiedRecord(new XincoCoreUserModifiedRecordPK(getChangerID(),
                    getRecordId()), new Date());
            Logger.getLogger(XincoModifiedRecordDAOObject.class.getName()).log(Level.INFO,
                    "Created XincoCoreUserModifiedRecord: " + t);
            t.setModReason(getModReason());
            //Set atomic to false, this is part of a batch
            return ((XincoConfigSingletonServer) getSingleton()).getPersistenceManager().persist(t,
                    false, false);
        } catch (Exception ex) {
            Logger.getLogger(XincoModifiedRecordDAOObject.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public Integer getRecordId() {
        return new XincoIDServer("xinco_core_user_modified_record").getNewTableID(false);
    }
}
