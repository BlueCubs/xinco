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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_data")
@EntityListeners(AuditedEntityListener.class)
@NamedQueries({
    @NamedQuery(name = "XincoCoreData.findAll", query = "SELECT x FROM XincoCoreData x"),
    @NamedQuery(name = "XincoCoreData.findById", query = "SELECT x FROM XincoCoreData x WHERE x.id = :id"),
    @NamedQuery(name = "XincoCoreData.findByDesignation", query = "SELECT x FROM XincoCoreData x WHERE x.designation = :designation"),
    @NamedQuery(name = "XincoCoreData.findByStatusNumber", query = "SELECT x FROM XincoCoreData x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreData extends XincoAuditedObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 255)
    private String designation;
    @Basic(optional = false)
    @Column(name = "status_number", nullable = false)
    private int statusNumber;
    @OneToMany(mappedBy = "xincoCoreDataId", fetch = FetchType.LAZY)
    private List<XincoCoreAce> xincoCoreAceList;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "xincoCoreDataId", fetch = FetchType.LAZY)
    private List<XincoCoreLog> xincoCoreLogList;
    @JoinColumn(name = "xinco_core_data_type_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoCoreDataType xincoCoreDataTypeId;
    @JoinColumn(name = "xinco_core_language_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoCoreLanguage xincoCoreLanguageId;
    @JoinColumn(name = "xinco_core_node_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoCoreNode xincoCoreNodeId;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "xincoCoreData", fetch = FetchType.LAZY)
    private List<XincoAddAttribute> xincoAddAttributeList;

    public XincoCoreData() {
    }

    public XincoCoreData(Integer id) {
        this.id = id;
    }

    public XincoCoreData(Integer id, String designation, int statusNumber) {
        this.id = id;
        this.designation = designation;
        this.statusNumber = statusNumber;
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

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    public List<XincoCoreAce> getXincoCoreAceList() {
        return xincoCoreAceList;
    }

    public void setXincoCoreAceList(List<XincoCoreAce> xincoCoreAceList) {
        this.xincoCoreAceList = xincoCoreAceList;
    }

    public List<XincoCoreLog> getXincoCoreLogList() {
        return xincoCoreLogList;
    }

    public void setXincoCoreLogList(List<XincoCoreLog> xincoCoreLogList) {
        this.xincoCoreLogList = xincoCoreLogList;
    }

    public XincoCoreDataType getXincoCoreDataTypeId() {
        return xincoCoreDataTypeId;
    }

    public void setXincoCoreDataTypeId(XincoCoreDataType xincoCoreDataTypeId) {
        this.xincoCoreDataTypeId = xincoCoreDataTypeId;
    }

    public XincoCoreLanguage getXincoCoreLanguageId() {
        return xincoCoreLanguageId;
    }

    public void setXincoCoreLanguageId(XincoCoreLanguage xincoCoreLanguageId) {
        this.xincoCoreLanguageId = xincoCoreLanguageId;
    }

    public XincoCoreNode getXincoCoreNodeId() {
        return xincoCoreNodeId;
    }

    public void setXincoCoreNodeId(XincoCoreNode xincoCoreNodeId) {
        this.xincoCoreNodeId = xincoCoreNodeId;
    }

    public List<XincoAddAttribute> getXincoAddAttributeList() {
        return xincoAddAttributeList;
    }

    public void setXincoAddAttributeList(List<XincoAddAttribute> xincoAddAttributeList) {
        this.xincoAddAttributeList = xincoAddAttributeList;
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
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreData[id=" + id + "]";
    }
}
