/*
 *  XincoSettingConverter
 *
 * Created on March 25, 2008, 11:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoSetting;
import java.math.BigInteger;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoSetting")
public class XincoSettingConverter {
    private XincoSetting entity;
    private URI uri;
  
    /** Creates a new instance of XincoSettingConverter */
    public XincoSettingConverter() {
        entity = new XincoSetting();
    }

    /**
     * Creates a new instance of XincoSettingConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     */
    public XincoSettingConverter(XincoSetting entity, URI uri) {
        this.entity = entity;
        this.uri = uri;
    }

    /**
     * Getter for id.
     *
     * @return value for id
     */
    @XmlElement
    public Integer getId() {
        return entity.getId();
    }

    /**
     * Setter for id.
     *
     * @param value the value to set
     */
    public void setId(Integer value) {
        entity.setId(value);
    }

    /**
     * Getter for description.
     *
     * @return value for description
     */
    @XmlElement
    public String getDescription() {
        return entity.getDescription();
    }

    /**
     * Setter for description.
     *
     * @param value the value to set
     */
    public void setDescription(String value) {
        entity.setDescription(value);
    }

    /**
     * Getter for intValue.
     *
     * @return value for intValue
     */
    @XmlElement
    public Integer getIntValue() {
        return entity.getIntValue();
    }

    /**
     * Setter for intValue.
     *
     * @param value the value to set
     */
    public void setIntValue(Integer value) {
        entity.setIntValue(value);
    }

    /**
     * Getter for stringValue.
     *
     * @return value for stringValue
     */
    @XmlElement
    public String getStringValue() {
        return entity.getStringValue();
    }

    /**
     * Setter for stringValue.
     *
     * @param value the value to set
     */
    public void setStringValue(String value) {
        entity.setStringValue(value);
    }

    /**
     * Getter for boolValue.
     *
     * @return value for boolValue
     */
    @XmlElement
    public Boolean getBoolValue() {
        return entity.getBoolValue();
    }

    /**
     * Setter for boolValue.
     *
     * @param value the value to set
     */
    public void setBoolValue(Boolean value) {
        entity.setBoolValue(value);
    }

    /**
     * Getter for longValue.
     *
     * @return value for longValue
     */
    @XmlElement
    public BigInteger getLongValue() {
        return entity.getLongValue();
    }

    /**
     * Setter for longValue.
     *
     * @param value the value to set
     */
    public void setLongValue(BigInteger value) {
        entity.setLongValue(value);
    }

    /**
     * Returns the URI associated with this converter.
     *
     * @return the uri
     */
    @XmlAttribute(name = "uri")
    public URI getResourceUri() {
        return uri;
    }

    /**
     * Returns the XincoSetting entity.
     *
     * @return an entity
     */
    @XmlTransient
    public XincoSetting getEntity() {
        return entity;
    }

    /**
     * Sets the XincoSetting entity.
     *
     * @param entity to set
     */
    public void setEntity(XincoSetting entity) {
        this.entity = entity;
    }
}
