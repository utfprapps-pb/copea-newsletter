package br.edu.utfpr.user;

import br.edu.utfpr.email.config.ConfigEmailService;
import br.edu.utfpr.email.send.SendEmailService;
import br.edu.utfpr.reponses.DefaultResponse;
import br.edu.utfpr.user.recover_password.RecoverPassword;
import br.edu.utfpr.user.recover_password.RecoverPasswordDTO;
import br.edu.utfpr.user.recover_password.RecoverPasswordService;
import br.edu.utfpr.user.responses.SendEmailCodeRecoverPassword;
import br.edu.utfpr.user.scenarios.UserServiceTestScenario;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.resteasy.reactive.RestResponse;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    private UserServiceTestScenario userServiceTestScenario = new UserServiceTestScenario();

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
        String username = "teste";
        String email = "teste@teste.com";
        User user = userServiceTestScenario.getUser(username, email);
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
        RecoverPasswordDTO recoverPasswordDTO = userServiceTestScenario.getRecoverPasswordDTO(
                "inexistente",
                null,
                null);
        Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.recoverPassword(recoverPasswordDTO),
                "Quando passar um usuário inexistente no método recoverPassword " +
                        "deve retornar a exception 'NotFoundException'."
        );
    }

    @Test
    public void test_Return_Status_OK_WhenCode_To_RecoverPassword_Is_Valid() {
        String username = "teste";
        User user = userServiceTestScenario.getUser(username, "");
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(user);

        Map<String, RecoverPassword> codeValid = new HashMap<>();
        codeValid.put(username, userServiceTestScenario.getRecoverPassworCode(username, 123));
        Mockito.when(recoverPasswordService.getCodeSentByEmail()).thenReturn(codeValid);

        RecoverPasswordDTO recoverPasswordDTO = userServiceTestScenario.getRecoverPasswordDTO(
                username,
                "novaSenha",
                123
        );

        DefaultResponse defaultResponse = userService.recoverPassword(recoverPasswordDTO);
        Assertions.assertEquals(
                RestResponse.StatusCode.OK,
                defaultResponse.getHttpStatus(),
                "Quando o código passado no método recoverPassword for válido, " +
                        "deve retornar o status OK (200) no retorno do método."
        );
    }

    @Test
    public void test_Return_Status_BAD_REQUEST_WhenCode_To_RecoverPassword_Is_Invalid() {
        String username = "teste";
        User user = userServiceTestScenario.getUser(username, "");
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(user);

        Map<String, RecoverPassword> codeValid = new HashMap<>();
        codeValid.put(username, userServiceTestScenario.getRecoverPassworCode(username, 123));
        Mockito.when(recoverPasswordService.getCodeSentByEmail()).thenReturn(codeValid);

        RecoverPasswordDTO recoverPasswordDTO = userServiceTestScenario.getRecoverPasswordDTO(
                username,
                "novaSenha",
                1234
        );

        DefaultResponse defaultResponse = userService.recoverPassword(recoverPasswordDTO);
        Assertions.assertEquals(
                RestResponse.StatusCode.BAD_REQUEST,
                defaultResponse.getHttpStatus(),
                "Quando o código passado no método recoverPassword for inválido, " +
                        "deve retornar o status BAD_REQUEST (400) no retorno do método."
        );
    }

    @Test
    public void test_If_UpdateUserNewPasswordByUsername_SetDataCorrect() {
        String username = "teste";
        User user = userServiceTestScenario.getUser(username, "");
        user.setPassword("senhaAntiga");
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(user);

        Map<String, RecoverPassword> codeValid = new HashMap<>();
        codeValid.put(username, userServiceTestScenario.getRecoverPassworCode(username, 123));
        Mockito.when(recoverPasswordService.getCodeSentByEmail()).thenReturn(codeValid);

        RecoverPasswordDTO recoverPasswordDTO = userServiceTestScenario.getRecoverPasswordDTO(
                username,
                "novaSenha",
                123
        );

        DefaultResponse defaultResponse = userService.recoverPassword(recoverPasswordDTO);
        Assertions.assertNotEquals(
                "senhaAntiga",
                user.getPassword(),
                "Quando passado um usuário e o código válido para recuperar a senha, " +
                        "a senha antiga não pode ser igual a nova, " +
                        "isso garante que a senha foi atualizada."
        );
    }

}
