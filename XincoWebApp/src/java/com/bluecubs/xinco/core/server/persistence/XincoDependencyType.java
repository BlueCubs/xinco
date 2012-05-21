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
 * Name: XincoDependencyType
 * 
 * Description: XincoDependencyType JPA class
 * 
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_dependency_type", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"designation"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoDependencyType.findAll", query = "SELECT x FROM XincoDependencyType x"),
    @NamedQuery(name = "XincoDependencyType.findById", query = "SELECT x FROM XincoDependencyType x WHERE x.id = :id"),
    @NamedQuery(name = "XincoDependencyType.findByDesignation", query = "SELECT x FROM XincoDependencyType x WHERE x.designation = :designation"),
    @NamedQuery(name = "XincoDependencyType.findByDescription", query = "SELECT x FROM XincoDependencyType x WHERE x.description = :description")})
public class XincoDependencyType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "XincoDependencyTypeGen")
    @TableGenerator(name = "XincoDependencyTypeGen", table = "xinco_id",
    pkColumnName = "tablename",
    valueColumnName = "last_id",
    pkColumnValue = "xinco_dependency_type",
    allocationSize = 1,
    initialValue = 1000)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "designation")
    private String designation;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoDependencyType")
    private List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyList;
    @JoinColumn(name = "xinco_dependency_behavior_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private XincoDependencyBehavior xincoDependencyBehavior;

    public XincoDependencyType() {
    }

    public XincoDependencyType(Integer id) {
        this.id = id;
    }

    public XincoDependencyType(Integer id, String designation) {
        this.id = id;
        this.designation = designation;
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
    public List<XincoCoreDataHasDependency> getXincoCoreDataHasDependencyList() {
        return xincoCoreDataHasDependencyList;
    }

    public void setXincoCoreDataHasDependencyList(List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyList) {
        this.xincoCoreDataHasDependencyList = xincoCoreDataHasDependencyList;
    }

    public XincoDependencyBehavior getXincoDependencyBehavior() {
        return xincoDependencyBehavior;
    }

    public void setXincoDependencyBehavior(XincoDependencyBehavior xincoDependencyBehavior) {
        this.xincoDependencyBehavior = xincoDependencyBehavior;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoDependencyType)) {
            return false;
        }
        XincoDependencyType other = (XincoDependencyType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoDependencyType[ id=" + id + " ]";
    }
}
