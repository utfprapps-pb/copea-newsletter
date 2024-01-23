package br.edu.utfpr.generic.crud.resource.mapstruct;

import br.edu.utfpr.generic.crud.EntityId;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.generic.mapstruct.GenericMapper;
import br.edu.utfpr.reponses.GenericResponse;
import lombok.Getter;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

/**
 * @param <T> Entity
 * @param <D> DTO
 * @param <M> GenericMapper
 * @param <I> ID type
 * @param <S> GenericService
 */
public abstract class GenericResourceDto<
        T extends EntityId<I>,
        D extends EntityId<I>,
        M extends GenericMapper<T, D>,
        I,
        S extends GenericService> {

    @Getter
    @Inject
    M genericMapper;

    @Getter
    @Inject
    S service;

    @GET
    public List<D> get() {
        return genericMapper.toDtoList(service.findAll());
    }

    @GET
    @Path("{id}")
    public D getById(@PathParam("id") I id) {
        return genericMapper.toDto((T) service.findById(id));
    }

    @POST
    @Transactional
    public Response save(@Valid D dto) {
        T entity = getEntityFromDtoById(dto);
        return Response.status(Response.Status.CREATED).entity(service.save(entity)).build();
    }

    @PUT
    @Transactional
    public GenericResponse update(@Valid D dto) {
        T entity = getEntityFromDtoById(dto);
        return service.update(entity);
    }

    /**
     * Caso não possuir o id no DTO, somente é feito a conversão, caso existir,
     * buscar o registro do banco para depois realizar a conversão,
     * isso serve para evitar de setar nulo no banco para os campos
     * que existem somente na entidade e não no DTO.
     * @param dto
     * @return
     */
    public T getEntityFromDtoById(D dto) {
        if (Objects.isNull(dto.getId()))
            return genericMapper.toEntity(dto);
        else {
            T entityDatabase = (T) service.findById(dto.getId());
            genericMapper.copyDtoToEntity(dto, entityDatabase);
            return entityDatabase;
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public GenericResponse deleteById(@PathParam("id") I id) {
        return service.deleteById(id);
    }

}
