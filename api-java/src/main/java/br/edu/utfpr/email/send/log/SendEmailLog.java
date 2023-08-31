package br.edu.utfpr.email.send.log;

import br.edu.utfpr.email.send.log.enums.SendEmailLogStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "send_email_log")
public class SendEmailLog implements Serializable {

    @Id
    @SequenceGenerator(name = "send_email_log_id_sequence", sequenceName = "send_email_log_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "send_email_log_id_sequence")
    private Long id;

    @Column(name = "log_date")
    private LocalDateTime logDate;

    @Column(name = "sent_host")
    private String sentHost;

    @Column(name = "sent_port")
    private Integer sentPort;

    @Column(name = "email_from")
    private String emailFrom;

    @Column(name = "sent_emails", columnDefinition = "TEXT")
    private String sentEmails;

    @Column(name = "sent_subject")
    private String sentSubject;

    @Column(name = "sent_message", columnDefinition = "TEXT")
    private String sentMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "sent_status")
    private SendEmailLogStatusEnum sentStatus;

    @Column(columnDefinition = "TEXT")
    private String error;

    @Column(name = "message_id")
    private String messageID;

}
