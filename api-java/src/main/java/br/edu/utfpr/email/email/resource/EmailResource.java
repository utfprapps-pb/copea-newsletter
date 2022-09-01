package br.edu.utfpr.email.email.resource;

import br.edu.utfpr.email.email.entity.EmailEntity;
import br.edu.utfpr.email.email.request.EmailSendRequest;
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
    @Path("/send")
    public Response sendEmail(EmailSendRequest emailSendRequest) {
        try {
            emailService.send(emailSendRequest.getTitle(), emailSendRequest.getBody(), emailSendRequest.getEmailsList());
            return Response.ok("Email enviado com sucesso para os contatos informados.").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao enviar email." +
                            System.lineSeparator() +
                            "Motivo: " + e.getMessage())
                    .build();
        }
    }

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
