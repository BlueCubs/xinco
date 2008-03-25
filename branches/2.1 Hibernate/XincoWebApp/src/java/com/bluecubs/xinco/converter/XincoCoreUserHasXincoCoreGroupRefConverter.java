/*
 *  XincoCoreUserHasXincoCoreGroupRefConverter
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
import javax.ws.rs.core.UriBuilder;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoCoreUserHasXincoCoreGroupRef")
public class XincoCoreUserHasXincoCoreGroupRefConverter {
    private XincoCoreUserHasXincoCoreGroup entity;
    private boolean isUriExtendable;
    private URI uri;
  
    /** Creates a new instance of XincoCoreUserHasXincoCoreGroupRefConverter */
    public XincoCoreUserHasXincoCoreGroupRefConverter() {
    }

    /**
     * Creates a new instance of XincoCoreUserHasXincoCoreGroupRefConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param isUriExtendable indicates whether the uri can be extended
     */
    public XincoCoreUserHasXincoCoreGroupRefConverter(XincoCoreUserHasXincoCoreGroup entity, URI uri, boolean isUriExtendable) {
        this.entity = entity;
        this.uri = uri;
        this.isUriExtendable = isUriExtendable;
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
     * Returns the URI associated with this reference converter.
     *
     * @return the converted uri
     */
    @XmlAttribute(name = "uri")
    public URI getResourceUri() {
        if (isUriExtendable) {
            return UriBuilder.fromUri(uri).path(entity.getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreUserId() + "," + entity.getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId() + "/").build();
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
     * Returns the XincoCoreUserHasXincoCoreGroup entity.
     *
     * @return XincoCoreUserHasXincoCoreGroup entity
     */
    @XmlTransient
    public XincoCoreUserHasXincoCoreGroup getEntity() {
        XincoCoreUserHasXincoCoreGroupConverter result = UriResolver.getInstance().resolve(XincoCoreUserHasXincoCoreGroupConverter.class, uri);
        if (result != null) {
            return result.getEntity();
        }
        return null;
    }
}
