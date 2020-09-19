package dev.feather.orm.reflection;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.feather.orm.annotation.Attribute;
import dev.feather.orm.annotation.Entity;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.util.StringUtils;

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

        if (entity == null) {
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

        if (attributeList == null) {
            attributeList = new ArrayList<>(0);

            List<Attribute> attributeListTemp = retrieveAttributeList(clazz);

            for (Attribute attribute : attributeListTemp) {

                if (attribute.key()) {

                    attributeList.add(attribute);
                }
            }

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

        if (attributeList == null) {
            attributeList = new ArrayList<>(0);

            List<Field> fieldList = _retrieveFieldList(clazz);

            for (Field field : fieldList) {

                Attribute attribute = field.getAnnotation(Attribute.class);

                if (attribute != null) {

                    attributeList.add(attribute);
                }
            }

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

        List<Method> methodList = GET_METHOD_LIST_MAP.get(clazz.getSimpleName());

        if (methodList == null) {

            methodList = new ArrayList<>(0);

            List<Field> fieldList = _retrieveFieldList(clazz);

            for (Field field : fieldList) {

                Attribute attribute = field.getAnnotation(Attribute.class);

                if (attribute != null) {

                    String name = "get" + StringUtils.capitalize(field.getName());
                    Method method = MethodUtils.getAccessibleMethod(clazz, name, new Class[0]);

                    if (method != null) {
                        methodList.add(method);
                    }
                }
            }

            GET_METHOD_LIST_MAP.put(clazz.getSimpleName(), methodList);
        }

        return methodList;
    }

    /**
     * Retrieve SET method list.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @return the list
     */
    public static <E> List<Method> retrieveSETMethodList(Class<E> clazz) {

        List<Method> methodList = SET_METHOD_LIST_MAP.get(clazz.getSimpleName());

        if (methodList == null) {
            methodList = new ArrayList<>(0);

            List<Field> fieldList = _retrieveFieldList(clazz);

            for (Field field : fieldList) {

                Attribute attribute = field.getAnnotation(Attribute.class);

                if (attribute != null) {

                    String name = "set" + StringUtils.capitalize(field.getName());
                    Method method = MethodUtils.getAccessibleMethod(clazz, name, field.getType());

                    methodList.add(method);
                }
            }

            SET_METHOD_LIST_MAP.put(clazz.getSimpleName(), methodList);
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
