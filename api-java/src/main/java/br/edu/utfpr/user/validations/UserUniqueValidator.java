package br.edu.utfpr.user.validations;

import br.edu.utfpr.user.User;
import br.edu.utfpr.user.UserService;
import br.edu.utfpr.user.dtos.UserDTO;
import br.edu.utfpr.user.validations.constraints.UserUniqueConstraint;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UserUniqueValidator implements ConstraintValidator<UserUniqueConstraint, UserDTO> {

    @Inject
    UserService userService;

    private static final String messageConstraint = "O %s informado já está sendo utilizado por outro cadastro. Por favor, informe outro.";

    @Override
    public boolean isValid(UserDTO userDTO, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();

        Boolean usernameValid = userValid(userDTO,
                Optional.ofNullable(userService.findByUsername(userDTO.getUsername())),
                constraintValidatorContext,
                String.format(messageConstraint, "username"),
                "username"

        );

        Boolean emailValid = userValid(userDTO,
                Optional.ofNullable(userService.findByEmail(userDTO.getEmail())),
                constraintValidatorContext,
                String.format(messageConstraint, "email"),
                "email"
        );


        return (usernameValid && emailValid);
    }

    private Boolean userValid(UserDTO userDTO, Optional<User> user, ConstraintValidatorContext constraintValidatorContext, String messageConstraint, String fieldName) {
        if ((user.isPresent()) && (user.get().getId() != userDTO.getId())) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(messageConstraint)
                    .addPropertyNode(fieldName)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
