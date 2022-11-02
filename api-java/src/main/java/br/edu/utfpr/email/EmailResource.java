package br.edu.utfpr.email;

import br.edu.utfpr.generic.crud.GenericResource;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("v1/email")
@RequestScoped
public class EmailResource extends GenericResource<Email, Long, EmailService> {

    @GET
    @Path("find-email")
    public Response get(@QueryParam("email") String email) {
        return Response.ok(getService().findEmail(email)).build();
    }

}
