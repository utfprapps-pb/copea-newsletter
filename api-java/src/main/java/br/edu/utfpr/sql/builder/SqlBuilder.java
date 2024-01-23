package br.edu.utfpr.sql.builder;

import br.edu.utfpr.exception.validation.ValidationException;
import br.edu.utfpr.utils.SQLUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.type.BasicTypeReference;
import org.hibernate.type.StandardBasicTypes;

import java.util.HashMap;
import java.util.Map;

public class SqlBuilder {

    private int condicoes = 0;

    private StringBuilder sql;

    @Getter
    private Map<String, Object> parameters = new HashMap<>();

    public SqlBuilder() {
        criarSql();
    }

    public SqlBuilder(String sql) {
        criarSql(sql);
    }

    public SqlBuilder(String sql, boolean existeWhere) {
        if (existeWhere)
            this.condicoes = 1;
        criarSql(sql);
    }

    private void criarSql(String sql) {
        criarSql();
        this.sql.append(sql);
    }

    private void criarSql() {
        this.sql = new StringBuilder();
    }

    public void add(String value) {
        sql.append(value);
    }

    public void addAnd(String condicao, String parameter, Object value) {
        addParameter(parameter, value);
        addAnd(condicao);
    }

    public void addAnd(String condicao, Map<String, Object> parameters) {
        addParameters(parameters);
        addAnd(condicao);
    }

    public void addAnd(SqlBuilderBlocoWhere blocoWhere) {
        this.addAnd(blocoWhere.toString());
        this.copyParameters(blocoWhere.getParameters());
    }

    public void addAnd(String condicao) {
        if (condicoes == 0) {
            sql.append("\n where ");
        } else {
            sql.append("\n and ");
        }
        sql.append(condicao);
        condicoes++;
    }

    public void addOr(String condicao, String parameter, Object value) {
        addParameter(parameter, value);
        addOr(condicao);
    }

    public void addOr(SqlBuilderBlocoWhere blocoWhere) {
        this.addOr(blocoWhere.toString());
        this.copyParameters(blocoWhere.getParameters());
    }

    public void addOr(String condicao) {
        if (condicoes == 0) {
            sql.append("\n where ");
        } else {
            sql.append("\n or ");
        }
        sql.append(condicao);
        condicoes++;
    }

    public void addParameter(String parameter, Object value) {
        parameters.put(parameter, value);
    }

//    public <T> QueryDto<T> createQueryDto(EntityManager em, Class<T> tClass) {
//        QueryDto<T> q = new QueryDto<>(sql.toString(), em, tClass);
//        for (Map.Entry<String,Object> entry : parameters.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
//        return q;
//    }

    public void setParameters(Query query) {
        for (Map.Entry<String,Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }

    public String toString() {
        return sql.toString();
    }

    public <T> org.hibernate.query.Query createQuery(EntityManager em, Class<T> tClass, String alias) {
        org.hibernate.query.Query q = em.unwrap(Session.class)
                .createNativeQuery(sql.toString())
                .addScalar(alias, getType(tClass));
        for (Map.Entry<String,Object> entry : parameters.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        return q;
    }

    public Query createQuery(EntityManager em) {
        Query q = em.createNativeQuery(sql.toString());
        for (Map.Entry<String,Object> entry : parameters.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        return q;
    }

    public Query createNativeQuery(EntityManager em, Class aClass) {
        Query query = em.createNativeQuery(sql.toString(), aClass);
        setParameters(query);
        return query;
    }

    public <T> Query createQuery(EntityManager em, Boolean nativeQuery) {
        if (nativeQuery)
            return createQuery(em);

        Query query = em.createQuery(sql.toString());
        setParameters(query);
        return query;
    }

    private BasicTypeReference getType(Class classe) {
        if (classe.equals(Long.class)) {
            return StandardBasicTypes.LONG;
        }
        if (classe.equals(Integer.class)) {
            return StandardBasicTypes.INTEGER;
        }
        if (classe.equals(Boolean.class)) {
            return StandardBasicTypes.BOOLEAN;
        }
        throw new ValidationException("O tipo " + classe.getSimpleName() + " Não foi definido");
    }

    public void unionAll() {
        sql.append(" union all ");
    }

    public void unionAll(boolean limparCondicoes) {
        unionAll();
        if (limparCondicoes)
            limparCondicoes();
    }

    public void startWith(String alias) {
        sql.append("with ").append(alias).append(" as (");
    }

    public void endWith() {
        sql.append(" ) ");
    }

    public void limit(Integer quantidade) {
        sql.append(" limit ").append(quantidade);
    }

    public void offset(Integer limit) {
        sql.append(" offset ").append(limit);
    }

    public SqlBuilder append(String value) {
        sql.append(value);
        return this;
    }

    public void addParameters(Map<String, Object> parameters) {
        this.parameters.putAll(parameters);
    }

    public void copyParameters(Map<String, Object> parameters) {
        addParameters(parameters);
    }

    public void addJoin(String condicao) {
        sql.append("\n" + condicao);
    }

    private void limparCondicoes() {
        this.condicoes = 0;
    }

    public void sqlFormat(SqlBuilder sql, Object... args) {
        this.sql = new StringBuilder(String.format(sql.toString(), args));
    }

    public static SqlBuilder file(String filePath){
        return new SqlBuilder(SQLUtils.lerSql(filePath));
    }

}