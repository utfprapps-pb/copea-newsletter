package br.edu.utfpr.features.newsletter;

import br.edu.utfpr.generic.mapstruct.GenericMapper;
import br.edu.utfpr.generic.mapstruct.GenericMapperConfig;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = GenericMapperConfig.COMPONENT_MODEL_CDI)
public interface NewsletterMapper extends GenericMapper<Newsletter, NewsletterDTO> {

    @AfterMapping
    default void setNewsletter(@MappingTarget Newsletter newsletter) {
        /**
         * Seta a newsletter em cada item pois está anotado como @OneToMany e é bidirecional (mappedBy),
         * sem isso o NewsletterEmailGroup ficaria sem o vínculo da newsletter.
         */
        Optional.ofNullable(newsletter.getEmailGroups())
                .ifPresent(emailGroups -> emailGroups.forEach(emailGroup -> emailGroup.setNewsletter(newsletter)));
    }

}
