/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Embeddable
public class XincoCoreDataTypeAttributePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "xinco_core_data_type_id", nullable = false)
    private int xincoCoreDataTypeId;
    @Basic(optional = false)
    @Column(name = "attribute_id", nullable = false)
    private int attributeId;

    public XincoCoreDataTypeAttributePK() {
    }

    public XincoCoreDataTypeAttributePK(int xincoCoreDataTypeId, int attributeId) {
        this.xincoCoreDataTypeId = xincoCoreDataTypeId;
        this.attributeId = attributeId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) xincoCoreDataTypeId;
        hash += (int) attributeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof XincoCoreDataTypeAttributePK)) {
            return false;
        }
        XincoCoreDataTypeAttributePK other = (XincoCoreDataTypeAttributePK) object;
        if (this.xincoCoreDataTypeId != other.xincoCoreDataTypeId) {
            return false;
        }
        if (this.attributeId != other.attributeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttributePK[xincoCoreDataTypeId=" + xincoCoreDataTypeId + ", attributeId=" + attributeId + "]";
    }

}