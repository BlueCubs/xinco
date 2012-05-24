/*
 * Copyright 2012 blueCubs.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *************************************************************
 * This project supports the blueCubs vision of giving back to the community in
 * exchange for free software! More information on: http://www.bluecubs.org
 * ************************************************************
 * 
 * Name: XincoAddAttribute
 * 
 * Description: Table entity
 * 
 * Original Author: Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com> Date: Nov 29, 2011
 * 
 * ************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.server.AuditedEntityListener;
import com.bluecubs.xinco.core.server.XincoAuditedObject;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_add_attribute")
@EntityListeners(AuditedEntityListener.class)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "XincoAddAttribute.findAll", query = "SELECT x FROM XincoAddAttribute x"),
    @NamedQuery(name = "XincoAddAttribute.findByXincoCoreDataId", query = "SELECT x FROM XincoAddAttribute x WHERE x.xincoAddAttributePK.xincoCoreDataId = :xincoCoreDataId"),
    @NamedQuery(name = "XincoAddAttribute.findByAttributeId", query = "SELECT x FROM XincoAddAttribute x WHERE x.xincoAddAttributePK.attributeId = :attributeId"),
    @NamedQuery(name = "XincoAddAttribute.findByAttribInt", query = "SELECT x FROM XincoAddAttribute x WHERE x.attribInt = :attribInt"),
    @NamedQuery(name = "XincoAddAttribute.findByAttribUnsignedint", query = "SELECT x FROM XincoAddAttribute x WHERE x.attribUnsignedint = :attribUnsignedint"),
    @NamedQuery(name = "XincoAddAttribute.findByAttribDouble", query = "SELECT x FROM XincoAddAttribute x WHERE x.attribDouble = :attribDouble"),
    @NamedQuery(name = "XincoAddAttribute.findByAttribVarchar", query = "SELECT x FROM XincoAddAttribute x WHERE x.attribVarchar = :attribVarchar"),
    @NamedQuery(name = "XincoAddAttribute.findByAttribDatetime", query = "SELECT x FROM XincoAddAttribute x WHERE x.attribDatetime = :attribDatetime")})
public class XincoAddAttribute extends XincoAuditedObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XincoAddAttributePK xincoAddAttributePK;
    @Column(name = "attrib_int")
    private Integer attribInt;
    @Column(name = "attrib_unsignedint")
    private long attribUnsignedint;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "attrib_double")
    private Double attribDouble;
    @Size(max = 255)
    @Column(name = "attrib_varchar")
    private String attribVarchar;
    @Lob
    @Size(max = 65535)
    @Column(name = "attrib_text")
    private String attribText;
    @Column(name = "attrib_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date attribDatetime;
    @JoinColumn(name = "xinco_core_data_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private XincoCoreData xincoCoreData;

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

    public long getAttribUnsignedint() {
        return attribUnsignedint;
    }

    public void setAttribUnsignedint(long attribUnsignedint) {
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

    public XincoCoreData getXincoCoreData() {
        return xincoCoreData;
    }

    public void setXincoCoreData(XincoCoreData xincoCoreData) {
        this.xincoCoreData = xincoCoreData;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xincoAddAttributePK != null ? xincoAddAttributePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
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
        return "com.bluecubs.xinco.core.server.persistence.XincoAddAttribute[ xincoAddAttributePK=" + xincoAddAttributePK + " ]";
    }
}
