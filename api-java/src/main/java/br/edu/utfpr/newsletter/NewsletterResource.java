package br.edu.utfpr.newsletter;

import br.edu.utfpr.generic.crud.GenericResource;
import br.edu.utfpr.reponses.DefaultResponse;
import org.jboss.resteasy.reactive.RestResponse;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("v1/newsletter")
@RequestScoped
public class NewsletterResource extends GenericResource<Newsletter, Newsletter, Long, NewsletterService> {

    public NewsletterResource() {
        super(Newsletter.class, Newsletter.class);
    }

    @Override
    public Response get() {
        return Response.ok(getService().findByUser()).build();
    }

    @Override
    public Response getById(Long aLong) {
        return Response.ok(getService().findByIdAndUser(aLong)).build();
    }

    @GET
    @Path("{id}/send-email")
    public Response sendEmailsNewsletter(@PathParam("id") Long newsletterId) {
        try {
            DefaultResponse defaultResponse = getService().sendNewsletterByEmail(newsletterId);
            return Response.status(defaultResponse.getHttpStatus()).entity(defaultResponse).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new DefaultResponse().builder()
                            .httpStatus(RestResponse.StatusCode.BAD_REQUEST)
                            .message("Erro ao enviar email." +
                                    System.lineSeparator() +
                                    "Motivo: " + e.getMessage()).build())
                    .build();
        }
    }

    @GET
    @Path("search")
    public Response getByFilters(@DefaultValue("true") @QueryParam("newslettersSent") Boolean newslettersSent,
                                 @DefaultValue("true") @QueryParam("newslettersNotSent") Boolean newslettersNotSent,
                                 @DefaultValue("false") @QueryParam("newsletterTemplate") Boolean newslettersTemplate,
                                 @DefaultValue("") @QueryParam("description") String description) {
        return Response.ok(
                getService().getByFilters(newslettersSent, newslettersNotSent, newslettersTemplate, description)
        ).build();
    }

}
