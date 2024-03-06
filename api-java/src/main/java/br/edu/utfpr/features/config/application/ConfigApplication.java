package br.edu.utfpr.features.config.application;

import br.edu.utfpr.generic.crud.EntityId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "config_application")
public class ConfigApplication implements EntityId<Long> {

    @Id
    @SequenceGenerator(name = "config_application_id_sequence", sequenceName = "config_application_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "config_application_id_sequence")
    private Long id;

    @Column(name = "url_web")
    private String urlWeb;

}
