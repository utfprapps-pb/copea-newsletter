package br.edu.utfpr.email.email.resource;

import br.edu.utfpr.email.email.request.SendEmailRequest;
import br.edu.utfpr.email.email.service.SendEmailService;
import br.edu.utfpr.reponses.DefaultResponse;
import org.jboss.resteasy.reactive.RestResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("api/v1/email/send")
public class SendEmailResource {

    @Inject
    SendEmailService sendEmailService;

    @POST
    public Response sendEmail(SendEmailRequest emailSendRequest) {
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

    @GET
    @Path("/newsletter/{id}")
    public Response sendEmailsNewsletter(@PathParam("id") Long newsletterId) {
        try {
            DefaultResponse defaultResponse = sendEmailService.sendNewsletterByEmail(newsletterId);
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

}
