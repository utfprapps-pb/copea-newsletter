package br.edu.utfpr.auth.context;

import br.edu.utfpr.user.User;
import br.edu.utfpr.user.UserRepository;
import lombok.Setter;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Optional;

@RequestScoped
public class AuthUserContext extends AuthContextGeneric<User, Long, UserRepository> {
}
