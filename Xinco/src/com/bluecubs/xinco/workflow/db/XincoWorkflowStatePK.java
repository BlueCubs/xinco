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
public class XincoWorkflowStatePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private int id;
    @Basic(optional = false)
    @Column(name = "xinco_workflow_id", nullable = false)
    private int xincoWorkflowId;
    @Basic(optional = false)
    @Column(name = "xinco_workflow_version", nullable = false)
    private int xincoWorkflowVersion;

    public XincoWorkflowStatePK() {
    }

    public XincoWorkflowStatePK(int id, int xincoWorkflowId, int xincoWorkflowVersion) {
        this.id = id;
        this.xincoWorkflowId = xincoWorkflowId;
        this.xincoWorkflowVersion = xincoWorkflowVersion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        hash += (int) xincoWorkflowId;
        hash += (int) xincoWorkflowVersion;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoWorkflowStatePK)) {
            return false;
        }
        XincoWorkflowStatePK other = (XincoWorkflowStatePK) object;
        if (this.id != other.id) {
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
        return "com.bluecubs.xinco.workflow.db.XincoWorkflowStatePK[id=" + id + ", xincoWorkflowId=" + xincoWorkflowId + ", xincoWorkflowVersion=" + xincoWorkflowVersion + "]";
    }

}
