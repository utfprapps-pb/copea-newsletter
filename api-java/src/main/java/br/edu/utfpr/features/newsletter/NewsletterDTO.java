package br.edu.utfpr.features.newsletter;

import br.edu.utfpr.features.email.EmailDTO;
import br.edu.utfpr.features.newsletter.email_group.NewsletterEmailGroupDTO;
import br.edu.utfpr.generic.crud.EntityId;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

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

    private Set<EmailDTO> emails = new HashSet<>();

    private Set<NewsletterEmailGroupDTO> emailGroups = new HashSet<>();

    private Boolean newsletterTemplate;

}
