/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.persistence;

import com.dreamer.Hibernate.audit.XincoAbstractAuditableObject;
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
 * Class: XincoCoreLog
 * Created: Mar 24, 2008 2:24:34 PM
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_log")
@NamedQueries({@NamedQuery(name = "XincoCoreLog.findById", query = "SELECT x FROM XincoCoreLog x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreLog.findByXincoCoreDataId", query = "SELECT x FROM XincoCoreLog x WHERE x.xincoCoreDataId = :xincoCoreDataId"), @NamedQuery(name = "XincoCoreLog.findByXincoCoreUserId", query = "SELECT x FROM XincoCoreLog x WHERE x.xincoCoreUserId = :xincoCoreUserId"), @NamedQuery(name = "XincoCoreLog.findByOpCode", query = "SELECT x FROM XincoCoreLog x WHERE x.opCode = :opCode"), @NamedQuery(name = "XincoCoreLog.findByOpDatetime", query = "SELECT x FROM XincoCoreLog x WHERE x.opDatetime = :opDatetime"), @NamedQuery(name = "XincoCoreLog.findByOpDescription", query = "SELECT x FROM XincoCoreLog x WHERE x.opDescription = :opDescription"), @NamedQuery(name = "XincoCoreLog.findByVersionHigh", query = "SELECT x FROM XincoCoreLog x WHERE x.versionHigh = :versionHigh"), @NamedQuery(name = "XincoCoreLog.findByVersionMid", query = "SELECT x FROM XincoCoreLog x WHERE x.versionMid = :versionMid"), @NamedQuery(name = "XincoCoreLog.findByVersionLow", query = "SELECT x FROM XincoCoreLog x WHERE x.versionLow = :versionLow"), @NamedQuery(name = "XincoCoreLog.findByVersionPostfix", query = "SELECT x FROM XincoCoreLog x WHERE x.versionPostfix = :versionPostfix")})
public class XincoCoreLog extends XincoAbstractAuditableObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "xinco_core_data_id", nullable = false)
    private int xincoCoreDataId;
    @Column(name = "xinco_core_user_id", nullable = false)
    private int xincoCoreUserId;
    @Column(name = "op_code", nullable = false)
    private int opCode;
    @Column(name = "op_datetime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date opDatetime;
    @Column(name = "op_description", nullable = false)
    private String opDescription;
    @Column(name = "version_high")
    private Integer versionHigh;
    @Column(name = "version_mid")
    private Integer versionMid;
    @Column(name = "version_low")
    private Integer versionLow;
    @Column(name = "version_postfix")
    private String versionPostfix;

    public XincoCoreLog() {
    }

    public XincoCoreLog(Integer id) {
        this.id = id;
    }

    public XincoCoreLog(Integer id, int xincoCoreDataId, int xincoCoreUserId, int opCode, Date opDatetime, String opDescription) {
        this.id = id;
        this.xincoCoreDataId = xincoCoreDataId;
        this.xincoCoreUserId = xincoCoreUserId;
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

    public int getXincoCoreDataId() {
        return xincoCoreDataId;
    }

    public void setXincoCoreDataId(int xincoCoreDataId) {
        this.xincoCoreDataId = xincoCoreDataId;
    }

    public int getXincoCoreUserId() {
        return xincoCoreUserId;
    }

    public void setXincoCoreUserId(int xincoCoreUserId) {
        this.xincoCoreUserId = xincoCoreUserId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
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
        return "com.bluecubs.xinco.persistence.XincoCoreLog[id=" + id + "]";
    }

}
