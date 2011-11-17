/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_data_t")
@NamedQueries({@NamedQuery(name = "XincoCoreDataT.findAll", query = "SELECT x FROM XincoCoreDataT x"), @NamedQuery(name = "XincoCoreDataT.findByRecordId", query = "SELECT x FROM XincoCoreDataT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoCoreDataT.findById", query = "SELECT x FROM XincoCoreDataT x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreDataT.findByXincoCoreNodeId", query = "SELECT x FROM XincoCoreDataT x WHERE x.xincoCoreNodeId = :xincoCoreNodeId"), @NamedQuery(name = "XincoCoreDataT.findByXincoCoreLanguageId", query = "SELECT x FROM XincoCoreDataT x WHERE x.xincoCoreLanguageId = :xincoCoreLanguageId"), @NamedQuery(name = "XincoCoreDataT.findByXincoCoreDataTypeId", query = "SELECT x FROM XincoCoreDataT x WHERE x.xincoCoreDataTypeId = :xincoCoreDataTypeId"), @NamedQuery(name = "XincoCoreDataT.findByDesignation", query = "SELECT x FROM XincoCoreDataT x WHERE x.designation = :designation"), @NamedQuery(name = "XincoCoreDataT.findByStatusNumber", query = "SELECT x FROM XincoCoreDataT x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreDataT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private int id;
    @Basic(optional = false)
    @Column(name = "xinco_core_node_id", nullable = false)
    private int xincoCoreNodeId;
    @Basic(optional = false)
    @Column(name = "xinco_core_language_id", nullable = false)
    private int xincoCoreLanguageId;
    @Basic(optional = false)
    @Column(name = "xinco_core_data_type_id", nullable = false)
    private int xincoCoreDataTypeId;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 255)
    private String designation;
    @Basic(optional = false)
    @Column(name = "status_number", nullable = false)
    private int statusNumber;

    public XincoCoreDataT() {
    }

    public XincoCoreDataT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreDataT(Integer recordId, int id, int xincoCoreNodeId, int xincoCoreLanguageId, int xincoCoreDataTypeId, String designation, int statusNumber) {
        this.recordId = recordId;
        this.id = id;
        this.xincoCoreNodeId = xincoCoreNodeId;
        this.xincoCoreLanguageId = xincoCoreLanguageId;
        this.xincoCoreDataTypeId = xincoCoreDataTypeId;
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

    public int getXincoCoreNodeId() {
        return xincoCoreNodeId;
    }

    public void setXincoCoreNodeId(int xincoCoreNodeId) {
        this.xincoCoreNodeId = xincoCoreNodeId;
    }

    public int getXincoCoreLanguageId() {
        return xincoCoreLanguageId;
    }

    public void setXincoCoreLanguageId(int xincoCoreLanguageId) {
        this.xincoCoreLanguageId = xincoCoreLanguageId;
    }

    public int getXincoCoreDataTypeId() {
        return xincoCoreDataTypeId;
    }

    public void setXincoCoreDataTypeId(int xincoCoreDataTypeId) {
        this.xincoCoreDataTypeId = xincoCoreDataTypeId;
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
        
        if (!(object instanceof XincoCoreDataT)) {
            return false;
        }
        XincoCoreDataT other = (XincoCoreDataT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataT[recordId=" + recordId + "]";
    }

}
