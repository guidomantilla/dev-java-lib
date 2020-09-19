package dev.feather.orm.sql;

import dev.feather.orm.annotation.Entity;
import dev.feather.orm.reflection.EntityReflectionHelper;

import static dev.feather.orm.sql.util.SQLUtil.fieldNameSequence;
import static dev.feather.orm.sql.util.SQLUtil.fieldValueHolderSequence;

/**
 * The Class InsertQuerySQLBuilder.
 */
public class InsertQuerySQLBuilder {

    /**
     * Creates the insert SQL.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @param named the named
     * @return the string
     */
    public static <E> String createInsertSQL(Class<E> clazz, boolean named) {
        Entity entity = EntityReflectionHelper.retrieveEntity(clazz);
        String sql = "INSERT INTO " + entity.name() + fieldNameSequence(clazz, "(", ")") +
                " VALUES " + fieldValueHolderSequence(clazz, named, "", "", ", ");
        return sql;
    }
}
