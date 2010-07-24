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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

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
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "DATAKEYGEN")
    @TableGenerator(name = "DATAKEYGEN", table = "xinco_id",
    pkColumnName = "tablename", valueColumnName = "last_id",
    pkColumnValue = "xinco_core_data", initialValue = 1001, allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 255)
    private String designation;
    @Basic(optional = false)
    @Column(name = "status_number", nullable = false)
    private int statusNumber;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreData")
    private List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreData1")
    private List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyList1;
    @JoinColumn(name = "xinco_core_data_type_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private XincoCoreDataType xincoCoreDataType;
    @JoinColumn(name = "xinco_core_language_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private XincoCoreLanguage xincoCoreLanguage;
    @JoinColumn(name = "xinco_core_node_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private XincoCoreNode xincoCoreNode;

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

    public List<XincoCoreDataHasDependency> getXincoCoreDataHasDependencyList() {
        return xincoCoreDataHasDependencyList;
    }

    public void setXincoCoreDataHasDependencyList(List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyList) {
        this.xincoCoreDataHasDependencyList = xincoCoreDataHasDependencyList;
    }

    public List<XincoCoreDataHasDependency> getXincoCoreDataHasDependencyList1() {
        return xincoCoreDataHasDependencyList1;
    }

    public void setXincoCoreDataHasDependencyList1(List<XincoCoreDataHasDependency> xincoCoreDataHasDependencyList1) {
        this.xincoCoreDataHasDependencyList1 = xincoCoreDataHasDependencyList1;
    }

    public XincoCoreDataType getXincoCoreDataType() {
        return xincoCoreDataType;
    }

    public void setXincoCoreDataType(XincoCoreDataType xincoCoreDataType) {
        this.xincoCoreDataType = xincoCoreDataType;
    }

    public XincoCoreLanguage getXincoCoreLanguage() {
        return xincoCoreLanguage;
    }

    public void setXincoCoreLanguage(XincoCoreLanguage xincoCoreLanguage) {
        this.xincoCoreLanguage = xincoCoreLanguage;
    }

    public XincoCoreNode getXincoCoreNode() {
        return xincoCoreNode;
    }

    public void setXincoCoreNode(XincoCoreNode xincoCoreNode) {
        this.xincoCoreNode = xincoCoreNode;
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
