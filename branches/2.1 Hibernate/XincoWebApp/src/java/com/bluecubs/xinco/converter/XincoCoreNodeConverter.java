/*
 *  XincoCoreNodeConverter
 *
 * Created on March 25, 2008, 11:40 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreNode;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoCoreNode")
public class XincoCoreNodeConverter {
    private XincoCoreNode entity;
    private URI uri;
  
    /** Creates a new instance of XincoCoreNodeConverter */
    public XincoCoreNodeConverter() {
        entity = new XincoCoreNode();
    }

    /**
     * Creates a new instance of XincoCoreNodeConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     */
    public XincoCoreNodeConverter(XincoCoreNode entity, URI uri) {
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
     * Getter for xincoCoreLanguageId.
     *
     * @return value for xincoCoreLanguageId
     */
    @XmlElement
    public int getXincoCoreLanguageId() {
        return entity.getXincoCoreLanguageId();
    }

    /**
     * Setter for xincoCoreLanguageId.
     *
     * @param value the value to set
     */
    public void setXincoCoreLanguageId(int value) {
        entity.setXincoCoreLanguageId(value);
    }

    /**
     * Getter for designation.
     *
     * @return value for designation
     */
    @XmlElement
    public String getDesignation() {
        return entity.getDesignation();
    }

    /**
     * Setter for designation.
     *
     * @param value the value to set
     */
    public void setDesignation(String value) {
        entity.setDesignation(value);
    }

    /**
     * Getter for statusNumber.
     *
     * @return value for statusNumber
     */
    @XmlElement
    public int getStatusNumber() {
        return entity.getStatusNumber();
    }

    /**
     * Setter for statusNumber.
     *
     * @param value the value to set
     */
    public void setStatusNumber(int value) {
        entity.setStatusNumber(value);
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
     * Returns the XincoCoreNode entity.
     *
     * @return an entity
     */
    @XmlTransient
    public XincoCoreNode getEntity() {
        return entity;
    }

    /**
     * Sets the XincoCoreNode entity.
     *
     * @param entity to set
     */
    public void setEntity(XincoCoreNode entity) {
        this.entity = entity;
    }
}
