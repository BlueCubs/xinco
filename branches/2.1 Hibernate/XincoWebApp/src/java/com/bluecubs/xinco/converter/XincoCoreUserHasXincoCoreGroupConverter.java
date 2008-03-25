/*
 *  XincoCoreUserHasXincoCoreGroupConverter
 *
 * Created on March 25, 2008, 11:37 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.persistence.XincoCoreUserHasXincoCoreGroupPK;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoCoreUserHasXincoCoreGroup")
public class XincoCoreUserHasXincoCoreGroupConverter {
    private XincoCoreUserHasXincoCoreGroup entity;
    private URI uri;
  
    /** Creates a new instance of XincoCoreUserHasXincoCoreGroupConverter */
    public XincoCoreUserHasXincoCoreGroupConverter() {
        entity = new XincoCoreUserHasXincoCoreGroup();
    }

    /**
     * Creates a new instance of XincoCoreUserHasXincoCoreGroupConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     */
    public XincoCoreUserHasXincoCoreGroupConverter(XincoCoreUserHasXincoCoreGroup entity, URI uri) {
        this.entity = entity;
        this.uri = uri;
    }

    /**
     * Getter for xincoCoreUserHasXincoCoreGroupPK.
     *
     * @return value for xincoCoreUserHasXincoCoreGroupPK
     */
    @XmlElement
    public XincoCoreUserHasXincoCoreGroupPK getXincoCoreUserHasXincoCoreGroupPK() {
        return entity.getXincoCoreUserHasXincoCoreGroupPK();
    }

    /**
     * Setter for xincoCoreUserHasXincoCoreGroupPK.
     *
     * @param value the value to set
     */
    public void setXincoCoreUserHasXincoCoreGroupPK(XincoCoreUserHasXincoCoreGroupPK value) {
        entity.setXincoCoreUserHasXincoCoreGroupPK(value);
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
     * Returns the XincoCoreUserHasXincoCoreGroup entity.
     *
     * @return an entity
     */
    @XmlTransient
    public XincoCoreUserHasXincoCoreGroup getEntity() {
        return entity;
    }

    /**
     * Sets the XincoCoreUserHasXincoCoreGroup entity.
     *
     * @param entity to set
     */
    public void setEntity(XincoCoreUserHasXincoCoreGroup entity) {
        this.entity = entity;
    }
}
