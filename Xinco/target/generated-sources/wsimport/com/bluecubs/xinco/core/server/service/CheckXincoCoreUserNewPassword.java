
package com.bluecubs.xinco.core.server.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for checkXincoCoreUserNewPassword complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="checkXincoCoreUserNewPassword">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="in0" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="in1" type="{http://service.server.xinco.bluecubs.com/}xincoCoreUser" minOccurs="0"/>
 *         &lt;element name="in2" type="{http://service.server.xinco.bluecubs.com/}xincoCoreUser" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "checkXincoCoreUserNewPassword", propOrder = {
    "in0",
    "in1",
    "in2"
})
public class CheckXincoCoreUserNewPassword {

    protected String in0;
    protected XincoCoreUser in1;
    protected XincoCoreUser in2;

    /**
     * Gets the value of the in0 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIn0() {
        return in0;
    }

    /**
     * Sets the value of the in0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIn0(String value) {
        this.in0 = value;
    }

    /**
     * Gets the value of the in1 property.
     * 
     * @return
     *     possible object is
     *     {@link XincoCoreUser }
     *     
     */
    public XincoCoreUser getIn1() {
        return in1;
    }

    /**
     * Sets the value of the in1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XincoCoreUser }
     *     
     */
    public void setIn1(XincoCoreUser value) {
        this.in1 = value;
    }

    /**
     * Gets the value of the in2 property.
     * 
     * @return
     *     possible object is
     *     {@link XincoCoreUser }
     *     
     */
    public XincoCoreUser getIn2() {
        return in2;
    }

    /**
     * Sets the value of the in2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XincoCoreUser }
     *     
     */
    public void setIn2(XincoCoreUser value) {
        this.in2 = value;
    }

}
