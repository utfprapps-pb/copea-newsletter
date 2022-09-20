package br.edu.utfpr.newsletter.resource;

import br.edu.utfpr.newsletter.entity.NewsletterEntity;
import br.edu.utfpr.newsletter.service.NewsletterService;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("api/v1/newsletter")
@Resource
public class NewsletterResource {
    @Inject
    NewsletterService newsletterService;

    @POST
    public Response saveNewsletter(@Valid NewsletterEntity newsletterEntity) {
        return saveUpdateNewsletter(newsletterEntity);
    }

    @PUT
    public Response updateNewsletter(@Valid NewsletterEntity newsletterEntity) {
        return saveUpdateNewsletter(newsletterEntity);
    }

    public Response saveUpdateNewsletter(NewsletterEntity newsletterEntity) {
        try {
            newsletterService.save(newsletterEntity);
            return Response.ok(newsletterEntity).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteNewsletter(@PathParam("id") Long id) {
        try {
            return Response.ok(newsletterService.delete(id)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response findAllNewsletter() {
        try {
            return Response.ok(newsletterService.findAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response findNewsletterById(@PathParam("id") Long id) {
        try {
            return Response.ok(newsletterService.findNewsletterById(id)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
