/*
 *  XincoCoreDataTypeAttributeConverter
 *
 * Created on March 25, 2008, 11:39 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.persistence.XincoCoreDataTypeAttributePK;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoCoreDataTypeAttribute")
public class XincoCoreDataTypeAttributeConverter {
    private XincoCoreDataTypeAttribute entity;
    private URI uri;
  
    /** Creates a new instance of XincoCoreDataTypeAttributeConverter */
    public XincoCoreDataTypeAttributeConverter() {
        entity = new XincoCoreDataTypeAttribute();
    }

    /**
     * Creates a new instance of XincoCoreDataTypeAttributeConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     */
    public XincoCoreDataTypeAttributeConverter(XincoCoreDataTypeAttribute entity, URI uri) {
        this.entity = entity;
        this.uri = uri;
    }

    /**
     * Getter for xincoCoreDataTypeAttributePK.
     *
     * @return value for xincoCoreDataTypeAttributePK
     */
    @XmlElement
    public XincoCoreDataTypeAttributePK getXincoCoreDataTypeAttributePK() {
        return entity.getXincoCoreDataTypeAttributePK();
    }

    /**
     * Setter for xincoCoreDataTypeAttributePK.
     *
     * @param value the value to set
     */
    public void setXincoCoreDataTypeAttributePK(XincoCoreDataTypeAttributePK value) {
        entity.setXincoCoreDataTypeAttributePK(value);
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
     * Getter for dataType.
     *
     * @return value for dataType
     */
    @XmlElement
    public String getDataType() {
        return entity.getDataType();
    }

    /**
     * Setter for dataType.
     *
     * @param value the value to set
     */
    public void setDataType(String value) {
        entity.setDataType(value);
    }

    /**
     * Getter for size.
     *
     * @return value for size
     */
    @XmlElement
    public int getSize() {
        return entity.getSize();
    }

    /**
     * Setter for size.
     *
     * @param value the value to set
     */
    public void setSize(int value) {
        entity.setSize(value);
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
     * Returns the XincoCoreDataTypeAttribute entity.
     *
     * @return an entity
     */
    @XmlTransient
    public XincoCoreDataTypeAttribute getEntity() {
        return entity;
    }

    /**
     * Sets the XincoCoreDataTypeAttribute entity.
     *
     * @param entity to set
     */
    public void setEntity(XincoCoreDataTypeAttribute entity) {
        this.entity = entity;
    }
}
