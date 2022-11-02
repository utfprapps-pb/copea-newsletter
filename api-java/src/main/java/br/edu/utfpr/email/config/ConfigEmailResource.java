package br.edu.utfpr.email.config;

import br.edu.utfpr.generic.crud.GenericResource;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("v1/email/config")
@Resource
public class ConfigEmailResource extends GenericResource<ConfigEmail, Long, ConfigEmailService> {

    @Override
    public Response get() {
        return Response.ok(getService().findByUser()).build();
    }

    @Override
    public Response getById(Long aLong) {
        return Response.ok(getService().findByIdAndUser(aLong)).build();
    }

}
