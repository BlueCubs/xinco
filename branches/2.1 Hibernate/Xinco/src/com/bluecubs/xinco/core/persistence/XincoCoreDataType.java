package com.bluecubs.xinco.core.persistence;

import com.bluecubs.xinco.core.server.XincoAbstractAuditableObject;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Javier
 */
@Entity
@Table(name = "xinco_core_data_type", catalog = "xinco", schema = "")
@NamedQueries({@NamedQuery(name = "XincoCoreDataType.findAll",
    query = "SELECT x FROM XincoCoreDataType x"),
    @NamedQuery(name = "XincoCoreDataType.findById",
    query = "SELECT x FROM XincoCoreDataType x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreDataType.findByDesignation",
    query = "SELECT x FROM XincoCoreDataType x WHERE x.designation = :designation"),
    @NamedQuery(name = "XincoCoreDataType.findByDescription",
    query = "SELECT x FROM XincoCoreDataType x WHERE x.description = :description")
})
public class XincoCoreDataType extends XincoAbstractAuditableObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", nullable = false)
    private Integer xcdtId;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 255)
    private String designation;
    @Basic(optional = false)
    @Column(name = "description", nullable = false, length = 255)
    private String description;

    public XincoCoreDataType() {
    }

    public XincoCoreDataType(Integer id) {
        this.xcdtId = id;
    }

    public XincoCoreDataType(Integer id, String designation, String description) {
        this.xcdtId = id;
        this.designation = designation;
        this.description = description;
    }

    public Integer getId() {
        return xcdtId;
    }

    public void setId(Integer id) {
        this.xcdtId = id;
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
        hash += (xcdtId != null ? xcdtId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoCoreDataType)) {
            return false;
        }
        XincoCoreDataType other = (XincoCoreDataType) object;
        if ((this.xcdtId == null && other.xcdtId != null) || (this.xcdtId != null && !this.xcdtId.equals(other.xcdtId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.persistence.XincoCoreDataType[id=" + xcdtId + "]";
    }
}
