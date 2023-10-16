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
public class EmailGroupServiceTest {

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
    public void test_Return_ValidationException_When_Save_EmailGroup_Already_Exists() {
        Mockito.when(emailGroupRepository.findByName("teste")).thenReturn(
                Optional.of(emailGroupServiceTestScenario.getEmailGroup())
        );
        Assertions.assertThrows(
                ValidationException.class,
                () -> emailGroupService.save(emailGroupServiceTestScenario.getEmailGroup()),
                "Quando gravar um grupo de e-mail que já existe deve " +
                        "retornar a exception do tipo 'ValidationException'."
        );
    }

    @Test
    public void test_Call_MethodSave_When_Save_EmailGroup_Valid() {
        Mockito.when(emailGroupRepository.findByName("teste")).thenReturn(Optional.empty());
        emailGroupService.save(emailGroupServiceTestScenario.getEmailGroup());
        Mockito.verify(emailGroupRepository, Mockito.atLeastOnce())
                .save(Mockito.any());
    }

    @Test
    public void test_Return_ValidationException_When_Update_EmailGroup_Already_Exists() {
        Mockito.when(emailGroupRepository.findByName("teste")).thenReturn(
                Optional.of(emailGroupServiceTestScenario.getEmailGroup())
        );
        Assertions.assertThrows(
                ValidationException.class,
                () -> emailGroupService.update(emailGroupServiceTestScenario.getEmailGroup()),
                "Quando atualizar um grupo de e-mail que já existe deve " +
                        "retornar a exception do tipo 'ValidationException'."
        );
    }

    @Test
    public void test_Call_MethodSave_When_Update_EmailGroup_Valid() {
        Mockito.when(emailGroupRepository.findByName("teste")).thenReturn(Optional.empty());
        emailGroupService.update(emailGroupServiceTestScenario.getEmailGroup());
        Mockito.verify(emailGroupRepository, Mockito.atLeastOnce())
                .save(Mockito.any());
    }

}
