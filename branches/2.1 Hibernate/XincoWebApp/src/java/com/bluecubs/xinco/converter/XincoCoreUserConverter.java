/*
 *  XincoCoreUserConverter
 *
 * Created on March 25, 2008, 11:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.converter;

import com.bluecubs.xinco.persistence.XincoCoreUser;
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

@XmlRootElement(name = "xincoCoreUser")
public class XincoCoreUserConverter {
    private XincoCoreUser entity;
    private URI uri;
  
    /** Creates a new instance of XincoCoreUserConverter */
    public XincoCoreUserConverter() {
        entity = new XincoCoreUser();
    }

    /**
     * Creates a new instance of XincoCoreUserConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     */
    public XincoCoreUserConverter(XincoCoreUser entity, URI uri) {
        this.entity = entity;
        this.uri = uri;
    }

    /**
     * Getter for id.
     *
     * @return value for id
     */
    @XmlElement
    public Integer getId() {
        return entity.getId();
    }

    /**
     * Setter for id.
     *
     * @param value the value to set
     */
    public void setId(Integer value) {
        entity.setId(value);
    }

    /**
     * Getter for username.
     *
     * @return value for username
     */
    @XmlElement
    public String getUsername() {
        return entity.getUsername();
    }

    /**
     * Setter for username.
     *
     * @param value the value to set
     */
    public void setUsername(String value) {
        entity.setUsername(value);
    }

    /**
     * Getter for userpassword.
     *
     * @return value for userpassword
     */
    @XmlElement
    public String getUserpassword() {
        return entity.getUserpassword();
    }

    /**
     * Setter for userpassword.
     *
     * @param value the value to set
     */
    public void setUserpassword(String value) {
        entity.setUserpassword(value);
    }

    /**
     * Getter for name.
     *
     * @return value for name
     */
    @XmlElement
    public String getName() {
        return entity.getName();
    }

    /**
     * Setter for name.
     *
     * @param value the value to set
     */
    public void setName(String value) {
        entity.setName(value);
    }

    /**
     * Getter for firstname.
     *
     * @return value for firstname
     */
    @XmlElement
    public String getFirstname() {
        return entity.getFirstname();
    }

    /**
     * Setter for firstname.
     *
     * @param value the value to set
     */
    public void setFirstname(String value) {
        entity.setFirstname(value);
    }

    /**
     * Getter for email.
     *
     * @return value for email
     */
    @XmlElement
    public String getEmail() {
        return entity.getEmail();
    }

    /**
     * Setter for email.
     *
     * @param value the value to set
     */
    public void setEmail(String value) {
        entity.setEmail(value);
    }

    /**
     * Getter for statusNumber.
     *
     * @return value for statusNumber
     */
    @XmlElement
    public int getStatusNumber() {
        return entity.getStatusNumber();
    }

    /**
     * Setter for statusNumber.
     *
     * @param value the value to set
     */
    public void setStatusNumber(int value) {
        entity.setStatusNumber(value);
    }

    /**
     * Getter for attempts.
     *
     * @return value for attempts
     */
    @XmlElement
    public int getAttempts() {
        return entity.getAttempts();
    }

    /**
     * Setter for attempts.
     *
     * @param value the value to set
     */
    public void setAttempts(int value) {
        entity.setAttempts(value);
    }

    /**
     * Getter for lastModified.
     *
     * @return value for lastModified
     */
    @XmlElement
    public Date getLastModified() {
        return entity.getLastModified();
    }

    /**
     * Setter for lastModified.
     *
     * @param value the value to set
     */
    public void setLastModified(Date value) {
        entity.setLastModified(value);
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
     * Returns the XincoCoreUser entity.
     *
     * @return an entity
     */
    @XmlTransient
    public XincoCoreUser getEntity() {
        return entity;
    }

    /**
     * Sets the XincoCoreUser entity.
     *
     * @param entity to set
     */
    public void setEntity(XincoCoreUser entity) {
        this.entity = entity;
    }
}
