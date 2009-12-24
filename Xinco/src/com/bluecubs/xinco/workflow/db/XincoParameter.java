/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.workflow.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_parameter")
@NamedQueries({
    @NamedQuery(name = "XincoParameter.findAll", query = "SELECT x FROM XincoParameter x"),
    @NamedQuery(name = "XincoParameter.findById", query = "SELECT x FROM XincoParameter x WHERE x.xincoParameterPK.id = :id"),
    @NamedQuery(name = "XincoParameter.findByXincoActionId", query = "SELECT x FROM XincoParameter x WHERE x.xincoParameterPK.xincoActionId = :xincoActionId"),
    @NamedQuery(name = "XincoParameter.findByName", query = "SELECT x FROM XincoParameter x WHERE x.name = :name"),
    @NamedQuery(name = "XincoParameter.findByValueType", query = "SELECT x FROM XincoParameter x WHERE x.valueType = :valueType")})
public class XincoParameter implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoParameterPK xincoParameterPK;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Basic(optional = false)
    @Column(name = "value_type", nullable = false, length = 45)
    private String valueType;
    @Basic(optional = false)
    @Lob
    @Column(name = "value", nullable = false)
    private byte[] value;
    @JoinColumn(name = "xinco_action_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private XincoAction xincoAction;

    public XincoParameter() {
    }

    public XincoParameter(XincoParameterPK xincoParameterPK) {
        this.xincoParameterPK = xincoParameterPK;
    }

    public XincoParameter(XincoParameterPK xincoParameterPK, String name, String valueType, byte[] value) {
        this.xincoParameterPK = xincoParameterPK;
        this.name = name;
        this.valueType = valueType;
        this.value = value;
    }

    public XincoParameter(int id, int xincoActionId) {
        this.xincoParameterPK = new XincoParameterPK(id, xincoActionId);
    }

    public XincoParameterPK getXincoParameterPK() {
        return xincoParameterPK;
    }

    public void setXincoParameterPK(XincoParameterPK xincoParameterPK) {
        this.xincoParameterPK = xincoParameterPK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public XincoAction getXincoAction() {
        return xincoAction;
    }

    public void setXincoAction(XincoAction xincoAction) {
        this.xincoAction = xincoAction;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xincoParameterPK != null ? xincoParameterPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoParameter)) {
            return false;
        }
        XincoParameter other = (XincoParameter) object;
        if ((this.xincoParameterPK == null && other.xincoParameterPK != null) || (this.xincoParameterPK != null && !this.xincoParameterPK.equals(other.xincoParameterPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.workflow.db.XincoParameter[xincoParameterPK=" + xincoParameterPK + "]";
    }

}
