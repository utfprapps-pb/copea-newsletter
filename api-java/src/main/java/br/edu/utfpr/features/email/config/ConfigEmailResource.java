package br.edu.utfpr.features.email.config;

import br.edu.utfpr.generic.crud.resource.GenericResource;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
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
