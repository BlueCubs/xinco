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
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.server.XincoAuditedObject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.TABLE;
import static javax.persistence.TemporalType.DATE;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_user", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreUser.findAll", query = "SELECT x FROM XincoCoreUser x"),
    @NamedQuery(name = "XincoCoreUser.findById", query = "SELECT x FROM XincoCoreUser x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreUser.findByUsername", query = "SELECT x FROM XincoCoreUser x WHERE x.username = :username"),
    @NamedQuery(name = "XincoCoreUser.findByUserpassword", query = "SELECT x FROM XincoCoreUser x WHERE x.userpassword = :userpassword"),
    @NamedQuery(name = "XincoCoreUser.findByLastName", query = "SELECT x FROM XincoCoreUser x WHERE x.lastName = :lastName"),
    @NamedQuery(name = "XincoCoreUser.findByFirstName", query = "SELECT x FROM XincoCoreUser x WHERE x.firstName = :firstName"),
    @NamedQuery(name = "XincoCoreUser.findByEmail", query = "SELECT x FROM XincoCoreUser x WHERE x.email = :email"),
    @NamedQuery(name = "XincoCoreUser.findByStatusNumber", query = "SELECT x FROM XincoCoreUser x WHERE x.statusNumber = :statusNumber"),
    @NamedQuery(name = "XincoCoreUser.findByAttempts", query = "SELECT x FROM XincoCoreUser x WHERE x.attempts = :attempts"),
    @NamedQuery(name = "XincoCoreUser.findByLastModified", query = "SELECT x FROM XincoCoreUser x WHERE x.lastModified = :lastModified")})
public class XincoCoreUser extends XincoAuditedObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = TABLE, generator = "XincoCoreUserGen")
    @TableGenerator(name = "XincoCoreUserGen", table = "xinco_id",
    pkColumnName = "tablename",
    valueColumnName = "last_id",
    pkColumnValue = "xinco_core_user",
    allocationSize = 1,
    initialValue = 1000)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "username")
    @Pattern(regexp = "^[a-z0-9_-]{3,15}$", message = "Invalid user name")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "userpassword")
    private String userpassword;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "last_name")
    private String lastName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "first_name")
    private String firstName;
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status_number")
    private int statusNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "attempts")
    private int attempts;
    @Basic(optional = false)
    @NotNull
    @Column(name = "last_modified")
    @Temporal(DATE)
    private Date lastModified;
    @OneToMany(cascade = ALL, mappedBy = "xincoCoreUser")
    private List<XincoCoreUserModifiedRecord> xincoCoreUserModifiedRecordList;
    @OneToMany(mappedBy = "xincoCoreUser")
    private List<XincoCoreAce> xincoCoreAceList;
    @OneToMany(cascade = ALL, mappedBy = "xincoCoreUser")
    private List<XincoCoreLog> xincoCoreLogList;
    @OneToMany(cascade = ALL, mappedBy = "xincoCoreUser")
    private List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupList;

    public XincoCoreUser() {
    }

    public XincoCoreUser(Integer id) {
        this.id = id;
    }

    public XincoCoreUser(Integer id, String username, String userpassword, String lastName, String firstName, String email, int statusNumber, int attempts, Date lastModified) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @XmlTransient
    public List<XincoCoreUserModifiedRecord> getXincoCoreUserModifiedRecordList() {
        return xincoCoreUserModifiedRecordList;
    }

    public void setXincoCoreUserModifiedRecordList(List<XincoCoreUserModifiedRecord> xincoCoreUserModifiedRecordList) {
        this.xincoCoreUserModifiedRecordList = xincoCoreUserModifiedRecordList;
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

    @XmlTransient
    public List<XincoCoreUserHasXincoCoreGroup> getXincoCoreUserHasXincoCoreGroupList() {
        return xincoCoreUserHasXincoCoreGroupList;
    }

    public void setXincoCoreUserHasXincoCoreGroupList(List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupList) {
        this.xincoCoreUserHasXincoCoreGroupList = xincoCoreUserHasXincoCoreGroupList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoCoreUser)) {
            return false;
        }
        XincoCoreUser other = (XincoCoreUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreUser[ id=" + id + " ]";
    }
}
