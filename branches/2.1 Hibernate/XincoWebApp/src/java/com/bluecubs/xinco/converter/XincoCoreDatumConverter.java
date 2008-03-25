/*
 *  XincoCoreDatumConverter
 *
 * Created on March 25, 2008, 11:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreData;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoCoreDatum")
public class XincoCoreDatumConverter {
    private XincoCoreData entity;
    private URI uri;
  
    /** Creates a new instance of XincoCoreDatumConverter */
    public XincoCoreDatumConverter() {
        entity = new XincoCoreData();
    }

    /**
     * Creates a new instance of XincoCoreDatumConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     */
    public XincoCoreDatumConverter(XincoCoreData entity, URI uri) {
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
    public int getXincoCoreNodeId() {
        return entity.getXincoCoreNodeId();
    }

    /**
     * Setter for xincoCoreNodeId.
     *
     * @param value the value to set
     */
    public void setXincoCoreNodeId(int value) {
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
     * Getter for xincoCoreDataTypeId.
     *
     * @return value for xincoCoreDataTypeId
     */
    @XmlElement
    public int getXincoCoreDataTypeId() {
        return entity.getXincoCoreDataTypeId();
    }

    /**
     * Setter for xincoCoreDataTypeId.
     *
     * @param value the value to set
     */
    public void setXincoCoreDataTypeId(int value) {
        entity.setXincoCoreDataTypeId(value);
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
     * Returns the XincoCoreData entity.
     *
     * @return an entity
     */
    @XmlTransient
    public XincoCoreData getEntity() {
        return entity;
    }

    /**
     * Sets the XincoCoreData entity.
     *
     * @param entity to set
     */
    public void setEntity(XincoCoreData entity) {
        this.entity = entity;
    }
}
