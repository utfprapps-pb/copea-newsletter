package br.edu.utfpr.features.newsletter;

import br.edu.utfpr.exception.validation.ValidationException;
import br.edu.utfpr.features.email.Email;
import br.edu.utfpr.features.email.EmailService;
import br.edu.utfpr.features.email.config.ConfigEmail;
import br.edu.utfpr.features.email.config.ConfigEmailService;
import br.edu.utfpr.features.email.group.EmailGroup;
import br.edu.utfpr.features.email.read.ReadEmailService;
import br.edu.utfpr.features.email.send.SendEmailService;
import br.edu.utfpr.features.email.send.log.SendEmailLog;
import br.edu.utfpr.features.email.send.log.SendEmailLogService;
import br.edu.utfpr.features.email.send.log.enums.SendEmailLogStatusEnum;
import br.edu.utfpr.features.newsletter.email_group.NewsletterEmailGroup;
import br.edu.utfpr.features.newsletter.requests.NewsletterSearchRequest;
import br.edu.utfpr.features.newsletter.responses.LastSentEmailNewsletter;
import br.edu.utfpr.features.quartz.tasks.QuartzTasks;
import br.edu.utfpr.features.quartz.tasks.QuartzTasksService;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.DefaultResponse;
import br.edu.utfpr.reponses.GenericResponse;
import br.edu.utfpr.shared.enums.NoYesEnum;
import br.edu.utfpr.utils.DateTimeUtils;
import com.sun.mail.imap.IMAPMessage;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.jboss.resteasy.reactive.RestResponse;

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
    SendEmailLogService sendEmailLogService;

    @Inject
    ConfigEmailService configEmailService;

    @Inject
    ReadEmailService readEmailService;

    @Inject
    EmailService emailService;

    @Inject
    QuartzTasksService quartzTasksService;

    @Inject
    NewsletterSearchQuery newsletterSearchQuery;

    @Override
    public GenericResponse save(Newsletter entity) {
        return saveOrUpdate(entity);
    }

    @Override
    public GenericResponse update(Newsletter entity) {
        return saveOrUpdate(entity);
    }

    private GenericResponse saveOrUpdate(Newsletter entity) {
        validJustOneNewsletterByDescription(entity);
        setDefaultValues(entity);
        GenericResponse response = super.save(entity);
        response.setMessage(entity.getId().toString());
        return response;
    }

    private void validJustOneNewsletterByDescription(Newsletter entity) {
        if (descriptionEqualsDescriptionDatabaseIgnoreCase(entity.getId(), entity.getDescription()))
            return;

        Optional<Newsletter> newsletterOptional = getRepository().findByDescriptionIgnoreCase(entity.getDescription());
        if (newsletterOptional.isPresent())
            throw new ValidationException("Já existe uma newsletter com o título informado. Por favor, informe outro.");
    }

    private boolean descriptionEqualsDescriptionDatabaseIgnoreCase(Long id, String description) {
        if (Objects.isNull(id))
            return false;

        Newsletter newsletterDb = findById(id);
        return (Objects.nonNull(newsletterDb) && description.equalsIgnoreCase(newsletterDb.getDescription()));
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

    /**
     * Método utilizado pelo Job do Quartz para enviar a newsletter por e-mail
     * sem utilizar o usuário logado, pois quando executa o job da tarefa
     * agendada não encontra o usuário logado
     * @param newsletterId
     * @return
     * @throws Exception
     */
    public DefaultResponse sendScheduledNewsletterByEmail(
            Long newsletterId, String jobName, String jobGroup, String triggerName, String triggerGroup
    ) throws Exception {
        Optional<Newsletter> optionalNewsletterEntity = getRepository().findById(newsletterId);
        if (optionalNewsletterEntity.isEmpty())
            return getResponseNewsletterNotFound();
        Newsletter newsletter = optionalNewsletterEntity.get();

        Optional<QuartzTasks> quartzTasksOptional = quartzTasksService.findByJobNameAndJobGroupAndTriggerNameAndTriggerGroup(
                jobName, jobGroup, triggerName, triggerGroup
        );
        Long quartzTaskId = null;
        if (quartzTasksOptional.isPresent())
            quartzTaskId = quartzTasksOptional.get().getId();

        return sendNewsletterByEmail(newsletter, configEmailService.getOneConfigEmailByUser(newsletter.getUser()), quartzTaskId);
    }

    /**
     * Método utilizado para enviar a newsletter por e-mail utilizando o usuário logado
     * @param newsletterId
     * @return
     * @throws Exception
     */
    public DefaultResponse sendNewsletterByEmail(Long newsletterId) throws Exception {
        Optional<Newsletter> optionalNewsletterEntity = getRepository().findByIdAndUser(newsletterId, getAuthSecurityFilter().getAuthUserContext().findByToken());
        return sendNewsletterByEmail(optionalNewsletterEntity.get(), configEmailService.getConfigEmailByLoggedUser(), null);
    }

    // TODO: SOMENTE TESTE: Ajustar depois para não passar o QuartzTaskId nesse método, pois só usa quando é por agendamento, normal não utiliza, deveria ser um método separado
    private DefaultResponse sendNewsletterByEmail(Newsletter newsletter, ConfigEmail configEmail, Long quartzTaskId) throws Exception {
        if (Objects.isNull(newsletter))
            return getResponseNewsletterNotFound();

        List<Email> subscribedEmails =
                newsletter.getEmails().stream().filter(
                        (Email email) -> Objects.equals(email.getSubscribed(), NoYesEnum.YES)
                ).collect(Collectors.toList());

        addEmailsOfGroupsInListEmailsToSendNewsletter(newsletter, subscribedEmails);

        validateSubscribedEmails(subscribedEmails);
        // TODO: comentado pois estou testando o cancelamento da inscrição por um link,
        //  após finalizar remover o cancelamento da forma atual, que é pela resposta no e-mail, pois deixa lento o processo
//        checkAnswersInEmailToUnsubscribe(subscribedEmails, configEmail);
        List<SendEmailLog> sendEmailLogs = sendEmailService.sendOneAtTime(
                newsletter.getSubject(),
                newsletter.getNewsletter(),
                configEmail,
                subscribedEmails);

        // TODO: Encontrar uma forma melhor de setar esse id, pois no método sendEmailService.send já é feito um save da entidade no banco
        if ((Objects.nonNull(quartzTaskId)) && (quartzTaskId > 0)) {
            sendEmailLogs.forEach((sendEmailLog -> {
                sendEmailLog.setQuartzTaskId(quartzTaskId);
                sendEmailLogService.update(sendEmailLog);
            }));
        }

        newsletter.getSendEmailLogs().addAll(sendEmailLogs);
        getRepository().save(newsletter);

        if (sendEmailLogs.stream().anyMatch((sendEmailLog -> !SendEmailLogStatusEnum.SENT.equals(sendEmailLog.getSentStatus()))))
            return DefaultResponse.builder()
                    .httpStatus(RestResponse.StatusCode.BAD_REQUEST)
                    .message("Algumas newsletter não foram enviadas com sucesso. Verifique os logs para maiores detalhes.")
                    .build();

        return DefaultResponse.builder()
                .httpStatus(RestResponse.StatusCode.OK)
                .message("Newsletter enviada aos emails vinculados com sucesso.")
                .build();
    }

    private DefaultResponse getResponseNewsletterNotFound() {
        return DefaultResponse.builder()
                .httpStatus(RestResponse.StatusCode.BAD_REQUEST)
                .message("Nenhuma newsletter encontrada para o parâmetro informado.")
                .build();
    }

    private void checkAnswersInEmailToUnsubscribe(List<Email> subscribedEmails, ConfigEmail configEmail) throws MessagingException {
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
        List<Message> messages = readEmailService.read(orTerm, configEmail);
        for (Message message : messages) {
            Address[] addresses = message.getFrom();
            if (addresses.length == 0)
                continue;
            String justEmail = ((InternetAddress) addresses[0]).getAddress();
            List<Email> emailsByAddressEmail = emailService.findEmailOrElseAll(justEmail);
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
        return newsletterSearchQuery.search(newsletterSearchRequest);
    }

    public LastSentEmailNewsletter getLastSentEmail(Long newsletterId) {
        return new LastSentEmailNewsletter(getRepository().findLastSentEmail(newsletterId));
    }

    // TODO: Melhorar e deixar mais limpo esse método, não é boa prática deixar for dentro de for
    private void addEmailsOfGroupsInListEmailsToSendNewsletter(Newsletter newsletter, List<Email> listEmailsToSend) {
        if ((Objects.isNull(newsletter.getEmailGroups())) || (newsletter.getEmailGroups().isEmpty()))
            return;

        for (NewsletterEmailGroup newsletterEmailGroup : newsletter.getEmailGroups()) {
            EmailGroup emailGroup = newsletterEmailGroup.getEmailGroup();
            if (Objects.isNull(emailGroup))
                continue;

            List<Email> emailsByGroup = emailService.findByGroupId(emailGroup.getId());
            if (emailsByGroup.isEmpty())
                continue;

            for (Email emailByGroup : emailsByGroup) {
                if ((Objects.equals(emailByGroup.getSubscribed(), NoYesEnum.YES)) &&
                        (!listEmailsToSend.stream().anyMatch(
                                (Email email) -> Objects.equals(email.getEmail(), emailByGroup.getEmail())
                        ))) {
                    listEmailsToSend.add(emailByGroup);
                }
            }
        }
    }

}
