package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_group_t")
@NamedQueries({
    @NamedQuery(name = "XincoCoreGroupT.findAll", query = "SELECT x FROM XincoCoreGroupT x"),
    @NamedQuery(name = "XincoCoreGroupT.findByRecordId", query = "SELECT x FROM XincoCoreGroupT x WHERE x.recordId = :recordId"),
    @NamedQuery(name = "XincoCoreGroupT.findById", query = "SELECT x FROM XincoCoreGroupT x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreGroupT.findByDesignation", query = "SELECT x FROM XincoCoreGroupT x WHERE x.designation = :designation"),
    @NamedQuery(name = "XincoCoreGroupT.findByStatusNumber", query = "SELECT x FROM XincoCoreGroupT x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreGroupT implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "XCGRECORDIDKEYGEN")
    @TableGenerator(name = "XCGRECORDIDKEYGEN", table = "xinco_id",
    pkColumnName = "tablename", valueColumnName = "last_id",
    pkColumnValue = "xinco_core_user_modified_record", initialValue = 1, allocationSize = 1)
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private int id;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 255)
    private String designation;
    @Basic(optional = false)
    @Column(name = "status_number", nullable = false)
    private int statusNumber;

    public XincoCoreGroupT() {
    }

    public XincoCoreGroupT(int id, String designation, int statusNumber) {
        this.id = id;
        this.designation = designation;
        this.statusNumber = statusNumber;
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
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof XincoCoreGroupT)) {
            return false;
        }
        XincoCoreGroupT other = (XincoCoreGroupT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreGroupT[recordId=" + recordId + "]";
    }
}
