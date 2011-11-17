/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_data_has_dependency_t")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreDataHasDependencyT.findAll", query = "SELECT x FROM XincoCoreDataHasDependencyT x"),
    @NamedQuery(name = "XincoCoreDataHasDependencyT.findByRecordId", query = "SELECT x FROM XincoCoreDataHasDependencyT x WHERE x.recordId = :recordId"),
    @NamedQuery(name = "XincoCoreDataHasDependencyT.findByDependencyTypeId", query = "SELECT x FROM XincoCoreDataHasDependencyT x WHERE x.dependencyTypeId = :dependencyTypeId"),
    @NamedQuery(name = "XincoCoreDataHasDependencyT.findByXincoCoreDataParentId", query = "SELECT x FROM XincoCoreDataHasDependencyT x WHERE x.xincoCoreDataParentId = :xincoCoreDataParentId"),
    @NamedQuery(name = "XincoCoreDataHasDependencyT.findByXincoCoreDataChildrenId", query = "SELECT x FROM XincoCoreDataHasDependencyT x WHERE x.xincoCoreDataChildrenId = :xincoCoreDataChildrenId")})
public class XincoCoreDataHasDependencyT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Basic(optional = false)
    @Column(name = "dependency_type_id", nullable = false)
    private int dependencyTypeId;
    @Basic(optional = false)
    @Column(name = "xinco_core_data_parent_id", nullable = false)
    private int xincoCoreDataParentId;
    @Basic(optional = false)
    @Column(name = "xinco_core_data_children_id", nullable = false)
    private int xincoCoreDataChildrenId;

    public XincoCoreDataHasDependencyT() {
    }

    public XincoCoreDataHasDependencyT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreDataHasDependencyT(Integer recordId, int dependencyTypeId, int xincoCoreDataParentId, int xincoCoreDataChildrenId) {
        this.recordId = recordId;
        this.dependencyTypeId = dependencyTypeId;
        this.xincoCoreDataParentId = xincoCoreDataParentId;
        this.xincoCoreDataChildrenId = xincoCoreDataChildrenId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
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
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof XincoCoreDataHasDependencyT)) {
            return false;
        }
        XincoCoreDataHasDependencyT other = (XincoCoreDataHasDependencyT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependencyT[recordId=" + recordId + "]";
    }

}
