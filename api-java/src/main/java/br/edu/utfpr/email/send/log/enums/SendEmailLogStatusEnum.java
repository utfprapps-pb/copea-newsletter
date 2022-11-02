package br.edu.utfpr.email.send.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum SendEmailLogStatusEnum {

    NOT_SENT("Não enviado"),
    SENT("Enviado");

    @Getter
    private String value;

}
