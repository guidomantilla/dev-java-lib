package dev.feather.orm.sql;

import dev.feather.orm.annotation.Entity;
import dev.feather.orm.reflection.EntityReflectionHelper;

import static dev.feather.orm.sql.util.SQLUtil.fieldNameAndFieldValueHolderSequence;
import static dev.feather.orm.sql.util.SQLUtil.whereSequence;

/**
 * The Class UpdateQuerySQLBuilder.
 */
public class UpdateQuerySQLBuilder {

    /**
     * Creates the update SQL.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @param named the named
     * @return the string
     */
    public static <E> String createUpdateSQL(Class<E> clazz, boolean named) {
        Entity entity = EntityReflectionHelper.retrieveEntity(clazz);
        String sql = "UPDATE " + entity.name() +
                " SET " + fieldNameAndFieldValueHolderSequence(clazz, named, "", "", ", ") +
                " WHERE " + whereSequence(clazz, named);
        return sql;
    }
}
