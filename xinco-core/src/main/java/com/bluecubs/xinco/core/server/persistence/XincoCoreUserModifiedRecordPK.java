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
 * Name: XincoCoreUserModifiedRecordPK
 *
 * Description: PK JPA class
 *
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
 *
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import static javax.persistence.GenerationType.TABLE;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com */
@Embeddable
public class XincoCoreUserModifiedRecordPK implements Serializable {

  @Basic(optional = false)
  @NotNull
  @Setter
  @Column(name = "id")
  @GeneratedValue(strategy = TABLE, generator = "UserModifiedRecordGen")
  @Getter
  @TableGenerator(
      name = "UserModifiedRecordGen",
      table = "xinco_id",
      pkColumnName = "tablename",
      valueColumnName = "last_id",
      pkColumnValue = "xinco_core_user_modified_record",
      allocationSize = 1,
      initialValue = 1)
  private int id;

  @Basic(optional = false)
  @NotNull
  @Setter
  @Column(name = "record_id")
  @Getter
  private int recordId;

  public XincoCoreUserModifiedRecordPK() {}

  public XincoCoreUserModifiedRecordPK(int id, int recordId) {
    this.id = id;
    this.recordId = recordId;
  }

  @Override
  public int hashCode() {
    int hash = (0 + ((int) id)) + ((int) recordId);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof XincoCoreUserModifiedRecordPK)) {
      return false;
    }
    XincoCoreUserModifiedRecordPK other = (XincoCoreUserModifiedRecordPK) object;
    if (this.id != other.id) {
      return false;
    }
    return !(this.recordId != other.recordId);
  }

  @Override
  public String toString() {
    return "com.bluecubs.xinco.core.server.persistence.XincoCoreUserModifiedRecordPK[ id="
        + id
        + ", recordId="
        + recordId
        + " ]";
  }
}
