/*
 *  XincoCoreLanguageConverter
 *
 * Created on March 25, 2008, 11:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreLanguage;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoCoreLanguage")
public class XincoCoreLanguageConverter {
    private XincoCoreLanguage entity;
    private URI uri;
  
    /** Creates a new instance of XincoCoreLanguageConverter */
    public XincoCoreLanguageConverter() {
        entity = new XincoCoreLanguage();
    }

    /**
     * Creates a new instance of XincoCoreLanguageConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     */
    public XincoCoreLanguageConverter(XincoCoreLanguage entity, URI uri) {
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
     * Getter for sign.
     *
     * @return value for sign
     */
    @XmlElement
    public String getSign() {
        return entity.getSign();
    }

    /**
     * Setter for sign.
     *
     * @param value the value to set
     */
    public void setSign(String value) {
        entity.setSign(value);
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
     * Returns the URI associated with this converter.
     *
     * @return the uri
     */
    @XmlAttribute(name = "uri")
    public URI getResourceUri() {
        return uri;
    }

    /**
     * Returns the XincoCoreLanguage entity.
     *
     * @return an entity
     */
    @XmlTransient
    public XincoCoreLanguage getEntity() {
        return entity;
    }

    /**
     * Sets the XincoCoreLanguage entity.
     *
     * @param entity to set
     */
    public void setEntity(XincoCoreLanguage entity) {
        this.entity = entity;
    }
}
