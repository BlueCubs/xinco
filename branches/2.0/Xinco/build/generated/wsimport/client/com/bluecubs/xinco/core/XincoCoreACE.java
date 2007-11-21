
package com.bluecubs.xinco.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for XincoCoreACE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XincoCoreACE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="admin_permission" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="changerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="execute_permission" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="xinco_core_data_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="xinco_core_group_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="xinco_core_node_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="xinco_core_user_id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="read_permission" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="write_permission" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="owner" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XincoCoreACE", propOrder = {
    "adminPermission",
    "changerID",
    "executePermission",
    "xincoCoreDataId",
    "xincoCoreGroupId",
    "xincoCoreNodeId",
    "xincoCoreUserId",
    "id",
    "readPermission",
    "writePermission",
    "owner",
    "userId"
})
public class XincoCoreACE {

    @XmlElement(name = "admin_permission")
    protected boolean adminPermission;
    protected int changerID;
    @XmlElement(name = "execute_permission")
    protected boolean executePermission;
    @XmlElement(name = "xinco_core_data_id")
    protected int xincoCoreDataId;
    @XmlElement(name = "xinco_core_group_id")
    protected int xincoCoreGroupId;
    @XmlElement(name = "xinco_core_node_id")
    protected int xincoCoreNodeId;
    @XmlElement(name = "xinco_core_user_id")
    protected int xincoCoreUserId;
    protected int id;
    @XmlElement(name = "read_permission")
    protected boolean readPermission;
    @XmlElement(name = "write_permission")
    protected boolean writePermission;
    protected boolean owner;
    protected int userId;

    /**
     * Gets the value of the adminPermission property.
     * 
     */
    public boolean isAdminPermission() {
        return adminPermission;
    }

    /**
     * Sets the value of the adminPermission property.
     * 
     */
    public void setAdminPermission(boolean value) {
        this.adminPermission = value;
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
     * Gets the value of the executePermission property.
     * 
     */
    public boolean isExecutePermission() {
        return executePermission;
    }

    /**
     * Sets the value of the executePermission property.
     * 
     */
    public void setExecutePermission(boolean value) {
        this.executePermission = value;
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
     * Gets the value of the xincoCoreGroupId property.
     * 
     */
    public int getXincoCoreGroupId() {
        return xincoCoreGroupId;
    }

    /**
     * Sets the value of the xincoCoreGroupId property.
     * 
     */
    public void setXincoCoreGroupId(int value) {
        this.xincoCoreGroupId = value;
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
     * Gets the value of the readPermission property.
     * 
     */
    public boolean isReadPermission() {
        return readPermission;
    }

    /**
     * Sets the value of the readPermission property.
     * 
     */
    public void setReadPermission(boolean value) {
        this.readPermission = value;
    }

    /**
     * Gets the value of the writePermission property.
     * 
     */
    public boolean isWritePermission() {
        return writePermission;
    }

    /**
     * Sets the value of the writePermission property.
     * 
     */
    public void setWritePermission(boolean value) {
        this.writePermission = value;
    }

    /**
     * Gets the value of the owner property.
     * 
     */
    public boolean isOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     * 
     */
    public void setOwner(boolean value) {
        this.owner = value;
    }

    /**
     * Gets the value of the userId property.
     * 
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     */
    public void setUserId(int value) {
        this.userId = value;
    }

}
