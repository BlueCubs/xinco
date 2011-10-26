/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_dependency_type", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"designation"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoDependencyType.findAll", query = "SELECT x FROM XincoDependencyType x"),
    @NamedQuery(name = "XincoDependencyType.findById", query = "SELECT x FROM XincoDependencyType x WHERE x.id = :id"),
    @NamedQuery(name = "XincoDependencyType.findByDescription", query = "SELECT x FROM XincoDependencyType x WHERE x.description = :description"),
    @NamedQuery(name = "XincoDependencyType.findByDesignation", query = "SELECT x FROM XincoDependencyType x WHERE x.designation = :designation")})
public class XincoDependencyType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "XincoDependencyTypeGen")
    @TableGenerator(name = "XincoDependencyTypeGen", table = "xinco_id",
    pkColumnName = "tablename",
    valueColumnName = "last_id",
    pkColumnValue = "xinco_dependency_type",
    allocationSize = 1,
    initialValue=1000)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "description", length = 255)
    private String description;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 45)
    private String designation;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoDependencyType")
    private Collection<XincoCoreDataHasDependency> xincoCoreDataHasDependencyCollection;
    @JoinColumn(name = "xinco_dependency_behavior_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private XincoDependencyBehavior xincoDependencyBehavior;

    public XincoDependencyType() {
    }

    public XincoDependencyType(Integer id) {
        this.id = id;
    }

    public XincoDependencyType(Integer id, String designation) {
        this.id = id;
        this.designation = designation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @XmlTransient
    public Collection<XincoCoreDataHasDependency> getXincoCoreDataHasDependencyCollection() {
        return xincoCoreDataHasDependencyCollection;
    }

    public void setXincoCoreDataHasDependencyCollection(Collection<XincoCoreDataHasDependency> xincoCoreDataHasDependencyCollection) {
        this.xincoCoreDataHasDependencyCollection = xincoCoreDataHasDependencyCollection;
    }

    public XincoDependencyBehavior getXincoDependencyBehavior() {
        return xincoDependencyBehavior;
    }

    public void setXincoDependencyBehavior(XincoDependencyBehavior xincoDependencyBehaviorId) {
        this.xincoDependencyBehavior = xincoDependencyBehaviorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
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
