package br.edu.utfpr.auth.context;

import br.edu.utfpr.features.user.User;
import br.edu.utfpr.features.user.UserRepository;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class AuthUserContext extends AuthContextGeneric<User, Long, UserRepository> {
}
