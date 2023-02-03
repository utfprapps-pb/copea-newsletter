package br.edu.utfpr.email.send;

import br.edu.utfpr.email.config.ConfigEmailService;
import br.edu.utfpr.email.send.request.SendEmailRequest;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("v1/email/send")
public class SendEmailResource {

    @Inject
    SendEmailService sendEmailService;

    @Inject
    ConfigEmailService configEmailService;

    @POST
    public Response sendEmail(SendEmailRequest emailSendRequest) {
        try {
            sendEmailService.send(
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
