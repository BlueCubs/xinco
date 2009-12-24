/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.db;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_state_transitions_to_xinco_state")
@NamedQueries({
    @NamedQuery(name = "XincoStateTransitionsToXincoState.findAll", query = "SELECT x FROM XincoStateTransitionsToXincoState x"),
    @NamedQuery(name = "XincoStateTransitionsToXincoState.findBySourceStateId", query = "SELECT x FROM XincoStateTransitionsToXincoState x WHERE x.xincoStateTransitionsToXincoStatePK.sourceStateId = :sourceStateId"),
    @NamedQuery(name = "XincoStateTransitionsToXincoState.findByXincoStateXincoWorkflowId", query = "SELECT x FROM XincoStateTransitionsToXincoState x WHERE x.xincoStateTransitionsToXincoStatePK.xincoStateXincoWorkflowId = :xincoStateXincoWorkflowId"),
    @NamedQuery(name = "XincoStateTransitionsToXincoState.findByXincoStateXincoWorkflowVersion", query = "SELECT x FROM XincoStateTransitionsToXincoState x WHERE x.xincoStateTransitionsToXincoStatePK.xincoStateXincoWorkflowVersion = :xincoStateXincoWorkflowVersion"),
    @NamedQuery(name = "XincoStateTransitionsToXincoState.findByDestinationStateId", query = "SELECT x FROM XincoStateTransitionsToXincoState x WHERE x.xincoStateTransitionsToXincoStatePK.destinationStateId = :destinationStateId"),
    @NamedQuery(name = "XincoStateTransitionsToXincoState.findByXincoStateXincoWorkflowId1", query = "SELECT x FROM XincoStateTransitionsToXincoState x WHERE x.xincoStateTransitionsToXincoStatePK.xincoStateXincoWorkflowId1 = :xincoStateXincoWorkflowId1"),
    @NamedQuery(name = "XincoStateTransitionsToXincoState.findByXincoStateXincoWorkflowVersion1", query = "SELECT x FROM XincoStateTransitionsToXincoState x WHERE x.xincoStateTransitionsToXincoStatePK.xincoStateXincoWorkflowVersion1 = :xincoStateXincoWorkflowVersion1")})
public class XincoStateTransitionsToXincoState implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoStateTransitionsToXincoStatePK xincoStateTransitionsToXincoStatePK;
    @JoinTable(name = "transition_has_xinco_action", joinColumns = {
        @JoinColumn(name = "source_state_id", referencedColumnName = "source_state_id", nullable = false),
        @JoinColumn(name = "source_xinco_workflow_id", referencedColumnName = "xinco_state_xinco_workflow_id", nullable = false),
        @JoinColumn(name = "source_xinco_workflow_version", referencedColumnName = "xinco_state_xinco_workflow_version", nullable = false),
        @JoinColumn(name = "destination_state_id", referencedColumnName = "destination_state_id", nullable = false),
        @JoinColumn(name = "dest_xinco_workflow_id", referencedColumnName = "xinco_state_xinco_workflow_id1", nullable = false),
        @JoinColumn(name = "dest_workflow_version", referencedColumnName = "xinco_state_xinco_workflow_version1", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "xinco_action_id", referencedColumnName = "id", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<XincoAction> xincoActionList;
    @JoinColumns({
        @JoinColumn(name = "source_state_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "xinco_state_xinco_workflow_id", referencedColumnName = "xinco_workflow_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "xinco_state_xinco_workflow_version", referencedColumnName = "xinco_workflow_version", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoWorkflowState xincoWorkflowState;
    @JoinColumns({
        @JoinColumn(name = "destination_state_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "xinco_state_xinco_workflow_id1", referencedColumnName = "xinco_workflow_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "xinco_state_xinco_workflow_version1", referencedColumnName = "xinco_workflow_version", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoWorkflowState xincoWorkflowState1;

    public XincoStateTransitionsToXincoState() {
    }

    public XincoStateTransitionsToXincoState(XincoStateTransitionsToXincoStatePK xincoStateTransitionsToXincoStatePK) {
        this.xincoStateTransitionsToXincoStatePK = xincoStateTransitionsToXincoStatePK;
    }

    public XincoStateTransitionsToXincoState(int sourceStateId, int xincoStateXincoWorkflowId, int xincoStateXincoWorkflowVersion, int destinationStateId, int xincoStateXincoWorkflowId1, int xincoStateXincoWorkflowVersion1) {
        this.xincoStateTransitionsToXincoStatePK = new XincoStateTransitionsToXincoStatePK(sourceStateId, xincoStateXincoWorkflowId, xincoStateXincoWorkflowVersion, destinationStateId, xincoStateXincoWorkflowId1, xincoStateXincoWorkflowVersion1);
    }

    public XincoStateTransitionsToXincoStatePK getXincoStateTransitionsToXincoStatePK() {
        return xincoStateTransitionsToXincoStatePK;
    }

    public void setXincoStateTransitionsToXincoStatePK(XincoStateTransitionsToXincoStatePK xincoStateTransitionsToXincoStatePK) {
        this.xincoStateTransitionsToXincoStatePK = xincoStateTransitionsToXincoStatePK;
    }

    public List<XincoAction> getXincoActionList() {
        return xincoActionList;
    }

    public void setXincoActionList(List<XincoAction> xincoActionList) {
        this.xincoActionList = xincoActionList;
    }

    public XincoWorkflowState getXincoWorkflowState() {
        return xincoWorkflowState;
    }

    public void setXincoWorkflowState(XincoWorkflowState xincoWorkflowState) {
        this.xincoWorkflowState = xincoWorkflowState;
    }

    public XincoWorkflowState getXincoWorkflowState1() {
        return xincoWorkflowState1;
    }

    public void setXincoWorkflowState1(XincoWorkflowState xincoWorkflowState1) {
        this.xincoWorkflowState1 = xincoWorkflowState1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xincoStateTransitionsToXincoStatePK != null ? xincoStateTransitionsToXincoStatePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoStateTransitionsToXincoState)) {
            return false;
        }
        XincoStateTransitionsToXincoState other = (XincoStateTransitionsToXincoState) object;
        if ((this.xincoStateTransitionsToXincoStatePK == null && other.xincoStateTransitionsToXincoStatePK != null) || (this.xincoStateTransitionsToXincoStatePK != null && !this.xincoStateTransitionsToXincoStatePK.equals(other.xincoStateTransitionsToXincoStatePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.db.XincoStateTransitionsToXincoState[xincoStateTransitionsToXincoStatePK=" + xincoStateTransitionsToXincoStatePK + "]";
    }

}
