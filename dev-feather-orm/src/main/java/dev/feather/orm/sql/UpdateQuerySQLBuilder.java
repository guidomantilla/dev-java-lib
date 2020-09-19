package dev.feather.orm.sql;

import dev.feather.orm.annotation.Attribute;
import dev.feather.orm.annotation.Entity;
import dev.feather.orm.reflection.EntityReflectionHelper;

import java.util.List;

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

        String sql = "UPDATE " + entity.name() + " SET " + createUpdateSet(clazz, named) + " WHERE "
                + createUpdateKeyWhere(clazz, named);

        return sql;
    }

    /**
     * Creates the update set.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @param named the named
     * @return the string
     */
    private static <E> String createUpdateSet(Class<E> clazz, boolean named) {

        List<Attribute> attributeList = EntityReflectionHelper.retrieveAttributeList(clazz);

        String string = "";
        string = attributeList.stream().map((Attribute attribute) -> {
            if (named) {
                return attribute.name() + " = :" + attribute.name() + ", ";
            } else {
                return attribute.name() + " = :?, ";
            }
        }).reduce(string, String::concat);

        if (!string.isEmpty()) {
            string = string.substring(0, string.lastIndexOf(","));
        }

        return string;
    }

    /**
     * Creates the update key where.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @param named the named
     * @return the string
     */
    private static <E> String createUpdateKeyWhere(Class<E> clazz, boolean named) {

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
