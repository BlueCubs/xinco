/*
 *  XincoAddAttributeResource
 *
 * Created on March 25, 2008, 11:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.persistence.XincoAddAttribute;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.ConsumeMime;
import javax.ws.rs.WebApplicationException;
import javax.persistence.NoResultException;
import com.bluecubs.xinco.converter.XincoAddAttributeConverter;
import javax.ws.rs.core.UriInfo;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

public class XincoAddAttributeResource {
    private int id2;
    private int id1;
    private UriInfo context;
  
    /** Creates a new instance of XincoAddAttributeResource */
    public XincoAddAttributeResource() {
    }

    /**
     * Constructor used for instantiating an instance of dynamic resource.
     *
     * @param context HttpContext inherited from the parent resource
     */
    public XincoAddAttributeResource(int id1, int id2, UriInfo context) {
        this.id1 = id1;
        this.id2 = id2;
        this.context = context;
    }

    /**
     * Get method for retrieving an instance of XincoAddAttribute identified by id in XML format.
     *
     * @param id identifier for the entity
     * @return an instance of XincoAddAttributeConverter
     */
    @GET
    @ProduceMime({"application/xml", "application/json"})
    public XincoAddAttributeConverter get() {
        try {
            return new XincoAddAttributeConverter(getEntity(), context.getAbsolutePath());
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Put method for updating an instance of XincoAddAttribute identified by id using XML as the input format.
     *
     * @param id identifier for the entity
     * @param data an XincoAddAttributeConverter entity that is deserialized from a XML stream
     */
    @PUT
    @ConsumeMime({"application/xml", "application/json"})
    public void put(XincoAddAttributeConverter data) {
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
     * Delete method for deleting an instance of XincoAddAttribute identified by id.
     *
     * @param id identifier for the entity
     */
    @DELETE
    public void delete() {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            XincoAddAttribute entity = getEntity();
            service.removeEntity(entity);
            service.commitTx();
        } finally {
            service.close();
        }
    }

    /**
     * Returns an instance of XincoAddAttribute identified by id.
     *
     * @param id identifier for the entity
     * @return an instance of XincoAddAttribute
     */
    protected XincoAddAttribute getEntity() {
        try {
            com.bluecubs.xinco.persistence.XincoAddAttributePK id = new com.bluecubs.xinco.persistence.XincoAddAttributePK(id1, id2);
            return (XincoAddAttribute) PersistenceService.getInstance().createQuery("SELECT e FROM XincoAddAttribute e where e.xincoAddAttributePK = :xincoAddAttributePK").setParameter("xincoAddAttributePK", id).getSingleResult();
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
    protected XincoAddAttribute updateEntity(XincoAddAttribute entity, XincoAddAttribute newEntity) {
        newEntity.setXincoAddAttributePK(entity.getXincoAddAttributePK());
        entity = PersistenceService.getInstance().mergeEntity(newEntity);
        return entity;
    }
}
