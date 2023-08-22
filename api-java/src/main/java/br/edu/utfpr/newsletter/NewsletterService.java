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
import br.edu.utfpr.newsletter.requests.NewsletterSearchRequest;
import br.edu.utfpr.newsletter.responses.LastSentEmailNewsletter;
import br.edu.utfpr.reponses.DefaultResponse;
import br.edu.utfpr.reponses.GenericResponse;
import br.edu.utfpr.shared.enums.NoYesEnum;
import br.edu.utfpr.sql.SQLBuilder;
import br.edu.utfpr.user.User;
import br.edu.utfpr.utils.DateTimeUtils;
import com.sun.mail.imap.IMAPMessage;
import org.jboss.resteasy.reactive.RestResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.search.*;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.*;

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

    @Inject
    EntityManager entityManager;

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
                ).toList();

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
            if (Objects.nonNull(email.getLastUnsubscribedDate()))
                searchTerms.add(new ReceivedDateTerm(ComparisonTerm.GE, DateTimeUtils.localDateTimeToDate(email.getLastUnsubscribedDate())));

            AndTerm andTerm = new AndTerm(searchTerms.toArray(new SearchTerm[0]));
            andTermArrayList.add(andTerm);
        }

        OrTerm orTerm = new OrTerm(andTermArrayList.toArray(new SearchTerm[0]));
        List<Message> messages = readEmailService.read(orTerm);
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
        emails.forEach(email -> {
            try {
                Date dateEmail = messageEmail.getReceivedDate();
                if ((Objects.isNull(email.getLastUnsubscribedDate())) || (dateEmail.after(DateTimeUtils.localDateTimeToDate(email.getLastUnsubscribedDate())))) {
                    subscribedEmails.remove(email);
                    email.setSubscribed(NoYesEnum.NO);
                    email.setLastUnsubscribedDate(LocalDateTime.now());
                    email.setLastEmailUnsubscribedDate(DateTimeUtils.dateToLocalDateTime(dateEmail));
                    email.setLastEmailUnsubscribedMessageID(((IMAPMessage) messageEmail).getMessageID());
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

    public List<Newsletter> search(NewsletterSearchRequest newsletterSearchRequest) {
        SQLBuilder sqlBuilder = new SQLBuilder("select newsletter.* from newsletter");
        if ((Objects.nonNull(newsletterSearchRequest.getDescription())) && (!newsletterSearchRequest.getDescription().isEmpty()))
            sqlBuilder.addAnd("(newsletter.description ilike '%' || :description || '%')", "description", newsletterSearchRequest.getDescription());
        addFilterNewsletterSentOrNot(sqlBuilder, newsletterSearchRequest);
        addFilterNewslettersTemplateMineOrShared(sqlBuilder, newsletterSearchRequest);
        Query query = sqlBuilder.createNativeQuery(entityManager, Newsletter.class);
        return query.getResultList();
    }

    private void addFilterNewsletterSentOrNot(SQLBuilder sqlBuilder, NewsletterSearchRequest newsletterSearchRequest) {
        if (newsletterSearchRequest.isNewslettersSent() == newsletterSearchRequest.isNewslettersNotSent())
            return;

        final String NEWSLETTER_SENT_FILTER = "(exists(select 1 from newsletter_send_email_log " +
                "left join send_email_log on (send_email_log.id = newsletter_send_email_log.send_email_log_id) " +
                "where (newsletter_send_email_log.newsletter_id = newsletter.id) and send_email_log.sentstatus = :sentStatusSent))";

        if (newsletterSearchRequest.isNewslettersSent())
            sqlBuilder.addAnd(NEWSLETTER_SENT_FILTER, "sentStatusSent", SendEmailLogStatusEnum.SENT.name());

        if (newsletterSearchRequest.isNewslettersNotSent())
            sqlBuilder.addAnd("(not " + NEWSLETTER_SENT_FILTER + ")", "sentStatusSent", SendEmailLogStatusEnum.SENT.name());
    }

    private void addFilterNewslettersTemplateMineOrShared(SQLBuilder sqlBuilder, NewsletterSearchRequest newsletterSearchRequest) {
        if (Objects.equals(newsletterSearchRequest.isNewslettersTemplateMine(), newsletterSearchRequest.isNewslettersTemplateShared())) {
            if (newsletterSearchRequest.isNewslettersTemplateMine())
                sqlBuilder.addAnd("(newsletter.newsletter_template)");
            else
                sqlBuilder.addAnd("(not (newsletter.newsletter_template))");
            return;
        }

        User loggedUser = getAuthSecurityFilter().getAuthUserContext().findByToken();
        if (!newsletterSearchRequest.isNewslettersTemplateMine())
            sqlBuilder.addAnd("(newsletter.newsletter_template) and (newsletter.user_id <> :loggedUserId)", "loggedUserId", loggedUser.getId());
        if (!newsletterSearchRequest.isNewslettersTemplateShared())
            sqlBuilder.addAnd("(newsletter.newsletter_template) and (newsletter.user_id = :loggedUserId)", "loggedUserId", loggedUser.getId());
    }

    public LastSentEmailNewsletter getLastSentEmail(Long newsletterId) {
        return new LastSentEmailNewsletter(getRepository().findLastSentEmail(newsletterId));
    }

}
