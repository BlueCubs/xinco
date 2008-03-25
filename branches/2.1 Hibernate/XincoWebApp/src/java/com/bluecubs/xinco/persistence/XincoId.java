/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.persistence;

import com.dreamer.Hibernate.audit.XincoAbstractAuditableObject;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Class: XincoId
 * Created: Mar 24, 2008 2:29:52 PM
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_id")
@NamedQueries({@NamedQuery(name = "XincoId.findByTablename", query = "SELECT x FROM XincoId x WHERE x.tablename = :tablename"), @NamedQuery(name = "XincoId.findByLastId", query = "SELECT x FROM XincoId x WHERE x.lastId = :lastId")})
public class XincoId extends XincoAbstractAuditableObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "tablename", nullable = false)
    private String tablename;
    @Column(name = "last_id", nullable = false)
    private int lastId;

    public XincoId() {
    }

    public XincoId(String tablename) {
        this.tablename = tablename;
    }

    public XincoId(String tablename, int lastId) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tablename != null ? tablename.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoId)) {
            return false;
        }
        XincoId other = (XincoId) object;
        if ((this.tablename == null && other.tablename != null) || (this.tablename != null && !this.tablename.equals(other.tablename))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.persistence.XincoId[tablename=" + tablename + "]";
    }

}
