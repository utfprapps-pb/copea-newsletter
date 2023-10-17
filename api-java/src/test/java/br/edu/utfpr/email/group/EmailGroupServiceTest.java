package br.edu.utfpr.email.group;

import br.edu.utfpr.email.group.scenarios.EmailGroupServiceTestScenario;
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
class EmailGroupServiceTest {

    private EmailGroupServiceTestScenario emailGroupServiceTestScenario = new EmailGroupServiceTestScenario();

    @InjectMocks
    EmailGroupService emailGroupService;

    @Mock
    EmailGroupRepository emailGroupRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_Return_ValidationException_When_Save_EmailGroup_Already_Exists() {
        Mockito.when(emailGroupRepository.findByName("teste")).thenReturn(
                Optional.of(emailGroupServiceTestScenario.getEmailGroup())
        );
        EmailGroup emailGroup = emailGroupServiceTestScenario.getEmailGroup();
        Assertions.assertThrows(
                ValidationException.class,
                () -> emailGroupService.save(emailGroup),
                "Quando gravar um grupo de e-mail que já existe deve " +
                        "retornar a exception do tipo 'ValidationException'."
        );
    }

    @Test
    void test_Call_MethodSave_When_Save_EmailGroup_Valid() {
        Mockito.when(emailGroupRepository.findByName("teste")).thenReturn(Optional.empty());
        emailGroupService.save(emailGroupServiceTestScenario.getEmailGroup());
        Mockito.verify(emailGroupRepository, Mockito.atLeastOnce())
                .save(Mockito.any());
    }

    @Test
    void test_Return_ValidationException_When_Update_EmailGroup_Already_Exists() {
        Mockito.when(emailGroupRepository.findByName("teste")).thenReturn(
                Optional.of(emailGroupServiceTestScenario.getEmailGroup())
        );
        EmailGroup emailGroup = emailGroupServiceTestScenario.getEmailGroup();
        Assertions.assertThrows(
                ValidationException.class,
                () -> emailGroupService.update(emailGroup),
                "Quando atualizar um grupo de e-mail que já existe deve " +
                        "retornar a exception do tipo 'ValidationException'."
        );
    }

    @Test
    void test_Call_MethodSave_When_Update_EmailGroup_Valid() {
        Mockito.when(emailGroupRepository.findByName("teste")).thenReturn(Optional.empty());
        emailGroupService.update(emailGroupServiceTestScenario.getEmailGroup());
        Mockito.verify(emailGroupRepository, Mockito.atLeastOnce())
                .save(Mockito.any());
    }

}
