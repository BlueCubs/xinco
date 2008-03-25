/*
 *  XincoAddAttributeConverter
 *
 * Created on March 25, 2008, 11:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoAddAttribute;
import com.bluecubs.xinco.persistence.XincoAddAttributePK;
import java.net.URI;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@XmlRootElement(name = "xincoAddAttribute")
public class XincoAddAttributeConverter {
    private XincoAddAttribute entity;
    private URI uri;
  
    /** Creates a new instance of XincoAddAttributeConverter */
    public XincoAddAttributeConverter() {
        entity = new XincoAddAttribute();
    }

    /**
     * Creates a new instance of XincoAddAttributeConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     */
    public XincoAddAttributeConverter(XincoAddAttribute entity, URI uri) {
        this.entity = entity;
        this.uri = uri;
    }

    /**
     * Getter for xincoAddAttributePK.
     *
     * @return value for xincoAddAttributePK
     */
    @XmlElement
    public XincoAddAttributePK getXincoAddAttributePK() {
        return entity.getXincoAddAttributePK();
    }

    /**
     * Setter for xincoAddAttributePK.
     *
     * @param value the value to set
     */
    public void setXincoAddAttributePK(XincoAddAttributePK value) {
        entity.setXincoAddAttributePK(value);
    }

    /**
     * Getter for attribInt.
     *
     * @return value for attribInt
     */
    @XmlElement
    public Integer getAttribInt() {
        return entity.getAttribInt();
    }

    /**
     * Setter for attribInt.
     *
     * @param value the value to set
     */
    public void setAttribInt(Integer value) {
        entity.setAttribInt(value);
    }

    /**
     * Getter for attribUnsignedint.
     *
     * @return value for attribUnsignedint
     */
    @XmlElement
    public Integer getAttribUnsignedint() {
        return entity.getAttribUnsignedint();
    }

    /**
     * Setter for attribUnsignedint.
     *
     * @param value the value to set
     */
    public void setAttribUnsignedint(Integer value) {
        entity.setAttribUnsignedint(value);
    }

    /**
     * Getter for attribDouble.
     *
     * @return value for attribDouble
     */
    @XmlElement
    public Double getAttribDouble() {
        return entity.getAttribDouble();
    }

    /**
     * Setter for attribDouble.
     *
     * @param value the value to set
     */
    public void setAttribDouble(Double value) {
        entity.setAttribDouble(value);
    }

    /**
     * Getter for attribVarchar.
     *
     * @return value for attribVarchar
     */
    @XmlElement
    public String getAttribVarchar() {
        return entity.getAttribVarchar();
    }

    /**
     * Setter for attribVarchar.
     *
     * @param value the value to set
     */
    public void setAttribVarchar(String value) {
        entity.setAttribVarchar(value);
    }

    /**
     * Getter for attribText.
     *
     * @return value for attribText
     */
    @XmlElement
    public String getAttribText() {
        return entity.getAttribText();
    }

    /**
     * Setter for attribText.
     *
     * @param value the value to set
     */
    public void setAttribText(String value) {
        entity.setAttribText(value);
    }

    /**
     * Getter for attribDatetime.
     *
     * @return value for attribDatetime
     */
    @XmlElement
    public Date getAttribDatetime() {
        return entity.getAttribDatetime();
    }

    /**
     * Setter for attribDatetime.
     *
     * @param value the value to set
     */
    public void setAttribDatetime(Date value) {
        entity.setAttribDatetime(value);
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
     * Returns the XincoAddAttribute entity.
     *
     * @return an entity
     */
    @XmlTransient
    public XincoAddAttribute getEntity() {
        return entity;
    }

    /**
     * Sets the XincoAddAttribute entity.
     *
     * @param entity to set
     */
    public void setEntity(XincoAddAttribute entity) {
        this.entity = entity;
    }
}
