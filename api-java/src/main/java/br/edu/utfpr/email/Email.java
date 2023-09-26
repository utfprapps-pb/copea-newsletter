package br.edu.utfpr.email;

import br.edu.utfpr.email.group.relation.EmailGroupRelation;
import br.edu.utfpr.shared.enums.NoYesEnum;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(
        name = "email",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_email", columnNames = "email")
        }
)
public class Email {

    @Id
    @SequenceGenerator(name = "email_id_sequence", sequenceName = "email_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "email_id_sequence")
    private Long id;

    @NotBlank(message = "Parameter email is required.")
    @Column(nullable = false)
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private NoYesEnum subscribed;

    @Column(name = "last_unsubscribed_date")
    private LocalDateTime lastUnsubscribedDate;

    @Column(name = "last_email_unsubscribed_date")
    private LocalDateTime lastEmailUnsubscribedDate;

    @Column(name = "last_email_unsubscribed_message_id")
    private String lastEmailUnsubscribedMessageID;

    @OneToMany(mappedBy = "email", cascade = { CascadeType.MERGE, CascadeType.PERSIST }, orphanRemoval = true)
    @JsonManagedReference
    private List<EmailGroupRelation> emailGroupRelations;

}
