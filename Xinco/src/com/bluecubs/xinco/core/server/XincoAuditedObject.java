package com.bluecubs.xinco.core.server;

import java.sql.Timestamp;

/**
 *
 * @author Javier A. Ortiz Bultr�n <javier.ortiz.78@gmail.com>
 */
public class XincoAuditedObject implements AuditedObject {

    private String reason;
    //Default to Admin
    private int modifierId=1;
    private Timestamp modDate;

    @Override
    public void setModificationReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String getModificationReason() {
        return reason;
    }

    @Override
    public void setModifierId(int id) {
        this.modifierId=id;
    }

    @Override
    public int getModifierId() {
        return modifierId;
    }

    @Override
    public Timestamp getModificationTime() {
        return modDate;
    }

    @Override
    public void setModificationTime(Timestamp d) {
        this.modDate=d;
    }
}