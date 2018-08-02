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
 * Name: XincoCoreLanguageT
 * 
 * Description: Audot Trail Table
 * 
 * Original Author: Javier A. Ortiz Bultron javier.ortiz.78@gmail.com Date: Nov 29, 2011
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
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
@Entity
@Table(name = "xinco_core_language_t")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreLanguageT.findAll", query = "SELECT x FROM XincoCoreLanguageT x"),
    @NamedQuery(name = "XincoCoreLanguageT.findByRecordId", query = "SELECT x FROM XincoCoreLanguageT x WHERE x.recordId = :recordId"),
    @NamedQuery(name = "XincoCoreLanguageT.findById", query = "SELECT x FROM XincoCoreLanguageT x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreLanguageT.findBySign", query = "SELECT x FROM XincoCoreLanguageT x WHERE x.sign = :sign"),
    @NamedQuery(name = "XincoCoreLanguageT.findByDesignation", query = "SELECT x FROM XincoCoreLanguageT x WHERE x.designation = :designation")})
public class XincoCoreLanguageT implements Serializable {

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
    @Size(min = 1, max = 255)
    @Column(name = "sign")
    private String sign;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "designation")
    private String designation;

    public XincoCoreLanguageT() {
    }

    public XincoCoreLanguageT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreLanguageT(Integer recordId, int id, String sign, String designation) {
        this.recordId = recordId;
        this.id = id;
        this.sign = sign;
        this.designation = designation;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoCoreLanguageT)) {
            return false;
        }
        XincoCoreLanguageT other = (XincoCoreLanguageT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreLanguageT[ recordId=" + recordId + " ]";
    }
}
