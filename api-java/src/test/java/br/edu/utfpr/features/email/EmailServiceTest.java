package br.edu.utfpr.features.email;

import br.edu.utfpr.exception.validation.ValidationException;
import br.edu.utfpr.features.email.group.EmailGroup;
import br.edu.utfpr.features.email.group.EmailGroupService;
import br.edu.utfpr.features.email.group.relation.EmailGroupRelation;
import br.edu.utfpr.features.email.request.EmailSelfRegistrationRequest;
import br.edu.utfpr.features.email.scenarios.EmailServiceTestScenario;
import br.edu.utfpr.shared.enums.NoYesEnum;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

@QuarkusComponentTest
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
        EmailGroup emailGroup = emailServiceTestScenario.getEmailGroup();
        Email email = emailServiceTestScenario.getEmail();
        email.setSubscribed(NoYesEnum.YES);
        EmailGroupRelation emailGroupRelation = new EmailGroupRelation();
        emailGroupRelation.setEmail(email);
        emailGroupRelation.setEmailGroup(emailGroup);
        email.setEmailGroupRelations(List.of(emailGroupRelation));

        Mockito.when(emailGroupService.findByUuidToSelfRegistration(Mockito.any()))
                .thenReturn(Optional.of(emailGroup));
        Mockito.when(emailRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(email));

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
