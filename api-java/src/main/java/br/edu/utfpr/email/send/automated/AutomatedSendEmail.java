package br.edu.utfpr.email.send.automated;

import br.edu.utfpr.shared.enums.NoYesEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "automated_send_email")
public class AutomatedSendEmail {

    @Id
    @SequenceGenerator(name = "automated_send_email_id_sequence", sequenceName = "automated_send_email_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "automated_send_email_id_sequence")
    private Long id;

    @Column(name = "date_time_sent")
    @NotNull
    private LocalDateTime dateTimeSent;

    private boolean active;

    private boolean recurrent;

    @Column(name = "day_range")
    private Integer dayRange;

    private LocalDateTime deadline;

}
