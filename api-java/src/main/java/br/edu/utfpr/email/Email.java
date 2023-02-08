package br.edu.utfpr.email;

import br.edu.utfpr.emailgroup.EmailGroup;
import br.edu.utfpr.shared.enums.NoYesEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity(name = "email")
public class Email {

    @Id
    @SequenceGenerator(name = "email_id_sequence", sequenceName = "email_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "email_id_sequence")
    private Long id;

    @NotBlank(message = "Parameter email is required.")
    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private NoYesEnum subscribed;

    @Column(name = "unsubscribed_date")
    private LocalDateTime unsubscribedDate;

    @ManyToMany
    @JoinTable(
            name = "email_group_email",
            joinColumns = { @JoinColumn(name = "email_id") },
            inverseJoinColumns = { @JoinColumn(name = "email_group_id") }
    )
    private List<EmailGroup> groups;

}
