/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.persistance.audit;

import com.bluecubs.xinco.core.server.persistance.audit.XincoAbstractAuditableObject;
import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Javier A. Ortiz
 */
@Entity
@Table(name = "xinco_setting_t")
@NamedQueries({@NamedQuery(name = "XincoSettingT.findByRecordId", query = "SELECT x FROM XincoSettingT x WHERE x.recordId = :recordId"), @NamedQuery(name = "XincoSettingT.findById", query = "SELECT x FROM XincoSettingT x WHERE x.id = :id"), @NamedQuery(name = "XincoSettingT.findByDescription", query = "SELECT x FROM XincoSettingT x WHERE x.description = :description"), @NamedQuery(name = "XincoSettingT.findByIntValue", query = "SELECT x FROM XincoSettingT x WHERE x.intValue = :intValue"), @NamedQuery(name = "XincoSettingT.findByStringValue", query = "SELECT x FROM XincoSettingT x WHERE x.stringValue = :stringValue"), @NamedQuery(name = "XincoSettingT.findByBoolValue", query = "SELECT x FROM XincoSettingT x WHERE x.boolValue = :boolValue"), @NamedQuery(name = "XincoSettingT.findByLongValue", query = "SELECT x FROM XincoSettingT x WHERE x.longValue = :longValue")})
public class XincoSettingT extends XincoAbstractAuditableObject implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "record_id", nullable = false)
    private Integer recordId;
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "int_value")
    private Integer intValue;
    @Column(name = "string_value")
    private String stringValue;
    @Column(name = "bool_value")
    private Boolean boolValue;
    @Column(name = "long_value")
    private BigInteger longValue;

    public XincoSettingT() {
    }

    public XincoSettingT(Integer recordId) {
        this.recordId = recordId;
    }

    public XincoSettingT(Integer recordId, int id, String description) {
        this.recordId = recordId;
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public void setBoolValue(Boolean boolValue) {
        this.boolValue = boolValue;
    }

    public BigInteger getLongValue() {
        return longValue;
    }

    public void setLongValue(BigInteger longValue) {
        this.longValue = longValue;
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
        if (!(object instanceof XincoSettingT)) {
            return false;
        }
        XincoSettingT other = (XincoSettingT) object;
        if ((this.recordId == null && other.recordId != null) || (this.recordId != null && !this.recordId.equals(other.recordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.add.XincoSettingT[recordId=" + recordId + "]";
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }
}
