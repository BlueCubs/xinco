/*
 *  XincoCoreLanguagesResource
 *
 * Created on March 25, 2008, 11:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.service;

import com.bluecubs.xinco.persistence.XincoCoreLanguage;
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
import com.bluecubs.xinco.converter.XincoCoreLanguagesConverter;
import com.bluecubs.xinco.converter.XincoCoreLanguageConverter;


/**
 *
 * @author Javier A. Ortiz <javier.ortiz.78@gmail.com>
 */

@Path("/xincoCoreLanguages/")
public class XincoCoreLanguagesResource {
    @Context
    private UriInfo context;
  
    /** Creates a new instance of XincoCoreLanguagesResource */
    public XincoCoreLanguagesResource() {
    }

    /**
     * Constructor used for instantiating an instance of dynamic resource.
     *
     * @param context HttpContext inherited from the parent resource
     */
    public XincoCoreLanguagesResource(UriInfo context) {
        this.context = context;
    }

    /**
     * Get method for retrieving a collection of XincoCoreLanguage instance in XML format.
     *
     * @return an instance of XincoCoreLanguagesConverter
     */
    @GET
    @ProduceMime({"application/xml", "application/json"})
    public XincoCoreLanguagesConverter get(@QueryParam("start")
    @DefaultValue("0")
    int start, @QueryParam("max")
    @DefaultValue("10")
    int max) {
        try {
            return new XincoCoreLanguagesConverter(getEntities(start, max), context.getAbsolutePath());
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Post method for creating an instance of XincoCoreLanguage using XML as the input format.
     *
     * @param data an XincoCoreLanguageConverter entity that is deserialized from an XML stream
     * @return an instance of XincoCoreLanguageConverter
     */
    @POST
    @ConsumeMime({"application/xml", "application/json"})
    public Response post(XincoCoreLanguageConverter data) {
        PersistenceService service = PersistenceService.getInstance();
        try {
            service.beginTx();
            XincoCoreLanguage entity = data.getEntity();
            createEntity(entity);
            service.commitTx();
            return Response.created(context.getAbsolutePath().resolve(entity.getId() + "/")).build();
        } finally {
            service.close();
        }
    }

    /**
     * Returns a dynamic instance of XincoCoreLanguageResource used for entity navigation.
     *
     * @return an instance of XincoCoreLanguageResource
     */
    @Path("{id}/")
    public com.bluecubs.xinco.service.XincoCoreLanguageResource getXincoCoreLanguageResource(@PathParam("id")
    Integer id) {
        return new XincoCoreLanguageResource(id, context);
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of XincoCoreLanguage instances
     */
    protected Collection<XincoCoreLanguage> getEntities(int start, int max) {
        return PersistenceService.getInstance().createQuery("SELECT e FROM XincoCoreLanguage e").setFirstResult(start).setMaxResults(max).getResultList();
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(XincoCoreLanguage entity) {
        PersistenceService.getInstance().persistEntity(entity);
    }
}
