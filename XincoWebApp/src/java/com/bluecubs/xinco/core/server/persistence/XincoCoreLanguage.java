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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@EntityListeners(AuditedEntityListener.class)
@Table(name = "xinco_core_language")
@NamedQueries({@NamedQuery(name = "XincoCoreLanguage.findAll", query = "SELECT x FROM XincoCoreLanguage x"), @NamedQuery(name = "XincoCoreLanguage.findById", query = "SELECT x FROM XincoCoreLanguage x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreLanguage.findBySign", query = "SELECT x FROM XincoCoreLanguage x WHERE x.sign = :sign"), @NamedQuery(name = "XincoCoreLanguage.findByDesignation", query = "SELECT x FROM XincoCoreLanguage x WHERE x.designation = :designation")})
public class XincoCoreLanguage extends XincoAuditedObject implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreLanguageId")
    private Collection<XincoCoreNode> xincoCoreNodeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreLanguageId")
    private Collection<XincoCoreData> xincoCoreDataCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "sign", nullable = false, length = 255)
    private String sign;
    @Basic(optional = false)
    @Column(name = "designation", nullable = false, length = 255)
    private String designation;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreLanguageId", fetch = FetchType.LAZY)
    private List<XincoCoreNode> xincoCoreNodeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoCoreLanguageId", fetch = FetchType.LAZY)
    private List<XincoCoreData> xincoCoreDataList;

    public XincoCoreLanguage() {
    }

    public XincoCoreLanguage(Integer id) {
        this.id = id;
    }

    public XincoCoreLanguage(Integer id, String sign, String designation) {
        this.id = id;
        this.sign = sign;
        this.designation = designation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public List<XincoCoreNode> getXincoCoreNodeList() {
        return xincoCoreNodeList;
    }

    public void setXincoCoreNodeList(List<XincoCoreNode> xincoCoreNodeList) {
        this.xincoCoreNodeList = xincoCoreNodeList;
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
        if (!(object instanceof XincoCoreLanguage)) {
            return false;
        }
        XincoCoreLanguage other = (XincoCoreLanguage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoCoreLanguage[id=" + id + "]";
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
