/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluecubs.xinco.client;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author ortizbj
 */
@Entity
@Table(name = "xinco_setting")
@NamedQueries({@NamedQuery(name = "XincoSetting.findById", query = "SELECT x FROM XincoSetting x WHERE x.id = :id"), @NamedQuery(name = "XincoSetting.findByDescription", query = "SELECT x FROM XincoSetting x WHERE x.description = :description"), @NamedQuery(name = "XincoSetting.findByIntValue", query = "SELECT x FROM XincoSetting x WHERE x.intValue = :intValue"), @NamedQuery(name = "XincoSetting.findByStringValue", query = "SELECT x FROM XincoSetting x WHERE x.stringValue = :stringValue"), @NamedQuery(name = "XincoSetting.findByBoolValue", query = "SELECT x FROM XincoSetting x WHERE x.boolValue = :boolValue"), @NamedQuery(name = "XincoSetting.findByLongValue", query = "SELECT x FROM XincoSetting x WHERE x.longValue = :longValue")})
public class XincoSetting implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
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
        Integer oldId = this.id;
        this.id = id;
        changeSupport.firePropertyChange("id", oldId, id);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        String oldDescription = this.description;
        this.description = description;
        changeSupport.firePropertyChange("description", oldDescription, description);
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        Integer oldIntValue = this.intValue;
        this.intValue = intValue;
        changeSupport.firePropertyChange("intValue", oldIntValue, intValue);
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        String oldStringValue = this.stringValue;
        this.stringValue = stringValue;
        changeSupport.firePropertyChange("stringValue", oldStringValue, stringValue);
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public void setBoolValue(Boolean boolValue) {
        Boolean oldBoolValue = this.boolValue;
        this.boolValue = boolValue;
        changeSupport.firePropertyChange("boolValue", oldBoolValue, boolValue);
    }

    public BigInteger getLongValue() {
        return longValue;
    }

    public void setLongValue(BigInteger longValue) {
        BigInteger oldLongValue = this.longValue;
        this.longValue = longValue;
        changeSupport.firePropertyChange("longValue", oldLongValue, longValue);
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
        return "com.bluecubs.xinco.tools.XincoSetting[id=" + id + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

}
