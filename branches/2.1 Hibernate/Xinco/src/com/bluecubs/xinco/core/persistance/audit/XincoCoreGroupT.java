/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.persistance.audit;

import com.bluecubs.xinco.core.server.persistance.audit.XincoAbstractAuditableObject;
import java.io.Serializable;
import java.util.HashMap;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 *
 * @author Javier A. Ortiz
 */
@Entity
@Table(name = "xinco_core_group_t")
@NamedQueries({@NamedQuery(name = "XincoCoreGroupT.findByRecordId", query = "SELECT x FROM XincoCoreGroupT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoCoreGroupT.findById", query = "SELECT x FROM XincoCoreGroupT x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreGroupT.findByDesignation", query = "SELECT x FROM XincoCoreGroupT x WHERE x.designation = :designation"), @NamedQuery(name = "XincoCoreGroupT.findByStatusNumber", query = "SELECT x FROM XincoCoreGroupT x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreGroupT extends XincoAbstractAuditableObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "designation", nullable = false)
    private String designation;
    @Column(name = "status_number", nullable = false)
    private int statusNumber;

    public XincoCoreGroupT() {
    }

    public XincoCoreGroupT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreGroupT(Integer recordId, int id, String designation, int statusNumber) {
        this.recordId = recordId;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
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
        return "com.bluecubs.xinco.add.XincoCoreGroupT[recordId=" + recordId + "]";
    }
}
