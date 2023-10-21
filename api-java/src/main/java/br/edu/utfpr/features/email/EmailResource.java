package br.edu.utfpr.features.email;

import br.edu.utfpr.features.email.self_registration.EmailSelfRegistration;
import br.edu.utfpr.generic.crud.resource.GenericResource;
import br.edu.utfpr.reponses.DefaultResponse;
import br.edu.utfpr.features.user.responses.ExistsResponse;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("v1/email")
@RequestScoped
public class EmailResource extends GenericResource<Email, Long, EmailService> {

    @GET
    @Path("find-email")
    public Response get(@QueryParam("email") String email) {
        return Response.ok(getService().findEmailOrElseAll(email)).build();
    }

    @GET
    @Path("find-by-group")
    public List<Email> buscarPorGrupo(
            @QueryParam("groupId") Long groupId
    ) {
        return getService().findEmailsByGroup(groupId);
    }

    @POST
    @Path("self-registration")
    @Transactional
    public Response saveSelfEmailRegistration(EmailSelfRegistration emailSelfRegistration) {
        getService().saveSelfEmailRegistration(emailSelfRegistration);
        return Response.ok(
                new DefaultResponse(
                        Response.Status.OK.getStatusCode(),
                        "E-mail cadastrado com sucesso."
                )
        ).build();
    }

    @Path("exists")
    @GET
    public Response emailExists(@QueryParam("id") Long id, @QueryParam("email") String email) {
        return Response.ok(
                new ExistsResponse(getService().existsByEmail(id, email))
        ).build();
    }

}
