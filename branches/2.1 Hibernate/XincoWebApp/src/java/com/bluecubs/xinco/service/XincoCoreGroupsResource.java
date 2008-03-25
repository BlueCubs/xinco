/*
 *  XincoCoreGroupsResource
 *
 * Created on March 25, 2008, 11:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.persistence.XincoCoreGroup;
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
import com.bluecubs.xinco.converter.XincoCoreGroupsConverter;
import com.bluecubs.xinco.converter.XincoCoreGroupConverter;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@Path("/xincoCoreGroups/")
public class XincoCoreGroupsResource {
    @Context
    private UriInfo context;
  
    /** Creates a new instance of XincoCoreGroupsResource */
    public XincoCoreGroupsResource() {
    }

    /**
     * Constructor used for instantiating an instance of dynamic resource.
     *
     * @param context HttpContext inherited from the parent resource
     */
    public XincoCoreGroupsResource(UriInfo context) {
        this.context = context;
    }

    /**
     * Get method for retrieving a collection of XincoCoreGroup instance in XML format.
     *
     * @return an instance of XincoCoreGroupsConverter
     */
    @GET
    @ProduceMime({"application/xml", "application/json"})
    public XincoCoreGroupsConverter get(@QueryParam("start")
    @DefaultValue("0")
    int start, @QueryParam("max")
    @DefaultValue("10")
    int max) {
        try {
            return new XincoCoreGroupsConverter(getEntities(start, max), context.getAbsolutePath());
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Post method for creating an instance of XincoCoreGroup using XML as the input format.
     *
     * @param data an XincoCoreGroupConverter entity that is deserialized from an XML stream
     * @return an instance of XincoCoreGroupConverter
     */
    @POST
    @ConsumeMime({"application/xml", "application/json"})
    public Response post(XincoCoreGroupConverter data) {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            XincoCoreGroup entity = data.getEntity();
            createEntity(entity);
            service.commitTx();
            return Response.created(context.getAbsolutePath().resolve(entity.getId() + "/")).build();
        } finally {
            service.close();
        }
    }

    /**
     * Returns a dynamic instance of XincoCoreGroupResource used for entity navigation.
     *
     * @return an instance of XincoCoreGroupResource
     */
    @Path("{id}/")
    public com.bluecubs.xinco.service.XincoCoreGroupResource getXincoCoreGroupResource(@PathParam("id")
    Integer id) {
        return new XincoCoreGroupResource(id, context);
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of XincoCoreGroup instances
     */
    protected Collection<XincoCoreGroup> getEntities(int start, int max) {
        return PersistenceService.getInstance().createQuery("SELECT e FROM XincoCoreGroup e").setFirstResult(start).setMaxResults(max).getResultList();
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(XincoCoreGroup entity) {
        PersistenceService.getInstance().persistEntity(entity);
    }
}
