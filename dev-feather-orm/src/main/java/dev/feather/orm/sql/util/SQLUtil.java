package dev.feather.orm.sql.util;

import dev.feather.orm.annotation.Attribute;
import dev.feather.orm.reflection.EntityReflectionHelper;

public class SQLUtil {

    public static <E> String fieldNameSequence(Class<E> clazz, String initChar, String endChar) {

        String string = initChar;
        string = EntityReflectionHelper.retrieveAttributeList(clazz).stream()
                .map(attribute -> attribute.name() + ", ")
                .reduce(string, String::concat);

        if (!string.isEmpty()) {
            string = string.substring(0, string.lastIndexOf(",")) + endChar;
        }

        return string;
    }

    public static <E> String fieldNameAndFieldValueHolderSequence(Class<E> clazz, boolean named,
                                                                  String initChar, String endChar, String separator) {

        String string = initChar;
        string = EntityReflectionHelper.retrieveKeyList(clazz).stream()
                .map(attribute -> {
                    if (named) {
                        return attribute.name() + " = :" + attribute.name() + separator;
                    } else {
                        return attribute.name() + " = :?" + separator;
                    }
                }).reduce(string, String::concat);

        if (!string.isEmpty()) {
            string = string.substring(0, string.lastIndexOf(separator.trim())) + endChar;
        }

        return string;
    }

    public static <E> String fieldValueHolderSequence(Class<E> clazz, boolean named,
                                                      String initChar, String endChar, String separator) {

        String string = initChar;
        string = EntityReflectionHelper.retrieveAttributeList(clazz).stream()
                .map((Attribute attribute) -> {
                    if (named) {
                        return ":" + attribute.name() + separator;
                    } else {
                        return "? " + separator;
                    }
                }).reduce(string, String::concat);

        if (!string.isEmpty()) {
            string = string.substring(0, string.lastIndexOf(",")) + endChar;
        }

        return string;
    }

    public static <E> String whereSequence(Class<E> clazz, boolean named) {
        return fieldNameAndFieldValueHolderSequence(clazz, named, "", "", " AND ");
    }
}
