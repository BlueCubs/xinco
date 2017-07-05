
package com.bluecubs.xinco.core.server.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for xincoCoreNode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="xincoCoreNode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="changerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="designation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="status_number" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="xinco_core_acl" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="xinco_core_data" type="{http://service.server.xinco.bluecubs.com/}xincoCoreData" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="xinco_core_language" type="{http://service.server.xinco.bluecubs.com/}xincoCoreLanguage" minOccurs="0"/>
 *         &lt;element name="xinco_core_node_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="xinco_core_nodes" type="{http://service.server.xinco.bluecubs.com/}xincoCoreNode" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xincoCoreNode", propOrder = {
    "changerID",
    "designation",
    "id",
    "statusNumber",
    "xincoCoreAcl",
    "xincoCoreData",
    "xincoCoreLanguage",
    "xincoCoreNodeId",
    "xincoCoreNodes"
})
public class XincoCoreNode {

    protected int changerID;
    protected String designation;
    protected int id;
    @XmlElement(name = "status_number")
    protected int statusNumber;
    @XmlElement(name = "xinco_core_acl", nillable = true)
    protected List<Object> xincoCoreAcl;
    @XmlElement(name = "xinco_core_data", nillable = true)
    protected List<XincoCoreData> xincoCoreData;
    @XmlElement(name = "xinco_core_language")
    protected XincoCoreLanguage xincoCoreLanguage;
    @XmlElement(name = "xinco_core_node_id")
    protected int xincoCoreNodeId;
    @XmlElement(name = "xinco_core_nodes", nillable = true)
    protected List<XincoCoreNode> xincoCoreNodes;

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
     * Gets the value of the xincoCoreAcl property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the xincoCoreAcl property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getXincoCoreAcl().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getXincoCoreAcl() {
        if (xincoCoreAcl == null) {
            xincoCoreAcl = new ArrayList<Object>();
        }
        return this.xincoCoreAcl;
    }

    /**
     * Gets the value of the xincoCoreData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the xincoCoreData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getXincoCoreData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XincoCoreData }
     * 
     * 
     */
    public List<XincoCoreData> getXincoCoreData() {
        if (xincoCoreData == null) {
            xincoCoreData = new ArrayList<XincoCoreData>();
        }
        return this.xincoCoreData;
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
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the xincoCoreNodes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getXincoCoreNodes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XincoCoreNode }
     * 
     * 
     */
    public List<XincoCoreNode> getXincoCoreNodes() {
        if (xincoCoreNodes == null) {
            xincoCoreNodes = new ArrayList<XincoCoreNode>();
        }
        return this.xincoCoreNodes;
    }

}
