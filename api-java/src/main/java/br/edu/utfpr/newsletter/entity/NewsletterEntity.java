package br.edu.utfpr.newsletter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "newsletter")
@NamedQuery(name = "NewsletterEntity.findAll",
            query = "SELECT nl FROM newsletter nl",
            hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
public class NewsletterEntity {
    @Id
    @SequenceGenerator(name = "newsletter_id_sequence", sequenceName = "newsletter_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "newsletter_id_sequence")
    private Long id;
    @Column(length = 2000)
    private String descricao;
    @Column(updatable = false)
    @JsonIgnore
    private LocalDateTime dataInclusao;
    @JsonIgnore
    private LocalDateTime dataAlteracao;
    @Column(columnDefinition = "TEXT")
    private String newsletter;
}
