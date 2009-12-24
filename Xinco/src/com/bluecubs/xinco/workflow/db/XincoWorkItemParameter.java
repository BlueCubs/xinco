/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_work_item_parameter")
@NamedQueries({
    @NamedQuery(name = "XincoWorkItemParameter.findAll", query = "SELECT x FROM XincoWorkItemParameter x"),
    @NamedQuery(name = "XincoWorkItemParameter.findById", query = "SELECT x FROM XincoWorkItemParameter x WHERE x.xincoWorkItemParameterPK.id = :id"),
    @NamedQuery(name = "XincoWorkItemParameter.findByXincoWorkItemId", query = "SELECT x FROM XincoWorkItemParameter x WHERE x.xincoWorkItemParameterPK.xincoWorkItemId = :xincoWorkItemId"),
    @NamedQuery(name = "XincoWorkItemParameter.findByXincoStateId", query = "SELECT x FROM XincoWorkItemParameter x WHERE x.xincoWorkItemParameterPK.xincoStateId = :xincoStateId"),
    @NamedQuery(name = "XincoWorkItemParameter.findByXincoWorkflowId", query = "SELECT x FROM XincoWorkItemParameter x WHERE x.xincoWorkItemParameterPK.xincoWorkflowId = :xincoWorkflowId"),
    @NamedQuery(name = "XincoWorkItemParameter.findByXincoWorkflowVersion", query = "SELECT x FROM XincoWorkItemParameter x WHERE x.xincoWorkItemParameterPK.xincoWorkflowVersion = :xincoWorkflowVersion"),
    @NamedQuery(name = "XincoWorkItemParameter.findByName", query = "SELECT x FROM XincoWorkItemParameter x WHERE x.name = :name"),
    @NamedQuery(name = "XincoWorkItemParameter.findByValueType", query = "SELECT x FROM XincoWorkItemParameter x WHERE x.valueType = :valueType")})
public class XincoWorkItemParameter implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoWorkItemParameterPK xincoWorkItemParameterPK;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Basic(optional = false)
    @Column(name = "value_type", nullable = false, length = 45)
    private String valueType;
    @Basic(optional = false)
    @Lob
    @Column(name = "value", nullable = false)
    private byte[] value;
    @JoinColumns({
        @JoinColumn(name = "xinco_work_item_id", referencedColumnName = "xinco_work_item_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "xinco_state_id", referencedColumnName = "xinco_state_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "xinco_workflow_id", referencedColumnName = "xinco_state_xinco_workflow_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "xinco_workflow_version", referencedColumnName = "xinco_state_xinco_workflow_version", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoWorkItemHasXincoState xincoWorkItemHasXincoState;

    public XincoWorkItemParameter() {
    }

    public XincoWorkItemParameter(XincoWorkItemParameterPK xincoWorkItemParameterPK) {
        this.xincoWorkItemParameterPK = xincoWorkItemParameterPK;
    }

    public XincoWorkItemParameter(XincoWorkItemParameterPK xincoWorkItemParameterPK, String name, String valueType, byte[] value) {
        this.xincoWorkItemParameterPK = xincoWorkItemParameterPK;
        this.name = name;
        this.valueType = valueType;
        this.value = value;
    }

    public XincoWorkItemParameter(int id, int xincoWorkItemId, int xincoStateId, int xincoWorkflowId, int xincoWorkflowVersion) {
        this.xincoWorkItemParameterPK = new XincoWorkItemParameterPK(id, xincoWorkItemId, xincoStateId, xincoWorkflowId, xincoWorkflowVersion);
    }

    public XincoWorkItemParameterPK getXincoWorkItemParameterPK() {
        return xincoWorkItemParameterPK;
    }

    public void setXincoWorkItemParameterPK(XincoWorkItemParameterPK xincoWorkItemParameterPK) {
        this.xincoWorkItemParameterPK = xincoWorkItemParameterPK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public XincoWorkItemHasXincoState getXincoWorkItemHasXincoState() {
        return xincoWorkItemHasXincoState;
    }

    public void setXincoWorkItemHasXincoState(XincoWorkItemHasXincoState xincoWorkItemHasXincoState) {
        this.xincoWorkItemHasXincoState = xincoWorkItemHasXincoState;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xincoWorkItemParameterPK != null ? xincoWorkItemParameterPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoWorkItemParameter)) {
            return false;
        }
        XincoWorkItemParameter other = (XincoWorkItemParameter) object;
        if ((this.xincoWorkItemParameterPK == null && other.xincoWorkItemParameterPK != null) || (this.xincoWorkItemParameterPK != null && !this.xincoWorkItemParameterPK.equals(other.xincoWorkItemParameterPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.db.XincoWorkItemParameter[xincoWorkItemParameterPK=" + xincoWorkItemParameterPK + "]";
    }

}
