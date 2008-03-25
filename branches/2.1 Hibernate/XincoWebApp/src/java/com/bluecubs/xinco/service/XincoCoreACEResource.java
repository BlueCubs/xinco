/*
 *  XincoCoreACEResource
 *
 * Created on March 25, 2008, 11:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.persistence.XincoCoreACE;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.ConsumeMime;
import javax.ws.rs.WebApplicationException;
import javax.persistence.NoResultException;
import com.bluecubs.xinco.converter.XincoCoreACEConverter;
import javax.ws.rs.core.UriInfo;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

public class XincoCoreACEResource {
    private Integer id;
    private UriInfo context;
  
    /** Creates a new instance of XincoCoreACEResource */
    public XincoCoreACEResource() {
    }

    /**
     * Constructor used for instantiating an instance of dynamic resource.
     *
     * @param context HttpContext inherited from the parent resource
     */
    public XincoCoreACEResource(Integer id, UriInfo context) {
        this.id = id;
        this.context = context;
    }

    /**
     * Get method for retrieving an instance of XincoCoreACE identified by id in XML format.
     *
     * @param id identifier for the entity
     * @return an instance of XincoCoreACEConverter
     */
    @GET
    @ProduceMime({"application/xml", "application/json"})
    public XincoCoreACEConverter get() {
        try {
            return new XincoCoreACEConverter(getEntity(), context.getAbsolutePath());
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Put method for updating an instance of XincoCoreACE identified by id using XML as the input format.
     *
     * @param id identifier for the entity
     * @param data an XincoCoreACEConverter entity that is deserialized from a XML stream
     */
    @PUT
    @ConsumeMime({"application/xml", "application/json"})
    public void put(XincoCoreACEConverter data) {
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
     * Delete method for deleting an instance of XincoCoreACE identified by id.
     *
     * @param id identifier for the entity
     */
    @DELETE
    public void delete() {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            XincoCoreACE entity = getEntity();
            service.removeEntity(entity);
            service.commitTx();
        } finally {
            service.close();
        }
    }

    /**
     * Returns an instance of XincoCoreACE identified by id.
     *
     * @param id identifier for the entity
     * @return an instance of XincoCoreACE
     */
    protected XincoCoreACE getEntity() {
        try {
            return (XincoCoreACE) PersistenceService.getInstance().createQuery("SELECT e FROM XincoCoreACE e where e.id = :id").setParameter("id", id).getSingleResult();
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
    protected XincoCoreACE updateEntity(XincoCoreACE entity, XincoCoreACE newEntity) {
        newEntity.setId(entity.getId());
        entity = PersistenceService.getInstance().mergeEntity(newEntity);
        return entity;
    }
}
