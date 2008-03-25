/*
 *  XincoCoreDataTypeAttributeRefConverter
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
import javax.ws.rs.core.UriBuilder;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoCoreDataTypeAttributeRef")
public class XincoCoreDataTypeAttributeRefConverter {
    private XincoCoreDataTypeAttribute entity;
    private boolean isUriExtendable;
    private URI uri;
  
    /** Creates a new instance of XincoCoreDataTypeAttributeRefConverter */
    public XincoCoreDataTypeAttributeRefConverter() {
    }

    /**
     * Creates a new instance of XincoCoreDataTypeAttributeRefConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param isUriExtendable indicates whether the uri can be extended
     */
    public XincoCoreDataTypeAttributeRefConverter(XincoCoreDataTypeAttribute entity, URI uri, boolean isUriExtendable) {
        this.entity = entity;
        this.uri = uri;
        this.isUriExtendable = isUriExtendable;
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
     * Returns the URI associated with this reference converter.
     *
     * @return the converted uri
     */
    @XmlAttribute(name = "uri")
    public URI getResourceUri() {
        if (isUriExtendable) {
            return UriBuilder.fromUri(uri).path(entity.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId() + "," + entity.getXincoCoreDataTypeAttributePK().getAttributeId() + "/").build();
        }
        return uri;
    }

    /**
     * Sets the URI for this reference converter.
     *
     */
    public void setResourceUri(URI uri) {
        this.uri = uri;
    }

    /**
     * Returns the XincoCoreDataTypeAttribute entity.
     *
     * @return XincoCoreDataTypeAttribute entity
     */
    @XmlTransient
    public XincoCoreDataTypeAttribute getEntity() {
        XincoCoreDataTypeAttributeConverter result = UriResolver.getInstance().resolve(XincoCoreDataTypeAttributeConverter.class, uri);
        if (result != null) {
            return result.getEntity();
        }
        return null;
    }
}
