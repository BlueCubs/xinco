/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.add.persistence;

import com.bluecubs.xinco.core.server.persistence.audit.XincoAbstractAuditableObject;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Javier A. Ortiz
 */
@Entity
@Table(name = "xinco_add_attribute")
@NamedQueries({@NamedQuery(name = "XincoAddAttribute.findByXincoCoreDataId", query = "SELECT x FROM XincoAddAttribute x WHERE x.xincoAddAttributePK.xincoCoreDataId = :xincoCoreDataId"), @NamedQuery(name = "XincoAddAttribute.findByAttributeId", query = "SELECT x FROM XincoAddAttribute x WHERE x.xincoAddAttributePK.attributeId = :attributeId"), @NamedQuery(name = "XincoAddAttribute.findByAttribInt", query = "SELECT x FROM XincoAddAttribute x WHERE x.attribInt = :attribInt"), @NamedQuery(name = "XincoAddAttribute.findByAttribUnsignedint", query = "SELECT x FROM XincoAddAttribute x WHERE x.attribUnsignedint = :attribUnsignedint"), @NamedQuery(name = "XincoAddAttribute.findByAttribDouble", query = "SELECT x FROM XincoAddAttribute x WHERE x.attribDouble = :attribDouble"), @NamedQuery(name = "XincoAddAttribute.findByAttribVarchar", query = "SELECT x FROM XincoAddAttribute x WHERE x.attribVarchar = :attribVarchar"), @NamedQuery(name = "XincoAddAttribute.findByAttribDatetime", query = "SELECT x FROM XincoAddAttribute x WHERE x.attribDatetime = :attribDatetime")})
public class XincoAddAttribute extends XincoAbstractAuditableObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoAddAttributePK xincoAddAttributePK;
    @Column(name = "attrib_int")
    private Integer attribInt;
    @Column(name = "attrib_unsignedint")
    private Integer attribUnsignedint;
    @Column(name = "attrib_double")
    private Double attribDouble;
    @Column(name = "attrib_varchar")
    private String attribVarchar;
    @Lob
    @Column(name = "attrib_text")
    private String attribText;
    @Column(name = "attrib_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date attribDatetime;

    public XincoAddAttribute() {
    }

    public XincoAddAttribute(XincoAddAttributePK xincoAddAttributePK) {
        this.xincoAddAttributePK = xincoAddAttributePK;
    }

    public XincoAddAttribute(int xincoCoreDataId, int attributeId) {
        this.xincoAddAttributePK = new XincoAddAttributePK(xincoCoreDataId, attributeId);
    }

    public XincoAddAttributePK getXincoAddAttributePK() {
        return xincoAddAttributePK;
    }

    public void setXincoAddAttributePK(XincoAddAttributePK xincoAddAttributePK) {
        this.xincoAddAttributePK = xincoAddAttributePK;
    }

    public Integer getAttribInt() {
        return attribInt;
    }

    public void setAttribInt(Integer attribInt) {
        this.attribInt = attribInt;
    }

    public Integer getAttribUnsignedint() {
        return attribUnsignedint;
    }

    public void setAttribUnsignedint(Integer attribUnsignedint) {
        this.attribUnsignedint = attribUnsignedint;
    }

    public Double getAttribDouble() {
        return attribDouble;
    }

    public void setAttribDouble(Double attribDouble) {
        this.attribDouble = attribDouble;
    }

    public String getAttribVarchar() {
        return attribVarchar;
    }

    public void setAttribVarchar(String attribVarchar) {
        this.attribVarchar = attribVarchar;
    }

    public String getAttribText() {
        return attribText;
    }

    public void setAttribText(String attribText) {
        this.attribText = attribText;
    }

    public Date getAttribDatetime() {
        return attribDatetime;
    }

    public void setAttribDatetime(Date attribDatetime) {
        this.attribDatetime = attribDatetime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xincoAddAttributePK != null ? xincoAddAttributePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoAddAttribute)) {
            return false;
        }
        XincoAddAttribute other = (XincoAddAttribute) object;
        if ((this.xincoAddAttributePK == null && other.xincoAddAttributePK != null) || (this.xincoAddAttributePK != null && !this.xincoAddAttributePK.equals(other.xincoAddAttributePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.add.persistance.XincoAddAttribute[xincoAddAttributePK=" + xincoAddAttributePK + "]";
    }

}
