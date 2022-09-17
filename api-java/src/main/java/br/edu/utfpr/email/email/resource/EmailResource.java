package br.edu.utfpr.email.email.resource;

import br.edu.utfpr.email.email.entity.EmailEntity;
import br.edu.utfpr.email.email.service.EmailService;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("api/v1/email")
@Resource
public class EmailResource {

    @Inject
    EmailService emailService;

    @POST
    public Response saveEmail(@Valid EmailEntity email) {
        return saveUpdateEmail(email);
    }

    @PUT
    public Response updateEmail(@Valid EmailEntity email) {
        return saveUpdateEmail(email);
    }

    public Response saveUpdateEmail(EmailEntity email) {
        try {
            emailService.saveEmail(email);
            return Response.ok(email).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEmail(@PathParam("id") Long id) {
        return Response.ok(emailService.deleteEmail(id)).build();
    }

    @GET
    @Path("/find-all")
    public Response findAllEmail() {
        return Response.ok(emailService.findAllEmail()).build();
    }

    @GET
    @Path("/{id}")
    public Response findEmailById(@PathParam("id") Long id) {
        try {
            return Response.ok(emailService.findEmailById(id)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response findEmail(@QueryParam("email") String email) {
        try {
            return Response.ok(emailService.findEmail(email)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

}
