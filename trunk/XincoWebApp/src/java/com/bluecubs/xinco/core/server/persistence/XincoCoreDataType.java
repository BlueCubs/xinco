/*
 * Copyright 2011 blueCubs.com.
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
 * Name: XincoCoreDataType
 * 
 * Description: Audit Trail Table
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.server.AuditedEntityListener;
import com.bluecubs.xinco.core.server.XincoAuditedObject;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_data_type")
@EntityListeners(AuditedEntityListener.class)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreDataType.findAll", query = "SELECT x FROM XincoCoreDataType x"),
    @NamedQuery(name = "XincoCoreDataType.findById", query = "SELECT x FROM XincoCoreDataType x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreDataType.findByDesignation", query = "SELECT x FROM XincoCoreDataType x WHERE x.designation = :designation"),
    @NamedQuery(name = "XincoCoreDataType.findByDescription", query = "SELECT x FROM XincoCoreDataType x WHERE x.description = :description")})
public class XincoCoreDataType extends XincoAuditedObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "XincoCoreDataTypeGen")
    @TableGenerator(name = "XincoCoreDataTypeGen", table = "xinco_id",
    pkColumnName = "tablename",
    valueColumnName = "last_id",
    pkColumnValue = "xinco_core_data_type",
    allocationSize = 1,
    initialValue = 1000)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "designation")
    private String designation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreDataType")
    private List<XincoCoreDataTypeAttribute> xincoCoreDataTypeAttributeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreDataType")
    private List<XincoCoreData> xincoCoreDataList;

    public XincoCoreDataType() {
    }

    public XincoCoreDataType(Integer id) {
        this.id = id;
    }

    public XincoCoreDataType(Integer id, String designation, String description) {
        this.id = id;
        this.designation = designation;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<XincoCoreDataTypeAttribute> getXincoCoreDataTypeAttributeList() {
        return xincoCoreDataTypeAttributeList;
    }

    public void setXincoCoreDataTypeAttributeList(List<XincoCoreDataTypeAttribute> xincoCoreDataTypeAttributeList) {
        this.xincoCoreDataTypeAttributeList = xincoCoreDataTypeAttributeList;
    }

    @XmlTransient
    public List<XincoCoreData> getXincoCoreDataList() {
        return xincoCoreDataList;
    }

    public void setXincoCoreDataList(List<XincoCoreData> xincoCoreDataList) {
        this.xincoCoreDataList = xincoCoreDataList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoCoreDataType)) {
            return false;
        }
        XincoCoreDataType other = (XincoCoreDataType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataType[ id=" + id + " ]";
    }
}
