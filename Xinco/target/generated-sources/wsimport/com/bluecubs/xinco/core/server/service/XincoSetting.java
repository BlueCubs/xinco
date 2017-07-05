
package com.bluecubs.xinco.core.server.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for xincoSetting complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="xincoSetting">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bool_value" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="changerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="int_value" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="long_value" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="string_value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="xinco_settings" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xincoSetting", propOrder = {
    "boolValue",
    "changerID",
    "description",
    "id",
    "intValue",
    "longValue",
    "stringValue",
    "xincoSettings"
})
public class XincoSetting {

    @XmlElement(name = "bool_value")
    protected boolean boolValue;
    protected int changerID;
    protected String description;
    protected int id;
    @XmlElement(name = "int_value")
    protected int intValue;
    @XmlElement(name = "long_value")
    protected long longValue;
    @XmlElement(name = "string_value")
    protected String stringValue;
    @XmlElement(name = "xinco_settings", nillable = true)
    protected List<Object> xincoSettings;

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
     * Gets the value of the xincoSettings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the xincoSettings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getXincoSettings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getXincoSettings() {
        if (xincoSettings == null) {
            xincoSettings = new ArrayList<Object>();
        }
        return this.xincoSettings;
    }

}
