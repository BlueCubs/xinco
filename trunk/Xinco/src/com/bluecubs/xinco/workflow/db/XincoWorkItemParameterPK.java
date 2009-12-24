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
public class XincoWorkItemParameterPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private int id;
    @Basic(optional = false)
    @Column(name = "xinco_work_item_id", nullable = false)
    private int xincoWorkItemId;
    @Basic(optional = false)
    @Column(name = "xinco_state_id", nullable = false)
    private int xincoStateId;
    @Basic(optional = false)
    @Column(name = "xinco_workflow_id", nullable = false)
    private int xincoWorkflowId;
    @Basic(optional = false)
    @Column(name = "xinco_workflow_version", nullable = false)
    private int xincoWorkflowVersion;

    public XincoWorkItemParameterPK() {
    }

    public XincoWorkItemParameterPK(int id, int xincoWorkItemId, int xincoStateId, int xincoWorkflowId, int xincoWorkflowVersion) {
        this.id = id;
        this.xincoWorkItemId = xincoWorkItemId;
        this.xincoStateId = xincoStateId;
        this.xincoWorkflowId = xincoWorkflowId;
        this.xincoWorkflowVersion = xincoWorkflowVersion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getXincoWorkflowId() {
        return xincoWorkflowId;
    }

    public void setXincoWorkflowId(int xincoWorkflowId) {
        this.xincoWorkflowId = xincoWorkflowId;
    }

    public int getXincoWorkflowVersion() {
        return xincoWorkflowVersion;
    }

    public void setXincoWorkflowVersion(int xincoWorkflowVersion) {
        this.xincoWorkflowVersion = xincoWorkflowVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) xincoWorkItemId;
        hash += (int) xincoStateId;
        hash += (int) xincoWorkflowId;
        hash += (int) xincoWorkflowVersion;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoWorkItemParameterPK)) {
            return false;
        }
        XincoWorkItemParameterPK other = (XincoWorkItemParameterPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.xincoWorkItemId != other.xincoWorkItemId) {
            return false;
        }
        if (this.xincoStateId != other.xincoStateId) {
            return false;
        }
        if (this.xincoWorkflowId != other.xincoWorkflowId) {
            return false;
        }
        if (this.xincoWorkflowVersion != other.xincoWorkflowVersion) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.db.XincoWorkItemParameterPK[id=" + id + ", xincoWorkItemId=" + xincoWorkItemId + ", xincoStateId=" + xincoStateId + ", xincoWorkflowId=" + xincoWorkflowId + ", xincoWorkflowVersion=" + xincoWorkflowVersion + "]";
    }

}
