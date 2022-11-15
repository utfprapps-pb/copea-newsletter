package br.edu.utfpr.user;

import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.GenericResponse;
import io.quarkus.elytron.security.common.BcryptUtil;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class UserService extends GenericService<User, Long, UserRepository> {

    @Override
    public GenericResponse save(User entity) {
        encryptPassword(entity);
        return super.save(entity);
    }

    @Override
    public GenericResponse update(User entity) {
        encryptPassword(entity);
        return super.update(entity);
    }

    private void encryptPassword(User entity) {
        entity.setPassword(BcryptUtil.bcryptHash(entity.getPassword()));
    }

    public User findByUsername(String username) {
        return getRepository().findByUsername(username);
    }

    public User findByEmail(String email) {
        return getRepository().findByEmail(email);
    }

    public User findByUsernameOrEmail(String username, String email) {
        return getRepository().findByUsernameOrEmail(username, email);
    }

}
