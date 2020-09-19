package dev.feather.orm.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dev.feather.orm.annotation.Attribute;
import dev.feather.orm.reflection.EntityReflectionHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * The Class EntityQueryHelper.
 */
public class EntityQueryHelper {

    /**
     * Convert.
     *
     * @param <E> the element type
     * @param e   the e
     * @return the map
     * @throws RuntimeException the runtime exception
     */
    public static <E> Map<String, Object> convert(E e) throws RuntimeException {

        Map<String, Object> map = new LinkedHashMap<>(0);

        try {

            List<Method> methodList = EntityReflectionHelper.retrieveGETMethodList(e.getClass());

            for (Method method : methodList) {

                String name = StringUtils.uncapitalize(method.getName().substring(3, method.getName().length()));
                Field field = FieldUtils.getDeclaredField(e.getClass(), name, true);
                Attribute attribute = field.getAnnotation(Attribute.class);

                map.put(attribute.name(), method.invoke(e, new Object[]{}));

            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }

        return map;
    }

    /**
     * Convert.
     *
     * @param <E>       the element type
     * @param clazz     the clazz
     * @param resultSet the result set
     * @return the e
     * @throws RuntimeException the runtime exception
     */
    public static <E> E convert(Class<E> clazz, ResultSet resultSet) throws RuntimeException {

        E e = null;

        try {

            e = clazz.newInstance();

            List<Method> methodList = EntityReflectionHelper.retrieveSETMethodList(e.getClass());

            for (Method method : methodList) {

                Object value = null;

                String fieldName = StringUtils.uncapitalize(method.getName().substring(3, method.getName().length()));
                Field field = FieldUtils.getDeclaredField(e.getClass(), fieldName, true);
                Attribute attribute = field.getAnnotation(Attribute.class);

                Class<?> parameterType = method.getParameterTypes()[0];
                // Date
                if (parameterType.equals(Date.class)) {
                    value = resultSet.getTimestamp(attribute.name());
                }

                // Boolean
                if (parameterType.equals(Boolean.class)) {
                    value = resultSet.getBoolean(attribute.name());
                }

                //Long
                if (parameterType.equals(Long.class)) {
                    value = resultSet.getLong(attribute.name());
                }

                //Integer
                if (parameterType.equals(Integer.class)) {
                    value = resultSet.getInt(attribute.name());
                }

                //Double
                if (parameterType.equals(Double.class)) {
                    value = resultSet.getDouble(attribute.name());
                }

                // Float
                if (parameterType.equals(Float.class)) {
                    value = resultSet.getFloat(attribute.name());
                }

                // BigDecimal
                if (parameterType.equals(BigDecimal.class)) {
                    value = resultSet.getBigDecimal(attribute.name());
                }

                // String
                if (parameterType.equals(String.class)) {
                    String str = resultSet.getString(attribute.name());

                    if (str != null) {
                        value = str.trim();
                    }
                }

                // byte[]
                if (parameterType.equals(byte[].class)) {
                    Blob blob = resultSet.getBlob(attribute.name());

                    if (blob != null) {
                        int blobLength = (int) blob.length();
                        byte[] blobAsBytes = blob.getBytes(1, blobLength);

                        value = blobAsBytes;
                        blob.free();
                    }
                }

                // Default
                if (value == null) {
                    value = resultSet.getObject(attribute.name());

                    if (value != null && value instanceof Clob) {
                        Clob clob = (Clob) value;
                        value = clobStringConversion(clob);
                    }
                }

                method.invoke(e, value);

            }
        } catch (IllegalAccessException | InstantiationException | RuntimeException | InvocationTargetException | SQLException ex) {
            throw new RuntimeException(ex);
        }

        return e;
    }

    /**
     * Creates the insert SQL.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @param named the named
     * @return the string
     */
    public static <E> String createInsertSQL(Class<E> clazz, boolean named) {

        return InsertQuerySQLBuilder.createInsertSQL(clazz, named);
    }

    /**
     * Creates the update SQL.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @param named the named
     * @return the string
     */
    public static <E> String createUpdateSQL(Class<E> clazz, boolean named) {

        return UpdateQuerySQLBuilder.createUpdateSQL(clazz, named);
    }

    /**
     * Creates the delete SQL.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @param named the named
     * @return the string
     */
    public static <E> String createDeleteSQL(Class<E> clazz, boolean named) {

        return DeleteQuerySQLBuilder.createDeleteSQL(clazz, named);
    }

    /**
     * Creates the select all SQL.
     *
     * @param <E>   the element type
     * @param clazz the clazz
     * @return the string
     */
    public static <E> String createSelectAllSQL(Class<E> clazz) {

        return SelectQuerySQLBuilder.createSelectAllSQL(clazz);
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

        return SelectQuerySQLBuilder.createSelectByKeySQL(clazz, named);
    }

    /**
     * Clob string conversion.
     *
     * @param clob the clob
     * @return the string
     * @throws RuntimeException the runtime exception
     */
    private static String clobStringConversion(Clob clob) throws RuntimeException {

        StringBuilder str = new StringBuilder();

        try {
            if (clob == null) {
                return "";
            }

            String strng;
            BufferedReader bufferRead = new BufferedReader(clob.getCharacterStream());

            while ((strng = bufferRead.readLine()) != null) {
                str.append(strng);
            }
        } catch (IOException | SQLException ex) {
            throw new RuntimeException(ex);
        }
        return str.toString();
    }
}

