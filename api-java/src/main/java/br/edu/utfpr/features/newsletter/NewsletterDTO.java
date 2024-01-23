package br.edu.utfpr.features.newsletter;

import br.edu.utfpr.features.email.Email;
import br.edu.utfpr.features.newsletter.email_group.NewsletterEmailGroup;
import br.edu.utfpr.generic.crud.EntityId;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class NewsletterDTO implements EntityId<Long> {

    private Long id;

    @NotBlank(message = "Parameter description is required.")
    private String description;

    @NotBlank(message = "Parameter subject is required.")
    private String subject;

    @NotBlank(message = "Parameter newsletter is required.")
    private String newsletter;

    private Set<Email> emails = new HashSet<>();

    private Set<NewsletterEmailGroup> emailGroups = new HashSet<>();

    private Boolean newsletterTemplate;

}
