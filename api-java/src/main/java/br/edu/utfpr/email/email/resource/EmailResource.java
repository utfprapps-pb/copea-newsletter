package br.edu.utfpr.email.email.resource;

import br.edu.utfpr.email.email.entity.EmailEntity;
import br.edu.utfpr.email.email.service.EmailService;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("api/v1/email")
@Resource
public class EmailResource {

    @Inject
    EmailService emailService;

    @POST
    public Response saveEmail(EmailEntity configEmail) {
        emailService.saveEmail(configEmail);
        return Response.ok(configEmail).build();
    }

    @PUT
    public Response updateConfigEmail(EmailEntity email) {
        emailService.saveEmail(email);
        return Response.ok(email).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteConfigEmail(@PathParam("id") Long id) {
        return Response.ok(emailService.deleteEmail(id)).build();
    }

    @GET
    public Response findAllConfigEmail() {
        return Response.ok(emailService.findAllEmail()).build();
    }

}
