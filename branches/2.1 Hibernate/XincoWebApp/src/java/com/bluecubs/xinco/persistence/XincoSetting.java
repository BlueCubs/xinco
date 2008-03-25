/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.persistence;

import com.dreamer.Hibernate.audit.XincoAbstractAuditableObject;
import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Class: XincoSetting
 * Created: Mar 24, 2008 2:26:51 PM
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_setting")
@NamedQueries({@NamedQuery(name = "XincoSetting.findById", query = "SELECT x FROM XincoSetting x WHERE x.id = :id"), @NamedQuery(name = "XincoSetting.findByDescription", query = "SELECT x FROM XincoSetting x WHERE x.description = :description"), @NamedQuery(name = "XincoSetting.findByIntValue", query = "SELECT x FROM XincoSetting x WHERE x.intValue = :intValue"), @NamedQuery(name = "XincoSetting.findByStringValue", query = "SELECT x FROM XincoSetting x WHERE x.stringValue = :stringValue"), @NamedQuery(name = "XincoSetting.findByBoolValue", query = "SELECT x FROM XincoSetting x WHERE x.boolValue = :boolValue"), @NamedQuery(name = "XincoSetting.findByLongValue", query = "SELECT x FROM XincoSetting x WHERE x.longValue = :longValue")})
public class XincoSetting extends XincoAbstractAuditableObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
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

    public XincoSetting() {
    }

    public XincoSetting(Integer id) {
        this.id = id;
    }

    public XincoSetting(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoSetting)) {
            return false;
        }
        XincoSetting other = (XincoSetting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.persistence.XincoSetting[id=" + id + "]";
    }

}
