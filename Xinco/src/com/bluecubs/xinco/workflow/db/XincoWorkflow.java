/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.workflow.db;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_workflow", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"})})
@NamedQueries({
    @NamedQuery(name = "XincoWorkflow.findAll", query = "SELECT x FROM XincoWorkflow x"),
    @NamedQuery(name = "XincoWorkflow.findById", query = "SELECT x FROM XincoWorkflow x WHERE x.xincoWorkflowPK.id = :id"),
    @NamedQuery(name = "XincoWorkflow.findByVersion", query = "SELECT x FROM XincoWorkflow x WHERE x.xincoWorkflowPK.version = :version"),
    @NamedQuery(name = "XincoWorkflow.findByName", query = "SELECT x FROM XincoWorkflow x WHERE x.name = :name")})
public class XincoWorkflow implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoWorkflowPK xincoWorkflowPK;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Lob
    @Column(name = "description", length = 65535)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoWorkflow", fetch = FetchType.LAZY)
    private List<XincoWorkflowState> xincoWorkflowStateList;

    public XincoWorkflow() {
    }

    public XincoWorkflow(XincoWorkflowPK xincoWorkflowPK) {
        this.xincoWorkflowPK = xincoWorkflowPK;
    }

    public XincoWorkflow(XincoWorkflowPK xincoWorkflowPK, String name) {
        this.xincoWorkflowPK = xincoWorkflowPK;
        this.name = name;
    }

    public XincoWorkflow(int id, int version) {
        this.xincoWorkflowPK = new XincoWorkflowPK(id, version);
    }

    public XincoWorkflowPK getXincoWorkflowPK() {
        return xincoWorkflowPK;
    }

    public void setXincoWorkflowPK(XincoWorkflowPK xincoWorkflowPK) {
        this.xincoWorkflowPK = xincoWorkflowPK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<XincoWorkflowState> getXincoWorkflowStateList() {
        return xincoWorkflowStateList;
    }

    public void setXincoWorkflowStateList(List<XincoWorkflowState> xincoWorkflowStateList) {
        this.xincoWorkflowStateList = xincoWorkflowStateList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xincoWorkflowPK != null ? xincoWorkflowPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoWorkflow)) {
            return false;
        }
        XincoWorkflow other = (XincoWorkflow) object;
        if ((this.xincoWorkflowPK == null && other.xincoWorkflowPK != null) || (this.xincoWorkflowPK != null && !this.xincoWorkflowPK.equals(other.xincoWorkflowPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.db.XincoWorkflow[xincoWorkflowPK=" + xincoWorkflowPK + "]";
    }
}
