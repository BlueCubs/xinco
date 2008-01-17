/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.persistence;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author javydreamercsw
 */
@Embeddable
public class XincoCoreUserModifiedRecordPK implements Serializable {
    @Column(name = "id", nullable = false)
    private int id;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
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
        return "com.bluecubs.xinco.core.persistence.XincoCoreUserModifiedRecordPK[id=" + id + ", recordId=" + recordId + "]";
    }

}
