package dev.feather.orm.sql;

import dev.feather.orm.annotation.Entity;
import dev.feather.orm.reflection.EntityReflectionHelper;

import static dev.feather.orm.sql.util.SQLUtil.fieldNameSequence;
import static dev.feather.orm.sql.util.SQLUtil.whereSequence;

/**
 * The Class SelectQuerySQLBuilder.
 */
public class SelectQuerySQLBuilder {

    /**
     * Creates the select all SQL.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @return the string
     */
    public static <E> String createSelectAllSQL(Class<E> clazz) {

        Entity entity = EntityReflectionHelper.retrieveEntity(clazz);
        String sql = "SELECT " + fieldNameSequence(clazz, "", "")  + " FROM " + entity.name();
        return sql;
    }

    /**
     * Creates the select by key SQL.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @param named the named
     * @return the string
     */
    public static <E> String createSelectByKeySQL(Class<E> clazz, boolean named) {

        Entity entity = EntityReflectionHelper.retrieveEntity(clazz);
        String sql = "SELECT " + fieldNameSequence(clazz, "", "") + " FROM " + entity.name() +
                " WHERE " + whereSequence(clazz, named);
        return sql;
    }
}
