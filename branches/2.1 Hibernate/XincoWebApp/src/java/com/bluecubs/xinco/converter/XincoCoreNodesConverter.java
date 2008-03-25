/*
 *  XincoCoreNodesConverter
 *
 * Created on March 25, 2008, 11:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreNode;
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

@XmlRootElement(name = "xincoCoreNodes")
public class XincoCoreNodesConverter {
    private Collection<XincoCoreNode> entities;
    private Collection<com.bluecubs.xinco.converter.XincoCoreNodeRefConverter> references;
    private URI uri;
  
    /** Creates a new instance of XincoCoreNodesConverter */
    public XincoCoreNodesConverter() {
    }

    /**
     * Creates a new instance of XincoCoreNodesConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     */
    public XincoCoreNodesConverter(Collection<XincoCoreNode> entities, URI uri) {
        this.entities = entities;
        this.uri = uri;
    }

    /**
     * Returns a collection of XincoCoreNodeRefConverter.
     *
     * @return a collection of XincoCoreNodeRefConverter
     */
    @XmlElement(name = "xincoCoreNodeRef")
    public Collection<com.bluecubs.xinco.converter.XincoCoreNodeRefConverter> getReferences() {
        references = new ArrayList<XincoCoreNodeRefConverter>();
        if (entities != null) {
            for (XincoCoreNode entity : entities) {
                references.add(new XincoCoreNodeRefConverter(entity, uri, true));
            }
        }
        return references;
    }

    /**
     * Sets a collection of XincoCoreNodeRefConverter.
     *
     * @param a collection of XincoCoreNodeRefConverter to set
     */
    public void setReferences(Collection<com.bluecubs.xinco.converter.XincoCoreNodeRefConverter> references) {
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
     * Returns a collection XincoCoreNode entities.
     *
     * @return a collection of XincoCoreNode entities
     */
    @XmlTransient
    public Collection<XincoCoreNode> getEntities() {
        entities = new ArrayList<XincoCoreNode>();
        if (references != null) {
            for (XincoCoreNodeRefConverter ref : references) {
                entities.add(ref.getEntity());
            }
        }
        return entities;
    }
}
