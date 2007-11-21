
package com.bluecubs.xinco.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for XincoCoreUser complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XincoCoreUser">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="change" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="changerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="firstname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="reason" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="status_number" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userpassword" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="writeGroups" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="xinco_core_groups" type="{http://xml.apache.org/xml-soap}Vector"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XincoCoreUser", propOrder = {
    "change",
    "changerID",
    "email",
    "firstname",
    "id",
    "name",
    "reason",
    "statusNumber",
    "username",
    "userpassword",
    "writeGroups",
    "xincoCoreGroups"
})
public class XincoCoreUser {

    protected boolean change;
    protected int changerID;
    @XmlElement(required = true, nillable = true)
    protected String email;
    @XmlElement(required = true, nillable = true)
    protected String firstname;
    protected int id;
    @XmlElement(required = true, nillable = true)
    protected String name;
    @XmlElement(required = true, nillable = true)
    protected String reason;
    @XmlElement(name = "status_number")
    protected int statusNumber;
    @XmlElement(required = true, nillable = true)
    protected String username;
    @XmlElement(required = true, nillable = true)
    protected String userpassword;
    protected boolean writeGroups;
    @XmlElement(name = "xinco_core_groups", required = true, nillable = true)
    protected Vector xincoCoreGroups;

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
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the firstname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Sets the value of the firstname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstname(String value) {
        this.firstname = value;
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
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReason(String value) {
        this.reason = value;
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
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the userpassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserpassword() {
        return userpassword;
    }

    /**
     * Sets the value of the userpassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserpassword(String value) {
        this.userpassword = value;
    }

    /**
     * Gets the value of the writeGroups property.
     * 
     */
    public boolean isWriteGroups() {
        return writeGroups;
    }

    /**
     * Sets the value of the writeGroups property.
     * 
     */
    public void setWriteGroups(boolean value) {
        this.writeGroups = value;
    }

    /**
     * Gets the value of the xincoCoreGroups property.
     * 
     * @return
     *     possible object is
     *     {@link Vector }
     *     
     */
    public Vector getXincoCoreGroups() {
        return xincoCoreGroups;
    }

    /**
     * Sets the value of the xincoCoreGroups property.
     * 
     * @param value
     *     allowed object is
     *     {@link Vector }
     *     
     */
    public void setXincoCoreGroups(Vector value) {
        this.xincoCoreGroups = value;
    }

}
