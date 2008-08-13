package com.bluecubs.xinco.core.persistence;

import com.bluecubs.xinco.core.hibernate.audit.XincoAbstractAuditableObject;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Javier
 */
@Entity
@Table(name = "xinco_core_group", catalog = "xinco", schema = "", 
uniqueConstraints = {@UniqueConstraint(columnNames = {"designation"})})
@NamedQueries({@NamedQuery(name = "XincoCoreGroup.findAll",
    query = "SELECT x FROM XincoCoreGroup x"),
    @NamedQuery(name = "XincoCoreGroup.findById",
    query = "SELECT x FROM XincoCoreGroup x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreGroup.findByDesignation",
    query = "SELECT x FROM XincoCoreGroup x WHERE x.designation = :designation"),
    @NamedQuery(name = "XincoCoreGroup.findByStatusNumber",
    query = "SELECT x FROM XincoCoreGroup x WHERE x.statusNumber = :statusNumber")
})
public class XincoCoreGroup extends XincoAbstractAuditableObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", nullable = false)
    private Integer xcgId;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 255)
    private String designation;
    @Basic(optional = false)
    @Column(name = "status_number", nullable = false)
    private int statusNumber;

    public XincoCoreGroup() {
    }

    public XincoCoreGroup(Integer id) {
        this.xcgId = id;
    }

    public XincoCoreGroup(Integer id, String designation, int statusNumber) {
        this.xcgId = id;
        this.designation = designation;
        this.statusNumber = statusNumber;
    }

    public Integer getId() {
        return xcgId;
    }

    public void setId(Integer id) {
        this.xcgId = id;
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
        hash += (xcgId != null ? xcgId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoCoreGroup)) {
            return false;
        }
        XincoCoreGroup other = (XincoCoreGroup) object;
        if ((this.xcgId == null && other.xcgId != null) || (this.xcgId != null && !this.xcgId.equals(other.xcgId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.persistence.XincoCoreGroup[id=" + xcgId + "]";
    }
}
