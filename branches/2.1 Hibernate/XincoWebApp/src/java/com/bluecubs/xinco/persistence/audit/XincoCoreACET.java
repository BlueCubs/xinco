/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.persistence.audit;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Class: XincoCoreACET
 * Created: Mar 24, 2008 2:44:23 PM
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_ace_t")
@NamedQueries({@NamedQuery(name = "XincoCoreACET.findByRecordId", query = "SELECT x FROM XincoCoreACET x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoCoreACET.findByAdminPermission", query = "SELECT x FROM XincoCoreACET x WHERE x.adminPermission = :adminPermission"), @NamedQuery(name = "XincoCoreACET.findByExecutePermission", query = "SELECT x FROM XincoCoreACET x WHERE x.executePermission = :executePermission"), @NamedQuery(name = "XincoCoreACET.findById", query = "SELECT x FROM XincoCoreACET x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreACET.findByReadPermission", query = "SELECT x FROM XincoCoreACET x WHERE x.readPermission = :readPermission"), @NamedQuery(name = "XincoCoreACET.findByWritePermission", query = "SELECT x FROM XincoCoreACET x WHERE x.writePermission = :writePermission"), @NamedQuery(name = "XincoCoreACET.findByXincoCoreDataId", query = "SELECT x FROM XincoCoreACET x WHERE x.xincoCoreDataId = :xincoCoreDataId"), @NamedQuery(name = "XincoCoreACET.findByXincoCoreGroupId", query = "SELECT x FROM XincoCoreACET x WHERE x.xincoCoreGroupId = :xincoCoreGroupId"), @NamedQuery(name = "XincoCoreACET.findByXincoCoreNodeId", query = "SELECT x FROM XincoCoreACET x WHERE x.xincoCoreNodeId = :xincoCoreNodeId"), @NamedQuery(name = "XincoCoreACET.findByXincoCoreUserId", query = "SELECT x FROM XincoCoreACET x WHERE x.xincoCoreUserId = :xincoCoreUserId")})
public class XincoCoreACET implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Column(name = "admin_permission", nullable = false)
    private boolean adminPermission;
    @Column(name = "execute_permission", nullable = false)
    private boolean executePermission;
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "read_permission", nullable = false)
    private boolean readPermission;
    @Column(name = "write_permission", nullable = false)
    private boolean writePermission;
    @Column(name = "xinco_core_data_id")
    private Integer xincoCoreDataId;
    @Column(name = "xinco_core_group_id")
    private Integer xincoCoreGroupId;
    @Column(name = "xinco_core_node_id")
    private Integer xincoCoreNodeId;
    @Column(name = "xinco_core_user_id")
    private Integer xincoCoreUserId;

    public XincoCoreACET() {
    }

    public XincoCoreACET(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreACET(Integer recordId, boolean adminPermission, boolean executePermission, int id, boolean readPermission, boolean writePermission) {
        this.recordId = recordId;
        this.adminPermission = adminPermission;
        this.executePermission = executePermission;
        this.id = id;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public boolean getAdminPermission() {
        return adminPermission;
    }

    public void setAdminPermission(boolean adminPermission) {
        this.adminPermission = adminPermission;
    }

    public boolean getExecutePermission() {
        return executePermission;
    }

    public void setExecutePermission(boolean executePermission) {
        this.executePermission = executePermission;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Integer getXincoCoreDataId() {
        return xincoCoreDataId;
    }

    public void setXincoCoreDataId(Integer xincoCoreDataId) {
        this.xincoCoreDataId = xincoCoreDataId;
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

    public Integer getXincoCoreUserId() {
        return xincoCoreUserId;
    }

    public void setXincoCoreUserId(Integer xincoCoreUserId) {
        this.xincoCoreUserId = xincoCoreUserId;
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
        return "com.bluecubs.xinco.persistence.audit.XincoCoreACET[recordId=" + recordId + "]";
    }

}
