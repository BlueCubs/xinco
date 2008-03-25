/*
 *  XincoAddAttributeRefConverter
 *
 * Created on March 25, 2008, 11:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoAddAttribute;
import com.bluecubs.xinco.persistence.XincoAddAttributePK;
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

@XmlRootElement(name = "xincoAddAttributeRef")
public class XincoAddAttributeRefConverter {
    private XincoAddAttribute entity;
    private boolean isUriExtendable;
    private URI uri;
  
    /** Creates a new instance of XincoAddAttributeRefConverter */
    public XincoAddAttributeRefConverter() {
    }

    /**
     * Creates a new instance of XincoAddAttributeRefConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param isUriExtendable indicates whether the uri can be extended
     */
    public XincoAddAttributeRefConverter(XincoAddAttribute entity, URI uri, boolean isUriExtendable) {
        this.entity = entity;
        this.uri = uri;
        this.isUriExtendable = isUriExtendable;
    }

    /**
     * Getter for xincoAddAttributePK.
     *
     * @return value for xincoAddAttributePK
     */
    @XmlElement
    public XincoAddAttributePK getXincoAddAttributePK() {
        return entity.getXincoAddAttributePK();
    }

    /**
     * Returns the URI associated with this reference converter.
     *
     * @return the converted uri
     */
    @XmlAttribute(name = "uri")
    public URI getResourceUri() {
        if (isUriExtendable) {
            return UriBuilder.fromUri(uri).path(entity.getXincoAddAttributePK().getXincoCoreDataId() + "," + entity.getXincoAddAttributePK().getAttributeId() + "/").build();
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
     * Returns the XincoAddAttribute entity.
     *
     * @return XincoAddAttribute entity
     */
    @XmlTransient
    public XincoAddAttribute getEntity() {
        XincoAddAttributeConverter result = UriResolver.getInstance().resolve(XincoAddAttributeConverter.class, uri);
        if (result != null) {
            return result.getEntity();
        }
        return null;
    }
}
