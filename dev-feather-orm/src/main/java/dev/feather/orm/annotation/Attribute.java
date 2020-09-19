package dev.feather.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface Attribute.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Attribute {

    /**
     * Name.
     *
     * @return the string
     */
    String name();

    /**
     * Key.
     *
     * @return true, if successful
     */
    boolean key() default false;

    /**
     * Label.
     *
     * @return the string
     */
    String label() default "";
}
