package br.edu.utfpr.features.email.config;

import br.edu.utfpr.generic.crud.resource.GenericResource;
import jakarta.annotation.Resource;

import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("v1/email/config")
@Resource
public class ConfigEmailResource extends GenericResource<ConfigEmail, Long, ConfigEmailService> {

    @Override
    public List<ConfigEmail> get() {
        return getService().findByLoggedUser();
    }

    @Override
    public ConfigEmail getById(Long id) {
        return getService().findByIdAndUser(id);
    }

    @Override
    public Response save(@Valid ConfigEmail genericClass) {
        return super.save(genericClass);
    }

}
