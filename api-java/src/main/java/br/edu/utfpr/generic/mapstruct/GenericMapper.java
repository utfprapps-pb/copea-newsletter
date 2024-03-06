package br.edu.utfpr.generic.mapstruct;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.control.DeepClone;

import java.util.List;

public interface GenericMapper<T, D> {

    T toEntity(D dto);

    D toDto(T entity);

    List<T> toEntityList(List<D> dtoList);

    List<D> toDtoList(List<T> entityList);

    /**
     * Utilizado DeepClone pois quando vem do DTO e a entidade foi carregada do banco,
     * não deve criar uma nova instância, mas sim copiar cada campo e manter a referência da entidade.
     * @param dto
     * @param entity
     */
    @BeanMapping(mappingControl = DeepClone.class)
    void copyDtoToEntity(D dto, @MappingTarget T entity);

    void copyEntityToDto(T entity, @MappingTarget D dto);

    void copyEntityToEntity(T source, @MappingTarget T target);

}
