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
 * Name: XincoCoreLanguage
 * 
 * Description: XincoCoreLanguage JPA class
 * 
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.server.AuditedEntityListener;
import com.bluecubs.xinco.core.server.XincoAuditedObject;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.TABLE;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com
 */
@Entity
@EntityListeners(AuditedEntityListener.class)
@Table(name = "xinco_core_language", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"designation"}),
    @UniqueConstraint(columnNames = {"sign"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreLanguage.findAll", query = "SELECT x FROM XincoCoreLanguage x"),
    @NamedQuery(name = "XincoCoreLanguage.findById", query = "SELECT x FROM XincoCoreLanguage x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreLanguage.findBySign", query = "SELECT x FROM XincoCoreLanguage x WHERE x.sign = :sign"),
    @NamedQuery(name = "XincoCoreLanguage.findByDesignation", query = "SELECT x FROM XincoCoreLanguage x WHERE x.designation = :designation")})
public class XincoCoreLanguage extends XincoAuditedObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = TABLE, generator = "XincoCoreLanguageGen")
    @TableGenerator(name = "XincoCoreLanguageGen", table = "xinco_id",
    pkColumnName = "tablename",
    valueColumnName = "last_id",
    pkColumnValue = "xinco_core_language",
    allocationSize = 1,
    initialValue = 1_000)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sign")
    private String sign;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "designation")
    private String designation;
    @OneToMany(cascade = ALL, mappedBy = "xincoCoreLanguage")
    private List<XincoCoreNode> xincoCoreNodeList;
    @OneToMany(cascade = ALL, mappedBy = "xincoCoreLanguage")
    private List<XincoCoreData> xincoCoreDataList;

    public XincoCoreLanguage() {
    }

    public XincoCoreLanguage(Integer id) {
        this.id = id;
    }

    public XincoCoreLanguage(Integer id, String sign, String designation) {
        this.id = id;
        this.sign = sign;
        this.designation = designation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @XmlTransient
    public List<XincoCoreNode> getXincoCoreNodeList() {
        return xincoCoreNodeList;
    }

    public void setXincoCoreNodeList(List<XincoCoreNode> xincoCoreNodeList) {
        this.xincoCoreNodeList = xincoCoreNodeList;
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
        if (!(object instanceof XincoCoreLanguage)) {
            return false;
        }
        XincoCoreLanguage other = (XincoCoreLanguage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage[ id=" + id + " ]";
    }
}
