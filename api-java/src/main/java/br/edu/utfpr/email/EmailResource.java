package br.edu.utfpr.email;

import br.edu.utfpr.email.self_registration.EmailSelfRegistration;
import br.edu.utfpr.generic.crud.GenericResource;
import br.edu.utfpr.reponses.DefaultResponse;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
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

}
