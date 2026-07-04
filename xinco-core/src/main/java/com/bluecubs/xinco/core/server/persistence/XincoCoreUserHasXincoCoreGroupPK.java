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
 * Name: XincoCoreUserHasXincoCoreGroupPK
 *
 * Description: PK JPA class
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

/**
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
@Embeddable
public class XincoCoreUserHasXincoCoreGroupPK implements Serializable {

  @Basic(optional = false)
  @NotNull
  @Setter
  @Column(name = "xinco_core_user_id")
  @Getter
  private int xincoCoreUserId;

  @Basic(optional = false)
  @NotNull
  @Setter
  @Column(name = "xinco_core_group_id")
  @Getter
  private int xincoCoreGroupId;

  public XincoCoreUserHasXincoCoreGroupPK() {}

  public XincoCoreUserHasXincoCoreGroupPK(int xincoCoreUserId, int xincoCoreGroupId) {
    this.xincoCoreUserId = xincoCoreUserId;
    this.xincoCoreGroupId = xincoCoreGroupId;
  }

  @Override
  public int hashCode() {
    int hash = (0 + ((int) xincoCoreUserId)) + ((int) xincoCoreGroupId);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof XincoCoreUserHasXincoCoreGroupPK)) {
      return false;
    }
    XincoCoreUserHasXincoCoreGroupPK other = (XincoCoreUserHasXincoCoreGroupPK) object;
    if (this.xincoCoreUserId != other.xincoCoreUserId) {
      return false;
    }
    return !(this.xincoCoreGroupId != other.xincoCoreGroupId);
  }

  @Override
  public String toString() {
    return "com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroupPK[ xincoCoreUserId="
        + xincoCoreUserId
        + ", xincoCoreGroupId="
        + xincoCoreGroupId
        + " ]";
  }
}
