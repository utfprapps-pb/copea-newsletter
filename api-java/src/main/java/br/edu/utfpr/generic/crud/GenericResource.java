package br.edu.utfpr.generic.crud;

import lombok.Getter;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

public abstract class GenericResource<T, ID, S extends GenericService> {

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
    public Response save(@Valid T genericClass) {
        return Response.status(Response.Status.CREATED).entity(service.save(genericClass)).build();
    }

    @PUT
    @Transactional
    public Response update(@Valid T genericClass) {
        return Response.ok(service.update(genericClass)).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteById(@PathParam("id") ID id) {
        return Response.ok(service.deleteById(id)).build();
    }

}
