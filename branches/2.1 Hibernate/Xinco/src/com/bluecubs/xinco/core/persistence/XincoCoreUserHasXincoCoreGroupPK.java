package com.bluecubs.xinco.core.persistence;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Javier
 */
@Embeddable
public class XincoCoreUserHasXincoCoreGroupPK implements Serializable {
    @Column(name = "xinco_core_user_id", nullable = false)
    private int xincoCoreUserId;
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
        hash += xincoCoreUserId;
        hash += xincoCoreGroupId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
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
        return "com.bluecubs.xinco.core.persistence.XincoCoreUserHasXincoCoreGroupPK[xincoCoreUserId=" + xincoCoreUserId + ", xincoCoreGroupId=" + xincoCoreGroupId + "]";
    }

}
