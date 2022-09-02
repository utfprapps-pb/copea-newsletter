package br.edu.utfpr.email.email.service;

import br.edu.utfpr.email.config.entity.ConfigEmailEntity;
import br.edu.utfpr.email.config.service.ConfigEmailService;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import javax.inject.Inject;
import javax.mail.internet.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SendEmailService {

    @Inject
    ConfigEmailService configEmailService;

    private ConfigEmailEntity configEmail;

    private ConfigEmailEntity setFirstConfigEmail() throws Exception {
        List<ConfigEmailEntity> configEmails = configEmailService.findAllConfigEmail();
        if (configEmails.size() > 0)
            configEmail = configEmails.get(0);
        else
            throw new Exception("Nenhuma configuração de email encontrada.");
        return configEmail;
    }

    public void send(String title, String body, String ...emailsList) throws Exception {
        setFirstConfigEmail();

        HtmlEmail htmlEmail = buildEmail();

        htmlEmail.setSubject(title);

        MimeBodyPart filePart = new PreencodedMimeBodyPart("base64");
        filePart.setFileName("teste.png");
        filePart.setHeader("Content-ID", "<teste>");
        filePart.setText(body);

        MimeMultipart mimeMultipart = new MimeMultipart();
        mimeMultipart.addBodyPart(filePart);
        htmlEmail.addPart(mimeMultipart);

        htmlEmail.setTo(getEmailsForSend(emailsList));
//        htmlEmail.setHtmlMsg(body);
        htmlEmail.setHtmlMsg("<img src=\"cid:teste\"/>");
        if (htmlEmail.getMimeMessage() == null) {
            htmlEmail.buildMimeMessage();
        }
        htmlEmail.sendMimeMessage();
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

}
