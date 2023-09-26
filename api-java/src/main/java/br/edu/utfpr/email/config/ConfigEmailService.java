package br.edu.utfpr.email.config;

import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.GenericResponse;
import br.edu.utfpr.user.User;
import br.edu.utfpr.user.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequestScoped
public class ConfigEmailService extends GenericService<ConfigEmail, Long, ConfigEmailRepository> {

    @Inject
    UserService userService;

    @Override
    public GenericResponse save(ConfigEmail entity) {
        return saveOrUpdate(entity);
    }

    @Override
    public GenericResponse update(ConfigEmail entity) {
        return saveOrUpdate(entity);
    }

    private GenericResponse saveOrUpdate(ConfigEmail entity) {
        setDefaultValues(entity);
        return super.save(entity);
    }

    private void setDefaultValues(ConfigEmail entity) {
        if (Objects.isNull(entity.getUser()))
            entity.setUser(getAuthSecurityFilter().getAuthUserContext().findByToken());
    }

    public List<ConfigEmail> findByLoggedUser() {
        return findByUser(getAuthSecurityFilter().getAuthUserContext().findByToken());
    }

    public List<ConfigEmail> findByUsernameUser(String username) {
        return findByUser(userService.findByUsername(username));
    }

    public List<ConfigEmail> findByUser(User user) {
        List<ConfigEmail> configEmailList =
                getRepository().findByUser(user);

        if (configEmailList.isEmpty())
            return new ArrayList<>();

        return configEmailList;
    }

    public ConfigEmail findByIdAndUser(Long id) {
        Optional<ConfigEmail> configEmailOptional =
                getRepository().findByIdAndUser(id, getAuthSecurityFilter().getAuthUserContext().findByToken());

        if (configEmailOptional.isEmpty())
            throwNotFoundException();

        return configEmailOptional.get();
    }

    public ConfigEmail getConfigEmailByUsernameUser(String usernameUser) {
        return getConfigEmail(findByUsernameUser(usernameUser));
    }

    public ConfigEmail getConfigEmailByLoggedUser() {
        return getConfigEmail(findByLoggedUser());
    }

    private ConfigEmail getConfigEmail(List<ConfigEmail> configEmails) {
        if (configEmails.size() == 0)
            throwConfigEmailNotFoundException();

        return configEmails.get(0);
    }

    public ConfigEmail getOneConfigEmailByUser(User user) {
        return getConfigEmail(findByUser(user));
    }

    private void throwConfigEmailNotFoundException() {
        throw new NotFoundException("Nenhuma configuração para envio de e-mail encontrada.");
    }

}
