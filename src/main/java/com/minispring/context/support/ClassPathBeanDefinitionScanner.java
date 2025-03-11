package com.minispring.context.support;

import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minispring.annotation.Component;
import com.minispring.annotation.Scope;
import com.minispring.beans.factory.BeanDefinition;
import com.minispring.beans.factory.DefaultListableBeanFactory;
import com.minispring.core.ClassUtils;
import com.minispring.exception.BeansException;

/**
 * Scanner for component classes to be auto-registered as beans.
 */
public class ClassPathBeanDefinitionScanner {

    private static final Logger logger = LoggerFactory.getLogger(ClassPathBeanDefinitionScanner.class);

    private final DefaultListableBeanFactory beanFactory;

    public ClassPathBeanDefinitionScanner(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Scan packages for component classes and register them as beans.
     *
     * @param basePackages the packages to scan
     * @return the number of bean definitions that were registered
     */
    public int scan(String... basePackages) {
        int beanCount = 0;

        for (String basePackage : basePackages) {
            beanCount += doScan(basePackage);
        }

        return beanCount;
    }

    /**
     * Scan a package and register beans.
     *
     * @param basePackage the package to scan
     * @return the number of bean definitions registered
     */
    protected int doScan(String basePackage) {
        logger.info("Scanning package: {}", basePackage);

        int beanCount = 0;
        Reflections reflections = new Reflections(basePackage);

        // Find all classes annotated with @Component
        Set<Class<?>> componentClasses = reflections.getTypesAnnotatedWith(Component.class);

        for (Class<?> componentClass : componentClasses) {
            String beanName = determineBeanName(componentClass);
            BeanDefinition beanDefinition = createBeanDefinition(componentClass);

            try {
                // Register the bean definition
                beanFactory.registerBeanDefinition(beanName, beanDefinition);
                beanCount++;
                logger.info("Registered bean: {} of type {}", beanName, componentClass.getName());
            } catch (BeansException e) {
                logger.error("Failed to register bean: {} of type {}", beanName, componentClass.getName(), e);
            }
        }

        return beanCount;
    }

    /**
     * Create a bean definition for the given class.
     *
     * @param beanClass the class of the bean
     * @return the bean definition
     */
    protected BeanDefinition createBeanDefinition(Class<?> beanClass) {
        BeanDefinition beanDefinition = new BeanDefinition(beanClass);

        // Handle scope
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (scope != null) {
            beanDefinition.setScope(scope.value());
        }

        return beanDefinition;
    }

    /**
     * Determine the bean name for the given bean class.
     *
     * @param beanClass the class of the bean
     * @return the bean name
     */
    protected String determineBeanName(Class<?> beanClass) {
        Component component = beanClass.getAnnotation(Component.class);
        String value = component.value();

        if (value.isEmpty()) {
            // Use the class name with first letter lowercase
            value = ClassUtils.lowerFirstLetter(ClassUtils.getSimpleName(beanClass));
        }

        return value;
    }
}