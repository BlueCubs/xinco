/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_dependency_behavior", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"designation"})})
@NamedQueries({
    @NamedQuery(name = "XincoDependencyBehavior.findAll", query = "SELECT x FROM XincoDependencyBehavior x"),
    @NamedQuery(name = "XincoDependencyBehavior.findById", query = "SELECT x FROM XincoDependencyBehavior x WHERE x.id = :id"),
    @NamedQuery(name = "XincoDependencyBehavior.findByDesignation", query = "SELECT x FROM XincoDependencyBehavior x WHERE x.designation = :designation"),
    @NamedQuery(name = "XincoDependencyBehavior.findByDescription", query = "SELECT x FROM XincoDependencyBehavior x WHERE x.description = :description")})
public class XincoDependencyBehavior implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "XDBKEYGEN")
    @TableGenerator(name = "XDBKEYGEN", table = "xinco_id",
    pkColumnName = "tablename", valueColumnName = "last_id",
    pkColumnValue = "xinco_dependency_behavior", initialValue = 1001, allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 45)
    private String designation;
    @Column(name = "description", length = 45)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoDependencyBehavior")
    private List<XincoDependencyType> xincoDependencyTypeList;

    public XincoDependencyBehavior() {
    }

    public XincoDependencyBehavior(String designation) {
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

    public List<XincoDependencyType> getXincoDependencyTypeList() {
        return xincoDependencyTypeList;
    }

    public void setXincoDependencyTypeList(List<XincoDependencyType> xincoDependencyTypeList) {
        this.xincoDependencyTypeList = xincoDependencyTypeList;
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
