/*
 *  XincoCoreLogsConverter
 *
 * Created on March 25, 2008, 11:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreLog;
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

@XmlRootElement(name = "xincoCoreLogs")
public class XincoCoreLogsConverter {
    private Collection<XincoCoreLog> entities;
    private Collection<com.bluecubs.xinco.converter.XincoCoreLogRefConverter> references;
    private URI uri;
  
    /** Creates a new instance of XincoCoreLogsConverter */
    public XincoCoreLogsConverter() {
    }

    /**
     * Creates a new instance of XincoCoreLogsConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     */
    public XincoCoreLogsConverter(Collection<XincoCoreLog> entities, URI uri) {
        this.entities = entities;
        this.uri = uri;
    }

    /**
     * Returns a collection of XincoCoreLogRefConverter.
     *
     * @return a collection of XincoCoreLogRefConverter
     */
    @XmlElement(name = "xincoCoreLogRef")
    public Collection<com.bluecubs.xinco.converter.XincoCoreLogRefConverter> getReferences() {
        references = new ArrayList<XincoCoreLogRefConverter>();
        if (entities != null) {
            for (XincoCoreLog entity : entities) {
                references.add(new XincoCoreLogRefConverter(entity, uri, true));
            }
        }
        return references;
    }

    /**
     * Sets a collection of XincoCoreLogRefConverter.
     *
     * @param a collection of XincoCoreLogRefConverter to set
     */
    public void setReferences(Collection<com.bluecubs.xinco.converter.XincoCoreLogRefConverter> references) {
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
     * Returns a collection XincoCoreLog entities.
     *
     * @return a collection of XincoCoreLog entities
     */
    @XmlTransient
    public Collection<XincoCoreLog> getEntities() {
        entities = new ArrayList<XincoCoreLog>();
        if (references != null) {
            for (XincoCoreLogRefConverter ref : references) {
                entities.add(ref.getEntity());
            }
        }
        return entities;
    }
}
