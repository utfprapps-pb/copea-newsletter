package br.edu.utfpr.features.email.config;

import br.edu.utfpr.generic.crud.EntityId;
import br.edu.utfpr.features.user.User;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(
        name = "config_email",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_config_email_user", columnNames = "user_id")
        }
)
public class ConfigEmail implements EntityId<Long> {

    @Id
    @SequenceGenerator(name = "configemail_id_sequence", sequenceName = "configemail_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "configemail_id_sequence")
    private Long id;

    @NotBlank(message = "Parameter emailFrom is required.")
    @Column(name = "email_from")
    private String emailFrom;

    @NotBlank(message = "Parameter passwordEmailFrom is required.")
    @Column(name = "password_email_from")
    private String passwordEmailFrom;

    @NotBlank(message = "Parameter sendHost is required.")
    @Column(name = "send_host")
    private String sendHost;

    @NotNull(message = "Parameter sendPort is required.")
    @Column(name = "send_port")
    private Integer sendPort;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

}
