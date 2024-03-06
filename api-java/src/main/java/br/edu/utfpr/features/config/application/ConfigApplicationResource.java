package br.edu.utfpr.features.config.application;

import br.edu.utfpr.generic.crud.resource.GenericResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@RequestScoped
@Path("application/config")
public class ConfigApplicationResource extends GenericResource<ConfigApplication, Long, ConfigApplicationService> {

    @GET
    @Path("one")
    public ConfigApplication getOne() {
        return getService().getOneOrElseThrowException();
    }

}
