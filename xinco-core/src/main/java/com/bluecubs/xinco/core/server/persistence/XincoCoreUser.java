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
 * Name: XincoCoreUser
 *
 * Description: XincoCoreUser JPA class
 *
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
 *
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.TABLE;
import static jakarta.persistence.TemporalType.DATE;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

/**
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
@Audited
@Entity
@Table(
    name = "xinco_core_user",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "XincoCoreUser.findAll", query = "SELECT x FROM XincoCoreUser x"),
  @NamedQuery(
      name = "XincoCoreUser.findById",
      query = "SELECT x FROM XincoCoreUser x WHERE x.id = :id"),
  @NamedQuery(
      name = "XincoCoreUser.findByUsername",
      query = "SELECT x FROM XincoCoreUser x WHERE x.username = :username"),
  @NamedQuery(
      name = "XincoCoreUser.findByUserpassword",
      query = "SELECT x FROM XincoCoreUser x WHERE x.userpassword = :userpassword"),
  @NamedQuery(
      name = "XincoCoreUser.findByLastName",
      query = "SELECT x FROM XincoCoreUser x WHERE x.lastName = :lastName"),
  @NamedQuery(
      name = "XincoCoreUser.findByFirstName",
      query = "SELECT x FROM XincoCoreUser x WHERE x.firstName = :firstName"),
  @NamedQuery(
      name = "XincoCoreUser.findByEmail",
      query = "SELECT x FROM XincoCoreUser x WHERE x.email = :email"),
  @NamedQuery(
      name = "XincoCoreUser.findByStatusNumber",
      query = "SELECT x FROM XincoCoreUser x WHERE x.statusNumber = :statusNumber"),
  @NamedQuery(
      name = "XincoCoreUser.findByAttempts",
      query = "SELECT x FROM XincoCoreUser x WHERE x.attempts = :attempts"),
  @NamedQuery(
      name = "XincoCoreUser.findByLastModified",
      query = "SELECT x FROM XincoCoreUser x WHERE x.lastModified = :lastModified")
})
public class XincoCoreUser implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Basic(optional = false)
  @NotNull @Setter
  @GeneratedValue(strategy = TABLE, generator = "XincoCoreUserGen")
  @Getter
  @TableGenerator(
      name = "XincoCoreUserGen",
      table = "xinco_id",
      pkColumnName = "tablename",
      valueColumnName = "last_id",
      pkColumnValue = "xinco_core_user",
      allocationSize = 1,
      initialValue = 1_000)
  @Column(name = "id")
  private Integer id;

  @Basic(optional = false)
  @NotNull @Size(min = 1, max = 255) @Column(name = "username")
  @Getter
  @Pattern(regexp = "^[a-z0-9_-]{3,15}$", message = "Invalid user name") @Setter
  private String username;

  @Basic(optional = false)
  @NotNull @Setter
  @Size(min = 1, max = 255) @Column(name = "userpassword")
  @Getter
  private String userpassword;

  @Basic(optional = false)
  @NotNull @Setter
  @Size(min = 1, max = 255) @Column(name = "last_name")
  @Getter
  private String lastName;

  @Basic(optional = false)
  @NotNull @Setter
  @Size(min = 1, max = 255) @Column(name = "first_name")
  @Getter
  private String firstName;

  @Pattern(
      regexp =
          "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
      message = "Invalid email")
  @Setter // if the field contains email address consider using this annotation to
  // enforce field validation
  @Basic(optional = false)
  @NotNull @Size(min = 1, max = 255) @Column(name = "email")
  @Getter
  private String email;

  @Basic(optional = false)
  @NotNull @Setter
  @Column(name = "status_number")
  @Getter
  private int statusNumber;

  @Basic(optional = false)
  @NotNull @Setter
  @Column(name = "attempts")
  @Getter
  private int attempts;

  @Basic(optional = false)
  @NotNull @Setter
  @Column(name = "last_modified")
  @Getter
  @Temporal(DATE)
  private Date lastModified;

  @Getter(onMethod_ = {@XmlTransient})
  @OneToMany(mappedBy = "xincoCoreUser")
  @Setter
  private List<XincoCoreAce> xincoCoreAceList;

  @Getter(onMethod_ = {@XmlTransient})
  @OneToMany(cascade = ALL, mappedBy = "xincoCoreUser")
  @Setter
  private List<XincoCoreLog> xincoCoreLogList;

  @Getter(onMethod_ = {@XmlTransient})
  @OneToMany(cascade = ALL, mappedBy = "xincoCoreUser")
  @Setter
  private List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupList;

  public XincoCoreUser() {}

  public XincoCoreUser(Integer id) {
    this.id = id;
  }

  public XincoCoreUser(
      Integer id,
      String username,
      String userpassword,
      String lastName,
      String firstName,
      String email,
      int statusNumber,
      int attempts,
      Date lastModified) {
    this.id = id;
    this.username = username;
    this.userpassword = userpassword;
    this.lastName = lastName;
    this.firstName = firstName;
    this.email = email;
    this.statusNumber = statusNumber;
    this.attempts = attempts;
    this.lastModified = lastModified;
  }

  @Override
  public int hashCode() {
    int hash = 0 + (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof XincoCoreUser)) {
      return false;
    }
    XincoCoreUser other = (XincoCoreUser) object;
    return (!(this.id == null) || !(other.id != null))
        && (!(this.id != null) || this.id.equals(other.id));
  }

  @Override
  public String toString() {
    return "com.bluecubs.xinco.core.server.persistence.XincoCoreUser[ id=" + id + " ]";
  }
}
