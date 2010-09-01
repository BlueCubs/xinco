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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_dependency_behavior", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"designation"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoDependencyBehavior.findAll", query = "SELECT x FROM XincoDependencyBehavior x"),
    @NamedQuery(name = "XincoDependencyBehavior.findById", query = "SELECT x FROM XincoDependencyBehavior x WHERE x.id = :id"),
    @NamedQuery(name = "XincoDependencyBehavior.findByDescription", query = "SELECT x FROM XincoDependencyBehavior x WHERE x.description = :description"),
    @NamedQuery(name = "XincoDependencyBehavior.findByDesignation", query = "SELECT x FROM XincoDependencyBehavior x WHERE x.designation = :designation")})
public class XincoDependencyBehavior implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "description", length = 45)
    private String description;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 45)
    private String designation;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoDependencyBehaviorId")
    private Collection<XincoDependencyType> xincoDependencyTypeCollection;

    public XincoDependencyBehavior() {
    }

    public XincoDependencyBehavior(Integer id) {
        this.id = id;
    }

    public XincoDependencyBehavior(Integer id, String designation) {
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
    public Collection<XincoDependencyType> getXincoDependencyTypeCollection() {
        return xincoDependencyTypeCollection;
    }

    public void setXincoDependencyTypeCollection(Collection<XincoDependencyType> xincoDependencyTypeCollection) {
        this.xincoDependencyTypeCollection = xincoDependencyTypeCollection;
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
        if (!(object instanceof XincoDependencyBehavior)) {
            return false;
        }
        XincoDependencyBehavior other = (XincoDependencyBehavior) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoDependencyBehavior[id=" + id + "]";
    }

}
