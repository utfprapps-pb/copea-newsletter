package br.edu.utfpr.features.newsletter;

import br.edu.utfpr.features.newsletter.requests.NewsletterSearchRequest;
import br.edu.utfpr.features.newsletter.responses.NewsletterSearchResponse;
import br.edu.utfpr.generic.crud.resource.mapstruct.GenericResourceDto;
import br.edu.utfpr.reponses.DefaultResponse;
import br.edu.utfpr.reponses.GenericResponse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("v1/newsletter")
@RequestScoped
public class NewsletterResource extends GenericResourceDto<
        Newsletter,
        NewsletterDTO,
        NewsletterMapper,
        Long,
        NewsletterService> {

    public NewsletterResource() {
        super(Newsletter.class, NewsletterDTO.class);
    }

    @Override
    public List<NewsletterDTO> get() {
        return getGenericMapper().toDtoList(
                getService().findByUser()
        );
    }

    @Override
    public NewsletterDTO getById(Long id) {
        return getGenericMapper().toDto(
                getService().findByIdAndUser(id)
        );
    }

    /**
     * Busca apenas o texto da newsletter pelo id.
     */
    @GET
    @Path("text/{id}")
    public GenericResponse<String> getTextById(@PathParam("id") Long id) {
        return GenericResponse.of(getService().findById(id).getNewsletter());
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
                    .entity(DefaultResponse.builder()
                            .httpStatus(RestResponse.StatusCode.BAD_REQUEST)
                            .message("Erro ao enviar email." +
                                    System.lineSeparator() +
                                    "Motivo: " + e.getMessage()).build())
                    .build();
        }
    }

    /**
     * Busca apenas os dados necessários para mostrar na pesquisa, sem o texto da newsletter, pois afeta a performance devido ao tamanho.
     */
    @POST
    @Path("search")
    public List<NewsletterSearchResponse> search(NewsletterSearchRequest newsletterSearchRequest) {
        return getService().search(newsletterSearchRequest);
    }

    @GET
    @Path("last-sent-email/newsletter/{id}")
    public Response getLastSentEmail(@PathParam("id") Long newsletterId) {
        return Response.ok(getService().getLastSentEmail(newsletterId)).build();
    }

}
