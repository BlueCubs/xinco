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
 * Name: XincoCoreAce
 * 
 * Description: Table entity
 * 
 * Original Author: Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.server.AuditedEntityListener;
import com.bluecubs.xinco.core.server.XincoAuditedObject;
import java.io.Serializable;
import javax.persistence.*;
import static javax.persistence.GenerationType.TABLE;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com
 */
@Entity
@Table(name = "xinco_core_ace")
@EntityListeners(AuditedEntityListener.class)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoCoreAce.findAll", query = "SELECT x FROM XincoCoreAce x"),
    @NamedQuery(name = "XincoCoreAce.findById", query = "SELECT x FROM XincoCoreAce x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreAce.findByReadPermission", query = "SELECT x FROM XincoCoreAce x WHERE x.readPermission = :readPermission"),
    @NamedQuery(name = "XincoCoreAce.findByWritePermission", query = "SELECT x FROM XincoCoreAce x WHERE x.writePermission = :writePermission"),
    @NamedQuery(name = "XincoCoreAce.findByExecutePermission", query = "SELECT x FROM XincoCoreAce x WHERE x.executePermission = :executePermission"),
    @NamedQuery(name = "XincoCoreAce.findByAdminPermission", query = "SELECT x FROM XincoCoreAce x WHERE x.adminPermission = :adminPermission")})
public class XincoCoreAce extends XincoAuditedObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = TABLE, generator = "XincoCoreACEGen")
    @TableGenerator(name = "XincoCoreACEGen", table = "xinco_id",
    pkColumnName = "tablename",
    valueColumnName = "last_id",
    pkColumnValue = "xinco_core_ace",
    allocationSize = 1,
    initialValue = 1000)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "read_permission")
    private boolean readPermission;
    @Basic(optional = false)
    @NotNull
    @Column(name = "write_permission")
    private boolean writePermission;
    @Basic(optional = false)
    @NotNull
    @Column(name = "execute_permission")
    private boolean executePermission;
    @Basic(optional = false)
    @NotNull
    @Column(name = "admin_permission")
    private boolean adminPermission;
    @JoinColumn(name = "xinco_core_user_id", referencedColumnName = "id")
    @ManyToOne
    private XincoCoreUser xincoCoreUser;
    @JoinColumn(name = "xinco_core_data_id", referencedColumnName = "id")
    @ManyToOne
    private XincoCoreData xincoCoreData;
    @JoinColumn(name = "xinco_core_node_id", referencedColumnName = "id")
    @ManyToOne
    private XincoCoreNode xincoCoreNode;
    @JoinColumn(name = "xinco_core_group_id", referencedColumnName = "id")
    @ManyToOne
    private XincoCoreGroup xincoCoreGroup;

    public XincoCoreAce() {
    }

    public XincoCoreAce(Integer id) {
        this.id = id;
    }

    public XincoCoreAce(Integer id, boolean readPermission, boolean writePermission, boolean executePermission, boolean adminPermission) {
        this.id = id;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
        this.executePermission = executePermission;
        this.adminPermission = adminPermission;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getReadPermission() {
        return readPermission;
    }

    public void setReadPermission(boolean readPermission) {
        this.readPermission = readPermission;
    }

    public boolean getWritePermission() {
        return writePermission;
    }

    public void setWritePermission(boolean writePermission) {
        this.writePermission = writePermission;
    }

    public boolean getExecutePermission() {
        return executePermission;
    }

    public void setExecutePermission(boolean executePermission) {
        this.executePermission = executePermission;
    }

    public boolean getAdminPermission() {
        return adminPermission;
    }

    public void setAdminPermission(boolean adminPermission) {
        this.adminPermission = adminPermission;
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

    public XincoCoreNode getXincoCoreNode() {
        return xincoCoreNode;
    }

    public void setXincoCoreNode(XincoCoreNode xincoCoreNode) {
        this.xincoCoreNode = xincoCoreNode;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XincoCoreAce)) {
            return false;
        }
        XincoCoreAce other = (XincoCoreAce) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreAce[ id=" + id + " ]";
    }
}
