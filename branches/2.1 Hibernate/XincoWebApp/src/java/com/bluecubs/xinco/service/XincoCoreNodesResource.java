/*
 *  XincoCoreNodesResource
 *
 * Created on March 25, 2008, 11:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.persistence.XincoCoreNode;
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
import com.bluecubs.xinco.converter.XincoCoreNodesConverter;
import com.bluecubs.xinco.converter.XincoCoreNodeConverter;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@Path("/xincoCoreNodes/")
public class XincoCoreNodesResource {
    @Context
    private UriInfo context;
  
    /** Creates a new instance of XincoCoreNodesResource */
    public XincoCoreNodesResource() {
    }

    /**
     * Constructor used for instantiating an instance of dynamic resource.
     *
     * @param context HttpContext inherited from the parent resource
     */
    public XincoCoreNodesResource(UriInfo context) {
        this.context = context;
    }

    /**
     * Get method for retrieving a collection of XincoCoreNode instance in XML format.
     *
     * @return an instance of XincoCoreNodesConverter
     */
    @GET
    @ProduceMime({"application/xml", "application/json"})
    public XincoCoreNodesConverter get(@QueryParam("start")
    @DefaultValue("0")
    int start, @QueryParam("max")
    @DefaultValue("10")
    int max) {
        try {
            return new XincoCoreNodesConverter(getEntities(start, max), context.getAbsolutePath());
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Post method for creating an instance of XincoCoreNode using XML as the input format.
     *
     * @param data an XincoCoreNodeConverter entity that is deserialized from an XML stream
     * @return an instance of XincoCoreNodeConverter
     */
    @POST
    @ConsumeMime({"application/xml", "application/json"})
    public Response post(XincoCoreNodeConverter data) {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            XincoCoreNode entity = data.getEntity();
            createEntity(entity);
            service.commitTx();
            return Response.created(context.getAbsolutePath().resolve(entity.getId() + "/")).build();
        } finally {
            service.close();
        }
    }

    /**
     * Returns a dynamic instance of XincoCoreNodeResource used for entity navigation.
     *
     * @return an instance of XincoCoreNodeResource
     */
    @Path("{id}/")
    public com.bluecubs.xinco.service.XincoCoreNodeResource getXincoCoreNodeResource(@PathParam("id")
    Integer id) {
        return new XincoCoreNodeResource(id, context);
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of XincoCoreNode instances
     */
    protected Collection<XincoCoreNode> getEntities(int start, int max) {
        return PersistenceService.getInstance().createQuery("SELECT e FROM XincoCoreNode e").setFirstResult(start).setMaxResults(max).getResultList();
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(XincoCoreNode entity) {
        PersistenceService.getInstance().persistEntity(entity);
    }
}
