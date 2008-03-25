/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.persistence.audit;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Class: XincoCoreGroupT
 * Created: Mar 24, 2008 2:44:19 PM
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_group_t")
@NamedQueries({@NamedQuery(name = "XincoCoreGroupT.findByRecordId", query = "SELECT x FROM XincoCoreGroupT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoCoreGroupT.findByDesignation", query = "SELECT x FROM XincoCoreGroupT x WHERE x.designation = :designation"), @NamedQuery(name = "XincoCoreGroupT.findById", query = "SELECT x FROM XincoCoreGroupT x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreGroupT.findByStatusNumber", query = "SELECT x FROM XincoCoreGroupT x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreGroupT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Column(name = "designation", nullable = false)
    private String designation;
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "status_number", nullable = false)
    private int statusNumber;

    public XincoCoreGroupT() {
    }

    public XincoCoreGroupT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreGroupT(Integer recordId, String designation, int id, int statusNumber) {
        this.recordId = recordId;
        this.designation = designation;
        this.id = id;
        this.statusNumber = statusNumber;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "com.bluecubs.xinco.persistence.audit.XincoCoreGroupT[recordId=" + recordId + "]";
    }

}
