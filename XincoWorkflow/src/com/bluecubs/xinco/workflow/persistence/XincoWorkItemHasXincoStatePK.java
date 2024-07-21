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
public class XincoWorkItemHasXincoStatePK implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "xinco_work_item_id", nullable = false)
    private int xincoWorkItemId;
    @Basic(optional = false)
    @Column(name = "xinco_state_id", nullable = false)
    private int xincoStateId;
    @Basic(optional = false)
    @Column(name = "xinco_state_xinco_workflow_id", nullable = false)
    private int xincoStateXincoWorkflowId;
    @Basic(optional = false)
    @Column(name = "xinco_state_xinco_workflow_version", nullable = false)
    private int xincoStateXincoWorkflowVersion;

    public XincoWorkItemHasXincoStatePK() {
    }

    public XincoWorkItemHasXincoStatePK(int xincoWorkItemId, int xincoStateId, int xincoStateXincoWorkflowId, int xincoStateXincoWorkflowVersion) {
        this.xincoWorkItemId = xincoWorkItemId;
        this.xincoStateId = xincoStateId;
        this.xincoStateXincoWorkflowId = xincoStateXincoWorkflowId;
        this.xincoStateXincoWorkflowVersion = xincoStateXincoWorkflowVersion;
    }

    public int getXincoWorkItemId() {
        return xincoWorkItemId;
    }

    public void setXincoWorkItemId(int xincoWorkItemId) {
        this.xincoWorkItemId = xincoWorkItemId;
    }

    public int getXincoStateId() {
        return xincoStateId;
    }

    public void setXincoStateId(int xincoStateId) {
        this.xincoStateId = xincoStateId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += xincoWorkItemId;
        hash += xincoStateId;
        hash += xincoStateXincoWorkflowId;
        hash += xincoStateXincoWorkflowVersion;
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof XincoWorkItemHasXincoStatePK)) {
            return false;
        }
        XincoWorkItemHasXincoStatePK other = (XincoWorkItemHasXincoStatePK) object;
        if (this.xincoWorkItemId != other.xincoWorkItemId) {
            return false;
        }
        if (this.xincoStateId != other.xincoStateId) {
            return false;
        }
        if (this.xincoStateXincoWorkflowId != other.xincoStateXincoWorkflowId) {
            return false;
        }
        if (this.xincoStateXincoWorkflowVersion != other.xincoStateXincoWorkflowVersion) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.persistence.XincoWorkItemHasXincoStatePK[xincoWorkItemId=" + xincoWorkItemId + ", xincoStateId=" + xincoStateId + ", xincoStateXincoWorkflowId=" + xincoStateXincoWorkflowId + ", xincoStateXincoWorkflowVersion=" + xincoStateXincoWorkflowVersion + "]";
    }
}
