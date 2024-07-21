/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.persistence;

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
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_state_transitions_to_xinco_state")
@NamedQueries({
    @NamedQuery(name = "XincoStateTransitionsToXincoState.findAll", query = "SELECT x FROM XincoStateTransitionsToXincoState x"),
    @NamedQuery(name = "XincoStateTransitionsToXincoState.findBySourceStateId", query = "SELECT x FROM XincoStateTransitionsToXincoState x WHERE x.xincoStateTransitionsToXincoStatePK.sourceStateId = :sourceStateId"),
    @NamedQuery(name = "XincoStateTransitionsToXincoState.findBySourceXincoStateXincoWorkflowId", query = "SELECT x FROM XincoStateTransitionsToXincoState x WHERE x.xincoStateTransitionsToXincoStatePK.sourceXincoStateXincoWorkflowId = :sourceXincoStateXincoWorkflowId"),
    @NamedQuery(name = "XincoStateTransitionsToXincoState.findBySourceXincoStateXincoWorkflowVersion", query = "SELECT x FROM XincoStateTransitionsToXincoState x WHERE x.xincoStateTransitionsToXincoStatePK.sourceXincoStateXincoWorkflowVersion = :sourceXincoStateXincoWorkflowVersion"),
    @NamedQuery(name = "XincoStateTransitionsToXincoState.findByDestinationStateId", query = "SELECT x FROM XincoStateTransitionsToXincoState x WHERE x.xincoStateTransitionsToXincoStatePK.destinationStateId = :destinationStateId"),
    @NamedQuery(name = "XincoStateTransitionsToXincoState.findByDestXincoStateXincoWorkflowId1", query = "SELECT x FROM XincoStateTransitionsToXincoState x WHERE x.xincoStateTransitionsToXincoStatePK.destXincoStateXincoWorkflowId1 = :destXincoStateXincoWorkflowId1"),
    @NamedQuery(name = "XincoStateTransitionsToXincoState.findByDestXincoStateXincoWorkflowVersion1", query = "SELECT x FROM XincoStateTransitionsToXincoState x WHERE x.xincoStateTransitionsToXincoStatePK.destXincoStateXincoWorkflowVersion1 = :destXincoStateXincoWorkflowVersion1")})
public class XincoStateTransitionsToXincoState implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoStateTransitionsToXincoStatePK xincoStateTransitionsToXincoStatePK;
    @JoinTable(name = "transition_has_xinco_core_user_restriction", joinColumns = {
        @JoinColumn(name = "source_state_id", referencedColumnName = "source_state_id", nullable = false),
        @JoinColumn(name = "source_xinco_workflow_id", referencedColumnName = "source_xinco_state_xinco_workflow_id", nullable = false),
        @JoinColumn(name = "source_xinco_workflow_version", referencedColumnName = "source_xinco_state_xinco_workflow_version", nullable = false),
        @JoinColumn(name = "destination_state_id", referencedColumnName = "destination_state_id", nullable = false),
        @JoinColumn(name = "dest_xinco_workflow_id", referencedColumnName = "dest_xinco_state_xinco_workflow_id1", nullable = false),
        @JoinColumn(name = "dest_xinco_workflow_version", referencedColumnName = "dest_xinco_state_xinco_workflow_version1", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "user_link_id", referencedColumnName = "id", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<UserLink> userLinkList;
    @JoinTable(name = "transition_has_xinco_action", joinColumns = {
        @JoinColumn(name = "source_state_id", referencedColumnName = "source_state_id", nullable = false),
        @JoinColumn(name = "source_xinco_workflow_id", referencedColumnName = "source_xinco_state_xinco_workflow_id", nullable = false),
        @JoinColumn(name = "source_xinco_workflow_version", referencedColumnName = "source_xinco_state_xinco_workflow_version", nullable = false),
        @JoinColumn(name = "destination_state_id", referencedColumnName = "destination_state_id", nullable = false),
        @JoinColumn(name = "dest_xinco_workflow_id", referencedColumnName = "dest_xinco_state_xinco_workflow_id1", nullable = false),
        @JoinColumn(name = "dest_workflow_version", referencedColumnName = "dest_xinco_state_xinco_workflow_version1", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "xinco_action_id", referencedColumnName = "id", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<XincoAction> xincoActionList;
    @JoinColumns({
        @JoinColumn(name = "source_state_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "source_xinco_state_xinco_workflow_id", referencedColumnName = "xinco_workflow_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "source_xinco_state_xinco_workflow_version", referencedColumnName = "xinco_workflow_version", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoWorkflowState sourceXincoWorkflowState;
    @JoinColumns({
        @JoinColumn(name = "destination_state_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "dest_xinco_state_xinco_workflow_id1", referencedColumnName = "xinco_workflow_id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "dest_xinco_state_xinco_workflow_version1", referencedColumnName = "xinco_workflow_version", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoWorkflowState destXincoWorkflowState;

    public XincoStateTransitionsToXincoState() {
    }

    public XincoStateTransitionsToXincoState(XincoStateTransitionsToXincoStatePK xincoStateTransitionsToXincoStatePK) {
        this.xincoStateTransitionsToXincoStatePK = xincoStateTransitionsToXincoStatePK;
    }

    public XincoStateTransitionsToXincoState(int sourceStateId, int sourceXincoStateXincoWorkflowId, int sourceXincoStateXincoWorkflowVersion, int destinationStateId, int destXincoStateXincoWorkflowId1, int destXincoStateXincoWorkflowVersion1) {
        this.xincoStateTransitionsToXincoStatePK = new XincoStateTransitionsToXincoStatePK(sourceStateId, sourceXincoStateXincoWorkflowId, sourceXincoStateXincoWorkflowVersion, destinationStateId, destXincoStateXincoWorkflowId1, destXincoStateXincoWorkflowVersion1);
    }

    public XincoStateTransitionsToXincoStatePK getXincoStateTransitionsToXincoStatePK() {
        return xincoStateTransitionsToXincoStatePK;
    }

    public void setXincoStateTransitionsToXincoStatePK(XincoStateTransitionsToXincoStatePK xincoStateTransitionsToXincoStatePK) {
        this.xincoStateTransitionsToXincoStatePK = xincoStateTransitionsToXincoStatePK;
    }

    public List<UserLink> getUserLinkList() {
        return userLinkList;
    }

    public void setUserLinkList(List<UserLink> userLinkList) {
        this.userLinkList = userLinkList;
    }

    public List<XincoAction> getXincoActionList() {
        return xincoActionList;
    }

    public void setXincoActionList(List<XincoAction> xincoActionList) {
        this.xincoActionList = xincoActionList;
    }

    public XincoWorkflowState getSourceXincoWorkflowState() {
        return sourceXincoWorkflowState;
    }

    public void setSourceXincoWorkflowState(XincoWorkflowState xincoWorkflowState) {
        this.sourceXincoWorkflowState = xincoWorkflowState;
    }

    public XincoWorkflowState getDestXincoWorkflowState() {
        return destXincoWorkflowState;
    }

    public void setDestXincoWorkflowState(XincoWorkflowState xincoWorkflowState1) {
        this.destXincoWorkflowState = xincoWorkflowState1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xincoStateTransitionsToXincoStatePK != null ? xincoStateTransitionsToXincoStatePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
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
        return "com.bluecubs.xinco.workflow.persistence.XincoStateTransitionsToXincoState[xincoStateTransitionsToXincoStatePK=" + xincoStateTransitionsToXincoStatePK + "]";
    }

}
