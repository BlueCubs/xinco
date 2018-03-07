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
 * Name: XincoCoreUserHasXincoCoreGroup
 * 
 * Description: XincoCoreUserhasXincoCoreGroup JPA class
 * 
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
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
 * @author Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com
 */
@Entity
@Table(name = "xinco_core_user_has_xinco_core_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroup.findAll", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroup x"),
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroup.findByXincoCoreUserId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreUserId = :xincoCoreUserId"),
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroup.findByXincoCoreGroupId", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE x.xincoCoreUserHasXincoCoreGroupPK.xincoCoreGroupId = :xincoCoreGroupId"),
    @NamedQuery(name = "XincoCoreUserHasXincoCoreGroup.findByStatusNumber", query = "SELECT x FROM XincoCoreUserHasXincoCoreGroup x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreUserHasXincoCoreGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoCoreUserHasXincoCoreGroupPK xincoCoreUserHasXincoCoreGroupPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status_number")
    private int statusNumber;
    @JoinColumn(name = "xinco_core_user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private XincoCoreUser xincoCoreUser;
    @JoinColumn(name = "xinco_core_group_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private XincoCoreGroup xincoCoreGroup;

    public XincoCoreUserHasXincoCoreGroup() {
    }

    public XincoCoreUserHasXincoCoreGroup(XincoCoreUserHasXincoCoreGroupPK xincoCoreUserHasXincoCoreGroupPK) {
        this.xincoCoreUserHasXincoCoreGroupPK = xincoCoreUserHasXincoCoreGroupPK;
    }

    public XincoCoreUserHasXincoCoreGroup(XincoCoreUserHasXincoCoreGroupPK xincoCoreUserHasXincoCoreGroupPK, int statusNumber) {
        this.xincoCoreUserHasXincoCoreGroupPK = xincoCoreUserHasXincoCoreGroupPK;
        this.statusNumber = statusNumber;
    }

    public XincoCoreUserHasXincoCoreGroup(int xincoCoreUserId, int xincoCoreGroupId) {
        this.xincoCoreUserHasXincoCoreGroupPK = new XincoCoreUserHasXincoCoreGroupPK(xincoCoreUserId, xincoCoreGroupId);
    }

    public XincoCoreUserHasXincoCoreGroupPK getXincoCoreUserHasXincoCoreGroupPK() {
        return xincoCoreUserHasXincoCoreGroupPK;
    }

    public void setXincoCoreUserHasXincoCoreGroupPK(XincoCoreUserHasXincoCoreGroupPK xincoCoreUserHasXincoCoreGroupPK) {
        this.xincoCoreUserHasXincoCoreGroupPK = xincoCoreUserHasXincoCoreGroupPK;
    }

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    public XincoCoreUser getXincoCoreUser() {
        return xincoCoreUser;
    }

    public void setXincoCoreUser(XincoCoreUser xincoCoreUser) {
        this.xincoCoreUser = xincoCoreUser;
    }

    public XincoCoreGroup getXincoCoreGroup() {
        return xincoCoreGroup;
    }

    public void setXincoCoreGroup(XincoCoreGroup xincoCoreGroup) {
        this.xincoCoreGroup = xincoCoreGroup;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xincoCoreUserHasXincoCoreGroupPK != null ? xincoCoreUserHasXincoCoreGroupPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoCoreUserHasXincoCoreGroup)) {
            return false;
        }
        XincoCoreUserHasXincoCoreGroup other = (XincoCoreUserHasXincoCoreGroup) object;
        if ((this.xincoCoreUserHasXincoCoreGroupPK == null && other.xincoCoreUserHasXincoCoreGroupPK != null) || (this.xincoCoreUserHasXincoCoreGroupPK != null && !this.xincoCoreUserHasXincoCoreGroupPK.equals(other.xincoCoreUserHasXincoCoreGroupPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup[ xincoCoreUserHasXincoCoreGroupPK=" + xincoCoreUserHasXincoCoreGroupPK + " ]";
    }
}
