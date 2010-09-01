/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.server.AuditedEntityListener;
import com.bluecubs.xinco.core.server.XincoAuditedObject;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@EntityListeners(AuditedEntityListener.class)
@Table(name = "xinco_core_group", uniqueConstraints = {@UniqueConstraint(columnNames = {"designation"})})
@NamedQueries({@NamedQuery(name = "XincoCoreGroup.findAll", query = "SELECT x FROM XincoCoreGroup x"), @NamedQuery(name = "XincoCoreGroup.findById", query = "SELECT x FROM XincoCoreGroup x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreGroup.findByDesignation", query = "SELECT x FROM XincoCoreGroup x WHERE x.designation = :designation"), @NamedQuery(name = "XincoCoreGroup.findByStatusNumber", query = "SELECT x FROM XincoCoreGroup x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreGroup extends XincoAuditedObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 255)
    private String designation;
    @Basic(optional = false)
    @Column(name = "status_number", nullable = false)
    private int statusNumber;
    @OneToMany(mappedBy = "xincoCoreGroupId", fetch = FetchType.LAZY)
    private List<XincoCoreAce> xincoCoreAceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreGroup", fetch = FetchType.LAZY)
    private List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupList;

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

    public List<XincoCoreAce> getXincoCoreAceList() {
        return xincoCoreAceList;
    }

    public void setXincoCoreAceList(List<XincoCoreAce> xincoCoreAceList) {
        this.xincoCoreAceList = xincoCoreAceList;
    }

    public List<XincoCoreUserHasXincoCoreGroup> getXincoCoreUserHasXincoCoreGroupList() {
        return xincoCoreUserHasXincoCoreGroupList;
    }

    public void setXincoCoreUserHasXincoCoreGroupList(List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupList) {
        this.xincoCoreUserHasXincoCoreGroupList = xincoCoreUserHasXincoCoreGroupList;
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
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreGroup[id=" + id + "]";
    }

}
