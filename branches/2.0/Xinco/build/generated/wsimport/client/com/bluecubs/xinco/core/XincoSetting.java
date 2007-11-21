
package com.bluecubs.xinco.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for XincoSetting complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XincoSetting">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="int_value" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="string_value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="bool_value" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="changerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="long_value" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="xinco_settings" type="{http://xml.apache.org/xml-soap}Vector"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XincoSetting", propOrder = {
    "id",
    "description",
    "intValue",
    "stringValue",
    "boolValue",
    "changerID",
    "longValue",
    "xincoSettings"
})
public class XincoSetting {

    protected int id;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(name = "int_value")
    protected int intValue;
    @XmlElement(name = "string_value", required = true)
    protected String stringValue;
    @XmlElement(name = "bool_value")
    protected boolean boolValue;
    protected int changerID;
    @XmlElement(name = "long_value")
    protected long longValue;
    @XmlElement(name = "xinco_settings", required = true, nillable = true)
    protected Vector xincoSettings;

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the intValue property.
     * 
     */
    public int getIntValue() {
        return intValue;
    }

    /**
     * Sets the value of the intValue property.
     * 
     */
    public void setIntValue(int value) {
        this.intValue = value;
    }

    /**
     * Gets the value of the stringValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * Sets the value of the stringValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStringValue(String value) {
        this.stringValue = value;
    }

    /**
     * Gets the value of the boolValue property.
     * 
     */
    public boolean isBoolValue() {
        return boolValue;
    }

    /**
     * Sets the value of the boolValue property.
     * 
     */
    public void setBoolValue(boolean value) {
        this.boolValue = value;
    }

    /**
     * Gets the value of the changerID property.
     * 
     */
    public int getChangerID() {
        return changerID;
    }

    /**
     * Sets the value of the changerID property.
     * 
     */
    public void setChangerID(int value) {
        this.changerID = value;
    }

    /**
     * Gets the value of the longValue property.
     * 
     */
    public long getLongValue() {
        return longValue;
    }

    /**
     * Sets the value of the longValue property.
     * 
     */
    public void setLongValue(long value) {
        this.longValue = value;
    }

    /**
     * Gets the value of the xincoSettings property.
     * 
     * @return
     *     possible object is
     *     {@link Vector }
     *     
     */
    public Vector getXincoSettings() {
        return xincoSettings;
    }

    /**
     * Sets the value of the xincoSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vector }
     *     
     */
    public void setXincoSettings(Vector value) {
        this.xincoSettings = value;
    }

}
