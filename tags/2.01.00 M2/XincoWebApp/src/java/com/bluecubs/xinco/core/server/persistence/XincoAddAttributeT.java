/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server.persistence;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_add_attribute_t")
@NamedQueries({@NamedQuery(name = "XincoAddAttributeT.findAll", query = "SELECT x FROM XincoAddAttributeT x"), @NamedQuery(name = "XincoAddAttributeT.findByRecordId", query = "SELECT x FROM XincoAddAttributeT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoAddAttributeT.findByXincoCoreDataId", query = "SELECT x FROM XincoAddAttributeT x WHERE x.xincoCoreDataId = :xincoCoreDataId"), @NamedQuery(name = "XincoAddAttributeT.findByAttributeId", query = "SELECT x FROM XincoAddAttributeT x WHERE x.attributeId = :attributeId"), @NamedQuery(name = "XincoAddAttributeT.findByAttribInt", query = "SELECT x FROM XincoAddAttributeT x WHERE x.attribInt = :attribInt"), @NamedQuery(name = "XincoAddAttributeT.findByAttribUnsignedint", query = "SELECT x FROM XincoAddAttributeT x WHERE x.attribUnsignedint = :attribUnsignedint"), @NamedQuery(name = "XincoAddAttributeT.findByAttribDouble", query = "SELECT x FROM XincoAddAttributeT x WHERE x.attribDouble = :attribDouble"), @NamedQuery(name = "XincoAddAttributeT.findByAttribVarchar", query = "SELECT x FROM XincoAddAttributeT x WHERE x.attribVarchar = :attribVarchar"), @NamedQuery(name = "XincoAddAttributeT.findByAttribDatetime", query = "SELECT x FROM XincoAddAttributeT x WHERE x.attribDatetime = :attribDatetime")})
public class XincoAddAttributeT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Basic(optional = false)
    @Column(name = "xinco_core_data_id", nullable = false)
    private int xincoCoreDataId;
    @Basic(optional = false)
    @Column(name = "attribute_id", nullable = false)
    private int attributeId;
    @Basic(optional = false)
    @Column(name = "attrib_int", nullable = false)
    private int attribInt;
    @Basic(optional = false)
    @Column(name = "attrib_unsignedint", nullable = false)
    private long attribUnsignedint;
    @Basic(optional = false)
    @Column(name = "attrib_double", nullable = false)
    private double attribDouble;
    @Basic(optional = false)
    @Column(name = "attrib_varchar", nullable = false, length = 255)
    private String attribVarchar;
    @Basic(optional = false)
    @Lob
    @Column(name = "attrib_text", nullable = false, length = 65535)
    private String attribText;
    @Basic(optional = false)
    @Column(name = "attrib_datetime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date attribDatetime;

    public XincoAddAttributeT() {
    }

    public XincoAddAttributeT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoAddAttributeT(Integer recordId, int xincoCoreDataId, int attributeId, int attribInt, long attribUnsignedint, double attribDouble, String attribVarchar, String attribText, Date attribDatetime) {
        this.recordId = recordId;
        this.xincoCoreDataId = xincoCoreDataId;
        this.attributeId = attributeId;
        this.attribInt = attribInt;
        this.attribUnsignedint = attribUnsignedint;
        this.attribDouble = attribDouble;
        this.attribVarchar = attribVarchar;
        this.attribText = attribText;
        this.attribDatetime = attribDatetime;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public int getXincoCoreDataId() {
        return xincoCoreDataId;
    }

    public void setXincoCoreDataId(int xincoCoreDataId) {
        this.xincoCoreDataId = xincoCoreDataId;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public int getAttribInt() {
        return attribInt;
    }

    public void setAttribInt(int attribInt) {
        this.attribInt = attribInt;
    }

    public long getAttribUnsignedint() {
        return attribUnsignedint;
    }

    public void setAttribUnsignedint(long attribUnsignedint) {
        this.attribUnsignedint = attribUnsignedint;
    }

    public double getAttribDouble() {
        return attribDouble;
    }

    public void setAttribDouble(double attribDouble) {
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
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof XincoAddAttributeT)) {
            return false;
        }
        XincoAddAttributeT other = (XincoAddAttributeT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.server.persistence.XincoAddAttributeT[recordId=" + recordId + "]";
    }

}
