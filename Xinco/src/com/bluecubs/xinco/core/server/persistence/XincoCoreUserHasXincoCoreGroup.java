/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_user_has_xinco_core_group")
@NamedQueries({
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroup.findAll",
    query = "SELECT x FROM XincoCoreUserHasXincoCoreGroup x"),
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroup.findByXincoCoreUserId",
    query = "SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreUserId = :xincoCoreUserId"),
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroup.findByXincoCoreGroupId",
    query = "SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreGroupId = :xincoCoreGroupId"),
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroup.findByStatusNumber",
    query = "SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreUserHasXincoCoreGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoCoreUserHasXincoCoreGroupPK xincoCoreUserHasXincoCoreGroupPK;
    @Basic(optional = false)
    @Column(name = "status_number", nullable = false)
    private int statusNumber;
    @JoinColumn(name = "xinco_core_group_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoCoreGroup xincoCoreGroup;
    @JoinColumn(name = "xinco_core_user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoCoreUser xincoCoreUser;

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

    public XincoCoreGroup getXincoCoreGroup() {
        return xincoCoreGroup;
    }

    public void setXincoCoreGroup(XincoCoreGroup xincoCoreGroup) {
        this.xincoCoreGroup = xincoCoreGroup;
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
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup[xincoCoreUserHasXincoCoreGroupPK=" + xincoCoreUserHasXincoCoreGroupPK + "]";
    }
}
