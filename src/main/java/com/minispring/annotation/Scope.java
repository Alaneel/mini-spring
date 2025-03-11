package com.minispring.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When used as a type-level annotation in conjunction with
 * {@link Component}, indicates the scope of a component.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    /**
     * Specifies the scope to use for the annotated component/bean.
     * @return the specified scope
     */
    String value() default "singleton";
}