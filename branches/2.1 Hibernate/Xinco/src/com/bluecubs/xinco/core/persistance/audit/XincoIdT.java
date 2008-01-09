/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.persistance.audit;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Javier A. Ortiz
 */
@Entity
@Table(name = "xinco_id_t")
@NamedQueries({@NamedQuery(name = "XincoIdT.findByRecordId", query = "SELECT x FROM XincoIdT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoIdT.findByTablename", query = "SELECT x FROM XincoIdT x WHERE x.tablename = :tablename"), @NamedQuery(name = "XincoIdT.findByLastId", query = "SELECT x FROM XincoIdT x WHERE x.lastId = :lastId")})
public class XincoIdT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Column(name = "tablename", nullable = false)
    private String tablename;
    @Column(name = "last_id", nullable = false)
    private int lastId;

    public XincoIdT() {
    }

    public XincoIdT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoIdT(Integer recordId, String tablename, int lastId) {
        this.recordId = recordId;
        this.tablename = tablename;
        this.lastId = lastId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
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
        return "com.bluecubs.xinco.core.persistance.audit.XincoIdT[recordId=" + recordId + "]";
    }

}
