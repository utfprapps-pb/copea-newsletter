package br.edu.utfpr.utils;

import lombok.Getter;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ModelMapperUtils<T, D> {

    @Getter
    private final ModelMapper modelMapper = new ModelMapper();
    private final Class<T> entityClass;
    private final Class<D> dtoClass;

    public ModelMapperUtils(Class<T> entityClass, Class<D> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    public D convertToDto(T entity) {
        return modelMapper.map(entity, dtoClass);
    }

    public T convertToEntity(D dto) {
        return modelMapper.map(dto, entityClass);
    }

    public T convertToEntity(D dto, T entity) {
        modelMapper.map(dto, entity);
        return entity;
    }

    public List<D> convertEntityListToDtoList(List<T> entityList) {
        return entityList.stream()
                .map(entity -> modelMapper.map(entity, dtoClass))
                .collect(Collectors.toList());
    }

    public List<T> convertDtoListToEntityList(List<D> dtoList) {
        return dtoList.stream()
                .map(dto -> modelMapper.map(dto, entityClass))
                .collect(Collectors.toList());
    }

}
