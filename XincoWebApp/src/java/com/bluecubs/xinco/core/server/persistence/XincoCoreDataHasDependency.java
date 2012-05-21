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
 * Name: XincoCoreDataHasDependency
 * 
 * Description: Table entity
 * 
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_data_has_dependency")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreDataHasDependency.findAll", query = "SELECT x FROM XincoCoreDataHasDependency x"),
    @NamedQuery(name = "XincoCoreDataHasDependency.findByXincoCoreDataParentId", query = "SELECT x FROM XincoCoreDataHasDependency x WHERE x.xincoCoreDataHasDependencyPK.xincoCoreDataParentId = :xincoCoreDataParentId"),
    @NamedQuery(name = "XincoCoreDataHasDependency.findByXincoCoreDataChildrenId", query = "SELECT x FROM XincoCoreDataHasDependency x WHERE x.xincoCoreDataHasDependencyPK.xincoCoreDataChildrenId = :xincoCoreDataChildrenId"),
    @NamedQuery(name = "XincoCoreDataHasDependency.findByDependencyTypeId", query = "SELECT x FROM XincoCoreDataHasDependency x WHERE x.xincoCoreDataHasDependencyPK.dependencyTypeId = :dependencyTypeId")})
public class XincoCoreDataHasDependency implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoCoreDataHasDependencyPK xincoCoreDataHasDependencyPK;
    @JoinColumn(name = "dependency_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private XincoDependencyType xincoDependencyType;
    @JoinColumn(name = "xinco_core_data_children_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private XincoCoreData xincoCoreData;
    @JoinColumn(name = "xinco_core_data_parent_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private XincoCoreData xincoCoreData1;

    public XincoCoreDataHasDependency() {
    }

    public XincoCoreDataHasDependency(XincoCoreDataHasDependencyPK xincoCoreDataHasDependencyPK) {
        this.xincoCoreDataHasDependencyPK = xincoCoreDataHasDependencyPK;
    }

    public XincoCoreDataHasDependency(int xincoCoreDataParentId, int xincoCoreDataChildrenId, int dependencyTypeId) {
        this.xincoCoreDataHasDependencyPK = new XincoCoreDataHasDependencyPK(xincoCoreDataParentId, xincoCoreDataChildrenId, dependencyTypeId);
    }

    public XincoCoreDataHasDependencyPK getXincoCoreDataHasDependencyPK() {
        return xincoCoreDataHasDependencyPK;
    }

    public void setXincoCoreDataHasDependencyPK(XincoCoreDataHasDependencyPK xincoCoreDataHasDependencyPK) {
        this.xincoCoreDataHasDependencyPK = xincoCoreDataHasDependencyPK;
    }

    public XincoDependencyType getXincoDependencyType() {
        return xincoDependencyType;
    }

    public void setXincoDependencyType(XincoDependencyType xincoDependencyType) {
        this.xincoDependencyType = xincoDependencyType;
    }

    public XincoCoreData getXincoCoreData() {
        return xincoCoreData;
    }

    public void setXincoCoreData(XincoCoreData xincoCoreData) {
        this.xincoCoreData = xincoCoreData;
    }

    public XincoCoreData getXincoCoreData1() {
        return xincoCoreData1;
    }

    public void setXincoCoreData1(XincoCoreData xincoCoreData1) {
        this.xincoCoreData1 = xincoCoreData1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xincoCoreDataHasDependencyPK != null ? xincoCoreDataHasDependencyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoCoreDataHasDependency)) {
            return false;
        }
        XincoCoreDataHasDependency other = (XincoCoreDataHasDependency) object;
        if ((this.xincoCoreDataHasDependencyPK == null && other.xincoCoreDataHasDependencyPK != null) || (this.xincoCoreDataHasDependencyPK != null && !this.xincoCoreDataHasDependencyPK.equals(other.xincoCoreDataHasDependencyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency[ xincoCoreDataHasDependencyPK=" + xincoCoreDataHasDependencyPK + " ]";
    }
}
