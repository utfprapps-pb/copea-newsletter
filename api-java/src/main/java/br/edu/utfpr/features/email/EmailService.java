package br.edu.utfpr.features.email;

import br.edu.utfpr.exception.validation.ValidationException;
import br.edu.utfpr.features.email.group.EmailGroup;
import br.edu.utfpr.features.email.group.EmailGroupService;
import br.edu.utfpr.features.email.group.relation.EmailGroupRelation;
import br.edu.utfpr.features.email.request.EmailSelfRegistrationRequest;
import br.edu.utfpr.features.email.request.EmailUnsubscribeRequest;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.GenericResponse;
import br.edu.utfpr.shared.enums.NoYesEnum;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.*;

@RequestScoped
public class EmailService extends GenericService<Email, Long, EmailRepository> {

    @Inject
    EntityManager em;

    @Inject
    EmailGroupService emailGroupService;

    @Override
    public GenericResponse save(Email email) {
        validJustOneEmailByEmail(email);
        generateUuidToUnsubscribe(email);
        return super.save(email);
    }

    @Override
    public GenericResponse update(Email email) {
        validJustOneEmailByEmail(email);
        generateUuidToUnsubscribe(email);
        return super.update(email);
    }

    public void generateUuidToUnsubscribeAndSave(Email email) {
        generateUuidToUnsubscribe(email);
        super.update(email);
    }

    private void generateUuidToUnsubscribe(Email email) {
        if (Objects.isNull(email.getUuidToUnsubscribe()) || email.getUuidToUnsubscribe().isEmpty())
            email.setUuidToUnsubscribe(UUID.randomUUID().toString());
    }

    private void validJustOneEmailByEmail(Email email) {
        if (emailEqualsEmailDatabase(email.getId(), email.getEmail()))
            return;

        Optional<Email> emailOptional = findByEmail(email.getEmail());
        if (emailOptional.isPresent())
            throw new ValidationException("Já existe um destinatário com o e-mail informado. Por favor, informe outro.");
    }

    public boolean existsByEmail(Long id, String email) {
        return (!emailEqualsEmailDatabase(id, email)) && (findByEmail(email).isPresent());
    }

    private boolean emailEqualsEmailDatabase(Long id, String email) {
        if (Objects.isNull(id))
            return false;
        Email emailDb = findById(id);
        return (Objects.nonNull(emailDb) && Objects.equals(email, emailDb.getEmail()));
    }

    @Override
    public void setDefaultValuesWhenNew(Email email) {
        email.setCreatedAt(LocalDateTime.now());
    }

    public List<Email> findAllEmail() {
        return getRepository().findAll();
    }

    public List<Email> findEmailOrElseAll(String email) {
        List<Email> emails;
        if (Objects.isNull(email))
            emails = getRepository().findAll();
        else
            emails = getRepository().findAllByEmailContaining(email);

        return emails;
    }

    public Optional<Email> findByEmail(String email) {
        return getRepository().findByEmail(email);
    }

    public List<Email> findByEmailAndNewsletter(String email, Long newsletterId) {
        List<Email> emails = getRepository().findByEmailAndNewsletter(email, newsletterId);
        if (emails.isEmpty())
            return new ArrayList<>();

        return emails;
    }


    public List<Email> findEmailsByGroup(Long groupId) {
        Query query = em.createNativeQuery(
                "SELECT e.id as id, e.email as email " +
                        "FROM email_group_relation ege " +
                        "LEFT JOIN email e on ege.email_id = e.id " +
                        "WHERE ege.email_group_id = :groupId"
        );
        query.setParameter("groupId", groupId);
        return query.getResultList();
    }

    @Override
    public GenericResponse deleteById(Long id) {
        // remove instâncias do grupo de destinatários
        Query queryGroup = em.createNativeQuery("DELETE FROM email_group_relation ege " +
                "WHERE ege.email_id = :emailId");
        queryGroup.setParameter("emailId", id);
        queryGroup.executeUpdate();

        // remove instâncias do grupo das newsletters
        Query queryNewsletter = em.createNativeQuery("DELETE FROM newsletter_email ne " +
                "WHERE ne.email_id = :emailId");
        queryNewsletter.setParameter("emailId", id);
        queryNewsletter.executeUpdate();

        return super.deleteById(id);
    }

