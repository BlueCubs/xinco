
package com.bluecubs.xinco.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for XincoAddAttribute complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XincoAddAttribute">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attrib_datetime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="attrib_double" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="attrib_int" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="attrib_text" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="attrib_unsignedint" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="attrib_varchar" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="attribute_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="xinco_core_data_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="changerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="change" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XincoAddAttribute", namespace = "http://add.xinco.bluecubs.com", propOrder = {
    "attribDatetime",
    "attribDouble",
    "attribInt",
    "attribText",
    "attribUnsignedint",
    "attribVarchar",
    "attributeId",
    "xincoCoreDataId",
    "changerID",
    "change"
})
public class XincoAddAttribute {

    @XmlElement(name = "attrib_datetime", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar attribDatetime;
    @XmlElement(name = "attrib_double")
    protected double attribDouble;
    @XmlElement(name = "attrib_int")
    protected int attribInt;
    @XmlElement(name = "attrib_text", required = true, nillable = true)
    protected String attribText;
    @XmlElement(name = "attrib_unsignedint")
    protected long attribUnsignedint;
    @XmlElement(name = "attrib_varchar", required = true, nillable = true)
    protected String attribVarchar;
    @XmlElement(name = "attribute_id")
    protected int attributeId;
    @XmlElement(name = "xinco_core_data_id")
    protected int xincoCoreDataId;
    protected int changerID;
    protected boolean change;

    /**
     * Gets the value of the attribDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAttribDatetime() {
        return attribDatetime;
    }

    /**
     * Sets the value of the attribDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAttribDatetime(XMLGregorianCalendar value) {
        this.attribDatetime = value;
    }

    /**
     * Gets the value of the attribDouble property.
     * 
     */
    public double getAttribDouble() {
        return attribDouble;
    }

    /**
     * Sets the value of the attribDouble property.
     * 
     */
    public void setAttribDouble(double value) {
        this.attribDouble = value;
    }

    /**
     * Gets the value of the attribInt property.
     * 
     */
    public int getAttribInt() {
        return attribInt;
    }

    /**
     * Sets the value of the attribInt property.
     * 
     */
    public void setAttribInt(int value) {
        this.attribInt = value;
    }

    /**
     * Gets the value of the attribText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribText() {
        return attribText;
    }

    /**
     * Sets the value of the attribText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribText(String value) {
        this.attribText = value;
    }

    /**
     * Gets the value of the attribUnsignedint property.
     * 
     */
    public long getAttribUnsignedint() {
        return attribUnsignedint;
    }

    /**
     * Sets the value of the attribUnsignedint property.
     * 
     */
    public void setAttribUnsignedint(long value) {
        this.attribUnsignedint = value;
    }

    /**
     * Gets the value of the attribVarchar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribVarchar() {
        return attribVarchar;
    }

    /**
     * Sets the value of the attribVarchar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribVarchar(String value) {
        this.attribVarchar = value;
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
     * Gets the value of the xincoCoreDataId property.
     * 
     */
    public int getXincoCoreDataId() {
        return xincoCoreDataId;
    }

    /**
     * Sets the value of the xincoCoreDataId property.
     * 
     */
    public void setXincoCoreDataId(int value) {
        this.xincoCoreDataId = value;
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
     * Gets the value of the change property.
     * 
     */
    public boolean isChange() {
        return change;
    }

    /**
     * Sets the value of the change property.
     * 
     */
    public void setChange(boolean value) {
        this.change = value;
    }

}
