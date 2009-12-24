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
public class XincoParameterPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private int id;
    @Basic(optional = false)
    @Column(name = "xinco_action_id", nullable = false)
    private int xincoActionId;

    public XincoParameterPK() {
    }

    public XincoParameterPK(int id, int xincoActionId) {
        this.id = id;
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
        hash += (int) id;
        hash += (int) xincoActionId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
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
        return "com.bluecubs.xinco.workflow.db.XincoParameterPK[id=" + id + ", xincoActionId=" + xincoActionId + "]";
    }

}
