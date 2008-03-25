/*
 *  XincoCoreACEConverter
 *
 * Created on March 25, 2008, 11:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreACE;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoCoreACE")
public class XincoCoreACEConverter {
    private XincoCoreACE entity;
    private URI uri;
  
    /** Creates a new instance of XincoCoreACEConverter */
    public XincoCoreACEConverter() {
        entity = new XincoCoreACE();
    }

    /**
     * Creates a new instance of XincoCoreACEConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     */
    public XincoCoreACEConverter(XincoCoreACE entity, URI uri) {
        this.entity = entity;
        this.uri = uri;
    }

    /**
     * Getter for id.
     *
     * @return value for id
     */
    @XmlElement
    public Integer getId() {
        return entity.getId();
    }

    /**
     * Setter for id.
     *
     * @param value the value to set
     */
    public void setId(Integer value) {
        entity.setId(value);
    }

    /**
     * Getter for xincoCoreUserId.
     *
     * @return value for xincoCoreUserId
     */
    @XmlElement
    public Integer getXincoCoreUserId() {
        return entity.getXincoCoreUserId();
    }

    /**
     * Setter for xincoCoreUserId.
     *
     * @param value the value to set
     */
    public void setXincoCoreUserId(Integer value) {
        entity.setXincoCoreUserId(value);
    }

    /**
     * Getter for xincoCoreGroupId.
     *
     * @return value for xincoCoreGroupId
     */
    @XmlElement
    public Integer getXincoCoreGroupId() {
        return entity.getXincoCoreGroupId();
    }

    /**
     * Setter for xincoCoreGroupId.
     *
     * @param value the value to set
     */
    public void setXincoCoreGroupId(Integer value) {
        entity.setXincoCoreGroupId(value);
    }

    /**
     * Getter for xincoCoreNodeId.
     *
     * @return value for xincoCoreNodeId
     */
    @XmlElement
    public Integer getXincoCoreNodeId() {
        return entity.getXincoCoreNodeId();
    }

    /**
     * Setter for xincoCoreNodeId.
     *
     * @param value the value to set
     */
    public void setXincoCoreNodeId(Integer value) {
        entity.setXincoCoreNodeId(value);
    }

    /**
     * Getter for xincoCoreDataId.
     *
     * @return value for xincoCoreDataId
     */
    @XmlElement
    public Integer getXincoCoreDataId() {
        return entity.getXincoCoreDataId();
    }

    /**
     * Setter for xincoCoreDataId.
     *
     * @param value the value to set
     */
    public void setXincoCoreDataId(Integer value) {
        entity.setXincoCoreDataId(value);
    }

    /**
     * Getter for readPermission.
     *
     * @return value for readPermission
     */
    @XmlElement
    public boolean getReadPermission() {
        return entity.getReadPermission();
    }

    /**
     * Setter for readPermission.
     *
     * @param value the value to set
     */
    public void setReadPermission(boolean value) {
        entity.setReadPermission(value);
    }

    /**
     * Getter for writePermission.
     *
     * @return value for writePermission
     */
    @XmlElement
    public boolean getWritePermission() {
        return entity.getWritePermission();
    }

    /**
     * Setter for writePermission.
     *
     * @param value the value to set
     */
    public void setWritePermission(boolean value) {
        entity.setWritePermission(value);
    }

    /**
     * Getter for executePermission.
     *
     * @return value for executePermission
     */
    @XmlElement
    public boolean getExecutePermission() {
        return entity.getExecutePermission();
    }

    /**
     * Setter for executePermission.
     *
     * @param value the value to set
     */
    public void setExecutePermission(boolean value) {
        entity.setExecutePermission(value);
    }

    /**
     * Getter for adminPermission.
     *
     * @return value for adminPermission
     */
    @XmlElement
    public boolean getAdminPermission() {
        return entity.getAdminPermission();
    }

    /**
     * Setter for adminPermission.
     *
     * @param value the value to set
     */
    public void setAdminPermission(boolean value) {
        entity.setAdminPermission(value);
    }

    /**
     * Returns the URI associated with this converter.
     *
     * @return the uri
     */
    @XmlAttribute(name = "uri")
    public URI getResourceUri() {
        return uri;
    }

    /**
     * Returns the XincoCoreACE entity.
     *
     * @return an entity
     */
    @XmlTransient
    public XincoCoreACE getEntity() {
        return entity;
    }

    /**
     * Sets the XincoCoreACE entity.
     *
     * @param entity to set
     */
    public void setEntity(XincoCoreACE entity) {
        this.entity = entity;
    }
}
