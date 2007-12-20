/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.persistance;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Javier A. Ortiz
 */
@Entity
@Table(name = "xinco_core_user_modified_record")
@NamedQueries({@NamedQuery(name = "XincoCoreUserModifiedRecord.findById", query = "SELECT x FROM XincoCoreUserModifiedRecord x WHERE x.xincoCoreUserModifiedRecordPK.id = :id"), @NamedQuery(name = "XincoCoreUserModifiedRecord.findByRecordId", query = "SELECT x FROM XincoCoreUserModifiedRecord x WHERE x.xincoCoreUserModifiedRecordPK.recordId = :recordId"), @NamedQuery(name = "XincoCoreUserModifiedRecord.findByModTime", query = "SELECT x FROM XincoCoreUserModifiedRecord x WHERE x.modTime = :modTime"), @NamedQuery(name = "XincoCoreUserModifiedRecord.findByModReason", query = "SELECT x FROM XincoCoreUserModifiedRecord x WHERE x.modReason = :modReason")})
public class XincoCoreUserModifiedRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoCoreUserModifiedRecordPK xincoCoreUserModifiedRecordPK;
    @Column(name = "mod_Time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modTime;
    @Column(name = "mod_Reason")
    private String modReason;

    public XincoCoreUserModifiedRecord() {
    }

    public XincoCoreUserModifiedRecord(XincoCoreUserModifiedRecordPK xincoCoreUserModifiedRecordPK) {
        this.xincoCoreUserModifiedRecordPK = xincoCoreUserModifiedRecordPK;
    }

    public XincoCoreUserModifiedRecord(XincoCoreUserModifiedRecordPK xincoCoreUserModifiedRecordPK, Date modTime) {
        this.xincoCoreUserModifiedRecordPK = xincoCoreUserModifiedRecordPK;
        this.modTime = modTime;
    }

    public XincoCoreUserModifiedRecord(int id, int recordId) {
        this.xincoCoreUserModifiedRecordPK = new XincoCoreUserModifiedRecordPK(id, recordId);
    }

    public XincoCoreUserModifiedRecordPK getXincoCoreUserModifiedRecordPK() {
        return xincoCoreUserModifiedRecordPK;
    }

    public void setXincoCoreUserModifiedRecordPK(XincoCoreUserModifiedRecordPK xincoCoreUserModifiedRecordPK) {
        this.xincoCoreUserModifiedRecordPK = xincoCoreUserModifiedRecordPK;
    }

    public Date getModTime() {
        return modTime;
    }

    public void setModTime(Date modTime) {
        this.modTime = modTime;
    }

    public String getModReason() {
        return modReason;
    }

    public void setModReason(String modReason) {
        this.modReason = modReason;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xincoCoreUserModifiedRecordPK != null ? xincoCoreUserModifiedRecordPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoCoreUserModifiedRecord)) {
            return false;
        }
        XincoCoreUserModifiedRecord other = (XincoCoreUserModifiedRecord) object;
        if ((this.xincoCoreUserModifiedRecordPK == null && other.xincoCoreUserModifiedRecordPK != null) || (this.xincoCoreUserModifiedRecordPK != null && !this.xincoCoreUserModifiedRecordPK.equals(other.xincoCoreUserModifiedRecordPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.add.XincoCoreUserModifiedRecord[xincoCoreUserModifiedRecordPK=" + xincoCoreUserModifiedRecordPK + "]";
    }

}
