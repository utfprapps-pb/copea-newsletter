package br.edu.utfpr.email.config.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "configemail")
@NamedQuery(name = "ConfigEmailEntity.findAll",
            query = "SELECT ce FROM configemail ce",
            hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
public class ConfigEmailEntity {

    @Id
    @SequenceGenerator(name = "configemail_id_sequence", sequenceName = "configemail_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "configemail_id_sequence")
    private Long id;
    private String emailFrom;
    private String passwordEmailFrom;
    private String sendHost;
    private Integer sendPort;

}
