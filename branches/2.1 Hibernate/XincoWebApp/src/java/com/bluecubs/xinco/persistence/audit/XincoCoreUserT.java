/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.persistence.audit;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class: XincoCoreUserT
 * Created: Mar 24, 2008 2:44:25 PM
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_user_t")
@NamedQueries({@NamedQuery(name = "XincoCoreUserT.findByRecordId", query = "SELECT x FROM XincoCoreUserT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoCoreUserT.findByAttempts", query = "SELECT x FROM XincoCoreUserT x WHERE x.attempts = :attempts"), @NamedQuery(name = "XincoCoreUserT.findByEmail", query = "SELECT x FROM XincoCoreUserT x WHERE x.email = :email"), @NamedQuery(name = "XincoCoreUserT.findByFirstname", query = "SELECT x FROM XincoCoreUserT x WHERE x.firstname = :firstname"), @NamedQuery(name = "XincoCoreUserT.findById", query = "SELECT x FROM XincoCoreUserT x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreUserT.findByLastModified", query = "SELECT x FROM XincoCoreUserT x WHERE x.lastModified = :lastModified"), @NamedQuery(name = "XincoCoreUserT.findByName", query = "SELECT x FROM XincoCoreUserT x WHERE x.name = :name"), @NamedQuery(name = "XincoCoreUserT.findByStatusNumber", query = "SELECT x FROM XincoCoreUserT x WHERE x.statusNumber = :statusNumber"), @NamedQuery(name = "XincoCoreUserT.findByUsername", query = "SELECT x FROM XincoCoreUserT x WHERE x.username = :username"), @NamedQuery(name = "XincoCoreUserT.findByUserpassword", query = "SELECT x FROM XincoCoreUserT x WHERE x.userpassword = :userpassword")})
public class XincoCoreUserT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Column(name = "attempts", nullable = false)
    private int attempts;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "firstname", nullable = false)
    private String firstname;
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "last_modified", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date lastModified;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "status_number", nullable = false)
    private int statusNumber;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "userpassword", nullable = false)
    private String userpassword;

    public XincoCoreUserT() {
    }

    public XincoCoreUserT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreUserT(Integer recordId, int attempts, String email, String firstname, int id, Date lastModified, String name, int statusNumber, String username, String userpassword) {
        this.recordId = recordId;
        this.attempts = attempts;
        this.email = email;
        this.firstname = firstname;
        this.id = id;
        this.lastModified = lastModified;
        this.name = name;
        this.statusNumber = statusNumber;
        this.username = username;
        this.userpassword = userpassword;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
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
        return "com.bluecubs.xinco.persistence.audit.XincoCoreUserT[recordId=" + recordId + "]";
    }

}
