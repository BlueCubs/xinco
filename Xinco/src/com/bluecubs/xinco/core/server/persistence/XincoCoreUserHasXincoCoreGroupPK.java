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
public class XincoCoreUserHasXincoCoreGroupPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "xinco_core_user_id", nullable = false)
    private int xincoCoreUserId;
    @Basic(optional = false)
    @Column(name = "xinco_core_group_id", nullable = false)
    private int xincoCoreGroupId;

    public XincoCoreUserHasXincoCoreGroupPK() {
    }

    public XincoCoreUserHasXincoCoreGroupPK(int xincoCoreUserId, int xincoCoreGroupId) {
        this.xincoCoreUserId = xincoCoreUserId;
        this.xincoCoreGroupId = xincoCoreGroupId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) xincoCoreUserId;
        hash += (int) xincoCoreGroupId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof XincoCoreUserHasXincoCoreGroupPK)) {
            return false;
        }
        XincoCoreUserHasXincoCoreGroupPK other = (XincoCoreUserHasXincoCoreGroupPK) object;
        if (this.xincoCoreUserId != other.xincoCoreUserId) {
            return false;
        }
        if (this.xincoCoreGroupId != other.xincoCoreGroupId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroupPK[xincoCoreUserId=" + xincoCoreUserId + ", xincoCoreGroupId=" + xincoCoreGroupId + "]";
    }
}
