package br.edu.utfpr.user.validations.constraints;

import br.edu.utfpr.user.validations.UsernameUniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = UsernameUniqueValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UsernameUniqueConstraint {

    String message() default "O usuário já existe.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
