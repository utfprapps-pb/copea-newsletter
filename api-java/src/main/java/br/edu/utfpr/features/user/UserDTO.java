package br.edu.utfpr.features.user;

import br.edu.utfpr.generic.crud.EntityId;
import br.edu.utfpr.features.user.validations.constraints.UserUniqueConstraint;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.security.jpa.Password;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@UserUniqueConstraint
public class UserDTO implements EntityId<Long> {

    private Long id;

    @JsonProperty("fullname")
    @NotNull
    private String fullName;

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    @Password
    private String password;

}
