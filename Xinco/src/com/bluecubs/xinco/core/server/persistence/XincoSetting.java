package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.server.AuditedEntityListener;
import com.bluecubs.xinco.core.server.XincoAuditedObject;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Javier A. Ortiz Bultr�n <javier.ortiz.78@gmail.com>
 */
@Entity
@Table(name = "xinco_setting", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"description"})})
@EntityListeners(AuditedEntityListener.class)
@NamedQueries({
    @NamedQuery(name = "XincoSetting.findAll", query = "SELECT x FROM XincoSetting x"),
    @NamedQuery(name = "XincoSetting.findById", query = "SELECT x FROM XincoSetting x WHERE x.id = :id"),
    @NamedQuery(name = "XincoSetting.findByDescription", query = "SELECT x FROM XincoSetting x WHERE x.description = :description"),
    @NamedQuery(name = "XincoSetting.findByIntValue", query = "SELECT x FROM XincoSetting x WHERE x.intValue = :intValue"),
    @NamedQuery(name = "XincoSetting.findByBoolValue", query = "SELECT x FROM XincoSetting x WHERE x.boolValue = :boolValue"),
    @NamedQuery(name = "XincoSetting.findByLongValue", query = "SELECT x FROM XincoSetting x WHERE x.longValue = :longValue")})
public class XincoSetting extends XincoAuditedObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SETTINGKEYGEN")
    @TableGenerator(name = "SETTINGKEYGEN", table = "xinco_id",
    pkColumnName = "tablename", valueColumnName = "last_id",
    pkColumnValue = "xinco_setting", initialValue = 1001, allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "description", nullable = false, length = 45)
    private String description;
    @Column(name = "int_value")
    private Integer intValue;
    @Lob
    @Column(name = "string_value", length = 65535)
    private String stringValue;
    @Column(name = "bool_value")
    private Boolean boolValue;
    @Column(name = "long_value")
    private Long longValue;

    public XincoSetting() {
    }

    public XincoSetting(String description) {
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

    public Long getLongValue() {
        return longValue;
    }

    public void setLongValue(Long longValue) {
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
        return "com.bluecubs.xinco.core.server.persistence.XincoSetting[id=" + id + "]";
    }
}
