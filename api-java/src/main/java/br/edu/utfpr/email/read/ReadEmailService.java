package br.edu.utfpr.email.read;

import br.edu.utfpr.email.config.ConfigEmail;
import br.edu.utfpr.email.config.ConfigEmailService;
import br.edu.utfpr.exception.validation.ValidationException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.search.SearchTerm;
import java.util.Properties;

@RequestScoped
public class ReadEmailService {

    @Inject
    ConfigEmailService configEmailService;

    private Store store;
    private Folder inbox;

    public Message[] read(SearchTerm searchTerm) throws MessagingException {
        Session emailSession = Session.getDefaultInstance(new Properties());
        store = emailSession.getStore("imaps");

        ConfigEmail configEmail = configEmailService.getConfigEmailByLoggedUser();
        store.connect(configEmail.getSendHost(), configEmail.getEmailFrom(), configEmail.getPasswordEmailFrom());

        inbox = store.getFolder("Inbox");
        if (!inbox.exists())
            throw new ValidationException("Caixa Inbox não existe na conta de e-mail configurada para o envio da newsletter.");
        inbox.open(Folder.READ_WRITE);

        Message[] messages = inbox.search(searchTerm);

        return messages;
    }

    public void close() throws MessagingException {
        if (store.isConnected())
            store.close();
        if (inbox.isOpen())
            inbox.close(false);
    }

}
