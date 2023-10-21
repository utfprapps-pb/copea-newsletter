package br.edu.utfpr.features.user.recover_password;

import br.edu.utfpr.features.user.recover_password.scenarios.RecoverPasswordServiceTestScenario;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
class RecoverPasswordServiceTest {

    @InjectMocks
    RecoverPasswordService recoverPasswordService;

    private RecoverPasswordServiceTestScenario recoverPasswordServiceTestScenario = new RecoverPasswordServiceTestScenario();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_Return_True_If_Code_Is_Expired_After_30_minutes() {
        RecoverPassword recoverPassword = recoverPasswordServiceTestScenario.getRecoverPassword(
                "teste",
                123,
                LocalDateTime.now().minusMinutes(30)
        );
        recoverPasswordService.addCode(recoverPassword.getUsername(), recoverPassword);
        Assertions.assertTrue(
                recoverPasswordService.codeExpired(recoverPassword),
                "Quando a data que o código foi adicionado passou de 30 minutos, " +
                        "deve retornar true pois está expirado."
        );
    }

    @Test
    void test_Return_False_If_Code_Is_Valid_Before_30_minutes() {
        RecoverPassword recoverPassword = recoverPasswordServiceTestScenario.getRecoverPassword(
                "teste",
                123,
                LocalDateTime.now().minusMinutes(15)
        );
        recoverPasswordService.addCode(recoverPassword.getUsername(), recoverPassword);
        Assertions.assertFalse(
                recoverPasswordService.codeExpired(recoverPassword),
                "Quando a data que o código foi adicionado ainda não passou de 30 minutos, " +
                        "deve retornar false pois não está expirado."
        );
    }

    @Test
    void test_If_ClearExpiredCodesSentByEmail_After_30_minutes() {
        RecoverPassword recoverPassword = recoverPasswordServiceTestScenario.getRecoverPassword(
                "teste",
                123,
                LocalDateTime.now().minusMinutes(30)
        );
        recoverPasswordService.addCode(recoverPassword.getUsername(), recoverPassword);

        recoverPasswordService.clearExpiredCodesSentByEmail();
        Assertions.assertTrue(
                recoverPasswordService.getCodeSentByEmail().isEmpty(),
            "Quando adicionado um código de recuperação de senha que o tempo " +
                    "entre a data de criação e a data atual seja maior igual a 30 minutos, " +
                    "deve remover da lista."
        );
    }

    @Test
    void test_If_Not_ClearExpiredCodesSentByEmail_Before_30_minutes() {
        RecoverPassword recoverPassword = recoverPasswordServiceTestScenario.getRecoverPassword(
                "teste",
                123,
                LocalDateTime.now().minusMinutes(15)
        );
        recoverPasswordService.addCode(recoverPassword.getUsername(), recoverPassword);

        recoverPasswordService.clearExpiredCodesSentByEmail();
        Assertions.assertTrue(
                (recoverPasswordService.getCodeSentByEmail().size() == 1),
                "Quando adicionado um código de recuperação de senha que o tempo " +
                        "entre a data de criação e a data atual seja menor que 30 minutos, " +
                        "deve manter na lista."
        );
    }

}
