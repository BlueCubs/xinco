/*
 *  XincoCoreGroupConverter
 *
 * Created on March 25, 2008, 11:40 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreGroup;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoCoreGroup")
public class XincoCoreGroupConverter {
    private XincoCoreGroup entity;
    private URI uri;
  
    /** Creates a new instance of XincoCoreGroupConverter */
    public XincoCoreGroupConverter() {
        entity = new XincoCoreGroup();
    }

    /**
     * Creates a new instance of XincoCoreGroupConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     */
    public XincoCoreGroupConverter(XincoCoreGroup entity, URI uri) {
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
     * Returns the XincoCoreGroup entity.
     *
     * @return an entity
     */
    @XmlTransient
    public XincoCoreGroup getEntity() {
        return entity;
    }

    /**
     * Sets the XincoCoreGroup entity.
     *
     * @param entity to set
     */
    public void setEntity(XincoCoreGroup entity) {
        this.entity = entity;
    }
}
