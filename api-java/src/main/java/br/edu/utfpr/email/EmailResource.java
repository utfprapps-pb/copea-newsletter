package br.edu.utfpr.email;

import br.edu.utfpr.generic.crud.GenericResource;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("v1/email")
@RequestScoped
public class EmailResource extends GenericResource<Email, Email, Long, EmailService> {

    public EmailResource() {
        super(Email.class, Email.class);
    }

    @GET
    @Path("find-email")
    public Response get(@QueryParam("email") String email) {
        return Response.ok(getService().findEmail(email)).build();
    }

    @GET
    @Path("find-by-group")
    public List<Email> buscarPorGrupo(
            @QueryParam("groupId") Long groupId
    ) {
        return getService().findEmailsByGroup(groupId);
    }

}
