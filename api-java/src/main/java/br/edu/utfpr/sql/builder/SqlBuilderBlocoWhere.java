package br.edu.utfpr.sql.builder;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Classe responsável por montar blocos de condições para o where de SQL
 */
public class SqlBuilderBlocoWhere {

    private StringBuilder bloco = new StringBuilder();

    @Getter
    private boolean existeCondicoes = false;

    @Getter
    private Map<String, Object> parameters = new HashMap<>();

    public void iniciar() {
        add("(");
        this.limparCondicoes();
    }

    /**
     * Iniciar bloco dentro de um bloco já existente
     * @param condicao
     */
    public void iniciar(SqlBuilderCondicaoWhere condicao) {
        if (existeCondicoes)
            add("\n " + condicao.getDescricao() + " ");

        iniciar();
    }

    public void finalizar() {
        this.add(")");
    }

    private void limparCondicoes() {
        existeCondicoes = false;
    }

    public void add(String value) {
        bloco.append(value);
    }

    public void addAnd(String condicao, String parameter, Object value) {
        addParameter(parameter, value);
        addAnd(condicao);
    }

    public void addAnd(SqlBuilderBlocoWhere blocoWhereOrigem) {
        this.addAnd(blocoWhereOrigem.toString());
        this.copyParameters(blocoWhereOrigem.getParameters());
    }

    public void addAnd(String condicao) {
        if (existeCondicoes)
            add("\n " + SqlBuilderCondicaoWhere.AND.getDescricao() + " ");

        add(condicao);
        existeCondicoes = true;
    }

    public void addOr(String condicao, String parameter, Object value) {
        addParameter(parameter, value);
        addOr(condicao);
    }

    public void addOr(SqlBuilderBlocoWhere blocoWhereOrigem) {
        this.addOr(blocoWhereOrigem.toString());
        this.copyParameters(blocoWhereOrigem.getParameters());
    }

    public void addOr(String condicao) {
        if (existeCondicoes)
            add("\n " + SqlBuilderCondicaoWhere.OR.getDescricao() + " ");

        add(condicao);
        existeCondicoes = true;
    }

    public void addParameter(String parameter, Object value) {
        parameters.put(parameter, value);
    }

    public void addParameters(Map<String, Object> parameters) {
        this.parameters.putAll(parameters);
    }

    public void copyParameters(Map<String, Object> parameters) {
        addParameters(parameters);
    }

    public String toString() {
        return bloco.toString();
    }

    public boolean isEmpty() {
        String blocoToString = toString().trim();
        return (blocoToString.isEmpty() ||
                Objects.equals(blocoToString, "(") ||
                Objects.equals(blocoToString, ")") ||
                Objects.equals(blocoToString, "()"));
    }

}
