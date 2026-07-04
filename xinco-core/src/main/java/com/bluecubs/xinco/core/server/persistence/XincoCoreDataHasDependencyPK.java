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
 * Name: XincoCoreDataHasDependencyPK
 *
 * Description: Table entity
 *
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
 *
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/** @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com */
@Embeddable
public class XincoCoreDataHasDependencyPK implements Serializable {

  @Basic(optional = false)
  @NotNull
  @Setter
  @Column(name = "xinco_core_data_parent_id")
  @Getter
  private int xincoCoreDataParentId;

  @Basic(optional = false)
  @NotNull
  @Setter
  @Column(name = "xinco_core_data_children_id")
  @Getter
  private int xincoCoreDataChildrenId;

  @Basic(optional = false)
  @NotNull
  @Setter
  @Column(name = "dependency_type_id")
  @Getter
  private int dependencyTypeId;

  public XincoCoreDataHasDependencyPK() {}

  public XincoCoreDataHasDependencyPK(
      int xincoCoreDataParentId, int xincoCoreDataChildrenId, int dependencyTypeId) {
    this.xincoCoreDataParentId = xincoCoreDataParentId;
    this.xincoCoreDataChildrenId = xincoCoreDataChildrenId;
    this.dependencyTypeId = dependencyTypeId;
  }

  @Override
  public int hashCode() {
    int hash =
        ((0 + ((int) xincoCoreDataParentId)) + ((int) xincoCoreDataChildrenId))
            + ((int) dependencyTypeId);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof XincoCoreDataHasDependencyPK)) {
      return false;
    }
    XincoCoreDataHasDependencyPK other = (XincoCoreDataHasDependencyPK) object;
    if (this.xincoCoreDataParentId != other.xincoCoreDataParentId) {
      return false;
    }
    if (this.xincoCoreDataChildrenId != other.xincoCoreDataChildrenId) {
      return false;
    }
    return !(this.dependencyTypeId != other.dependencyTypeId);
  }

  @Override
  public String toString() {
    return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataHasDependencyPK[ xincoCoreDataParentId="
        + xincoCoreDataParentId
        + ", xincoCoreDataChildrenId="
        + xincoCoreDataChildrenId
        + ", dependencyTypeId="
        + dependencyTypeId
        + " ]";
  }
}
