package br.edu.utfpr.email.config.resource;

import br.edu.utfpr.email.config.entity.ConfigEmailEntity;
import br.edu.utfpr.email.config.service.ConfigEmailService;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("api/v1/email/config")
@Resource
public class ConfigEmailResource {

    @Inject
    ConfigEmailService configEmailService;

    @POST
    public Response saveConfigEmail(ConfigEmailEntity configEmail) {
        configEmailService.saveConfigEmail(configEmail);
        return Response.ok(configEmail).build();
    }

    @PUT
    public Response updateConfigEmail(ConfigEmailEntity configEmail) {
        configEmailService.saveConfigEmail(configEmail);
        return Response.ok(configEmail).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteConfigEmail(@PathParam("id") Long id) {
        return Response.ok(configEmailService.deleteConfigEmail(id)).build();
    }

    @GET
    public Response findAllConfigEmail() {
        return Response.ok(configEmailService.findAllConfigEmail()).build();
    }

}
