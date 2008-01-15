/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.persistence.audit;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Javier A. Ortiz
 */
@Entity
@Table(name = "xinco_core_data_type_attribute_t")
@NamedQueries({@NamedQuery(name = "XincoCoreDataTypeAttributeT.findByRecordId", query = "SELECT x FROM XincoCoreDataTypeAttributeT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoCoreDataTypeAttributeT.findByXincoCoreDataTypeId", query = "SELECT x FROM XincoCoreDataTypeAttributeT x WHERE x.xincoCoreDataTypeId = :xincoCoreDataTypeId"), @NamedQuery(name = "XincoCoreDataTypeAttributeT.findByAttributeId", query = "SELECT x FROM XincoCoreDataTypeAttributeT x WHERE x.attributeId = :attributeId"), @NamedQuery(name = "XincoCoreDataTypeAttributeT.findByDesignation", query = "SELECT x FROM XincoCoreDataTypeAttributeT x WHERE x.designation = :designation"), @NamedQuery(name = "XincoCoreDataTypeAttributeT.findByDataType", query = "SELECT x FROM XincoCoreDataTypeAttributeT x WHERE x.dataType = :dataType"), @NamedQuery(name = "XincoCoreDataTypeAttributeT.findBySize", query = "SELECT x FROM XincoCoreDataTypeAttributeT x WHERE x.size = :size")})
public class XincoCoreDataTypeAttributeT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Column(name = "xinco_core_data_type_id", nullable = false)
    private int xincoCoreDataTypeId;
    @Column(name = "attribute_id", nullable = false)
    private int attributeId;
    @Column(name = "designation", nullable = false)
    private String designation;
    @Column(name = "data_type", nullable = false)
    private String dataType;
    @Column(name = "size", nullable = false)
    private int size;

    public XincoCoreDataTypeAttributeT() {
    }

    public XincoCoreDataTypeAttributeT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreDataTypeAttributeT(Integer recordId, int xincoCoreDataTypeId, int attributeId, String designation, String dataType, int size) {
        this.recordId = recordId;
        this.xincoCoreDataTypeId = xincoCoreDataTypeId;
        this.attributeId = attributeId;
        this.designation = designation;
        this.dataType = dataType;
        this.size = size;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public int getXincoCoreDataTypeId() {
        return xincoCoreDataTypeId;
    }

    public void setXincoCoreDataTypeId(int xincoCoreDataTypeId) {
        this.xincoCoreDataTypeId = xincoCoreDataTypeId;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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
        if (!(object instanceof XincoCoreDataTypeAttributeT)) {
            return false;
        }
        XincoCoreDataTypeAttributeT other = (XincoCoreDataTypeAttributeT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.persistence.audit.XincoCoreDataTypeAttributeT[recordId=" + recordId + "]";
    }

}
