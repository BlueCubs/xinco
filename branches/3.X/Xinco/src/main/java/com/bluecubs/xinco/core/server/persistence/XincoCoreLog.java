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
 * Name: XincoCoreLog
 * 
 * Description: XincoCoreLog JPA class
 * 
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import static javax.persistence.GenerationType.TABLE;
import static javax.persistence.TemporalType.TIMESTAMP;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreLog.findAll", query = "SELECT x FROM XincoCoreLog x"),
    @NamedQuery(name = "XincoCoreLog.findById", query = "SELECT x FROM XincoCoreLog x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreLog.findByOpCode", query = "SELECT x FROM XincoCoreLog x WHERE x.opCode = :opCode"),
    @NamedQuery(name = "XincoCoreLog.findByOpDatetime", query = "SELECT x FROM XincoCoreLog x WHERE x.opDatetime = :opDatetime"),
    @NamedQuery(name = "XincoCoreLog.findByOpDescription", query = "SELECT x FROM XincoCoreLog x WHERE x.opDescription = :opDescription"),
    @NamedQuery(name = "XincoCoreLog.findByVersionHigh", query = "SELECT x FROM XincoCoreLog x WHERE x.versionHigh = :versionHigh"),
    @NamedQuery(name = "XincoCoreLog.findByVersionMid", query = "SELECT x FROM XincoCoreLog x WHERE x.versionMid = :versionMid"),
    @NamedQuery(name = "XincoCoreLog.findByVersionLow", query = "SELECT x FROM XincoCoreLog x WHERE x.versionLow = :versionLow"),
    @NamedQuery(name = "XincoCoreLog.findByVersionPostfix", query = "SELECT x FROM XincoCoreLog x WHERE x.versionPostfix = :versionPostfix")})
public class XincoCoreLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = TABLE, generator = "XincoCoreLogGen")
    @TableGenerator(name = "XincoCoreLogGen", table = "xinco_id",
    pkColumnName = "tablename",
    valueColumnName = "last_id",
    pkColumnValue = "xinco_core_log",
    allocationSize = 1,
    initialValue = 1000)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "op_code")
    private int opCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "op_datetime")
    @Temporal(TIMESTAMP)
    private Date opDatetime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "op_description")
    private String opDescription;
    @Column(name = "version_high")
    private Integer versionHigh;
    @Column(name = "version_mid")
    private Integer versionMid;
    @Column(name = "version_low")
    private Integer versionLow;
    @Size(max = 255)
    @Column(name = "version_postfix")
    private String versionPostfix;
    @JoinColumn(name = "xinco_core_user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private XincoCoreUser xincoCoreUser;
    @JoinColumn(name = "xinco_core_data_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private XincoCoreData xincoCoreData;

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

    public XincoCoreUser getXincoCoreUser() {
        return xincoCoreUser;
    }

    public void setXincoCoreUser(XincoCoreUser xincoCoreUser) {
        this.xincoCoreUser = xincoCoreUser;
    }

    public XincoCoreData getXincoCoreData() {
        return xincoCoreData;
    }

    public void setXincoCoreData(XincoCoreData xincoCoreData) {
        this.xincoCoreData = xincoCoreData;
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
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreLog[ id=" + id + " ]";
    }
}
