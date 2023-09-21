package br.edu.utfpr.email.group;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Entity(name = "email_group")
public class EmailGroup {

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
