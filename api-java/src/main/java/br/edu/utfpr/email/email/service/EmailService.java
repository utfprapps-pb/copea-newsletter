package br.edu.utfpr.email.email.service;

import br.edu.utfpr.email.config.entity.ConfigEmailEntity;
import br.edu.utfpr.email.config.service.ConfigEmailService;
import br.edu.utfpr.email.email.entity.EmailEntity;
import br.edu.utfpr.email.email.repository.EmailRepository;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.internet.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    public EmailEntity findEmailById(Long id) throws Exception {
        Optional<EmailEntity> email = emailRepository.findById(id);

        if (email.isPresent())
            return email.get();
        else
            throw new Exception("Registro não encontrado.");
    }

    public List<EmailEntity> findEmail(String email) throws Exception {
        List<EmailEntity> emails = emailRepository.findAllByEmailContaining(email);

        if (emails.isEmpty())
            throw new Exception("Nenhum registro encontrado.");
        else
            return emails;
    }

}
