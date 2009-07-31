package com.bluecubs.xinco.core;

import java.util.Vector;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoCoreSetting {

    private static final long serialVersionUID = 1L;
    private int id;
    private String description;
    private int intValue;
    private String stringValue;
    private Boolean boolValue;
    private long longValue;
    private Vector xinco_settings;

    public XincoCoreSetting() {
    }

    public XincoCoreSetting(int id, String description, int intValue, String stringValue, Boolean boolValue, long longValue) {
        this.id = id;
        this.description = description;
        this.intValue = intValue;
        this.stringValue = stringValue;
        this.boolValue = boolValue;
        this.longValue = longValue;
    }

    public XincoCoreSetting(Integer id) {
        this.id = id;
    }

    public XincoCoreSetting(Integer id, String description) {
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

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
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
        hash += id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XincoCoreSetting)) {
            return false;
        }
        XincoCoreSetting other = (XincoCoreSetting) object;
        if ((this.id != other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bluecubs.xinco.core.persistence.XincoCoreSetting[id=" + id + "]";
    }

    /**
     * @return the xinco_settings
     */
    public Vector getXincoSettings() {
        return xinco_settings;
    }

    /**
     * @param xinco_settings the xinco_settings to set
     */
    public void setXincoSettings(Vector xinco_settings) {
        this.xinco_settings = xinco_settings;
    }
}
