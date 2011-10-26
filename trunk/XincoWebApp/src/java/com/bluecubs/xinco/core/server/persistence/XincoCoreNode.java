/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.server.AuditedEntityListener;
import com.bluecubs.xinco.core.server.XincoAuditedObject;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_node")
@EntityListeners(AuditedEntityListener.class)
@NamedQueries({@NamedQuery(name = "XincoCoreNode.findAll", query = "SELECT x FROM XincoCoreNode x"), @NamedQuery(name = "XincoCoreNode.findById", query = "SELECT x FROM XincoCoreNode x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreNode.findByDesignation", query = "SELECT x FROM XincoCoreNode x WHERE x.designation = :designation"), @NamedQuery(name = "XincoCoreNode.findByStatusNumber", query = "SELECT x FROM XincoCoreNode x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreNode extends XincoAuditedObject implements Serializable {
    @OneToMany(mappedBy = "xincoCoreNode")
    private Collection<XincoCoreNode> xincoCoreNodeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreNode")
    private Collection<XincoCoreData> xincoCoreDataCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "XincoCoreNodeGen")
    @TableGenerator(name = "XincoCoreNodeGen", table = "xinco_id",
    pkColumnName = "tablename",
    valueColumnName = "last_id",
    pkColumnValue = "xinco_core_node",
    allocationSize = 1,
    initialValue=1000)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 255)
    private String designation;
    @Basic(optional = false)
    @Column(name = "status_number", nullable = false)
    private int statusNumber;
    @JoinColumn(name = "xinco_core_language_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoCoreLanguage xincoCoreLanguage;
    @OneToMany(mappedBy = "xincoCoreNode", fetch = FetchType.LAZY)
    private List<XincoCoreNode> xincoCoreNodeList;
    @JoinColumn(name = "xinco_core_node_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private XincoCoreNode xincoCoreNode;
    @OneToMany(mappedBy = "xincoCoreNode", fetch = FetchType.LAZY)
    private List<XincoCoreAce> xincoCoreAceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreNode", fetch = FetchType.LAZY)
    private List<XincoCoreData> xincoCoreDataList;

    public XincoCoreNode() {
    }

    public XincoCoreNode(Integer id) {
        this.id = id;
    }

    public XincoCoreNode(Integer id, String designation, int statusNumber) {
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

    public XincoCoreLanguage getXincoCoreLanguage() {
        return xincoCoreLanguage;
    }

    public void setXincoCoreLanguage(XincoCoreLanguage xincoCoreLanguageId) {
        this.xincoCoreLanguage = xincoCoreLanguageId;
    }

    public List<XincoCoreNode> getXincoCoreNodeList() {
        return xincoCoreNodeList;
    }

    public void setXincoCoreNodeList(List<XincoCoreNode> xincoCoreNodeList) {
        this.xincoCoreNodeList = xincoCoreNodeList;
    }

    public XincoCoreNode getXincoCoreNode() {
        return xincoCoreNode;
    }

    public void setXincoCoreNode(XincoCoreNode xincoCoreNodeId) {
        this.xincoCoreNode = xincoCoreNodeId;
    }

    public List<XincoCoreAce> getXincoCoreAceList() {
        return xincoCoreAceList;
    }

    public void setXincoCoreAceList(List<XincoCoreAce> xincoCoreAceList) {
        this.xincoCoreAceList = xincoCoreAceList;
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
        
        if (!(object instanceof XincoCoreNode)) {
            return false;
        }
        XincoCoreNode other = (XincoCoreNode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreNode[id=" + id + "]";
    }

    @XmlTransient
    public Collection<XincoCoreNode> getXincoCoreNodeCollection() {
        return xincoCoreNodeCollection;
    }

    public void setXincoCoreNodeCollection(Collection<XincoCoreNode> xincoCoreNodeCollection) {
        this.xincoCoreNodeCollection = xincoCoreNodeCollection;
    }

    @XmlTransient
    public Collection<XincoCoreData> getXincoCoreDataCollection() {
        return xincoCoreDataCollection;
    }

    public void setXincoCoreDataCollection(Collection<XincoCoreData> xincoCoreDataCollection) {
        this.xincoCoreDataCollection = xincoCoreDataCollection;
    }

}
