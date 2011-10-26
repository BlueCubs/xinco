/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author Javier A. Ortiz Bultrón<javier.ortiz.78@gmail.com>
 */
@Entity@Table(name = "xinco_core_user_modified_record")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreUserModifiedRecord.findAll", query = "SELECT x FROM XincoCoreUserModifiedRecord x"),
    @NamedQuery(name = "XincoCoreUserModifiedRecord.findById", query = "SELECT x FROM XincoCoreUserModifiedRecord x WHERE x.xincoCoreUserModifiedRecordPK.id = :id"),
    @NamedQuery(name = "XincoCoreUserModifiedRecord.findByRecordId", query = "SELECT x FROM XincoCoreUserModifiedRecord x WHERE x.xincoCoreUserModifiedRecordPK.recordId = :recordId"),
    @NamedQuery(name = "XincoCoreUserModifiedRecord.findByModTime", query = "SELECT x FROM XincoCoreUserModifiedRecord x WHERE x.modTime = :modTime"),
    @NamedQuery(name = "XincoCoreUserModifiedRecord.findByModReason", query = "SELECT x FROM XincoCoreUserModifiedRecord x WHERE x.modReason = :modReason")})
public class XincoCoreUserModifiedRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoCoreUserModifiedRecordPK xincoCoreUserModifiedRecordPK;
    @Basic(optional = false)
    @Column(name = "mod_Time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modTime;
    @Basic(optional = false)
    @Column(name = "mod_Reason", nullable = false, length = 255)
    private String modReason;
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoCoreUser xincoCoreUser;

    public XincoCoreUserModifiedRecord() {
    }

    public XincoCoreUserModifiedRecord(XincoCoreUserModifiedRecordPK xincoCoreUserModifiedRecordPK) {
        this.xincoCoreUserModifiedRecordPK = xincoCoreUserModifiedRecordPK;
    }

    public XincoCoreUserModifiedRecord(XincoCoreUserModifiedRecordPK xincoCoreUserModifiedRecordPK, Date modTime) {
        this.xincoCoreUserModifiedRecordPK = xincoCoreUserModifiedRecordPK;
        this.modTime = modTime;
    }

    public XincoCoreUserModifiedRecord(int id, int record_id) {
        this.xincoCoreUserModifiedRecordPK = new XincoCoreUserModifiedRecordPK(id, record_id);
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

    public XincoCoreUser getXincoCoreUser() {
        return xincoCoreUser;
    }

    public void setXincoCoreUser(XincoCoreUser xincoCoreUser) {
        this.xincoCoreUser = xincoCoreUser;
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
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreUserModifiedRecord[ xincoCoreUserModifiedRecordPK=" + xincoCoreUserModifiedRecordPK + " ]";
    }

    
}
