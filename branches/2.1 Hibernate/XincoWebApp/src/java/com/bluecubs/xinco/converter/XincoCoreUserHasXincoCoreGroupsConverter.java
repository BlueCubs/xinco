/*
 *  XincoCoreUserHasXincoCoreGroupsConverter
 *
 * Created on March 25, 2008, 11:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreUserHasXincoCoreGroup;
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

@XmlRootElement(name = "xincoCoreUserHasXincoCoreGroups")
public class XincoCoreUserHasXincoCoreGroupsConverter {
    private Collection<XincoCoreUserHasXincoCoreGroup> entities;
    private Collection<com.bluecubs.xinco.converter.XincoCoreUserHasXincoCoreGroupRefConverter> references;
    private URI uri;
  
    /** Creates a new instance of XincoCoreUserHasXincoCoreGroupsConverter */
    public XincoCoreUserHasXincoCoreGroupsConverter() {
    }

    /**
     * Creates a new instance of XincoCoreUserHasXincoCoreGroupsConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     */
    public XincoCoreUserHasXincoCoreGroupsConverter(Collection<XincoCoreUserHasXincoCoreGroup> entities, URI uri) {
        this.entities = entities;
        this.uri = uri;
    }

    /**
     * Returns a collection of XincoCoreUserHasXincoCoreGroupRefConverter.
     *
     * @return a collection of XincoCoreUserHasXincoCoreGroupRefConverter
     */
    @XmlElement(name = "xincoCoreUserHasXincoCoreGroupRef")
    public Collection<com.bluecubs.xinco.converter.XincoCoreUserHasXincoCoreGroupRefConverter> getReferences() {
        references = new ArrayList<XincoCoreUserHasXincoCoreGroupRefConverter>();
        if (entities != null) {
            for (XincoCoreUserHasXincoCoreGroup entity : entities) {
                references.add(new XincoCoreUserHasXincoCoreGroupRefConverter(entity, uri, true));
            }
        }
        return references;
    }

    /**
     * Sets a collection of XincoCoreUserHasXincoCoreGroupRefConverter.
     *
     * @param a collection of XincoCoreUserHasXincoCoreGroupRefConverter to set
     */
    public void setReferences(Collection<com.bluecubs.xinco.converter.XincoCoreUserHasXincoCoreGroupRefConverter> references) {
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
     * Returns a collection XincoCoreUserHasXincoCoreGroup entities.
     *
     * @return a collection of XincoCoreUserHasXincoCoreGroup entities
     */
    @XmlTransient
    public Collection<XincoCoreUserHasXincoCoreGroup> getEntities() {
        entities = new ArrayList<XincoCoreUserHasXincoCoreGroup>();
        if (references != null) {
            for (XincoCoreUserHasXincoCoreGroupRefConverter ref : references) {
                entities.add(ref.getEntity());
            }
        }
        return entities;
    }
}
