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
 * Name: XincoCoreDataHasDependency
 *
 * Description: Table entity
 *
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
 *
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
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
@Table(name = "xinco_core_data_has_dependency")
@XmlRootElement
@NamedQueries({
  @NamedQuery(
      name = "XincoCoreDataHasDependency.findAll",
      query = "SELECT x FROM XincoCoreDataHasDependency x"),
  @NamedQuery(
      name = "XincoCoreDataHasDependency.findByXincoCoreDataParentId",
      query =
          "SELECT x FROM XincoCoreDataHasDependency x WHERE"
              + " x.xincoCoreDataHasDependencyPK.xincoCoreDataParentId = :xincoCoreDataParentId"),
  @NamedQuery(
      name = "XincoCoreDataHasDependency.findByXincoCoreDataChildrenId",
      query =
          "SELECT x FROM XincoCoreDataHasDependency x WHERE"
              + " x.xincoCoreDataHasDependencyPK.xincoCoreDataChildrenId ="
              + " :xincoCoreDataChildrenId"),
  @NamedQuery(
      name = "XincoCoreDataHasDependency.findByDependencyTypeId",
      query =
          "SELECT x FROM XincoCoreDataHasDependency x WHERE"
              + " x.xincoCoreDataHasDependencyPK.dependencyTypeId = :dependencyTypeId")
})
public class XincoCoreDataHasDependency implements Serializable {

  private static final long serialVersionUID = 1L;
  @EmbeddedId @Getter @Setter protected XincoCoreDataHasDependencyPK xincoCoreDataHasDependencyPK;

  @Getter
  @JoinColumn(
      name = "dependency_type_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  @ManyToOne(optional = false)
  @Setter
  private XincoDependencyType xincoDependencyType;

  @Getter
  @JoinColumn(
      name = "xinco_core_data_children_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  @ManyToOne(optional = false)
  @Setter
  private XincoCoreData xincoCoreData;

  @Getter
  @JoinColumn(
      name = "xinco_core_data_parent_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  @ManyToOne(optional = false)
  @Setter
  private XincoCoreData xincoCoreData1;

  public XincoCoreDataHasDependency() {}

  public XincoCoreDataHasDependency(XincoCoreDataHasDependencyPK xincoCoreDataHasDependencyPK) {
    this.xincoCoreDataHasDependencyPK = xincoCoreDataHasDependencyPK;
  }

  public XincoCoreDataHasDependency(
      int xincoCoreDataParentId, int xincoCoreDataChildrenId, int dependencyTypeId) {
    this.xincoCoreDataHasDependencyPK =
        new XincoCoreDataHasDependencyPK(
            xincoCoreDataParentId, xincoCoreDataChildrenId, dependencyTypeId);
  }

  @Override
  public int hashCode() {
    int hash =
        0 + (xincoCoreDataHasDependencyPK != null ? xincoCoreDataHasDependencyPK.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof XincoCoreDataHasDependency)) {
      return false;
    }
    XincoCoreDataHasDependency other = (XincoCoreDataHasDependency) object;
    return (!(this.xincoCoreDataHasDependencyPK == null)
            || !(other.xincoCoreDataHasDependencyPK != null))
        && (!(this.xincoCoreDataHasDependencyPK != null)
            || this.xincoCoreDataHasDependencyPK.equals(other.xincoCoreDataHasDependencyPK));
  }

  @Override
  public String toString() {
    return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependency["
        + " xincoCoreDataHasDependencyPK="
        + xincoCoreDataHasDependencyPK
        + " ]";
  }
}
