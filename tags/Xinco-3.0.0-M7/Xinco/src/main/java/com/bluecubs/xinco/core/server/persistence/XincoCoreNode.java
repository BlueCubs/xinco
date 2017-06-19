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
 * Name: XincoCoreNode
 * 
 * Description: XincoCoreNode JPA class
 * 
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
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
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_node")
@EntityListeners(AuditedEntityListener.class)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreNode.findAll", query = "SELECT x FROM XincoCoreNode x"),
    @NamedQuery(name = "XincoCoreNode.findById", query = "SELECT x FROM XincoCoreNode x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreNode.findByDesignation", query = "SELECT x FROM XincoCoreNode x WHERE x.designation = :designation"),
    @NamedQuery(name = "XincoCoreNode.findByStatusNumber", query = "SELECT x FROM XincoCoreNode x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreNode extends XincoAuditedObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "XincoCoreNodeGen")
    @TableGenerator(name = "XincoCoreNodeGen", table = "xinco_id",
    pkColumnName = "tablename",
    valueColumnName = "last_id",
    pkColumnValue = "xinco_core_node",
    initialValue = 1000,
    allocationSize = 1)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "designation")
    private String designation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status_number")
    private int statusNumber;
    @JoinColumn(name = "xinco_core_language_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private XincoCoreLanguage xincoCoreLanguage;
    @OneToMany(mappedBy = "xincoCoreNode")
    private List<XincoCoreNode> xincoCoreNodeList;
    @JoinColumn(name = "xinco_core_node_id", referencedColumnName = "id")
    @ManyToOne
    private XincoCoreNode xincoCoreNode;
    @OneToMany(mappedBy = "xincoCoreNode")
    private List<XincoCoreAce> xincoCoreAceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreNode")
    private List<XincoCoreData> xincoCoreDataList;

    public XincoCoreNode() {
    }

    public XincoCoreNode(Integer id) {
        this.id = id;
    }

    public XincoCoreNode(Integer id, String designation, int statusNumber) {
        this.id = id;
        this.designation = designation;
        this.statusNumber = statusNumber;
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

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    public XincoCoreLanguage getXincoCoreLanguage() {
        return xincoCoreLanguage;
    }

    public void setXincoCoreLanguage(XincoCoreLanguage xincoCoreLanguage) {
        this.xincoCoreLanguage = xincoCoreLanguage;
    }

    @XmlTransient
    public List<XincoCoreNode> getXincoCoreNodeList() {
        return xincoCoreNodeList;
    }

    public void setXincoCoreNodeList(List<XincoCoreNode> xincoCoreNodeList) {
        this.xincoCoreNodeList = xincoCoreNodeList;
    }

    public XincoCoreNode getXincoCoreNode() {
        return xincoCoreNode;
    }

    public void setXincoCoreNode(XincoCoreNode xincoCoreNode) {
        this.xincoCoreNode = xincoCoreNode;
    }

    @XmlTransient
    public List<XincoCoreAce> getXincoCoreAceList() {
        return xincoCoreAceList;
    }

    public void setXincoCoreAceList(List<XincoCoreAce> xincoCoreAceList) {
        this.xincoCoreAceList = xincoCoreAceList;
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
        if (!(object instanceof XincoCoreNode)) {
            return false;
        }
        XincoCoreNode other = (XincoCoreNode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreNode[ id=" + id + " ]";
    }
}
