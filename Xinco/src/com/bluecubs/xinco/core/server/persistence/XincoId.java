/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence;

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

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_id")
@NamedQueries({@NamedQuery(name = "XincoId.findAll", query = "SELECT x FROM XincoId x"), @NamedQuery(name = "XincoId.findById", query = "SELECT x FROM XincoId x WHERE x.id = :id"), @NamedQuery(name = "XincoId.findByTablename", query = "SELECT x FROM XincoId x WHERE x.tablename = :tablename"), @NamedQuery(name = "XincoId.findByLastId", query = "SELECT x FROM XincoId x WHERE x.lastId = :lastId")})
public class XincoId implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "tablename", nullable = false, length = 255)
    private String tablename;
    @Basic(optional = false)
    @Column(name = "last_id", nullable = false)
    private int lastId;

    public XincoId() {
    }

    public XincoId(Integer id) {
        this.id = id;
    }

    public XincoId(Integer id, String tablename, int lastId) {
        this.id = id;
        this.tablename = tablename;
        this.lastId = lastId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
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
        return "com.bluecubs.xinco.core.server.persistence.XincoId[id=" + id + "]";
    }

}
