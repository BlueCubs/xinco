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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_log")
@NamedQueries({@NamedQuery(name = "XincoCoreLog.findAll", query = "SELECT x FROM XincoCoreLog x"), @NamedQuery(name = "XincoCoreLog.findById", query = "SELECT x FROM XincoCoreLog x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreLog.findByOpCode", query = "SELECT x FROM XincoCoreLog x WHERE x.opCode = :opCode"), @NamedQuery(name = "XincoCoreLog.findByOpDatetime", query = "SELECT x FROM XincoCoreLog x WHERE x.opDatetime = :opDatetime"), @NamedQuery(name = "XincoCoreLog.findByOpDescription", query = "SELECT x FROM XincoCoreLog x WHERE x.opDescription = :opDescription"), @NamedQuery(name = "XincoCoreLog.findByVersionHigh", query = "SELECT x FROM XincoCoreLog x WHERE x.versionHigh = :versionHigh"), @NamedQuery(name = "XincoCoreLog.findByVersionMid", query = "SELECT x FROM XincoCoreLog x WHERE x.versionMid = :versionMid"), @NamedQuery(name = "XincoCoreLog.findByVersionLow", query = "SELECT x FROM XincoCoreLog x WHERE x.versionLow = :versionLow"), @NamedQuery(name = "XincoCoreLog.findByVersionPostfix", query = "SELECT x FROM XincoCoreLog x WHERE x.versionPostfix = :versionPostfix")})
public class XincoCoreLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "XincoCoreLogGen")
    @TableGenerator(name = "XincoCoreLogGen", table = "xinco_id",
    pkColumnName = "tablename",
    valueColumnName = "last_id",
    pkColumnValue = "id",
    allocationSize = 1,
    initialValue=1000)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "op_code", nullable = false)
    private int opCode;
    @Basic(optional = false)
    @Column(name = "op_datetime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date opDatetime;
    @Basic(optional = false)
    @Column(name = "op_description", nullable = false, length = 255)
    private String opDescription;
    @Column(name = "version_high")
    private Integer versionHigh;
    @Column(name = "version_mid")
    private Integer versionMid;
    @Column(name = "version_low")
    private Integer versionLow;
    @Column(name = "version_postfix", length = 255)
    private String versionPostfix;
    @JoinColumn(name = "xinco_core_data_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoCoreData xincoCoreData;
    @JoinColumn(name = "xinco_core_user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoCoreUser xincoCoreUser;

    public XincoCoreLog() {
    }

    public XincoCoreLog(Integer id) {
        this.id = id;
    }

    public XincoCoreLog(Integer id, int opCode, Date opDatetime, String opDescription) {
        this.id = id;
        this.opCode = opCode;
        this.opDatetime = opDatetime;
        this.opDescription = opDescription;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public Date getOpDatetime() {
        return opDatetime;
    }

    public void setOpDatetime(Date opDatetime) {
        this.opDatetime = opDatetime;
    }

    public String getOpDescription() {
        return opDescription;
    }

    public void setOpDescription(String opDescription) {
        this.opDescription = opDescription;
    }

    public Integer getVersionHigh() {
        return versionHigh;
    }

    public void setVersionHigh(Integer versionHigh) {
        this.versionHigh = versionHigh;
    }

    public Integer getVersionMid() {
        return versionMid;
    }

    public void setVersionMid(Integer versionMid) {
        this.versionMid = versionMid;
    }

    public Integer getVersionLow() {
        return versionLow;
    }

    public void setVersionLow(Integer versionLow) {
        this.versionLow = versionLow;
    }

    public String getVersionPostfix() {
        return versionPostfix;
    }

    public void setVersionPostfix(String versionPostfix) {
        this.versionPostfix = versionPostfix;
    }

    public XincoCoreData getXincoCoreData() {
        return xincoCoreData;
    }

    public void setXincoCoreData(XincoCoreData xincoCoreDataId) {
        this.xincoCoreData = xincoCoreDataId;
    }

    public XincoCoreUser getXincoCoreUser() {
        return xincoCoreUser;
    }

    public void setXincoCoreUser(XincoCoreUser xincoCoreUserId) {
        this.xincoCoreUser = xincoCoreUserId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof XincoCoreLog)) {
            return false;
        }
        XincoCoreLog other = (XincoCoreLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreLog[id=" + id + "]";
    }

}
