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
 * Name: XincoCoreUserModifiedRecord
 *
 * Description: Audot Trail Table
 *
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
 *
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/** @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com */
@Entity
@Table(name = "xinco_core_user_modified_record")
@XmlRootElement
@NamedQueries({
  @NamedQuery(
      name = "XincoCoreUserModifiedRecord.findAll",
      query = "SELECT x FROM XincoCoreUserModifiedRecord x"),
  @NamedQuery(
      name = "XincoCoreUserModifiedRecord.findById",
      query =
          "SELECT x FROM XincoCoreUserModifiedRecord x WHERE x.xincoCoreUserModifiedRecordPK.id = :id"),
  @NamedQuery(
      name = "XincoCoreUserModifiedRecord.findByRecordId",
      query =
          "SELECT x FROM XincoCoreUserModifiedRecord x WHERE x.xincoCoreUserModifiedRecordPK.recordId = :recordId"),
  @NamedQuery(
      name = "XincoCoreUserModifiedRecord.findByModTime",
      query = "SELECT x FROM XincoCoreUserModifiedRecord x WHERE x.modTime = :modTime"),
  @NamedQuery(
      name = "XincoCoreUserModifiedRecord.findByModReason",
      query = "SELECT x FROM XincoCoreUserModifiedRecord x WHERE x.modReason = :modReason")
})
public class XincoCoreUserModifiedRecord implements Serializable {

  private static final long serialVersionUID = 1L;
  @EmbeddedId protected XincoCoreUserModifiedRecordPK xincoCoreUserModifiedRecordPK;

  @Basic(optional = false)
  @NotNull
  @Column(name = "mod_Time")
  @Temporal(TIMESTAMP)
  private Date modTime;

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "mod_Reason")
  private String modReason;

  @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
  @ManyToOne(optional = false)
  private XincoCoreUser xincoCoreUser;

  public XincoCoreUserModifiedRecord() {}

  public XincoCoreUserModifiedRecord(XincoCoreUserModifiedRecordPK xincoCoreUserModifiedRecordPK) {
    this.xincoCoreUserModifiedRecordPK = xincoCoreUserModifiedRecordPK;
  }

  public XincoCoreUserModifiedRecord(
      XincoCoreUserModifiedRecordPK xincoCoreUserModifiedRecordPK, Date modTime, String modReason) {
    this.xincoCoreUserModifiedRecordPK = xincoCoreUserModifiedRecordPK;
    this.modTime = modTime;
    this.modReason = modReason;
  }

  public XincoCoreUserModifiedRecord(int id, int recordId) {
    this.xincoCoreUserModifiedRecordPK = new XincoCoreUserModifiedRecordPK(id, recordId);
  }

  public XincoCoreUserModifiedRecordPK getXincoCoreUserModifiedRecordPK() {
    return xincoCoreUserModifiedRecordPK;
  }

  public void setXincoCoreUserModifiedRecordPK(
      XincoCoreUserModifiedRecordPK xincoCoreUserModifiedRecordPK) {
    this.xincoCoreUserModifiedRecordPK = xincoCoreUserModifiedRecordPK;
  }

  public Date getModTime() {
    return modTime;
  }

  public void setModTime(Date modTime) {
    this.modTime = modTime;
  }

  public String getModReason() {
    return modReason;
  }

  public void setModReason(String modReason) {
    this.modReason = modReason;
  }

  public XincoCoreUser getXincoCoreUser() {
    return xincoCoreUser;
  }

  public void setXincoCoreUser(XincoCoreUser xincoCoreUser) {
    this.xincoCoreUser = xincoCoreUser;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (xincoCoreUserModifiedRecordPK != null ? xincoCoreUserModifiedRecordPK.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof XincoCoreUserModifiedRecord)) {
      return false;
    }
    XincoCoreUserModifiedRecord other = (XincoCoreUserModifiedRecord) object;
    if ((this.xincoCoreUserModifiedRecordPK == null && other.xincoCoreUserModifiedRecordPK != null)
        || (this.xincoCoreUserModifiedRecordPK != null
            && !this.xincoCoreUserModifiedRecordPK.equals(other.xincoCoreUserModifiedRecordPK))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.bluecubs.xinco.core.server.persistence.XincoCoreUserModifiedRecord[ xincoCoreUserModifiedRecordPK="
        + xincoCoreUserModifiedRecordPK
        + " ]";
  }
}
