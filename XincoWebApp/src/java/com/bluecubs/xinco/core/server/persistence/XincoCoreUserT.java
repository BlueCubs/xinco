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
 * Name: XincoCoreUserT
 * 
 * Description: //TODO: Add description
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_user_t")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreUserT.findAll", query = "SELECT x FROM XincoCoreUserT x"),
    @NamedQuery(name = "XincoCoreUserT.findByRecordId", query = "SELECT x FROM XincoCoreUserT x WHERE x.recordId = :recordId"),
    @NamedQuery(name = "XincoCoreUserT.findById", query = "SELECT x FROM XincoCoreUserT x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreUserT.findByUsername", query = "SELECT x FROM XincoCoreUserT x WHERE x.username = :username"),
    @NamedQuery(name = "XincoCoreUserT.findByUserpassword", query = "SELECT x FROM XincoCoreUserT x WHERE x.userpassword = :userpassword"),
    @NamedQuery(name = "XincoCoreUserT.findByName", query = "SELECT x FROM XincoCoreUserT x WHERE x.name = :name"),
    @NamedQuery(name = "XincoCoreUserT.findByFirstname", query = "SELECT x FROM XincoCoreUserT x WHERE x.firstname = :firstname"),
    @NamedQuery(name = "XincoCoreUserT.findByEmail", query = "SELECT x FROM XincoCoreUserT x WHERE x.email = :email"),
    @NamedQuery(name = "XincoCoreUserT.findByStatusNumber", query = "SELECT x FROM XincoCoreUserT x WHERE x.statusNumber = :statusNumber"),
    @NamedQuery(name = "XincoCoreUserT.findByAttempts", query = "SELECT x FROM XincoCoreUserT x WHERE x.attempts = :attempts"),
    @NamedQuery(name = "XincoCoreUserT.findByLastModified", query = "SELECT x FROM XincoCoreUserT x WHERE x.lastModified = :lastModified")})
public class XincoCoreUserT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "record_id")
    private Integer recordId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;
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

    public XincoCoreUserT() {
    }

    public XincoCoreUserT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreUserT(Integer recordId, int id, String username, String userpassword, String name, String firstname, String email, int statusNumber, int attempts, Date lastModified) {
        this.recordId = recordId;
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

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoCoreUserT)) {
            return false;
        }
        XincoCoreUserT other = (XincoCoreUserT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreUserT[ recordId=" + recordId + " ]";
    }
    
}
