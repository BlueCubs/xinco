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
 * Name: XincoCoreDataHasDependencyT
 * 
 * Description: Table entity
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_data_has_dependency_t")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreDataHasDependencyT.findAll", query = "SELECT x FROM XincoCoreDataHasDependencyT x"),
    @NamedQuery(name = "XincoCoreDataHasDependencyT.findByRecordId", query = "SELECT x FROM XincoCoreDataHasDependencyT x WHERE x.recordId = :recordId"),
    @NamedQuery(name = "XincoCoreDataHasDependencyT.findByXincoCoreDataParentId", query = "SELECT x FROM XincoCoreDataHasDependencyT x WHERE x.xincoCoreDataParentId = :xincoCoreDataParentId"),
    @NamedQuery(name = "XincoCoreDataHasDependencyT.findByXincoCoreDataChildrenId", query = "SELECT x FROM XincoCoreDataHasDependencyT x WHERE x.xincoCoreDataChildrenId = :xincoCoreDataChildrenId"),
    @NamedQuery(name = "XincoCoreDataHasDependencyT.findByDependencyTypeId", query = "SELECT x FROM XincoCoreDataHasDependencyT x WHERE x.dependencyTypeId = :dependencyTypeId")})
public class XincoCoreDataHasDependencyT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "record_id")
    private Integer recordId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "xinco_core_data_parent_id")
    private int xincoCoreDataParentId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "xinco_core_data_children_id")
    private int xincoCoreDataChildrenId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dependency_type_id")
    private int dependencyTypeId;

    public XincoCoreDataHasDependencyT() {
    }

    public XincoCoreDataHasDependencyT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreDataHasDependencyT(Integer recordId, int xincoCoreDataParentId, int xincoCoreDataChildrenId, int dependencyTypeId) {
        this.recordId = recordId;
        this.xincoCoreDataParentId = xincoCoreDataParentId;
        this.xincoCoreDataChildrenId = xincoCoreDataChildrenId;
        this.dependencyTypeId = dependencyTypeId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public int getXincoCoreDataParentId() {
        return xincoCoreDataParentId;
    }

    public void setXincoCoreDataParentId(int xincoCoreDataParentId) {
        this.xincoCoreDataParentId = xincoCoreDataParentId;
    }

    public int getXincoCoreDataChildrenId() {
        return xincoCoreDataChildrenId;
    }

    public void setXincoCoreDataChildrenId(int xincoCoreDataChildrenId) {
        this.xincoCoreDataChildrenId = xincoCoreDataChildrenId;
    }

    public int getDependencyTypeId() {
        return dependencyTypeId;
    }

    public void setDependencyTypeId(int dependencyTypeId) {
        this.dependencyTypeId = dependencyTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoCoreDataHasDependencyT)) {
            return false;
        }
        XincoCoreDataHasDependencyT other = (XincoCoreDataHasDependencyT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependencyT[ recordId=" + recordId + " ]";
    }
    
}
