
package com.bluecubs.xinco.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for XincoCoreNode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XincoCoreNode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="designation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="changerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="xinco_core_acl" type="{http://xml.apache.org/xml-soap}Vector"/>
 *         &lt;element name="xinco_core_data" type="{http://xml.apache.org/xml-soap}Vector"/>
 *         &lt;element name="xinco_core_language" type="{http://core.xinco.bluecubs.com}XincoCoreLanguage"/>
 *         &lt;element name="xinco_core_node_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="xinco_core_nodes" type="{http://xml.apache.org/xml-soap}Vector"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="status_number" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XincoCoreNode", propOrder = {
    "designation",
    "changerID",
    "xincoCoreAcl",
    "xincoCoreData",
    "xincoCoreLanguage",
    "xincoCoreNodeId",
    "xincoCoreNodes",
    "id",
    "statusNumber"
})
public class XincoCoreNode {

    @XmlElement(required = true, nillable = true)
    protected String designation;
    protected int changerID;
    @XmlElement(name = "xinco_core_acl", required = true, nillable = true)
    protected Vector xincoCoreAcl;
    @XmlElement(name = "xinco_core_data", required = true, nillable = true)
    protected Vector xincoCoreData;
    @XmlElement(name = "xinco_core_language", required = true, nillable = true)
    protected XincoCoreLanguage xincoCoreLanguage;
    @XmlElement(name = "xinco_core_node_id")
    protected int xincoCoreNodeId;
    @XmlElement(name = "xinco_core_nodes", required = true, nillable = true)
    protected Vector xincoCoreNodes;
    protected int id;
    @XmlElement(name = "status_number")
    protected int statusNumber;

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
     * Gets the value of the xincoCoreData property.
     * 
     * @return
     *     possible object is
     *     {@link Vector }
     *     
     */
    public Vector getXincoCoreData() {
        return xincoCoreData;
    }

    /**
     * Sets the value of the xincoCoreData property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vector }
     *     
     */
    public void setXincoCoreData(Vector value) {
        this.xincoCoreData = value;
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
     * Gets the value of the xincoCoreNodes property.
     * 
     * @return
     *     possible object is
     *     {@link Vector }
     *     
     */
    public Vector getXincoCoreNodes() {
        return xincoCoreNodes;
    }

    /**
     * Sets the value of the xincoCoreNodes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vector }
     *     
     */
    public void setXincoCoreNodes(Vector value) {
        this.xincoCoreNodes = value;
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

}
