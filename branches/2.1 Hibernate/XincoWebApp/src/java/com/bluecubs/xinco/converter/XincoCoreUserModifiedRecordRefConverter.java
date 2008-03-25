/*
 *  XincoCoreUserModifiedRecordRefConverter
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoCoreUserModifiedRecordRef")
public class XincoCoreUserModifiedRecordRefConverter {
    private XincoCoreUserModifiedRecord entity;
    private boolean isUriExtendable;
    private URI uri;
  
    /** Creates a new instance of XincoCoreUserModifiedRecordRefConverter */
    public XincoCoreUserModifiedRecordRefConverter() {
    }

    /**
     * Creates a new instance of XincoCoreUserModifiedRecordRefConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param isUriExtendable indicates whether the uri can be extended
     */
    public XincoCoreUserModifiedRecordRefConverter(XincoCoreUserModifiedRecord entity, URI uri, boolean isUriExtendable) {
        this.entity = entity;
        this.uri = uri;
        this.isUriExtendable = isUriExtendable;
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
     * Returns the URI associated with this reference converter.
     *
     * @return the converted uri
     */
    @XmlAttribute(name = "uri")
    public URI getResourceUri() {
        if (isUriExtendable) {
            return UriBuilder.fromUri(uri).path(entity.getXincoCoreUserModifiedRecordPK().getId() + "," + entity.getXincoCoreUserModifiedRecordPK().getRecordId() + "/").build();
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
     * Returns the XincoCoreUserModifiedRecord entity.
     *
     * @return XincoCoreUserModifiedRecord entity
     */
    @XmlTransient
    public XincoCoreUserModifiedRecord getEntity() {
        XincoCoreUserModifiedRecordConverter result = UriResolver.getInstance().resolve(XincoCoreUserModifiedRecordConverter.class, uri);
        if (result != null) {
            return result.getEntity();
        }
        return null;
    }
}
