
package com.bluecubs.xinco.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for XincoCoreDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XincoCoreDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="designation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="changerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="xinco_core_data_type_attributes" type="{http://xml.apache.org/xml-soap}Vector"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XincoCoreDataType", propOrder = {
    "designation",
    "id",
    "changerID",
    "description",
    "xincoCoreDataTypeAttributes"
})
public class XincoCoreDataType {

    @XmlElement(required = true, nillable = true)
    protected String designation;
    protected int id;
    protected int changerID;
    @XmlElement(required = true, nillable = true)
    protected String description;
    @XmlElement(name = "xinco_core_data_type_attributes", required = true, nillable = true)
    protected Vector xincoCoreDataTypeAttributes;

    /**
     * Gets the value of the designation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Sets the value of the designation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesignation(String value) {
        this.designation = value;
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
     * Gets the value of the xincoCoreDataTypeAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link Vector }
     *     
     */
    public Vector getXincoCoreDataTypeAttributes() {
        return xincoCoreDataTypeAttributes;
    }

    /**
     * Sets the value of the xincoCoreDataTypeAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vector }
     *     
     */
    public void setXincoCoreDataTypeAttributes(Vector value) {
        this.xincoCoreDataTypeAttributes = value;
    }

}
