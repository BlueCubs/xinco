/*
 *  XincoSettingRefConverter
 *
 * Created on March 25, 2008, 11:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoSetting;
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

@XmlRootElement(name = "xincoSettingRef")
public class XincoSettingRefConverter {
    private XincoSetting entity;
    private boolean isUriExtendable;
    private URI uri;
  
    /** Creates a new instance of XincoSettingRefConverter */
    public XincoSettingRefConverter() {
    }

    /**
     * Creates a new instance of XincoSettingRefConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param isUriExtendable indicates whether the uri can be extended
     */
    public XincoSettingRefConverter(XincoSetting entity, URI uri, boolean isUriExtendable) {
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
     * Returns the XincoSetting entity.
     *
     * @return XincoSetting entity
     */
    @XmlTransient
    public XincoSetting getEntity() {
        XincoSettingConverter result = UriResolver.getInstance().resolve(XincoSettingConverter.class, uri);
        if (result != null) {
            return result.getEntity();
        }
        return null;
    }
}
