package br.edu.utfpr.utils;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ModelMapperUtils<T, DTO> {

    private final ModelMapper modelMapper = new ModelMapper();
    private final Class<T> entityClass;
    private final Class<DTO> dtoClass;

    public ModelMapperUtils(Class<T> entityClass, Class<DTO> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    public DTO convertToDto(T entity) {
        return modelMapper.map(entity, dtoClass);
    }

    public T convertToEntity(DTO entityDto) {
        return modelMapper.map(entityDto, entityClass);
    }

    public List<DTO> convertListToDtoList(List<T> entityList) {
        return entityList.stream()
                .map(entity -> modelMapper.map(entity, dtoClass))
                .collect(Collectors.toList());
    }

    public List<T> convertListToEntityList(List<DTO> entityDto) {
        return entityDto.stream()
                .map(dto -> modelMapper.map(dto, entityClass))
                .collect(Collectors.toList());
    }

}
