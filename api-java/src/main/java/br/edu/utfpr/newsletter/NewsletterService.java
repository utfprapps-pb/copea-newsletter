package br.edu.utfpr.newsletter;

import br.edu.utfpr.email.Email;
import br.edu.utfpr.email.EmailService;
import br.edu.utfpr.email.config.ConfigEmailService;
import br.edu.utfpr.email.read.ReadEmailService;
import br.edu.utfpr.email.send.SendEmailService;
import br.edu.utfpr.email.send.log.SendEmailLog;
import br.edu.utfpr.email.send.log.enums.SendEmailLogStatusEnum;
import br.edu.utfpr.exception.validation.ValidationException;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.newsletter.responses.LastSentEmailNewsletter;
import br.edu.utfpr.reponses.DefaultResponse;
import br.edu.utfpr.reponses.GenericResponse;
import br.edu.utfpr.shared.enums.NoYesEnum;
import br.edu.utfpr.user.User;
import br.edu.utfpr.utils.DateTimeUtil;
import org.jboss.resteasy.reactive.RestResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.search.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequestScoped
public class NewsletterService extends GenericService<Newsletter, Long, NewsletterRepository> {

    @Inject
    SendEmailService sendEmailService;

    @Inject
    ConfigEmailService configEmailService;

    @Inject
    ReadEmailService readEmailService;

    @Inject
    EmailService emailService;

    @Override
    public GenericResponse save(Newsletter entity) {
        return saveOrUpdate(entity);
    }

    @Override
    public GenericResponse update(Newsletter entity) {
        return saveOrUpdate(entity);
    }

    private GenericResponse saveOrUpdate(Newsletter entity) {
        setDefaultValues(entity);
        GenericResponse response = super.save(entity);
        response.setMessage(entity.getId().toString());
        return response;
    }

    private void setDatesByNewOrUpdate(Newsletter entity) {
        if ((!Objects.isNull(entity.getId())) && (getRepository().existsById(entity.getId())))
            entity.setAlterationDate(LocalDateTime.now());
        else
            entity.setInclusionDate(LocalDateTime.now());
    }

    private void setDefaultValues(Newsletter entity) {
        setDatesByNewOrUpdate(entity);
        if (Objects.isNull(entity.getUser()))
            entity.setUser(getAuthSecurityFilter().getAuthUserContext().findByToken());
        if (Objects.isNull(entity.getNewsletterTemplate()))
            entity.setNewsletterTemplate(false);
    }

    public DefaultResponse sendNewsletterByEmail(Long newsletterId) throws Exception {
        Optional<Newsletter> optionalNewsletterEntity = getRepository().findByIdAndUser(newsletterId, getAuthSecurityFilter().getAuthUserContext().findByToken());
        if (optionalNewsletterEntity.isEmpty())
            return DefaultResponse.builder()
                    .httpStatus(RestResponse.StatusCode.BAD_REQUEST)
                    .message("Nenhuma newsletter encontrada para o parâmetro informado.")
                    .build();

        Newsletter newsletterEntity = optionalNewsletterEntity.get();

        List<Email> subscribedEmails =
                newsletterEntity.getEmails().stream().filter(
                        (Email email) -> Objects.equals(email.getSubscribed(), NoYesEnum.YES)
                ).collect(Collectors.toList());

        validateSubscribedEmails(subscribedEmails);
        checkAnswersInEmailToUnsubscribe(subscribedEmails);
        SendEmailLog sendEmailLog = sendEmailService.send(
                newsletterEntity.getSubject(),
                newsletterEntity.getNewsletter(),
                configEmailService.getConfigEmailByLoggedUser(),
                sendEmailService.convertArrayEmailEntityToStringArray(subscribedEmails));

        newsletterEntity.getSendEmailLogs().add(sendEmailLog);
        getRepository().save(newsletterEntity);

        if (SendEmailLogStatusEnum.SENT.equals(sendEmailLog.getSentStatus()))
            return DefaultResponse.builder()
                    .httpStatus(RestResponse.StatusCode.OK)
                    .message("Newsletter enviada aos emails vinculados com sucesso.")
                    .build();

        return DefaultResponse.builder()
                .httpStatus(RestResponse.StatusCode.BAD_REQUEST)
                .message(sendEmailLog.getError())
                .build();

    }

