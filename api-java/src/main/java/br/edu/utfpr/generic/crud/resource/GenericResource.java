package br.edu.utfpr.generic.crud.resource;

import br.edu.utfpr.generic.crud.EntityId;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.GenericResponse;
import lombok.Getter;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @param <T> Entity
 * @param <I> ID type
 * @param <S> GenericService
 */
public abstract class GenericResource<
        T extends EntityId<I>,
        I,
        S extends GenericService> {

    @Getter
    @Inject
    S service;

    @GET
    public List<T> get() {
        return service.findAll();
    }

    @GET
    @Path("{id}")
    public T getById(@PathParam("id") I id) {
        return (T) service.findById(id);
    }

    @POST
    @Transactional
    public Response save(@Valid T entity) {
        return Response.status(Response.Status.CREATED).entity(service.save(entity)).build();
    }

    @PUT
    @Transactional
    public GenericResponse update(@Valid T entity) {
        return service.update(entity);
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public GenericResponse deleteById(@PathParam("id") I id) {
        return service.deleteById(id);
    }

}
