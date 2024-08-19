package br.edu.utfpr.features.email;

import br.edu.utfpr.features.email.request.EmailSelfRegistrationRequest;
import br.edu.utfpr.features.email.request.EmailUnsubscribeRequest;
import br.edu.utfpr.features.user.responses.ExistsResponse;
import br.edu.utfpr.generic.crud.resource.mapstruct.GenericResourceDto;
import br.edu.utfpr.reponses.DefaultResponse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("v1/email")
@RequestScoped
public class EmailResource extends GenericResourceDto<Email, EmailDTO, EmailMapper, Long, EmailService> {

    public EmailResource() {
        super(Email.class, EmailDTO.class);
    }

    @GET
    @Path("find-email")
    public List<EmailDTO> get(@QueryParam("email") String email) {
        return getGenericMapper().toDtoList(
                getService().findEmailOrElseAll(email)
        );
    }

    @GET
    @Path("find-by-group")
    public List<EmailDTO> buscarPorGrupo(
            @QueryParam("groupId") Long groupId
    ) {
        return getGenericMapper().toDtoList(
                getService().findEmailsByGroup(groupId)
        );
    }

    @POST
    @Path("self-registration")
    @Transactional
    public Response saveSelfEmailRegistration(EmailSelfRegistrationRequest emailSelfRegistration) {
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

    @Path("unsubscribe")
    @POST
    public DefaultResponse unsubscribe(EmailUnsubscribeRequest emailUnsubscribeRequest) {
        getService().unsubscribe(emailUnsubscribeRequest);
        return new DefaultResponse(
                Response.Status.OK.getStatusCode(),
                "Inscrição cancelada com sucesso."
        );
    }

}
