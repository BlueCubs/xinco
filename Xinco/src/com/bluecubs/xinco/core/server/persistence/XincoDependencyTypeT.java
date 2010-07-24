/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_dependency_type_t")
@NamedQueries({
    @NamedQuery(name = "XincoDependencyTypeT.findAll", query = "SELECT x FROM XincoDependencyTypeT x"),
    @NamedQuery(name = "XincoDependencyTypeT.findByRecordId", query = "SELECT x FROM XincoDependencyTypeT x WHERE x.recordId = :recordId"),
    @NamedQuery(name = "XincoDependencyTypeT.findById", query = "SELECT x FROM XincoDependencyTypeT x WHERE x.id = :id"),
    @NamedQuery(name = "XincoDependencyTypeT.findByXincoDependencyBehaviorId", query = "SELECT x FROM XincoDependencyTypeT x WHERE x.xincoDependencyBehaviorId = :xincoDependencyBehaviorId"),
    @NamedQuery(name = "XincoDependencyTypeT.findByDesignation", query = "SELECT x FROM XincoDependencyTypeT x WHERE x.designation = :designation"),
    @NamedQuery(name = "XincoDependencyTypeT.findByDescription", query = "SELECT x FROM XincoDependencyTypeT x WHERE x.description = :description")})
public class XincoDependencyTypeT implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "XincoDependencyTypeRECORDIDKEYGEN")
    @TableGenerator(name = "XincoDependencyTypeRECORDIDKEYGEN", table = "xinco_id",
    pkColumnName = "tablename", valueColumnName = "last_id",
    pkColumnValue = "xinco_core_user_modified_record", initialValue = 1, allocationSize = 1)
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private int id;
    @Basic(optional = false)
    @Column(name = "xinco_dependency_behavior_id", nullable = false)
    private int xincoDependencyBehaviorId;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 45)
    private String designation;
    @Column(name = "description", length = 255)
    private String description;

    public XincoDependencyTypeT() {
    }

    public XincoDependencyTypeT(int id, int xincoDependencyBehaviorId, String designation) {
        this.id = id;
        this.xincoDependencyBehaviorId = xincoDependencyBehaviorId;
        this.designation = designation;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getXincoDependencyBehaviorId() {
        return xincoDependencyBehaviorId;
    }

    public void setXincoDependencyBehaviorId(int xincoDependencyBehaviorId) {
        this.xincoDependencyBehaviorId = xincoDependencyBehaviorId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof XincoDependencyTypeT)) {
            return false;
        }
        XincoDependencyTypeT other = (XincoDependencyTypeT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoDependencyTypeT[recordId=" + recordId + "]";
    }
}
