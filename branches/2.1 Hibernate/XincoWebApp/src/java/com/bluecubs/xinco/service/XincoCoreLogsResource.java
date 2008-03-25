/*
 *  XincoCoreLogsResource
 *
 * Created on March 25, 2008, 11:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.persistence.XincoCoreLog;
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
import com.bluecubs.xinco.converter.XincoCoreLogsConverter;
import com.bluecubs.xinco.converter.XincoCoreLogConverter;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@Path("/xincoCoreLogs/")
public class XincoCoreLogsResource {
    @Context
    private UriInfo context;
  
    /** Creates a new instance of XincoCoreLogsResource */
    public XincoCoreLogsResource() {
    }

    /**
     * Constructor used for instantiating an instance of dynamic resource.
     *
     * @param context HttpContext inherited from the parent resource
     */
    public XincoCoreLogsResource(UriInfo context) {
        this.context = context;
    }

    /**
     * Get method for retrieving a collection of XincoCoreLog instance in XML format.
     *
     * @return an instance of XincoCoreLogsConverter
     */
    @GET
    @ProduceMime({"application/xml", "application/json"})
    public XincoCoreLogsConverter get(@QueryParam("start")
    @DefaultValue("0")
    int start, @QueryParam("max")
    @DefaultValue("10")
    int max) {
        try {
            return new XincoCoreLogsConverter(getEntities(start, max), context.getAbsolutePath());
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Post method for creating an instance of XincoCoreLog using XML as the input format.
     *
     * @param data an XincoCoreLogConverter entity that is deserialized from an XML stream
     * @return an instance of XincoCoreLogConverter
     */
    @POST
    @ConsumeMime({"application/xml", "application/json"})
    public Response post(XincoCoreLogConverter data) {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            XincoCoreLog entity = data.getEntity();
            createEntity(entity);
            service.commitTx();
            return Response.created(context.getAbsolutePath().resolve(entity.getId() + "/")).build();
        } finally {
            service.close();
        }
    }

    /**
     * Returns a dynamic instance of XincoCoreLogResource used for entity navigation.
     *
     * @return an instance of XincoCoreLogResource
     */
    @Path("{id}/")
    public com.bluecubs.xinco.service.XincoCoreLogResource getXincoCoreLogResource(@PathParam("id")
    Integer id) {
        return new XincoCoreLogResource(id, context);
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of XincoCoreLog instances
     */
    protected Collection<XincoCoreLog> getEntities(int start, int max) {
        return PersistenceService.getInstance().createQuery("SELECT e FROM XincoCoreLog e").setFirstResult(start).setMaxResults(max).getResultList();
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(XincoCoreLog entity) {
        PersistenceService.getInstance().persistEntity(entity);
    }
}
