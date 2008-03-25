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
 * Class: XincoIdT
 * Created: Mar 24, 2008 2:44:21 PM
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_id_t")
@NamedQueries({@NamedQuery(name = "XincoIdT.findByRecordId", query = "SELECT x FROM XincoIdT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoIdT.findByLastId", query = "SELECT x FROM XincoIdT x WHERE x.lastId = :lastId"), @NamedQuery(name = "XincoIdT.findByTablename", query = "SELECT x FROM XincoIdT x WHERE x.tablename = :tablename")})
public class XincoIdT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Column(name = "last_id", nullable = false)
    private int lastId;
    @Column(name = "tablename", nullable = false)
    private String tablename;

    public XincoIdT() {
    }

    public XincoIdT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoIdT(Integer recordId, int lastId, String tablename) {
        this.recordId = recordId;
        this.lastId = lastId;
        this.tablename = tablename;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
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
        if (!(object instanceof XincoIdT)) {
            return false;
        }
        XincoIdT other = (XincoIdT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.persistence.audit.XincoIdT[recordId=" + recordId + "]";
    }

}
