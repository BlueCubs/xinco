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
 * Class: XincoCoreDataTypeT
 * Created: Mar 24, 2008 2:44:26 PM
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_data_type_t")
@NamedQueries({@NamedQuery(name = "XincoCoreDataTypeT.findByRecordId", query = "SELECT x FROM XincoCoreDataTypeT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoCoreDataTypeT.findByDescription", query = "SELECT x FROM XincoCoreDataTypeT x WHERE x.description = :description"), @NamedQuery(name = "XincoCoreDataTypeT.findByDesignation", query = "SELECT x FROM XincoCoreDataTypeT x WHERE x.designation = :designation"), @NamedQuery(name = "XincoCoreDataTypeT.findById", query = "SELECT x FROM XincoCoreDataTypeT x WHERE x.id = :id")})
public class XincoCoreDataTypeT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "designation", nullable = false)
    private String designation;
    @Column(name = "id", nullable = false)
    private int id;

    public XincoCoreDataTypeT() {
    }

    public XincoCoreDataTypeT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreDataTypeT(Integer recordId, String description, String designation, int id) {
        this.recordId = recordId;
        this.description = description;
        this.designation = designation;
        this.id = id;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        if (!(object instanceof XincoCoreDataTypeT)) {
            return false;
        }
        XincoCoreDataTypeT other = (XincoCoreDataTypeT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.persistence.audit.XincoCoreDataTypeT[recordId=" + recordId + "]";
    }

}
