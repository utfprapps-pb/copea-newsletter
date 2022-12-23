package br.edu.utfpr.email;

import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.GenericResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Objects;

@RequestScoped
public class EmailService extends GenericService<Email, Long, EmailRepository> {

    @Inject
    EntityManager em;

    public List<Email> findAllEmail() {
        return getRepository().findAll();
    }

    public List<Email> findEmail(String email) {
        List<Email> emails;
        if (Objects.isNull(email))
            emails = getRepository().findAll();
        else
            emails = getRepository().findAllByEmailContaining(email);

        return emails;
    }

    public List<Email> findEmailsByGroup(Long groupId) {
        Query query = em.createNativeQuery("SELECT e.id as id, e.email as email " +
                "FROM email_group_email ege " +
                "LEFT JOIN email e on ege.email_id = e.id " +
                "WHERE ege.email_group_id = :groupId");
        query.setParameter("groupId", groupId);
        return query.getResultList();
    }

    @Override
    public GenericResponse deleteById(Long aLong) {
        // remove instâncias do groupo de destinatários
        Query queryGroup = em.createNativeQuery("DELETE FROM email_group_email ege " +
                "WHERE ege.email_id = :emailId");
        queryGroup.setParameter("emailId", aLong);
        queryGroup.executeUpdate();

        // remove instâncias do groupo das newsletters
        Query queryNewsletter = em.createNativeQuery("DELETE FROM newsletter_email ne " +
                "WHERE ne.email_id = :emailId");
        queryNewsletter.setParameter("emailId", aLong);
        queryNewsletter.executeUpdate();

        // remove entidade
        return super.deleteById(aLong);
    }
}
