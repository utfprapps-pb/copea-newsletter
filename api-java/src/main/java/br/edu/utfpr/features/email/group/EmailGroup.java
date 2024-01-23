package br.edu.utfpr.features.email.group;

import br.edu.utfpr.generic.crud.EntityId;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@Entity
@Table(
        name = "email_group",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_email_group", columnNames = "name")
        }
)
public class EmailGroup implements EntityId<Long> {

    @Id
    @SequenceGenerator(name = "email_group_id_sequence", sequenceName = "email_group_id_sequence", allocationSize = 1)
    @GeneratedValue(generator = "email_group_id_sequence")
    private Long id;

    @NotBlank(message = "Parameter name is required.")
    @Column(nullable = false)
    private String name;

    @Column(name = "uuid_to_self_registration")
    private String uuidToSelfRegistration;

}
