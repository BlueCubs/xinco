/*
 *  XincoCoreDataResource
 *
 * Created on March 25, 2008, 11:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.persistence.XincoCoreData;
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
import com.bluecubs.xinco.converter.XincoCoreDataConverter;
import com.bluecubs.xinco.converter.XincoCoreDatumConverter;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@Path("/xincoCoreData/")
public class XincoCoreDataResource {
    @Context
    private UriInfo context;
  
    /** Creates a new instance of XincoCoreDataResource */
    public XincoCoreDataResource() {
    }

    /**
     * Constructor used for instantiating an instance of dynamic resource.
     *
     * @param context HttpContext inherited from the parent resource
     */
    public XincoCoreDataResource(UriInfo context) {
        this.context = context;
    }

    /**
     * Get method for retrieving a collection of XincoCoreData instance in XML format.
     *
     * @return an instance of XincoCoreDataConverter
     */
    @GET
    @ProduceMime({"application/xml", "application/json"})
    public XincoCoreDataConverter get(@QueryParam("start")
    @DefaultValue("0")
    int start, @QueryParam("max")
    @DefaultValue("10")
    int max) {
        try {
            return new XincoCoreDataConverter(getEntities(start, max), context.getAbsolutePath());
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Post method for creating an instance of XincoCoreData using XML as the input format.
     *
     * @param data an XincoCoreDatumConverter entity that is deserialized from an XML stream
     * @return an instance of XincoCoreDatumConverter
     */
    @POST
    @ConsumeMime({"application/xml", "application/json"})
    public Response post(XincoCoreDatumConverter data) {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            XincoCoreData entity = data.getEntity();
            createEntity(entity);
            service.commitTx();
            return Response.created(context.getAbsolutePath().resolve(entity.getId() + "/")).build();
        } finally {
            service.close();
        }
    }

    /**
     * Returns a dynamic instance of XincoCoreDatumResource used for entity navigation.
     *
     * @return an instance of XincoCoreDatumResource
     */
    @Path("{id}/")
    public com.bluecubs.xinco.service.XincoCoreDatumResource getXincoCoreDatumResource(@PathParam("id")
    Integer id) {
        return new XincoCoreDatumResource(id, context);
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of XincoCoreData instances
     */
    protected Collection<XincoCoreData> getEntities(int start, int max) {
        return PersistenceService.getInstance().createQuery("SELECT e FROM XincoCoreData e").setFirstResult(start).setMaxResults(max).getResultList();
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(XincoCoreData entity) {
        PersistenceService.getInstance().persistEntity(entity);
    }
}
