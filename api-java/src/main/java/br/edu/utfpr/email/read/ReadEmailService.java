package br.edu.utfpr.email.read;

import br.edu.utfpr.email.config.ConfigEmail;
import br.edu.utfpr.email.config.ConfigEmailService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.search.SearchTerm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

@RequestScoped
public class ReadEmailService {

    @Inject
    ConfigEmailService configEmailService;

    private static final Logger LOGGER = Logger.getLogger(ReadEmailService.class.getName());

    private Store store;
    private Folder folder;

    public List<Message> read(SearchTerm searchTerm) throws MessagingException {
        Session emailSession = Session.getDefaultInstance(new Properties());
        store = emailSession.getStore("imaps");

        ConfigEmail configEmail = configEmailService.getConfigEmailByLoggedUser();
        String emailFrom = configEmail.getEmailFrom();
        store.connect(configEmail.getSendHost(), emailFrom, configEmail.getPasswordEmailFrom());

        List<Message> messages = new ArrayList<>();
        addArrayMessageIntoList(readFolder("Inbox", searchTerm, emailFrom), messages);
        addArrayMessageIntoList(readFolder("Spam", searchTerm, emailFrom), messages);
        addArrayMessageIntoList(readFolder("[Gmail]/Spam", searchTerm, emailFrom), messages);
        return messages;
    }

    private void addArrayMessageIntoList(Message[] arrayMessage, List<Message> list) {
        Collections.addAll(list, arrayMessage);
    }

    private Message[] readFolder(String folderName, SearchTerm searchTerm, String emailFrom) throws MessagingException {
        folder = store.getFolder(folderName);
        if (!folder.exists()) {
            String message = String.format("Caixa %s não existe na conta de e-mail %s configurada para o envio da newsletter.", folderName, emailFrom);
            LOGGER.info(message);
            return new Message[0];
        }
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
