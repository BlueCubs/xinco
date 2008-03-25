/*
 *  XincoCoreLanguagesConverter
 *
 * Created on March 25, 2008, 11:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreLanguage;
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

@XmlRootElement(name = "xincoCoreLanguages")
public class XincoCoreLanguagesConverter {
    private Collection<XincoCoreLanguage> entities;
    private Collection<com.bluecubs.xinco.converter.XincoCoreLanguageRefConverter> references;
    private URI uri;
  
    /** Creates a new instance of XincoCoreLanguagesConverter */
    public XincoCoreLanguagesConverter() {
    }

    /**
     * Creates a new instance of XincoCoreLanguagesConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     */
    public XincoCoreLanguagesConverter(Collection<XincoCoreLanguage> entities, URI uri) {
        this.entities = entities;
        this.uri = uri;
    }

    /**
     * Returns a collection of XincoCoreLanguageRefConverter.
     *
     * @return a collection of XincoCoreLanguageRefConverter
     */
    @XmlElement(name = "xincoCoreLanguageRef")
    public Collection<com.bluecubs.xinco.converter.XincoCoreLanguageRefConverter> getReferences() {
        references = new ArrayList<XincoCoreLanguageRefConverter>();
        if (entities != null) {
            for (XincoCoreLanguage entity : entities) {
                references.add(new XincoCoreLanguageRefConverter(entity, uri, true));
            }
        }
        return references;
    }

    /**
     * Sets a collection of XincoCoreLanguageRefConverter.
     *
     * @param a collection of XincoCoreLanguageRefConverter to set
     */
    public void setReferences(Collection<com.bluecubs.xinco.converter.XincoCoreLanguageRefConverter> references) {
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
     * Returns a collection XincoCoreLanguage entities.
     *
     * @return a collection of XincoCoreLanguage entities
     */
    @XmlTransient
    public Collection<XincoCoreLanguage> getEntities() {
        entities = new ArrayList<XincoCoreLanguage>();
        if (references != null) {
            for (XincoCoreLanguageRefConverter ref : references) {
                entities.add(ref.getEntity());
            }
        }
        return entities;
    }
}
