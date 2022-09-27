package br.edu.utfpr.email.config;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Entity(name = "configemail")
@NamedQuery(name = "ConfigEmailEntity.findAll",
            query = "SELECT ce FROM configemail ce",
            hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
public class ConfigEmail {

    @Id
    @SequenceGenerator(name = "configemail_id_sequence", sequenceName = "configemail_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "configemail_id_sequence")
    private Long id;

    @NotBlank(message = "Parameter emailFrom is required.")
    private String emailFrom;

    @NotBlank(message = "Parameter passwordEmailFrom is required.")
    private String passwordEmailFrom;

    @NotBlank(message = "Parameter sendHost is required.")
    private String sendHost;

    @NotBlank(message = "Parameter sendPort is required.")
    private Integer sendPort;

}
