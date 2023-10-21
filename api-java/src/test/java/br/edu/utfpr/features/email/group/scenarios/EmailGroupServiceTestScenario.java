package br.edu.utfpr.features.email.group.scenarios;

import br.edu.utfpr.features.email.group.EmailGroup;

public class EmailGroupServiceTestScenario {

    public EmailGroup getEmailGroup() {
        EmailGroup emailGroup = new EmailGroup();
        emailGroup.setName("teste");
        return emailGroup;
    }

}
