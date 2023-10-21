package br.edu.utfpr.generic.crud.resource.modelmapper;

import br.edu.utfpr.generic.crud.EntityId;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.GenericResponse;
import br.edu.utfpr.utils.ModelMapperUtils;
import lombok.Getter;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @param <T> Entity
 * @param <D> DTO
 * @param <I> ID type
 * @param <S> GenericService
 */
public abstract class GenericResourceDto<
        T extends EntityId<I>,
        D extends EntityId<I>,
        I,
        S extends GenericService> {

    @Getter
    private final ModelMapperUtils<T, D> modelMapperUtils;

    protected GenericResourceDto(Class<T> entityClass, Class<D> dtoClass) {
        this.modelMapperUtils = new ModelMapperUtils<>(entityClass, dtoClass);
    }

    @Getter
    @Inject
    S service;

    @GET
    public List<D> get() {
        return modelMapperUtils.convertEntityListToDtoList(service.findAll());
    }

    @GET
    @Path("{id}")
    public D getById(@PathParam("id") I id) {
        return modelMapperUtils.convertToDto((T) service.findById(id));
    }

    @POST
    @Transactional
    public Response save(@Valid D dto) {
        return Response.status(Response.Status.CREATED)
                .entity(service.save(
                                modelMapperUtils.convertToEntity(dto)
                        )
                ).build();
    }

    @PUT
    @Transactional
    public GenericResponse update(@Valid D dto) {
        return service.update(modelMapperUtils.convertToEntity(dto));
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public GenericResponse deleteById(@PathParam("id") I id) {
        return service.deleteById(id);
    }

}
