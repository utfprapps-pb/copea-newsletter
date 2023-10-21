package br.edu.utfpr.features.user;

import br.edu.utfpr.generic.mapstruct.GenericMapper;
import br.edu.utfpr.generic.mapstruct.GenericMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = GenericMapperConfig.COMPONENT_MODEL_CDI)
public interface UserMapper extends GenericMapper<User, UserDTO> {
}
