package br.edu.utfpr.features.email;

import br.edu.utfpr.exception.validation.ValidationException;
import br.edu.utfpr.features.email.group.EmailGroupService;
import br.edu.utfpr.features.email.scenarios.EmailServiceTestScenario;
import br.edu.utfpr.features.email.request.EmailSelfRegistrationRequest;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

@QuarkusTest
class EmailServiceTest {

    private EmailServiceTestScenario emailServiceTestScenario = new EmailServiceTestScenario();

    @Inject
    EmailService emailService;

    @InjectMock
    EmailGroupService emailGroupService;

    @InjectMock
    EmailRepository emailRepository;

    @BeforeEach
    public void setup() {
    }

    @Test
    void test_Return_ValidationException_When_GroupToSelfRegistration_IsInvalid() {
        Mockito.when(emailGroupService.findByUuidToSelfRegistration(Mockito.any()))
                .thenReturn(Optional.empty());
        EmailSelfRegistrationRequest emailSelfRegistration = emailServiceTestScenario.getEmailSelfRegistration();
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

        EmailSelfRegistrationRequest emailSelfRegistration = emailServiceTestScenario.getEmailSelfRegistration();
        Assertions.assertThrows(
                ValidationException.class,
                () -> emailService.saveSelfEmailRegistration(emailSelfRegistration),
                "Quando for passado um grupo válido para autoinscrição " +
                        "mas o e-mail informado já existir no grupo " +
                        "deve retornar a validação do tipo 'ValidationException'."
        );
    }

}
