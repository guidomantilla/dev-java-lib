package dev.feather.orm.sql;

import dev.feather.orm.annotation.Attribute;
import dev.feather.orm.annotation.Entity;
import dev.feather.orm.reflection.EntityReflectionHelper;

import java.util.List;

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

        String sql = "SELECT " + createSelectField(clazz) + " FROM " + entity.name();

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

        String sql = "SELECT " + createSelectField(clazz) + " FROM " + entity.name() + " WHERE " + createSelectKeyWhere(clazz, named);

        return sql;
    }

    /**
     * Creates the select key where.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @param named the named
     * @return the string
     */
    private static <E> String createSelectKeyWhere(Class<E> clazz, boolean named) {

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

    /**
     * Creates the select field.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @return the string
     */
    private static <E> String createSelectField(Class<E> clazz) {

        List<Attribute> attributeList = EntityReflectionHelper.retrieveAttributeList(clazz);

        String string = "";
        string = attributeList.stream().map((attribute) -> attribute.name() + ", ").reduce(string, String::concat);

        if (!string.isEmpty()) {
            string = string.substring(0, string.lastIndexOf(",")) + "";
        }

        return string;
    }
}
