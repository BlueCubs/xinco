package com.bluecubs.xinco.core.server;

import java.sql.Timestamp;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public interface AuditedObject {

    /**
     * Reason for the change
     *
     * @param reason reason for modification
     */
    public void setModificationReason(String reason);

    /**
     * Get reason of change
     *
     * @return reason of change
     */
    public String getModificationReason();

    /**
     * User id that modified the record
     *
     * @param id modifier id
     */
    public void setModifierId(int id);

    /**
     * Get modifier id
     *
     * @return User id that modified the record
     */
    public int getModifierId();

    /**
     * Get modification date
     *
     * @return modification date
     */
    public Timestamp getModificationTime();

    /**
     * Set modification date
     *
     * @param d Modification date
     */
    public void setModificationTime(Timestamp d);
}
