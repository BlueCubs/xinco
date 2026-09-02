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
 * Name: XincoSetting
 *
 * Description: XincoSetting JPA class
 *
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
 *
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import static jakarta.persistence.GenerationType.TABLE;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

/**
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
@Audited
@Entity
@Table(name = "xinco_setting")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "XincoSetting.findAll", query = "SELECT x FROM XincoSetting x"),
  @NamedQuery(
      name = "XincoSetting.findById",
      query = "SELECT x FROM XincoSetting x WHERE x.id = :id"),
  @NamedQuery(
      name = "XincoSetting.findByDescription",
      query = "SELECT x FROM XincoSetting x WHERE x.description = :description"),
  @NamedQuery(
      name = "XincoSetting.findByIntValue",
      query = "SELECT x FROM XincoSetting x WHERE x.intValue = :intValue"),
  @NamedQuery(
      name = "XincoSetting.findByBoolValue",
      query = "SELECT x FROM XincoSetting x WHERE x.boolValue = :boolValue"),
  @NamedQuery(
      name = "XincoSetting.findByLongValue",
      query = "SELECT x FROM XincoSetting x WHERE x.longValue = :longValue")
})
public class XincoSetting implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Basic(optional = false)
  @NotNull @Setter
  @GeneratedValue(strategy = TABLE, generator = "XincoSettingGen")
  @Getter
  @TableGenerator(
      name = "XincoSettingGen",
      table = "xinco_id",
      pkColumnName = "tablename",
      valueColumnName = "last_id",
      pkColumnValue = "xinco_setting",
      allocationSize = 1,
      initialValue = 1_000)
  @Column(name = "id")
  private Integer id;

  @Basic(optional = false)
  @NotNull @Setter
  @Size(min = 1, max = 45) @Column(name = "description")
  @Getter
  private String description;

  @Column(name = "int_value")
  @Getter
  @Setter
  private Integer intValue;

  @Lob
  @Setter
  @Size(max = 65_535) @Column(name = "string_value")
  @Getter
  private String stringValue;

  @Column(name = "bool_value")
  @Getter
  @Setter
  private Boolean boolValue;

  @Column(name = "long_value")
  @Getter
  @Setter
  private long longValue;

  public XincoSetting() {}

  public XincoSetting(Integer id) {
    this.id = id;
  }

  public XincoSetting(Integer id, String description) {
    this.id = id;
    this.description = description;
  }

  @Override
  public int hashCode() {
    int hash = 0 + (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof XincoSetting)) {
      return false;
    }
    XincoSetting other = (XincoSetting) object;
    return (!(this.id == null) || !(other.id != null))
        && (!(this.id != null) || this.id.equals(other.id));
  }

  @Override
  public String toString() {
    return "com.bluecubs.xinco.core.server.persistence.XincoSetting[ id=" + id + " ]";
  }
}
