package br.edu.utfpr.generic.crud;

import br.edu.utfpr.utils.ModelMapperUtils;
import lombok.Getter;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public abstract class GenericResource<T, DTO, ID, S extends GenericService> {

    private final ModelMapperUtils<T, DTO> modelMapperUtils;

    public GenericResource(Class<T> entityClass, Class<DTO> dtoClass) {
        this.modelMapperUtils = new ModelMapperUtils<>(entityClass, dtoClass);
    }

    @Getter
    @Inject
    S service;

    @GET
    public Response get() {
        return Response.ok(service.findAll()).build();
    }

    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") ID id) {
        return Response.ok(service.findById(id)).build();
    }

    @POST
    @Transactional
    public Response save(@Valid DTO requestBody) {
        return Response.status(Response.Status.CREATED)
                .entity(service.save(
                            modelMapperUtils.convertToEntity(requestBody)
                        )
                ).build();
    }

    @PUT
    @Transactional
    public Response update(@Valid DTO requestBody) {
        return Response.ok(service.update(modelMapperUtils.convertToEntity(requestBody))).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteById(@PathParam("id") ID id) {
        return Response.ok(service.deleteById(id)).build();
    }

}
