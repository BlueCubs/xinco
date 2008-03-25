/*
 *  XincoCoreDataConverter
 *
 * Created on March 25, 2008, 11:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreData;
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

@XmlRootElement(name = "xincoCoreData")
public class XincoCoreDataConverter {
    private Collection<XincoCoreData> entities;
    private Collection<com.bluecubs.xinco.converter.XincoCoreDatumRefConverter> references;
    private URI uri;
  
    /** Creates a new instance of XincoCoreDataConverter */
    public XincoCoreDataConverter() {
    }

    /**
     * Creates a new instance of XincoCoreDataConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     */
    public XincoCoreDataConverter(Collection<XincoCoreData> entities, URI uri) {
        this.entities = entities;
        this.uri = uri;
    }

    /**
     * Returns a collection of XincoCoreDatumRefConverter.
     *
     * @return a collection of XincoCoreDatumRefConverter
     */
    @XmlElement(name = "xincoCoreDatumRef")
    public Collection<com.bluecubs.xinco.converter.XincoCoreDatumRefConverter> getReferences() {
        references = new ArrayList<XincoCoreDatumRefConverter>();
        if (entities != null) {
            for (XincoCoreData entity : entities) {
                references.add(new XincoCoreDatumRefConverter(entity, uri, true));
            }
        }
        return references;
    }

    /**
     * Sets a collection of XincoCoreDatumRefConverter.
     *
     * @param a collection of XincoCoreDatumRefConverter to set
     */
    public void setReferences(Collection<com.bluecubs.xinco.converter.XincoCoreDatumRefConverter> references) {
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
     * Returns a collection XincoCoreData entities.
     *
     * @return a collection of XincoCoreData entities
     */
    @XmlTransient
    public Collection<XincoCoreData> getEntities() {
        entities = new ArrayList<XincoCoreData>();
        if (references != null) {
            for (XincoCoreDatumRefConverter ref : references) {
                entities.add(ref.getEntity());
            }
        }
        return entities;
    }
}
