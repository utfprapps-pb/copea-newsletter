package br.edu.utfpr.email.send;

import br.edu.utfpr.email.Email;
import br.edu.utfpr.email.config.ConfigEmail;
import br.edu.utfpr.email.config.ConfigEmailService;
import br.edu.utfpr.email.send.log.SendEmailLog;
import br.edu.utfpr.email.send.log.SendEmailLogService;
import br.edu.utfpr.htmlfileswithcidinsteadbase64.HtmlFilesWithCidInsteadBase64Service;
import br.edu.utfpr.htmlfileswithcidinsteadbase64.models.HtmlFileModel;
import br.edu.utfpr.htmlfileswithcidinsteadbase64.models.HtmlFilesWithCidInsteadBase64Model;
import lombok.Setter;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mail.internet.*;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RequestScoped
public class SendEmailService {

    @Inject
    ConfigEmailService configEmailService;

    @Autowired
    HtmlFilesWithCidInsteadBase64Service htmlFilesWithCidInsteadBase64Service;

    @Inject
    SendEmailLogService sendEmailLogService;

    private ConfigEmail configEmail;

    @Setter
    public Boolean findConfigEmailByUsernameUser = false;
    @Setter
    public String usernameUser = "";

    private ConfigEmail setFirstConfigEmail() {
        List<ConfigEmail> configEmails =
                ((findConfigEmailByUsernameUser) ? configEmailService.findByUsernameUser(usernameUser) : configEmailService.findByLoggedUser());
        if (configEmails.size() > 0)
            configEmail = configEmails.get(0);
        else
            throw new NotFoundException("Nenhuma configuração para envio de e-mail encontrada.");
        return configEmail;
    }

    public SendEmailLog send(String title, String body, String ...emailsList) throws Exception {
        HtmlEmail htmlEmail = new HtmlEmail();
        try {

            setFirstConfigEmail();

            htmlEmail = buildEmail();

            htmlEmail.setSubject(title);

            MimeMultipart mimeMultipart = new MimeMultipart();
            HtmlFilesWithCidInsteadBase64Model htmlFilesWithCidInsteadBase64Model =
                    htmlFilesWithCidInsteadBase64Service.findHtmlFilesWithCidInsteadBase64Model(body);
            if (htmlFilesWithCidInsteadBase64Model != null) {
                for (HtmlFileModel htmlFileModel : htmlFilesWithCidInsteadBase64Model.getHtml_files()) {
                    MimeBodyPart filePart = new PreencodedMimeBodyPart("base64");
                    filePart.setHeader("Content-ID", "<" + htmlFileModel.getContent_id() + ">");
                    filePart.setFileName(htmlFileModel.getContent_id() + '.' + htmlFileModel.getType_file());
                    filePart.setText(htmlFileModel.getJustbase64());

                    mimeMultipart.addBodyPart(filePart);
                }
                body = htmlFilesWithCidInsteadBase64Model.getHtml_with_content_id_instead_base64();
            }
            if (mimeMultipart.getCount() > 0)
                htmlEmail.addPart(mimeMultipart);

            htmlEmail.setTo(getEmailsForSend(emailsList));

            htmlEmail.setHtmlMsg(body);

            if (htmlEmail.getMimeMessage() == null) {
                htmlEmail.buildMimeMessage();
            }

            htmlEmail.sendMimeMessage();

            return sendEmailLogService.saveLog(htmlEmail, body, null);

        } catch (Exception exception) {

            sendEmailLogService.saveLog(htmlEmail, body, exception.getMessage());

            if (exception instanceof NotFoundException)
                throw new NotFoundException(exception.getMessage());

            throw new Exception(exception.getMessage());
        }
    }

    private Collection<InternetAddress> getEmailsForSend(String ...emailsList) throws AddressException {
        Collection<InternetAddress> aCollection = new ArrayList<>();
        for (String email : emailsList) {
            InternetAddress ie = new InternetAddress(email);
            aCollection.add(ie);
        }

        return aCollection;
    }

    private HtmlEmail buildEmail() throws EmailException {
        HtmlEmail htmlEmail = new HtmlEmail();

        htmlEmail.setHostName(configEmail.getSendHost());
        htmlEmail.setSmtpPort(configEmail.getSendPort());
        htmlEmail.setSslSmtpPort(configEmail.getSendPort().toString());

        htmlEmail.setAuthenticator(new DefaultAuthenticator(configEmail.getEmailFrom(), configEmail.getPasswordEmailFrom()));
        htmlEmail.setSSLOnConnect(true);
        htmlEmail.setFrom(configEmail.getEmailFrom());
        htmlEmail.setStartTLSRequired(true);

        return htmlEmail;
    }

    public String[] convertArrayEmailEntityToStringArray(Set<Email> array) {

        String stringArray = "";

        for (Email emailEntity : array) {
            stringArray += " " + emailEntity.getEmail();
        }

        return stringArray.trim().split(" ");

    }

}
