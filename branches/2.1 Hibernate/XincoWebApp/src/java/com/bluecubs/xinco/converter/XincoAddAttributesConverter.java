/*
 *  XincoAddAttributesConverter
 *
 * Created on March 25, 2008, 11:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoAddAttribute;
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

@XmlRootElement(name = "xincoAddAttributes")
public class XincoAddAttributesConverter {
    private Collection<XincoAddAttribute> entities;
    private Collection<com.bluecubs.xinco.converter.XincoAddAttributeRefConverter> references;
    private URI uri;
  
    /** Creates a new instance of XincoAddAttributesConverter */
    public XincoAddAttributesConverter() {
    }

    /**
     * Creates a new instance of XincoAddAttributesConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     */
    public XincoAddAttributesConverter(Collection<XincoAddAttribute> entities, URI uri) {
        this.entities = entities;
        this.uri = uri;
    }

    /**
     * Returns a collection of XincoAddAttributeRefConverter.
     *
     * @return a collection of XincoAddAttributeRefConverter
     */
    @XmlElement(name = "xincoAddAttributeRef")
    public Collection<com.bluecubs.xinco.converter.XincoAddAttributeRefConverter> getReferences() {
        references = new ArrayList<XincoAddAttributeRefConverter>();
        if (entities != null) {
            for (XincoAddAttribute entity : entities) {
                references.add(new XincoAddAttributeRefConverter(entity, uri, true));
            }
        }
        return references;
    }

    /**
     * Sets a collection of XincoAddAttributeRefConverter.
     *
     * @param a collection of XincoAddAttributeRefConverter to set
     */
    public void setReferences(Collection<com.bluecubs.xinco.converter.XincoAddAttributeRefConverter> references) {
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
     * Returns a collection XincoAddAttribute entities.
     *
     * @return a collection of XincoAddAttribute entities
     */
    @XmlTransient
    public Collection<XincoAddAttribute> getEntities() {
        entities = new ArrayList<XincoAddAttribute>();
        if (references != null) {
            for (XincoAddAttributeRefConverter ref : references) {
                entities.add(ref.getEntity());
            }
        }
        return entities;
    }
}
