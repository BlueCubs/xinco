package com.bluecubs.xinco.core.persistence;

import com.bluecubs.xinco.core.hibernate.audit.XincoAbstractAuditableObject;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Javier
 */
@Entity
@Table(name = "xinco_setting", catalog = "xinco", schema = "")
@NamedQueries({@NamedQuery(name = "XincoSetting.findAll",
    query = "SELECT x FROM XincoSetting x"),
    @NamedQuery(name = "XincoSetting.findById",
    query = "SELECT x FROM XincoSetting x WHERE x.id = :id"),
    @NamedQuery(name = "XincoSetting.findByDescription",
    query = "SELECT x FROM XincoSetting x WHERE x.description = :description"),
    @NamedQuery(name = "XincoSetting.findByIntValue",
    query = "SELECT x FROM XincoSetting x WHERE x.intValue = :intValue"),
    @NamedQuery(name = "XincoSetting.findByStringValue",
    query = "SELECT x FROM XincoSetting x WHERE x.stringValue = :stringValue"),
    @NamedQuery(name = "XincoSetting.findByBoolValue",
    query = "SELECT x FROM XincoSetting x WHERE x.boolValue = :boolValue"),
    @NamedQuery(name = "XincoSetting.findByLongValue",
    query = "SELECT x FROM XincoSetting x WHERE x.longValue = :longValue")
})
public class XincoSetting extends XincoAbstractAuditableObject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", nullable = false)
    private Integer sId;
    @Basic(optional = false)
    @Column(name = "description", nullable = false, length = 45)
    private String description;
    @Column(name = "int_value")
    private Integer intValue;
    @Column(name = "string_value", length = 500)
    private String stringValue;
    @Column(name = "bool_value")
    private Boolean boolValue;
    @Column(name = "long_value")
    private long longValue;

    public XincoSetting() {
    }

    public XincoSetting(Integer id) {
        this.sId = id;
    }

    public XincoSetting(Integer id, String description) {
        this.sId = id;
        this.description = description;
    }

    public Integer getId() {
        return sId;
    }

    public void setId(Integer id) {
        this.sId = id;
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

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sId != null ? sId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoSetting)) {
            return false;
        }
        XincoSetting other = (XincoSetting) object;
        if ((this.sId == null && other.sId != null) || (this.sId != null && !this.sId.equals(other.sId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.persistence.XincoSetting[id=" + sId + "]";
    }
}
