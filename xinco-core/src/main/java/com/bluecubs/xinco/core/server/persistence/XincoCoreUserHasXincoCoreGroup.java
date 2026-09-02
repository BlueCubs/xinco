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
 * Name: XincoCoreUserHasXincoCoreGroup
 *
 * Description: XincoCoreUserhasXincoCoreGroup JPA class
 *
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
 *
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "xinco_core_user_has_xinco_core_group")
@XmlRootElement
@NamedQueries({
  @NamedQuery(
      name = "XincoCoreUserHasXincoCoreGroup.findAll",
      query = "SELECT x FROM XincoCoreUserHasXincoCoreGroup x"),
  @NamedQuery(
      name = "XincoCoreUserHasXincoCoreGroup.findByXincoCoreUserId",
      query =
          "SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE"
              + " x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreUserId = :xincoCoreUserId"),
  @NamedQuery(
      name = "XincoCoreUserHasXincoCoreGroup.findByXincoCoreGroupId",
      query =
          "SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE"
              + " x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreGroupId = :xincoCoreGroupId"),
  @NamedQuery(
      name = "XincoCoreUserHasXincoCoreGroup.findByStatusNumber",
      query = "SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE x.statusNumber = :statusNumber")
})
public class XincoCoreUserHasXincoCoreGroup implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId @Getter @Setter
  protected XincoCoreUserHasXincoCoreGroupPK xincoCoreUserHasXincoCoreGroupPK;

  @Basic(optional = false)
  @NotNull @Setter
  @Column(name = "status_number")
  @Getter
  private int statusNumber;

  @Getter
  @JoinColumn(
      name = "xinco_core_user_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  @ManyToOne(optional = false)
  @Setter
  private XincoCoreUser xincoCoreUser;

  @Getter
  @JoinColumn(
      name = "xinco_core_group_id",
      referencedColumnName = "id",
      insertable = false,
      updatable = false)
  @ManyToOne(optional = false)
  @Setter
  private XincoCoreGroup xincoCoreGroup;

  public XincoCoreUserHasXincoCoreGroup() {}

  public XincoCoreUserHasXincoCoreGroup(
      XincoCoreUserHasXincoCoreGroupPK xincoCoreUserHasXincoCoreGroupPK) {
    this.xincoCoreUserHasXincoCoreGroupPK = xincoCoreUserHasXincoCoreGroupPK;
  }

  public XincoCoreUserHasXincoCoreGroup(
      XincoCoreUserHasXincoCoreGroupPK xincoCoreUserHasXincoCoreGroupPK, int statusNumber) {
    this.xincoCoreUserHasXincoCoreGroupPK = xincoCoreUserHasXincoCoreGroupPK;
    this.statusNumber = statusNumber;
  }

  public XincoCoreUserHasXincoCoreGroup(int xincoCoreUserId, int xincoCoreGroupId) {
    this.xincoCoreUserHasXincoCoreGroupPK =
        new XincoCoreUserHasXincoCoreGroupPK(xincoCoreUserId, xincoCoreGroupId);
  }

  @Override
  public int hashCode() {
    int hash =
        0
            + (xincoCoreUserHasXincoCoreGroupPK != null
                ? xincoCoreUserHasXincoCoreGroupPK.hashCode()
                : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof XincoCoreUserHasXincoCoreGroup)) {
      return false;
    }
    XincoCoreUserHasXincoCoreGroup other = (XincoCoreUserHasXincoCoreGroup) object;
    return (!(this.xincoCoreUserHasXincoCoreGroupPK == null)
            || !(other.xincoCoreUserHasXincoCoreGroupPK != null))
        && (!(this.xincoCoreUserHasXincoCoreGroupPK != null)
            || this.xincoCoreUserHasXincoCoreGroupPK.equals(
                other.xincoCoreUserHasXincoCoreGroupPK));
  }

  @Override
  public String toString() {
    return "com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup["
        + " xincoCoreUserHasXincoCoreGroupPK="
        + xincoCoreUserHasXincoCoreGroupPK
        + " ]";
  }
}
