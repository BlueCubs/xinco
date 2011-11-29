/*
 * Copyright 2011 blueCubs.com.
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
 * Description: //TODO: Add description
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.server.AuditedEntityListener;
import com.bluecubs.xinco.core.server.XincoAuditedObject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@EntityListeners(AuditedEntityListener.class)
@Table(name = "xinco_core_user", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreUser.findAll", query = "SELECT x FROM XincoCoreUser x"),
    @NamedQuery(name = "XincoCoreUser.findById", query = "SELECT x FROM XincoCoreUser x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreUser.findByUsername", query = "SELECT x FROM XincoCoreUser x WHERE x.username = :username"),
    @NamedQuery(name = "XincoCoreUser.findByUserpassword", query = "SELECT x FROM XincoCoreUser x WHERE x.userpassword = :userpassword"),
    @NamedQuery(name = "XincoCoreUser.findByName", query = "SELECT x FROM XincoCoreUser x WHERE x.name = :name"),
    @NamedQuery(name = "XincoCoreUser.findByFirstname", query = "SELECT x FROM XincoCoreUser x WHERE x.firstname = :firstname"),
    @NamedQuery(name = "XincoCoreUser.findByEmail", query = "SELECT x FROM XincoCoreUser x WHERE x.email = :email"),
    @NamedQuery(name = "XincoCoreUser.findByStatusNumber", query = "SELECT x FROM XincoCoreUser x WHERE x.statusNumber = :statusNumber"),
    @NamedQuery(name = "XincoCoreUser.findByAttempts", query = "SELECT x FROM XincoCoreUser x WHERE x.attempts = :attempts"),
    @NamedQuery(name = "XincoCoreUser.findByLastModified", query = "SELECT x FROM XincoCoreUser x WHERE x.lastModified = :lastModified")})
public class XincoCoreUser extends XincoAuditedObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "userpassword")
    private String userpassword;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "firstname")
    private String firstname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
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
    @Temporal(TemporalType.DATE)
    private Date lastModified;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreUser")
    private List<XincoCoreUserModifiedRecord> xincoCoreUserModifiedRecordList;
    @OneToMany(mappedBy = "xincoCoreUser")
    private List<XincoCoreAce> xincoCoreAceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreUser")
    private List<XincoCoreLog> xincoCoreLogList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreUser")
    private List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupList;

    public XincoCoreUser() {
    }

    public XincoCoreUser(Integer id) {
        this.id = id;
    }

    public XincoCoreUser(Integer id, String username, String userpassword, String name, String firstname, String email, int statusNumber, int attempts, Date lastModified) {
        this.id = id;
        this.username = username;
        this.userpassword = userpassword;
        this.name = name;
        this.firstname = firstname;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
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
