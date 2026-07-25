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
 * Name: XincoCoreData
 *
 * Description: Table entity
 *
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
 *
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.TABLE;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import org.hibernate.envers.Audited;

/**
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
@Audited
@Entity
@Table(name = "xinco_core_data")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "XincoCoreData.findAll", query = "SELECT x FROM XincoCoreData x"),
  @NamedQuery(
      name = "XincoCoreData.findById",
      query = "SELECT x FROM XincoCoreData x WHERE x.id = :id"),
  @NamedQuery(
      name = "XincoCoreData.findByDesignation",
      query = "SELECT x FROM XincoCoreData x WHERE x.designation = :designation"),
  @NamedQuery(
      name = "XincoCoreData.findByStatusNumber",
      query = "SELECT x FROM XincoCoreData x WHERE x.statusNumber = :statusNumber")
})
public class XincoCoreData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Basic(optional = false)
  @NotNull @GeneratedValue(strategy = TABLE, generator = "XincoCoreDataGen")
  @TableGenerator(
      name = "XincoCoreDataGen",
      table = "xinco_id",
      pkColumnName = "tablename",
      valueColumnName = "last_id",
      pkColumnValue = "xinco_core_data",
      allocationSize = 1,
      initialValue = 1_000)
  @Column(name = "id")
  private Integer id;

  @Basic(optional = false)
  @NotNull @Size(min = 1, max = 255) @Column(name = "designation")
  private String designation;

  /**
   * Status in the database (values 1–5).
   *
   * <p>Mapping (never use enum ordinals directly):
   *
   * <ul>
   *   <li>1 = OPEN
   *   <li>2 = LOCKED
   *   <li>3 = ARCHIVED
   *   <li>4 = CHECKED_OUT
   *   <li>5 = PUBLISHED
   * </ul>
   *
   * @see com.bluecubs.xinco.core.XincoDataStatus#toDbValue()
   * @see com.bluecubs.xinco.core.XincoDataStatus#fromDbValue(int)
   */
  @Basic(optional = false)
  @NotNull @Column(name = "status_number")
  private int statusNumber;

  @OneToMany(cascade = ALL, mappedBy = "xincoCoreData")
  private List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyList;

  @OneToMany(cascade = ALL, mappedBy = "xincoCoreData1")
  private List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyList1;

  @OneToMany(mappedBy = "xincoCoreData")
  private List<XincoCoreAce> xincoCoreAceList;

  @OneToMany(cascade = ALL, mappedBy = "xincoCoreData")
  private List<XincoCoreLog> xincoCoreLogList;

  @JoinColumn(name = "xinco_core_data_type_id", referencedColumnName = "id")
  @ManyToOne(optional = false)
  private XincoCoreDataType xincoCoreDataType;

  @JoinColumn(name = "xinco_core_language_id", referencedColumnName = "id")
  @ManyToOne(optional = false)
  private XincoCoreLanguage xincoCoreLanguage;

  @JoinColumn(name = "xinco_core_node_id", referencedColumnName = "id")
  @ManyToOne(optional = false)
  private XincoCoreNode xincoCoreNode;

  @OneToMany(cascade = ALL, mappedBy = "xincoCoreData")
  private List<XincoAddAttribute> xincoAddAttributeList;

  public XincoCoreData() {}

  public XincoCoreData(Integer id) {
    this.id = id;
  }

  public XincoCoreData(Integer id, String designation, int statusNumber) {
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

  @XmlTransient
  public List<XincoCoreDataHasDependency> getXincoCoreDataHasDependencyList() {
    return xincoCoreDataHasDependencyList;
  }

  public void setXincoCoreDataHasDependencyList(
      List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyList) {
    this.xincoCoreDataHasDependencyList = xincoCoreDataHasDependencyList;
  }

  @XmlTransient
  public List<XincoCoreDataHasDependency> getXincoCoreDataHasDependencyList1() {
    return xincoCoreDataHasDependencyList1;
  }

  public void setXincoCoreDataHasDependencyList1(
      List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyList1) {
    this.xincoCoreDataHasDependencyList1 = xincoCoreDataHasDependencyList1;
  }

  @XmlTransient
  public List<XincoCoreAce> getXincoCoreAceList() {
    return xincoCoreAceList;
  }

  public void setXincoCoreAceList(List<XincoCoreAce> xincoCoreAceList) {
    this.xincoCoreAceList = xincoCoreAceList;
  }

  @XmlTransient
  public List<XincoCoreLog> getXincoCoreLogList() {
    return xincoCoreLogList;
  }

  public void setXincoCoreLogList(List<XincoCoreLog> xincoCoreLogList) {
    this.xincoCoreLogList = xincoCoreLogList;
  }

  public XincoCoreDataType getXincoCoreDataType() {
    return xincoCoreDataType;
  }

  public void setXincoCoreDataType(XincoCoreDataType xincoCoreDataType) {
    this.xincoCoreDataType = xincoCoreDataType;
  }

  public XincoCoreLanguage getXincoCoreLanguage() {
    return xincoCoreLanguage;
  }

  public void setXincoCoreLanguage(XincoCoreLanguage xincoCoreLanguage) {
    this.xincoCoreLanguage = xincoCoreLanguage;
  }

  public XincoCoreNode getXincoCoreNode() {
    return xincoCoreNode;
  }

  public void setXincoCoreNode(XincoCoreNode xincoCoreNode) {
    this.xincoCoreNode = xincoCoreNode;
  }

  @XmlTransient
  public List<XincoAddAttribute> getXincoAddAttributeList() {
    return xincoAddAttributeList;
  }

  public void setXincoAddAttributeList(List<XincoAddAttribute> xincoAddAttributeList) {
    this.xincoAddAttributeList = xincoAddAttributeList;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof XincoCoreData)) {
      return false;
    }
    XincoCoreData other = (XincoCoreData) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.bluecubs.xinco.core.server.persistence.XincoCoreData[ id=" + id + " ]";
  }
}
