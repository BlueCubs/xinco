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
 * Class: XincoCoreGroup
 * Created: Mar 24, 2008 2:29:21 PM
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_group")
@NamedQueries({@NamedQuery(name = "XincoCoreGroup.findById", query = "SELECT x FROM XincoCoreGroup x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreGroup.findByDesignation", query = "SELECT x FROM XincoCoreGroup x WHERE x.designation = :designation"), @NamedQuery(name = "XincoCoreGroup.findByStatusNumber", query = "SELECT x FROM XincoCoreGroup x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreGroup extends XincoAbstractAuditableObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "designation", nullable = false, unique = true)
    private String designation;
    @Column(name = "status_number", nullable = false)
    private int statusNumber;

    public XincoCoreGroup() {
    }

    public XincoCoreGroup(Integer id) {
        this.id = id;
    }

    public XincoCoreGroup(Integer id, String designation, int statusNumber) {
        this.id = id;
        this.designation = designation;
        this.statusNumber = statusNumber;
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

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
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
        if (!(object instanceof XincoCoreGroup)) {
            return false;
        }
        XincoCoreGroup other = (XincoCoreGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.persistence.XincoCoreGroup[id=" + id + "]";
    }
}
