/*
 *  XincoCoreGroupRefConverter
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
import javax.ws.rs.core.UriBuilder;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoCoreGroupRef")
public class XincoCoreGroupRefConverter {
    private XincoCoreGroup entity;
    private boolean isUriExtendable;
    private URI uri;
  
    /** Creates a new instance of XincoCoreGroupRefConverter */
    public XincoCoreGroupRefConverter() {
    }

    /**
     * Creates a new instance of XincoCoreGroupRefConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param isUriExtendable indicates whether the uri can be extended
     */
    public XincoCoreGroupRefConverter(XincoCoreGroup entity, URI uri, boolean isUriExtendable) {
        this.entity = entity;
        this.uri = uri;
        this.isUriExtendable = isUriExtendable;
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
     * Returns the URI associated with this reference converter.
     *
     * @return the converted uri
     */
    @XmlAttribute(name = "uri")
    public URI getResourceUri() {
        if (isUriExtendable) {
            return UriBuilder.fromUri(uri).path(entity.getId() + "/").build();
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
     * Returns the XincoCoreGroup entity.
     *
     * @return XincoCoreGroup entity
     */
    @XmlTransient
    public XincoCoreGroup getEntity() {
        XincoCoreGroupConverter result = UriResolver.getInstance().resolve(XincoCoreGroupConverter.class, uri);
        if (result != null) {
            return result.getEntity();
        }
        return null;
    }
}
