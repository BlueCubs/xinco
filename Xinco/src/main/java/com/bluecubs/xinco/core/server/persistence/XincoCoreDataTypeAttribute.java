/*
 * Copyright 2012 blueCubs.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 * 
 * Name: XincoCoreDataTypeAttribute
 * 
 * Description: Audit Trail Table
 * 
 * Original Author: Javier A. Ortiz Bultron javier.ortiz.78@gmail.com Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.server.AuditedEntityListener;
import com.bluecubs.xinco.core.server.XincoAuditedObject;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
@Entity
@Table(name = "xinco_core_data_type_attribute")
@EntityListeners(AuditedEntityListener.class)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreDataTypeAttribute.findAll", query = "SELECT x FROM XincoCoreDataTypeAttribute x"),
    @NamedQuery(name = "XincoCoreDataTypeAttribute.findByXincoCoreDataTypeId", query = "SELECT x FROM XincoCoreDataTypeAttribute x WHERE x.xincoCoreDataTypeAttributePK.xincoCoreDataTypeId = :xincoCoreDataTypeId"),
    @NamedQuery(name = "XincoCoreDataTypeAttribute.findByAttributeId", query = "SELECT x FROM XincoCoreDataTypeAttribute x WHERE x.xincoCoreDataTypeAttributePK.attributeId = :attributeId"),
    @NamedQuery(name = "XincoCoreDataTypeAttribute.findByDesignation", query = "SELECT x FROM XincoCoreDataTypeAttribute x WHERE x.designation = :designation"),
    @NamedQuery(name = "XincoCoreDataTypeAttribute.findByDataType", query = "SELECT x FROM XincoCoreDataTypeAttribute x WHERE x.dataType = :dataType"),
    @NamedQuery(name = "XincoCoreDataTypeAttribute.findByAttrSize", query = "SELECT x FROM XincoCoreDataTypeAttribute x WHERE x.attrSize = :attrSize")})
public class XincoCoreDataTypeAttribute extends XincoAuditedObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoCoreDataTypeAttributePK xincoCoreDataTypeAttributePK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "designation")
    private String designation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "data_type")
    private String dataType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "attr_size")
    private int attrSize;
    @JoinColumn(name = "xinco_core_data_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private XincoCoreDataType xincoCoreDataType;

    public XincoCoreDataTypeAttribute() {
    }

    public XincoCoreDataTypeAttribute(XincoCoreDataTypeAttributePK xincoCoreDataTypeAttributePK) {
        this.xincoCoreDataTypeAttributePK = xincoCoreDataTypeAttributePK;
    }

    public XincoCoreDataTypeAttribute(XincoCoreDataTypeAttributePK xincoCoreDataTypeAttributePK, String designation, String dataType, int attrSize) {
        this.xincoCoreDataTypeAttributePK = xincoCoreDataTypeAttributePK;
        this.designation = designation;
        this.dataType = dataType;
        this.attrSize = attrSize;
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

    public int getAttrSize() {
        return attrSize;
    }

    public void setAttrSize(int attrSize) {
        this.attrSize = attrSize;
    }

    public XincoCoreDataType getXincoCoreDataType() {
        return xincoCoreDataType;
    }

    public void setXincoCoreDataType(XincoCoreDataType xincoCoreDataType) {
        this.xincoCoreDataType = xincoCoreDataType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xincoCoreDataTypeAttributePK != null ? xincoCoreDataTypeAttributePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
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
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute[ xincoCoreDataTypeAttributePK=" + xincoCoreDataTypeAttributePK + " ]";
    }
}
