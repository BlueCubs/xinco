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
 * Name: XincoSettingT
 * 
 * Description: Audot Trail Table
 * 
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_setting_t")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoSettingT.findAll", query = "SELECT x FROM XincoSettingT x"),
    @NamedQuery(name = "XincoSettingT.findByRecordId", query = "SELECT x FROM XincoSettingT x WHERE x.recordId = :recordId"),
    @NamedQuery(name = "XincoSettingT.findById", query = "SELECT x FROM XincoSettingT x WHERE x.id = :id"),
    @NamedQuery(name = "XincoSettingT.findByDescription", query = "SELECT x FROM XincoSettingT x WHERE x.description = :description"),
    @NamedQuery(name = "XincoSettingT.findByIntValue", query = "SELECT x FROM XincoSettingT x WHERE x.intValue = :intValue"),
    @NamedQuery(name = "XincoSettingT.findByBoolValue", query = "SELECT x FROM XincoSettingT x WHERE x.boolValue = :boolValue"),
    @NamedQuery(name = "XincoSettingT.findByLongValue", query = "SELECT x FROM XincoSettingT x WHERE x.longValue = :longValue")})
public class XincoSettingT implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "record_id")
    private Integer recordId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "description")
    private String description;
    @Column(name = "int_value")
    private Integer intValue;
    @Lob
    @Size(max = 65535)
    @Column(name = "string_value")
    private String stringValue;
    @Column(name = "bool_value")
    private Boolean boolValue;
    @Column(name = "long_value")
    private long longValue;

    public XincoSettingT() {
    }

    public XincoSettingT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoSettingT(Integer recordId, int id, String description, int intVal,
            String stringVal, boolean boolVal, long longVal) {
        this.recordId = recordId;
        this.id = id;
        this.description = description;
        this.intValue = intVal;
        this.longValue = longVal;
        this.stringValue = stringVal;
        this.boolValue = boolVal;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public void setBoolValue(Boolean boolValue) {
        this.boolValue = boolValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoSettingT)) {
            return false;
        }
        XincoSettingT other = (XincoSettingT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoSettingT[ recordId=" + recordId + " ]";
    }
}
