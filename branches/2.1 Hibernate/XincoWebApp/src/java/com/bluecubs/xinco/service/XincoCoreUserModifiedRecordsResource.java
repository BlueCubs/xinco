/*
 *  XincoCoreUserModifiedRecordsResource
 *
 * Created on March 25, 2008, 11:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.persistence.XincoCoreUserModifiedRecord;
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
import com.bluecubs.xinco.converter.XincoCoreUserModifiedRecordsConverter;
import com.bluecubs.xinco.converter.XincoCoreUserModifiedRecordConverter;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@Path("/xincoCoreUserModifiedRecords/")
public class XincoCoreUserModifiedRecordsResource {
    @Context
    private UriInfo context;
  
    /** Creates a new instance of XincoCoreUserModifiedRecordsResource */
    public XincoCoreUserModifiedRecordsResource() {
    }

    /**
     * Constructor used for instantiating an instance of dynamic resource.
     *
     * @param context HttpContext inherited from the parent resource
     */
    public XincoCoreUserModifiedRecordsResource(UriInfo context) {
        this.context = context;
    }

    /**
     * Get method for retrieving a collection of XincoCoreUserModifiedRecord instance in XML format.
     *
     * @return an instance of XincoCoreUserModifiedRecordsConverter
     */
    @GET
    @ProduceMime({"application/xml", "application/json"})
    public XincoCoreUserModifiedRecordsConverter get(@QueryParam("start")
    @DefaultValue("0")
    int start, @QueryParam("max")
    @DefaultValue("10")
    int max) {
        try {
            return new XincoCoreUserModifiedRecordsConverter(getEntities(start, max), context.getAbsolutePath());
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Post method for creating an instance of XincoCoreUserModifiedRecord using XML as the input format.
     *
     * @param data an XincoCoreUserModifiedRecordConverter entity that is deserialized from an XML stream
     * @return an instance of XincoCoreUserModifiedRecordConverter
     */
    @POST
    @ConsumeMime({"application/xml", "application/json"})
    public Response post(XincoCoreUserModifiedRecordConverter data) {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            XincoCoreUserModifiedRecord entity = data.getEntity();
            createEntity(entity);
            service.commitTx();
            return Response.created(context.getAbsolutePath().resolve(entity.getXincoCoreUserModifiedRecordPK().getId() + "," + entity.getXincoCoreUserModifiedRecordPK().getRecordId() + "/")).build();
        } finally {
            service.close();
        }
    }

    /**
     * Returns a dynamic instance of XincoCoreUserModifiedRecordResource used for entity navigation.
     *
     * @return an instance of XincoCoreUserModifiedRecordResource
     */
    @Path("{id},{recordId}/")
    public com.bluecubs.xinco.service.XincoCoreUserModifiedRecordResource getXincoCoreUserModifiedRecordResource(@PathParam("id")
    int id1, @PathParam("recordId")
    int id2) {
        return new XincoCoreUserModifiedRecordResource(id1, id2, context);
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of XincoCoreUserModifiedRecord instances
     */
    protected Collection<XincoCoreUserModifiedRecord> getEntities(int start, int max) {
        return PersistenceService.getInstance().createQuery("SELECT e FROM XincoCoreUserModifiedRecord e").setFirstResult(start).setMaxResults(max).getResultList();
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(XincoCoreUserModifiedRecord entity) {
        PersistenceService.getInstance().persistEntity(entity);
    }
}
