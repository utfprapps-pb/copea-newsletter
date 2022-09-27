package br.edu.utfpr.newsletter;

import br.edu.utfpr.email.email.Email;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "newsletter")
public class Newsletter {

    @Id
    @SequenceGenerator(name = "newsletter_id_sequence", sequenceName = "newsletter_id_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "newsletter_id_sequence")
    private Long id;

    @NotBlank(message = "Parameter description is required.")
    @Column(length = 2000, nullable = false)
    private String description;

    @NotBlank(message = "Parameter subject is required.")
    @Column(length = 2000, nullable = false)
    private String subject;

    @Column(updatable = false,  nullable = true)
    @JsonIgnore
    private LocalDateTime inclusionDate;

    @JsonIgnore
    private LocalDateTime alterationDate;

    @NotBlank(message = "Parameter newsletter is required.")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String newsletter;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "newsletter_email",
            joinColumns = @JoinColumn(name = "newsletter_id"),
            inverseJoinColumns = @JoinColumn(name = "email_id")
    )
    private Set<Email> emails = new HashSet<>();

}
