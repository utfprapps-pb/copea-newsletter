package br.edu.utfpr.features.email.send;

import br.edu.utfpr.features.email.config.ConfigEmailService;
import br.edu.utfpr.features.email.send.request.SendEmailRequest;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("v1/email/send")
public class SendEmailResource {

    @Inject
    SendEmailService sendEmailService;

    @Inject
    ConfigEmailService configEmailService;

    @POST
    public Response sendEmail(SendEmailRequest emailSendRequest) {
        try {
            sendEmailService.sendAllAtOnce(
                    emailSendRequest.getTitle(),
                    emailSendRequest.getBody(),
                    configEmailService.getConfigEmailByLoggedUser(),
                    emailSendRequest.getEmailsList()
            );
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

}
