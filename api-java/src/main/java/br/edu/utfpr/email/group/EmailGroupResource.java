package br.edu.utfpr.email.group;

import br.edu.utfpr.generic.crud.GenericResource;
import br.edu.utfpr.user.responses.ExistsResponse;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Path("v1/email-group")
@RequestScoped
public class EmailGroupResource extends GenericResource<EmailGroup, EmailGroup, Long, EmailGroupService> {

    public EmailGroupResource() {
        super(EmailGroup.class, EmailGroup.class);
    }

    @GET
    @Path("uuid/generate/{id}")
    @Transactional
    public Response uuidGenerate(@PathParam("id") Long id) {
        return Response.ok(getService().uuidGenerate(id)).build();
    }

    @Path("exists")
    @GET
    public Response groupExists(@QueryParam("id") Long id, @QueryParam("name") String name) {
        return Response.ok(
                new ExistsResponse(getService().existsByName(id, name))
        ).build();
    }

}