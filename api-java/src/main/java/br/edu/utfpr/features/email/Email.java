package br.edu.utfpr.features.email;

import br.edu.utfpr.features.email.group.relation.EmailGroupRelation;
import br.edu.utfpr.generic.crud.EntityId;
import br.edu.utfpr.shared.enums.NoYesEnum;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

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
public class Email implements EntityId<Long> {

    public static final String URL_TO_SELF_REGISTRATION_KEY = "${URL_TO_SELF_REGISTRATION}";

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

    @Column(name = "uuid_to_unsubscribe")
    private String uuidToUnsubscribe;

    @Column(name = "unsubscribe_reason")
    private String unsubscribeReason;

    @OneToMany(mappedBy = "email", cascade = { CascadeType.MERGE, CascadeType.PERSIST }, orphanRemoval = true)
    @JsonManagedReference
    private List<EmailGroupRelation> emailGroupRelations;

}
