/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.persistence;

import com.dreamer.Hibernate.audit.XincoAbstractAuditableObject;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Class: XincoCoreNode
 * Created: Mar 24, 2008 2:23:43 PM
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_core_node")
@NamedQueries({@NamedQuery(name = "XincoCoreNode.findById", query = "SELECT x FROM XincoCoreNode x WHERE x.id = :id"), @NamedQuery(name = "XincoCoreNode.findByXincoCoreNodeId", query = "SELECT x FROM XincoCoreNode x WHERE x.xincoCoreNodeId = :xincoCoreNodeId"), @NamedQuery(name = "XincoCoreNode.findByXincoCoreLanguageId", query = "SELECT x FROM XincoCoreNode x WHERE x.xincoCoreLanguageId = :xincoCoreLanguageId"), @NamedQuery(name = "XincoCoreNode.findByDesignation", query = "SELECT x FROM XincoCoreNode x WHERE x.designation = :designation"), @NamedQuery(name = "XincoCoreNode.findByStatusNumber", query = "SELECT x FROM XincoCoreNode x WHERE x.statusNumber = :statusNumber")})
public class XincoCoreNode extends XincoAbstractAuditableObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "xinco_core_node_id")
    private Integer xincoCoreNodeId;
    @Column(name = "xinco_core_language_id", nullable = false)
    private int xincoCoreLanguageId;
    @Column(name = "designation", nullable = false)
    private String designation;
    @Column(name = "status_number", nullable = false)
    private int statusNumber;

    public XincoCoreNode() {
    }

    public XincoCoreNode(Integer id) {
        this.id = id;
    }

    public XincoCoreNode(Integer id, int xincoCoreLanguageId, String designation, int statusNumber) {
        this.id = id;
        this.xincoCoreLanguageId = xincoCoreLanguageId;
        this.designation = designation;
        this.statusNumber = statusNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getXincoCoreNodeId() {
        return xincoCoreNodeId;
    }

    public void setXincoCoreNodeId(Integer xincoCoreNodeId) {
        this.xincoCoreNodeId = xincoCoreNodeId;
    }

    public int getXincoCoreLanguageId() {
        return xincoCoreLanguageId;
    }

    public void setXincoCoreLanguageId(int xincoCoreLanguageId) {
        this.xincoCoreLanguageId = xincoCoreLanguageId;
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
        return "com.bluecubs.xinco.persistence.XincoCoreNode[id=" + id + "]";
    }

}
