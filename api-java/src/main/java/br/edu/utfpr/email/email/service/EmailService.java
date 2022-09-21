package br.edu.utfpr.email.email.service;

import br.edu.utfpr.email.email.entity.EmailEntity;
import br.edu.utfpr.email.email.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class EmailService {

    @Autowired
    EmailRepository emailRepository;

    public void saveEmail(EmailEntity email) {
        emailRepository.save(email);
    }

    public String deleteEmail(Long id) {
        if (emailRepository.existsById(id)) {
            emailRepository.deleteById(id);
            return "Registro deletado com sucesso.";
        } else {
            return "Registro não econtrado.";
        }
    }

    public List<EmailEntity> findAllEmail() {
        return emailRepository.findAll();
    }

    public EmailEntity findEmailById(Long id) {
        Optional<EmailEntity> email = emailRepository.findById(id);

        return email.orElse(null);
    }

    public List<EmailEntity> findEmail(String email) {
        List<EmailEntity> emails;
        if (Objects.isNull(email))
            emails = emailRepository.findAll();
        else
            emails = emailRepository.findAllByEmailContaining(email);

        return emails;
    }

}
