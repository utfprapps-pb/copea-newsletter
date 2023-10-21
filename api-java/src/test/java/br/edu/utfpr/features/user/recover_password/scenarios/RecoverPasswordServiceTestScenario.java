package br.edu.utfpr.features.user.recover_password.scenarios;

import br.edu.utfpr.features.user.recover_password.RecoverPassword;

import java.time.LocalDateTime;

public class RecoverPasswordServiceTestScenario {

    public RecoverPassword getRecoverPassword(
            String username,
            Integer code,
            LocalDateTime dateTime
    ) {
        RecoverPassword recoverPassword = new RecoverPassword();
        recoverPassword.setUsername(username);
        recoverPassword.setCode(code);
        recoverPassword.setDateTime(dateTime);
        return recoverPassword;
    }

}
