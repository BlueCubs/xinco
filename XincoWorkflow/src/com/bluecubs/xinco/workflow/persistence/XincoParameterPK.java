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
public class XincoParameterPK implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PARAMKEYGEN")
    @TableGenerator(name = "PARAMKEYGEN", table = "xinco_id",
    pkColumnName = "tablename", valueColumnName = "last_id",
    pkColumnValue = "xinco_parameter", initialValue = 1001, allocationSize = 1)
    @Column(name = "id", nullable = false)
    private int id;
    @Basic(optional = false)
    @Column(name = "xinco_action_id", nullable = false)
    private int xincoActionId;

    public XincoParameterPK() {
    }

    public XincoParameterPK(int xincoActionId) {
        this.xincoActionId = xincoActionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getXincoActionId() {
        return xincoActionId;
    }

    public void setXincoActionId(int xincoActionId) {
        this.xincoActionId = xincoActionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += id;
        hash += xincoActionId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof XincoParameterPK)) {
            return false;
        }
        XincoParameterPK other = (XincoParameterPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.xincoActionId != other.xincoActionId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.persistence.XincoParameterPK[id=" + id + ", xincoActionId=" + xincoActionId + "]";
    }
}
