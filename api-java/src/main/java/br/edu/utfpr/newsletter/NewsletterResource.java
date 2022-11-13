package br.edu.utfpr.newsletter;

import br.edu.utfpr.generic.crud.GenericResource;
import br.edu.utfpr.reponses.DefaultResponse;
import org.jboss.resteasy.reactive.RestResponse;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
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
    @Path("pesquisar-noticias")
    public Response pesquisarNoticias(
            @QueryParam("filtro") String filtro,
            @QueryParam("somenteNaoEnviadas") boolean somenteNaoEnviadas
    ) {
        return super.get();
    }

}
