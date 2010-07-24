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
@Table(name = "xinco_core_data_type_attribute_t")
@NamedQueries({
    @NamedQuery(name = "XincoCoreDataTypeAttributeT.findAll", query = "SELECT x FROM XincoCoreDataTypeAttributeT x"),
    @NamedQuery(name = "XincoCoreDataTypeAttributeT.findByRecordId", query = "SELECT x FROM XincoCoreDataTypeAttributeT x WHERE x.recordId = :recordId"),
    @NamedQuery(name = "XincoCoreDataTypeAttributeT.findByXincoCoreDataTypeId", query = "SELECT x FROM XincoCoreDataTypeAttributeT x WHERE x.xincoCoreDataTypeId = :xincoCoreDataTypeId"),
    @NamedQuery(name = "XincoCoreDataTypeAttributeT.findByAttributeId", query = "SELECT x FROM XincoCoreDataTypeAttributeT x WHERE x.attributeId = :attributeId"),
    @NamedQuery(name = "XincoCoreDataTypeAttributeT.findByDesignation", query = "SELECT x FROM XincoCoreDataTypeAttributeT x WHERE x.designation = :designation"),
    @NamedQuery(name = "XincoCoreDataTypeAttributeT.findByDataType", query = "SELECT x FROM XincoCoreDataTypeAttributeT x WHERE x.dataType = :dataType"),
    @NamedQuery(name = "XincoCoreDataTypeAttributeT.findByAttrSize", query = "SELECT x FROM XincoCoreDataTypeAttributeT x WHERE x.attrSize = :attrSize")})
public class XincoCoreDataTypeAttributeT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "XCDTRECORDIDKEYGEN")
    @TableGenerator(name = "XCDTRECORDIDKEYGEN", table = "xinco_id",
    pkColumnName = "tablename", valueColumnName = "last_id",
    pkColumnValue = "xinco_core_user_modified_record", initialValue = 1, allocationSize = 1)
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Basic(optional = false)
    @Column(name = "xinco_core_data_type_id", nullable = false)
    private int xincoCoreDataTypeId;
    @Basic(optional = false)
    @Column(name = "attribute_id", nullable = false)
    private int attributeId;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 255)
    private String designation;
    @Basic(optional = false)
    @Column(name = "data_type", nullable = false, length = 255)
    private String dataType;
    @Basic(optional = false)
    @Column(name = "attr_size", nullable = false)
    private int attrSize;

    public XincoCoreDataTypeAttributeT() {
    }

    public XincoCoreDataTypeAttributeT(int xincoCoreDataTypeId, int attributeId, String designation, String dataType, int attrSize) {
        this.xincoCoreDataTypeId = xincoCoreDataTypeId;
        this.attributeId = attributeId;
        this.designation = designation;
        this.dataType = dataType;
        this.attrSize = attrSize;
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

    public int getAttrSize() {
        return attrSize;
    }

    public void setAttrSize(int attrSize) {
        this.attrSize = attrSize;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
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
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttributeT[recordId=" + recordId + "]";
    }
}
