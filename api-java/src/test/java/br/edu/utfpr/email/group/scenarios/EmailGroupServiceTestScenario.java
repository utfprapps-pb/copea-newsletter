package br.edu.utfpr.email.group.scenarios;

import br.edu.utfpr.email.group.EmailGroup;

public class EmailGroupServiceTestScenario {

    public EmailGroup getEmailGroup() {
        EmailGroup emailGroup = new EmailGroup();
        emailGroup.setName("teste");
        return emailGroup;
    }

}
