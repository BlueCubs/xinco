/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.server.AuditedEntityListener;
import com.bluecubs.xinco.core.server.XincoAuditedObject;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Javier A. Ortiz Bultr�n <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_data_type")
@EntityListeners(AuditedEntityListener.class)
@NamedQueries({@NamedQuery(name = "XincoCoreDataType.findAll", query = "SELECT x FROM XincoCoreDataType x"), @NamedQuery(name = "XincoCoreDataType.findById", query = "SELECT x FROM XincoCoreDataType x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreDataType.findByDesignation", query = "SELECT x FROM XincoCoreDataType x WHERE x.designation = :designation"), @NamedQuery(name = "XincoCoreDataType.findByDescription", query = "SELECT x FROM XincoCoreDataType x WHERE x.description = :description")})
public class XincoCoreDataType extends XincoAuditedObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 255)
    private String designation;
    @Basic(optional = false)
    @Column(name = "description", nullable = false, length = 255)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreDataType", fetch = FetchType.LAZY)
    private List<XincoCoreDataTypeAttribute> xincoCoreDataTypeAttributeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreDataTypeId", fetch = FetchType.LAZY)
    private List<XincoCoreData> xincoCoreDataList;

    public XincoCoreDataType() {
    }

    public XincoCoreDataType(Integer id) {
        this.id = id;
    }

    public XincoCoreDataType(Integer id, String designation, String description) {
        this.id = id;
        this.designation = designation;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<XincoCoreDataTypeAttribute> getXincoCoreDataTypeAttributeList() {
        return xincoCoreDataTypeAttributeList;
    }

    public void setXincoCoreDataTypeAttributeList(List<XincoCoreDataTypeAttribute> xincoCoreDataTypeAttributeList) {
        this.xincoCoreDataTypeAttributeList = xincoCoreDataTypeAttributeList;
    }

    public List<XincoCoreData> getXincoCoreDataList() {
        return xincoCoreDataList;
    }

    public void setXincoCoreDataList(List<XincoCoreData> xincoCoreDataList) {
        this.xincoCoreDataList = xincoCoreDataList;
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
        if (!(object instanceof XincoCoreDataType)) {
            return false;
        }
        XincoCoreDataType other = (XincoCoreDataType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreDataType[id=" + id + "]";
    }

}