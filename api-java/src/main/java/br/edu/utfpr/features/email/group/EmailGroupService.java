package br.edu.utfpr.features.email.group;

import br.edu.utfpr.exception.validation.ValidationException;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.GenericResponse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
public class EmailGroupService extends GenericService<EmailGroup, Long, EmailGroupRepository> {

    @Inject
    EntityManager entityManager;

    @Override
    public GenericResponse save(EmailGroup entity) {
        validJustOneEmailGroupByName(entity);
        return super.save(entity);
    }

    @Override
    public GenericResponse update(EmailGroup entity) {
        validJustOneEmailGroupByName(entity);
        return super.update(entity);
    }

    private void validJustOneEmailGroupByName(EmailGroup entity) {
        if (nameEqualsNameDatabaseIgnoreCase(entity.getId(), entity.getName()))
            return;

        Optional<EmailGroup> emailGroupOptional = getRepository().findByNameIgnoreCase(entity.getName());
        if (emailGroupOptional.isPresent())
            throw new ValidationException("Já existe um grupo com o nome informado. Por favor, informe outro.");
    }

    public Optional<EmailGroup> findByName(String name) {
        return getRepository().findByName(name);
    }

    public boolean existsByNameIgnoreCase(Long id, String name) {
        return (!nameEqualsNameDatabaseIgnoreCase(id, name)) && (getRepository().findByNameIgnoreCase(name).isPresent());
    }

    private boolean nameEqualsNameDatabaseIgnoreCase(Long id, String name) {
        if (Objects.isNull(id))
            return false;

        EmailGroup emailGroupDb = findById(id);
        return (Objects.nonNull(emailGroupDb) && name.equalsIgnoreCase(emailGroupDb.getName()));
    }

    @Override
    public GenericResponse deleteById(Long id) {
        deleteEmailGroupRelation(id);
        return super.deleteById(id);
    }

    private void deleteEmailGroupRelation(Long id) {
        Query queryGroup = entityManager.createNativeQuery(
                "DELETE FROM email_group_relation ege " +
                        "WHERE ege.email_group_id = :emailGroupId");
        queryGroup.setParameter("emailGroupId", id);
        queryGroup.executeUpdate();
    }

    public EmailGroup uuidGenerate(Long id) {
        return configUuid(id, UUID.randomUUID().toString());
    }

    public void uuidRemove(Long id) {
        configUuid(id, null);
    }

    private EmailGroup configUuid(Long groupId, String value) {
        Optional<EmailGroup> emailGroupOptional = getRepository().findById(groupId);
        if (emailGroupOptional.isEmpty())
            throwNotFoundException();

        EmailGroup emailGroup = emailGroupOptional.get();
        emailGroup.setUuidToSelfRegistration(value);
        return getRepository().save(emailGroup);
    }

    public Optional<EmailGroup> findByUuidToSelfRegistration(String uuidToSelfRegistration) {
        return getRepository().findByUuidToSelfRegistration(uuidToSelfRegistration);
    }

}
