package br.edu.utfpr.newsletter.resource;

import br.edu.utfpr.newsletter.entity.NewsletterEntity;
import br.edu.utfpr.newsletter.service.NewsletterService;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("api/v1/newsletter")
@Resource
public class NewsletterResource {
    @Inject
    NewsletterService newsletterService;

    @POST
    public Response saveConfigEmail(NewsletterEntity newsletterEntity) {
        newsletterService.save(newsletterEntity);
        return Response.ok(newsletterEntity).build();
    }

    @PUT
    public Response updateConfigEmail(NewsletterEntity newsletterEntity) {
        newsletterService.update(newsletterEntity);
        return Response.ok(newsletterEntity).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteConfigEmail(@PathParam("id") Long id) {
        return Response.ok(newsletterService.delete(id)).build();
    }

    @GET
    public Response findAllConfigEmail() {
        return Response.ok(newsletterService.findAll()).build();
    }
}
