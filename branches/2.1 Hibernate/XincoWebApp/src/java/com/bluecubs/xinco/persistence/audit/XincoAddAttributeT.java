/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.persistence.audit;

import java.io.Serializable;
import java.util.Date;
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
 * Class: XincoAddAttributeT
 * Created: Mar 24, 2008 2:44:25 PM
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_add_attribute_t")
@NamedQueries({@NamedQuery(name = "XincoAddAttributeT.findByRecordId", query = "SELECT x FROM XincoAddAttributeT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoAddAttributeT.findByAttribDatetime", query = "SELECT x FROM XincoAddAttributeT x WHERE x.attribDatetime = :attribDatetime"), @NamedQuery(name = "XincoAddAttributeT.findByAttribDouble", query = "SELECT x FROM XincoAddAttributeT x WHERE x.attribDouble = :attribDouble"), @NamedQuery(name = "XincoAddAttributeT.findByAttribInt", query = "SELECT x FROM XincoAddAttributeT x WHERE x.attribInt = :attribInt"), @NamedQuery(name = "XincoAddAttributeT.findByAttribUnsignedint", query = "SELECT x FROM XincoAddAttributeT x WHERE x.attribUnsignedint = :attribUnsignedint"), @NamedQuery(name = "XincoAddAttributeT.findByAttribVarchar", query = "SELECT x FROM XincoAddAttributeT x WHERE x.attribVarchar = :attribVarchar"), @NamedQuery(name = "XincoAddAttributeT.findByAttributeId", query = "SELECT x FROM XincoAddAttributeT x WHERE x.attributeId = :attributeId"), @NamedQuery(name = "XincoAddAttributeT.findByXincoCoreDataId", query = "SELECT x FROM XincoAddAttributeT x WHERE x.xincoCoreDataId = :xincoCoreDataId")})
public class XincoAddAttributeT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Column(name = "attrib_datetime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date attribDatetime;
    @Column(name = "attrib_double", nullable = false)
    private double attribDouble;
    @Column(name = "attrib_int", nullable = false)
    private int attribInt;
    @Lob
    @Column(name = "attrib_text", nullable = false)
    private String attribText;
    @Column(name = "attrib_unsignedint", nullable = false)
    private int attribUnsignedint;
    @Column(name = "attrib_varchar", nullable = false)
    private String attribVarchar;
    @Column(name = "attribute_id", nullable = false)
    private int attributeId;
    @Column(name = "xinco_core_data_id", nullable = false)
    private int xincoCoreDataId;

    public XincoAddAttributeT() {
    }

    public XincoAddAttributeT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoAddAttributeT(Integer recordId, Date attribDatetime, double attribDouble, int attribInt, String attribText, int attribUnsignedint, String attribVarchar, int attributeId, int xincoCoreDataId) {
        this.recordId = recordId;
        this.attribDatetime = attribDatetime;
        this.attribDouble = attribDouble;
        this.attribInt = attribInt;
        this.attribText = attribText;
        this.attribUnsignedint = attribUnsignedint;
        this.attribVarchar = attribVarchar;
        this.attributeId = attributeId;
        this.xincoCoreDataId = xincoCoreDataId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Date getAttribDatetime() {
        return attribDatetime;
    }

    public void setAttribDatetime(Date attribDatetime) {
        this.attribDatetime = attribDatetime;
    }

    public double getAttribDouble() {
        return attribDouble;
    }

    public void setAttribDouble(double attribDouble) {
        this.attribDouble = attribDouble;
    }

    public int getAttribInt() {
        return attribInt;
    }

    public void setAttribInt(int attribInt) {
        this.attribInt = attribInt;
    }

    public String getAttribText() {
        return attribText;
    }

    public void setAttribText(String attribText) {
        this.attribText = attribText;
    }

    public int getAttribUnsignedint() {
        return attribUnsignedint;
    }

    public void setAttribUnsignedint(int attribUnsignedint) {
        this.attribUnsignedint = attribUnsignedint;
    }

    public String getAttribVarchar() {
        return attribVarchar;
    }

    public void setAttribVarchar(String attribVarchar) {
        this.attribVarchar = attribVarchar;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public int getXincoCoreDataId() {
        return xincoCoreDataId;
    }

    public void setXincoCoreDataId(int xincoCoreDataId) {
        this.xincoCoreDataId = xincoCoreDataId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (recordId != null ? recordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
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
        return "com.bluecubs.xinco.persistence.audit.XincoAddAttributeT[recordId=" + recordId + "]";
    }

}
