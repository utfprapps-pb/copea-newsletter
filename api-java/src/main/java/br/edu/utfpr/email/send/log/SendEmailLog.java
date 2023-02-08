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

    private LocalDateTime logDate;

    private String sentHost;

    private Integer sentPort;

    private String emailFrom;

    @Column(columnDefinition = "TEXT")
    private String sentEmails;

    private String sentSubject;

    @Column(columnDefinition = "TEXT")
    private String sentMessage;

    @Enumerated(EnumType.STRING)
    private SendEmailLogStatusEnum sentStatus;

    @Column(columnDefinition = "TEXT")
    private String error;

    private String messageID;

}
