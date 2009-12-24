/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Embeddable
public class XincoStateTransitionsToXincoStatePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "source_state_id", nullable = false)
    private int sourceStateId;
    @Basic(optional = false)
    @Column(name = "xinco_state_xinco_workflow_id", nullable = false)
    private int xincoStateXincoWorkflowId;
    @Basic(optional = false)
    @Column(name = "xinco_state_xinco_workflow_version", nullable = false)
    private int xincoStateXincoWorkflowVersion;
    @Basic(optional = false)
    @Column(name = "destination_state_id", nullable = false)
    private int destinationStateId;
    @Basic(optional = false)
    @Column(name = "xinco_state_xinco_workflow_id1", nullable = false)
    private int xincoStateXincoWorkflowId1;
    @Basic(optional = false)
    @Column(name = "xinco_state_xinco_workflow_version1", nullable = false)
    private int xincoStateXincoWorkflowVersion1;

    public XincoStateTransitionsToXincoStatePK() {
    }

    public XincoStateTransitionsToXincoStatePK(int sourceStateId, int xincoStateXincoWorkflowId, int xincoStateXincoWorkflowVersion, int destinationStateId, int xincoStateXincoWorkflowId1, int xincoStateXincoWorkflowVersion1) {
        this.sourceStateId = sourceStateId;
        this.xincoStateXincoWorkflowId = xincoStateXincoWorkflowId;
        this.xincoStateXincoWorkflowVersion = xincoStateXincoWorkflowVersion;
        this.destinationStateId = destinationStateId;
        this.xincoStateXincoWorkflowId1 = xincoStateXincoWorkflowId1;
        this.xincoStateXincoWorkflowVersion1 = xincoStateXincoWorkflowVersion1;
    }

    public int getSourceStateId() {
        return sourceStateId;
    }

    public void setSourceStateId(int sourceStateId) {
        this.sourceStateId = sourceStateId;
    }

    public int getXincoStateXincoWorkflowId() {
        return xincoStateXincoWorkflowId;
    }

    public void setXincoStateXincoWorkflowId(int xincoStateXincoWorkflowId) {
        this.xincoStateXincoWorkflowId = xincoStateXincoWorkflowId;
    }

    public int getXincoStateXincoWorkflowVersion() {
        return xincoStateXincoWorkflowVersion;
    }

    public void setXincoStateXincoWorkflowVersion(int xincoStateXincoWorkflowVersion) {
        this.xincoStateXincoWorkflowVersion = xincoStateXincoWorkflowVersion;
    }

    public int getDestinationStateId() {
        return destinationStateId;
    }

    public void setDestinationStateId(int destinationStateId) {
        this.destinationStateId = destinationStateId;
    }

    public int getXincoStateXincoWorkflowId1() {
        return xincoStateXincoWorkflowId1;
    }

    public void setXincoStateXincoWorkflowId1(int xincoStateXincoWorkflowId1) {
        this.xincoStateXincoWorkflowId1 = xincoStateXincoWorkflowId1;
    }

    public int getXincoStateXincoWorkflowVersion1() {
        return xincoStateXincoWorkflowVersion1;
    }

    public void setXincoStateXincoWorkflowVersion1(int xincoStateXincoWorkflowVersion1) {
        this.xincoStateXincoWorkflowVersion1 = xincoStateXincoWorkflowVersion1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) sourceStateId;
        hash += (int) xincoStateXincoWorkflowId;
        hash += (int) xincoStateXincoWorkflowVersion;
        hash += (int) destinationStateId;
        hash += (int) xincoStateXincoWorkflowId1;
        hash += (int) xincoStateXincoWorkflowVersion1;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoStateTransitionsToXincoStatePK)) {
            return false;
        }
        XincoStateTransitionsToXincoStatePK other = (XincoStateTransitionsToXincoStatePK) object;
        if (this.sourceStateId != other.sourceStateId) {
            return false;
        }
        if (this.xincoStateXincoWorkflowId != other.xincoStateXincoWorkflowId) {
            return false;
        }
        if (this.xincoStateXincoWorkflowVersion != other.xincoStateXincoWorkflowVersion) {
            return false;
        }
        if (this.destinationStateId != other.destinationStateId) {
            return false;
        }
        if (this.xincoStateXincoWorkflowId1 != other.xincoStateXincoWorkflowId1) {
            return false;
        }
        if (this.xincoStateXincoWorkflowVersion1 != other.xincoStateXincoWorkflowVersion1) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.db.XincoStateTransitionsToXincoStatePK[sourceStateId=" + sourceStateId + ", xincoStateXincoWorkflowId=" + xincoStateXincoWorkflowId + ", xincoStateXincoWorkflowVersion=" + xincoStateXincoWorkflowVersion + ", destinationStateId=" + destinationStateId + ", xincoStateXincoWorkflowId1=" + xincoStateXincoWorkflowId1 + ", xincoStateXincoWorkflowVersion1=" + xincoStateXincoWorkflowVersion1 + "]";
    }

}
