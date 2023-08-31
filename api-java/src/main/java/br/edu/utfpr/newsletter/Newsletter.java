package br.edu.utfpr.newsletter;

import br.edu.utfpr.email.Email;
import br.edu.utfpr.email.send.log.SendEmailLog;
import br.edu.utfpr.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "newsletter")
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

    @Column(name = "inclusion_date", updatable = false,  nullable = true)
    @JsonIgnore
    private LocalDateTime inclusionDate;

    @JsonIgnore
    @Column(name = "alteration_date")
    private LocalDateTime alterationDate;

    @NotBlank(message = "Parameter newsletter is required.")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String newsletter;

    @ManyToMany
    @JoinTable(
            name = "newsletter_email",
            joinColumns = @JoinColumn(name = "newsletter_id"),
            inverseJoinColumns = @JoinColumn(name = "email_id")
    )
    private Set<Email> emails = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "newsletter_send_email_log",
            joinColumns = @JoinColumn(name = "newsletter_id"),
            inverseJoinColumns = @JoinColumn(name = "send_email_log_id")
    )
    private Set<SendEmailLog> sendEmailLogs = new HashSet<>();

    @ManyToOne(optional = false)
    private User user;

    @Column(name = "newsletter_template")
    private Boolean newsletterTemplate;

}
