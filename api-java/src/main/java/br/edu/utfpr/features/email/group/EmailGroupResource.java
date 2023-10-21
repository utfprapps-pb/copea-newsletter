package br.edu.utfpr.features.email.group;

import br.edu.utfpr.generic.crud.resource.GenericResource;
import br.edu.utfpr.features.user.responses.ExistsResponse;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("v1/email-group")
@RequestScoped
public class EmailGroupResource extends GenericResource<EmailGroup, Long, EmailGroupService> {

    @GET
    @Path("uuid/generate/{id}")
    @Transactional
    public Response uuidGenerate(@PathParam("id") Long id) {
        return Response.ok(getService().uuidGenerate(id)).build();
    }

    @DELETE
    @Path("uuid/{id}")
    @Transactional
    public Response uuidRemove(@PathParam("id") Long id) {
        getService().uuidRemove(id);
        return Response.ok().build();
    }

    @Path("exists")
    @GET
    public Response groupExists(@QueryParam("id") Long id, @QueryParam("name") String name) {
        return Response.ok(
                new ExistsResponse(getService().existsByName(id, name))
        ).build();
    }

}