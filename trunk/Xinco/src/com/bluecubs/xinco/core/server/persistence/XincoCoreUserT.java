/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_user_t")
@NamedQueries({@NamedQuery(name = "XincoCoreUserT.findAll", query = "SELECT x FROM XincoCoreUserT x"), @NamedQuery(name = "XincoCoreUserT.findByRecordId", query = "SELECT x FROM XincoCoreUserT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoCoreUserT.findById", query = "SELECT x FROM XincoCoreUserT x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreUserT.findByUsername", query = "SELECT x FROM XincoCoreUserT x WHERE x.username = :username"), @NamedQuery(name = "XincoCoreUserT.findByUserpassword", query = "SELECT x FROM XincoCoreUserT x WHERE x.userpassword = :userpassword"), @NamedQuery(name = "XincoCoreUserT.findByName", query = "SELECT x FROM XincoCoreUserT x WHERE x.name = :name"), @NamedQuery(name = "XincoCoreUserT.findByFirstname", query = "SELECT x FROM XincoCoreUserT x WHERE x.firstname = :firstname"), @NamedQuery(name = "XincoCoreUserT.findByEmail", query = "SELECT x FROM XincoCoreUserT x WHERE x.email = :email"), @NamedQuery(name = "XincoCoreUserT.findByStatusNumber", query = "SELECT x FROM XincoCoreUserT x WHERE x.statusNumber = :statusNumber"), @NamedQuery(name = "XincoCoreUserT.findByAttempts", query = "SELECT x FROM XincoCoreUserT x WHERE x.attempts = :attempts"), @NamedQuery(name = "XincoCoreUserT.findByLastModified", query = "SELECT x FROM XincoCoreUserT x WHERE x.lastModified = :lastModified")})
public class XincoCoreUserT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private int id;
    @Basic(optional = false)
    @Column(name = "username", nullable = false, length = 255)
    private String username;
    @Basic(optional = false)
    @Column(name = "userpassword", nullable = false, length = 255)
    private String userpassword;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Basic(optional = false)
    @Column(name = "firstname", nullable = false, length = 255)
    private String firstname;
    @Basic(optional = false)
    @Column(name = "email", nullable = false, length = 255)
    private String email;
    @Basic(optional = false)
    @Column(name = "status_number", nullable = false)
    private int statusNumber;
    @Basic(optional = false)
    @Column(name = "attempts", nullable = false)
    private int attempts;
    @Basic(optional = false)
    @Column(name = "last_modified", nullable = false)
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
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreUserT[recordId=" + recordId + "]";
    }

}
