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
@Table(name = "xinco_dependency_type_t")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoDependencyTypeT.findAll", query = "SELECT x FROM XincoDependencyTypeT x"),
    @NamedQuery(name = "XincoDependencyTypeT.findByRecordId", query = "SELECT x FROM XincoDependencyTypeT x WHERE x.recordId = :recordId"),
    @NamedQuery(name = "XincoDependencyTypeT.findById", query = "SELECT x FROM XincoDependencyTypeT x WHERE x.id = :id"),
    @NamedQuery(name = "XincoDependencyTypeT.findByDescription", query = "SELECT x FROM XincoDependencyTypeT x WHERE x.description = :description"),
    @NamedQuery(name = "XincoDependencyTypeT.findByXincoDependencyBehaviorId", query = "SELECT x FROM XincoDependencyTypeT x WHERE x.xincoDependencyBehaviorId = :xincoDependencyBehaviorId"),
    @NamedQuery(name = "XincoDependencyTypeT.findByDesignation", query = "SELECT x FROM XincoDependencyTypeT x WHERE x.designation = :designation")})
public class XincoDependencyTypeT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "description", length = 255)
    private String description;
    @Basic(optional = false)
    @Column(name = "xinco_dependency_behavior_id", nullable = false)
    private int xincoDependencyBehaviorId;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 45)
    private String designation;

    public XincoDependencyTypeT() {
    }

    public XincoDependencyTypeT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoDependencyTypeT(Integer recordId, int id, int xincoDependencyBehaviorId, String designation) {
        this.recordId = recordId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
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
