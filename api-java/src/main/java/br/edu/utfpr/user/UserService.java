package br.edu.utfpr.user;

import br.edu.utfpr.email.send.SendEmailService;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.GenericResponse;
import br.edu.utfpr.user.recover_password.RecoverPassword;
import br.edu.utfpr.user.recover_password.RecoverPasswordService;
import br.edu.utfpr.user.responses.SendEmailCodeRecoverPassword;
import br.edu.utfpr.utils.DateTimeUtil;
import io.quarkus.elytron.security.common.BcryptUtil;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.Objects;
import java.util.Random;

@RequestScoped
public class UserService extends GenericService<User, Long, UserRepository> {

    @Inject
    SendEmailService sendEmailService;

    @Inject
    RecoverPasswordService recoverPasswordService;

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

    public SendEmailCodeRecoverPassword sendEmailCodeRecoverPassword(String username) throws Exception {
        User user = getRepository().findByUsername(username);
        if (Objects.isNull(user))
            throw new NotFoundException("Usuário não encontrado.");

        Integer codigo = new Random().nextInt(1000000);
        recoverPasswordService.addCode(username, new RecoverPassword(username, codigo, DateTimeUtil.getCurrentDateTime()));
        sendEmailService.send("Recuperação de senha", "O código para recuperação da sua senha no sistema de Newsletter é <b>"+codigo+"</b>.", user.getEmail());

        return new SendEmailCodeRecoverPassword("Código enviado com sucesso.", user.getEmail());
    }

}
