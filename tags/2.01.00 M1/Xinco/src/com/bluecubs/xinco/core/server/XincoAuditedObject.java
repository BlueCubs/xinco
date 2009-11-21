package com.bluecubs.xinco.core.server;

import java.sql.Timestamp;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoAuditedObject implements AuditedObject {

    private String reason;
    //Default to Admin
    private int modifierId=1;
    private Timestamp modDate;

    public void setModificationReason(String reason) {
        this.reason = reason;
    }

    public String getModificationReason() {
        return reason;
    }

    public void setModifierId(int id) {
        this.modifierId=id;
    }

    public int getModifierId() {
        return modifierId;
    }

    public Timestamp getModificationTime() {
        return modDate;
    }

    public void setModificationTime(Timestamp d) {
        this.modDate=d;
    }
}
