/*
 *  XincoCoreDataTypesConverter
 *
 * Created on March 25, 2008, 11:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreDataType;
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

@XmlRootElement(name = "xincoCoreDataTypes")
public class XincoCoreDataTypesConverter {
    private Collection<XincoCoreDataType> entities;
    private Collection<com.bluecubs.xinco.converter.XincoCoreDataTypeRefConverter> references;
    private URI uri;
  
    /** Creates a new instance of XincoCoreDataTypesConverter */
    public XincoCoreDataTypesConverter() {
    }

    /**
     * Creates a new instance of XincoCoreDataTypesConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     */
    public XincoCoreDataTypesConverter(Collection<XincoCoreDataType> entities, URI uri) {
        this.entities = entities;
        this.uri = uri;
    }

    /**
     * Returns a collection of XincoCoreDataTypeRefConverter.
     *
     * @return a collection of XincoCoreDataTypeRefConverter
     */
    @XmlElement(name = "xincoCoreDataTypeRef")
    public Collection<com.bluecubs.xinco.converter.XincoCoreDataTypeRefConverter> getReferences() {
        references = new ArrayList<XincoCoreDataTypeRefConverter>();
        if (entities != null) {
            for (XincoCoreDataType entity : entities) {
                references.add(new XincoCoreDataTypeRefConverter(entity, uri, true));
            }
        }
        return references;
    }

    /**
     * Sets a collection of XincoCoreDataTypeRefConverter.
     *
     * @param a collection of XincoCoreDataTypeRefConverter to set
     */
    public void setReferences(Collection<com.bluecubs.xinco.converter.XincoCoreDataTypeRefConverter> references) {
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
     * Returns a collection XincoCoreDataType entities.
     *
     * @return a collection of XincoCoreDataType entities
     */
    @XmlTransient
    public Collection<XincoCoreDataType> getEntities() {
        entities = new ArrayList<XincoCoreDataType>();
        if (references != null) {
            for (XincoCoreDataTypeRefConverter ref : references) {
                entities.add(ref.getEntity());
            }
        }
        return entities;
    }
}
