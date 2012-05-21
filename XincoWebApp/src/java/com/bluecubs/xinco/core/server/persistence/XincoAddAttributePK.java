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
 * Name: XincoAddAttributePK
 * 
 * Description: Table entity
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
public class XincoAddAttributePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "xinco_core_data_id")
    private int xincoCoreDataId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "attribute_id")
    private int attributeId;

    public XincoAddAttributePK() {
    }

    public XincoAddAttributePK(int xincoCoreDataId, int attributeId) {
        this.xincoCoreDataId = xincoCoreDataId;
        this.attributeId = attributeId;
    }

    public int getXincoCoreDataId() {
        return xincoCoreDataId;
    }

    public void setXincoCoreDataId(int xincoCoreDataId) {
        this.xincoCoreDataId = xincoCoreDataId;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) xincoCoreDataId;
        hash += (int) attributeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoAddAttributePK)) {
            return false;
        }
        XincoAddAttributePK other = (XincoAddAttributePK) object;
        if (this.xincoCoreDataId != other.xincoCoreDataId) {
            return false;
        }
        if (this.attributeId != other.attributeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoAddAttributePK[ xincoCoreDataId=" + xincoCoreDataId + ", attributeId=" + attributeId + " ]";
    }
}
