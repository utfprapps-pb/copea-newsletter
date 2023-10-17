package br.edu.utfpr.email;

import br.edu.utfpr.email.group.EmailGroupService;
import br.edu.utfpr.email.scenarios.EmailServiceTestScenario;
import br.edu.utfpr.email.self_registration.EmailSelfRegistration;
import br.edu.utfpr.exception.validation.ValidationException;
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

import java.util.Optional;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    private EmailServiceTestScenario emailServiceTestScenario = new EmailServiceTestScenario();

    @InjectMocks
    EmailService emailService;

    @Mock
    EmailGroupService emailGroupService;

    @Mock
    EmailRepository emailRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_Return_ValidationException_When_GroupToSelfRegistration_IsInvalid() {
        Mockito.when(emailGroupService.findByUuidToSelfRegistration(Mockito.any()))
                .thenReturn(Optional.empty());
        EmailSelfRegistration emailSelfRegistration = emailServiceTestScenario.getEmailSelfRegistration();
        Assertions.assertThrows(
                ValidationException.class,
                () -> emailService.saveSelfEmailRegistration(emailSelfRegistration),
                "Quando for passado um grupo inválido para autoinscrição " +
                        "deve retornar a validação do tipo 'ValidationException'."
        );
    }

    @Test
    void test_Return_ValidationException_When_Email_Already_SubscribedInGroup() {
        Mockito.when(emailGroupService.findByUuidToSelfRegistration(Mockito.any()))
                .thenReturn(Optional.of(emailServiceTestScenario.getEmailGroup()));

        Mockito.when(emailRepository.findByEmailAndGroupId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(emailServiceTestScenario.getEmail()));

        EmailSelfRegistration emailSelfRegistration = emailServiceTestScenario.getEmailSelfRegistration();
        Assertions.assertThrows(
                ValidationException.class,
                () -> emailService.saveSelfEmailRegistration(emailSelfRegistration),
                "Quando for passado um grupo válido para autoinscrição " +
                        "mas o e-mail informado já existir no grupo " +
                        "deve retornar a validação do tipo 'ValidationException'."
        );
    }

}
