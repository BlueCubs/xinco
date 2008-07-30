/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.persistence;

import com.bluecubs.xinco.core.server.XincoAbstractAuditableObject;
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
@Table(name = "xinco_core_data")
@NamedQueries({@NamedQuery(name = "XincoCoreData.findById", query = "SELECT x FROM XincoCoreData x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreData.findByXincoCoreNodeId", query = "SELECT x FROM XincoCoreData x WHERE x.xincoCoreNodeId = :xincoCoreNodeId"), @NamedQuery(name = "XincoCoreData.findByXincoCoreLanguageId", query = "SELECT x FROM XincoCoreData x WHERE x.xincoCoreLanguageId = :xincoCoreLanguageId"), @NamedQuery(name = "XincoCoreData.findByXincoCoreDataTypeId", query = "SELECT x FROM XincoCoreData x WHERE x.xincoCoreDataTypeId = :xincoCoreDataTypeId"), @NamedQuery(name = "XincoCoreData.findByDesignation", query = "SELECT x FROM XincoCoreData x WHERE x.designation = :designation"), @NamedQuery(name = "XincoCoreData.findByStatusNumber", query = "SELECT x FROM XincoCoreData x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreData extends XincoAbstractAuditableObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "xinco_core_node_id", nullable = false)
    private int xincoCoreNodeId;
    @Column(name = "xinco_core_language_id", nullable = false)
    private int xincoCoreLanguageId;
    @Column(name = "xinco_core_data_type_id", nullable = false)
    private int xincoCoreDataTypeId;
    @Column(name = "designation", nullable = false)
    private String designation;
    @Column(name = "status_number", nullable = false)
    private int statusNumber;

    public XincoCoreData() {
    }

    public XincoCoreData(Integer id) {
        this.id = id;
    }

    public XincoCoreData(Integer id, int xincoCoreNodeId, int xincoCoreLanguageId, int xincoCoreDataTypeId, String designation, int statusNumber) {
        this.id = id;
        this.xincoCoreNodeId = xincoCoreNodeId;
        this.xincoCoreLanguageId = xincoCoreLanguageId;
        this.xincoCoreDataTypeId = xincoCoreDataTypeId;
        this.designation = designation;
        this.statusNumber = statusNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getXincoCoreNodeId() {
        return xincoCoreNodeId;
    }

    public void setXincoCoreNodeId(int xincoCoreNodeId) {
        this.xincoCoreNodeId = xincoCoreNodeId;
    }

    public int getXincoCoreLanguageId() {
        return xincoCoreLanguageId;
    }

    public void setXincoCoreLanguageId(int xincoCoreLanguageId) {
        this.xincoCoreLanguageId = xincoCoreLanguageId;
    }

    public int getXincoCoreDataTypeId() {
        return xincoCoreDataTypeId;
    }

    public void setXincoCoreDataTypeId(int xincoCoreDataTypeId) {
        this.xincoCoreDataTypeId = xincoCoreDataTypeId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
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
        if (!(object instanceof XincoCoreData)) {
            return false;
        }
        XincoCoreData other = (XincoCoreData) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.persistence.XincoCoreData[id=" + id + "]";
    }

}
