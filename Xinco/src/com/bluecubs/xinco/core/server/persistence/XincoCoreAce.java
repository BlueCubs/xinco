package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.server.AuditedEntityListener;
import com.bluecubs.xinco.core.server.XincoAuditedObject;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.CascadeType;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_ace")
@EntityListeners(AuditedEntityListener.class)
@NamedQueries({
    @NamedQuery(name = "XincoCoreAce.findAll",
    query = "SELECT x FROM XincoCoreAce x"),
    @NamedQuery(name = "XincoCoreAce.findById",
    query = "SELECT x FROM XincoCoreAce x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreAce.findByReadPermission",
    query = "SELECT x FROM XincoCoreAce x WHERE x.readPermission = :readPermission"),
    @NamedQuery(name = "XincoCoreAce.findByWritePermission",
    query = "SELECT x FROM XincoCoreAce x WHERE x.writePermission = :writePermission"),
    @NamedQuery(name = "XincoCoreAce.findByExecutePermission",
    query = "SELECT x FROM XincoCoreAce x WHERE x.executePermission = :executePermission"),
    @NamedQuery(name = "XincoCoreAce.findByAdminPermission",
    query = "SELECT x FROM XincoCoreAce x WHERE x.adminPermission = :adminPermission")})
public class XincoCoreAce extends XincoAuditedObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
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
    @JoinColumn(name = "xinco_core_data_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private XincoCoreData xincoCoreDataId;
    @JoinColumn(name = "xinco_core_group_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private XincoCoreGroup xincoCoreGroupId;
    @JoinColumn(name = "xinco_core_node_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private XincoCoreNode xincoCoreNodeId;
    @JoinColumn(name = "xinco_core_user_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private XincoCoreUser xincoCoreUserId;

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

    public XincoCoreData getXincoCoreDataId() {
        return xincoCoreDataId;
    }

    public void setXincoCoreDataId(XincoCoreData xincoCoreDataId) {
        this.xincoCoreDataId = xincoCoreDataId;
    }

    public XincoCoreGroup getXincoCoreGroupId() {
        return xincoCoreGroupId;
    }

    public void setXincoCoreGroupId(XincoCoreGroup xincoCoreGroupId) {
        this.xincoCoreGroupId = xincoCoreGroupId;
    }

    public XincoCoreNode getXincoCoreNodeId() {
        return xincoCoreNodeId;
    }

    public void setXincoCoreNodeId(XincoCoreNode xincoCoreNodeId) {
        this.xincoCoreNodeId = xincoCoreNodeId;
    }

    public XincoCoreUser getXincoCoreUserId() {
        return xincoCoreUserId;
    }

    public void setXincoCoreUserId(XincoCoreUser xincoCoreUserId) {
        this.xincoCoreUserId = xincoCoreUserId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
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
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreAce[id=" + id + "]";
    }
}
