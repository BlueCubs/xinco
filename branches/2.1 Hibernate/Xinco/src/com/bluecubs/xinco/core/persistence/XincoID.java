package com.bluecubs.xinco.core.persistence;

import com.bluecubs.xinco.core.hibernate.audit.XincoAbstractAuditableObject;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author ortijaus
 */
@Entity
@Table(name = "xinco_id", catalog = "xinco", schema = "",
uniqueConstraints = {@UniqueConstraint(columnNames = {"tablename"})})
@NamedQueries({@NamedQuery(name = "XincoID.findAll",
    query = "SELECT x FROM XincoID x"),
    @NamedQuery(name = "XincoID.findByTablename",
    query = "SELECT x FROM XincoID x WHERE x.tablename = :tablename"),
    @NamedQuery(name = "XincoID.findByLastId",
    query = "SELECT x FROM XincoID x WHERE x.lastId = :lastId"),
    @NamedQuery(name = "XincoID.findById",
    query = "SELECT x FROM XincoID x WHERE x.id = :id")
})
public class XincoID extends XincoAbstractAuditableObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "tablename", nullable = false, length = 255)
    private String tablename;
    @Basic(optional = false)
    @Column(name = "last_id", nullable = false)
    private int lastId;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer tableId;

    public XincoID() {
    }

    public XincoID(Integer id) {
        this.tableId = id;
    }

    public XincoID(Integer id, String tablename, int lastId) {
        this.tableId = id;
        this.tablename = tablename;
        this.lastId = lastId;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public Integer getId() {
        return tableId;
    }

    public void setId(Integer id) {
        this.tableId = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tableId != null ? tableId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoID)) {
            return false;
        }
        XincoID other = (XincoID) object;
        if ((this.tableId == null && other.tableId != null) || (this.tableId != null && !this.tableId.equals(other.tableId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.persistence.XincoID[id=" + tableId + "]";
    }
}
