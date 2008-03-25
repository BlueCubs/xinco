/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.persistence.audit;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Class: XincoCoreUserHasXincoCoreGroupT
 * Created: Mar 24, 2008 2:44:23 PM
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_user_has_xinco_core_group_t")
@NamedQueries({@NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByRecordId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByStatusNumber", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.statusNumber = :statusNumber"), @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByXincoCoreGroupId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.xincoCoreGroupId = :xincoCoreGroupId"), @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByXincoCoreUserId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.xincoCoreUserId = :xincoCoreUserId")})
public class XincoCoreUserHasXincoCoreGroupT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Column(name = "status_number", nullable = false)
    private int statusNumber;
    @Column(name = "xinco_core_group_id", nullable = false)
    private int xincoCoreGroupId;
    @Column(name = "xinco_core_user_id", nullable = false)
    private int xincoCoreUserId;

    public XincoCoreUserHasXincoCoreGroupT() {
    }

    public XincoCoreUserHasXincoCoreGroupT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreUserHasXincoCoreGroupT(Integer recordId, int statusNumber, int xincoCoreGroupId, int xincoCoreUserId) {
        this.recordId = recordId;
        this.statusNumber = statusNumber;
        this.xincoCoreGroupId = xincoCoreGroupId;
        this.xincoCoreUserId = xincoCoreUserId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    public int getXincoCoreGroupId() {
        return xincoCoreGroupId;
    }

    public void setXincoCoreGroupId(int xincoCoreGroupId) {
        this.xincoCoreGroupId = xincoCoreGroupId;
    }

    public int getXincoCoreUserId() {
        return xincoCoreUserId;
    }

    public void setXincoCoreUserId(int xincoCoreUserId) {
        this.xincoCoreUserId = xincoCoreUserId;
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
        return "com.bluecubs.xinco.persistence.audit.XincoCoreUserHasXincoCoreGroupT[recordId=" + recordId + "]";
    }

}
