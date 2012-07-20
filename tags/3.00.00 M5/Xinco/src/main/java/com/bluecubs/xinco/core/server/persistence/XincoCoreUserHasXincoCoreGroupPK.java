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
 * Name: XincoCoreUserHasXincoCoreGroupPK
 * 
 * Description: PK JPA class
 * 
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Embeddable
public class XincoCoreUserHasXincoCoreGroupPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "xinco_core_user_id")
    private int xincoCoreUserId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "xinco_core_group_id")
    private int xincoCoreGroupId;

    public XincoCoreUserHasXincoCoreGroupPK() {
    }

    public XincoCoreUserHasXincoCoreGroupPK(int xincoCoreUserId, int xincoCoreGroupId) {
        this.xincoCoreUserId = xincoCoreUserId;
        this.xincoCoreGroupId = xincoCoreGroupId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) xincoCoreUserId;
        hash += (int) xincoCoreGroupId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoCoreUserHasXincoCoreGroupPK)) {
            return false;
        }
        XincoCoreUserHasXincoCoreGroupPK other = (XincoCoreUserHasXincoCoreGroupPK) object;
        if (this.xincoCoreUserId != other.xincoCoreUserId) {
            return false;
        }
        if (this.xincoCoreGroupId != other.xincoCoreGroupId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroupPK[ xincoCoreUserId=" + xincoCoreUserId + ", xincoCoreGroupId=" + xincoCoreGroupId + " ]";
    }
}