    private void checkAnswersInEmailToUnsubscribe(List<Email> subscribedEmails) throws MessagingException {
        List<SearchTerm> andTermArrayList = new ArrayList<>();

        for (Email email : subscribedEmails) {
            List<SearchTerm> searchTerms = new ArrayList<>();

            searchTerms.add(new BodyTerm("unsubscribe"));

            searchTerms.add(new FromStringTerm(email.getEmail()));
            if (Objects.nonNull(email.getUnsubscribedDate()))
                searchTerms.add(new ReceivedDateTerm(ComparisonTerm.GE, DateTimeUtil.localDateTimeToDate(email.getUnsubscribedDate())));

            AndTerm andTerm = new AndTerm(searchTerms.toArray(new SearchTerm[0]));
            andTermArrayList.add(andTerm);
        }

        OrTerm orTerm = new OrTerm(andTermArrayList.toArray(new SearchTerm[0]));

        Message[] messages = readEmailService.read(orTerm);

        for (Message message : messages) {
            Address[] addresses = message.getFrom();
            if (addresses.length == 0)
                continue;
            String justEmail = ((InternetAddress) addresses[0]).getAddress();
            List<Email> emailsByAddressEmail = emailService.findEmail(justEmail);
            unsubscribeEmails(emailsByAddressEmail, subscribedEmails, message);
        }

        readEmailService.close();

    }

    private void unsubscribeEmails(List<Email> emails, List<Email> subscribedEmails, Message messageEmail) {
        emails.forEach((email) -> {
            try {
                Date dateEmail = messageEmail.getReceivedDate();
                if ((Objects.isNull(email.getUnsubscribedDate())) || (dateEmail.after(DateTimeUtil.localDateTimeToDate(email.getUnsubscribedDate())))) {
                    subscribedEmails.remove(email);
                    email.setSubscribed(NoYesEnum.NO);
                    email.setUnsubscribedDate(LocalDateTime.now());
                    emailService.save(email);
                }
            } catch (MessagingException e) {
                e.printStackTrace();
                throw new ValidationException("Erro ao buscar data do e-mail de unsubscribe.");
            }
        });
    }

    private void validateSubscribedEmails(List<Email> subscribedEmails) {
        if (subscribedEmails.isEmpty())
            throw new ValidationException("Nenhum e-mail inscrito encontrado para a newsletter.");
    }

    public List<Newsletter> findByUser() {
        List<Newsletter> newsletterList =
                getRepository().findByUser(getAuthSecurityFilter().getAuthUserContext().findByToken());

        if (newsletterList.isEmpty())
            throwNotFoundException();

        return newsletterList;
    }

    public Newsletter findByIdAndUser(Long id) {
        Optional<Newsletter> newsletterOptional =
                getRepository().findByIdAndUser(id, getAuthSecurityFilter().getAuthUserContext().findByToken());

        if (newsletterOptional.isEmpty())
            throwNotFoundException();

        return newsletterOptional.get();
    }

    public List<Newsletter> getByFilters(Boolean newslettersSent, Boolean newslettersNotSent, Boolean newslettersTemplate, String description) {
        User loggedUser = getAuthSecurityFilter().getAuthUserContext().findByToken();

        if (newslettersSent && newslettersNotSent)
            return getRepository().findByNewsletterTemplateAndDescriptionContainsAndUser(newslettersTemplate, description, loggedUser);

        if (newslettersSent)
            return getRepository().findBySentStatusAndNewsletterTemplateAndDescription(SendEmailLogStatusEnum.SENT, newslettersTemplate, description, loggedUser.getId());

        return getRepository().findBySentStatusIsNotAndNewsletterTemplateAndDescription(SendEmailLogStatusEnum.SENT, newslettersTemplate, description, loggedUser.getId());
    }

    public LastSentEmailNewsletter getLastSentEmail(Long newsletterId) {
        return new LastSentEmailNewsletter(getRepository().findLastSentEmail(newsletterId));
    }

}
