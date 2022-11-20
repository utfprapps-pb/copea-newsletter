package br.edu.utfpr.email;

import br.edu.utfpr.generic.crud.GenericService;

import javax.enterprise.context.RequestScoped;
import java.util.List;
import java.util.Objects;

@RequestScoped
public class EmailService extends GenericService<Email, Long, EmailRepository> {

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

    public List<Email> findByGroupId(Long groupId) {
        //return findByGroupId(groupId);
        return null;
    }

}
