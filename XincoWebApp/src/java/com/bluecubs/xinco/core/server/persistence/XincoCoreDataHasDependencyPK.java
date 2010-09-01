/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Embeddable
public class XincoCoreDataHasDependencyPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "dependency_type_id", nullable = false)
    private int dependencyTypeId;
    @Basic(optional = false)
    @Column(name = "xinco_core_data_parent_id", nullable = false)
    private int xincoCoreDataParentId;
    @Basic(optional = false)
    @Column(name = "xinco_core_data_children_id", nullable = false)
    private int xincoCoreDataChildrenId;

    public XincoCoreDataHasDependencyPK() {
    }

    public XincoCoreDataHasDependencyPK(int dependencyTypeId, int xincoCoreDataParentId, int xincoCoreDataChildrenId) {
        this.dependencyTypeId = dependencyTypeId;
        this.xincoCoreDataParentId = xincoCoreDataParentId;
        this.xincoCoreDataChildrenId = xincoCoreDataChildrenId;
    }

    public int getDependencyTypeId() {
        return dependencyTypeId;
    }

    public void setDependencyTypeId(int dependencyTypeId) {
        this.dependencyTypeId = dependencyTypeId;
    }

    public int getXincoCoreDataParentId() {
        return xincoCoreDataParentId;
    }

    public void setXincoCoreDataParentId(int xincoCoreDataParentId) {
        this.xincoCoreDataParentId = xincoCoreDataParentId;
    }

    public int getXincoCoreDataChildrenId() {
        return xincoCoreDataChildrenId;
    }

    public void setXincoCoreDataChildrenId(int xincoCoreDataChildrenId) {
        this.xincoCoreDataChildrenId = xincoCoreDataChildrenId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) dependencyTypeId;
        hash += (int) xincoCoreDataParentId;
        hash += (int) xincoCoreDataChildrenId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoCoreDataHasDependencyPK)) {
            return false;
        }
        XincoCoreDataHasDependencyPK other = (XincoCoreDataHasDependencyPK) object;
        if (this.dependencyTypeId != other.dependencyTypeId) {
            return false;
        }
        if (this.xincoCoreDataParentId != other.xincoCoreDataParentId) {
            return false;
        }
        if (this.xincoCoreDataChildrenId != other.xincoCoreDataChildrenId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependencyPK[dependencyTypeId=" + dependencyTypeId + ", xincoCoreDataParentId=" + xincoCoreDataParentId + ", xincoCoreDataChildrenId=" + xincoCoreDataChildrenId + "]";
    }

}
