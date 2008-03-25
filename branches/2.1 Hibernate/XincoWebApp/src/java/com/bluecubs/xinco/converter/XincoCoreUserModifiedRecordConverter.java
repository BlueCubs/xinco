/*
 *  XincoCoreUserModifiedRecordConverter
 *
 * Created on March 25, 2008, 11:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreUserModifiedRecord;
import com.bluecubs.xinco.persistence.XincoCoreUserModifiedRecordPK;
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

@XmlRootElement(name = "xincoCoreUserModifiedRecord")
public class XincoCoreUserModifiedRecordConverter {
    private XincoCoreUserModifiedRecord entity;
    private URI uri;
  
    /** Creates a new instance of XincoCoreUserModifiedRecordConverter */
    public XincoCoreUserModifiedRecordConverter() {
        entity = new XincoCoreUserModifiedRecord();
    }

    /**
     * Creates a new instance of XincoCoreUserModifiedRecordConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     */
    public XincoCoreUserModifiedRecordConverter(XincoCoreUserModifiedRecord entity, URI uri) {
        this.entity = entity;
        this.uri = uri;
    }

    /**
     * Getter for xincoCoreUserModifiedRecordPK.
     *
     * @return value for xincoCoreUserModifiedRecordPK
     */
    @XmlElement
    public XincoCoreUserModifiedRecordPK getXincoCoreUserModifiedRecordPK() {
        return entity.getXincoCoreUserModifiedRecordPK();
    }

    /**
     * Setter for xincoCoreUserModifiedRecordPK.
     *
     * @param value the value to set
     */
    public void setXincoCoreUserModifiedRecordPK(XincoCoreUserModifiedRecordPK value) {
        entity.setXincoCoreUserModifiedRecordPK(value);
    }

    /**
     * Getter for modReason.
     *
     * @return value for modReason
     */
    @XmlElement
    public String getModReason() {
        return entity.getModReason();
    }

    /**
     * Setter for modReason.
     *
     * @param value the value to set
     */
    public void setModReason(String value) {
        entity.setModReason(value);
    }

    /**
     * Getter for modTime.
     *
     * @return value for modTime
     */
    @XmlElement
    public Date getModTime() {
        return entity.getModTime();
    }

    /**
     * Setter for modTime.
     *
     * @param value the value to set
     */
    public void setModTime(Date value) {
        entity.setModTime(value);
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
     * Returns the XincoCoreUserModifiedRecord entity.
     *
     * @return an entity
     */
    @XmlTransient
    public XincoCoreUserModifiedRecord getEntity() {
        return entity;
    }

    /**
     * Sets the XincoCoreUserModifiedRecord entity.
     *
     * @param entity to set
     */
    public void setEntity(XincoCoreUserModifiedRecord entity) {
        this.entity = entity;
    }
}
