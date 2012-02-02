package com.bluecubs.xinco.workflow.persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.TableGenerator;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Embeddable
public class XincoWorkflowStatePK implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "WFSKEYGEN")
    @TableGenerator(name = "WFSKEYGEN", table = "xinco_id",
    pkColumnName = "tablename", valueColumnName = "last_id",
    pkColumnValue = "xinco_workflow_state", initialValue = 1001, allocationSize = 1)
    private int id;
    @Basic(optional = false)
    @Column(name = "xinco_workflow_id", nullable = false)
    private int xincoWorkflowId;
    @Basic(optional = false)
    @Column(name = "xinco_workflow_version", nullable = false)
    private int xincoWorkflowVersion;

    public XincoWorkflowStatePK() {
    }

    public XincoWorkflowStatePK(int xincoWorkflowId, int xincoWorkflowVersion) {
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
        hash += id;
        hash += xincoWorkflowId;
        hash += xincoWorkflowVersion;
        return hash;
    }

    @Override
    public boolean equals(Object object) {

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
        return "com.bluecubs.xinco.workflow.persistence.XincoWorkflowStatePK[id=" + id + ", xincoWorkflowId=" + xincoWorkflowId + ", xincoWorkflowVersion=" + xincoWorkflowVersion + "]";
    }
}
