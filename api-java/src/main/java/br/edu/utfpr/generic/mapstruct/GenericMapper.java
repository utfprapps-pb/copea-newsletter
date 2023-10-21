package br.edu.utfpr.generic.mapstruct;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface GenericMapper<T, D> {

    T toEntity(D dto);

    D toDto(T entity);

    List<T> toEntityList(List<D> dtoList);

    List<D> toDtoList(List<T> entityList);

    void copyDtoToEntity(D dto, @MappingTarget T entity);

    void copyEntityToDto(T entity, @MappingTarget D dto);


}
