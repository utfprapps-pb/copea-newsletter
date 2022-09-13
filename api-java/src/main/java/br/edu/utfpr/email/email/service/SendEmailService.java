package br.edu.utfpr.email.email.service;

import br.edu.utfpr.email.config.entity.ConfigEmailEntity;
import br.edu.utfpr.email.config.service.ConfigEmailService;
import br.edu.utfpr.email.email.entity.EmailEntity;
import br.edu.utfpr.newsletter.entity.NewsletterEntity;
import br.edu.utfpr.newsletter.repository.NewsletterRepository;
import br.edu.utfpr.reponses.DefaultResponse;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.jboss.resteasy.reactive.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.internet.*;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class SendEmailService {

    @Inject
    ConfigEmailService configEmailService;

    @Autowired
    NewsletterRepository newsletterRepository;

    private ConfigEmailEntity configEmail;

    private ConfigEmailEntity setFirstConfigEmail() throws Exception {
        List<ConfigEmailEntity> configEmails = configEmailService.findAllConfigEmail();
        if (configEmails.size() > 0)
            configEmail = configEmails.get(0);
        else
            throw new Exception("Nenhuma configuração de email encontrada.");
        return configEmail;
    }

    public Boolean send(String title, String body, String ...emailsList) throws Exception {
        try {
            setFirstConfigEmail();

            HtmlEmail htmlEmail = buildEmail();

            htmlEmail.setSubject(title);

//            MimeBodyPart filePart = new PreencodedMimeBodyPart("base64");
//            filePart.setFileName("teste.png");
//            filePart.setHeader("Content-ID", "<teste>");
//            filePart.setText(body);

//            MimeMultipart mimeMultipart = new MimeMultipart();
//            mimeMultipart.addBodyPart(filePart);
//            htmlEmail.addPart(mimeMultipart);

            htmlEmail.setTo(getEmailsForSend(emailsList));
            htmlEmail.setHtmlMsg(body);
//            htmlEmail.setHtmlMsg("<img src=\"cid:teste\"/>");

            if (htmlEmail.getMimeMessage() == null) {
                htmlEmail.buildMimeMessage();
            }
            htmlEmail.sendMimeMessage();

            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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

    public DefaultResponse sendNewsletterByEmail(Long newsletterId) throws Exception {
        Optional<NewsletterEntity> optionalNewsletterEntity = newsletterRepository.findById(newsletterId);
        if (!optionalNewsletterEntity.isPresent())
            return new DefaultResponse().builder()
                    .httpStatus(RestResponse.StatusCode.BAD_REQUEST)
                    .message("Nenhuma newsletter encontrada para o parâmetro informado.")
                    .build();

        NewsletterEntity newsletterEntity = optionalNewsletterEntity.get();

        this.send(
                newsletterEntity.getSubject(),
                newsletterEntity.getNewsletter(),
                convertArrayEmailEntityToStringArray(newsletterEntity.getEmails()));

        return new DefaultResponse().builder()
                .httpStatus(RestResponse.StatusCode.OK)
                .message("Newsletter enviada aos emails vinculados com sucesso.")
                .build();
    }

    private String[] convertArrayEmailEntityToStringArray(Set<EmailEntity> array) {

        String stringArray = "";

        for (EmailEntity emailEntity : array) {
            stringArray += " " + emailEntity.getEmail();
        }

        return stringArray.trim().split(" ");

    }
}
