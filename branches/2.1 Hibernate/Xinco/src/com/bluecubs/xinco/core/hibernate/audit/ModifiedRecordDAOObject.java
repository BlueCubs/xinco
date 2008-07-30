package com.bluecubs.xinco.core.hibernate.audit;

import com.bluecubs.xinco.core.hibernate.conf.ConfigSingletonServer;
import java.util.Date;

/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
public class ModifiedRecordDAOObject extends AbstractAuditableObject {

    private String modReason;
    private Date modTime;

    /**
     * To be implemented for each case since it might vary
     * @return True if saved successfully
     * @see com.bluecubs.xinco.core.hibernate.audit.Auditable#saveAuditData()
     */
    @SuppressWarnings("static-access")
    public boolean saveAuditData() {
        ConfigSingletonServer singleton = ConfigSingletonServer.getInstance();
        return singleton.getPersistenceManager().persist(this, false, false);
    }

    public Integer getRecordId() {
        throw new UnsupportedOperationException("Not to be implemented.");
    }

    public String getModReason() {
        return modReason;
    }

    public Date getModTime() {
        return modTime;
    }

    public void setModReason(String modReason) {
        this.modReason = modReason;
    }

    public void setModTime(Date modTime) {
        this.modTime = modTime;
    }
}
