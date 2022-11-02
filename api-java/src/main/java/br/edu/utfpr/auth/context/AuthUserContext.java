package br.edu.utfpr.auth.context;

import br.edu.utfpr.user.User;
import br.edu.utfpr.user.UserRepository;
import lombok.Setter;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Optional;

@RequestScoped
public class AuthUserContext {

    @Inject
    UserRepository userRepository;

    @Setter
    private String username;

    public User findByToken() {
        Optional<User> userOptional = userRepository.findById(Long.parseLong(username));
        return userOptional.orElse(null);
    }

}
