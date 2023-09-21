package br.edu.utfpr.email.group.relation;

import br.edu.utfpr.email.Email;
import br.edu.utfpr.email.group.EmailGroup;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(
        name = "email_group_relation",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_email_group_relation", columnNames = { "email_id", "email_group_id" })
        }
)
public class EmailGroupRelation {

    @Id
    @SequenceGenerator(name = "email_group_relation_id_sequence", sequenceName = "email_group_relation_id_sequence", allocationSize = 1)
    @GeneratedValue(generator = "email_group_relation_id_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "email_id")
    @JsonBackReference
    private Email email;

    @ManyToOne
    @JoinColumn(name = "email_group_id")
    private EmailGroup emailGroup;

    @Column(name = "uuid_was_self_registration")
    private String uuidWasSelfRegistration;

}
