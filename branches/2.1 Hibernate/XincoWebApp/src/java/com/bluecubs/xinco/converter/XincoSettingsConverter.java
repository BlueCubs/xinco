/*
 *  XincoSettingsConverter
 *
 * Created on March 25, 2008, 11:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoSetting;
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

@XmlRootElement(name = "xincoSettings")
public class XincoSettingsConverter {
    private Collection<XincoSetting> entities;
    private Collection<com.bluecubs.xinco.converter.XincoSettingRefConverter> references;
    private URI uri;
  
    /** Creates a new instance of XincoSettingsConverter */
    public XincoSettingsConverter() {
    }

    /**
     * Creates a new instance of XincoSettingsConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     */
    public XincoSettingsConverter(Collection<XincoSetting> entities, URI uri) {
        this.entities = entities;
        this.uri = uri;
    }

    /**
     * Returns a collection of XincoSettingRefConverter.
     *
     * @return a collection of XincoSettingRefConverter
     */
    @XmlElement(name = "xincoSettingRef")
    public Collection<com.bluecubs.xinco.converter.XincoSettingRefConverter> getReferences() {
        references = new ArrayList<XincoSettingRefConverter>();
        if (entities != null) {
            for (XincoSetting entity : entities) {
                references.add(new XincoSettingRefConverter(entity, uri, true));
            }
        }
        return references;
    }

    /**
     * Sets a collection of XincoSettingRefConverter.
     *
     * @param a collection of XincoSettingRefConverter to set
     */
    public void setReferences(Collection<com.bluecubs.xinco.converter.XincoSettingRefConverter> references) {
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
     * Returns a collection XincoSetting entities.
     *
     * @return a collection of XincoSetting entities
     */
    @XmlTransient
    public Collection<XincoSetting> getEntities() {
        entities = new ArrayList<XincoSetting>();
        if (references != null) {
            for (XincoSettingRefConverter ref : references) {
                entities.add(ref.getEntity());
            }
        }
        return entities;
    }
}
