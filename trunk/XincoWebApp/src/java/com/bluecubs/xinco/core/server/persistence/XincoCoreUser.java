/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.server.AuditedEntityListener;
import com.bluecubs.xinco.core.server.XincoAuditedObject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@EntityListeners(AuditedEntityListener.class)
@Table(name = "xinco_core_user", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"username"})})
@NamedQueries({
    @NamedQuery(name = "XincoCoreUser.findAll",
    query = "SELECT x FROM XincoCoreUser x"),
    @NamedQuery(name = "XincoCoreUser.findById",
    query = "SELECT x FROM XincoCoreUser x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreUser.findByUsername",
    query = "SELECT x FROM XincoCoreUser x WHERE x.username = :username"),
    @NamedQuery(name = "XincoCoreUser.findByUserpassword",
    query = "SELECT x FROM XincoCoreUser x WHERE x.userpassword = :userpassword"),
    @NamedQuery(name = "XincoCoreUser.findByName",
    query = "SELECT x FROM XincoCoreUser x WHERE x.name = :name"),
    @NamedQuery(name = "XincoCoreUser.findByFirstname",
    query = "SELECT x FROM XincoCoreUser x WHERE x.firstname = :firstname"),
    @NamedQuery(name = "XincoCoreUser.findByEmail",
    query = "SELECT x FROM XincoCoreUser x WHERE x.email = :email"),
    @NamedQuery(name = "XincoCoreUser.findByStatusNumber",
    query = "SELECT x FROM XincoCoreUser x WHERE x.statusNumber = :statusNumber"),
    @NamedQuery(name = "XincoCoreUser.findByAttempts",
    query = "SELECT x FROM XincoCoreUser x WHERE x.attempts = :attempts"),
    @NamedQuery(name = "XincoCoreUser.findByLastModified",
    query = "SELECT x FROM XincoCoreUser x WHERE x.lastModified = :lastModified")})
public class XincoCoreUser extends XincoAuditedObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
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
    @OneToMany(mappedBy = "xincoCoreUserId", fetch = FetchType.LAZY)
    private List<XincoCoreAce> xincoCoreAceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreUserId", fetch = FetchType.LAZY)
    private List<XincoCoreLog> xincoCoreLogList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreUser", fetch = FetchType.LAZY)
    private List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreUser", fetch = FetchType.LAZY)
    private List<XincoCoreUserModifiedRecord> xincoCoreUserModifiedRecordList;

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

    public List<XincoCoreAce> getXincoCoreAceList() {
        return xincoCoreAceList;
    }

    public void setXincoCoreAceList(List<XincoCoreAce> xincoCoreAceList) {
        this.xincoCoreAceList = xincoCoreAceList;
    }

    public List<XincoCoreLog> getXincoCoreLogList() {
        return xincoCoreLogList;
    }

    public void setXincoCoreLogList(List<XincoCoreLog> xincoCoreLogList) {
        this.xincoCoreLogList = xincoCoreLogList;
    }

    public List<XincoCoreUserHasXincoCoreGroup> getXincoCoreUserHasXincoCoreGroupList1() {
        return xincoCoreUserHasXincoCoreGroupList1;
    }

    public void setXincoCoreUserHasXincoCoreGroupList1(List<XincoCoreUserHasXincoCoreGroup> xincoCoreUserHasXincoCoreGroupList1) {
        this.xincoCoreUserHasXincoCoreGroupList1 = xincoCoreUserHasXincoCoreGroupList1;
    }

    public List<XincoCoreUserModifiedRecord> getXincoCoreUserModifiedRecordList() {
        return xincoCoreUserModifiedRecordList;
    }

    public void setXincoCoreUserModifiedRecordList(List<XincoCoreUserModifiedRecord> xincoCoreUserModifiedRecordList) {
        this.xincoCoreUserModifiedRecordList = xincoCoreUserModifiedRecordList;
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
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreUser[id=" + id + "]";
    }
}
