package com.bluecubs.xinco.workflow.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Embeddable
public class XincoStateTransitionsToXincoStatePK implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "source_state_id", nullable = false)
    private int sourceStateId;
    @Basic(optional = false)
    @Column(name = "source_xinco_state_xinco_workflow_id", nullable = false)
    private int sourceXincoStateXincoWorkflowId;
    @Basic(optional = false)
    @Column(name = "source_xinco_state_xinco_workflow_version", nullable = false)
    private int sourceXincoStateXincoWorkflowVersion;
    @Basic(optional = false)
    @Column(name = "destination_state_id", nullable = false)
    private int destinationStateId;
    @Basic(optional = false)
    @Column(name = "dest_xinco_state_xinco_workflow_id1", nullable = false)
    private int destXincoStateXincoWorkflowId1;
    @Basic(optional = false)
    @Column(name = "dest_xinco_state_xinco_workflow_version1", nullable = false)
    private int destXincoStateXincoWorkflowVersion1;

    public XincoStateTransitionsToXincoStatePK() {
    }

    public XincoStateTransitionsToXincoStatePK(int sourceStateId, int sourceXincoStateXincoWorkflowId, int sourceXincoStateXincoWorkflowVersion, int destinationStateId, int destXincoStateXincoWorkflowId1, int destXincoStateXincoWorkflowVersion1) {
        this.sourceStateId = sourceStateId;
        this.sourceXincoStateXincoWorkflowId = sourceXincoStateXincoWorkflowId;
        this.sourceXincoStateXincoWorkflowVersion = sourceXincoStateXincoWorkflowVersion;
        this.destinationStateId = destinationStateId;
        this.destXincoStateXincoWorkflowId1 = destXincoStateXincoWorkflowId1;
        this.destXincoStateXincoWorkflowVersion1 = destXincoStateXincoWorkflowVersion1;
    }

    public int getSourceStateId() {
        return sourceStateId;
    }

    public void setSourceStateId(int sourceStateId) {
        this.sourceStateId = sourceStateId;
    }

    public int getSourceXincoStateXincoWorkflowId() {
        return sourceXincoStateXincoWorkflowId;
    }

    public void setSourceXincoStateXincoWorkflowId(int sourceXincoStateXincoWorkflowId) {
        this.sourceXincoStateXincoWorkflowId = sourceXincoStateXincoWorkflowId;
    }

    public int getSourceXincoStateXincoWorkflowVersion() {
        return sourceXincoStateXincoWorkflowVersion;
    }

    public void setSourceXincoStateXincoWorkflowVersion(int sourceXincoStateXincoWorkflowVersion) {
        this.sourceXincoStateXincoWorkflowVersion = sourceXincoStateXincoWorkflowVersion;
    }

    public int getDestinationStateId() {
        return destinationStateId;
    }

    public void setDestinationStateId(int destinationStateId) {
        this.destinationStateId = destinationStateId;
    }

    public int getDestXincoStateXincoWorkflowId1() {
        return destXincoStateXincoWorkflowId1;
    }

    public void setDestXincoStateXincoWorkflowId1(int destXincoStateXincoWorkflowId1) {
        this.destXincoStateXincoWorkflowId1 = destXincoStateXincoWorkflowId1;
    }

    public int getDestXincoStateXincoWorkflowVersion1() {
        return destXincoStateXincoWorkflowVersion1;
    }

    public void setDestXincoStateXincoWorkflowVersion1(int destXincoStateXincoWorkflowVersion1) {
        this.destXincoStateXincoWorkflowVersion1 = destXincoStateXincoWorkflowVersion1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += sourceStateId;
        hash += sourceXincoStateXincoWorkflowId;
        hash += sourceXincoStateXincoWorkflowVersion;
        hash += destinationStateId;
        hash += destXincoStateXincoWorkflowId1;
        hash += destXincoStateXincoWorkflowVersion1;
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof XincoStateTransitionsToXincoStatePK)) {
            return false;
        }
        XincoStateTransitionsToXincoStatePK other = (XincoStateTransitionsToXincoStatePK) object;
        if (this.sourceStateId != other.sourceStateId) {
            return false;
        }
        if (this.sourceXincoStateXincoWorkflowId != other.sourceXincoStateXincoWorkflowId) {
            return false;
        }
        if (this.sourceXincoStateXincoWorkflowVersion != other.sourceXincoStateXincoWorkflowVersion) {
            return false;
        }
        if (this.destinationStateId != other.destinationStateId) {
            return false;
        }
        if (this.destXincoStateXincoWorkflowId1 != other.destXincoStateXincoWorkflowId1) {
            return false;
        }
        if (this.destXincoStateXincoWorkflowVersion1 != other.destXincoStateXincoWorkflowVersion1) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.persistence.XincoStateTransitionsToXincoStatePK[sourceStateId=" + sourceStateId + ", sourceXincoStateXincoWorkflowId=" + sourceXincoStateXincoWorkflowId + ", sourceXincoStateXincoWorkflowVersion=" + sourceXincoStateXincoWorkflowVersion + ", destinationStateId=" + destinationStateId + ", destXincoStateXincoWorkflowId1=" + destXincoStateXincoWorkflowId1 + ", destXincoStateXincoWorkflowVersion1=" + destXincoStateXincoWorkflowVersion1 + "]";
    }
}
