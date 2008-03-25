/*
 *  XincoCoreDataTypesResource
 *
 * Created on March 25, 2008, 11:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.persistence.XincoCoreDataType;
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
import com.bluecubs.xinco.converter.XincoCoreDataTypesConverter;
import com.bluecubs.xinco.converter.XincoCoreDataTypeConverter;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@Path("/xincoCoreDataTypes/")
public class XincoCoreDataTypesResource {
    @Context
    private UriInfo context;
  
    /** Creates a new instance of XincoCoreDataTypesResource */
    public XincoCoreDataTypesResource() {
    }

    /**
     * Constructor used for instantiating an instance of dynamic resource.
     *
     * @param context HttpContext inherited from the parent resource
     */
    public XincoCoreDataTypesResource(UriInfo context) {
        this.context = context;
    }

    /**
     * Get method for retrieving a collection of XincoCoreDataType instance in XML format.
     *
     * @return an instance of XincoCoreDataTypesConverter
     */
    @GET
    @ProduceMime({"application/xml", "application/json"})
    public XincoCoreDataTypesConverter get(@QueryParam("start")
    @DefaultValue("0")
    int start, @QueryParam("max")
    @DefaultValue("10")
    int max) {
        try {
            return new XincoCoreDataTypesConverter(getEntities(start, max), context.getAbsolutePath());
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Post method for creating an instance of XincoCoreDataType using XML as the input format.
     *
     * @param data an XincoCoreDataTypeConverter entity that is deserialized from an XML stream
     * @return an instance of XincoCoreDataTypeConverter
     */
    @POST
    @ConsumeMime({"application/xml", "application/json"})
    public Response post(XincoCoreDataTypeConverter data) {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            XincoCoreDataType entity = data.getEntity();
            createEntity(entity);
            service.commitTx();
            return Response.created(context.getAbsolutePath().resolve(entity.getId() + "/")).build();
        } finally {
            service.close();
        }
    }

    /**
     * Returns a dynamic instance of XincoCoreDataTypeResource used for entity navigation.
     *
     * @return an instance of XincoCoreDataTypeResource
     */
    @Path("{id}/")
    public com.bluecubs.xinco.service.XincoCoreDataTypeResource getXincoCoreDataTypeResource(@PathParam("id")
    Integer id) {
        return new XincoCoreDataTypeResource(id, context);
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of XincoCoreDataType instances
     */
    protected Collection<XincoCoreDataType> getEntities(int start, int max) {
        return PersistenceService.getInstance().createQuery("SELECT e FROM XincoCoreDataType e").setFirstResult(start).setMaxResults(max).getResultList();
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(XincoCoreDataType entity) {
        PersistenceService.getInstance().persistEntity(entity);
    }
}
