package br.edu.utfpr.newsletter.entity;

import br.edu.utfpr.email.email.entity.EmailEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "newsletter")
public class NewsletterEntity {
    @Id
    @SequenceGenerator(name = "newsletter_id_sequence", sequenceName = "newsletter_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "newsletter_id_sequence")
    private Long id;
    @Column(length = 2000, nullable = false)
    private String descricao;
    @Column(updatable = false, nullable = false)
    @JsonIgnore
    private LocalDateTime dataInclusao;
    @JsonIgnore
    private LocalDateTime dataAlteracao;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String newsletter;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "newsletter_email",
            joinColumns = @JoinColumn(name = "newsletter_id"),
            inverseJoinColumns = @JoinColumn(name = "email_id")
    )
    private Set<EmailEntity> emails = new HashSet<>();
}
