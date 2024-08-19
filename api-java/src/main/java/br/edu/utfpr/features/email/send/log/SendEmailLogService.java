package br.edu.utfpr.features.email.send.log;

import br.edu.utfpr.features.email.send.log.enums.SendEmailLogStatusEnum;
import br.edu.utfpr.generic.crud.GenericService;
import jakarta.enterprise.context.RequestScoped;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Objects;

@RequestScoped
public class SendEmailLogService extends GenericService<SendEmailLog, Long, SendEmailLogRepository> {

    public SendEmailLog saveLog(HtmlEmail htmlEmail, String message, String error) throws MessagingException {
        return getRepository().save(convertSendEmailToSendEmailLog(htmlEmail, message, error));
    }

    public SendEmailLog convertSendEmailToSendEmailLog(HtmlEmail htmlEmail, String message, String error) throws MessagingException {
        SendEmailLog sendEmailLog = new SendEmailLog();

        sendEmailLog.setError(error);
        sendEmailLog.setSentStatus((Objects.isNull(error) || error.isEmpty()) ? SendEmailLogStatusEnum.SENT : SendEmailLogStatusEnum.NOT_SENT);
        sendEmailLog.setLogDate(LocalDateTime.now());
        sendEmailLog.setSentHost(htmlEmail.getHostName());

        if (SendEmailLogStatusEnum.SENT.equals(sendEmailLog.getSentStatus())) {
            sendEmailLog.setSentPort(Integer.valueOf(htmlEmail.getSmtpPort()));
            sendEmailLog.setMessageID(htmlEmail.getMimeMessage().getMessageID());
        }

        if (!Objects.isNull(htmlEmail.getFromAddress()))
            sendEmailLog.setEmailFrom(htmlEmail.getFromAddress().toString());

        if ((!Objects.isNull(htmlEmail.getToAddresses())) && (!htmlEmail.getToAddresses().isEmpty()))
            sendEmailLog.setSentEmails(htmlEmail.getToAddresses().toString());

        sendEmailLog.setSentSubject(htmlEmail.getSubject());
        sendEmailLog.setSentMessage(message);

        return sendEmailLog;
    }

}
