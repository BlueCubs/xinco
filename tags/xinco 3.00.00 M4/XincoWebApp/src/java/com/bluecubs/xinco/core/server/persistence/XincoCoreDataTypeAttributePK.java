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
 * Name: XincoCoreDataTypeAttributePK
 * 
 * Description: Audit Trail Table
 * 
 * Original Author: Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
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
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Embeddable
public class XincoCoreDataTypeAttributePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "xinco_core_data_type_id")
    private int xincoCoreDataTypeId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "attribute_id")
    private int attributeId;

    public XincoCoreDataTypeAttributePK() {
    }

    public XincoCoreDataTypeAttributePK(int xincoCoreDataTypeId, int attributeId) {
        this.xincoCoreDataTypeId = xincoCoreDataTypeId;
        this.attributeId = attributeId;
    }

    public int getXincoCoreDataTypeId() {
        return xincoCoreDataTypeId;
    }

    public void setXincoCoreDataTypeId(int xincoCoreDataTypeId) {
        this.xincoCoreDataTypeId = xincoCoreDataTypeId;
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
        hash += (int) xincoCoreDataTypeId;
        hash += (int) attributeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoCoreDataTypeAttributePK)) {
            return false;
        }
        XincoCoreDataTypeAttributePK other = (XincoCoreDataTypeAttributePK) object;
        if (this.xincoCoreDataTypeId != other.xincoCoreDataTypeId) {
            return false;
        }
        if (this.attributeId != other.attributeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttributePK[ xincoCoreDataTypeId=" + xincoCoreDataTypeId + ", attributeId=" + attributeId + " ]";
    }
    
}
