/*
 *  XincoCoreUserResource
 *
 * Created on March 25, 2008, 11:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.persistence.XincoCoreUser;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.ConsumeMime;
import javax.ws.rs.WebApplicationException;
import javax.persistence.NoResultException;
import com.bluecubs.xinco.converter.XincoCoreUserConverter;
import javax.ws.rs.core.UriInfo;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

public class XincoCoreUserResource {
    private Integer id;
    private UriInfo context;
  
    /** Creates a new instance of XincoCoreUserResource */
    public XincoCoreUserResource() {
    }

    /**
     * Constructor used for instantiating an instance of dynamic resource.
     *
     * @param context HttpContext inherited from the parent resource
     */
    public XincoCoreUserResource(Integer id, UriInfo context) {
        this.id = id;
        this.context = context;
    }

    /**
     * Get method for retrieving an instance of XincoCoreUser identified by id in XML format.
     *
     * @param id identifier for the entity
     * @return an instance of XincoCoreUserConverter
     */
    @GET
    @ProduceMime({"application/xml", "application/json"})
    public XincoCoreUserConverter get() {
        try {
            return new XincoCoreUserConverter(getEntity(), context.getAbsolutePath());
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Put method for updating an instance of XincoCoreUser identified by id using XML as the input format.
     *
     * @param id identifier for the entity
     * @param data an XincoCoreUserConverter entity that is deserialized from a XML stream
     */
    @PUT
    @ConsumeMime({"application/xml", "application/json"})
    public void put(XincoCoreUserConverter data) {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            updateEntity(getEntity(), data.getEntity());
            service.commitTx();
        } finally {
            service.close();
        }
    }

    /**
     * Delete method for deleting an instance of XincoCoreUser identified by id.
     *
     * @param id identifier for the entity
     */
    @DELETE
    public void delete() {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            XincoCoreUser entity = getEntity();
            service.removeEntity(entity);
            service.commitTx();
        } finally {
            service.close();
        }
    }

    /**
     * Returns an instance of XincoCoreUser identified by id.
     *
     * @param id identifier for the entity
     * @return an instance of XincoCoreUser
     */
    protected XincoCoreUser getEntity() {
        try {
            return (XincoCoreUser) PersistenceService.getInstance().createQuery("SELECT e FROM XincoCoreUser e where e.id = :id").setParameter("id", id).getSingleResult();
        } catch (NoResultException ex) {
            throw new WebApplicationException(new Throwable("Resource for " + context.getAbsolutePath() + " does not exist."), 404);
        }
    }

    /**
     * Updates entity using data from newEntity.
     *
     * @param entity the entity to update
     * @param newEntity the entity containing the new data
     * @return the updated entity
     */
    protected XincoCoreUser updateEntity(XincoCoreUser entity, XincoCoreUser newEntity) {
        newEntity.setId(entity.getId());
        entity = PersistenceService.getInstance().mergeEntity(newEntity);
        return entity;
    }
}
