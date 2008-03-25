/*
 *  XincoCoreUserHasXincoCoreGroupsResource
 *
 * Created on March 25, 2008, 11:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.persistence.XincoCoreUserHasXincoCoreGroup;
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
import com.bluecubs.xinco.converter.XincoCoreUserHasXincoCoreGroupsConverter;
import com.bluecubs.xinco.converter.XincoCoreUserHasXincoCoreGroupConverter;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@Path("/xincoCoreUserHasXincoCoreGroups/")
public class XincoCoreUserHasXincoCoreGroupsResource {
    @Context
    private UriInfo context;
  
    /** Creates a new instance of XincoCoreUserHasXincoCoreGroupsResource */
    public XincoCoreUserHasXincoCoreGroupsResource() {
    }

    /**
     * Constructor used for instantiating an instance of dynamic resource.
     *
     * @param context HttpContext inherited from the parent resource
     */
    public XincoCoreUserHasXincoCoreGroupsResource(UriInfo context) {
        this.context = context;
    }

    /**
     * Get method for retrieving a collection of XincoCoreUserHasXincoCoreGroup instance in XML format.
     *
     * @return an instance of XincoCoreUserHasXincoCoreGroupsConverter
     */
    @GET
    @ProduceMime({"application/xml", "application/json"})
    public XincoCoreUserHasXincoCoreGroupsConverter get(@QueryParam("start")
    @DefaultValue("0")
    int start, @QueryParam("max")
    @DefaultValue("10")
    int max) {
        try {
            return new XincoCoreUserHasXincoCoreGroupsConverter(getEntities(start, max), context.getAbsolutePath());
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Post method for creating an instance of XincoCoreUserHasXincoCoreGroup using XML as the input format.
     *
     * @param data an XincoCoreUserHasXincoCoreGroupConverter entity that is deserialized from an XML stream
     * @return an instance of XincoCoreUserHasXincoCoreGroupConverter
     */
    @POST
    @ConsumeMime({"application/xml", "application/json"})
    public Response post(XincoCoreUserHasXincoCoreGroupConverter data) {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            XincoCoreUserHasXincoCoreGroup entity = data.getEntity();
            createEntity(entity);
            service.commitTx();
            return Response.created(context.getAbsolutePath().resolve(entity.getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreUserId() + "," + entity.getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId() + "/")).build();
        } finally {
            service.close();
        }
    }

    /**
     * Returns a dynamic instance of XincoCoreUserHasXincoCoreGroupResource used for entity navigation.
     *
     * @return an instance of XincoCoreUserHasXincoCoreGroupResource
     */
    @Path("{xincoCoreUserId},{xincoCoreGroupId}/")
    public com.bluecubs.xinco.service.XincoCoreUserHasXincoCoreGroupResource getXincoCoreUserHasXincoCoreGroupResource(@PathParam("xincoCoreUserId")
    int id1, @PathParam("xincoCoreGroupId")
    int id2) {
        return new XincoCoreUserHasXincoCoreGroupResource(id1, id2, context);
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of XincoCoreUserHasXincoCoreGroup instances
     */
    protected Collection<XincoCoreUserHasXincoCoreGroup> getEntities(int start, int max) {
        return PersistenceService.getInstance().createQuery("SELECT e FROM XincoCoreUserHasXincoCoreGroup e").setFirstResult(start).setMaxResults(max).getResultList();
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(XincoCoreUserHasXincoCoreGroup entity) {
        PersistenceService.getInstance().persistEntity(entity);
    }
}
