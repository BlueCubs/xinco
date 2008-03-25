/*
 *  XincoAddAttributesResource
 *
 * Created on March 25, 2008, 11:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.persistence.XincoAddAttribute;
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
import com.bluecubs.xinco.converter.XincoAddAttributesConverter;
import com.bluecubs.xinco.converter.XincoAddAttributeConverter;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@Path("/xincoAddAttributes/")
public class XincoAddAttributesResource {
    @Context
    private UriInfo context;
  
    /** Creates a new instance of XincoAddAttributesResource */
    public XincoAddAttributesResource() {
    }

    /**
     * Constructor used for instantiating an instance of dynamic resource.
     *
     * @param context HttpContext inherited from the parent resource
     */
    public XincoAddAttributesResource(UriInfo context) {
        this.context = context;
    }

    /**
     * Get method for retrieving a collection of XincoAddAttribute instance in XML format.
     *
     * @return an instance of XincoAddAttributesConverter
     */
    @GET
    @ProduceMime({"application/xml", "application/json"})
    public XincoAddAttributesConverter get(@QueryParam("start")
    @DefaultValue("0")
    int start, @QueryParam("max")
    @DefaultValue("10")
    int max) {
        try {
            return new XincoAddAttributesConverter(getEntities(start, max), context.getAbsolutePath());
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Post method for creating an instance of XincoAddAttribute using XML as the input format.
     *
     * @param data an XincoAddAttributeConverter entity that is deserialized from an XML stream
     * @return an instance of XincoAddAttributeConverter
     */
    @POST
    @ConsumeMime({"application/xml", "application/json"})
    public Response post(XincoAddAttributeConverter data) {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            XincoAddAttribute entity = data.getEntity();
            createEntity(entity);
            service.commitTx();
            return Response.created(context.getAbsolutePath().resolve(entity.getXincoAddAttributePK().getXincoCoreDataId() + "," + entity.getXincoAddAttributePK().getAttributeId() + "/")).build();
        } finally {
            service.close();
        }
    }

    /**
     * Returns a dynamic instance of XincoAddAttributeResource used for entity navigation.
     *
     * @return an instance of XincoAddAttributeResource
     */
    @Path("{xincoCoreDataId},{attributeId}/")
    public com.bluecubs.xinco.service.XincoAddAttributeResource getXincoAddAttributeResource(@PathParam("xincoCoreDataId")
    int id1, @PathParam("attributeId")
    int id2) {
        return new XincoAddAttributeResource(id1, id2, context);
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of XincoAddAttribute instances
     */
    protected Collection<XincoAddAttribute> getEntities(int start, int max) {
        return PersistenceService.getInstance().createQuery("SELECT e FROM XincoAddAttribute e").setFirstResult(start).setMaxResults(max).getResultList();
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(XincoAddAttribute entity) {
        PersistenceService.getInstance().persistEntity(entity);
    }
}
