package br.edu.utfpr.features.user;

import br.edu.utfpr.features.email.config.ConfigEmailService;
import br.edu.utfpr.features.email.send.SendEmailService;
import br.edu.utfpr.features.user.responses.SendEmailCodeRecoverPassword;
import br.edu.utfpr.generic.crud.GenericService;
import br.edu.utfpr.reponses.DefaultResponse;
import br.edu.utfpr.reponses.GenericResponse;
import br.edu.utfpr.features.user.recover_password.RecoverPassword;
import br.edu.utfpr.features.user.recover_password.RecoverPasswordDTO;
import br.edu.utfpr.features.user.recover_password.RecoverPasswordService;
import br.edu.utfpr.utils.DateTimeUtils;
import io.quarkus.elytron.security.common.BcryptUtil;
import org.jboss.resteasy.reactive.RestResponse;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.util.Objects;
import java.util.Random;

@RequestScoped
public class UserService extends GenericService<User, Long, UserRepository> {

    @Inject
    SendEmailService sendEmailService;

    @Inject
    RecoverPasswordService recoverPasswordService;

    @Inject
    ConfigEmailService configEmailService;

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
        User user = findByUsernameOrEmail(username, username);
        if (Objects.isNull(user))
            throwExceptionUserNotFound();

        Integer codigo = new Random().nextInt(1000000);
        recoverPasswordService.addCode(username, new RecoverPassword(username, codigo, DateTimeUtils.getCurrentDateTime()));

        sendEmailService.sendAllAtOnce(
                "Recuperação de senha",
                "O código para recuperação da sua senha no sistema de Newsletter é <b>"+codigo+"</b>.",
                configEmailService.getConfigEmailByUsernameOrEmailUser(username),
                user.getEmail());

        return new SendEmailCodeRecoverPassword("Código enviado com sucesso para o e-mail " + user.getEmail() + ".", user.getEmail());
    }

    public DefaultResponse recoverPassword(RecoverPasswordDTO recoverPasswordDTO) {
        String usernameOrEmail = recoverPasswordDTO.getUsername();
        User user = getRepository().findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (Objects.isNull(user))
            throwExceptionUserNotFound();

        RecoverPassword recoverPassword = recoverPasswordService.getCodeSentByEmail().getOrDefault(recoverPasswordDTO.getUsername(), new RecoverPassword());
        Boolean codesMatch = Objects.equals(recoverPasswordDTO.getCode(), recoverPassword.getCode());
        if (!codesMatch)
            return new DefaultResponse(RestResponse.StatusCode.BAD_REQUEST, "Código inválido.");

        updateUserNewPasswordByUsername(user, recoverPasswordDTO.getNewPassword());
        recoverPasswordService.getCodeSentByEmail().remove(recoverPasswordDTO.getUsername());
        return new DefaultResponse(RestResponse.StatusCode.OK, "Senha alterada com sucesso.");
    }

    private void updateUserNewPasswordByUsername(User user, String newPassword) {
        if (Objects.isNull(user))
            return;

        user.setPassword(newPassword);
        encryptPassword(user);
        super.update(user);
    }

    private void throwExceptionUserNotFound() {
        throw new NotFoundException("Usuário não encontrado.");
    }

}
