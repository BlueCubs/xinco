/*
 *  XincoCoreDataTypeAttributesResource
 *
 * Created on March 25, 2008, 11:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.persistence.XincoCoreDataTypeAttribute;
import java.util.Collection;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.ConsumeMime;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import com.bluecubs.xinco.converter.XincoCoreDataTypeAttributesConverter;
import com.bluecubs.xinco.converter.XincoCoreDataTypeAttributeConverter;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@Path("/xincoCoreDataTypeAttributes/")
public class XincoCoreDataTypeAttributesResource {
    @Context
    private UriInfo context;
  
    /** Creates a new instance of XincoCoreDataTypeAttributesResource */
    public XincoCoreDataTypeAttributesResource() {
    }

    /**
     * Constructor used for instantiating an instance of dynamic resource.
     *
     * @param context HttpContext inherited from the parent resource
     */
    public XincoCoreDataTypeAttributesResource(UriInfo context) {
        this.context = context;
    }

    /**
     * Get method for retrieving a collection of XincoCoreDataTypeAttribute instance in XML format.
     *
     * @return an instance of XincoCoreDataTypeAttributesConverter
     */
    @GET
    @ProduceMime({"application/xml", "application/json"})
    public XincoCoreDataTypeAttributesConverter get(@QueryParam("start")
    @DefaultValue("0")
    int start, @QueryParam("max")
    @DefaultValue("10")
    int max) {
        try {
            return new XincoCoreDataTypeAttributesConverter(getEntities(start, max), context.getAbsolutePath());
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Post method for creating an instance of XincoCoreDataTypeAttribute using XML as the input format.
     *
     * @param data an XincoCoreDataTypeAttributeConverter entity that is deserialized from an XML stream
     * @return an instance of XincoCoreDataTypeAttributeConverter
     */
    @POST
    @ConsumeMime({"application/xml", "application/json"})
    public Response post(XincoCoreDataTypeAttributeConverter data) {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            XincoCoreDataTypeAttribute entity = data.getEntity();
            createEntity(entity);
            service.commitTx();
            return Response.created(context.getAbsolutePath().resolve(entity.getXincoCoreDataTypeAttributePK().getXincoCoreDataTypeId() + "," + entity.getXincoCoreDataTypeAttributePK().getAttributeId() + "/")).build();
        } finally {
            service.close();
        }
    }

    /**
     * Returns a dynamic instance of XincoCoreDataTypeAttributeResource used for entity navigation.
     *
     * @return an instance of XincoCoreDataTypeAttributeResource
     */
    @Path("{xincoCoreDataTypeId},{attributeId}/")
    public com.bluecubs.xinco.service.XincoCoreDataTypeAttributeResource getXincoCoreDataTypeAttributeResource(@PathParam("xincoCoreDataTypeId")
    int id1, @PathParam("attributeId")
    int id2) {
        return new XincoCoreDataTypeAttributeResource(id1, id2, context);
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of XincoCoreDataTypeAttribute instances
     */
    protected Collection<XincoCoreDataTypeAttribute> getEntities(int start, int max) {
        return PersistenceService.getInstance().createQuery("SELECT e FROM XincoCoreDataTypeAttribute e").setFirstResult(start).setMaxResults(max).getResultList();
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(XincoCoreDataTypeAttribute entity) {
        PersistenceService.getInstance().persistEntity(entity);
    }
}
