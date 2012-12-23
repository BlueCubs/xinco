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
 * Name: XincoCoreDataTypeAttributeT
 * 
 * Description: Audit Trail Table
 * 
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_data_type_attribute_t")
@XmlRootElement
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
    @NotNull
    @Column(name = "record_id")
    private Integer recordId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "xinco_core_data_type_id")
    private int xincoCoreDataTypeId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "attribute_id")
    private int attributeId;
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

    public XincoCoreDataTypeAttributeT() {
    }

    public XincoCoreDataTypeAttributeT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreDataTypeAttributeT(Integer recordId, int xincoCoreDataTypeId, int attributeId, String designation, String dataType, int attrSize) {
        this.recordId = recordId;
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
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttributeT[ recordId=" + recordId + " ]";
    }
}
