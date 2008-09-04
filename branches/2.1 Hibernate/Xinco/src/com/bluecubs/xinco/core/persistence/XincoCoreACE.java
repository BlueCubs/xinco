/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.persistence;

import com.bluecubs.xinco.core.hibernate.audit.XincoAbstractAuditableObject;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_ace", catalog = "xinco", schema = "")
@NamedQueries({@NamedQuery(name = "XincoCoreACE.findById",
    query = "SELECT x FROM XincoCoreACE x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreACE.findByXincoCoreUserId",
    query = "SELECT x FROM XincoCoreACE x WHERE x.xincoCoreUserId = :xincoCoreUserId"),
    @NamedQuery(name = "XincoCoreACE.findByXincoCoreGroupId",
    query = "SELECT x FROM XincoCoreACE x WHERE x.xincoCoreGroupId = :xincoCoreGroupId"),
    @NamedQuery(name = "XincoCoreACE.findByXincoCoreNodeId",
    query = "SELECT x FROM XincoCoreACE x WHERE x.xincoCoreNodeId = :xincoCoreNodeId"),
    @NamedQuery(name = "XincoCoreACE.findByXincoCoreDataId",
    query = "SELECT x FROM XincoCoreACE x WHERE x.xincoCoreDataId = :xincoCoreDataId"),
    @NamedQuery(name = "XincoCoreACE.findByReadPermission",
    query = "SELECT x FROM XincoCoreACE x WHERE x.readPermission = :readPermission"),
    @NamedQuery(name = "XincoCoreACE.findByWritePermission",
    query = "SELECT x FROM XincoCoreACE x WHERE x.writePermission = :writePermission"),
    @NamedQuery(name = "XincoCoreACE.findByExecutePermission",
    query = "SELECT x FROM XincoCoreACE x WHERE x.executePermission = :executePermission"),
    @NamedQuery(name = "XincoCoreACE.findByAdminPermission",
    query = "SELECT x FROM XincoCoreACE x WHERE x.adminPermission = :adminPermission")
})
public class XincoCoreACE extends XincoAbstractAuditableObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", nullable = false)
    private Integer aceId;
    @Column(name = "xinco_core_user_id")
    private Integer xincoCoreUserId;
    @Column(name = "xinco_core_group_id")
    private Integer xincoCoreGroupId;
    @Column(name = "xinco_core_node_id")
    private Integer xincoCoreNodeId;
    @Column(name = "xinco_core_data_id")
    private Integer xincoCoreDataId;
    @Column(name = "read_permission", nullable = false)
    private boolean readPermission;
    @Column(name = "write_permission", nullable = false)
    private boolean writePermission;
    @Column(name = "execute_permission", nullable = false)
    private boolean executePermission;
    @Column(name = "admin_permission", nullable = false)
    private boolean adminPermission;

    public XincoCoreACE() {
    }

    public XincoCoreACE(Integer id) {
        this.aceId = id;
    }

    public XincoCoreACE(Integer id, boolean readPermission, boolean writePermission, boolean executePermission, boolean adminPermission) {
        this.aceId = id;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
        this.executePermission = executePermission;
        this.adminPermission = adminPermission;
    }

    public Integer getId() {
        return aceId;
    }

    public void setId(Integer id) {
        this.aceId = id;
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
        hash += (aceId != null ? aceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoCoreACE)) {
            return false;
        }
        XincoCoreACE other = (XincoCoreACE) object;
        if ((this.aceId == null && other.aceId != null) ||
                (this.aceId != null && !this.aceId.equals(other.aceId))) {
            return false;
    }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.persistence.XincoCoreACE[id=" + aceId + "]";
    }
}
