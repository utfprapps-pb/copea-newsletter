package br.edu.utfpr.shared.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum NoYesEnum {

    NO("Não"),
    YES("Sim");

    @Getter
    private String value;

}
