/*
 *  XincoCoreDatumRefConverter
 *
 * Created on March 25, 2008, 11:36 AM
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
import javax.ws.rs.core.UriBuilder;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoCoreDatumRef")
public class XincoCoreDatumRefConverter {
    private XincoCoreData entity;
    private boolean isUriExtendable;
    private URI uri;
  
    /** Creates a new instance of XincoCoreDatumRefConverter */
    public XincoCoreDatumRefConverter() {
    }

    /**
     * Creates a new instance of XincoCoreDatumRefConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param isUriExtendable indicates whether the uri can be extended
     */
    public XincoCoreDatumRefConverter(XincoCoreData entity, URI uri, boolean isUriExtendable) {
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
     * Returns the XincoCoreData entity.
     *
     * @return XincoCoreData entity
     */
    @XmlTransient
    public XincoCoreData getEntity() {
        XincoCoreDatumConverter result = UriResolver.getInstance().resolve(XincoCoreDatumConverter.class, uri);
        if (result != null) {
            return result.getEntity();
        }
        return null;
    }
}
