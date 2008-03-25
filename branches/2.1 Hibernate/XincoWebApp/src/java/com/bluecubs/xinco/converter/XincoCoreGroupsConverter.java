/*
 *  XincoCoreGroupsConverter
 *
 * Created on March 25, 2008, 11:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreGroup;
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

@XmlRootElement(name = "xincoCoreGroups")
public class XincoCoreGroupsConverter {
    private Collection<XincoCoreGroup> entities;
    private Collection<com.bluecubs.xinco.converter.XincoCoreGroupRefConverter> references;
    private URI uri;
  
    /** Creates a new instance of XincoCoreGroupsConverter */
    public XincoCoreGroupsConverter() {
    }

    /**
     * Creates a new instance of XincoCoreGroupsConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     */
    public XincoCoreGroupsConverter(Collection<XincoCoreGroup> entities, URI uri) {
        this.entities = entities;
        this.uri = uri;
    }

    /**
     * Returns a collection of XincoCoreGroupRefConverter.
     *
     * @return a collection of XincoCoreGroupRefConverter
     */
    @XmlElement(name = "xincoCoreGroupRef")
    public Collection<com.bluecubs.xinco.converter.XincoCoreGroupRefConverter> getReferences() {
        references = new ArrayList<XincoCoreGroupRefConverter>();
        if (entities != null) {
            for (XincoCoreGroup entity : entities) {
                references.add(new XincoCoreGroupRefConverter(entity, uri, true));
            }
        }
        return references;
    }

    /**
     * Sets a collection of XincoCoreGroupRefConverter.
     *
     * @param a collection of XincoCoreGroupRefConverter to set
     */
    public void setReferences(Collection<com.bluecubs.xinco.converter.XincoCoreGroupRefConverter> references) {
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
     * Returns a collection XincoCoreGroup entities.
     *
     * @return a collection of XincoCoreGroup entities
     */
    @XmlTransient
    public Collection<XincoCoreGroup> getEntities() {
        entities = new ArrayList<XincoCoreGroup>();
        if (references != null) {
            for (XincoCoreGroupRefConverter ref : references) {
                entities.add(ref.getEntity());
            }
        }
        return entities;
    }
}
