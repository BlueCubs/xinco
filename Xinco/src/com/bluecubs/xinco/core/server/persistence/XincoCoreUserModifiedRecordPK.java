package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Embeddable
public class XincoCoreUserModifiedRecordPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private int id;
    @Basic(optional = false)
    @Column(name = "record_id", nullable = false)
    private int recordId;

    public XincoCoreUserModifiedRecordPK() {
    }

    public XincoCoreUserModifiedRecordPK(int id, int recordId) {
        this.id = id;
        this.recordId = recordId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) recordId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof XincoCoreUserModifiedRecordPK)) {
            return false;
        }
        XincoCoreUserModifiedRecordPK other = (XincoCoreUserModifiedRecordPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.recordId != other.recordId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreUserModifiedRecordPK[id=" + id + ", recordId=" + recordId + "]";
    }
}
