package br.edu.utfpr.user;

import br.edu.utfpr.email.config.ConfigEmailService;
import br.edu.utfpr.email.send.SendEmailService;
import br.edu.utfpr.user.recover_password.RecoverPasswordDTO;
import br.edu.utfpr.user.recover_password.RecoverPasswordService;
import br.edu.utfpr.user.responses.SendEmailCodeRecoverPassword;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.NotFoundException;
import java.util.Objects;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    RecoverPasswordService recoverPasswordService;

    @Mock
    SendEmailService sendEmailService;

    @Mock
    ConfigEmailService configEmailService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_Return_NotFoundException_When_Username_To_SendEmailCodeRecoverPassword_NotExists() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);
        Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.sendEmailCodeRecoverPassword("usernameInexistente"),
                "Quando passar um usuário inexistente no método sendEmailCodeRecoverPassword " +
                        "deve retornar a exception 'NotFoundException'."
        );
    }

    @Test
    public void test_Return_Object_When_Username_To_SendEmailCodeRecoverPassword_Exists() throws Exception {
        User user = new User();
        String username = "teste";
        user.setUsername(username);
        String email = "teste@teste.com";
        user.setEmail(email);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);
        SendEmailCodeRecoverPassword sendEmailCodeRecoverPassword =
                userService.sendEmailCodeRecoverPassword(username);

        Assertions.assertTrue(
                Objects.equals(sendEmailCodeRecoverPassword.getMessage(), "Código enviado com sucesso para o e-mail " + email + ".") &&
                        Objects.equals(sendEmailCodeRecoverPassword.getEmail(), email),
                "Quando passado um usuário existente no método sendEmailCodeRecoverPassword, " +
                        "deve retornar o objeto com a mensagem e o e-mail do usuário."
        );
    }

    @Test
    public void test_Return_NotFoundException_When_Username_To_RecoverPassword_NotExists() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);
        RecoverPasswordDTO recoverPasswordDTO = new RecoverPasswordDTO();
        recoverPasswordDTO.setUsername("inexistente");
        Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.recoverPassword(recoverPasswordDTO),
                "Quando passar um usuário inexistente no método recoverPassword " +
                        "deve retornar a exception 'NotFoundException'."
        );
    }

}
