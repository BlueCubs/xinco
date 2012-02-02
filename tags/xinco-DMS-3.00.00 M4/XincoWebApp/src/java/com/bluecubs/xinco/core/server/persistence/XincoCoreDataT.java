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
 * Name: XincoCoreDataT
 * 
 * Description: Audit Trail Table
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
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
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_data_t")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreDataT.findAll", query = "SELECT x FROM XincoCoreDataT x"),
    @NamedQuery(name = "XincoCoreDataT.findByRecordId", query = "SELECT x FROM XincoCoreDataT x WHERE x.recordId = :recordId"),
    @NamedQuery(name = "XincoCoreDataT.findById", query = "SELECT x FROM XincoCoreDataT x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreDataT.findByXincoCoreNodeId", query = "SELECT x FROM XincoCoreDataT x WHERE x.xincoCoreNodeId = :xincoCoreNodeId"),
    @NamedQuery(name = "XincoCoreDataT.findByXincoCoreLanguageId", query = "SELECT x FROM XincoCoreDataT x WHERE x.xincoCoreLanguageId = :xincoCoreLanguageId"),
    @NamedQuery(name = "XincoCoreDataT.findByXincoCoreDataTypeId", query = "SELECT x FROM XincoCoreDataT x WHERE x.xincoCoreDataTypeId = :xincoCoreDataTypeId"),
    @NamedQuery(name = "XincoCoreDataT.findByDesignation", query = "SELECT x FROM XincoCoreDataT x WHERE x.designation = :designation"),
    @NamedQuery(name = "XincoCoreDataT.findByStatusNumber", query = "SELECT x FROM XincoCoreDataT x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreDataT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "record_id")
    private Integer recordId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "xinco_core_node_id")
    private int xincoCoreNodeId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "xinco_core_language_id")
    private int xincoCoreLanguageId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "xinco_core_data_type_id")
    private int xincoCoreDataTypeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "designation")
    private String designation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status_number")
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
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataT[ recordId=" + recordId + " ]";
    }
    
}
