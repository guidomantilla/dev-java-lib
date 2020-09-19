package dev.feather.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface Entity.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {

    /**
     * Name.
     *
     * @return the string
     */
    String name();

    /**
     * Label.
     *
     * @return the string
     */
    String label() default "";

}
