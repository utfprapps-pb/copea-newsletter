package br.edu.utfpr.features.email.scenarios;

import br.edu.utfpr.features.email.Email;
import br.edu.utfpr.features.email.group.EmailGroup;
import br.edu.utfpr.features.email.request.EmailSelfRegistrationRequest;

public class EmailServiceTestScenario {

    public EmailSelfRegistrationRequest getEmailSelfRegistration() {
        EmailSelfRegistrationRequest emailSelfRegistration = new EmailSelfRegistrationRequest();
        emailSelfRegistration.setEmail("teste@gmail.com");
        emailSelfRegistration.setGroupUuid("4rt5-tth5-3455-gg34");
        return emailSelfRegistration;
    }

    public EmailGroup getEmailGroup() {
        EmailGroup emailGroup = new EmailGroup();
        emailGroup.setName("teste");
        return emailGroup;
    }

    public Email getEmail() {
        Email email = new Email();
        email.setEmail("teste@gmail.com");
        return email;
    }

}
