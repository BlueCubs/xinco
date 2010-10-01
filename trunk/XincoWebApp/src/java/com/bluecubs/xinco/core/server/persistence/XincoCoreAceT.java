/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence;

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
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_ace_t")
@NamedQueries({@NamedQuery(name = "XincoCoreAceT.findAll", query = "SELECT x FROM XincoCoreAceT x"), @NamedQuery(name = "XincoCoreAceT.findByRecordId", query = "SELECT x FROM XincoCoreAceT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoCoreAceT.findById", query = "SELECT x FROM XincoCoreAceT x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreAceT.findByXincoCoreUserId", query = "SELECT x FROM XincoCoreAceT x WHERE x.xincoCoreUserId = :xincoCoreUserId"), @NamedQuery(name = "XincoCoreAceT.findByXincoCoreGroupId", query = "SELECT x FROM XincoCoreAceT x WHERE x.xincoCoreGroupId = :xincoCoreGroupId"), @NamedQuery(name = "XincoCoreAceT.findByXincoCoreNodeId", query = "SELECT x FROM XincoCoreAceT x WHERE x.xincoCoreNodeId = :xincoCoreNodeId"), @NamedQuery(name = "XincoCoreAceT.findByXincoCoreDataId", query = "SELECT x FROM XincoCoreAceT x WHERE x.xincoCoreDataId = :xincoCoreDataId"), @NamedQuery(name = "XincoCoreAceT.findByReadPermission", query = "SELECT x FROM XincoCoreAceT x WHERE x.readPermission = :readPermission"), @NamedQuery(name = "XincoCoreAceT.findByWritePermission", query = "SELECT x FROM XincoCoreAceT x WHERE x.writePermission = :writePermission"), @NamedQuery(name = "XincoCoreAceT.findByExecutePermission", query = "SELECT x FROM XincoCoreAceT x WHERE x.executePermission = :executePermission"), @NamedQuery(name = "XincoCoreAceT.findByAdminPermission", query = "SELECT x FROM XincoCoreAceT x WHERE x.adminPermission = :adminPermission")})
public class XincoCoreAceT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
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

    public XincoCoreAceT() {
    }

    public XincoCoreAceT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreAceT(Integer recordId, int id, boolean readPermission, boolean writePermission, boolean executePermission, boolean adminPermission) {
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
        
        if (!(object instanceof XincoCoreAceT)) {
            return false;
        }
        XincoCoreAceT other = (XincoCoreAceT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreAceT[recordId=" + recordId + "]";
    }

}
