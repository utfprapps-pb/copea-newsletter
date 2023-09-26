package br.edu.utfpr.email;

import br.edu.utfpr.email.group.EmailGroup;
import br.edu.utfpr.email.group.EmailGroupService;
import br.edu.utfpr.email.group.relation.EmailGroupRelation;
import br.edu.utfpr.email.self_registration.EmailSelfRegistration;
import br.edu.utfpr.exception.validation.ValidationException;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.GenericResponse;
import br.edu.utfpr.shared.enums.NoYesEnum;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequestScoped
public class EmailService extends GenericService<Email, Long, EmailRepository> {

    @Inject
    EntityManager em;

    @Inject
    EmailGroupService emailGroupService;

    @Override
    public GenericResponse save(Email entity) {
        validJustOneEmailByEmail(entity);
        return super.save(entity);
    }

    @Override
    public GenericResponse update(Email entity) {
        validJustOneEmailByEmail(entity);
        return super.update(entity);
    }

    private void validJustOneEmailByEmail(Email entity) {
        if (emailEqualsEmailDatabase(entity.getId(), entity.getEmail()))
            return;

        Optional<Email> emailOptional = findByEmail(entity.getEmail());
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
    public void setDefaultValuesWhenNew(Email entity) {
        entity.setCreatedAt(LocalDateTime.now());
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
        Query query = em.createNativeQuery("SELECT e.id as id, e.email as email " +
                "FROM email_group_relation ege " +
                "LEFT JOIN email e on ege.email_id = e.id " +
                "WHERE ege.email_group_id = :groupId");
        query.setParameter("groupId", groupId);
        return query.getResultList();
    }

    @Override
    public GenericResponse deleteById(Long aLong) {
        // remove instâncias do grupo de destinatários
        Query queryGroup = em.createNativeQuery("DELETE FROM email_group_relation ege " +
                "WHERE ege.email_id = :emailId");
        queryGroup.setParameter("emailId", aLong);
        queryGroup.executeUpdate();

        // remove instâncias do grupo das newsletters
        Query queryNewsletter = em.createNativeQuery("DELETE FROM newsletter_email ne " +
                "WHERE ne.email_id = :emailId");
        queryNewsletter.setParameter("emailId", aLong);
        queryNewsletter.executeUpdate();

        // remove entidade
        return super.deleteById(aLong);
    }

    public void saveSelfEmailRegistration(EmailSelfRegistration emailSelfRegistration) {
        EmailGroup emailGroup =
                emailGroupService.findByUuidToSelfRegistration(emailSelfRegistration.getGroupUuid())
                        .orElseThrow(
                                () -> new ValidationException("Não foi possível incluir o e-mail pois o código do grupo é inválido.")
                        );
        validEmailAlreadySubscribedInGroup(emailSelfRegistration, emailGroup.getId());
        Optional<Email> emailOptional = getRepository().findByEmail(emailSelfRegistration.getEmail());
        if (emailOptional.isPresent())
            addGroupInEmail(emailSelfRegistration, emailOptional.get(), emailGroup);
        else
            createEmailAndAddGroup(emailSelfRegistration, emailGroup);
    }

    private void validEmailAlreadySubscribedInGroup(EmailSelfRegistration emailSelfRegistration, Long emailGroupId) {
        Optional<Email> emailByGroup = getRepository().findByEmailAndGroupId(emailSelfRegistration.getEmail(), emailGroupId);
        if (emailByGroup.isPresent())
            throw new ValidationException("O e-mail informado já está inscrito.");
    }

    private void addGroupInEmail(EmailSelfRegistration emailSelfRegistration, Email email, EmailGroup emailGroup) {
        List<EmailGroupRelation> emailGroups = email.getEmailGroupRelations();
        if (Objects.isNull(emailGroups))
            emailGroups = new ArrayList<>();

        EmailGroupRelation emailGroupRelation = new EmailGroupRelation();
        emailGroupRelation.setEmail(email);
        emailGroupRelation.setEmailGroup(emailGroup);
        emailGroupRelation.setUuidWasSelfRegistration(emailSelfRegistration.getGroupUuid());

        emailGroups.add(emailGroupRelation);
        update(email);
    }

    private void createEmailAndAddGroup(EmailSelfRegistration emailSelfRegistration, EmailGroup emailGroup) {
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

}
