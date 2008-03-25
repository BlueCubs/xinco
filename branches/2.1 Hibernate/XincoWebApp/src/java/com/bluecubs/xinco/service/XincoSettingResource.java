/*
 *  XincoSettingResource
 *
 * Created on March 25, 2008, 11:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.persistence.XincoSetting;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.ConsumeMime;
import javax.ws.rs.WebApplicationException;
import javax.persistence.NoResultException;
import com.bluecubs.xinco.converter.XincoSettingConverter;
import javax.ws.rs.core.UriInfo;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

public class XincoSettingResource {
    private Integer id;
    private UriInfo context;
  
    /** Creates a new instance of XincoSettingResource */
    public XincoSettingResource() {
    }

    /**
     * Constructor used for instantiating an instance of dynamic resource.
     *
     * @param context HttpContext inherited from the parent resource
     */
    public XincoSettingResource(Integer id, UriInfo context) {
        this.id = id;
        this.context = context;
    }

    /**
     * Get method for retrieving an instance of XincoSetting identified by id in XML format.
     *
     * @param id identifier for the entity
     * @return an instance of XincoSettingConverter
     */
    @GET
    @ProduceMime({"application/xml", "application/json"})
    public XincoSettingConverter get() {
        try {
            return new XincoSettingConverter(getEntity(), context.getAbsolutePath());
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Put method for updating an instance of XincoSetting identified by id using XML as the input format.
     *
     * @param id identifier for the entity
     * @param data an XincoSettingConverter entity that is deserialized from a XML stream
     */
    @PUT
    @ConsumeMime({"application/xml", "application/json"})
    public void put(XincoSettingConverter data) {
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
     * Delete method for deleting an instance of XincoSetting identified by id.
     *
     * @param id identifier for the entity
     */
    @DELETE
    public void delete() {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            XincoSetting entity = getEntity();
            service.removeEntity(entity);
            service.commitTx();
        } finally {
            service.close();
        }
    }

    /**
     * Returns an instance of XincoSetting identified by id.
     *
     * @param id identifier for the entity
     * @return an instance of XincoSetting
     */
    protected XincoSetting getEntity() {
        try {
            return (XincoSetting) PersistenceService.getInstance().createQuery("SELECT e FROM XincoSetting e where e.id = :id").setParameter("id", id).getSingleResult();
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
    protected XincoSetting updateEntity(XincoSetting entity, XincoSetting newEntity) {
        newEntity.setId(entity.getId());
        entity = PersistenceService.getInstance().mergeEntity(newEntity);
        return entity;
    }
}
