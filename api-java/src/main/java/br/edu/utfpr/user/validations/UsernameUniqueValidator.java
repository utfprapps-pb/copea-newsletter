package br.edu.utfpr.user.validations;

import br.edu.utfpr.http.HttpServerRequestFilter;
import br.edu.utfpr.user.UserRepository;
import br.edu.utfpr.user.UserService;
import br.edu.utfpr.user.validations.constraints.UsernameUniqueConstraint;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.ws.rs.core.Context;
import java.util.Optional;

public class UsernameUniqueValidator implements ConstraintValidator<UsernameUniqueConstraint, String> {

    @Inject
    UserService userService;

    @Inject
    HttpServerRequestFilter httpServerRequestFilter;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        Boolean userExists = (Optional.ofNullable(userService.findByUsername(username)).isPresent());
        return ((!userExists));
    }

//    private Boolean updatingUser() {
//        return httpServerRequestFilter.getRequest().method().equals(HttpMethod.PUT);
//    }

}
