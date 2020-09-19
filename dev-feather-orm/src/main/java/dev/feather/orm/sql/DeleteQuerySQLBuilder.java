package dev.feather.orm.sql;

import dev.feather.orm.annotation.Entity;
import dev.feather.orm.reflection.EntityReflectionHelper;

import static dev.feather.orm.sql.util.SQLUtil.whereSequence;

/**
 * The Class DeleteQuerySQLBuilder.
 */
public class DeleteQuerySQLBuilder {

    /**
     * Creates the delete SQL.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @param named the named
     * @return the string
     */
    public static <E> String createDeleteSQL(Class<E> clazz, boolean named) {
        Entity entity = EntityReflectionHelper.retrieveEntity(clazz);
        String sql = "DELETE FROM " + entity.name() + " WHERE " + whereSequence(clazz, named);
        return sql;
    }
}
