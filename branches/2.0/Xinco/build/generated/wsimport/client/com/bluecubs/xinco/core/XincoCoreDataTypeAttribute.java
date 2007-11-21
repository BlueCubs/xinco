
package com.bluecubs.xinco.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for XincoCoreDataTypeAttribute complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XincoCoreDataTypeAttribute">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="designation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="changerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="attribute_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="data_type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="size" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="xinco_core_data_type_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XincoCoreDataTypeAttribute", propOrder = {
    "designation",
    "changerID",
    "attributeId",
    "dataType",
    "size",
    "xincoCoreDataTypeId"
})
public class XincoCoreDataTypeAttribute {

    @XmlElement(required = true, nillable = true)
    protected String designation;
    protected int changerID;
    @XmlElement(name = "attribute_id")
    protected int attributeId;
    @XmlElement(name = "data_type", required = true, nillable = true)
    protected String dataType;
    protected int size;
    @XmlElement(name = "xinco_core_data_type_id")
    protected int xincoCoreDataTypeId;

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
     * Gets the value of the attributeId property.
     * 
     */
    public int getAttributeId() {
        return attributeId;
    }

    /**
     * Sets the value of the attributeId property.
     * 
     */
    public void setAttributeId(int value) {
        this.attributeId = value;
    }

    /**
     * Gets the value of the dataType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Sets the value of the dataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataType(String value) {
        this.dataType = value;
    }

    /**
     * Gets the value of the size property.
     * 
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     */
    public void setSize(int value) {
        this.size = value;
    }

    /**
     * Gets the value of the xincoCoreDataTypeId property.
     * 
     */
    public int getXincoCoreDataTypeId() {
        return xincoCoreDataTypeId;
    }

    /**
     * Sets the value of the xincoCoreDataTypeId property.
     * 
     */
    public void setXincoCoreDataTypeId(int value) {
        this.xincoCoreDataTypeId = value;
    }

}
