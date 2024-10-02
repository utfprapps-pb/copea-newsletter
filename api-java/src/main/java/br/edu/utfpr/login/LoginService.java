package br.edu.utfpr.login;

import br.edu.utfpr.auth.AuthSecurityFilter;
import br.edu.utfpr.features.user.User;
import br.edu.utfpr.features.user.UserRepository;
import br.edu.utfpr.reponses.TokenResponse;
import br.edu.utfpr.roles.RoleNewsletterType;
import br.edu.utfpr.utils.TokenUtils;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.wildfly.security.password.Password;
import org.wildfly.security.password.PasswordFactory;
import org.wildfly.security.password.interfaces.BCryptPassword;
import org.wildfly.security.password.util.ModularCrypt;

import java.util.Optional;

@RequestScoped
public class LoginService {

    @Inject
    UserRepository userRepository;

    @Inject
    TokenUtils tokenUtils;

    @Inject
    AuthSecurityFilter authSecurityFilter;

    public Optional<TokenResponse> logar(LoginRequest loginRequest) throws Exception {
        Optional<TokenResponse> token = Optional.empty();

        String username = loginRequest.getUsername();
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsernameOrEmail(username, username));

        boolean gerarToken = ((userOptional.isPresent()) && (verifyPassword(loginRequest.getPassword(), userOptional.get().getPassword())));
        if (gerarToken)
            token = Optional.of(tokenUtils.generateToken(userOptional.get().getId().toString(), RoleNewsletterType.ADMIN));

        return token;
    }

    public boolean verifyPassword(String originalPwd, String encryptedPwd) throws Exception {
        // convert encrypted password string to a password key
        Password rawPassword = ModularCrypt.decode(encryptedPwd);

        // create the password factory based on the bcrypt algorithm
        PasswordFactory factory = PasswordFactory.getInstance(BCryptPassword.ALGORITHM_BCRYPT);

        // create encrypted password based on stored string
        BCryptPassword restored = (BCryptPassword) factory.translate(rawPassword);

        // verify restored password against original
        return factory.verify(restored, originalPwd.toCharArray());
    }

    public Optional<TokenResponse> refreshToken() throws Exception {
        Optional<User> userOptional = Optional.ofNullable(authSecurityFilter.getAuthUserContext().findByToken());
        if (userOptional.isEmpty())
            return Optional.empty();

        return Optional.of(tokenUtils.generateToken(userOptional.get().getId().toString(), RoleNewsletterType.ADMIN));
    }

}
