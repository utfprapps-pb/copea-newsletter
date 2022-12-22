package br.edu.utfpr.user.recover_password;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecoverPassword {

    private String username;
    private Integer code;
    private LocalDateTime dateTime;

}
