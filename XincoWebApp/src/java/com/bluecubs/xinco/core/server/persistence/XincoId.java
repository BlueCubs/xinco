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
 * Name: XincoId
 * 
 * Description: XincoId JPA class
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
@Table(name = "xinco_id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoId.findAll", query = "SELECT x FROM XincoId x"),
    @NamedQuery(name = "XincoId.findById", query = "SELECT x FROM XincoId x WHERE x.id = :id"),
    @NamedQuery(name = "XincoId.findByLastId", query = "SELECT x FROM XincoId x WHERE x.lastId = :lastId"),
    @NamedQuery(name = "XincoId.findByTablename", query = "SELECT x FROM XincoId x WHERE x.tablename = :tablename")})
public class XincoId implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "XincoIdGen")
    @SequenceGenerator(name = "XincoIdGen", sequenceName = "XINCO_ID_SEQ",
    allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "last_id")
    private Integer lastId;
    @Size(min = 1, max = 255)
    @Column(name = "tablename")
    private String tablename;

    public XincoId() {
    }

    public XincoId(String tablename, int lastId) {
        this.tablename = tablename;
        this.lastId = lastId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLastId() {
        return lastId;
    }

    public void setLastId(Integer lastId) {
        this.lastId = lastId;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoId)) {
            return false;
        }
        XincoId other = (XincoId) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoId[ id=" + id + " ]";
    }
}
