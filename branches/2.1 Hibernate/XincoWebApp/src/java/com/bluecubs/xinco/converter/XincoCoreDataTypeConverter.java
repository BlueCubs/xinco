/*
 *  XincoCoreDataTypeConverter
 *
 * Created on March 25, 2008, 11:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreDataType;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoCoreDataType")
public class XincoCoreDataTypeConverter {
    private XincoCoreDataType entity;
    private URI uri;
  
    /** Creates a new instance of XincoCoreDataTypeConverter */
    public XincoCoreDataTypeConverter() {
        entity = new XincoCoreDataType();
    }

    /**
     * Creates a new instance of XincoCoreDataTypeConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     */
    public XincoCoreDataTypeConverter(XincoCoreDataType entity, URI uri) {
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
     * Getter for description.
     *
     * @return value for description
     */
    @XmlElement
    public String getDescription() {
        return entity.getDescription();
    }

    /**
     * Setter for description.
     *
     * @param value the value to set
     */
    public void setDescription(String value) {
        entity.setDescription(value);
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
     * Returns the XincoCoreDataType entity.
     *
     * @return an entity
     */
    @XmlTransient
    public XincoCoreDataType getEntity() {
        return entity;
    }

    /**
     * Sets the XincoCoreDataType entity.
     *
     * @param entity to set
     */
    public void setEntity(XincoCoreDataType entity) {
        this.entity = entity;
    }
}
