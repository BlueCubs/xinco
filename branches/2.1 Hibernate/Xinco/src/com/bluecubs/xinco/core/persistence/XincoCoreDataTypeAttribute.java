/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.persistence;

import com.bluecubs.xinco.core.server.persistence.audit.XincoAbstractAuditableObject;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Javier A. Ortiz
 */
@Entity
@Table(name = "xinco_core_data_type_attribute")
@NamedQueries({@NamedQuery(name = "XincoCoreDataTypeAttribute.findByXincoCoreDataTypeId", query = "SELECT x FROM XincoCoreDataTypeAttribute x WHERE x.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId = :xincoCoreDataTypeId"), @NamedQuery(name = "XincoCoreDataTypeAttribute.findByAttributeId", query = "SELECT x FROM XincoCoreDataTypeAttribute x WHERE x.xincoCoreDataTypeAttributePK.attributeId = :attributeId"), @NamedQuery(name = "XincoCoreDataTypeAttribute.findByDesignation", query = "SELECT x FROM XincoCoreDataTypeAttribute x WHERE x.designation = :designation"), @NamedQuery(name = "XincoCoreDataTypeAttribute.findByDataType", query = "SELECT x FROM XincoCoreDataTypeAttribute x WHERE x.dataType = :dataType"), @NamedQuery(name = "XincoCoreDataTypeAttribute.findBySize", query = "SELECT x FROM XincoCoreDataTypeAttribute x WHERE x.size = :size")})
public class XincoCoreDataTypeAttribute extends XincoAbstractAuditableObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoCoreDataTypeAttributePK xincoCoreDataTypeAttributePK;
    @Column(name = "designation", nullable = false)
    private String designation;
    @Column(name = "data_type", nullable = false)
    private String dataType;
    @Column(name = "size", nullable = false)
    private int attribSize;

    public XincoCoreDataTypeAttribute() {
    }

    public XincoCoreDataTypeAttribute(XincoCoreDataTypeAttributePK xincoCoreDataTypeAttributePK) {
        this.xincoCoreDataTypeAttributePK = xincoCoreDataTypeAttributePK;
    }

    public XincoCoreDataTypeAttribute(XincoCoreDataTypeAttributePK xincoCoreDataTypeAttributePK, String designation, String dataType, int size) {
        this.xincoCoreDataTypeAttributePK = xincoCoreDataTypeAttributePK;
        this.designation = designation;
        this.dataType = dataType;
        this.attribSize = size;
    }

    public XincoCoreDataTypeAttribute(int xincoCoreDataTypeId, int attributeId) {
        this.xincoCoreDataTypeAttributePK = new XincoCoreDataTypeAttributePK(xincoCoreDataTypeId, attributeId);
    }

    public XincoCoreDataTypeAttributePK getXincoCoreDataTypeAttributePK() {
        return xincoCoreDataTypeAttributePK;
    }

    public void setXincoCoreDataTypeAttributePK(XincoCoreDataTypeAttributePK xincoCoreDataTypeAttributePK) {
        this.xincoCoreDataTypeAttributePK = xincoCoreDataTypeAttributePK;
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
        return attribSize;
    }

    public void setSize(int size) {
        this.attribSize = size;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xincoCoreDataTypeAttributePK != null ? xincoCoreDataTypeAttributePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoCoreDataTypeAttribute)) {
            return false;
        }
        XincoCoreDataTypeAttribute other = (XincoCoreDataTypeAttribute) object;
        if ((this.xincoCoreDataTypeAttributePK == null && other.xincoCoreDataTypeAttributePK != null) || (this.xincoCoreDataTypeAttributePK != null && !this.xincoCoreDataTypeAttributePK.equals(other.xincoCoreDataTypeAttributePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.persistence.XincoCoreDataTypeAttribute[xincoCoreDataTypeAttributePK=" + xincoCoreDataTypeAttributePK + "]";
    }

}
