package br.edu.utfpr.features.email.send;

import br.edu.utfpr.exception.validation.ValidationException;
import br.edu.utfpr.features.email.config.ConfigEmail;
import br.edu.utfpr.features.email.send.log.SendEmailLogService;
import br.edu.utfpr.features.htmlfileswithcidinsteadbase64.HtmlFilesWithCidInsteadBase64Service;
import io.quarkus.test.InjectMock;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusComponentTest
class SendEmailServiceTest {

    @Inject
    SendEmailService sendEmailService;

    @InjectMock
    SendEmailLogService sendEmailLogService;

    @InjectMock
    HtmlFilesWithCidInsteadBase64Service htmlFilesWithCidInsteadBase64Service;

    @BeforeEach
    public void setup() {
    }

    @Test
    void test_Return_ValidationException_When_ListEmailsEmpty() throws Exception {
        ConfigEmail configEmail = new ConfigEmail();
        Assertions.assertThrows(
                ValidationException.class,
                () -> sendEmailService.sendAllAtOnce("titulo", "corpo", configEmail),
                "Quando não passar e-mail no método send, deve retornar a " +
                        "exceção do tipo ValidationException."
        );
    }

    /**
     * TODO: Não funcionou pois quando faz o htmlEmail.sendMimeMessage() tenta enviar
     * de verdade o email, e da exception pois são dados somente pra teste
     */
//    @Test
//    void test_Return_SendEmailLog_When_DataSendEmail_Valid() throws Exception {
//        SendEmailLog sendEmailLog = new SendEmailLog();
//        sendEmailLog.setSentStatus(SendEmailLogStatusEnum.SENT);
//        Mockito.when(sendEmailLogService.saveLog(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(sendEmailLog);
//
//        ConfigEmail configEmail = new ConfigEmail();
//        configEmail.setSendPort(465);
//        configEmail.setEmailFrom("emailfrom@gmail.com");
//        configEmail.setSendHost("gmail.smtp.com");
//        configEmail.setId(1L);
//        configEmail.setUser(new User());
//        SendEmailLog sendEmailLogExpected = sendEmailService.send("titulo", "corpo", configEmail, "teste@gmail.com");
//        Assertions.assertTrue(
//                Objects.equals(sendEmailLogExpected.getSentStatus(), sendEmailLog.getSentStatus()),
//                ""
//        );
//    }

}
