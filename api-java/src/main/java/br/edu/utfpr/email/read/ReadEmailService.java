package br.edu.utfpr.email.read;

import br.edu.utfpr.email.config.ConfigEmail;
import br.edu.utfpr.email.config.ConfigEmailService;
import br.edu.utfpr.exception.validation.ValidationException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.search.SearchTerm;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RequestScoped
public class ReadEmailService {

    @Inject
    ConfigEmailService configEmailService;

    private Store store;
    private Folder folder;

    public List<Message> read(SearchTerm searchTerm) throws MessagingException {
        Session emailSession = Session.getDefaultInstance(new Properties());
        store = emailSession.getStore("imaps");

        ConfigEmail configEmail = configEmailService.getConfigEmailByLoggedUser();
        store.connect(configEmail.getSendHost(), configEmail.getEmailFrom(), configEmail.getPasswordEmailFrom());

        List<Message> messages = new ArrayList<>();
        addArrayMessageIntoList(readFolder("Inbox", searchTerm), messages);
        addArrayMessageIntoList(readFolder("Spam", searchTerm), messages);
        return messages;
    }

    private void addArrayMessageIntoList(Message[] arrayMessage, List<Message> list) {
        for (Message message : arrayMessage)
            list.add(message);
    }

    private Message[] readFolder(String folderName, SearchTerm searchTerm) throws MessagingException {
        folder = store.getFolder(folderName);
        if (!folder.exists())
            throw new ValidationException("Caixa " + folderName + " não existe na conta de e-mail configurada para o envio da newsletter.");
        folder.open(Folder.READ_WRITE);
        return folder.search(searchTerm);
    }

    public void close() throws MessagingException {
        if (store.isConnected())
            store.close();
        if (folder.isOpen())
            folder.close(false);

    }

}
