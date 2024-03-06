package br.edu.utfpr.features.email;

import br.edu.utfpr.generic.mapstruct.GenericMapper;
import br.edu.utfpr.generic.mapstruct.GenericMapperConfig;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = GenericMapperConfig.COMPONENT_MODEL_CDI)
public interface EmailMapper extends GenericMapper<Email, EmailDTO> {

    @AfterMapping
    default void setEmail(@MappingTarget Email email) {
        /**
         * Seta o email em cada item pois está anotado como @OneToMany e é bidirecional (mappedBy),
         * sem isso o EmailGroupRelation ficaria sem o vínculo do email.
         */
        Optional.ofNullable(email.getEmailGroupRelations())
                .ifPresent(emailGroupRelations -> emailGroupRelations.forEach(emailGroupRelation -> emailGroupRelation.setEmail(email)));
    }

}
