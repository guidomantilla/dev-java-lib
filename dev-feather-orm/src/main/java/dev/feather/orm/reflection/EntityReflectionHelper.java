package dev.feather.orm.reflection;


import dev.feather.orm.annotation.Attribute;
import dev.feather.orm.annotation.Entity;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The Class EntityReflectionHelper.
 */
public class EntityReflectionHelper {

    /**
     * The Constant ENTITY_MAP.
     */
    private static final Map<String, Entity> ENTITY_MAP = new HashMap<>();

    /**
     * The Constant KEY_LIST_MAP.
     */
    private static final Map<String, List<Attribute>> KEY_LIST_MAP = new HashMap<>();

    /**
     * The Constant ATTRIBUTTE_LIST_MAP.
     */
    private static final Map<String, List<Attribute>> ATTRIBUTTE_LIST_MAP = new HashMap<>();

    /**
     * The Constant GET_METHOD_LIST_MAP.
     */
    private static final Map<String, List<Method>> GET_METHOD_LIST_MAP = new HashMap<>();

    /**
     * The Constant SET_METHOD_LIST_MAP.
     */
    private static final Map<String, List<Method>> SET_METHOD_LIST_MAP = new HashMap<>();

    /**
     * Retrieve entity.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @return the entity
     */
    public static <E> Entity retrieveEntity(Class<E> clazz) {

        Entity entity = ENTITY_MAP.get(clazz.getSimpleName());

        if (Objects.isNull(entity)) {
            entity = clazz.getAnnotation(Entity.class);
            ENTITY_MAP.put(clazz.getSimpleName(), entity);
        }

        return entity;
    }

    /**
     * Retrieve key list.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @return the list
     */
    public static <E> List<Attribute> retrieveKeyList(Class<E> clazz) {

        List<Attribute> attributeList = KEY_LIST_MAP.get(clazz.getSimpleName());

        if (Objects.isNull(attributeList)) {

            List<Attribute> attributeListTemp = retrieveAttributeList(clazz);
            attributeList = attributeListTemp.stream()
                    .filter(Attribute::key)
                    .collect(Collectors.toList());

            KEY_LIST_MAP.put(clazz.getSimpleName(), attributeList);
        }

        return attributeList;
    }

    /**
     * Retrieve attribute list.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @return the list
     */
    public static <E> List<Attribute> retrieveAttributeList(Class<E> clazz) {

        List<Attribute> attributeList = ATTRIBUTTE_LIST_MAP.get(clazz.getSimpleName());

        if (Objects.isNull(attributeList)) {
            List<Field> fieldList = _retrieveFieldList(clazz);
            attributeList = fieldList.stream()
                    .map(field -> field.getAnnotation(Attribute.class))
                    .filter(attribute -> !Objects.isNull(attribute))
                    .collect(Collectors.toList());

            ATTRIBUTTE_LIST_MAP.put(clazz.getSimpleName(), attributeList);
        }
        return attributeList;
    }

    /**
     * Retrieve GET method list.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @return the list
     */
    public static <E> List<Method> retrieveGETMethodList(Class<E> clazz) {
        return _retrieveXXXMethodList(clazz, "get", GET_METHOD_LIST_MAP);
    }

    /**
     * Retrieve SET method list.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @return the list
     */
    public static <E> List<Method> retrieveSETMethodList(Class<E> clazz) {
        return _retrieveXXXMethodList(clazz, "set", SET_METHOD_LIST_MAP);
    }

    /**
     * @param clazz
     * @param xxxMethod
     * @param methodMapHolder
     * @param <E>
     * @return
     */
    private static <E> List<Method> _retrieveXXXMethodList(Class<E> clazz, String xxxMethod, Map<String, List<Method>> methodMapHolder) {

        List<Method> methodList = methodMapHolder.get(clazz.getSimpleName());

        if (Objects.isNull(methodList)) {

            List<Field> fieldList = _retrieveFieldList(clazz);
            methodList = fieldList.stream()
                    .map(field -> {
                        Attribute attribute = field.getAnnotation(Attribute.class);
                        if (!Objects.isNull(attribute)) {
                            String name = xxxMethod + StringUtils.capitalize(field.getName());
                            return MethodUtils.getAccessibleMethod(clazz, name, field.getType());
                        }
                        return null;
                    })
                    .filter(method -> !Objects.isNull(method))
                    .collect(Collectors.toList());

            methodMapHolder.put(clazz.getSimpleName(), methodList);
        }

        return methodList;
    }

    /**
     * Retrieve field list.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @return the list
     */
    private static <E> List<Field> _retrieveFieldList(Class<E> clazz) {

        Field[] fieldArray = clazz.getDeclaredFields();
        return Arrays.asList(fieldArray);
    }
}
