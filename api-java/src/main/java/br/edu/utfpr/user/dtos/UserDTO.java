package br.edu.utfpr.user.dtos;

import br.edu.utfpr.user.validations.constraints.UserUniqueConstraint;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.security.jpa.Password;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@UserUniqueConstraint
public class UserDTO {

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
