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
 * Name: XincoDependencyBehavior
 * 
 * Description: XincoDependencyBehavior JPA class
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
@Table(name = "xinco_dependency_behavior", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"designation"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoDependencyBehavior.findAll", query = "SELECT x FROM XincoDependencyBehavior x"),
    @NamedQuery(name = "XincoDependencyBehavior.findById", query = "SELECT x FROM XincoDependencyBehavior x WHERE x.id = :id"),
    @NamedQuery(name = "XincoDependencyBehavior.findByDesignation", query = "SELECT x FROM XincoDependencyBehavior x WHERE x.designation = :designation"),
    @NamedQuery(name = "XincoDependencyBehavior.findByDescription", query = "SELECT x FROM XincoDependencyBehavior x WHERE x.description = :description")})
public class XincoDependencyBehavior implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "XincoDependencyBehaviorGen")
    @TableGenerator(name = "XincoDependencyBehaviorGen", table = "xinco_id",
    pkColumnName = "tablename",
    valueColumnName = "last_id",
    pkColumnValue = "xinco_dependency_behavior",
    allocationSize = 1,
    initialValue = 1000)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "designation")
    private String designation;
    @Size(max = 45)
    @Column(name = "description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoDependencyBehavior")
    private List<XincoDependencyType> xincoDependencyTypeList;

    public XincoDependencyBehavior() {
    }

    public XincoDependencyBehavior(Integer id) {
        this.id = id;
    }

    public XincoDependencyBehavior(Integer id, String designation) {
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
    public List<XincoDependencyType> getXincoDependencyTypeList() {
        return xincoDependencyTypeList;
    }

    public void setXincoDependencyTypeList(List<XincoDependencyType> xincoDependencyTypeList) {
        this.xincoDependencyTypeList = xincoDependencyTypeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoDependencyBehavior)) {
            return false;
        }
        XincoDependencyBehavior other = (XincoDependencyBehavior) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoDependencyBehavior[ id=" + id + " ]";
    }
}
