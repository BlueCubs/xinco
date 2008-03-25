/*
 *  XincoCoreUserModifiedRecordResource
 *
 * Created on March 25, 2008, 11:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.persistence.XincoCoreUserModifiedRecord;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.ConsumeMime;
import javax.ws.rs.WebApplicationException;
import javax.persistence.NoResultException;
import com.bluecubs.xinco.converter.XincoCoreUserModifiedRecordConverter;
import javax.ws.rs.core.UriInfo;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

public class XincoCoreUserModifiedRecordResource {
    private int id2;
    private int id1;
    private UriInfo context;
  
    /** Creates a new instance of XincoCoreUserModifiedRecordResource */
    public XincoCoreUserModifiedRecordResource() {
    }

    /**
     * Constructor used for instantiating an instance of dynamic resource.
     *
     * @param context HttpContext inherited from the parent resource
     */
    public XincoCoreUserModifiedRecordResource(int id1, int id2, UriInfo context) {
        this.id1 = id1;
        this.id2 = id2;
        this.context = context;
    }

    /**
     * Get method for retrieving an instance of XincoCoreUserModifiedRecord identified by id in XML format.
     *
     * @param id identifier for the entity
     * @return an instance of XincoCoreUserModifiedRecordConverter
     */
    @GET
    @ProduceMime({"application/xml", "application/json"})
    public XincoCoreUserModifiedRecordConverter get() {
        try {
            return new XincoCoreUserModifiedRecordConverter(getEntity(), context.getAbsolutePath());
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Put method for updating an instance of XincoCoreUserModifiedRecord identified by id using XML as the input format.
     *
     * @param id identifier for the entity
     * @param data an XincoCoreUserModifiedRecordConverter entity that is deserialized from a XML stream
     */
    @PUT
    @ConsumeMime({"application/xml", "application/json"})
    public void put(XincoCoreUserModifiedRecordConverter data) {
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
     * Delete method for deleting an instance of XincoCoreUserModifiedRecord identified by id.
     *
     * @param id identifier for the entity
     */
    @DELETE
    public void delete() {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            XincoCoreUserModifiedRecord entity = getEntity();
            service.removeEntity(entity);
            service.commitTx();
        } finally {
            service.close();
        }
    }

    /**
     * Returns an instance of XincoCoreUserModifiedRecord identified by id.
     *
     * @param id identifier for the entity
     * @return an instance of XincoCoreUserModifiedRecord
     */
    protected XincoCoreUserModifiedRecord getEntity() {
        try {
            com.bluecubs.xinco.persistence.XincoCoreUserModifiedRecordPK id = new com.bluecubs.xinco.persistence.XincoCoreUserModifiedRecordPK(id1, id2);
            return (XincoCoreUserModifiedRecord) PersistenceService.getInstance().createQuery("SELECT e FROM XincoCoreUserModifiedRecord e where e.xincoCoreUserModifiedRecordPK = :xincoCoreUserModifiedRecordPK").setParameter("xincoCoreUserModifiedRecordPK", id).getSingleResult();
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
    protected XincoCoreUserModifiedRecord updateEntity(XincoCoreUserModifiedRecord entity, XincoCoreUserModifiedRecord newEntity) {
        newEntity.setXincoCoreUserModifiedRecordPK(entity.getXincoCoreUserModifiedRecordPK());
        entity = PersistenceService.getInstance().mergeEntity(newEntity);
        return entity;
    }
}
