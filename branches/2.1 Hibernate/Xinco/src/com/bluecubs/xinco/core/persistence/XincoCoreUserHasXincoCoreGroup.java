/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.persistence;

import com.bluecubs.xinco.core.persistence.audit.tools.XincoAbstractAuditableObject;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Javier A. Ortiz
 */
@Entity
@Table(name = "xinco_core_user_has_xinco_core_group")
@NamedQueries({@NamedQuery(name = "XincoCoreUserHasXincoCoreGroup.findByXincoCoreUserId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreUserId = :xincoCoreUserId"), @NamedQuery(name = "XincoCoreUserHasXincoCoreGroup.findByXincoCoreGroupId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreGroupId = :xincoCoreGroupId"), @NamedQuery(name = "XincoCoreUserHasXincoCoreGroup.findByStatusNumber", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreUserHasXincoCoreGroup extends XincoAbstractAuditableObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoCoreUserHasXincoCoreGroupPK xincoCoreUserHasXincoCoreGroupPK;
    @Column(name = "status_number", nullable = false)
    private int statusNumber;

    public XincoCoreUserHasXincoCoreGroup() {
    }

    public XincoCoreUserHasXincoCoreGroup(XincoCoreUserHasXincoCoreGroupPK xincoCoreUserHasXincoCoreGroupPK) {
        this.xincoCoreUserHasXincoCoreGroupPK = xincoCoreUserHasXincoCoreGroupPK;
    }

    public XincoCoreUserHasXincoCoreGroup(XincoCoreUserHasXincoCoreGroupPK xincoCoreUserHasXincoCoreGroupPK, int statusNumber) {
        this.xincoCoreUserHasXincoCoreGroupPK = xincoCoreUserHasXincoCoreGroupPK;
        this.statusNumber = statusNumber;
    }

    public XincoCoreUserHasXincoCoreGroup(int xincoCoreUserId, int xincoCoreGroupId) {
        this.xincoCoreUserHasXincoCoreGroupPK = new XincoCoreUserHasXincoCoreGroupPK(xincoCoreUserId, xincoCoreGroupId);
    }

    public XincoCoreUserHasXincoCoreGroupPK getXincoCoreUserHasXincoCoreGroupPK() {
        return xincoCoreUserHasXincoCoreGroupPK;
    }

    public void setXincoCoreUserHasXincoCoreGroupPK(XincoCoreUserHasXincoCoreGroupPK xincoCoreUserHasXincoCoreGroupPK) {
        this.xincoCoreUserHasXincoCoreGroupPK = xincoCoreUserHasXincoCoreGroupPK;
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
        hash += (xincoCoreUserHasXincoCoreGroupPK != null ? xincoCoreUserHasXincoCoreGroupPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoCoreUserHasXincoCoreGroup)) {
            return false;
        }
        XincoCoreUserHasXincoCoreGroup other = (XincoCoreUserHasXincoCoreGroup) object;
        if ((this.xincoCoreUserHasXincoCoreGroupPK == null && other.xincoCoreUserHasXincoCoreGroupPK != null) || (this.xincoCoreUserHasXincoCoreGroupPK != null && !this.xincoCoreUserHasXincoCoreGroupPK.equals(other.xincoCoreUserHasXincoCoreGroupPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.persistence.XincoCoreUserHasXincoCoreGroup[xincoCoreUserHasXincoCoreGroupPK=" + xincoCoreUserHasXincoCoreGroupPK + "]";
    }

}
