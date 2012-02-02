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
 * Name: XincoCoreUserHasXincoCoreGroupT
 * 
 * Description: Audot Trail Table
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_user_has_xinco_core_group_t")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findAll", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x"),
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByRecordId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.recordId = :recordId"),
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByXincoCoreUserId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.xincoCoreUserId = :xincoCoreUserId"),
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByXincoCoreGroupId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.xincoCoreGroupId = :xincoCoreGroupId"),
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroupT.findByStatusNumber", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroupT x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreUserHasXincoCoreGroupT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "record_id")
    private Integer recordId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "xinco_core_user_id")
    private int xincoCoreUserId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "xinco_core_group_id")
    private int xincoCoreGroupId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status_number")
    private int statusNumber;

    public XincoCoreUserHasXincoCoreGroupT() {
    }

    public XincoCoreUserHasXincoCoreGroupT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreUserHasXincoCoreGroupT(Integer recordId, int xincoCoreUserId, int xincoCoreGroupId, int statusNumber) {
        this.recordId = recordId;
        this.xincoCoreUserId = xincoCoreUserId;
        this.xincoCoreGroupId = xincoCoreGroupId;
        this.statusNumber = statusNumber;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public int getXincoCoreUserId() {
        return xincoCoreUserId;
    }

    public void setXincoCoreUserId(int xincoCoreUserId) {
        this.xincoCoreUserId = xincoCoreUserId;
    }

    public int getXincoCoreGroupId() {
        return xincoCoreGroupId;
    }

    public void setXincoCoreGroupId(int xincoCoreGroupId) {
        this.xincoCoreGroupId = xincoCoreGroupId;
    }

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoCoreUserHasXincoCoreGroupT)) {
            return false;
        }
        XincoCoreUserHasXincoCoreGroupT other = (XincoCoreUserHasXincoCoreGroupT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroupT[ recordId=" + recordId + " ]";
    }
    
}
