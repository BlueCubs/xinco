/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.persistence;

import com.dreamer.Hibernate.audit.XincoAbstractAuditableObject;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Class: XincoCoreDataType
 * Created: Mar 24, 2008 2:30:53 PM
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_data_type")
@NamedQueries({@NamedQuery(name = "XincoCoreDataType.findById", query = "SELECT x FROM XincoCoreDataType x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreDataType.findByDesignation", query = "SELECT x FROM XincoCoreDataType x WHERE x.designation = :designation"), @NamedQuery(name = "XincoCoreDataType.findByDescription", query = "SELECT x FROM XincoCoreDataType x WHERE x.description = :description")})
public class XincoCoreDataType extends XincoAbstractAuditableObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "designation", nullable = false)
    private String designation;
    @Column(name = "description", nullable = false)
    private String description;

    public XincoCoreDataType() {
    }

    public XincoCoreDataType(Integer id) {
        this.id = id;
    }

    public XincoCoreDataType(Integer id, String designation, String description) {
        this.id = id;
        this.designation = designation;
        this.description = description;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoCoreDataType)) {
            return false;
        }
        XincoCoreDataType other = (XincoCoreDataType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.persistence.XincoCoreDataType[id=" + id + "]";
    }

}
