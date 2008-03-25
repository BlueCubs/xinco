/*
 *  XincoCoreUserModifiedRecordsConverter
 *
 * Created on March 25, 2008, 11:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreUserModifiedRecord;
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

@XmlRootElement(name = "xincoCoreUserModifiedRecords")
public class XincoCoreUserModifiedRecordsConverter {
    private Collection<XincoCoreUserModifiedRecord> entities;
    private Collection<com.bluecubs.xinco.converter.XincoCoreUserModifiedRecordRefConverter> references;
    private URI uri;
  
    /** Creates a new instance of XincoCoreUserModifiedRecordsConverter */
    public XincoCoreUserModifiedRecordsConverter() {
    }

    /**
     * Creates a new instance of XincoCoreUserModifiedRecordsConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     */
    public XincoCoreUserModifiedRecordsConverter(Collection<XincoCoreUserModifiedRecord> entities, URI uri) {
        this.entities = entities;
        this.uri = uri;
    }

    /**
     * Returns a collection of XincoCoreUserModifiedRecordRefConverter.
     *
     * @return a collection of XincoCoreUserModifiedRecordRefConverter
     */
    @XmlElement(name = "xincoCoreUserModifiedRecordRef")
    public Collection<com.bluecubs.xinco.converter.XincoCoreUserModifiedRecordRefConverter> getReferences() {
        references = new ArrayList<XincoCoreUserModifiedRecordRefConverter>();
        if (entities != null) {
            for (XincoCoreUserModifiedRecord entity : entities) {
                references.add(new XincoCoreUserModifiedRecordRefConverter(entity, uri, true));
            }
        }
        return references;
    }

    /**
     * Sets a collection of XincoCoreUserModifiedRecordRefConverter.
     *
     * @param a collection of XincoCoreUserModifiedRecordRefConverter to set
     */
    public void setReferences(Collection<com.bluecubs.xinco.converter.XincoCoreUserModifiedRecordRefConverter> references) {
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
     * Returns a collection XincoCoreUserModifiedRecord entities.
     *
     * @return a collection of XincoCoreUserModifiedRecord entities
     */
    @XmlTransient
    public Collection<XincoCoreUserModifiedRecord> getEntities() {
        entities = new ArrayList<XincoCoreUserModifiedRecord>();
        if (references != null) {
            for (XincoCoreUserModifiedRecordRefConverter ref : references) {
                entities.add(ref.getEntity());
            }
        }
        return entities;
    }
}
