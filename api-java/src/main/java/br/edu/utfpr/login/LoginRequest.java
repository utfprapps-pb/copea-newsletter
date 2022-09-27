package br.edu.utfpr.login;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "O parâmetro username é obrigatório.")
    private String username;

    @NotBlank(message = "O parâmetro password é obrigatório.")
    private String password;

}
