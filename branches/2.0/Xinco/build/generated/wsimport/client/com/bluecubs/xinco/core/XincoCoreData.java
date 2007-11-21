
package com.bluecubs.xinco.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for XincoCoreData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XincoCoreData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="designation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="xinco_core_acl" type="{http://xml.apache.org/xml-soap}Vector"/>
 *         &lt;element name="changerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="xinco_core_data_type" type="{http://core.xinco.bluecubs.com}XincoCoreDataType"/>
 *         &lt;element name="xinco_core_language" type="{http://core.xinco.bluecubs.com}XincoCoreLanguage"/>
 *         &lt;element name="xinco_core_node_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="status_number" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="xinco_add_attributes" type="{http://xml.apache.org/xml-soap}Vector"/>
 *         &lt;element name="xinco_core_logs" type="{http://xml.apache.org/xml-soap}Vector"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XincoCoreData", propOrder = {
    "designation",
    "xincoCoreAcl",
    "changerID",
    "xincoCoreDataType",
    "xincoCoreLanguage",
    "xincoCoreNodeId",
    "id",
    "statusNumber",
    "xincoAddAttributes",
    "xincoCoreLogs"
})
public class XincoCoreData {

    @XmlElement(required = true, nillable = true)
    protected String designation;
    @XmlElement(name = "xinco_core_acl", required = true, nillable = true)
    protected Vector xincoCoreAcl;
    protected int changerID;
    @XmlElement(name = "xinco_core_data_type", required = true, nillable = true)
    protected XincoCoreDataType xincoCoreDataType;
    @XmlElement(name = "xinco_core_language", required = true, nillable = true)
    protected XincoCoreLanguage xincoCoreLanguage;
    @XmlElement(name = "xinco_core_node_id")
    protected int xincoCoreNodeId;
    protected int id;
    @XmlElement(name = "status_number")
    protected int statusNumber;
    @XmlElement(name = "xinco_add_attributes", required = true, nillable = true)
    protected Vector xincoAddAttributes;
    @XmlElement(name = "xinco_core_logs", required = true, nillable = true)
    protected Vector xincoCoreLogs;

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
     * Gets the value of the xincoCoreAcl property.
     * 
     * @return
     *     possible object is
     *     {@link Vector }
     *     
     */
    public Vector getXincoCoreAcl() {
        return xincoCoreAcl;
    }

    /**
     * Sets the value of the xincoCoreAcl property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vector }
     *     
     */
    public void setXincoCoreAcl(Vector value) {
        this.xincoCoreAcl = value;
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
     * Gets the value of the xincoCoreDataType property.
     * 
     * @return
     *     possible object is
     *     {@link XincoCoreDataType }
     *     
     */
    public XincoCoreDataType getXincoCoreDataType() {
        return xincoCoreDataType;
    }

    /**
     * Sets the value of the xincoCoreDataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link XincoCoreDataType }
     *     
     */
    public void setXincoCoreDataType(XincoCoreDataType value) {
        this.xincoCoreDataType = value;
    }

    /**
     * Gets the value of the xincoCoreLanguage property.
     * 
     * @return
     *     possible object is
     *     {@link XincoCoreLanguage }
     *     
     */
    public XincoCoreLanguage getXincoCoreLanguage() {
        return xincoCoreLanguage;
    }

    /**
     * Sets the value of the xincoCoreLanguage property.
     * 
     * @param value
     *     allowed object is
     *     {@link XincoCoreLanguage }
     *     
     */
    public void setXincoCoreLanguage(XincoCoreLanguage value) {
        this.xincoCoreLanguage = value;
    }

    /**
     * Gets the value of the xincoCoreNodeId property.
     * 
     */
    public int getXincoCoreNodeId() {
        return xincoCoreNodeId;
    }

    /**
     * Sets the value of the xincoCoreNodeId property.
     * 
     */
    public void setXincoCoreNodeId(int value) {
        this.xincoCoreNodeId = value;
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
     * Gets the value of the statusNumber property.
     * 
     */
    public int getStatusNumber() {
        return statusNumber;
    }

    /**
     * Sets the value of the statusNumber property.
     * 
     */
    public void setStatusNumber(int value) {
        this.statusNumber = value;
    }

    /**
     * Gets the value of the xincoAddAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link Vector }
     *     
     */
    public Vector getXincoAddAttributes() {
        return xincoAddAttributes;
    }

    /**
     * Sets the value of the xincoAddAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vector }
     *     
     */
    public void setXincoAddAttributes(Vector value) {
        this.xincoAddAttributes = value;
    }

    /**
     * Gets the value of the xincoCoreLogs property.
     * 
     * @return
     *     possible object is
     *     {@link Vector }
     *     
     */
    public Vector getXincoCoreLogs() {
        return xincoCoreLogs;
    }

    /**
     * Sets the value of the xincoCoreLogs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vector }
     *     
     */
    public void setXincoCoreLogs(Vector value) {
        this.xincoCoreLogs = value;
    }

}
