package com.bluecubs.xinco.workflow.persistence;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_workflow_state")
@NamedQueries({
    @NamedQuery(name = "XincoWorkflowState.findAll", query = "SELECT x FROM XincoWorkflowState x"),
    @NamedQuery(name = "XincoWorkflowState.findById", query = "SELECT x FROM XincoWorkflowState x WHERE x.xincoWorkflowStatePK.id = :id"),
    @NamedQuery(name = "XincoWorkflowState.findByXincoWorkflowId", query = "SELECT x FROM XincoWorkflowState x WHERE x.xincoWorkflowStatePK.xincoWorkflowId = :xincoWorkflowId"),
    @NamedQuery(name = "XincoWorkflowState.findByXincoWorkflowVersion", query = "SELECT x FROM XincoWorkflowState x WHERE x.xincoWorkflowStatePK.xincoWorkflowVersion = :xincoWorkflowVersion"),
    @NamedQuery(name = "XincoWorkflowState.findByName", query = "SELECT x FROM XincoWorkflowState x WHERE x.name = :name")})
public class XincoWorkflowState implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoWorkflowStatePK xincoWorkflowStatePK;
    @Column(name = "name", length = 45)
    private String name;
    @JoinTable(name = "xinco_state_has_xinco_action", joinColumns = {
        @JoinColumn(name = "xinco_state_id", referencedColumnName = "id", nullable = false),
        @JoinColumn(name = "xinco_state_xinco_workflow_id", referencedColumnName = "xinco_workflow_id", nullable = false),
        @JoinColumn(name = "xinco_state_xinco_workflow_version", referencedColumnName = "xinco_workflow_version", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "xinco_action_id", referencedColumnName = "id", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<XincoAction> xincoActionList;
    @JoinTable(name = "xinco_state_has_actors", joinColumns = {
        @JoinColumn(name = "xinco_state_id", referencedColumnName = "id", nullable = false),
        @JoinColumn(name = "xinco_state_xinco_workflow_id", referencedColumnName = "xinco_workflow_id", nullable = false),
        @JoinColumn(name = "xinco_state_xinco_workflow_version", referencedColumnName = "xinco_workflow_version", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "user_link_id", referencedColumnName = "id", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<UserLink> userLinkList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xincoWorkflowState", fetch = FetchType.LAZY)
    private List<XincoWorkItemHasXincoState> xincoWorkItemHasXincoStateList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sourceXincoWorkflowState", fetch = FetchType.LAZY)
    private List<XincoStateTransitionsToXincoState> sourceXincoStateTransitionsToXincoStateList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "destXincoWorkflowState", fetch = FetchType.LAZY)
    private List<XincoStateTransitionsToXincoState> destXincoStateTransitionsToXincoStateList;
    @JoinColumns({
        @JoinColumn(name = "xinco_workflow_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "xinco_workflow_id", referencedColumnName = "xincoWorkflowId", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "xinco_workflow_version", referencedColumnName = "version", nullable = false, insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoWorkflow xincoWorkflow;
    @JoinColumn(name = "xinco_state_type_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoStateType xincoStateTypeId;

    public XincoWorkflowState() {
    }

    public XincoWorkflowState(XincoWorkflowStatePK xincoWorkflowStatePK) {
        this.xincoWorkflowStatePK = xincoWorkflowStatePK;
    }

    public XincoWorkflowState(int xincoWorkflowId, int xincoWorkflowVersion, String name) {
        this.xincoWorkflowStatePK = new XincoWorkflowStatePK(xincoWorkflowId, xincoWorkflowVersion);
        setName(name);
    }

    public XincoWorkflowStatePK getXincoWorkflowStatePK() {
        return xincoWorkflowStatePK;
    }

    public void setXincoWorkflowStatePK(XincoWorkflowStatePK xincoWorkflowStatePK) {
        this.xincoWorkflowStatePK = xincoWorkflowStatePK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<XincoAction> getXincoActionList() {
        return xincoActionList;
    }

    public void setXincoActionList(List<XincoAction> xincoActionList) {
        this.xincoActionList = xincoActionList;
    }

    public List<UserLink> getUserLinkList() {
        return userLinkList;
    }

    public void setUserLinkList(List<UserLink> userLinkList) {
        this.userLinkList = userLinkList;
    }

    public List<XincoWorkItemHasXincoState> getXincoWorkItemHasXincoStateList() {
        return xincoWorkItemHasXincoStateList;
    }

    public void setXincoWorkItemHasXincoStateList(List<XincoWorkItemHasXincoState> xincoWorkItemHasXincoStateList) {
        this.xincoWorkItemHasXincoStateList = xincoWorkItemHasXincoStateList;
    }

    public List<XincoStateTransitionsToXincoState> getSourceXincoStateTransitionsToXincoStateList() {
        return sourceXincoStateTransitionsToXincoStateList;
    }

    public void setSourceXincoStateTransitionsToXincoStateList(List<XincoStateTransitionsToXincoState> xincoStateTransitionsToXincoStateList) {
        this.sourceXincoStateTransitionsToXincoStateList = xincoStateTransitionsToXincoStateList;
    }

    public List<XincoStateTransitionsToXincoState> getDestXincoStateTransitionsToXincoStateList() {
        return destXincoStateTransitionsToXincoStateList;
    }

    public void setDestXincoStateTransitionsToXincoStateList(List<XincoStateTransitionsToXincoState> xincoStateTransitionsToXincoStateList1) {
        this.destXincoStateTransitionsToXincoStateList = xincoStateTransitionsToXincoStateList1;
    }

    public XincoWorkflow getXincoWorkflow() {
        return xincoWorkflow;
    }

    public void setXincoWorkflow(XincoWorkflow xincoWorkflow) {
        this.xincoWorkflow = xincoWorkflow;
    }

    public XincoStateType getXincoStateTypeId() {
        return xincoStateTypeId;
    }

    public void setXincoStateTypeId(XincoStateType xincoStateTypeId) {
        this.xincoStateTypeId = xincoStateTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xincoWorkflowStatePK != null ? xincoWorkflowStatePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof XincoWorkflowState)) {
            return false;
        }
        XincoWorkflowState other = (XincoWorkflowState) object;
        if ((this.xincoWorkflowStatePK == null && other.xincoWorkflowStatePK != null) || (this.xincoWorkflowStatePK != null && !this.xincoWorkflowStatePK.equals(other.xincoWorkflowStatePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.persistence.XincoWorkflowState[xincoWorkflowStatePK=" + xincoWorkflowStatePK + "]";
    }
}
