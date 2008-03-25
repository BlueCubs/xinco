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
 * Class: XincoCoreNodeT
 * Created: Mar 24, 2008 2:44:22 PM
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_node_t")
@NamedQueries({@NamedQuery(name = "XincoCoreNodeT.findByRecordId", query = "SELECT x FROM XincoCoreNodeT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoCoreNodeT.findByDesignation", query = "SELECT x FROM XincoCoreNodeT x WHERE x.designation = :designation"), @NamedQuery(name = "XincoCoreNodeT.findById", query = "SELECT x FROM XincoCoreNodeT x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreNodeT.findByStatusNumber", query = "SELECT x FROM XincoCoreNodeT x WHERE x.statusNumber = :statusNumber"), @NamedQuery(name = "XincoCoreNodeT.findByXincoCoreLanguageId", query = "SELECT x FROM XincoCoreNodeT x WHERE x.xincoCoreLanguageId = :xincoCoreLanguageId"), @NamedQuery(name = "XincoCoreNodeT.findByXincoCoreNodeId", query = "SELECT x FROM XincoCoreNodeT x WHERE x.xincoCoreNodeId = :xincoCoreNodeId")})
public class XincoCoreNodeT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Column(name = "designation", nullable = false)
    private String designation;
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "status_number", nullable = false)
    private int statusNumber;
    @Column(name = "xinco_core_language_id", nullable = false)
    private int xincoCoreLanguageId;
    @Column(name = "xinco_core_node_id", nullable = false)
    private int xincoCoreNodeId;

    public XincoCoreNodeT() {
    }

    public XincoCoreNodeT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoCoreNodeT(Integer recordId, String designation, int id, int statusNumber, int xincoCoreLanguageId, int xincoCoreNodeId) {
        this.recordId = recordId;
        this.designation = designation;
        this.id = id;
        this.statusNumber = statusNumber;
        this.xincoCoreLanguageId = xincoCoreLanguageId;
        this.xincoCoreNodeId = xincoCoreNodeId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    public int getXincoCoreLanguageId() {
        return xincoCoreLanguageId;
    }

    public void setXincoCoreLanguageId(int xincoCoreLanguageId) {
        this.xincoCoreLanguageId = xincoCoreLanguageId;
    }

    public int getXincoCoreNodeId() {
        return xincoCoreNodeId;
    }

    public void setXincoCoreNodeId(int xincoCoreNodeId) {
        this.xincoCoreNodeId = xincoCoreNodeId;
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
        if (!(object instanceof XincoCoreNodeT)) {
            return false;
        }
        XincoCoreNodeT other = (XincoCoreNodeT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.persistence.audit.XincoCoreNodeT[recordId=" + recordId + "]";
    }

}
