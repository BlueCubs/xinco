/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.server.AuditedEntityListener;
import com.bluecubs.xinco.core.server.XincoAuditedObject;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@EntityListeners(AuditedEntityListener.class)
@Table(name = "xinco_dependency_type", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"designation"})})
@NamedQueries({
    @NamedQuery(name = "XincoDependencyType.findAll", query = "SELECT x FROM XincoDependencyType x"),
    @NamedQuery(name = "XincoDependencyType.findById", query = "SELECT x FROM XincoDependencyType x WHERE x.id = :id"),
    @NamedQuery(name = "XincoDependencyType.findByDesignation", query = "SELECT x FROM XincoDependencyType x WHERE x.designation = :designation"),
    @NamedQuery(name = "XincoDependencyType.findByDescription", query = "SELECT x FROM XincoDependencyType x WHERE x.description = :description")})
public class XincoDependencyType extends XincoAuditedObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "XDEPKEYGEN")
    @TableGenerator(name = "XDEPKEYGEN", table = "xinco_id",
    pkColumnName = "tablename", valueColumnName = "last_id",
    pkColumnValue = "xinco_dependency_type", initialValue = 1001, allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 45)
    private String designation;
    @Column(name = "description", length = 255)
    private String description;
    @JoinColumn(name = "xinco_dependency_behavior_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private XincoDependencyBehavior xincoDependencyBehavior;

    public XincoDependencyType() {
    }

    public XincoDependencyType(String designation) {
        this.designation = designation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public XincoDependencyBehavior getXincoDependencyBehavior() {
        return xincoDependencyBehavior;
    }

    public void setXincoDependencyBehavior(XincoDependencyBehavior xincoDependencyBehavior) {
        this.xincoDependencyBehavior = xincoDependencyBehavior;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoDependencyType)) {
            return false;
        }
        XincoDependencyType other = (XincoDependencyType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoDependencyType[id=" + id + "]";
    }
}
