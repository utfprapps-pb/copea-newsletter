package br.edu.utfpr.newsletter.email_group;

import br.edu.utfpr.email.group.EmailGroup;
import br.edu.utfpr.newsletter.Newsletter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(
        name = "newsletter_email_group",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_newsletter_email_group", columnNames = { "newsletter_id", "email_group_id" })
        }
)
public class NewsletterEmailGroup {

    @Id
    @SequenceGenerator(name = "newsletter_email_group_id_sequence", sequenceName = "newsletter_email_group_id_sequence", allocationSize = 1)
    @GeneratedValue(generator = "newsletter_email_group_id_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "newsletter_id")
    @JsonBackReference
    private Newsletter newsletter;

    @ManyToOne
    @JoinColumn(name = "email_group_id")
    private EmailGroup emailGroup;

}

