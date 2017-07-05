
package com.bluecubs.xinco.core.server.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getAllXincoCoreDataTypes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getAllXincoCoreDataTypes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="in0" type="{http://service.server.xinco.bluecubs.com/}xincoCoreUser" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAllXincoCoreDataTypes", propOrder = {
    "in0"
})
public class GetAllXincoCoreDataTypes {

    protected XincoCoreUser in0;

    /**
     * Gets the value of the in0 property.
     * 
     * @return
     *     possible object is
     *     {@link XincoCoreUser }
     *     
     */
    public XincoCoreUser getIn0() {
        return in0;
    }

    /**
     * Sets the value of the in0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XincoCoreUser }
     *     
     */
    public void setIn0(XincoCoreUser value) {
        this.in0 = value;
    }

}
