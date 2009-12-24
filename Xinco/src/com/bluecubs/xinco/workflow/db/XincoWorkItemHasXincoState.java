/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.db;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_work_item_has_xinco_state")
@NamedQueries({
    @NamedQuery(name = "XincoWorkItemHasXincoState.findAll", query = "SELECT x FROM XincoWorkItemHasXincoState x"),
    @NamedQuery(name = "XincoWorkItemHasXincoState.findByXincoWorkItemId", query = "SELECT x FROM XincoWorkItemHasXincoState x WHERE x.xincoWorkItemHasXincoStatePK.xincoWorkItemId = :xincoWorkItemId"),
    @NamedQuery(name = "XincoWorkItemHasXincoState.findByXincoStateId", query = "SELECT x FROM XincoWorkItemHasXincoState x WHERE x.xincoWorkItemHasXincoStatePK.xincoStateId = :xincoStateId"),
    @NamedQuery(name = "XincoWorkItemHasXincoState.findByXincoStateXincoWorkflowId", query = "SELECT x FROM XincoWorkItemHasXincoState x WHERE x.xincoWorkItemHasXincoStatePK.xincoStateXincoWorkflowId = :xincoStateXincoWorkflowId"),
    @NamedQuery(name = "XincoWorkItemHasXincoState.findByXincoStateXincoWorkflowVersion", query = "SELECT x FROM XincoWorkItemHasXincoState x WHERE x.xincoWorkItemHasXincoStatePK.xincoStateXincoWorkflowVersion = :xincoStateXincoWorkflowVersion"),
    @NamedQuery(name = "XincoWorkItemHasXincoState.findBySequence", query = "SELECT x FROM XincoWorkItemHasXincoState x WHERE x.sequence = :sequence"),
    @NamedQuery(name = "XincoWorkItemHasXincoState.findByStateReachedDate", query = "SELECT x FROM XincoWorkItemHasXincoState x WHERE x.stateReachedDate = :stateReachedDate")})
public class XincoWorkItemHasXincoState implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoWorkItemHasXincoStatePK xincoWorkItemHasXincoStatePK;
    @Basic(optional = false)
    @Column(name = "sequence", nullable = false)
    private int sequence;
    @Basic(optional = false)
    @Column(name = "state_reached_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date stateReachedDate;
    @JoinColumn(name = "xinco_work_item_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoWorkflowItem xincoWorkflowItem;
    @JoinColumns({
        @JoinColumn(name = "xinco_state_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "xinco_state_xinco_workflow_id", referencedColumnName = "xinco_workflow_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "xinco_state_xinco_workflow_version", referencedColumnName = "xinco_workflow_version", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoWorkflowState xincoWorkflowState;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoWorkItemHasXincoState", fetch = FetchType.LAZY)
    private List<XincoWorkItemParameter> xincoWorkItemParameterList;

    public XincoWorkItemHasXincoState() {
    }

    public XincoWorkItemHasXincoState(XincoWorkItemHasXincoStatePK xincoWorkItemHasXincoStatePK) {
        this.xincoWorkItemHasXincoStatePK = xincoWorkItemHasXincoStatePK;
    }

    public XincoWorkItemHasXincoState(XincoWorkItemHasXincoStatePK xincoWorkItemHasXincoStatePK, int sequence, Date stateReachedDate) {
        this.xincoWorkItemHasXincoStatePK = xincoWorkItemHasXincoStatePK;
        this.sequence = sequence;
        this.stateReachedDate = stateReachedDate;
    }

    public XincoWorkItemHasXincoState(int xincoWorkItemId, int xincoStateId, int xincoStateXincoWorkflowId, int xincoStateXincoWorkflowVersion) {
        this.xincoWorkItemHasXincoStatePK = new XincoWorkItemHasXincoStatePK(xincoWorkItemId, xincoStateId, xincoStateXincoWorkflowId, xincoStateXincoWorkflowVersion);
    }

    public XincoWorkItemHasXincoStatePK getXincoWorkItemHasXincoStatePK() {
        return xincoWorkItemHasXincoStatePK;
    }

    public void setXincoWorkItemHasXincoStatePK(XincoWorkItemHasXincoStatePK xincoWorkItemHasXincoStatePK) {
        this.xincoWorkItemHasXincoStatePK = xincoWorkItemHasXincoStatePK;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Date getStateReachedDate() {
        return stateReachedDate;
    }

    public void setStateReachedDate(Date stateReachedDate) {
        this.stateReachedDate = stateReachedDate;
    }

    public XincoWorkflowItem getXincoWorkflowItem() {
        return xincoWorkflowItem;
    }

    public void setXincoWorkflowItem(XincoWorkflowItem xincoWorkflowItem) {
        this.xincoWorkflowItem = xincoWorkflowItem;
    }

    public XincoWorkflowState getXincoWorkflowState() {
        return xincoWorkflowState;
    }

    public void setXincoWorkflowState(XincoWorkflowState xincoWorkflowState) {
        this.xincoWorkflowState = xincoWorkflowState;
    }

    public List<XincoWorkItemParameter> getXincoWorkItemParameterList() {
        return xincoWorkItemParameterList;
    }

    public void setXincoWorkItemParameterList(List<XincoWorkItemParameter> xincoWorkItemParameterList) {
        this.xincoWorkItemParameterList = xincoWorkItemParameterList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xincoWorkItemHasXincoStatePK != null ? xincoWorkItemHasXincoStatePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoWorkItemHasXincoState)) {
            return false;
        }
        XincoWorkItemHasXincoState other = (XincoWorkItemHasXincoState) object;
        if ((this.xincoWorkItemHasXincoStatePK == null && other.xincoWorkItemHasXincoStatePK != null) || (this.xincoWorkItemHasXincoStatePK != null && !this.xincoWorkItemHasXincoStatePK.equals(other.xincoWorkItemHasXincoStatePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.db.XincoWorkItemHasXincoState[xincoWorkItemHasXincoStatePK=" + xincoWorkItemHasXincoStatePK + "]";
    }

}
