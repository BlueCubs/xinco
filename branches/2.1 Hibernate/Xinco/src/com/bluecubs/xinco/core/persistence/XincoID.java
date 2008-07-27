package com.bluecubs.xinco.core.persistence;

import com.bluecubs.xinco.core.server.XincoAbstractAuditableObject;
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
@Table(name = "xinco_id", catalog = "xinco", schema = "")
@NamedQueries({@NamedQuery(name = "XincoID.findAll", query = "SELECT x FROM XincoID x"), @NamedQuery(name = "XincoID.findByTablename", query = "SELECT x FROM XincoID x WHERE x.tablename = :tablename"), @NamedQuery(name = "XincoID.findById", query = "SELECT x FROM XincoID x WHERE x.id = :id"), @NamedQuery(name = "XincoID.findByLastId", query = "SELECT x FROM XincoID x WHERE x.lastId = :lastId")})
public class XincoID extends XincoAbstractAuditableObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "tablename", nullable = false, length = 255)
    private String tablename;
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "last_id", nullable = false)
    private int lastId;

    public XincoID() {
    }

    public XincoID(Integer id) {
        this.id = id;
    }

    public XincoID(Integer id, String tablename, int lastId) {
        this.id = id;
        this.tablename = tablename;
        this.lastId = lastId;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
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
        if (!(object instanceof XincoID)) {
            return false;
        }
        XincoID other = (XincoID) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.persistence.XincoID[id=" + id + "]";
    }

}
