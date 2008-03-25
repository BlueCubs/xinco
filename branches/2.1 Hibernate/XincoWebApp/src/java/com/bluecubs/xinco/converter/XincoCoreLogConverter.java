/*
 *  XincoCoreLogConverter
 *
 * Created on March 25, 2008, 11:37 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreLog;
import java.net.URI;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoCoreLog")
public class XincoCoreLogConverter {
    private XincoCoreLog entity;
    private URI uri;
  
    /** Creates a new instance of XincoCoreLogConverter */
    public XincoCoreLogConverter() {
        entity = new XincoCoreLog();
    }

    /**
     * Creates a new instance of XincoCoreLogConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     */
    public XincoCoreLogConverter(XincoCoreLog entity, URI uri) {
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
     * Getter for xincoCoreDataId.
     *
     * @return value for xincoCoreDataId
     */
    @XmlElement
    public int getXincoCoreDataId() {
        return entity.getXincoCoreDataId();
    }

    /**
     * Setter for xincoCoreDataId.
     *
     * @param value the value to set
     */
    public void setXincoCoreDataId(int value) {
        entity.setXincoCoreDataId(value);
    }

    /**
     * Getter for xincoCoreUserId.
     *
     * @return value for xincoCoreUserId
     */
    @XmlElement
    public int getXincoCoreUserId() {
        return entity.getXincoCoreUserId();
    }

    /**
     * Setter for xincoCoreUserId.
     *
     * @param value the value to set
     */
    public void setXincoCoreUserId(int value) {
        entity.setXincoCoreUserId(value);
    }

    /**
     * Getter for opCode.
     *
     * @return value for opCode
     */
    @XmlElement
    public int getOpCode() {
        return entity.getOpCode();
    }

    /**
     * Setter for opCode.
     *
     * @param value the value to set
     */
    public void setOpCode(int value) {
        entity.setOpCode(value);
    }

    /**
     * Getter for opDatetime.
     *
     * @return value for opDatetime
     */
    @XmlElement
    public Date getOpDatetime() {
        return entity.getOpDatetime();
    }

    /**
     * Setter for opDatetime.
     *
     * @param value the value to set
     */
    public void setOpDatetime(Date value) {
        entity.setOpDatetime(value);
    }

    /**
     * Getter for opDescription.
     *
     * @return value for opDescription
     */
    @XmlElement
    public String getOpDescription() {
        return entity.getOpDescription();
    }

    /**
     * Setter for opDescription.
     *
     * @param value the value to set
     */
    public void setOpDescription(String value) {
        entity.setOpDescription(value);
    }

    /**
     * Getter for versionHigh.
     *
     * @return value for versionHigh
     */
    @XmlElement
    public Integer getVersionHigh() {
        return entity.getVersionHigh();
    }

    /**
     * Setter for versionHigh.
     *
     * @param value the value to set
     */
    public void setVersionHigh(Integer value) {
        entity.setVersionHigh(value);
    }

    /**
     * Getter for versionMid.
     *
     * @return value for versionMid
     */
    @XmlElement
    public Integer getVersionMid() {
        return entity.getVersionMid();
    }

    /**
     * Setter for versionMid.
     *
     * @param value the value to set
     */
    public void setVersionMid(Integer value) {
        entity.setVersionMid(value);
    }

    /**
     * Getter for versionLow.
     *
     * @return value for versionLow
     */
    @XmlElement
    public Integer getVersionLow() {
        return entity.getVersionLow();
    }

    /**
     * Setter for versionLow.
     *
     * @param value the value to set
     */
    public void setVersionLow(Integer value) {
        entity.setVersionLow(value);
    }

    /**
     * Getter for versionPostfix.
     *
     * @return value for versionPostfix
     */
    @XmlElement
    public String getVersionPostfix() {
        return entity.getVersionPostfix();
    }

    /**
     * Setter for versionPostfix.
     *
     * @param value the value to set
     */
    public void setVersionPostfix(String value) {
        entity.setVersionPostfix(value);
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
     * Returns the XincoCoreLog entity.
     *
     * @return an entity
     */
    @XmlTransient
    public XincoCoreLog getEntity() {
        return entity;
    }

    /**
     * Sets the XincoCoreLog entity.
     *
     * @param entity to set
     */
    public void setEntity(XincoCoreLog entity) {
        this.entity = entity;
    }
}
