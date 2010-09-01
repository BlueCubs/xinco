/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_user_has_xinco_core_group_t")
@NamedQueries({@NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findAll", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x"), @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByRecordId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByXincoCoreUserId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.xincoCoreUserId = :xincoCoreUserId"), @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByXincoCoreGroupId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.xincoCoreGroupId = :xincoCoreGroupId"), @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByStatusNumber", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreUserHasXincoCoreGroupT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Basic(optional = false)
    @Column(name = "xinco_core_user_id", nullable = false)
    private int xincoCoreUserId;
    @Basic(optional = false)
    @Column(name = "xinco_core_group_id", nullable = false)
    private int xincoCoreGroupId;
    @Basic(optional = false)
    @Column(name = "status_number", nullable = false)
    private int statusNumber;

    public XincoCoreUserHasXincoCoreGroupT() {
    }

    public XincoCoreUserHasXincoCoreGroupT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreUserHasXincoCoreGroupT(Integer recordId, int xincoCoreUserId, int xincoCoreGroupId, int statusNumber) {
        this.recordId = recordId;
        this.xincoCoreUserId = xincoCoreUserId;
        this.xincoCoreGroupId = xincoCoreGroupId;
        this.statusNumber = statusNumber;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public int getXincoCoreUserId() {
        return xincoCoreUserId;
    }

    public void setXincoCoreUserId(int xincoCoreUserId) {
        this.xincoCoreUserId = xincoCoreUserId;
    }

    public int getXincoCoreGroupId() {
        return xincoCoreGroupId;
    }

    public void setXincoCoreGroupId(int xincoCoreGroupId) {
        this.xincoCoreGroupId = xincoCoreGroupId;
    }

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoCoreUserHasXincoCoreGroupT)) {
            return false;
        }
        XincoCoreUserHasXincoCoreGroupT other = (XincoCoreUserHasXincoCoreGroupT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroupT[recordId=" + recordId + "]";
    }

}
