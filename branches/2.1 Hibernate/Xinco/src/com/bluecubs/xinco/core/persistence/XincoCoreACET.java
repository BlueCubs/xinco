/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Javier
 */
@Entity
@Table(name = "xinco_core_ace_t", catalog = "xinco", schema = "")
@NamedQueries({@NamedQuery(name = "XincoCoreACET.findAll", query = "SELECT x FROM XincoCoreACET x"), @NamedQuery(name = "XincoCoreACET.findByRecordId", query = "SELECT x FROM XincoCoreACET x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoCoreACET.findById", query = "SELECT x FROM XincoCoreACET x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreACET.findByXincoCoreUserId", query = "SELECT x FROM XincoCoreACET x WHERE x.xincoCoreUserId = :xincoCoreUserId"), @NamedQuery(name = "XincoCoreACET.findByXincoCoreGroupId", query = "SELECT x FROM XincoCoreACET x WHERE x.xincoCoreGroupId = :xincoCoreGroupId"), @NamedQuery(name = "XincoCoreACET.findByXincoCoreNodeId", query = "SELECT x FROM XincoCoreACET x WHERE x.xincoCoreNodeId = :xincoCoreNodeId"), @NamedQuery(name = "XincoCoreACET.findByXincoCoreDataId", query = "SELECT x FROM XincoCoreACET x WHERE x.xincoCoreDataId = :xincoCoreDataId"), @NamedQuery(name = "XincoCoreACET.findByReadPermission", query = "SELECT x FROM XincoCoreACET x WHERE x.readPermission = :readPermission"), @NamedQuery(name = "XincoCoreACET.findByWritePermission", query = "SELECT x FROM XincoCoreACET x WHERE x.writePermission = :writePermission"), @NamedQuery(name = "XincoCoreACET.findByExecutePermission", query = "SELECT x FROM XincoCoreACET x WHERE x.executePermission = :executePermission"), @NamedQuery(name = "XincoCoreACET.findByAdminPermission", query = "SELECT x FROM XincoCoreACET x WHERE x.adminPermission = :adminPermission")})
public class XincoCoreACET implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "xinco_core_user_id")
    private Integer xincoCoreUserId;
    @Column(name = "xinco_core_group_id")
    private Integer xincoCoreGroupId;
    @Column(name = "xinco_core_node_id")
    private Integer xincoCoreNodeId;
    @Column(name = "xinco_core_data_id")
    private Integer xincoCoreDataId;
    @Basic(optional = false)
    @Column(name = "read_permission", nullable = false)
    private boolean readPermission;
    @Basic(optional = false)
    @Column(name = "write_permission", nullable = false)
    private boolean writePermission;
    @Basic(optional = false)
    @Column(name = "execute_permission", nullable = false)
    private boolean executePermission;
    @Basic(optional = false)
    @Column(name = "admin_permission", nullable = false)
    private boolean adminPermission;

    public XincoCoreACET() {
    }

    public XincoCoreACET(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreACET(Integer recordId, int id, boolean readPermission, boolean writePermission, boolean executePermission, boolean adminPermission) {
        this.recordId = recordId;
        this.id = id;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
        this.executePermission = executePermission;
        this.adminPermission = adminPermission;
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

    public Integer getXincoCoreUserId() {
        return xincoCoreUserId;
    }

    public void setXincoCoreUserId(Integer xincoCoreUserId) {
        this.xincoCoreUserId = xincoCoreUserId;
    }

    public Integer getXincoCoreGroupId() {
        return xincoCoreGroupId;
    }

    public void setXincoCoreGroupId(Integer xincoCoreGroupId) {
        this.xincoCoreGroupId = xincoCoreGroupId;
    }

    public Integer getXincoCoreNodeId() {
        return xincoCoreNodeId;
    }

    public void setXincoCoreNodeId(Integer xincoCoreNodeId) {
        this.xincoCoreNodeId = xincoCoreNodeId;
    }

    public Integer getXincoCoreDataId() {
        return xincoCoreDataId;
    }

    public void setXincoCoreDataId(Integer xincoCoreDataId) {
        this.xincoCoreDataId = xincoCoreDataId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoCoreACET)) {
            return false;
        }
        XincoCoreACET other = (XincoCoreACET) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.persistence.XincoCoreACET[recordId=" + recordId + "]";
    }

}
