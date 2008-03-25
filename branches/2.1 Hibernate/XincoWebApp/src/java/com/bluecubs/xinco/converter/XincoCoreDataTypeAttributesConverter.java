/*
 *  XincoCoreDataTypeAttributesConverter
 *
 * Created on March 25, 2008, 11:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreDataTypeAttribute;
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

@XmlRootElement(name = "xincoCoreDataTypeAttributes")
public class XincoCoreDataTypeAttributesConverter {
    private Collection<XincoCoreDataTypeAttribute> entities;
    private Collection<com.bluecubs.xinco.converter.XincoCoreDataTypeAttributeRefConverter> references;
    private URI uri;
  
    /** Creates a new instance of XincoCoreDataTypeAttributesConverter */
    public XincoCoreDataTypeAttributesConverter() {
    }

    /**
     * Creates a new instance of XincoCoreDataTypeAttributesConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     */
    public XincoCoreDataTypeAttributesConverter(Collection<XincoCoreDataTypeAttribute> entities, URI uri) {
        this.entities = entities;
        this.uri = uri;
    }

    /**
     * Returns a collection of XincoCoreDataTypeAttributeRefConverter.
     *
     * @return a collection of XincoCoreDataTypeAttributeRefConverter
     */
    @XmlElement(name = "xincoCoreDataTypeAttributeRef")
    public Collection<com.bluecubs.xinco.converter.XincoCoreDataTypeAttributeRefConverter> getReferences() {
        references = new ArrayList<XincoCoreDataTypeAttributeRefConverter>();
        if (entities != null) {
            for (XincoCoreDataTypeAttribute entity : entities) {
                references.add(new XincoCoreDataTypeAttributeRefConverter(entity, uri, true));
            }
        }
        return references;
    }

    /**
     * Sets a collection of XincoCoreDataTypeAttributeRefConverter.
     *
     * @param a collection of XincoCoreDataTypeAttributeRefConverter to set
     */
    public void setReferences(Collection<com.bluecubs.xinco.converter.XincoCoreDataTypeAttributeRefConverter> references) {
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
     * Returns a collection XincoCoreDataTypeAttribute entities.
     *
     * @return a collection of XincoCoreDataTypeAttribute entities
     */
    @XmlTransient
    public Collection<XincoCoreDataTypeAttribute> getEntities() {
        entities = new ArrayList<XincoCoreDataTypeAttribute>();
        if (references != null) {
            for (XincoCoreDataTypeAttributeRefConverter ref : references) {
                entities.add(ref.getEntity());
            }
        }
        return entities;
    }
}
