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
 * Name: XincoCoreLog
 *
 * Description: XincoCoreLog JPA class
 *
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
 *
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import static jakarta.persistence.GenerationType.TABLE;
import static jakarta.persistence.TemporalType.TIMESTAMP;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

/**
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
@Audited
@Entity
@Table(name = "xinco_core_log")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "XincoCoreLog.findAll", query = "SELECT x FROM XincoCoreLog x"),
  @NamedQuery(
      name = "XincoCoreLog.findById",
      query = "SELECT x FROM XincoCoreLog x WHERE x.id = :id"),
  @NamedQuery(
      name = "XincoCoreLog.findByOpCode",
      query = "SELECT x FROM XincoCoreLog x WHERE x.opCode = :opCode"),
  @NamedQuery(
      name = "XincoCoreLog.findByOpDatetime",
      query = "SELECT x FROM XincoCoreLog x WHERE x.opDatetime = :opDatetime"),
  @NamedQuery(
      name = "XincoCoreLog.findByOpDescription",
      query = "SELECT x FROM XincoCoreLog x WHERE x.opDescription = :opDescription"),
  @NamedQuery(
      name = "XincoCoreLog.findByVersionHigh",
      query = "SELECT x FROM XincoCoreLog x WHERE x.versionHigh = :versionHigh"),
  @NamedQuery(
      name = "XincoCoreLog.findByVersionMid",
      query = "SELECT x FROM XincoCoreLog x WHERE x.versionMid = :versionMid"),
  @NamedQuery(
      name = "XincoCoreLog.findByVersionLow",
      query = "SELECT x FROM XincoCoreLog x WHERE x.versionLow = :versionLow"),
  @NamedQuery(
      name = "XincoCoreLog.findByVersionPostfix",
      query = "SELECT x FROM XincoCoreLog x WHERE x.versionPostfix = :versionPostfix")
})
public class XincoCoreLog implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Basic(optional = false)
  @NotNull @Setter
  @GeneratedValue(strategy = TABLE, generator = "XincoCoreLogGen")
  @Getter
  @TableGenerator(
      name = "XincoCoreLogGen",
      table = "xinco_id",
      pkColumnName = "tablename",
      valueColumnName = "last_id",
      pkColumnValue = "xinco_core_log",
      allocationSize = 1,
      initialValue = 1_000)
  @Column(name = "id")
  private Integer id;

  @Basic(optional = false)
  @NotNull @Setter
  @Column(name = "op_code")
  @Getter
  private int opCode;

  @Basic(optional = false)
  @NotNull @Setter
  @Column(name = "op_datetime")
  @Getter
  @Temporal(TIMESTAMP)
  private Date opDatetime;

  @Basic(optional = false)
  @NotNull @Setter
  @Size(min = 1, max = 255) @Column(name = "op_description")
  @Getter
  private String opDescription;

  @Column(name = "version_high")
  @Getter
  @Setter
  private Integer versionHigh;

  @Column(name = "version_mid")
  @Getter
  @Setter
  private Integer versionMid;

  @Column(name = "version_low")
  @Getter
  @Setter
  private Integer versionLow;

  @Size(max = 255) @Column(name = "version_postfix")
  @Getter
  @Setter
  private String versionPostfix;

  @Getter
  @JoinColumn(name = "xinco_core_user_id", referencedColumnName = "id")
  @ManyToOne(optional = false)
  @Setter
  private XincoCoreUser xincoCoreUser;

  @Getter
  @JoinColumn(name = "xinco_core_data_id", referencedColumnName = "id")
  @ManyToOne(optional = false)
  @Setter
  private XincoCoreData xincoCoreData;

  public XincoCoreLog() {}

  public XincoCoreLog(Integer id) {
    this.id = id;
  }

  public XincoCoreLog(Integer id, int opCode, Date opDatetime, String opDescription) {
    this.id = id;
    this.opCode = opCode;
    this.opDatetime = opDatetime;
    this.opDescription = opDescription;
  }

  @Override
  public int hashCode() {
    int hash = 0 + (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof XincoCoreLog)) {
      return false;
    }
    XincoCoreLog other = (XincoCoreLog) object;
    return (!(this.id == null) || !(other.id != null))
        && (!(this.id != null) || this.id.equals(other.id));
  }

  @Override
  public String toString() {
    return "com.bluecubs.xinco.core.server.persistence.XincoCoreLog[ id=" + id + " ]";
  }
}
