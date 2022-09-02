package br.edu.utfpr.email.email.resource;

import br.edu.utfpr.email.email.request.EmailNewsletterSendRequest;
import br.edu.utfpr.email.email.request.EmailSendRequest;
import br.edu.utfpr.email.email.service.SendEmailService;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("api/v1/email/send")
public class SendEmailResource {

    @Inject
    SendEmailService sendEmailService;

    @POST
    public Response sendEmail(EmailSendRequest emailSendRequest) {
        try {
            sendEmailService.send(emailSendRequest.getTitle(), emailSendRequest.getBody(), emailSendRequest.getEmailsList());
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
    public Response sendEmailsNewsletter(EmailNewsletterSendRequest emailNewsletterSendRequest) {
        return null;
    }

}
