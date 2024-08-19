package br.edu.utfpr.features.user.validations;

import br.edu.utfpr.features.user.User;
import br.edu.utfpr.features.user.UserService;
import br.edu.utfpr.features.user.UserDTO;
import br.edu.utfpr.features.user.validations.constraints.UserUniqueConstraint;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
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
