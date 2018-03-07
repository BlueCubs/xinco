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
 * Name: XincoCoreDataHasDependencyPK
 * 
 * Description: Table entity
 * 
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com
 */
@Embeddable
public class XincoCoreDataHasDependencyPK implements Serializable {

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

    public XincoCoreDataHasDependencyPK() {
    }

    public XincoCoreDataHasDependencyPK(int xincoCoreDataParentId, int xincoCoreDataChildrenId, int dependencyTypeId) {
        this.xincoCoreDataParentId = xincoCoreDataParentId;
        this.xincoCoreDataChildrenId = xincoCoreDataChildrenId;
        this.dependencyTypeId = dependencyTypeId;
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
        hash += (int) xincoCoreDataParentId;
        hash += (int) xincoCoreDataChildrenId;
        hash += (int) dependencyTypeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoCoreDataHasDependencyPK)) {
            return false;
        }
        XincoCoreDataHasDependencyPK other = (XincoCoreDataHasDependencyPK) object;
        if (this.xincoCoreDataParentId != other.xincoCoreDataParentId) {
            return false;
        }
        if (this.xincoCoreDataChildrenId != other.xincoCoreDataChildrenId) {
            return false;
        }
        if (this.dependencyTypeId != other.dependencyTypeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependencyPK[ xincoCoreDataParentId=" + xincoCoreDataParentId + ", xincoCoreDataChildrenId=" + xincoCoreDataChildrenId + ", dependencyTypeId=" + dependencyTypeId + " ]";
    }
}
