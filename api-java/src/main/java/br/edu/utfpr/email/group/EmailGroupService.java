package br.edu.utfpr.email.group;

import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.GenericResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
public class EmailGroupService extends GenericService<EmailGroup, Long, EmailGroupRepository> {

    @Inject
    EntityManager entityManager;

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
        Optional<EmailGroup> emailGroupOptional = getRepository().findById(id);
        if (emailGroupOptional.isEmpty())
            throwNotFoundException();

        EmailGroup emailGroup = emailGroupOptional.get();
        emailGroup.setUuidToSelfRegistration(UUID.randomUUID().toString());
        return getRepository().save(emailGroup);
    }

    public Optional<EmailGroup> findByUuidToSelfRegistration(String uuidToSelfRegistration) {
        return getRepository().findByUuidToSelfRegistration(uuidToSelfRegistration);
    }

}
