package br.edu.utfpr.sql;

import br.edu.utfpr.exception.validation.ValidationException;
import br.edu.utfpr.utils.SQLUtils;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;

public class SQLBuilder {

    private int condicoes = 0;

    private StringBuilder sql;

    @Getter
    private Map<String, Object> parameters = new HashMap<>();

    public SQLBuilder() {
        criarSQL();
    }

    public SQLBuilder(String sql) {
        criarSQL(sql);
    }

    public SQLBuilder(String sql, boolean existeWhere) {
        if (existeWhere)
            this.condicoes = 1;
        criarSQL(sql);
    }

    private void criarSQL(String sql) {
        criarSQL();
        this.sql.append(sql);
    }

    private void criarSQL() {
        this.sql = new StringBuilder();
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

    public void addAnd(String condicao, String parameter, Object value) {
        addParameter(parameter, value);
        addAnd(condicao);
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

    public void add(String value) {
        sql.append(value);
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

    public <T> Query createQuery(EntityManager entityManager, Class<T> tClass) {
        Query query = entityManager.createQuery(sql.toString(), tClass);
        setParameters(query);
        return query;
    }

    public <T> Query createQuery(EntityManager entityManager) {
        Query query = entityManager.createQuery(sql.toString());
        setParameters(query);
        return query;
    }

    public Query createNativeQuery(EntityManager entityManager, Class aClass) {
        Query query = entityManager.createNativeQuery(sql.toString(), aClass);
        setParameters(query);
        return query;
    }

    public Query createNativeQuery(EntityManager entityManager) {
        Query query = entityManager.createNativeQuery(sql.toString());
        setParameters(query);
        return query;
    }

    private Type getType(Class classe) {
        if (classe.equals(Long.class)) {
            return LongType.INSTANCE;
        }
        if (classe.equals(Integer.class)) {
            return IntegerType.INSTANCE;
        }
        if (classe.equals(Boolean.class)) {
            return BooleanType.INSTANCE;
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

    public SQLBuilder append(String value) {
        sql.append(value);
        return this;
    }

    public void addParameters(Map<String, Object> parameters) {
        this.parameters.putAll(parameters);
    }

    public void copyParameters(SQLBuilder sqlBuilderOrigem) {
        addParameters(sqlBuilderOrigem.getParameters());
    }

    public void addAnd(String condicao, Map<String, Object> parameters) {
        addParameters(parameters);
        addAnd(condicao);
    }

    private void limparCondicoes() {
        this.condicoes = 0;
    }

    public void sqlFormat(SQLBuilder sql, Object... args) {
        this.sql = new StringBuilder(String.format(sql.toString(), args));
    }

    public static SQLBuilder file(String filePath){
        return new SQLBuilder(SQLUtils.lerSql(filePath));
    }

}
