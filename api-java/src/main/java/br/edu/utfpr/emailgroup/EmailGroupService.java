package br.edu.utfpr.emailgroup;

import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.GenericResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@RequestScoped
public class EmailGroupService extends GenericService<EmailGroup, Long, EmailGroupRepository> {

    @Inject
    EntityManager entityManager;

    @Override
    public GenericResponse deleteById(Long id) {
        deleteLinkedEmailGroupEmail(id);
        return super.deleteById(id);
    }

    private void deleteLinkedEmailGroupEmail(Long id) {
        Query queryGroup = entityManager.createNativeQuery(
                "DELETE FROM email_group_email ege " +
                "WHERE ege.email_group_id = :emailGroupId");
        queryGroup.setParameter("emailGroupId", id);
        queryGroup.executeUpdate();
    }

}
