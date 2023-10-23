package br.edu.utfpr.sql.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum SqlBuilderCondicaoWhere {
    AND("and"),
    OR("or");

    @Getter
    private String descricao;
}
