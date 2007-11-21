
package com.bluecubs.xinco.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for XincoCoreLog complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XincoCoreLog">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="op_code" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="changerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="op_datetime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="op_description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="version" type="{http://core.xinco.bluecubs.com}XincoVersion"/>
 *         &lt;element name="xinco_core_data_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="xinco_core_user_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XincoCoreLog", propOrder = {
    "id",
    "opCode",
    "changerID",
    "opDatetime",
    "opDescription",
    "version",
    "xincoCoreDataId",
    "xincoCoreUserId"
})
public class XincoCoreLog {

    protected int id;
    @XmlElement(name = "op_code")
    protected int opCode;
    protected int changerID;
    @XmlElement(name = "op_datetime", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar opDatetime;
    @XmlElement(name = "op_description", required = true, nillable = true)
    protected String opDescription;
    @XmlElement(required = true, nillable = true)
    protected XincoVersion version;
    @XmlElement(name = "xinco_core_data_id")
    protected int xincoCoreDataId;
    @XmlElement(name = "xinco_core_user_id")
    protected int xincoCoreUserId;

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
     * Gets the value of the opCode property.
     * 
     */
    public int getOpCode() {
        return opCode;
    }

    /**
     * Sets the value of the opCode property.
     * 
     */
    public void setOpCode(int value) {
        this.opCode = value;
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
     * Gets the value of the opDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOpDatetime() {
        return opDatetime;
    }

    /**
     * Sets the value of the opDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOpDatetime(XMLGregorianCalendar value) {
        this.opDatetime = value;
    }

    /**
     * Gets the value of the opDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpDescription() {
        return opDescription;
    }

    /**
     * Sets the value of the opDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpDescription(String value) {
        this.opDescription = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link XincoVersion }
     *     
     */
    public XincoVersion getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link XincoVersion }
     *     
     */
    public void setVersion(XincoVersion value) {
        this.version = value;
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
     * Gets the value of the xincoCoreUserId property.
     * 
     */
    public int getXincoCoreUserId() {
        return xincoCoreUserId;
    }

    /**
     * Sets the value of the xincoCoreUserId property.
     * 
     */
    public void setXincoCoreUserId(int value) {
        this.xincoCoreUserId = value;
    }

}