    public void saveSelfEmailRegistration(EmailSelfRegistrationRequest emailSelfRegistration) {
        EmailGroup emailGroup =
                emailGroupService.findByUuidToSelfRegistration(emailSelfRegistration.getGroupUuid())
                        .orElseThrow(
                                () -> new ValidationException("Não foi possível incluir o e-mail pois o código do grupo é inválido.")
                        );
        Optional<Email> emailOptional = getRepository().findByEmail(emailSelfRegistration.getEmail());
        if (emailOptional.isPresent())
            selfRegistrationWhenEmailExists(emailSelfRegistration, emailOptional.get(), emailGroup);
        else
            createEmailAndAddGroup(emailSelfRegistration, emailGroup);
    }

    private void selfRegistrationWhenEmailExists(
            EmailSelfRegistrationRequest emailSelfRegistration,
            Email email,
            EmailGroup emailGroupBeingAdded
    ) {
        Optional<EmailGroupRelation> emailGroupRelationOptional =
                email.getEmailGroupRelations().stream().filter(item ->
                        Objects.equals(item.getEmailGroup().getId(), emailGroupBeingAdded.getId())
                ).findAny();
        boolean groupAlreadyExistsInEmail = emailGroupRelationOptional.isPresent();
        if (groupAlreadyExistsInEmail) {
            validEmailAlreadySubscribed(email);
            subscribeEmailAndUpdateUuidWasSelfRegistration(email, emailGroupRelationOptional.get(), emailGroupBeingAdded);
            return;
        }

        addGroupInEmail(emailSelfRegistration, email, emailGroupBeingAdded);
    }

    private void validEmailAlreadySubscribed(Email email) {
        if (Objects.equals(email.getSubscribed(), NoYesEnum.YES)) {
            throw new ValidationException("O e-mail informado já está inscrito.");
        }
    }

    private void subscribeEmailAndUpdateUuidWasSelfRegistration(
            Email email,
            EmailGroupRelation emailGroupRelation,
            EmailGroup emailGroupBeingAdded
    ) {
        email.setSubscribed(NoYesEnum.YES);
        emailGroupRelation.setUuidWasSelfRegistration(emailGroupBeingAdded.getUuidToSelfRegistration());
        update(email);
    }

    private void addGroupInEmail(
            EmailSelfRegistrationRequest emailSelfRegistration,
            Email email,
            EmailGroup emailGroupBeingAdded
    ) {
        List<EmailGroupRelation> emailGroups = email.getEmailGroupRelations();
        if (Objects.isNull(emailGroups))
            emailGroups = new ArrayList<>();

        EmailGroupRelation emailGroupRelation = new EmailGroupRelation();
        emailGroupRelation.setEmail(email);
        emailGroupRelation.setEmailGroup(emailGroupBeingAdded);
        emailGroupRelation.setUuidWasSelfRegistration(emailSelfRegistration.getGroupUuid());

        emailGroups.add(emailGroupRelation);
        email.setSubscribed(NoYesEnum.YES);
        update(email);
    }

    private void createEmailAndAddGroup(EmailSelfRegistrationRequest emailSelfRegistration, EmailGroup emailGroup) {
        Email newEmail = new Email();
        newEmail.setSubscribed(NoYesEnum.YES);
        newEmail.setEmail(emailSelfRegistration.getEmail());

        EmailGroupRelation emailGroupRelation = new EmailGroupRelation();
        emailGroupRelation.setEmail(newEmail);
        emailGroupRelation.setEmailGroup(emailGroup);
        emailGroupRelation.setUuidWasSelfRegistration(emailSelfRegistration.getGroupUuid());

        List<EmailGroupRelation> emailGroups = new ArrayList<>();
        emailGroups.add(emailGroupRelation);

        newEmail.setEmailGroupRelations(emailGroups);
        save(newEmail);
    }

    public List<Email> findByGroupId(Long groupId) {
        return getRepository().findByGroupId(groupId);
    }

    public void unsubscribe(EmailUnsubscribeRequest emailUnsubscribeRequest) {
        Optional<Email> emailOptional = getRepository().findByUuidToUnsubscribe(emailUnsubscribeRequest.getUuid());
        if (emailOptional.isEmpty())
            throw new NotFoundException("Não foi possível cancelar a inscrição pois nenhum e-mail pertence ao UUID informado.");

        Email email = emailOptional.get();
        email.setSubscribed(NoYesEnum.NO);
        email.setLastUnsubscribedDate(LocalDateTime.now());
        email.setLastEmailUnsubscribedDate(LocalDateTime.now());
        email.setUnsubscribeReason(emailUnsubscribeRequest.getReason());
        getRepository().save(email);
    }

}
