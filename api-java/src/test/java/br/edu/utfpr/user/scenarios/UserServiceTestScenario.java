package br.edu.utfpr.user.scenarios;

import br.edu.utfpr.user.User;
import br.edu.utfpr.user.recover_password.RecoverPassword;
import br.edu.utfpr.user.recover_password.RecoverPasswordDTO;

public class UserServiceTestScenario {

    public User getUser(String username, String email) {
        User user = new User();
        user.setId(1l);
        user.setUsername(username);
        user.setEmail(email);
        return user;
    }

    public RecoverPassword getRecoverPassworCode(String username, Integer code) {
        RecoverPassword recoverPassword = new RecoverPassword();
        recoverPassword.setUsername(username);
        recoverPassword.setCode(123);
        return recoverPassword;
    }

    public RecoverPasswordDTO getRecoverPasswordDTO(String username, String newPassword, Integer code) {
        RecoverPasswordDTO recoverPasswordDTO = new RecoverPasswordDTO();
        recoverPasswordDTO.setUsername(username);
        recoverPasswordDTO.setNewPassword(newPassword);
        recoverPasswordDTO.setCode(code);
        return recoverPasswordDTO;
    }

}
