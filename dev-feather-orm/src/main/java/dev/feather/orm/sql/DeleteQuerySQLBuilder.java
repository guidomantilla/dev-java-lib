package dev.feather.orm.sql;

import dev.feather.orm.annotation.Attribute;
import dev.feather.orm.annotation.Entity;
import dev.feather.orm.reflection.EntityReflectionHelper;

import java.util.List;

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

        String sql = "DELETE FROM " + entity.name() + " WHERE " + createDeleteKeyWhere(clazz, named);

        return sql;
    }

    /**
     * Creates the delete key where.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @param named the named
     * @return the string
     */
    private static <E> String createDeleteKeyWhere(Class<E> clazz, boolean named) {

        List<Attribute> attributeList = EntityReflectionHelper.retrieveKeyList(clazz);

        String string = "";
        string = attributeList.stream().map((Attribute attribute) -> {
            if (named) {
                return attribute.name() + " = :" + attribute.name() + " AND ";
            } else {
                return attribute.name() + " = :? AND ";
            }
        }).reduce(string, String::concat);

        if (!string.isEmpty()) {
            string = string.substring(0, string.lastIndexOf("AND"));
        }

        return string;
    }
}
