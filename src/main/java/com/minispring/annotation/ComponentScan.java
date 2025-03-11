package com.minispring.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configures component scanning directives for use with
 * {@link com.minispring.context.AnnotationConfigApplicationContext}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComponentScan {

    /**
     * Base packages to scan for annotated components.
     * @return base packages to scan
     */
    String[] basePackages() default {};
}