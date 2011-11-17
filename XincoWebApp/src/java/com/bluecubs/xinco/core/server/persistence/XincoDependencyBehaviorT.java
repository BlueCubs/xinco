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
@Table(name = "xinco_dependency_behavior_t")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoDependencyBehaviorT.findAll", query = "SELECT x FROM XincoDependencyBehaviorT x"),
    @NamedQuery(name = "XincoDependencyBehaviorT.findByRecordId", query = "SELECT x FROM XincoDependencyBehaviorT x WHERE x.recordId = :recordId"),
    @NamedQuery(name = "XincoDependencyBehaviorT.findById", query = "SELECT x FROM XincoDependencyBehaviorT x WHERE x.id = :id"),
    @NamedQuery(name = "XincoDependencyBehaviorT.findByDescription", query = "SELECT x FROM XincoDependencyBehaviorT x WHERE x.description = :description"),
    @NamedQuery(name = "XincoDependencyBehaviorT.findByDesignation", query = "SELECT x FROM XincoDependencyBehaviorT x WHERE x.designation = :designation")})
public class XincoDependencyBehaviorT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "description", length = 45)
    private String description;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 45)
    private String designation;

    public XincoDependencyBehaviorT() {
    }

    public XincoDependencyBehaviorT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoDependencyBehaviorT(Integer recordId, int id, String designation) {
        this.recordId = recordId;
        this.id = id;
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
        
        if (!(object instanceof XincoDependencyBehaviorT)) {
            return false;
        }
        XincoDependencyBehaviorT other = (XincoDependencyBehaviorT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoDependencyBehaviorT[recordId=" + recordId + "]";
    }

}
