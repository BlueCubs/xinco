
package com.bluecubs.xinco.core.server.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for xincoVersion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="xincoVersion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="version_high" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="version_low" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="version_mid" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="version_postfix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xincoVersion", propOrder = {
    "versionHigh",
    "versionLow",
    "versionMid",
    "versionPostfix"
})
public class XincoVersion {

    @XmlElement(name = "version_high")
    protected int versionHigh;
    @XmlElement(name = "version_low")
    protected int versionLow;
    @XmlElement(name = "version_mid")
    protected int versionMid;
    @XmlElement(name = "version_postfix")
    protected String versionPostfix;

    /**
     * Gets the value of the versionHigh property.
     * 
     */
    public int getVersionHigh() {
        return versionHigh;
    }

    /**
     * Sets the value of the versionHigh property.
     * 
     */
    public void setVersionHigh(int value) {
        this.versionHigh = value;
    }

    /**
     * Gets the value of the versionLow property.
     * 
     */
    public int getVersionLow() {
        return versionLow;
    }

    /**
     * Sets the value of the versionLow property.
     * 
     */
    public void setVersionLow(int value) {
        this.versionLow = value;
    }

    /**
     * Gets the value of the versionMid property.
     * 
     */
    public int getVersionMid() {
        return versionMid;
    }

    /**
     * Sets the value of the versionMid property.
     * 
     */
    public void setVersionMid(int value) {
        this.versionMid = value;
    }

    /**
     * Gets the value of the versionPostfix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionPostfix() {
        return versionPostfix;
    }

    /**
     * Sets the value of the versionPostfix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionPostfix(String value) {
        this.versionPostfix = value;
    }

}
