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

}
