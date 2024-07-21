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
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Embeddable
public class XincoWorkflowPK implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "WFKEYGEN")
    @TableGenerator(name = "WFKEYGEN", table = "xinco_id",
    pkColumnName = "tablename", valueColumnName = "last_id",
    pkColumnValue = "xinco_workflow", initialValue = 1001, allocationSize = 1)
    private int id;
    @Basic(optional = false)
    @Column(name = "version", nullable = false)
    private int version;
    @Basic(optional = false)
    @Column(name = "user_link_id", nullable = false)
    private int userLinkId;

    public XincoWorkflowPK() {
    }

    public XincoWorkflowPK(int version, int userLinkId) {
        this.version = version;
        this.userLinkId = userLinkId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getUserLinkId() {
        return userLinkId;
    }

    public void setUserLinkId(int userLinkId) {
        this.userLinkId = userLinkId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += id;
        hash += version;
        hash += userLinkId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof XincoWorkflowPK)) {
            return false;
        }
        XincoWorkflowPK other = (XincoWorkflowPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.version != other.version) {
            return false;
        }
        if (this.userLinkId != other.userLinkId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.persistence.XincoWorkflowPK[id=" + id + ", version=" + version + ", userLinkId=" + userLinkId + "]";
    }
}
