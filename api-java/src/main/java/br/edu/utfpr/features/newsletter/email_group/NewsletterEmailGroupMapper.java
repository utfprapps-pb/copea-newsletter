package br.edu.utfpr.features.newsletter.email_group;

import br.edu.utfpr.generic.mapstruct.GenericMapper;
import br.edu.utfpr.generic.mapstruct.GenericMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = GenericMapperConfig.COMPONENT_MODEL_CDI)
public interface NewsletterEmailGroupMapper extends GenericMapper<NewsletterEmailGroup, NewsletterEmailGroupDTO> {
}
