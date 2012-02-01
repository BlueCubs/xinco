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
 * Name: XincoDependencyTypeT
 * 
 * Description: Audot Trail Table
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
@Table(name = "xinco_dependency_type_t")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoDependencyTypeT.findAll", query = "SELECT x FROM XincoDependencyTypeT x"),
    @NamedQuery(name = "XincoDependencyTypeT.findByRecordId", query = "SELECT x FROM XincoDependencyTypeT x WHERE x.recordId = :recordId"),
    @NamedQuery(name = "XincoDependencyTypeT.findById", query = "SELECT x FROM XincoDependencyTypeT x WHERE x.id = :id"),
    @NamedQuery(name = "XincoDependencyTypeT.findByXincoDependencyBehaviorId", query = "SELECT x FROM XincoDependencyTypeT x WHERE x.xincoDependencyBehaviorId = :xincoDependencyBehaviorId"),
    @NamedQuery(name = "XincoDependencyTypeT.findByDesignation", query = "SELECT x FROM XincoDependencyTypeT x WHERE x.designation = :designation"),
    @NamedQuery(name = "XincoDependencyTypeT.findByDescription", query = "SELECT x FROM XincoDependencyTypeT x WHERE x.description = :description")})
public class XincoDependencyTypeT implements Serializable {
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
    @Column(name = "xinco_dependency_behavior_id")
    private int xincoDependencyBehaviorId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "designation")
    private String designation;
    @Size(max = 255)
    @Column(name = "description")
    private String description;

    public XincoDependencyTypeT() {
    }

    public XincoDependencyTypeT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoDependencyTypeT(Integer recordId, int id, int xincoDependencyBehaviorId, String designation) {
        this.recordId = recordId;
        this.id = id;
        this.xincoDependencyBehaviorId = xincoDependencyBehaviorId;
        this.designation = designation;
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

    public int getXincoDependencyBehaviorId() {
        return xincoDependencyBehaviorId;
    }

    public void setXincoDependencyBehaviorId(int xincoDependencyBehaviorId) {
        this.xincoDependencyBehaviorId = xincoDependencyBehaviorId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoDependencyTypeT)) {
            return false;
        }
        XincoDependencyTypeT other = (XincoDependencyTypeT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoDependencyTypeT[ recordId=" + recordId + " ]";
    }
    
}
