/*
 *  XincoCoreUsersConverter
 *
 * Created on March 25, 2008, 11:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreUser;
import java.net.URI;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.ArrayList;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoCoreUsers")
public class XincoCoreUsersConverter {
    private Collection<XincoCoreUser> entities;
    private Collection<com.bluecubs.xinco.converter.XincoCoreUserRefConverter> references;
    private URI uri;
  
    /** Creates a new instance of XincoCoreUsersConverter */
    public XincoCoreUsersConverter() {
    }

    /**
     * Creates a new instance of XincoCoreUsersConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     */
    public XincoCoreUsersConverter(Collection<XincoCoreUser> entities, URI uri) {
        this.entities = entities;
        this.uri = uri;
    }

    /**
     * Returns a collection of XincoCoreUserRefConverter.
     *
     * @return a collection of XincoCoreUserRefConverter
     */
    @XmlElement(name = "xincoCoreUserRef")
    public Collection<com.bluecubs.xinco.converter.XincoCoreUserRefConverter> getReferences() {
        references = new ArrayList<XincoCoreUserRefConverter>();
        if (entities != null) {
            for (XincoCoreUser entity : entities) {
                references.add(new XincoCoreUserRefConverter(entity, uri, true));
            }
        }
        return references;
    }

    /**
     * Sets a collection of XincoCoreUserRefConverter.
     *
     * @param a collection of XincoCoreUserRefConverter to set
     */
    public void setReferences(Collection<com.bluecubs.xinco.converter.XincoCoreUserRefConverter> references) {
        this.references = references;
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
     * Returns a collection XincoCoreUser entities.
     *
     * @return a collection of XincoCoreUser entities
     */
    @XmlTransient
    public Collection<XincoCoreUser> getEntities() {
        entities = new ArrayList<XincoCoreUser>();
        if (references != null) {
            for (XincoCoreUserRefConverter ref : references) {
                entities.add(ref.getEntity());
            }
        }
        return entities;
    }
}
