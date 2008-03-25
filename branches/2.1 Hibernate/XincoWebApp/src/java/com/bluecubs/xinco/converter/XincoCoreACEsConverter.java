/*
 *  XincoCoreACEsConverter
 *
 * Created on March 25, 2008, 11:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreACE;
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

@XmlRootElement(name = "xincoCoreACEs")
public class XincoCoreACEsConverter {
    private Collection<XincoCoreACE> entities;
    private Collection<com.bluecubs.xinco.converter.XincoCoreACERefConverter> references;
    private URI uri;
  
    /** Creates a new instance of XincoCoreACEsConverter */
    public XincoCoreACEsConverter() {
    }

    /**
     * Creates a new instance of XincoCoreACEsConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     */
    public XincoCoreACEsConverter(Collection<XincoCoreACE> entities, URI uri) {
        this.entities = entities;
        this.uri = uri;
    }

    /**
     * Returns a collection of XincoCoreACERefConverter.
     *
     * @return a collection of XincoCoreACERefConverter
     */
    @XmlElement(name = "xincoCoreACERef")
    public Collection<com.bluecubs.xinco.converter.XincoCoreACERefConverter> getReferences() {
        references = new ArrayList<XincoCoreACERefConverter>();
        if (entities != null) {
            for (XincoCoreACE entity : entities) {
                references.add(new XincoCoreACERefConverter(entity, uri, true));
            }
        }
        return references;
    }

    /**
     * Sets a collection of XincoCoreACERefConverter.
     *
     * @param a collection of XincoCoreACERefConverter to set
     */
    public void setReferences(Collection<com.bluecubs.xinco.converter.XincoCoreACERefConverter> references) {
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
     * Returns a collection XincoCoreACE entities.
     *
     * @return a collection of XincoCoreACE entities
     */
    @XmlTransient
    public Collection<XincoCoreACE> getEntities() {
        entities = new ArrayList<XincoCoreACE>();
        if (references != null) {
            for (XincoCoreACERefConverter ref : references) {
                entities.add(ref.getEntity());
            }
        }
        return entities;
    }
}
