package br.edu.utfpr.utils;

import br.edu.utfpr.exception.validation.ValidationException;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SQLUtils {

    public static String lerSql(String sql) {
        return lerSql(sql, SQLUtils.class, "");
    }

    public static String lerSql(String sql, Class<?> classe) {
        return lerSql(sql, classe, "");
    }

    public static String lerSql(String sql, Class<?> classe, String message) {
        InputStream is = null;
        try {
            is = classe.getClassLoader().getResourceAsStream(sql);
            return IOUtils.toString(is, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidationException("Não foi possível executar a consulta. " + message);
        }finally {
            try {
                is.close();
                is = null;
            } catch (Exception e) {
            }
        }
    }

}
