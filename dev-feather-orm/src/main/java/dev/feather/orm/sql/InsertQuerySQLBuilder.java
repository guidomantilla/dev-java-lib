package dev.feather.orm.sql;

import dev.feather.orm.annotation.Attribute;
import dev.feather.orm.annotation.Entity;
import dev.feather.orm.reflection.EntityReflectionHelper;

import java.util.List;

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

        String sql = "INSERT INTO " + entity.name() + createInsertField(clazz) + " VALUES "
                + createInsertParam(clazz, named);

        return sql;
    }

    /**
     * Creates the insert field.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @return the string
     */
    private static <E> String createInsertField(Class<E> clazz) {

        List<Attribute> attributeList = EntityReflectionHelper.retrieveAttributeList(clazz);

        String string = "(";
        string = attributeList.stream().map((attribute) -> attribute.name() + ", ").reduce(string, String::concat);

        if (!string.isEmpty()) {
            string = string.substring(0, string.lastIndexOf(",")) + ")";
        }

        return string;
    }

    /**
     * Creates the insert param.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @param named the named
     * @return the string
     */
    private static <E> String createInsertParam(Class<E> clazz, boolean named) {

        List<Attribute> attributeList = EntityReflectionHelper.retrieveAttributeList(clazz);

        String string = "(";
        string = attributeList.stream().map((Attribute attribute) -> {
            if (named) {
                return ":" + attribute.name() + ", ";
            } else {
                return "?, ";
            }
        }).reduce(string, String::concat);

        if (!string.isEmpty()) {
            string = string.substring(0, string.lastIndexOf(",")) + ")";
        }

        return string;
    }
}
