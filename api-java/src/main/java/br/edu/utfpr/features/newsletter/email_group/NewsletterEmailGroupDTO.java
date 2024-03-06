package br.edu.utfpr.features.newsletter.email_group;

import br.edu.utfpr.features.email.group.EmailGroup;
import br.edu.utfpr.features.newsletter.Newsletter;
import br.edu.utfpr.generic.crud.EntityId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsletterEmailGroupDTO implements EntityId<Long> {

    private Long id;
    private Newsletter newsletter;
    private EmailGroup emailGroup;

}
